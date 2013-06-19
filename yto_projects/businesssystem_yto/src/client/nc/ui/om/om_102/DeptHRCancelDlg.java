package nc.ui.om.om_102;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.generate.Gener;
import nc.bs.util.SleepTime;
import nc.bs.util.Uriread;
import nc.itf.hr.jf.IOrgInfo;
import nc.itf.hr.jf.ORGDelegator;
import nc.itf.uap.bd.dept.IDeptdocQry;
import nc.itf.yto.util.IFilePost;
import nc.itf.yto.util.IGener;
import nc.itf.yto.util.IReadmsg;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.bd.b04.DeptdocVO;
import nc.vo.om.om_101.DepthistoryVO;
import nc.vo.om.om_102.OMCommonValue;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

/**
 * 撤销部门对话框。 创建日期：(2004-12-20 13:31:31)
 * 
 * @author：王卫波
 */
public class DeptHRCancelDlg extends nc.ui.pub.beans.UIDialog implements
		ActionListener, ItemListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private nc.ui.pub.beans.UILabel ivjlbApproveDept = null;

	private nc.ui.pub.beans.UILabel ivjlbApproveNum = null;

	private nc.ui.pub.beans.UILabel ivjlbComment = null;

	private nc.ui.pub.beans.UILabel ivjlbName = null;

	private nc.ui.pub.beans.UIRefPane ivjrefEffectDate = null;

	private nc.ui.pub.beans.UIRefPane ivjrefName = null;

	private nc.ui.pub.beans.UITextField ivjtfApproveDept = null;

	private nc.ui.pub.beans.UITextField ivjtfApproveNum = null;

	private javax.swing.JPanel ivjUIDialogContentPane = null;

	private nc.ui.pub.beans.UIScrollPane ivjscpComment = null;

	private nc.ui.pub.beans.UITextArea ivjtaComment = null;

	private nc.ui.pub.beans.UILabel ivjlbEffectDate = null;

	private nc.ui.pub.beans.UIButton ivjbtnCancel = null;

	private nc.ui.pub.beans.UIButton ivjbtnOK = null;

	// add by lum
	private nc.ui.pub.beans.UICheckBox cbcanceled = null;

	private boolean canceled = false;

	DeptdocVO m_vDeptdocVO = null;

	/**
	 * DeptHRCanceldDlg 构造子注解。
	 */
	public DeptHRCancelDlg() {
		super();
		initialize();
	}

	/**
	 * DeptHRCanceldDlg 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 * @param title
	 *            java.lang.String
	 */
	public DeptHRCancelDlg(java.awt.Container parent, String title,
			DeptdocVO deptdocVO) {
		super(parent, title);
		initialize();
		// 设置部门参照
		getrefName().setPK(deptdocVO.getPk_deptdoc());
		// 暂存当前部门VO
		m_vDeptdocVO = deptdocVO;
	}
	
	private List<nc.vo.yto.business.DeptdocVO> DeptdocUpdate = new ArrayList<nc.vo.yto.business.DeptdocVO>();
	private List<nc.vo.yto.business.DeptdocVO> DeptdocUpdate2 = new ArrayList<nc.vo.yto.business.DeptdocVO>();
	

	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(java.awt.event.ActionEvent e) {
		try {
			if (e.getSource() == getbtnOK()) {
				if (!validateData())
					return;
				// add by lum
				String deptPK = m_vDeptdocVO.getPk_deptdoc();
				boolean isCanceled = ORGDelegator.getIDeptMerg().isDeptCancel(
						deptPK);
				if (!isCanceled) {
					if (isCanceled()) {
						ORGDelegator.getIOrgInfo().cancelOpera(m_vDeptdocVO);
					}
				}
				if (ORGDelegator.getIOrgInfo().deptChangeOperate(
						getCurDeptHVO(), m_vDeptdocVO, false)) {
					
//					 部门转移时同步部门信息至中间表 add by river for 2011-09-13
//					DeptdocVO deptdoc = m_vDeptdocVO;
					
					IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class.getName());
					IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
					IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
				
					nc.vo.yto.business.DeptdocVO deptdocvo = ((nc.vo.yto.business.DeptdocVO[])msg.getGeneralVOs(nc.vo.yto.business.DeptdocVO.class, " pk_deptdoc = '"+m_vDeptdocVO.getAttributeValue("pk_deptdoc")+"'"))[0];
					
					deptdocvo.setAttributeValue("hrcanceled", new UFBoolean("Y"));
					String retStr = filepost.postFile(Uriread.uriPath() , 
							gener.generateXml3(deptdocvo, "RequestDeptdoc", "dept", "update"));
					
					String[] strs = retStr.split("<success>");
					String retMsg = "";
					if (strs.length > 1)
						retMsg = strs[1].substring(0, strs[1].indexOf("<"));

					if (retMsg.equals("false")) {
						DeptdocUpdate.add(deptdocvo);
						new Thread() {
							public void run() {
								IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
								IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
							
								try {
									if(true) {
										this.sleep(SleepTime.Time);
									
										for(nc.vo.yto.business.DeptdocVO dept : DeptdocUpdate) {
											String retStr = filepost.postFile(Uriread.uriPath() , 
													gener.generateXml3(dept, "RequestDeptdoc", "dept", "add"));
											
											String[] strs = retStr.split("<success>");
											String retMsg = "";
											if (strs.length > 1)
												retMsg = strs[1].substring(0, strs[1].indexOf("<"));
											
											if(retMsg.equals("false") || strs.length <= 1)
												DeptdocUpdate2.add(dept);
										
										}
										
										DeptdocUpdate = DeptdocUpdate2;
										
										DeptdocUpdate2 = new ArrayList<nc.vo.yto.business.DeptdocVO>();
										
//										if(DeptdocUpdate.size() == 0)
//											break;
									}
									System.out.println("<<<<<<  部门档案修改线程停止！ >>>>>>");
									System.out.println("<<<<<<  线程号: " + this.getId() + " >>>>>>");
									this.stop();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
					
					closeOK();
					
				}
			} else if (e.getSource() == getbtnCancel()) {
				closeCancel();
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}

	/**
	 * 返回 btnCancel 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {
			try {
				ivjbtnCancel = new nc.ui.pub.beans.UIButton();
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("common", "UC001-0000008")/* @res "取消" */);
				// ivjbtnCancel.setBounds(242, 210, 75, 22);
				ivjbtnCancel.setBounds(242, 250, 75, 22);
				// user code begin {1}
				ivjbtnCancel.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnCancel;
	}

	/**
	 * 返回 btnOK 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getbtnOK() {
		if (ivjbtnOK == null) {
			try {
				ivjbtnOK = new nc.ui.pub.beans.UIButton();
				ivjbtnOK.setName("btnOK");
				ivjbtnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"common", "UC001-0000044")/* @res "确定" */);
				// ivjbtnOK.setBounds(84, 210, 75, 22);
				ivjbtnOK.setBounds(84, 250, 75, 22);
				// user code begin {1}
				ivjbtnOK.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnOK;
	}

	/**
	 * 获得当前DepthistoryVO。 创建日期：(2004-12-27 15:39:29)
	 */
	private DepthistoryVO getCurDeptHVO() {
		DepthistoryVO m_vDeptHVO = new DepthistoryVO();
		// 部门主键
		m_vDeptHVO.setPk_deptdoc(m_vDeptdocVO.getPk_deptdoc());
		// 变更类别
		m_vDeptHVO.setChangetype(new Integer(OMCommonValue.ChangeType_CANCEL));
		// 部门编码
		m_vDeptHVO.setDeptcode(m_vDeptdocVO.getDeptcode());
		// 部门级别
		m_vDeptHVO.setDeptlevel(m_vDeptdocVO.getDeptlevel());
		// 部门名称
		m_vDeptHVO.setDeptname(m_vDeptdocVO.getDeptname());
		// 上级部门
		m_vDeptHVO.setPk_fathedept(m_vDeptdocVO.getPk_fathedept());
		// 是否接收部门
		m_vDeptHVO.setIsreceived(new nc.vo.pub.lang.UFBoolean('N'));
		// 部门负责人
		m_vDeptHVO.setPk_psndoc(m_vDeptdocVO.getPk_psndoc());
		// 批准文号
		m_vDeptHVO.setApprovenum(gettfApproveNum().getText());
		// 批准单位
		m_vDeptHVO.setApprovedept(gettfApproveDept().getText());
		// 生效日期
		m_vDeptHVO.setEffectdate(new UFDate(getrefEffectDate().getText()));
		// 备注
		m_vDeptHVO.setMemos(gettaComment().getText());
		return m_vDeptHVO;
	}

	/**
	 * 返回 lbApproveDept 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getlbApproveDept() {
		if (ivjlbApproveDept == null) {
			try {
				ivjlbApproveDept = new nc.ui.pub.beans.UILabel();
				ivjlbApproveDept.setName("lbApproveDept");
				ivjlbApproveDept
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050404", "UPP60050404-000012")/*
																	 * @res
																	 * "批准单位："
																	 */);
				ivjlbApproveDept.setBounds(64, 73, 71, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlbApproveDept;
	}

	/**
	 * 返回 lbApproveNum 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getlbApproveNum() {
		if (ivjlbApproveNum == null) {
			try {
				ivjlbApproveNum = new nc.ui.pub.beans.UILabel();
				ivjlbApproveNum.setName("lbApproveNum");
				ivjlbApproveNum
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050404", "UPP60050404-000013")/*
																	 * @res
																	 * "批准文号："
																	 */);
				ivjlbApproveNum.setBounds(64, 42, 71, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlbApproveNum;
	}

	/**
	 * 返回 lbComment 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getlbComment() {
		if (ivjlbComment == null) {
			try {
				ivjlbComment = new nc.ui.pub.beans.UILabel();
				ivjlbComment.setName("lbComment");
				ivjlbComment
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050404", "UPP60050404-000014")/*
																	 * @res
																	 * "备注："
																	 */);
				// ivjlbComment.setBounds(64, 134, 71, 22);
				ivjlbComment.setBounds(64, 164, 71, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlbComment;
	}

	/**
	 * 返回 lbeEffectDate 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getlbEffectDate() {
		if (ivjlbEffectDate == null) {
			try {
				ivjlbEffectDate = new nc.ui.pub.beans.UILabel();
				ivjlbEffectDate.setName("lbEffectDate");
				ivjlbEffectDate
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050404", "UPP60050404-000015")/*
																	 * @res
																	 * "生效日期："
																	 */);
				ivjlbEffectDate.setBounds(64, 103, 122, 22);
				ivjlbEffectDate.setILabelType(5/** 必输框 */
				);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlbEffectDate;
	}

	/**
	 * 返回 lbName 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getlbName() {
		if (ivjlbName == null) {
			try {
				ivjlbName = new nc.ui.pub.beans.UILabel();
				ivjlbName.setName("lbName");
				ivjlbName.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"60050404", "UPP60050404-000016")/* @res "部门名称：" */);
				ivjlbName.setBounds(64, 13, 71, 22);
				ivjlbName.setILabelType(5/** 必输框 */
				);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlbName;
	}

	/**
	 * 返回 refEffectDate 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRefPane getrefEffectDate() {
		if (ivjrefEffectDate == null) {
			try {
				ivjrefEffectDate = new nc.ui.pub.beans.UIRefPane();
				ivjrefEffectDate.setName("refEffectDate");
				ivjrefEffectDate.setBounds(171, 103, 122, 22);
				ivjrefEffectDate.setRefNodeName("日历");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjrefEffectDate;
	}

	/**
	 * 返回 refName 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRefPane getrefName() {
		if (ivjrefName == null) {
			try {
				ivjrefName = new nc.ui.pub.beans.UIRefPane();
				ivjrefName.setName("refName");
				ivjrefName.setBounds(171, 13, 122, 22);
				ivjrefName.setRefNodeName("部门档案");
				ivjrefName.setEnabled(false);
				ivjrefName.setEditable(false);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjrefName;
	}

	/**
	 * 返回 scpComment 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getscpComment() {
		if (ivjscpComment == null) {
			try {
				ivjscpComment = new nc.ui.pub.beans.UIScrollPane();
				ivjscpComment.setName("scpComment");
				// ivjscpComment.setBounds(171, 134, 188, 73);
				ivjscpComment.setBounds(171, 164, 188, 73);
				getscpComment().setViewportView(gettaComment());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjscpComment;
	}

	/**
	 * 返回 UITextArea1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITextArea
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UITextArea gettaComment() {
		if (ivjtaComment == null) {
			try {
				ivjtaComment = new nc.ui.pub.beans.UITextArea();
				ivjtaComment.setName("taComment");
				ivjtaComment.setBounds(0, 0, 188, 73);
				ivjtaComment.setMaxLength(100);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtaComment;
	}

	/**
	 * 返回 tfApproveDept 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UITextField gettfApproveDept() {
		if (ivjtfApproveDept == null) {
			try {
				ivjtfApproveDept = new nc.ui.pub.beans.UITextField();
				ivjtfApproveDept.setName("tfApproveDept");
				ivjtfApproveDept.setBounds(171, 73, 122, 20);
				ivjtfApproveDept.setMaxLength(256);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtfApproveDept;
	}

	/**
	 * 返回 tfApproveNum 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UITextField gettfApproveNum() {
		if (ivjtfApproveNum == null) {
			try {
				ivjtfApproveNum = new nc.ui.pub.beans.UITextField();
				ivjtfApproveNum.setName("tfApproveNum");
				ivjtfApproveNum.setBounds(171, 42, 122, 20);
				ivjtfApproveNum.setMaxLength(50);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtfApproveNum;
	}

	// add by
	// lum****************************************************************

	/**
	 * 返回 cbcancled 特性值。
	 * 
	 * @return nc.ui.pub.beans.UICheckBox
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UICheckBox gethrcancled() {
		if (cbcanceled == null) {
			try {
				cbcanceled = new nc.ui.pub.beans.UICheckBox();
				cbcanceled.setName("UICheckBox_cancled");
				cbcanceled.setText(NCLangRes.getInstance().getStrByID(
						"60050404", "UPP60050404-000158")/* 是否封存 */);
				cbcanceled.setBounds(64, 134, 71, 22);
				cbcanceled.addItemListener(this);
				// hrcancled.setBounds(171, 42, 122, 20);
				// hrcancled.setMaxLength(50);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return cbcanceled;
	}

	public void itemStateChanged(java.awt.event.ItemEvent e) {
		try {
			if (e.getStateChange() == ItemEvent.SELECTED)
				// canceled = true;
				setCanceled(true);
			else if (e.getStateChange() == ItemEvent.DESELECTED)
				// canceled = false;
				setCanceled(false);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}

	// **********************************************************************

	/**
	 * 返回 UIDialogContentPane 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(null);
				getUIDialogContentPane()
						.add(getlbName(), getlbName().getName());
				getUIDialogContentPane().add(getlbApproveNum(),
						getlbApproveNum().getName());
				getUIDialogContentPane().add(getlbApproveDept(),
						getlbApproveDept().getName());
				getUIDialogContentPane().add(getlbEffectDate(),
						getlbEffectDate().getName());
				getUIDialogContentPane().add(getlbComment(),
						getlbComment().getName());
				getUIDialogContentPane().add(getrefName(),
						getrefName().getName());
				getUIDialogContentPane().add(gettfApproveNum(),
						gettfApproveNum().getName());
				getUIDialogContentPane().add(gettfApproveDept(),
						gettfApproveDept().getName());
				getUIDialogContentPane().add(getrefEffectDate(),
						getrefEffectDate().getName());

				// add by lum
				getUIDialogContentPane().add(gethrcancled(),
						gethrcancled().getName());

				getUIDialogContentPane().add(getscpComment(),
						getscpComment().getName());
				getUIDialogContentPane().add(getbtnOK(), getbtnOK().getName());
				getUIDialogContentPane().add(getbtnCancel(),
						getbtnCancel().getName());
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
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
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
			setName("DeptHRCanceldDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(450, 360);
			setContentPane(getUIDialogContentPane());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			DeptHRCancelDlg aDeptHRCancelDlg;
			aDeptHRCancelDlg = new DeptHRCancelDlg();
			aDeptHRCancelDlg.setModal(true);
			aDeptHRCancelDlg
					.addWindowListener(new java.awt.event.WindowAdapter() {
						public void windowClosing(java.awt.event.WindowEvent e) {
							System.exit(0);
						};
					});
			aDeptHRCancelDlg.show();
			java.awt.Insets insets = aDeptHRCancelDlg.getInsets();
			aDeptHRCancelDlg.setSize(aDeptHRCancelDlg.getWidth() + insets.left
					+ insets.right, aDeptHRCancelDlg.getHeight() + insets.top
					+ insets.bottom);
			aDeptHRCancelDlg.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("nc.ui.pub.beans.UIDialog 的 main() 中发生异常");
			exception.printStackTrace(System.out);
		}
	}

	/**
	 * 校验数据的合法性。 创建日期：(2004-12-29 11:16:06)
	 */
	private boolean validateData() {
		// 部门名称非空校验
		if (getrefName().getRefPK() == null
				|| getrefName().getRefPK().trim().length() == 0) {
			MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404",
							"UPP60050404-000017")/* @res "提示" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404",
							"UPP60050404-000018")/* @res "部门名称不允许空！" */);
			getrefName().grabFocus();
			return false;
		}
		// 生效日期非空校验
		if (getrefEffectDate().getRefPK() == null
				|| getrefEffectDate().getRefPK().trim().length() == 0) {
			MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404",
							"UPP60050404-000017")/* @res "提示" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404",
							"UPP60050404-000019")/* @res "生效日期不允许空！" */);
			getrefEffectDate().grabFocus();
			return false;
		}

		// 生效日期不可以早于部门成立日期
		UFDate effectDate = UFDate.getDate(getrefEffectDate().getText());
		if (effectDate.before(m_vDeptdocVO.getCreateDate())) {
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID(
					"60050404", "UPP60050404-000017")/* @res "提示" */,
			// UPP60050404-000159=生效日期不可早于成立日期！
					NCLangRes.getInstance().getStrByID("60050404",
							"UPP60050404-000159"));
			getrefEffectDate().grabFocus();
			return false;
		}

		// add by zhyan v50
		if (getrefEffectDate().getRefPK() != null) {
			try {
				IOrgInfo orginfo = ((IOrgInfo) NCLocator.getInstance().lookup(
						IOrgInfo.class.getName()));
				UFDate uncncldate = orginfo.getDate(m_vDeptdocVO
						.getPk_deptdoc(), OMCommonValue.ChangeType_UNCANCEL);
				UFDate effectdate = UFDate
						.getDate(getrefEffectDate().getText());
				UFDate creatdate = m_vDeptdocVO.getCreateDate();

				if (uncncldate != null && effectdate.before(uncncldate)) {
					MessageDialog
							.showHintDlg(
									this,
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("60050404",
													"UPP60050404-000017")/*
																			 * @res
																			 * "提示"
																			 */,
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("60050404",
													"UPP60050404-000147")/*
																			 * @res
																			 * "生效日期不得早于反撤销日期！"
																			 */);
					return false;
				}
				if (effectdate.before(creatdate)) {
					MessageDialog
							.showHintDlg(
									this,
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("60050404",
													"UPP60050404-000017")/*
																			 * @res
																			 * "提示"
																			 */,
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("60050404",
													"UPP60050404-000145")/*
																			 * @res
																			 * "生效日期不得早于建立日期！"
																			 */);
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// add by lum
		if (isCanceled()) {
			try {
				nc.itf.uap.sf.ICreateCorpQueryService createCorpQueryService = null;
				createCorpQueryService = (nc.itf.uap.sf.ICreateCorpQueryService) NCLocator
						.getInstance().lookup(
								nc.itf.uap.sf.ICreateCorpQueryService.class
										.getName());
				boolean instalmodel = createCorpQueryService.isEnabled(
						ClientEnvironment.getInstance().getCorporation()
								.getPk_corp(), "HI");
				boolean subdepnotcanceled = false;
				if (instalmodel) {
					IDeptdocQry ideptdocqry = ((IDeptdocQry) NCLocator
							.getInstance().lookup(IDeptdocQry.class.getName()));
					nc.vo.bd.b04.DeptdocVO[] deptdocVOs = ideptdocqry
							.queryAllchildernDeptdoc(m_vDeptdocVO
									.getPk_deptdoc());
					for (int i = 0; i < deptdocVOs.length; i++) {
						if (!deptdocVOs[i].getCanceled().booleanValue())
							subdepnotcanceled = true;
					}
					if (subdepnotcanceled) {// 如果不能封存
						MessageDialog
								.showHintDlg(
										this,
										nc.ui.ml.NCLangRes.getInstance()
												.getStrByID("60050404",
														"UPP60050404-000017")/*
																				 * @res
																				 * "提示"
																				 */,
										nc.ui.ml.NCLangRes.getInstance()
												.getStrByID("60050404",
														"UPP60050404-000143")/*
																				 * @res
																				 * "有未封存的下级部门，本部门不能封存"
																				 */); // 5.0是
																											// '是,否对话框'
						return false;
					}

					nc.itf.hr.jf.IDeptMerg ideptmerg = (nc.itf.hr.jf.IDeptMerg) NCLocator
							.getInstance().lookup(
									nc.itf.hr.jf.IDeptMerg.class.getName());
					ideptmerg.checkDeptCanceled(m_vDeptdocVO.getPk_deptdoc());
					m_vDeptdocVO
							.setCanceled(new nc.vo.pub.lang.UFBoolean(true));
					m_vDeptdocVO.setCancelDate(new UFDate(getrefEffectDate()
							.getText()));
				} else {
					IDeptdocQry ideptdocqry = ((IDeptdocQry) NCLocator
							.getInstance().lookup(IDeptdocQry.class.getName()));
					nc.vo.bd.b04.DeptdocVO[] deptdocVOs = ideptdocqry
							.queryAllchildernDeptdoc(m_vDeptdocVO
									.getPk_deptdoc());
					for (int i = 0; i < deptdocVOs.length; i++) {
						if (!deptdocVOs[i].getCanceled().booleanValue())
							subdepnotcanceled = true;
					}
					if (subdepnotcanceled) {// 如果不能封存
					// 3.5是'提示对话框'MessageDialog.showHintDlg(this,
					// nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404","UPP60050404-000017")/*@res
					// "提示"*/,
					// nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404","UPP60050404-000019")/*@res
					// "生效日期不允许空！"*/);
						MessageDialog
								.showHintDlg(
										this,
										nc.ui.ml.NCLangRes.getInstance()
												.getStrByID("60050404",
														"UPP60050404-000017")/*
																				 * @res
																				 * "提示"
																				 */,
										nc.ui.ml.NCLangRes.getInstance()
												.getStrByID("60050404",
														"UPP60050404-000143")/*
																				 * @res
																				 * "有未封存的下级部门，本部门不能封存"
																				 */); // 5.0是
																											// '是,否对话框'
						return false;
					} else {
						m_vDeptdocVO.setCanceled(new nc.vo.pub.lang.UFBoolean(
								true));
						m_vDeptdocVO.setCancelDate(new UFDate(
								getrefEffectDate().getText()));
					}
				}
			} catch (Exception e) {
				MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("60050404",
								"UPP60050404-000017")/* @res "提示" */, e
						.getCause().getMessage());
				e.printStackTrace();
				return false;
			}

		}

		return true;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
}
