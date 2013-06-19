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
 * ��λ�ϲ��Ի���
 *
 * �������ڣ�(2003-1-14 20:55:21)
 * @author��Administrator
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
 * JobMergerDlg ������ע�⡣
 * @param parent java.awt.Container
 */
public JobMergerDlg(
	java.awt.Container parent,
	String sMergedJobID,
	nc.vo.pub.lang.UFDate buildDate) {
	super(parent, nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000029")/*@res "��λ�ϲ�"*/);
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
 * У��
 *
 * �������ڣ�(2003-1-15 13:56:19)
 * @return boolean
 */
private boolean checkVaild() {
	boolean bool = false;
	try {
		if (getUIRefPMerge().getRefPK() == null
			|| getUIRefPMerge().getRefPK().length() <= 0
			|| getUIRefPDate().getRefPK() == null
			|| getUIRefPDate().getRefPK().length() <= 0) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000030")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000031")/*@res "Ŀ���λ�ͺϲ����ڲ���Ϊ�գ�"*/);
			bool = false;
		} else if (getAbortDate().compareTo(m_buildDate) < 0) {
			MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000030")/*@res "����"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000032")/*@res "�ϲ����ڲ������ڽ������ڣ����������ã�"*/);
			bool = false;
		} else
			bool = true;
	} catch (Exception e) {
		reportException(e);
	}
	return bool;
}


/**
 * ִ�и�λ�ϲ�
 *
 * �������ڣ�(2003-1-15 15:56:29)
 * @return boolean
 */
private boolean executeJobMerger() {
	try {
		if (checkVaild()) {
			try {
			    	boolean isInPhase = (MessageDialog.showYesNoDlg(this, getTitle(), nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000276")/*@res "�Ƿ�ͬ����λ����Ա����������"*/) == nc.ui.pub.beans.UIDialog.ID_YES);
			    	ORGDelegator.getIJob().mergeJob(
					m_sMergedJobID, // ���ϲ���λ
					getTargetJobID(), //��Ŀ���λ
					getAbortDate(),
					nc.ui.hr.global.Global.getUserID(),isInPhase);
			    	
			    	
			    	// �ϲ���λʱͬ�����м�� add by river for 2011-09-14
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
									System.out.println("<<<<<<  ��λ�����޸��߳�ֹͣ�� >>>>>>");
									System.out.println("<<<<<<  �̺߳�: " + this.getId() + " >>>>>>");
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
				MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000030")/*@res "����"*/, e.getMessage());
			}
		}
	} catch (Exception e) {
		reportException(e);
	}
	return false;
}
/**
 * ��úϲ�����
 *
 * �������ڣ�(2003-1-15 15:08:09)
 * @return nc.vo.pub.lang.UFDate
 */
public nc.vo.pub.lang.UFDate getAbortDate() {
	return new nc.vo.pub.lang.UFDate(getUIRefPDate().getText());
}
/**
 * ���Ŀ���λ
 *
 * �������ڣ�(2003-1-15 15:06:52)
 * @return java.lang.String
 */
public String getTargetJobID() {
	return getUIRefPMerge().getRefPK();
}
/**
 * ���� UIBtnCancel ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getUIBtnCancel() {
	if (ivjUIBtnCancel == null) {
		try {
			ivjUIBtnCancel = new nc.ui.pub.beans.UIButton();
			ivjUIBtnCancel.setName("UIBtnCancel");
			ivjUIBtnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "ȡ��"*/);
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
 * ���� UIBtnOk ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getUIBtnOk() {
	if (ivjUIBtnOk == null) {
		try {
			ivjUIBtnOk = new nc.ui.pub.beans.UIButton();
			ivjUIBtnOk.setName("UIBtnOk");
			ivjUIBtnOk.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000044")/*@res "ȷ��"*/);
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
 * ���� UIDialogContentPane ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
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
 * ���� UILabDate ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getUILabDate() {
	if (ivjUILabDate == null) {
		try {
			ivjUILabDate = new nc.ui.pub.beans.UILabel();
			ivjUILabDate.setName("UILabDate");
			ivjUILabDate.setPreferredSize(new java.awt.Dimension(80, 22));
			ivjUILabDate.setLayout(new BorderLayout());
			ivjUILabDate.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000033")/*@res "�ϲ�����"*/);
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
 * ���� UILabMerge ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getUILabMerge() {
	if (ivjUILabMerge == null) {
		try {
			ivjUILabMerge = new nc.ui.pub.beans.UILabel();
			ivjUILabMerge.setName("UILabMerge");
			ivjUILabMerge.setPreferredSize(new java.awt.Dimension(80, 22));
			ivjUILabMerge.setLayout(new BorderLayout());
			ivjUILabMerge.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000034")/*@res "�ϲ�Ŀ���λ"*/);
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
 * ���� UILabMerged ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getUILabMerged() {
	if (ivjUILabMerged == null) {
		try {
			ivjUILabMerged = new nc.ui.pub.beans.UILabel();
			ivjUILabMerged.setName("UILabMerged");
			ivjUILabMerged.setPreferredSize(new java.awt.Dimension(80, 22));
			ivjUILabMerged.setLayout(new BorderLayout());
			ivjUILabMerged.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("60050704","UPP60050704-000035")/*@res "���ϲ���λ"*/);
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
 * ���� UIPanel1 ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
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
 * ���� UIRefPDate ����ֵ��
 * @return nc.ui.pub.beans.UIRefPane
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIRefPane getUIRefPDate() {
	if (ivjUIRefPDate == null) {
		try {
			ivjUIRefPDate = new nc.ui.pub.beans.UIRefPane();
			ivjUIRefPDate.setName("UIRefPDate");
			ivjUIRefPDate.setPreferredSize(new java.awt.Dimension(150, 22));
			ivjUIRefPDate.setEditable(false);
			ivjUIRefPDate.setRefNodeName("����");
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
 * ���� UIRefPMerge ����ֵ��
 * @return nc.ui.pub.beans.UIRefPane
 */
/* ���棺�˷������������ɡ� */
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
 * ���� UIRefPMerged ����ֵ��
 * @return nc.ui.pub.beans.UIRefPane
 */
/* ���棺�˷������������ɡ� */
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
 * ÿ�������׳��쳣ʱ������
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
	// nc.bs.logging.Logger.error("--------- δ��׽�����쳣 ---------");
	// exception.printStackTrace(System.out);
}
/**
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
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
