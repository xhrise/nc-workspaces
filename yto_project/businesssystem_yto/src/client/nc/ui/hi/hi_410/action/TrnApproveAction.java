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
 * 转正审批节点下的审批按钮操作@创建日期 (2007-6-1下午03:29:23)
 * @author gaox
 */
public class TrnApproveAction extends PFApproveAction {

	private String opinion = "";// 审批意见
	private TranslateRegularMainPan mainPanel = (TranslateRegularMainPan) getMainPanel();// 得到主界面的Panel
	private int m_iDirectApproveResult = -1;

	private RegularApplyExAggVO applyVO = null;

	private RegularapplHVO curSelectedVO = null;
	
	private List<PsndocVO> PsndocUpdate = new ArrayList<PsndocVO>();
	private List<PsndocVO> PsndocUpdate2 = new ArrayList<PsndocVO>();

	public TrnApproveAction(FrameUI frameUI1) {
		super(frameUI1);
	}

	/**
	 * 审批按钮操作 创建日期(2007-6-1 下午03:29:54)
	 * @throws Exception
	 */
	public void execute() throws Exception {

		// 缓存中单据的状态可能不一致,重新设置聚合VO的主表数据为界面上的数据
		if (!isApproveable(applyVO)) {// 检查当前单据是否可以审批
			return;
		}
		boolean isLocked = false;
		try {
			if (curSelectedVO == null) {// 如果没有选择单据，给出提示，返回
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("600904",
								"UPP600904-000035")/* @res "请选择待审批的单据！" */);
				return;
			}

			String censor = mainPanel.getUserID();
			// 如果单据正在被其他用户操作，给出提示，返回，否则锁定单据
			if (!mainPanel.lockBill(curSelectedVO.getPk_rm_apply_h())) {
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("600904",
								"UPP600904-100084")/* @res "该单据正在被其它用户操作，请稍后再试!" */);
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
				// 取得单据上的人员数组
				String[] pk_psndocs = applyVO.getPk_psndocs();
				// 将是否同意为true的人员添加到Vector v中
				// (每个人只添加一条记录，供推动薪资订调资单据使用，审批流只使用aggVO的主表VO)
				Vector<RegularapplBVO> v = new Vector<RegularapplBVO>();
				for (String i : pk_psndocs) {
					RegularapplBVO[] bodyVOs = applyVO.getItemVOsByPK(i);
					for (RegularapplBVO j : bodyVOs) {
						if (j.getPk_flddict().equalsIgnoreCase("isapprove"))
							if (UFBoolean.valueOf(j.getVvalue().trim()).booleanValue())
								v.addElement(j);

					}
				}
				// 取得是否同意为true的人员的子VO(每个人只有一条记录)
				RegularapplBVO[] approvedBodyVO = new RegularapplBVO[v.size()];
				if (v.size() > 0)
					v.copyInto(approvedBodyVO);
				else
					approvedBodyVO = null;
				aggVO.setChildrenVO(approvedBodyVO);
				/** ******test审批 */
				// 执行审批操作
				PfUtilClient.runAction(getFrameUI(), IPFActionName.APPROVE + PubEnv.getPk_user(), dataModel
	                    .getBillTypeCode(), PubEnv.getServerDate().toString(), aggVO,
	                    null, null, null, hashPara);

				//如果是在代办事务中打开窗口,审批结束之后关闭窗口
				 if (mainPanel.getTrnApprovePanel().isActionForFlow()) {
					mainPanel.getTrnApprovePanel().onClosing();
				}
			} else {// 如果不是通过审批流进行审批
				// 能查询到的处于审批流中的单据，如果该用户没有审批权限，说明已经被此用户审批过，不能再进行审批操作
				if (curSelectedVO.getIbillstate() == IBillStatus.CHECKGOING) {
					getFrameUI().showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600904",
							"UPP600904-000113")/* @res "您已经审批过该单据，不能再进行审批操作!" */);
					return;
				}
		        DirectApproveDialog approveDlg = new DirectApproveDialog(getFrameUI());
		        approveDlg.showModal();
				m_iDirectApproveResult = approveDlg.getResult();
				if (m_iDirectApproveResult == DirectApproveDialog.PF_APPROVE_APPROVED) {// 审批通过
					setCensorOpinion(approveDlg.getApproveNote());
					doApprove(applyVO);
				} else if (m_iDirectApproveResult == DirectApproveDialog.PF_APPROVE_REJECTED) {// 审批不通过
					setCensorOpinion(approveDlg.getApproveNote());
					doUnApprove(applyVO);
				} else if (m_iDirectApproveResult == DirectApproveDialog.PF_APPROVE_RETURN) {// 驳回
					doReturn(applyVO);
				}else if(m_iDirectApproveResult == UIDialog.ID_CANCEL){
					return;
				}
				super.doDirectApprove(hrAggVO, approveDlg.getApproveNote(), m_iDirectApproveResult);
			}
			// 从数据库中得到更新后的单据
			curSelectedVO = TRNDelegator.getRegularappl()
					.findRegularapplHByPrimaryKey(curSelectedVO.getPrimaryKey());			
			// 同步列表和卡片上的单据状态
			mainPanel.getMainBillListPanel().setSelectedHeadData("ibillstate", curSelectedVO.getIbillstate());
			mainPanel.getMainBillCardPanel().getBillCardPanel().getBillData().setHeaderValueVO(curSelectedVO);
            getFrameUI().showHintMessage(
                    NCLangRes4VoTransl.getNCLangRes().getStrByID("nc_hr_pf", "UPPnc_hr_pf_000002"));// 已完成审批操作！
			if (curSelectedVO.getIbillstate() == IBillStatus.CHECKPASS) {// 如果当前单据是审批通过状态 
				
				// 转正审批通过后同步至中间表 by river for 2011-09-14
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
								System.out.println("<<<<<<  人员档案修改线程停止！ >>>>>>");
								System.out.println("<<<<<<  线程号: " + this.getId() + " >>>>>>");
								this.stop();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}.start();
				}
				
				// 同步操作 end
				
				// 审批通过后提示是否发送通知邮件,若选择是,则执行发送通知按钮操作
				if (mainPanel.getFrameUI().showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("hr_trn_pub","UPPhr_trn_pub-000000")/*@res "是否发送通知邮件"*/) == UIDialog.ID_YES)
					mainPanel.getFrameUI().getBtnManager().execCmd(TrnBtnReg.BTN_SENDOUT);
				
			}
			//同步缓存的单据 liudan
			applyVO.setParentVO(curSelectedVO);
			mainPanel.getHashTranslateRegularBill().put(curSelectedVO.getPrimaryKey(), applyVO);
		} catch (Exception e) {
			mainPanel.reportException(e);
			mainPanel.getTrnApprovePanel().showErrorMessage(e.getMessage());
		} finally {
			if (curSelectedVO != null && !isLocked) {// 解锁单据
				mainPanel.freeLockBill(curSelectedVO.getPk_rm_apply_h());
			}
		}
	}

	/**
	 * 设置审批意见 创建日期(2007-8-14 下午04:24:16)
	 * @param newOpinion
	 */
	protected void setCensorOpinion(String newOpinion) {
		opinion = newOpinion;
	}

	/**
	 * 审批通过(直批) 创建日期(2007-6-1 下午04:34:20)
	 * @return boolean
	 */
	public boolean doApprove(RegularApplyExAggVO applyVO) {
		try {
			RegularapplHVO headerVO = applyVO.getHeaderVO();
			headerVO.setAttributeValue("vopinion", opinion);// 设置审批意见
			headerVO.setIbillstate(new Integer(1));// 设置单据状态为已批准
			PFAggVO aggVO = new PFAggVO();
			aggVO.setParentVO(headerVO);
			// 取得单据上的人员数组
			String[] pk_psndocs = applyVO.getPk_psndocs();
			// 将是否同意为true的人员添加到Vector v中
			// (每个人只添加一条记录，供推动薪资订调资单据使用，审批流只使用aggVO的主表VO)
			Vector<RegularapplBVO> v = new Vector<RegularapplBVO>();
			for (String i : pk_psndocs) {
				RegularapplBVO[] bodyVOs = applyVO.getItemVOsByPK(i);
				for (RegularapplBVO j : bodyVOs) {
					if (j.getPk_flddict().equalsIgnoreCase("isapprove"))
						if (UFBoolean.valueOf(j.getVvalue().trim()).booleanValue())
							v.addElement(j);

				}
			}
			// 取得是否同意为true的人员的子VO(每个人只有一条记录)
			RegularapplBVO[] approvedBodyVO = new RegularapplBVO[v.size()];
			if (v.size() > 0)
				v.copyInto(approvedBodyVO);
			else
				approvedBodyVO = null;
			aggVO.setChildrenVO(approvedBodyVO);

			// 执行审批操作
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
	 * 审批不通过(直批) 创建日期(2007-6-1 下午04:40:18)
	 */
	public boolean doUnApprove(RegularApplyExAggVO applyVO) {
		try {
			RegularapplHVO headerVO = applyVO.getHeaderVO();
			headerVO.setAttributeValue("vopinion", opinion);// 设置审批意见
			headerVO.setIbillstate(new Integer(0));// 设置单据状态为审批未通过
			// 更新数据库
			TRNDelegator.getRegularappl().rejectLocalBillForCensor(applyVO,
					mainPanel.getUserID());

//			 用原来的审批方法在pub_workflownote表中插入记录
//			String censor = mainPanel.getUserID();
//			TRNDelegator.getRegularappl().approveLocalBillForCensor(applyVO,
//					censor);
		} catch (Exception e) {
			mainPanel.reportException(e);
			mainPanel.getTrnApprovePanel().showErrorMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("600904",
							"UPP600904-000048")/* @res "弃审异常" */);
			return false;
		}
		return true;
	}

	/**
	 * 驳回(直批) 创建日期(2007-8-10 上午09:48:48)
	 * @param applyVO
	 * @return boolean
	 */
	public boolean doReturn(RegularApplyExAggVO applyVO) {
		RegularapplHVO headerVO = applyVO.getHeaderVO();
		headerVO.setIbillstate(8);// 设置单据状态为编写中
		// 更新数据库
		try {
			TRNDelegator.getRegularappl().returnLocalBillForCensor(applyVO);

//			 用原来的审批方法在pub_workflownote表中插入记录
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
	 * 当前单据是否可以审批（考虑并发互斥，是否被别人提交或删除) 创建日期(2007-6-1 下午04:35:09)
	 * @return boolean
	 */
	public boolean isApproveable(RegularApplyExAggVO applyVO) {
		try {
			RegularapplHVO headerVO = applyVO.getHeaderVO();
			if (headerVO == null) {
				MessageDialog
						.showHintDlg(mainPanel, nc.ui.ml.NCLangRes
								.getInstance().getStrByID("600904",
										"UPP600904-000036")/* @res "提示" */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"600904", "UPP600904-000074")/*
																		 * @res
																		 * "此单据已经被删除，请刷新后再操作"
																		 */);
				return false;
			} else if ((headerVO.getIbillstate().intValue() == IBillStatus.NOPASS)
					|| (headerVO.getIbillstate().intValue() == IBillStatus.CHECKPASS)) {
				MessageDialog
						.showHintDlg(mainPanel, nc.ui.ml.NCLangRes
								.getInstance().getStrByID("600904",
										"UPP600904-000036")/* @res "提示" */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"600904", "UPP600904-000075")/*
																		 * @res
																		 * "此单据已经被审批，请刷新后再操作"
																		 */);
				return false;
			} else if (headerVO.getIbillstate().intValue() == IBillStatus.FREE) {
				MessageDialog
						.showHintDlg(mainPanel, nc.ui.ml.NCLangRes
								.getInstance().getStrByID("600904",
										"UPP600904-000036")/* @res "提示" */,
								nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"600904", "UPP600904-000076")/*
																		 * @res
																		 * "此单据尚未提交，请刷新后再操作"
																		 */);
				return false;
			}

			String[] pk_psndocs = applyVO.getPk_psndocs();// 得到申请单上的所有人员PK
			if (!(pk_psndocs.length > 0)) {// 如果申请单上没有人员,给出提示
				getFrameUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("600904",
								"UPP600904-000114")/* @res "申请单上没有人员" */);
				return false;
			}
			// 判断子表VO数组中是否含有部门以及岗位两个项目(默认不包含)
			boolean isIncludeDept = false;
			boolean isIncludeJob = false;
			// 只查询聚合VO中一个人的子表VO即可
			RegularapplBVO[] tempBodyVO = applyVO.getItemVOsByPK(pk_psndocs[0]);
			for (RegularapplBVO i : tempBodyVO) {// 循环子表数据
				if (i.getPk_flddict().equalsIgnoreCase("pk_deptdoc"))
					isIncludeDept = true;
				else if (i.getPk_flddict().equalsIgnoreCase("pk_om_job"))
					isIncludeJob = true;
			}

			// 根据人员主键查询相对应的人员是否已经转正
			java.util.Map isRegular=TRNDelegator.getRegularappl().queryRegularByPK(pk_psndocs);
			Vector<GeneralVO> oldPsnData=new Vector<GeneralVO>();// 转正前人员信息
			Vector<GeneralVO> newPsnData=new Vector<GeneralVO>();// 转正后人员信息
			// 循环人员,检查同意转正的人员是否已经转正,同时得到人员类别发生变化的同意转正的人员数组
			for (int i = 0; i < pk_psndocs.length; i++) {
				boolean isapprove = false;// 是否同意转正(默认为同意)
				// 得到单据上指定人员PK的子表数据数组
				RegularapplBVO[] bodyVO = applyVO.getItemVOsByPK(pk_psndocs[i]);
				String psnname = null;
				// 得到当前人员转正前后的信息(只取出人员姓名、转正前后人员类别、所在部门、岗位，供判断是否超编使用)
				GeneralVO oldPsnVO=new GeneralVO();
				GeneralVO newPsnVO=new GeneralVO();
				for (RegularapplBVO j : bodyVO) {
					// 得到是否同意
					if (j.getPk_flddict().equalsIgnoreCase("isapprove")) {
						if (UFBoolean.valueOf(j.getVvalue().trim()).booleanValue()) {
							isapprove = true;// 同意转正
						}
					}
					// 得到人员姓名
					else if (j.getPk_flddict().equalsIgnoreCase("psnname"))
						psnname = j.getVvalue();
					// 得到转正前人员类别
					else if (j.getPk_flddict().equalsIgnoreCase("pk_currpsncl"))
						oldPsnVO.setAttributeValue("pk_psncl", j.getVvalue());
					// 得到转正后人员类别
					else if (j.getPk_flddict().equalsIgnoreCase("pk_newpsncl"))
						newPsnVO.setAttributeValue("pk_psncl", j.getVvalue());
					// 取得当前人员所在部门的PK存放入newPsnVO和oldPsnVO中
					else if (j.getPk_flddict().equalsIgnoreCase("pk_deptdoc")) {
						newPsnVO.setAttributeValue("pk_deptdoc", j.getVvalue());
						oldPsnVO.setAttributeValue("pk_deptdoc", j.getVvalue());
					}// 取得当前人员所属岗位的PK存放入newPsnVO和oldPsnVO中
					else if (j.getPk_flddict().equalsIgnoreCase("pk_om_job")) {
						newPsnVO.setAttributeValue("pk_om_job", j.getVvalue());
						oldPsnVO.setAttributeValue("pk_om_job", j.getVvalue());
					}
				}

				if (isapprove) {// 如果同意转正
					//如果此人已经转正,给出提示,返回
					if (isRegular.get(pk_psndocs[i]).toString().equalsIgnoreCase("Y")) {
						getFrameUI().showWarningMessage(psnname
								+ nc.ui.ml.NCLangRes.getInstance()
								.getStrByID("600904", "UPP600904-000115")/* @res "已经转正，请修改后再审批" */);
						return false;
					}
					// 如果此人的转正前后人员类别发生变化,将转正前后的信息分别存放入Vector中
					if (!(oldPsnVO.getAttributeValue("pk_psncl").toString())
							.equalsIgnoreCase((newPsnVO
									.getAttributeValue("pk_psncl")).toString())) {
						oldPsnVO.setAttributeValue("pk_psndoc", pk_psndocs[i]);
						newPsnVO.setAttributeValue("pk_psndoc", pk_psndocs[i]);
						// 如果在转正项目中未设置部门或者岗位，需要从数据库中查出数据，放入oldPsnVO和newPsnVO中
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
			// 得到同意转正且人员类别发生变化的人员转正前后的信息数组
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
			// 如果oldPsnVOs和newPsnVOs都不为空,检查是否超编
			if (oldPsnVOs != null && newPsnVOs != null) {
				boolean isExceed = mainPanel.isExceedWorkout(oldPsnVOs, newPsnVOs);
				// 如果超编且强制不通过则返回
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
		int iSelection = mainPanel.getCurSelectRow();// 得到当前选择行

		curSelectedVO = (RegularapplHVO) mainPanel.getMainBillListPanel()
				.getBillListPanel().getHeadBillModel().getBodyValueRowVO(
						iSelection, RegularapplHVO.class.getName());

		if (iSelection < -1 || curSelectedVO == null) {
			throw new ValidationException(getResource("nc_hr_pf",
					"UPPnc_hr_pf_000000"));// 请先选择数据！
		}
		curSelectedVO.setPk_billtype("BA");

		applyVO = new RegularApplyExAggVO();

		// 得到复合VO
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
					"UPPnc_hr_pf_000042"));// 数据已经被修改，请先刷新再操作！
		}

		Integer intApproveState = (Integer) curSelectedVO.getAttributeValue(pfConfig
				.getApproveStateFieldCode());

		// 如果是审批通过、审批未通过的状态，就不能审批
		if (intApproveState != null && IBillStatus.COMMIT != intApproveState
				&& IBillStatus.CHECKGOING != intApproveState) {
			throw new ValidationException(getResource("nc_hr_pf",
					"UPPnc_hr_pf_000003"));// 审批流尚未开始或者已经结束，不能进行审批！
		}

		if (!dataModel.isDirectApprove(curSelectedVO.getPrimaryKey())
				&& !dataModel.isCheckman(curSelectedVO.getPrimaryKey(), "BA")) {
			throw new ValidationException(getResource("nc_hr_pf",
					"UPPnc_hr_pf_000029"));
			/** "您不是单据的当前审批人！" */
		}
		headVO = curSelectedVO;
		hrAggVO = new HRAggVO();
		hrAggVO.setParentVO(curSelectedVO);
		return true;
	}

}