package nc.bs.pub.compiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pf.IPfPersonFilter2;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.pub.pf.busistate.AbstractBusiStateCallback;
import nc.bs.pub.pf.busistate.PFBusiState;
import nc.bs.pub.pf.busistate.PFBusiStateOfMeta;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.pf.IPFMessage;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.sf.IServiceProviderSerivce;
import nc.itf.yto.blacklist.IblkQueryFunc;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.change.PublicHeadVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.msg.MessageTypes;
import nc.vo.pub.msg.MessageinfoVO;
import nc.vo.pub.pf.CurrencyInfo;
import nc.vo.pub.pf.IPFConfigInfo;
import nc.vo.pub.pf.IPfRetBackCheckInfo;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.pub.pf.PfUtilWorkFlowVO;
import nc.vo.pub.pf.Pfi18nTools;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.pfflow01.BillbusinessVO;
import nc.vo.sm.UserVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.uap.pf.RetBackWfVo;
import nc.vo.uap.rbac.RoleVO;
import nc.vo.wfengine.definition.IApproveflowConst;
import nc.vo.wfengine.pub.WFTask;
import nc.vo.yto.blacklist.HiPsndocBadAppBVO;
import nc.vo.yto.blacklist.MyBillVO;

/**
 * 实现平台编程运行环境接口的类
 * 
 * @author 樊冠军 2002-2-28 
 * @modifier leijun 2005-3-14 修改方法procActionFlow()和procFlowBacth(),如果为制单即审批通过,则不处理审批流
 * @modifier leijun 2005-6-20 修改方法runClass(),完全委托给PfUtilTools
 * @modifier leijun 2006-5-30 单据审批通过后需要发送"拉式"消息
 */
public class AbstractCompiler implements IPfRun, ICodeRemark {

	public AbstractCompiler() {
		super();
	}

