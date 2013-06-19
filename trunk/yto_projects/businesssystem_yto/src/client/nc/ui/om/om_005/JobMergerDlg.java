package nc.ui.om.om_005;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.generate.Gener;
import nc.bs.util.SleepTime;
import nc.bs.util.Uriread;
import nc.itf.hr.jf.ORGDelegator;
import nc.itf.yto.util.IFilePost;
import nc.itf.yto.util.IGener;
import nc.itf.yto.util.IReadmsg;
import nc.ui.pub.beans.*;
import nc.vo.yto.business.JobdocVO;
/**
 * 岗位合并对话框
 *
 * 创建日期：(2003-1-14 20:55:21)
 * @author：Administrator
 */
public class JobMergerDlg extends UIDialog implements ActionListener {
	
	private List<JobdocVO> JobdocUpdate = new ArrayList<JobdocVO>();
	private List<JobdocVO> JobdocUpdate2 = new ArrayList<JobdocVO>();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UIButton ivjUIBtnCancel = null;
	private UIButton ivjUIBtnOk = null;
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private UILabel ivjUILabDate = null;
	private UILabel ivjUILabMerge = null;
	private UILabel ivjUILabMerged = null;
	private UIPanel ivjUIPanel = null;
	private UIRefPane ivjUIRefPDate = null;
	private UIRefPane ivjUIRefPMerge = null;
	private UIRefPane ivjUIRefPMerged = null;
	//
	private String m_sMergedJobID;
	private nc.vo.pub.lang.UFDate m_buildDate;
/**
 * JobMergerDlg 构造子注解。
 * @param parent java.awt.Container
 */
public JobMergerDlg(
	java.awt.Container parent,
	String sMergedJobID,
	nc.vo.pub.lang.UFDate buildDate) {
	super(parent, nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000029")/*@res "岗位合并"*/);
	this.m_sMergedJobID = sMergedJobID;
	this.m_buildDate = buildDate;
	initialize();
}
/**
 * Invoked when an action occurs.
 */
public void actionPerformed(ActionEvent e) {
	if (e.getSource() == getUIBtnOk()) {
		if (executeJobMerger())
			closeOK();
	} else
		if (e.getSource() == getUIBtnCancel()) {
			closeCancel();
		}
}
/**
 * 校验
 *
 * 创建日期：(2003-1-15 13:56:19)
 * @return boolean
 */
private boolean checkVaild() {
	boolean bool = false;
	try {
		if (getUIRefPMerge().getRefPK() == null
			|| getUIRefPMerge().getRefPK().length() <= 0
			|| getUIRefPDate().getRefPK() == null
			|| getUIRefPDate().getRefPK().length() <= 0) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000030")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000031")/*@res "目标岗位和合并日期不能为空！"*/);
			bool = false;
		} else if (getAbortDate().compareTo(m_buildDate) < 0) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000030")/*@res "错误"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000032")/*@res "合并日期不能早于建立日期，请重新设置！"*/);
			bool = false;
		} else
			bool = true;
	} catch (Exception e) {
		reportException(e);
	}
	return bool;
}


/**
 * 执行岗位合并
 *
 * 创建日期：(2003-1-15 15:56:29)
 * @return boolean
 */
