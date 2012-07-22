package nc.ui.om.om_102;

import java.awt.event.ActionListener;
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
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.bd.b04.DeptdocVO;
import nc.vo.om.om_101.DepthistoryVO;
import nc.vo.om.om_102.OMCommonValue;
import nc.vo.pub.lang.UFDate;

/**
 * 变更部门对话框。 创建日期：(2004-12-20 13:31:31)
 * 
 * @author：王卫波
 */
public class DeptRenameDlg extends nc.ui.pub.beans.UIDialog implements
		ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private nc.ui.pub.beans.UILabel ivjlbApproveDept = null;

	private nc.ui.pub.beans.UILabel ivjlbApproveNum = null;

	private nc.ui.pub.beans.UILabel ivjlbComment = null;

	private nc.ui.pub.beans.UIRefPane ivjrefEffectDate = null;

	private nc.ui.pub.beans.UITextField ivjtfApproveDept = null;

	private nc.ui.pub.beans.UITextField ivjtfApproveNum = null;

	private javax.swing.JPanel ivjUIDialogContentPane = null;

	private nc.ui.pub.beans.UIScrollPane ivjscpComment = null;

	private nc.ui.pub.beans.UITextArea ivjtaComment = null;

	private nc.ui.pub.beans.UILabel ivjlbEffectDate = null;

	private nc.ui.pub.beans.UILabel ivjlbNewName = null;

	private nc.ui.pub.beans.UILabel ivjlbOldName = null;

	private nc.ui.pub.beans.UIRefPane ivjrefOldName = null;

	private nc.ui.pub.beans.UIButton ivjbtnCancel = null;

	private nc.ui.pub.beans.UIButton ivjbtnOK = null;

	DeptdocVO m_vDeptdocVO = null;

	private nc.ui.pub.beans.UITextField ivjtfNewName = null;

	/**
	 * DeptHRCanceldDlg 构造子注解。
	 */
	public DeptRenameDlg() {
		super();
		initialize();
	}

	/**
	 * DeptHRCanceldDlg 构造子注解。
	 */
	public DeptRenameDlg(java.awt.Container parent, String title,
			DeptdocVO deptdocVO) {
		super(parent, title);
		initialize();
		// 设置部门参照
		getrefOldName().setPK(deptdocVO.getPk_deptdoc());
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

				// int res = MessageDialog.showYesNoCancelDlg(this, "确认信息",
				// "是否同步变更工作履历记录？");
				int res = MessageDialog
						.showYesNoCancelDlg(
								this,
								NCLangRes.getInstance().getStrByID("60050404",
										"UPP60050404-000163")/* @res "确认信息" */,
								NCLangRes.getInstance().getStrByID("60050404",
										"UPP60050404-000163")/*
																 * @res
																 * "是否同步新增工作履历记录？"
																 */);
				if (res == MessageDialog.ID_YES) {
					ORGDelegator.getIOrgInfo().renameDept(getCurDeptHVO(),
							m_vDeptdocVO, true);
				} else if (res == MessageDialog.ID_NO) {
					ORGDelegator.getIOrgInfo().renameDept(getCurDeptHVO(),
							m_vDeptdocVO, false);
				} else {
					return;
				}
				
				// 部门名称变更时同步部门信息至中间表 add by river for 2011-09-13
//				DeptdocVO deptdoc = m_vDeptdocVO;
				
				IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class.getName());
				IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
				IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
			
				nc.vo.yto.business.DeptdocVO deptdocvo = ((nc.vo.yto.business.DeptdocVO[])msg.getGeneralVOs(nc.vo.yto.business.DeptdocVO.class, " pk_deptdoc = '"+m_vDeptdocVO.getAttributeValue("pk_deptdoc")+"'"))[0];
					
				
				deptdocvo.setAttributeValue("deptname", getCurDeptHVO().getNewdeptname());
				String retStr = filepost.postFile(Uriread.uriPath() , 
						gener.generateXml3(deptdocvo , "RequestDeptdoc", "dept", "update"));
				
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
									
