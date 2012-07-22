package nc.ui.hi.hi_410.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.generate.Gener;
import nc.bs.logging.Logger;
import nc.bs.util.SleepTime;
import nc.bs.util.Uriread;
import nc.hr.utils.PubEnv;
import nc.itf.hr.trn.TRNDelegator;
import nc.itf.yto.util.IFilePost;
import nc.itf.yto.util.IGener;
import nc.itf.yto.util.IReadmsg;
import nc.ui.hi.hi_410.TranslateRegularMainPan;
import nc.ui.hi.hi_410.TrnBtnReg;
import nc.ui.hr.comp.pf.DirectApproveDialog;
import nc.ui.hr.comp.pf.PFDataModel;
import nc.ui.hr.comp.pf.action.PFApproveAction;
import nc.ui.hr.frame.FrameUI;
import nc.ui.hr.global.Global;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.vo.hi.hi_301.GeneralVO;
import nc.vo.hi.hi_410.RegularApplyExAggVO;
import nc.vo.hi.hi_410.RegularapplBVO;
import nc.vo.hi.hi_410.RegularapplHVO;
import nc.vo.hr.comp.pf.PFAggVO;
import nc.vo.hr.comp.pf.PFConfig;
import nc.vo.hr.tools.pub.HRAggVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.yto.business.OperationMsg;
import nc.vo.yto.business.PsndocVO;

/**
 * ת�������ڵ��µ�������ť����@�������� (2007-6-1����03:29:23)
 * @author gaox
 */
public class TrnApproveAction extends PFApproveAction {

	private String opinion = "";// �������
	private TranslateRegularMainPan mainPanel = (TranslateRegularMainPan) getMainPanel();// �õ��������Panel
	private int m_iDirectApproveResult = -1;

	private RegularApplyExAggVO applyVO = null;

	private RegularapplHVO curSelectedVO = null;
	
	private List<PsndocVO> PsndocUpdate = new ArrayList<PsndocVO>();
	private List<PsndocVO> PsndocUpdate2 = new ArrayList<PsndocVO>();

	public TrnApproveAction(FrameUI frameUI1) {
		super(frameUI1);
	}