private boolean executeJobMerger() {
	try {
		if (checkVaild()) {
			try {
			    	boolean isInPhase = (MessageDialog.showYesNoDlg(this, getTitle(), nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000276")/*@res "是否同步岗位下人员工作履历？"*/) == nc.ui.pub.beans.UIDialog.ID_YES);
			    	ORGDelegator.getIJob().mergeJob(
					m_sMergedJobID, // 被合并岗位
					getTargetJobID(), //　目标岗位
					getAbortDate(),
					nc.ui.hr.global.Global.getUserID(),isInPhase);
			    	
			    	
			    	// 合并岗位时同步至中间表 add by river for 2011-09-14
			    	IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class.getName());
			    	IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
					IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
				
			    	JobdocVO[] jobvos = (JobdocVO[])msg.getGeneralVOs(JobdocVO.class, " pk_om_job in ('"+m_sMergedJobID+"' , '"+getTargetJobID()+"')");
			    	
					for(JobdocVO jobvo : jobvos) {
						String retStr = filepost.postFile(Uriread.uriPath() , 
								gener.generateXml5(jobvo, "RequestJob", "job", "update"));
						
						String[] strs = retStr.split("<success>");
						String retMsg = "";
						if (strs.length > 1)
							retMsg = strs[1].substring(0, strs[1].indexOf("<"));

						if (retMsg.equals("false") || strs.length <= 1) {
							JobdocUpdate.add(jobvo);
							
						}
					}
					
					if(JobdocUpdate.size() > 0) {
						new Thread() {
							public void run() {
								IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
								IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
							
								try {
									if(true) {
										this.sleep(SleepTime.Time);
									
										for(JobdocVO job : JobdocUpdate) {
											String retStr = filepost.postFile(Uriread.uriPath() , 
													gener.generateXml5(job, "RequestJob", "job", "add"));
											
											String[] strs = retStr.split("<success>");
											String retMsg = "";
											if (strs.length > 1)
												retMsg = strs[1].substring(0, strs[1].indexOf("<"));
											
											if(retMsg.equals("false") || strs.length <= 1)
												JobdocUpdate2.add(job);
										
										}
										
										JobdocUpdate = JobdocUpdate2;
										
										JobdocUpdate2 = new ArrayList<JobdocVO>();
										
//										if(JobdocUpdate.size() == 0)
//											break;
									}
									System.out.println("<<<<<<  岗位档案修改线程停止！ >>>>>>");
									System.out.println("<<<<<<  线程号: " + this.getId() + " >>>>>>");
									this.stop();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
					
				return true;
			} catch (Exception e) {
				reportException(e);
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000030")/*@res "错误"*/, e.getMessage());
			}
		}
	} catch (Exception e) {
		reportException(e);
	}
	return false;
}
/**
 * 获得合并日期
 *
 * 创建日期：(2003-1-15 15:08:09)
 * @return nc.vo.pub.lang.UFDate
 */
public nc.vo.pub.lang.UFDate getAbortDate() {
	return new nc.vo.pub.lang.UFDate(getUIRefPDate().getText());
}
/**
 * 获得目标岗位
 *
 * 创建日期：(2003-1-15 15:06:52)
 * @return java.lang.String
 */
public String getTargetJobID() {
	return getUIRefPMerge().getRefPK();
}
/**
 * 返回 UIBtnCancel 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnCancel() {
	if (ivjUIBtnCancel == null) {
		try {
			ivjUIBtnCancel = new nc.ui.pub.beans.UIButton();
			ivjUIBtnCancel.setName("UIBtnCancel");
			ivjUIBtnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "取消"*/);
			ivjUIBtnCancel.setLocation(242, 131);
			// user code begin {1}
			ivjUIBtnCancel.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnCancel;
}
/**
 * 返回 UIBtnOk 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnOk() {
	if (ivjUIBtnOk == null) {
		try {
			ivjUIBtnOk = new nc.ui.pub.beans.UIButton();
			ivjUIBtnOk.setName("UIBtnOk");
			ivjUIBtnOk.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000044")/*@res "确定"*/);
			ivjUIBtnOk.setLocation(86, 131);
			// user code begin {1}
			ivjUIBtnOk.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnOk;
}
/**
 * 返回 UIDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JPanel getUIDialogContentPane() {
	if (ivjUIDialogContentPane == null) {
		try {
			ivjUIDialogContentPane = new javax.swing.JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(null);
			getUIDialogContentPane().add(getUIPanel(), getUIPanel().getName());
			getUIDialogContentPane().add(getUIBtnOk(), getUIBtnOk().getName());
			getUIDialogContentPane().add(getUIBtnCancel(), getUIBtnCancel().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIDialogContentPane;
}
/**
 * 返回 UILabDate 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getUILabDate() {
	if (ivjUILabDate == null) {
		try {
			ivjUILabDate = new nc.ui.pub.beans.UILabel();
			ivjUILabDate.setName("UILabDate");
			ivjUILabDate.setPreferredSize(new java.awt.Dimension(80, 22));
			ivjUILabDate.setLayout(new BorderLayout());
			ivjUILabDate.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000033")/*@res "合并日期"*/);
			// user code begin {1}
			ivjUILabDate.setILabelType(5);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUILabDate;
}
/**
 * 返回 UILabMerge 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getUILabMerge() {
	if (ivjUILabMerge == null) {
		try {
			ivjUILabMerge = new nc.ui.pub.beans.UILabel();
			ivjUILabMerge.setName("UILabMerge");
			ivjUILabMerge.setPreferredSize(new java.awt.Dimension(80, 22));
			ivjUILabMerge.setLayout(new BorderLayout());
			ivjUILabMerge.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000034")/*@res "合并目标岗位"*/);
			// user code begin {1}
			ivjUILabMerge.setILabelType(5);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUILabMerge;
}
/**
 * 返回 UILabMerged 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getUILabMerged() {
	if (ivjUILabMerged == null) {
		try {
			ivjUILabMerged = new nc.ui.pub.beans.UILabel();
			ivjUILabMerged.setName("UILabMerged");
			ivjUILabMerged.setPreferredSize(new java.awt.Dimension(80, 22));
			ivjUILabMerged.setLayout(new BorderLayout());
			ivjUILabMerged.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000035")/*@res "被合并岗位"*/);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUILabMerged;
}
/**
 * 返回 UIPanel1 特性值。
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getUIPanel() {
	if (ivjUIPanel == null) {
		try {
			ivjUIPanel = new nc.ui.pub.beans.UIPanel();
			ivjUIPanel.setName("UIPanel");
			ivjUIPanel.setBounds(55, 23, 290, 96);
			getUIPanel().add(getUILabMerged(), getUILabMerged().getName());
			getUIPanel().add(getUIRefPMerged(), getUIRefPMerged().getName());
			getUIPanel().add(getUILabMerge(), getUILabMerge().getName());
			getUIPanel().add(getUIRefPMerge(), getUIRefPMerge().getName());
			getUIPanel().add(getUILabDate(), getUILabDate().getName());
			getUIPanel().add(getUIRefPDate(), getUIRefPDate().getName());
			// user code begin {1}
			ivjUIPanel.setBorder(new nc.ui.pub.beans.border.UITitledBorder(""));
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIPanel;
}
/**
 * 返回 UIRefPDate 特性值。
 * @return nc.ui.pub.beans.UIRefPane
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIRefPane getUIRefPDate() {
	if (ivjUIRefPDate == null) {
		try {
			ivjUIRefPDate = new nc.ui.pub.beans.UIRefPane();
			ivjUIRefPDate.setName("UIRefPDate");
			ivjUIRefPDate.setPreferredSize(new java.awt.Dimension(150, 22));
			ivjUIRefPDate.setEditable(false);
			ivjUIRefPDate.setRefNodeName("日历");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIRefPDate;
}
/**
 * 返回 UIRefPMerge 特性值。
 * @return nc.ui.pub.beans.UIRefPane
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIRefPane getUIRefPMerge() {
	if (ivjUIRefPMerge == null) {
		try {
			ivjUIRefPMerge = new nc.ui.pub.beans.UIRefPane();
			ivjUIRefPMerge.setName("UIRefPMerge");
			ivjUIRefPMerge.setPreferredSize(new java.awt.Dimension(150, 22));
			ivjUIRefPMerge.setEditable(false);
			// user code begin {1}
			ivjUIRefPMerge.setRefModel(
				new nc.ui.hi.ref.JobRef(nc.ui.hr.global.Global.getCorpPK(), m_sMergedJobID));
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIRefPMerge;
}
/**
 * 返回 UIRefPMerged 特性值。
 * @return nc.ui.pub.beans.UIRefPane
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIRefPane getUIRefPMerged() {
	if (ivjUIRefPMerged == null) {
		try {
			ivjUIRefPMerged = new nc.ui.pub.beans.UIRefPane();
			ivjUIRefPMerged.setName("UIRefPMerged");
			ivjUIRefPMerged.setPreferredSize(new java.awt.Dimension(150, 22));
			ivjUIRefPMerged.setEditable(false);
			ivjUIRefPMerged.setEnabled(false);
			// user code begin {1}
			ivjUIRefPMerged.setRefModel(new nc.ui.hi.ref.JobRef(nc.ui.hr.global.Global.getCorpPK(), null));
			ivjUIRefPMerged.getRefModel().reloadData();
			ivjUIRefPMerged.setPK(m_sMergedJobID);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}

	return ivjUIRefPMerged;
}
/**
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	// nc.bs.logging.Logger.error("--------- 未捕捉到的异常 ---------");
	// exception.printStackTrace(System.out);
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("JobMergerDlg");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(400, 189);
		//2005-5-11
		setResizable(false);
		setContentPane(getUIDialogContentPane());
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	getUIRefPDate().setPK(nc.ui.hr.global.Global.getLogDate().toString());
	getUIRefPDate().setText(nc.ui.hr.global.Global.getLogDate().toString());
	getUIRefPDate().getUITextField().setText(
		nc.ui.hr.global.Global.getLogDate().toString());
	// user code end
}
}