	/**
	 * 执行审批通过的状态 
	 */
	private void execApprovePass(PfParameterVO paraVo) throws Exception {
		try {
			if (!paraVo.m_actionName.endsWith(IPFActionName.APPROVE))
				return;

			boolean hasMeta = PfMetadataTools.isBilltypeRelatedMeta(paraVo.m_billType);
			AbstractBusiStateCallback absc = hasMeta ? new PFBusiStateOfMeta() : new PFBusiState();
			absc.execApproveState(paraVo, IPfRetCheckInfo.PASSING);

		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	/**
	 * 从主表VO中获取一些数据，比如billId和billNo等
	 */
	public void getHeadInfo(PfParameterVO paraVo) {
		// 获取单据主实体中平台相关信息
		PublicHeadVO standHeadVo = PfUtilBaseTools.fetchHeadVO(paraVo.m_preValueVo, paraVo.m_billType);
		if (StringUtil.isEmptyWithTrim(standHeadVo.businessType))
			paraVo.m_businessType = IPFConfigInfo.STATEBUSINESSTYPE;
		else
			paraVo.m_businessType = standHeadVo.businessType;
		paraVo.m_billNo = standHeadVo.billNo;
		paraVo.m_billId = standHeadVo.pkBillId;
		paraVo.m_coId = standHeadVo.corpId;
		paraVo.m_makeBillOperator = standHeadVo.operatorId;
	}

	/**
	 * 获得参数数据，从平台传入的VO[]中从新获得  
	 */
	public void copyParaVo(PfParameterVO vo, PfParameterVO inVo) {
		vo.m_actionName = inVo.m_actionName;
		vo.m_billType = inVo.m_billType;
		vo.m_businessType = inVo.m_businessType;
		vo.m_actionName = inVo.m_actionName;
		vo.m_billNo = inVo.m_billNo;
		vo.m_makeBillOperator = inVo.m_makeBillOperator;
		vo.m_operator = inVo.m_operator;
		vo.m_currentDate = inVo.m_currentDate;
		vo.m_coId = inVo.m_coId;
		vo.m_billId = inVo.m_billId;
		// 产品组自定义对象
		vo.m_userObj = inVo.m_userObj;
		// 原传入Vo值
		vo.m_preValueVo = inVo.m_preValueVo;
		// 当前数据源
		//vo.m_dataSource = inVo.m_dataSource;
	}

	/**
	 * 被审批"APPROVE"动作脚本调用,实现对单个单据的审批
	 * <li>也可调用procFlowBatch()来实现对单个单据的审批
	 * 
	 * @return Object 如果为流程审批进行中的审批,则返回IWorkFlowRet; 如果为导致流程审批通过的审批,则返回null.
	 */
	public Object procActionFlow(PfParameterVO paraVo) throws Exception {
		IWorkFlowRet retObj = null;

		// lj@ 2005-3-14 如果是制单即审批通过,则不处理工作流
		// if (paraVo.m_workFlow != null)
		if (paraVo.m_workFlow != null && !paraVo.m_autoApproveAfterCommit) {
			// 处理工作流
			int intFlag = processWorkFlow(paraVo);
			if (intFlag != IPfRetCheckInfo.PASSING) {
				
				if(intFlag == 0 || intFlag == -1) {
					if(paraVo.m_billType.equals("blk") && paraVo.m_actionName.equals("APPROVE")) {
						HiPsndocBadAppBVO[] badapps = (HiPsndocBadAppBVO[])((MyBillVO[])paraVo.m_preValueVos)[0].getChildrenVO();
						IblkQueryFunc blkQuery = (IblkQueryFunc) NCLocator.getInstance().lookup(IblkQueryFunc.class.getName());
						
						for(HiPsndocBadAppBVO badapp : badapps) {
							try{
								blkQuery.delPsndocBad(badapp.getPk_psnbasdoc(), "yt_test");		
							} catch (Exception e) {
								continue;
							}
						}
					}
				}
				
				// 未通过或进行中的处理
				retObj = new IWorkFlowRet();
				retObj.m_inVo = paraVo.m_preValueVo;
				return retObj;
			} else {
				if(paraVo.m_billType.equals("blk") && paraVo.m_actionName.equals("APPROVE")) {
					HiPsndocBadAppBVO[] badapps = (HiPsndocBadAppBVO[])((MyBillVO[])paraVo.m_preValueVos)[0].getChildrenVO();
					IblkQueryFunc blkQuery = (IblkQueryFunc) NCLocator.getInstance().lookup(IblkQueryFunc.class.getName());
					
					for(HiPsndocBadAppBVO badapp : badapps) {
						try{
							String str = blkQuery.getStr("select 1 from hi_psndoc_bad where id = '"+badapp.getId()+"'", "yt_test");
							if(!"1".equals(str)) {
								String permanentres = badapp.getAttributeValue("permanentres").toString();
								
//								if(permanentres.split("县").length > 1)
//									permanentres = permanentres.split("县")[0] + "县";
//								
//								if(permanentres.split("区").length > 1)
//									permanentres = permanentres.split("区")[0] + "区";
								
								
								blkQuery.insertPsndocBad(badapp, "yt_test"); 
							}
								
						} catch (Exception e) {
							continue;
						}
					}
				}

				//通过后的处理-发送"拉式"消息 lj+2006-5-30
				insertPullWorkitems(paraVo);
			}
		} else {
			// 执行审核通过的组件
			execApprovePass(paraVo);
			//通过后的处理-发送"拉式"消息 lj+2006-5-30
			insertPullWorkitems(paraVo);
		}
		return retObj;
	}

	/**
	 * 发送"拉式"消息
	 * @param paraVo
	 */
	private void insertPullWorkitems(PfParameterVO paravo) {
		Logger.debug(">>审批通过后发送拉式消息=" + paravo.m_billType + "开始");

		//判断是否发送拉式消息
		BillbusinessVO condVO = new BillbusinessVO();
		//condVO.setPk_corp(paravo.m_coId);
		condVO.setPk_businesstype(paravo.m_businessType);
		condVO.setPk_billtype(paravo.m_billType);

		BaseDAO dao = new BaseDAO();
		try {
			Collection co = dao.retrieve(condVO, true);
			if (co.size() > 0) {
				BillbusinessVO vo = (BillbusinessVO) co.iterator().next();
				UFBoolean isMsg = vo.getForwardmsgflag();
				if (isMsg == null || !isMsg.booleanValue()) {
					Logger.debug(">>源单据" + paravo.m_billType + "不可发送下游消息，返回");
					return;
				}
			}
		} catch (DAOException ex) {
			Logger.error(ex.getMessage(), ex);
			return;
		}

		try {
			//1.查找下游单据类型
			IPFConfig pfcfg = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());
			BillbusinessVO[] billbusiVOs = pfcfg.queryBillDest(paravo.m_billType, paravo.m_businessType);
			if (billbusiVOs == null || billbusiVOs.length == 0) {
				Logger.debug("该业务流程没有为单据" + paravo.m_billType + "配置下游单据，返回");
				return;
			}

			//查找上游单据的过滤器类
			Object checkClzInstance = PfUtilTools.getBizRuleImpl(paravo.m_billType);
			IPfPersonFilter2 filter = null;
			if (checkClzInstance instanceof IPfPersonFilter2)
				filter = (IPfPersonFilter2) checkClzInstance;
			for (int k = 0; k < billbusiVOs.length; k++) {
				BillbusinessVO destBillbusiVO = billbusiVOs[k];
				//2.获得下游单据的参与角色
				String destBillType = destBillbusiVO.getPk_billtype();
				RoleVO[] roles = pfcfg.queryRolesHasBillbusi(destBillbusiVO.getPk_corp(), destBillType,
						destBillbusiVO.getPk_businesstype(), true);
				if (roles == null || roles.length == 0) {
					Logger.debug(">>单据无参与角色，继续");
					continue;
				}

				//3.检查是否有过滤器
				HashSet hsUserPKs = null;
				if (filter == null) {
					//4a.获得角色的所有用户
					hsUserPKs = new HashSet();
					IRoleManageQuery rmq = (IRoleManageQuery) NCLocator.getInstance().lookup(
							IRoleManageQuery.class.getName());
					for (int i = 0; i < (roles == null ? 0 : roles.length); i++) {
						UserVO[] users = rmq.getUsers(roles[i].getPk_role(), roles[i].getPk_corp());
						for (int j = 0; j < (users == null ? 0 : users.length); j++) {
							hsUserPKs.add(users[j].getPrimaryKey());
						}
					}
				} else {
					//4b.过滤器返回的用户PK数组
					hsUserPKs = filter.filterUsers(paravo.m_billType, destBillType, paravo.m_preValueVo,
							roles);
				}

				//5.给这些用户发送"拉式"工作流消息
				ArrayList alItems = new ArrayList();
				for (Iterator iter = hsUserPKs.iterator(); iter.hasNext();) {
					String userId = (String) iter.next();

					MessageinfoVO wi = new MessageinfoVO();
					wi.setPk_billtype(destBillbusiVO.getPk_billtype());
					wi.setBillid(paravo.m_billId); //上游单据ID
					wi.setPk_srcbilltype(paravo.m_billType);
					wi.setBillno(paravo.m_billNo);

					wi.setCheckman(userId);
					//FIXME::i18n
					wi.setTitle(Pfi18nTools.i18nBilltypeName(paravo.m_billType, null) + paravo.m_billNo
							+ "已经审批通过，可从它拉式产生：" + Pfi18nTools.i18nBilltypeName(destBillType, null));
					wi.setPk_busitype(destBillbusiVO.getPk_businesstype());
					wi.setPk_corp(paravo.m_coId);
					wi.setSenderman(paravo.m_operator);
					alItems.add(wi);
				}

				//dao.insertVOList(alItems);
				IPFMessage pfmsg = (IPFMessage) NCLocator.getInstance().lookup(IPFMessage.class.getName());
				pfmsg.insertPushOrPullMsgs((MessageinfoVO[]) alItems.toArray(new MessageinfoVO[alItems
						.size()]), MessageTypes.MSG_TYPE_BUSIFLOW_PULL);

			}
		} catch (Exception e) {
			//WARN::仅仅日志异常，不能影响业务流程
			Logger.error(e.getMessage(), e);
		}
		Logger.debug(">>审批通过后发送拉式消息=" + paravo.m_billType + "结束");
	}

	/**
	 * 处理工作流;并处理单据的审批状态
	 * 
	 * @param paraVo
	 * @throws Exception
	 */
	private int processWorkFlow(PfParameterVO paraVo) throws Exception {
		Logger.info("****处理工作流processWorkFlow开始****");
		int intFlag = 0;
		try {
			// 1.工作流前进
			intFlag = NCLocator.getInstance().lookup(IWorkflowMachine.class).forwardCheckFlow(paraVo);

			// 2.业务状态修改——只有审批流才需要
			WFTask currentTask = paraVo.m_workFlow.getTaskInfo().getTask();
			boolean isWorkflow = IApproveflowConst.WORKFLOW_TYPE_WORKFLOW == currentTask
					.getWorkflowType();
			if (!isWorkflow) {
				boolean hasMeta = PfMetadataTools.isBilltypeRelatedMeta(paraVo.m_billType);
				AbstractBusiStateCallback absc = hasMeta ? new PFBusiStateOfMeta() : new PFBusiState();
				absc.execApproveState(paraVo, intFlag);
			}
		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw e;
			else {
				Throwable exp = e.getCause();
				throw new PFBusinessException(exp == null ? e.getMessage() : exp.getMessage(), e);
			}
		}
		Logger.debug(">>当前单据的审批状态=" + intFlag);
		Logger.info("****处理工作流processWorkFlow结束****");
		return intFlag;
	}

	/**
	 * 被审批"APPROVE"的动作脚本调用,实现批量审批
	 * 
	 * @throws Exception
	 */
	public Hashtable procFlowBacth(PfParameterVO paraVo) throws Exception {
		// 工作流的判断(提供组件进行执行)
		Hashtable retHas = new Hashtable();

		PfUtilWorkFlowVO workFlow = paraVo.m_workFlow;

		// 初始默认数据
		UFBoolean isCheck = UFBoolean.TRUE;
		boolean isCheckPass = true;
		String checkNote = "审批通过";
		IServiceProviderSerivce iServProvider = (IServiceProviderSerivce) NCLocator.getInstance()
				.lookup(IServiceProviderSerivce.class.getName());
		UFDateTime tmpDateTime = iServProvider.getServerTime();
		boolean isBackToFirst = false;
		if (workFlow != null) {
			//获取前台交互的信息，包括驳回信息
			isCheck = workFlow.getIsCheck();
			isCheckPass = workFlow.getIsCheckPass();
			checkNote = workFlow.getCheckNote();
			tmpDateTime = workFlow.getDealDate();
			isBackToFirst = workFlow.getTaskInfo().getTask().isBackToFirstActivity();
		}
		// 是否第一次
		boolean isFirst = true;
		HashMap hmParam = new HashMap();
		for (int i = 0; i < paraVo.m_preValueVos.length; i++) {
			// 重新定义参数
			PfParameterVO tmpParaVo = new PfParameterVO();
			// 复制数据
			copyParaVo(tmpParaVo, paraVo);
			// 当前单据数据
			tmpParaVo.m_preValueVo = paraVo.m_preValueVos[i];

			// 当前传入对象
			if (paraVo.m_userObjs != null && paraVo.m_userObjs.length > 1) {
				tmpParaVo.m_userObj = paraVo.m_userObjs[i];
			}
			// 从单据VO中获取其他数据
			getHeadInfo(tmpParaVo);

			if (workFlow == null && !isFirst) {
				workFlow = NCLocator.getInstance().lookup(IWorkflowMachine.class).checkWorkFlow(
						tmpParaVo.m_actionName + tmpParaVo.m_operator, tmpParaVo.m_billType,
						tmpParaVo.m_currentDate, tmpParaVo.m_preValueVo, hmParam);

				if (workFlow != null) {
					//批量审批时，需要前台交互的信息
					workFlow.setIsCheck(isCheck);
					workFlow.setIsCheckPass(isCheckPass);
					workFlow.setCheckNote(checkNote);
					workFlow.setDealDate(tmpDateTime);
					if (isBackToFirst) {
						workFlow.getTaskInfo().getTask().setTaskType(WFTask.TYPE_BACKWARD);
						workFlow.getTaskInfo().getTask().setBackToFirstActivity(true);
					}
				}
			} else {
				isFirst = false;
			}
			// lj@ 2005-3-14 如果是制单即审批通过,则不处理工作流
			if (workFlow != null && !paraVo.m_autoApproveAfterCommit) {
				// 置入工作流数据
				tmpParaVo.m_workFlow = workFlow;
				int intFlag = processWorkFlow(tmpParaVo);
				if (intFlag != IPfRetCheckInfo.PASSING) {
					// 未通过或进行中的处理
					retHas.put(String.valueOf(i), String.valueOf(i));
					// continue;
				} else {
					// 通过后的处理-发送"拉式"消息 lj+2006-5-30
					insertPullWorkitems(tmpParaVo);
				}
			} else {
				// 执行审核通过的组件
				execApprovePass(tmpParaVo);
				// 通过后的处理-发送"拉式"消息 lj+2006-5-30
				insertPullWorkitems(tmpParaVo);
			}

			//批量审批时，需要获取单据金额信息
			if (workFlow != null && IPFActionName.APPROVE.equals(tmpParaVo.m_actionName)) {
				/* 从单据聚合VO中获得币种、金额数据 */
				CurrencyInfo cinfo = new CurrencyInfo();
				PfUtilBaseTools.fetchMoneyInfo(tmpParaVo.m_preValueVo, cinfo, tmpParaVo.m_billType);
				workFlow.putMoney(tmpParaVo.m_billId, cinfo);
			}
			workFlow = null;
		}// /{end for}
		return retHas;
	}

	/**
	 * 运行类的某个方法,参数格式如下:
	 * <li>输入参数 :"AggressVo:20,pkbillType:String"
	 * <li>输入参数2:"&AggressVo:key,&key:DataType",DataType为其它标准类型
	 * 
	 * @param className
	 * @param method
	 * @param parameter
	 * @param paraVo
	 * @param keyHas
	 * @param methodReturnHas
	 * @return
	 * @throws BusinessException
	 * 
	 * @modifier 雷军 2005-6-20 完全委托PfUtilTools类执行了
	 */
	public Object runClass(String className, String method, String parameter, PfParameterVO paraVo,
			Hashtable keyHas, Hashtable methodReturnHas) throws BusinessException {
		return PfUtilTools.runClass(className, method, parameter, paraVo, keyHas, methodReturnHas);
	}

	/* (non-Javadoc)
	 * @see nc.bs.pub.compiler.IPfRun#runComClass(nc.vo.pub.compiler.PfParameterVO)
	 */
	public Object runComClass(PfParameterVO paraVo) throws BusinessException {
		// Noop!
		return null;
	}

	/**
	 * 被弃审"UNAPPROVE"动作脚本调用,实现对单个单据的弃审
	 * 
	 * @param paraVo
	 * @return true-审批流程由完成态返回到运行态;false-其他情况
	 */
	public boolean procUnApproveFlow(PfParameterVO paraVo) throws BusinessException {
		return unApproveCheckFlow(paraVo);
	}

	/**
	 * 弃审
	 */
	private boolean unApproveCheckFlow(PfParameterVO paraVo) throws BusinessException {
		try {
			//弃审处理 leijun@2008-9
			RetBackWfVo backWfVo = NCLocator.getInstance().lookup(IWorkflowMachine.class).backCheckFlow(
					paraVo);

			// 进行单据状态修改
			unApproveState(paraVo, backWfVo.getBackState(), backWfVo.getPreCheckMan());
			
			if(paraVo.m_billType.equals("blk") && paraVo.m_actionName.equals("UNAPPROVE")) {
				HiPsndocBadAppBVO[] badapps = (HiPsndocBadAppBVO[])((MyBillVO[])paraVo.m_preValueVos)[0].getChildrenVO();
				IblkQueryFunc blkQuery = (IblkQueryFunc) NCLocator.getInstance().lookup(IblkQueryFunc.class.getName());
				
				for(HiPsndocBadAppBVO badapp : badapps) {
					try{
						blkQuery.delPsndocBad(badapp.getPk_psnbasdoc(), "yt_test");		
					} catch (Exception e) {
						continue;
					}
				}
			}
			
			return backWfVo.getIsFinish().booleanValue();
		} catch (BusinessException ex) {
			throw ex;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFBusinessException(ex.getMessage(), ex);
		}
	}

	/**
	 * 修改反审批单据状态 创建日期:(2002-12-9 15:36:11)
	 */
	private void unApproveState(PfParameterVO paraVo, int iBackState, String preCheckMan)
			throws BusinessException {
		Logger.info("****执行单据的逐级反审操作unApproveState开始*****");
		try {
			boolean hasMeta = PfMetadataTools.isBilltypeRelatedMeta(paraVo.m_billType);
			AbstractBusiStateCallback absc = hasMeta ? new PFBusiStateOfMeta() : new PFBusiState();
			switch (iBackState) {
				case IPfRetBackCheckInfo.GOINGON: {
					absc.execUnApproveState(paraVo, preCheckMan, IPfRetBackCheckInfo.GOINGON);
					break;
				}
				case IPfRetBackCheckInfo.NOSTATE: {
					absc.execUnApproveState(paraVo, null, IPfRetBackCheckInfo.NOSTATE);
					break;
				}
			}
		} catch (BusinessException ex) {
			throw ex;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFBusinessException(ex.getMessage(), ex);
		}
		Logger.info("****执行单据的逐级反审操作unApproveState结束*****");
	}

	public String getCodeRemark() {
		return null;
	}
}