	/**
	 * ������ť���� ��������(2007-6-1 ����03:29:54)
	 * @throws Exception
	 */
	public void execute() throws Exception {

		// �����е��ݵ�״̬���ܲ�һ��,�������þۺ�VO����������Ϊ�����ϵ�����
		if (!isApproveable(applyVO)) {// ��鵱ǰ�����Ƿ��������
			return;
		}
		boolean isLocked = false;
		try {
			if (curSelectedVO == null) {// ���û��ѡ�񵥾ݣ�������ʾ������
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("600904",
								"UPP600904-000035")/* @res "��ѡ��������ĵ��ݣ�" */);
				return;
			}

			String censor = mainPanel.getUserID();
			// ����������ڱ������û�������������ʾ�����أ�������������
			if (!mainPanel.lockBill(curSelectedVO.getPk_rm_apply_h())) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("600904",
								"UPP600904-100084")/* @res "�õ������ڱ������û����������Ժ�����!" */);
				isLocked = true;
				return;
			}

	        String strBillPk_corp = null;

	        if (pfConfig.getBusinessTypeFieldCode() != null)
	        {
	            strBillPk_corp = (String) curSelectedVO.getAttributeValue(pfConfig.getCorpFieldCode());
	        }

	        hashPara = checkApproveType(strBillPk_corp, dataModel.getBillTypeCode(), null);

			if (!dataModel.isDirectApprove(curSelectedVO.getPrimaryKey())) {
				PFAggVO aggVO = new PFAggVO();
				aggVO.setParentVO(curSelectedVO);
				// ȡ�õ����ϵ���Ա����
				String[] pk_psndocs = applyVO.getPk_psndocs();
				// ���Ƿ�ͬ��Ϊtrue����Ա��ӵ�Vector v��
				// (ÿ����ֻ���һ����¼�����ƶ�н�ʶ����ʵ���ʹ�ã�������ֻʹ��aggVO������VO)
				Vector<RegularapplBVO> v = new Vector<RegularapplBVO>();
				for (String i : pk_psndocs) {
					RegularapplBVO[] bodyVOs = applyVO.getItemVOsByPK(i);
					for (RegularapplBVO j : bodyVOs) {
						if (j.getPk_flddict().equalsIgnoreCase("isapprove"))
							if (UFBoolean.valueOf(j.getVvalue().trim()).booleanValue())
								v.addElement(j);

					}
				}
				// ȡ���Ƿ�ͬ��Ϊtrue����Ա����VO(ÿ����ֻ��һ����¼)
				RegularapplBVO[] approvedBodyVO = new RegularapplBVO[v.size()];
				if (v.size() > 0)
					v.copyInto(approvedBodyVO);
				else
					approvedBodyVO = null;
				aggVO.setChildrenVO(approvedBodyVO);
				/** ******test���� */
				// ִ����������
				PfUtilClient.runAction(getFrameUI(), IPFActionName.APPROVE + PubEnv.getPk_user(), dataModel
	                    .getBillTypeCode(), PubEnv.getServerDate().toString(), aggVO,
	                    null, null, null, hashPara);

				//������ڴ��������д򿪴���,��������֮��رմ���
				 if (mainPanel.getTrnApprovePanel().isActionForFlow()) {
					mainPanel.getTrnApprovePanel().onClosing();
				}
			} else {// �������ͨ����������������
				// �ܲ�ѯ���Ĵ����������еĵ��ݣ�������û�û������Ȩ�ޣ�˵���Ѿ������û��������������ٽ�����������
				if (curSelectedVO.getIbillstate() == IBillStatus.CHECKGOING) {
					getFrameUI().showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600904",
							"UPP600904-000113")/* @res "���Ѿ��������õ��ݣ������ٽ�����������!" */);
					return;
				}
		        DirectApproveDialog approveDlg = new DirectApproveDialog(getFrameUI());
		        approveDlg.showModal();
				m_iDirectApproveResult = approveDlg.getResult();
				if (m_iDirectApproveResult == DirectApproveDialog.PF_APPROVE_APPROVED) {// ����ͨ��
					setCensorOpinion(approveDlg.getApproveNote());
					doApprove(applyVO);
				} else if (m_iDirectApproveResult == DirectApproveDialog.PF_APPROVE_REJECTED) {// ������ͨ��
					setCensorOpinion(approveDlg.getApproveNote());
					doUnApprove(applyVO);
				} else if (m_iDirectApproveResult == DirectApproveDialog.PF_APPROVE_RETURN) {// ����
					doReturn(applyVO);
				}else if(m_iDirectApproveResult == UIDialog.ID_CANCEL){
					return;
				}
				super.doDirectApprove(hrAggVO, approveDlg.getApproveNote(), m_iDirectApproveResult);
			}
			// �����ݿ��еõ����º�ĵ���
			curSelectedVO = TRNDelegator.getRegularappl()
					.findRegularapplHByPrimaryKey(curSelectedVO.getPrimaryKey());			
			// ͬ���б�Ϳ�Ƭ�ϵĵ���״̬
			mainPanel.getMainBillListPanel().setSelectedHeadData("ibillstate", curSelectedVO.getIbillstate());
			mainPanel.getMainBillCardPanel().getBillCardPanel().getBillData().setHeaderValueVO(curSelectedVO);
            getFrameUI().showHintMessage(
                    NCLangRes4VoTransl.getNCLangRes().getStrByID("nc_hr_pf", "UPPnc_hr_pf_000002"));// ���������������
			if (curSelectedVO.getIbillstate() == IBillStatus.CHECKPASS) {// �����ǰ����������ͨ��״̬ 
				
				// ת������ͨ����ͬ�����м�� by river for 2011-09-14
				String[] pk_psndocs = applyVO.getPk_psndocs();
				String pk_psndocFormat = "";
				for(String pk_psndoc : pk_psndocs) {
					pk_psndocFormat += "'" + pk_psndoc + "',";
				}
				
				if(pk_psndocFormat.length() > 0) 
					pk_psndocFormat = pk_psndocFormat.substring(0 , pk_psndocFormat.length() - 1);
				
				IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class.getName());
				PsndocVO[] psndocs = (PsndocVO[])msg.getGeneralVOs(PsndocVO.class, " pk_psndoc in ("+pk_psndocFormat+")");
				
				IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
				IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
				OperationMsg opmsg = new OperationMsg();
				opmsg.setOptime(Global.getClientEnvironment().getDate().toString());
				opmsg.setUnitcode(Global.getClientEnvironment().getCorporation().getUnitcode());
				opmsg.setUsercode(Global.getClientEnvironment().getUser().getUserCode());
				opmsg.setUsername(Global.getClientEnvironment().getUser().getUserName());
				
				for(PsndocVO psndoc : psndocs) {
					String retStr = filepost.postFile(Uriread.uriPath() , 
							gener.generateXml4(psndoc, "RequestPsndoc", "psn", "update" , opmsg));
				
					String[] strs = retStr.split("<success>");
					String retMsg = "";
					if (strs.length > 1)
						retMsg = strs[1].substring(0, strs[1].indexOf("<"));

					if (retMsg.equals("false")) {
						PsndocUpdate.add(psndoc);
					}
				}
				
				if(PsndocUpdate.size() > 0 ) {
					new Thread() {
						public void run() {
							IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
							IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
							OperationMsg opmsg = new OperationMsg();
							opmsg.setOptime(Global.getClientEnvironment().getDate().toString());
							opmsg.setUnitcode(Global.getClientEnvironment().getCorporation().getUnitcode());
							opmsg.setUsercode(Global.getClientEnvironment().getUser().getUserCode());
							opmsg.setUsername(Global.getClientEnvironment().getUser().getUserName());
							
							try {
								if(true) {
									this.sleep(SleepTime.Time);
								
									for(PsndocVO psn : PsndocUpdate) {
										String retStr = filepost.postFile(Uriread.uriPath() , 
												gener.generateXml4(psn, "RequestPsndoc", "psn", "add" , opmsg));
										
										String[] strs = retStr.split("<success>");
										String retMsg = "";
										if (strs.length > 1)
											retMsg = strs[1].substring(0, strs[1].indexOf("<"));
									
										if(retMsg.equals("false") || strs.length <= 1)
											PsndocUpdate2.add(psn);
									}
									
									PsndocUpdate = PsndocUpdate2;
									
									PsndocUpdate2 = new ArrayList<PsndocVO>();
									
//									if(PsndocUpdate.size() == 0)
//										break;
									
									
								}
								System.out.println("<<<<<<  ��Ա�����޸��߳�ֹͣ�� >>>>>>");
								System.out.println("<<<<<<  �̺߳�: " + this.getId() + " >>>>>>");
								this.stop();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}.start();
				}
				
				// ͬ������ end
				
				// ����ͨ������ʾ�Ƿ���֪ͨ�ʼ�,��ѡ����,��ִ�з���֪ͨ��ť����
				if (mainPanel.getFrameUI().showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("hr_trn_pub","UPPhr_trn_pub-000000")/*@res "�Ƿ���֪ͨ�ʼ�"*/) == UIDialog.ID_YES)
					mainPanel.getFrameUI().getBtnManager().execCmd(TrnBtnReg.BTN_SENDOUT);
				
			}
			//ͬ������ĵ��� liudan
			applyVO.setParentVO(curSelectedVO);
			mainPanel.getHashTranslateRegularBill().put(curSelectedVO.getPrimaryKey(), applyVO);
		} catch (Exception e) {
			mainPanel.reportException(e);
			mainPanel.getTrnApprovePanel().showErrorMessage(e.getMessage());
		} finally {
			if (curSelectedVO != null && !isLocked) {// ��������
				mainPanel.freeLockBill(curSelectedVO.getPk_rm_apply_h());
			}
		}
	}

	/**
	 * ����������� ��������(2007-8-14 ����04:24:16)
	 * @param newOpinion
	 */
	protected void setCensorOpinion(String newOpinion) {
		opinion = newOpinion;
	}

	/**
	 * ����ͨ��(ֱ��) ��������(2007-6-1 ����04:34:20)
	 * @return boolean
	 */
	public boolean doApprove(RegularApplyExAggVO applyVO) {
		try {
			RegularapplHVO headerVO = applyVO.getHeaderVO();
			headerVO.setAttributeValue("vopinion", opinion);// �����������
			headerVO.setIbillstate(new Integer(1));// ���õ���״̬Ϊ����׼
			PFAggVO aggVO = new PFAggVO();
			aggVO.setParentVO(headerVO);
			// ȡ�õ����ϵ���Ա����
			String[] pk_psndocs = applyVO.getPk_psndocs();
			// ���Ƿ�ͬ��Ϊtrue����Ա��ӵ�Vector v��
			// (ÿ����ֻ���һ����¼�����ƶ�н�ʶ����ʵ���ʹ�ã�������ֻʹ��aggVO������VO)
			Vector<RegularapplBVO> v = new Vector<RegularapplBVO>();
			for (String i : pk_psndocs) {
				RegularapplBVO[] bodyVOs = applyVO.getItemVOsByPK(i);
				for (RegularapplBVO j : bodyVOs) {
					if (j.getPk_flddict().equalsIgnoreCase("isapprove"))
						if (UFBoolean.valueOf(j.getVvalue().trim()).booleanValue())
							v.addElement(j);

				}
			}
			// ȡ���Ƿ�ͬ��Ϊtrue����Ա����VO(ÿ����ֻ��һ����¼)
			RegularapplBVO[] approvedBodyVO = new RegularapplBVO[v.size()];
			if (v.size() > 0)
				v.copyInto(approvedBodyVO);
			else
				approvedBodyVO = null;
			aggVO.setChildrenVO(approvedBodyVO);

			// ִ����������
			PfUtilClient.runAction(getFrameUI(), IPFActionName.APPROVE + PubEnv.getPk_user(), dataModel
                    .getBillTypeCode(), PubEnv.getServerDate().toString(), aggVO,
                    null, null, null, hashPara);
		} catch (Exception e) {
			mainPanel.reportException(e);
			mainPanel.getTrnApprovePanel().showErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * ������ͨ��(ֱ��) ��������(2007-6-1 ����04:40:18)
	 */
	public boolean doUnApprove(RegularApplyExAggVO applyVO) {
		try {
			RegularapplHVO headerVO = applyVO.getHeaderVO();
			headerVO.setAttributeValue("vopinion", opinion);// �����������
			headerVO.setIbillstate(new Integer(0));// ���õ���״̬Ϊ����δͨ��
			// �������ݿ�
			TRNDelegator.getRegularappl().rejectLocalBillForCensor(applyVO,
					mainPanel.getUserID());

//			 ��ԭ��������������pub_workflownote���в����¼
//			String censor = mainPanel.getUserID();
//			TRNDelegator.getRegularappl().approveLocalBillForCensor(applyVO,
//					censor);
		} catch (Exception e) {
			mainPanel.reportException(e);
			mainPanel.getTrnApprovePanel().showErrorMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("600904",
							"UPP600904-000048")/* @res "�����쳣" */);
			return false;
		}
		return true;
	}

	/**
	 * ����(ֱ��) ��������(2007-8-10 ����09:48:48)
	 * @param applyVO
	 * @return boolean
	 */
	public boolean doReturn(RegularApplyExAggVO applyVO) {
		RegularapplHVO headerVO = applyVO.getHeaderVO();
		headerVO.setIbillstate(8);// ���õ���״̬Ϊ��д��
		// �������ݿ�
		try {
			TRNDelegator.getRegularappl().returnLocalBillForCensor(applyVO);

//			 ��ԭ��������������pub_workflownote���в����¼
//			String censor = mainPanel.getUserID();
//			TRNDelegator.getRegularappl().approveLocalBillForCensor(applyVO,
//					censor);
		} catch (BusinessException e) {
			mainPanel.reportException(e);
			mainPanel.getTrnApprovePanel().showErrorMessage(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * ��ǰ�����Ƿ�������������ǲ������⣬�Ƿ񱻱����ύ��ɾ��) ��������(2007-6-1 ����04:35:09)
	 * @return boolean
	 */
	public boolean isApproveable(RegularApplyExAggVO applyVO) {
		try {
			RegularapplHVO headerVO = applyVO.getHeaderVO();
			if (headerVO == null) {
				MessageDialog
						.showHintDlg(mainPanel, nc.ui.ml.NCLangRes
								.getInstance().getStrByID("600904",
										"UPP600904-000036")/* @res "��ʾ" */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"600904", "UPP600904-000074")/*
																		 * @res
																		 * "�˵����Ѿ���ɾ������ˢ�º��ٲ���"
																		 */);
				return false;
			} else if ((headerVO.getIbillstate().intValue() == IBillStatus.NOPASS)
					|| (headerVO.getIbillstate().intValue() == IBillStatus.CHECKPASS)) {
				MessageDialog
						.showHintDlg(mainPanel, nc.ui.ml.NCLangRes
								.getInstance().getStrByID("600904",
										"UPP600904-000036")/* @res "��ʾ" */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"600904", "UPP600904-000075")/*
																		 * @res
																		 * "�˵����Ѿ�����������ˢ�º��ٲ���"
																		 */);
				return false;
			} else if (headerVO.getIbillstate().intValue() == IBillStatus.FREE) {
				MessageDialog
						.showHintDlg(mainPanel, nc.ui.ml.NCLangRes
								.getInstance().getStrByID("600904",
										"UPP600904-000036")/* @res "��ʾ" */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"600904", "UPP600904-000076")/*
																		 * @res
																		 * "�˵�����δ�ύ����ˢ�º��ٲ���"
																		 */);
				return false;
			}

			String[] pk_psndocs = applyVO.getPk_psndocs();// �õ����뵥�ϵ�������ԱPK
			if (!(pk_psndocs.length > 0)) {// ������뵥��û����Ա,������ʾ
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("600904",
								"UPP600904-000114")/* @res "���뵥��û����Ա" */);
				return false;
			}
			// �ж��ӱ�VO�������Ƿ��в����Լ���λ������Ŀ(Ĭ�ϲ�����)
			boolean isIncludeDept = false;
			boolean isIncludeJob = false;
			// ֻ��ѯ�ۺ�VO��һ���˵��ӱ�VO����
			RegularapplBVO[] tempBodyVO = applyVO.getItemVOsByPK(pk_psndocs[0]);
			for (RegularapplBVO i : tempBodyVO) {// ѭ���ӱ�����
				if (i.getPk_flddict().equalsIgnoreCase("pk_deptdoc"))
					isIncludeDept = true;
				else if (i.getPk_flddict().equalsIgnoreCase("pk_om_job"))
					isIncludeJob = true;
			}

			// ������Ա������ѯ���Ӧ����Ա�Ƿ��Ѿ�ת��
			java.util.Map isRegular=TRNDelegator.getRegularappl().queryRegularByPK(pk_psndocs);
			Vector<GeneralVO> oldPsnData=new Vector<GeneralVO>();// ת��ǰ��Ա��Ϣ
			Vector<GeneralVO> newPsnData=new Vector<GeneralVO>();// ת������Ա��Ϣ
			// ѭ����Ա,���ͬ��ת������Ա�Ƿ��Ѿ�ת��,ͬʱ�õ���Ա������仯��ͬ��ת������Ա����
			for (int i = 0; i < pk_psndocs.length; i++) {
				boolean isapprove = false;// �Ƿ�ͬ��ת��(Ĭ��Ϊͬ��)
				// �õ�������ָ����ԱPK���ӱ���������
				RegularapplBVO[] bodyVO = applyVO.getItemVOsByPK(pk_psndocs[i]);
				String psnname = null;
				// �õ���ǰ��Աת��ǰ�����Ϣ(ֻȡ����Ա������ת��ǰ����Ա������ڲ��š���λ�����ж��Ƿ񳬱�ʹ��)
				GeneralVO oldPsnVO=new GeneralVO();
				GeneralVO newPsnVO=new GeneralVO();
				for (RegularapplBVO j : bodyVO) {
					// �õ��Ƿ�ͬ��
					if (j.getPk_flddict().equalsIgnoreCase("isapprove")) {
						if (UFBoolean.valueOf(j.getVvalue().trim()).booleanValue()) {
							isapprove = true;// ͬ��ת��
						}
					}
					// �õ���Ա����
					else if (j.getPk_flddict().equalsIgnoreCase("psnname"))
						psnname = j.getVvalue();
					// �õ�ת��ǰ��Ա���
					else if (j.getPk_flddict().equalsIgnoreCase("pk_currpsncl"))
						oldPsnVO.setAttributeValue("pk_psncl", j.getVvalue());
					// �õ�ת������Ա���
					else if (j.getPk_flddict().equalsIgnoreCase("pk_newpsncl"))
						newPsnVO.setAttributeValue("pk_psncl", j.getVvalue());
					// ȡ�õ�ǰ��Ա���ڲ��ŵ�PK�����newPsnVO��oldPsnVO��
					else if (j.getPk_flddict().equalsIgnoreCase("pk_deptdoc")) {
						newPsnVO.setAttributeValue("pk_deptdoc", j.getVvalue());
						oldPsnVO.setAttributeValue("pk_deptdoc", j.getVvalue());
					}// ȡ�õ�ǰ��Ա������λ��PK�����newPsnVO��oldPsnVO��
					else if (j.getPk_flddict().equalsIgnoreCase("pk_om_job")) {
						newPsnVO.setAttributeValue("pk_om_job", j.getVvalue());
						oldPsnVO.setAttributeValue("pk_om_job", j.getVvalue());
					}
				}

				if (isapprove) {// ���ͬ��ת��
					//��������Ѿ�ת��,������ʾ,����
					if (isRegular.get(pk_psndocs[i]).toString().equalsIgnoreCase("Y")) {
						getFrameUI().showWarningMessage(psnname
								+ nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("600904", "UPP600904-000115")/* @res "�Ѿ�ת�������޸ĺ�������" */);
						return false;
					}
					// ������˵�ת��ǰ����Ա������仯,��ת��ǰ�����Ϣ�ֱ�����Vector��
					if (!(oldPsnVO.getAttributeValue("pk_psncl").toString())
							.equalsIgnoreCase((newPsnVO
									.getAttributeValue("pk_psncl")).toString())) {
						oldPsnVO.setAttributeValue("pk_psndoc", pk_psndocs[i]);
						newPsnVO.setAttributeValue("pk_psndoc", pk_psndocs[i]);
						// �����ת����Ŀ��δ���ò��Ż��߸�λ����Ҫ�����ݿ��в�����ݣ�����oldPsnVO��newPsnVO��
						if (isIncludeDept == false || isIncludeJob == false) {
							GeneralVO tempVO = TRNDelegator.getTrnPub()
									.queryDeptJobByPsnPK(pk_psndocs[i]);
							oldPsnVO.setAttributeValue("pk_deptdoc", tempVO
									.getAttributeValue("pk_deptdoc"));
							oldPsnVO.setAttributeValue("pk_om_job", tempVO
									.getAttributeValue("pk_om_job"));
							newPsnVO.setAttributeValue("pk_deptdoc", tempVO
									.getAttributeValue("pk_deptdoc"));
							newPsnVO.setAttributeValue("pk_om_job", tempVO
									.getAttributeValue("pk_om_job"));
						}
						oldPsnData.addElement(oldPsnVO);
						newPsnData.addElement(newPsnVO);
					}
				}
			}
			// �õ�ͬ��ת������Ա������仯����Աת��ǰ�����Ϣ����
			GeneralVO[] oldPsnVOs=new GeneralVO[oldPsnData.size()];
			if (oldPsnData.size() > 0)
				oldPsnData.copyInto(oldPsnVOs);
			else
				oldPsnVOs = null;
			GeneralVO[] newPsnVOs=new GeneralVO[newPsnData.size()];
			if (newPsnData.size() > 0)
				newPsnData.copyInto(newPsnVOs);
			else
				newPsnVOs = null;
			// ���oldPsnVOs��newPsnVOs����Ϊ��,����Ƿ񳬱�
			if (oldPsnVOs != null && newPsnVOs != null) {
				boolean isExceed = mainPanel.isExceedWorkout(oldPsnVOs, newPsnVOs);
				// ���������ǿ�Ʋ�ͨ���򷵻�
				if (isExceed)
					return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			mainPanel.reportException(e);
		}
		return true;
	}


	private PFDataModel dataModel = (PFDataModel) getDataModel();
	private PFConfig pfConfig = dataModel.getPFConfig();

	 @Override
	 public boolean validate() throws ValidationException {
		int iSelection = mainPanel.getCurSelectRow();// �õ���ǰѡ����

		curSelectedVO = (RegularapplHVO) mainPanel.getMainBillListPanel()
				.getBillListPanel().getHeadBillModel().getBodyValueRowVO(
						iSelection, RegularapplHVO.class.getName());

		if (iSelection < -1 || curSelectedVO == null) {
			throw new ValidationException(getResource("nc_hr_pf",
					"UPPnc_hr_pf_000000"));// ����ѡ�����ݣ�
		}
		curSelectedVO.setPk_billtype("BA");

		applyVO = new RegularApplyExAggVO();

		// �õ�����VO
		if (!mainPanel.getHashTranslateRegularBill().containsKey(
				curSelectedVO.getPrimaryKey())) {
			RegularapplBVO[] bodyVOs;
			try {
				RegularapplHVO headVOs = TRNDelegator.getRegularappl()
						.findRegularapplHByPrimaryKey(curSelectedVO.getPrimaryKey());

				curSelectedVO.setStrFunCode(headVOs.getStrFunCode());
				applyVO.setHeaderVO(curSelectedVO);
				bodyVOs = TRNDelegator.getRegularappl()
						.queryRegularapplBByMainPK(curSelectedVO.getPrimaryKey());
				applyVO.setBodyVO(bodyVOs);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
			mainPanel.getHashTranslateRegularBill().put(
					applyVO.getHeaderVO().getPrimaryKey(), applyVO);
		} else
			applyVO = (RegularApplyExAggVO) mainPanel
					.getHashTranslateRegularBill().get(
							curSelectedVO.getPrimaryKey());

		if (applyVO == null || curSelectedVO == null) {
			throw new ValidationException(getResource("nc_hr_pf",
					"UPPnc_hr_pf_000042"));// �����Ѿ����޸ģ�����ˢ���ٲ�����
		}

		Integer intApproveState = (Integer) curSelectedVO.getAttributeValue(pfConfig
				.getApproveStateFieldCode());

		// ���������ͨ��������δͨ����״̬���Ͳ�������
		if (intApproveState != null && IBillStatus.COMMIT != intApproveState
				&& IBillStatus.CHECKGOING != intApproveState) {
			throw new ValidationException(getResource("nc_hr_pf",
					"UPPnc_hr_pf_000003"));// ��������δ��ʼ�����Ѿ����������ܽ���������
		}

		if (!dataModel.isDirectApprove(curSelectedVO.getPrimaryKey())
				&& !dataModel.isCheckman(curSelectedVO.getPrimaryKey(), "BA")) {
			throw new ValidationException(getResource("nc_hr_pf",
					"UPPnc_hr_pf_000029"));
			/** "�����ǵ��ݵĵ�ǰ�����ˣ�" */
		}
		headVO = curSelectedVO;
		hrAggVO = new HRAggVO();
		hrAggVO.setParentVO(curSelectedVO);
		return true;
	}

}