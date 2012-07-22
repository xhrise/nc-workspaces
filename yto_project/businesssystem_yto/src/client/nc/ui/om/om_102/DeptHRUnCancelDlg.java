package nc.ui.om.om_102;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.generate.Gener;
import nc.bs.util.SleepTime;
import nc.bs.util.Uriread;
import nc.itf.hr.jf.IOrgInfo;
import nc.itf.hr.jf.ORGDelegator;
import nc.itf.yto.util.IFilePost;
import nc.itf.yto.util.IGener;
import nc.itf.yto.util.IReadmsg;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.bd.b04.DeptdocVO;
import nc.vo.om.om_101.DepthistoryVO;
import nc.vo.om.om_102.OMCommonValue;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

/**
 * ȡ���������ŶԻ��� �������ڣ�(2004-12-20 13:31:31)
 * 
 * @author��������
 */
public class DeptHRUnCancelDlg extends nc.ui.pub.beans.UIDialog implements
		ActionListener {
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

	private nc.ui.pub.beans.UICheckBox ivjckbIsLowerDept = null;

	private nc.ui.pub.beans.UICheckBox ivjckbIsLowerJob = null;

	private nc.ui.pub.beans.UILabel ivjlbEffectDate = null;

	private nc.ui.pub.beans.UIButton ivjbtnCancel = null;

	private nc.ui.pub.beans.UIButton ivjbtnOK = null;

	DeptdocVO m_vDeptdocVO = null;

	/**
	 * DeptHRCanceldDlg ������ע�⡣
	 */
	public DeptHRUnCancelDlg() {
		super();
		initialize();
	}

	/**
	 * DeptHRCanceldDlg ������ע�⡣
	 * 
	 * @param owner
	 *            java.awt.Frame
	 */
	public DeptHRUnCancelDlg(java.awt.Container parent, String title,
			DeptdocVO deptdocVO) {
		super(parent, title);
		initialize();
		// ���ò��Ų���
		getrefName().setPK(deptdocVO.getPk_deptdoc());
		// �ݴ浱ǰ����VO
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
				if (ORGDelegator.getIOrgInfo().deptChangeOperate(
						getCurDeptHVO(), m_vDeptdocVO, false)) {

					// ����ת��ʱͬ��������Ϣ���м�� add by river for 2011-09-13
//					DeptdocVO deptdoc = m_vDeptdocVO;
					
					IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class.getName());
					IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
					IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
				
					nc.vo.yto.business.DeptdocVO deptdocvo = ((nc.vo.yto.business.DeptdocVO[])msg.getGeneralVOs(nc.vo.yto.business.DeptdocVO.class, " pk_deptdoc = '"+m_vDeptdocVO.getAttributeValue("pk_deptdoc")+"'"))[0];
					
					
					deptdocvo.setAttributeValue("hrcanceled", new UFBoolean("N"));
					String retStr = filepost.postFile(Uriread
							.uriPath(), gener.generateXml3(deptdocvo,
							"RequestDeptdoc", "dept", "update"));
					
					String[] strs = retStr.split("<success>");
					String retMsg = "";
					if (strs.length > 1)
						retMsg = strs[1].substring(0, strs[1].indexOf("<"));

					if (retMsg.equals("false") || strs.length <= 1) {
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
									System.out.println("<<<<<<  ���ŵ����޸��߳�ֹͣ�� >>>>>>");
									System.out.println("<<<<<<  �̺߳�: " + this.getId() + " >>>>>>");
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
	 * ���� btnCancel ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {
			try {
				ivjbtnCancel = new nc.ui.pub.beans.UIButton();
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("common", "UC001-0000008")/* @res "ȡ��" */);
				ivjbtnCancel.setBounds(244, 231, 66, 23);
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
	 * ���� btnOK ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getbtnOK() {
		if (ivjbtnOK == null) {
			try {
				ivjbtnOK = new nc.ui.pub.beans.UIButton();
				ivjbtnOK.setName("btnOK");
				ivjbtnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"common", "UC001-0000044")/* @res "ȷ��" */);
				ivjbtnOK.setBounds(89, 231, 66, 22);
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
	 * ���� ckbIsLowerDept ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UICheckBox
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UICheckBox getckbIsLowerDept() {
		if (ivjckbIsLowerDept == null) {
			try {
				ivjckbIsLowerDept = new nc.ui.pub.beans.UICheckBox();
				ivjckbIsLowerDept.setName("ckbIsLowerDept");
				ivjckbIsLowerDept
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050404", "UPP60050404-000028")/*
																	 * @res
																	 * "���¼�����"
																	 */);
				ivjckbIsLowerDept.setLocation(51, 38);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjckbIsLowerDept;
	}

	/**
	 * ���� ckbIsLowerJob ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UICheckBox
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UICheckBox getckbIsLowerJob() {
		if (ivjckbIsLowerJob == null) {
			try {
				ivjckbIsLowerJob = new nc.ui.pub.beans.UICheckBox();
				ivjckbIsLowerJob.setName("ckbIsLowerJob");
				ivjckbIsLowerJob
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050404", "UPP60050404-000029")/*
																	 * @res
																	 * "��������λ"
																	 */);
				ivjckbIsLowerJob.setLocation(175, 38);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjckbIsLowerJob;
	}

	/**
	 * ��õ�ǰDepthistoryVO�� �������ڣ�(2004-12-27 15:39:29)
	 */
	private DepthistoryVO getCurDeptHVO() {
		DepthistoryVO m_vDeptHVO = new DepthistoryVO();
		// ��������
		m_vDeptHVO.setPk_deptdoc(m_vDeptdocVO.getPk_deptdoc());
		// ������
		m_vDeptHVO
				.setChangetype(new Integer(OMCommonValue.ChangeType_UNCANCEL));
		// ���ű���
		m_vDeptHVO.setDeptcode(m_vDeptdocVO.getDeptcode());
		// ���ż���
		m_vDeptHVO.setDeptlevel(m_vDeptdocVO.getDeptlevel());
		// ��������
		m_vDeptHVO.setDeptname(m_vDeptdocVO.getDeptname());
		// �ϼ�����
		m_vDeptHVO.setPk_fathedept(m_vDeptdocVO.getPk_fathedept());
		// �Ƿ���ղ���
		m_vDeptHVO.setIsreceived(new nc.vo.pub.lang.UFBoolean('N'));
		// ���Ÿ�����
		m_vDeptHVO.setPk_psndoc(m_vDeptdocVO.getPk_psndoc());
		// ��׼�ĺ�
		m_vDeptHVO.setApprovenum(gettfApproveNum().getText());
		// ��׼��λ
		m_vDeptHVO.setApprovedept(gettfApproveDept().getText());
		// ��Ч����
		m_vDeptHVO.setEffectdate(new UFDate(getrefEffectDate().getText()));
		// ��ע
		m_vDeptHVO.setMemos(gettaComment().getText());
		// �Ƿ�����¼�����
		m_vDeptHVO.setLowerdept(getckbIsLowerDept().isSelected());
		// �Ƿ����������λ
		m_vDeptHVO.setLowerjob(getckbIsLowerJob().isSelected());

		return m_vDeptHVO;
	}

	/**
	 * ���� lbApproveDept ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getlbApproveDept() {
		if (ivjlbApproveDept == null) {
			try {
				ivjlbApproveDept = new nc.ui.pub.beans.UILabel();
				ivjlbApproveDept.setName("lbApproveDept");
				ivjlbApproveDept
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050404", "UPP60050404-000012")/*
																	 * @res
																	 * "��׼��λ��"
																	 */);
				ivjlbApproveDept.setBounds(46, 95, 71, 22);
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
	 * ���� lbApproveNum ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getlbApproveNum() {
		if (ivjlbApproveNum == null) {
			try {
				ivjlbApproveNum = new nc.ui.pub.beans.UILabel();
				ivjlbApproveNum.setName("lbApproveNum");
				ivjlbApproveNum
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050404", "UPP60050404-000013")/*
																	 * @res
																	 * "��׼�ĺţ�"
																	 */);
				ivjlbApproveNum.setBounds(46, 67, 71, 22);
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
	 * ���� lbComment ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getlbComment() {
		if (ivjlbComment == null) {
			try {
				ivjlbComment = new nc.ui.pub.beans.UILabel();
				ivjlbComment.setName("lbComment");
				ivjlbComment
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050404", "UPP60050404-000014")/*
																	 * @res
																	 * "��ע��"
																	 */);
				ivjlbComment.setBounds(46, 154, 71, 22);
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
	 * ���� lbeEffectDate ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getlbEffectDate() {
		if (ivjlbEffectDate == null) {
			try {
				ivjlbEffectDate = new nc.ui.pub.beans.UILabel();
				ivjlbEffectDate.setName("lbEffectDate");
				ivjlbEffectDate
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"60050404", "UPP60050404-000015")/*
																	 * @res
																	 * "��Ч���ڣ�"
																	 */);
				ivjlbEffectDate.setBounds(46, 125, 71, 22);
				ivjlbEffectDate.setILabelType(5/** ����� */
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
	 * ���� lbName ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getlbName() {
		if (ivjlbName == null) {
			try {
				ivjlbName = new nc.ui.pub.beans.UILabel();
				ivjlbName.setName("lbName");
				ivjlbName.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"60050404", "UPP60050404-000016")/* @res "�������ƣ�" */);
				ivjlbName.setBounds(47, 10, 71, 22);
				ivjlbName.setILabelType(5/** ����� */
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
	 * ���� refEffectDate ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIRefPane getrefEffectDate() {
		if (ivjrefEffectDate == null) {
			try {
				ivjrefEffectDate = new nc.ui.pub.beans.UIRefPane();
				ivjrefEffectDate.setName("refEffectDate");
				// ivjrefEffectDate.setLocation(153, 125);
				ivjrefEffectDate.setBounds(153, 125, 122, 22);
				ivjrefEffectDate.setRefNodeName("����");
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
	 * ���� refName ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIRefPane getrefName() {
		if (ivjrefName == null) {
			try {
				ivjrefName = new nc.ui.pub.beans.UIRefPane();
				ivjrefName.setName("refName");
				// ivjrefName.setLocation(154, 10);
				ivjrefName.setBounds(154, 10, 122, 22);
				ivjrefName.setRefNodeName("���ŵ���");
				ivjrefName.setEditable(false);
				ivjrefName.setEnabled(false);
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
	 * ���� scpComment ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIScrollPane getscpComment() {
		if (ivjscpComment == null) {
			try {
				ivjscpComment = new nc.ui.pub.beans.UIScrollPane();
				ivjscpComment.setName("scpComment");
				ivjscpComment.setBounds(153, 154, 188, 73);
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
	 * ���� UITextArea1 ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITextArea
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� tfApproveDept ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UITextField gettfApproveDept() {
		if (ivjtfApproveDept == null) {
			try {
				ivjtfApproveDept = new nc.ui.pub.beans.UITextField();
				ivjtfApproveDept.setName("tfApproveDept");
				ivjtfApproveDept.setBounds(153, 95, 122, 20);
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
	 * ���� tfApproveNum ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UITextField gettfApproveNum() {
		if (ivjtfApproveNum == null) {
			try {
				ivjtfApproveNum = new nc.ui.pub.beans.UITextField();
				ivjtfApproveNum.setName("tfApproveNum");
				ivjtfApproveNum.setBounds(153, 67, 122, 20);
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
	 * ���� UIDialogContentPane ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
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
				getUIDialogContentPane().add(getscpComment(),
						getscpComment().getName());
				getUIDialogContentPane().add(getckbIsLowerDept(),
						getckbIsLowerDept().getName());
				getUIDialogContentPane().add(getckbIsLowerJob(),
						getckbIsLowerJob().getName());
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
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
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
			setName("DeptHRCanceldDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(399, 290);
			setContentPane(getUIDialogContentPane());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			DeptHRUnCancelDlg aDeptHRUnCancelDlg;
			aDeptHRUnCancelDlg = new DeptHRUnCancelDlg();
			aDeptHRUnCancelDlg.setModal(true);
			aDeptHRUnCancelDlg
					.addWindowListener(new java.awt.event.WindowAdapter() {
						public void windowClosing(java.awt.event.WindowEvent e) {
							System.exit(0);
						};
					});
			aDeptHRUnCancelDlg.show();
			java.awt.Insets insets = aDeptHRUnCancelDlg.getInsets();
			aDeptHRUnCancelDlg.setSize(aDeptHRUnCancelDlg.getWidth()
					+ insets.left + insets.right, aDeptHRUnCancelDlg
					.getHeight()
					+ insets.top + insets.bottom);
			aDeptHRUnCancelDlg.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("nc.ui.pub.beans.UIDialog �� main() �з����쳣");
			exception.printStackTrace(System.out);
		}
	}

	/**
	 * У�����ݵĺϷ��ԡ� �������ڣ�(2004-12-29 11:16:06)
	 */
	private boolean validateData() {
		// �������Ʒǿ�У��
		if (getrefName().getRefPK() == null
				|| getrefName().getRefPK().trim().length() == 0) {
			MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404",
							"UPP60050404-000017")/* @res "��ʾ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404",
							"UPP60050404-000018")/* @res "�������Ʋ�����գ�" */);
			getrefName().grabFocus();
			return false;
		}
		// ��Ч���ڷǿ�У��
		if (getrefEffectDate().getRefPK() == null
				|| getrefEffectDate().getRefPK().trim().length() == 0) {
			MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404",
							"UPP60050404-000017")/* @res "��ʾ" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("60050404",
							"UPP60050404-000019")/* @res "��Ч���ڲ�����գ�" */);
			getrefEffectDate().grabFocus();
			return false;
		}
		// add by zhyan v50
		if (getrefEffectDate().getRefPK() != null) {
			try {
				IOrgInfo orginfo = ((IOrgInfo) NCLocator.getInstance().lookup(
						IOrgInfo.class.getName()));
				UFDate cncldate = orginfo.getDate(m_vDeptdocVO.getPk_deptdoc(),
						OMCommonValue.ChangeType_CANCEL);
				UFDate effectdate = UFDate
						.getDate(getrefEffectDate().getText());
				UFDate creatdate = m_vDeptdocVO.getCreateDate();

				if (cncldate != null && effectdate.before(cncldate)) {
					MessageDialog
							.showHintDlg(
									this,
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("60050404",
													"UPP60050404-000017")/*
																			 * @res
																			 * "��ʾ"
																			 */,
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("60050404",
													"UPP60050404-000146")/*
																			 * @res
																			 * "��Ч���ڲ������ڳ������ڣ�"
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
																			 * "��ʾ"
																			 */,
									nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("60050404",
													"UPP60050404-000145")/*
																			 * @res
																			 * "��Ч���ڲ������ڽ������ڣ�"
																			 */);
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return true;
	}
}