//									if(DeptdocUpdate.size() == 0)
//										break;
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
			} else if (e.getSource() == getbtnCancel()) {
				closeCancel();
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404",
							"UPP60050404-000017")/* @res "提示" */, ex
							.getMessage());
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
				ivjbtnCancel.setBounds(244, 235, 64, 22);
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
				ivjbtnOK.setBounds(90, 235, 64, 22);
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
		m_vDeptHVO.setChangetype(new Integer(OMCommonValue.ChangeType_RENAME));
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
		// 新部门名称
		m_vDeptHVO.setNewdeptname(gettfNewName().getText());
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
				ivjlbApproveDept.setBounds(54, 96, 71, 22);
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
				ivjlbApproveNum.setBounds(54, 69, 71, 22);
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
				ivjlbComment.setBounds(54, 157, 71, 22);
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
				ivjlbEffectDate.setBounds(54, 126, 71, 22);
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
	private nc.ui.pub.beans.UILabel getlbNewName() {
		if (ivjlbNewName == null) {
			try {
				ivjlbNewName = new nc.ui.pub.beans.UILabel();
				ivjlbNewName.setName("lbNewName");
				ivjlbNewName
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050404", "UPP60050404-000089")/*
																	 * @res
																	 * "新部门名称："
																	 */);
				ivjlbNewName.setBounds(54, 40, 75, 22);
				ivjlbNewName.setILabelType(5/** 必输框 */
				);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlbNewName;
	}

	/**
	 * 返回 lbOldName 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getlbOldName() {
		if (ivjlbOldName == null) {
			try {
				ivjlbOldName = new nc.ui.pub.beans.UILabel();
				ivjlbOldName.setName("lbOldName");
				ivjlbOldName
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050404", "UPP60050404-000090")/*
																	 * @res
																	 * "原部门名称："
																	 */);
				ivjlbOldName.setBounds(54, 10, 75, 22);
				ivjlbOldName.setILabelType(5/** 必输框 */
				);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlbOldName;
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
				ivjrefEffectDate.setBounds(161, 126, 122, 20);
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
	 * 返回 refOldName 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRefPane getrefOldName() {
		if (ivjrefOldName == null) {
			try {
				ivjrefOldName = new nc.ui.pub.beans.UIRefPane();
				ivjrefOldName.setName("refOldName");
				ivjrefOldName.setBounds(161, 10, 122, 20);
				ivjrefOldName.setRefNodeName("部门档案");
				ivjrefOldName.setEditable(false);
				ivjrefOldName.setEnabled(false);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjrefOldName;
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
				ivjscpComment.setBounds(161, 157, 188, 73);
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
				ivjtfApproveDept.setBounds(161, 96, 122, 20);
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
				ivjtfApproveNum.setBounds(161, 69, 122, 20);
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

	/**
	 * 返回 tfNewName 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UITextField gettfNewName() {
		if (ivjtfNewName == null) {
			try {
				ivjtfNewName = new nc.ui.pub.beans.UITextField();
				ivjtfNewName.setName("tfNewName");
				ivjtfNewName.setBounds(161, 40, 122, 20);
				ivjtfNewName.setMaxLength(50);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtfNewName;
	}

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
				getUIDialogContentPane().add(getlbNewName(),
						getlbNewName().getName());
				getUIDialogContentPane().add(getlbApproveNum(),
						getlbApproveNum().getName());
				getUIDialogContentPane().add(getlbApproveDept(),
						getlbApproveDept().getName());
				getUIDialogContentPane().add(getlbEffectDate(),
						getlbEffectDate().getName());
				getUIDialogContentPane().add(getlbComment(),
						getlbComment().getName());
				getUIDialogContentPane().add(gettfApproveNum(),
						gettfApproveNum().getName());
				getUIDialogContentPane().add(gettfApproveDept(),
						gettfApproveDept().getName());
				getUIDialogContentPane().add(getrefEffectDate(),
						getrefEffectDate().getName());
				getUIDialogContentPane().add(getscpComment(),
						getscpComment().getName());
				getUIDialogContentPane().add(getlbOldName(),
						getlbOldName().getName());
				getUIDialogContentPane().add(getrefOldName(),
						getrefOldName().getName());
				getUIDialogContentPane().add(getbtnOK(), getbtnOK().getName());
				getUIDialogContentPane().add(getbtnCancel(),
						getbtnCancel().getName());
				getUIDialogContentPane().add(gettfNewName(),
						gettfNewName().getName());
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
			setSize(400, 290);
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
			DeptRenameDlg aDeptRenameDlg;
			aDeptRenameDlg = new DeptRenameDlg();
			aDeptRenameDlg.setModal(true);
			aDeptRenameDlg
					.addWindowListener(new java.awt.event.WindowAdapter() {
						public void windowClosing(java.awt.event.WindowEvent e) {
							System.exit(0);
						};
					});
			aDeptRenameDlg.show();
			java.awt.Insets insets = aDeptRenameDlg.getInsets();
			aDeptRenameDlg.setSize(aDeptRenameDlg.getWidth() + insets.left
					+ insets.right, aDeptRenameDlg.getHeight() + insets.top
					+ insets.bottom);
			aDeptRenameDlg.setVisible(true);
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
		if (getrefOldName().getRefPK() == null
				|| getrefOldName().getRefPK().trim().length() == 0) {
			MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404",
							"UPP60050404-000017")/* @res "提示" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404",
							"UPP60050404-000091")/* @res "原部门名称不允许空！" */);
			getrefOldName().grabFocus();
			return false;
		}
		if (gettfNewName().getText() == null
				|| gettfNewName().getText().trim().length() == 0) {
			MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404",
							"UPP60050404-000017")/* @res "提示" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404",
							"UPP60050404-000092")/* @res "新部门名称不允许空！" */);
			gettfNewName().grabFocus();
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
		// add by zhyan v50
		if (getrefEffectDate().getRefPK() != null) {
			UFDate effectdate = UFDate.getDate(getrefEffectDate().getText());
			UFDate creatdate = m_vDeptdocVO.getCreateDate();
			if (creatdate != null) {
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
			}
		}
		return true;
	}
}
