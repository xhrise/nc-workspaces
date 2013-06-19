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
 * ʵ��ƽ̨������л����ӿڵ���
 * 
 * @author ���ھ� 2002-2-28 
 * @modifier leijun 2005-3-14 �޸ķ���procActionFlow()��procFlowBacth(),���Ϊ�Ƶ�������ͨ��,�򲻴���������
 * @modifier leijun 2005-6-20 �޸ķ���runClass(),��ȫί�и�PfUtilTools
 * @modifier leijun 2006-5-30 ��������ͨ������Ҫ����"��ʽ"��Ϣ
 */
public class AbstractCompiler implements IPfRun, ICodeRemark {

	public AbstractCompiler() {
		super();
	}

	/**
	 * ִ������ͨ����״̬ 
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
	 * ������VO�л�ȡһЩ���ݣ�����billId��billNo��
	 */
	public void getHeadInfo(PfParameterVO paraVo) {
		// ��ȡ������ʵ����ƽ̨�����Ϣ
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
	 * ��ò������ݣ���ƽ̨�����VO[]�д��»��  
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
		// ��Ʒ���Զ������
		vo.m_userObj = inVo.m_userObj;
		// ԭ����Voֵ
		vo.m_preValueVo = inVo.m_preValueVo;
		// ��ǰ����Դ
		//vo.m_dataSource = inVo.m_dataSource;
	}

	/**
	 * ������"APPROVE"�����ű�����,ʵ�ֶԵ������ݵ�����
	 * <li>Ҳ�ɵ���procFlowBatch()��ʵ�ֶԵ������ݵ�����
	 * 
	 * @return Object ���Ϊ�������������е�����,�򷵻�IWorkFlowRet; ���Ϊ������������ͨ��������,�򷵻�null.
	 */
	public Object procActionFlow(PfParameterVO paraVo) throws Exception {
		IWorkFlowRet retObj = null;

		// lj@ 2005-3-14 ������Ƶ�������ͨ��,�򲻴�������
		// if (paraVo.m_workFlow != null)
		if (paraVo.m_workFlow != null && !paraVo.m_autoApproveAfterCommit) {
			// ��������
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
				
				// δͨ��������еĴ���
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
								
//								if(permanentres.split("��").length > 1)
//									permanentres = permanentres.split("��")[0] + "��";
//								
//								if(permanentres.split("��").length > 1)
//									permanentres = permanentres.split("��")[0] + "��";
								
								
								blkQuery.insertPsndocBad(badapp, "yt_test"); 
							}
								
						} catch (Exception e) {
							continue;
						}
					}
				}

				//ͨ����Ĵ���-����"��ʽ"��Ϣ lj+2006-5-30
				insertPullWorkitems(paraVo);
			}
		} else {
			// ִ�����ͨ�������
			execApprovePass(paraVo);
			//ͨ����Ĵ���-����"��ʽ"��Ϣ lj+2006-5-30
			insertPullWorkitems(paraVo);
		}
		return retObj;
	}

	/**
	 * ����"��ʽ"��Ϣ
	 * @param paraVo
	 */
	private void insertPullWorkitems(PfParameterVO paravo) {
		Logger.debug(">>����ͨ��������ʽ��Ϣ=" + paravo.m_billType + "��ʼ");

		//�ж��Ƿ�����ʽ��Ϣ
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
					Logger.debug(">>Դ����" + paravo.m_billType + "���ɷ���������Ϣ������");
					return;
				}
			}
		} catch (DAOException ex) {
			Logger.error(ex.getMessage(), ex);
			return;
		}

		try {
			//1.�������ε�������
			IPFConfig pfcfg = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());
			BillbusinessVO[] billbusiVOs = pfcfg.queryBillDest(paravo.m_billType, paravo.m_businessType);
			if (billbusiVOs == null || billbusiVOs.length == 0) {
				Logger.debug("��ҵ������û��Ϊ����" + paravo.m_billType + "�������ε��ݣ�����");
				return;
			}

			//�������ε��ݵĹ�������
			Object checkClzInstance = PfUtilTools.getBizRuleImpl(paravo.m_billType);
			IPfPersonFilter2 filter = null;
			if (checkClzInstance instanceof IPfPersonFilter2)
				filter = (IPfPersonFilter2) checkClzInstance;
			for (int k = 0; k < billbusiVOs.length; k++) {
				BillbusinessVO destBillbusiVO = billbusiVOs[k];
				//2.������ε��ݵĲ����ɫ
				String destBillType = destBillbusiVO.getPk_billtype();
				RoleVO[] roles = pfcfg.queryRolesHasBillbusi(destBillbusiVO.getPk_corp(), destBillType,
						destBillbusiVO.getPk_businesstype(), true);
				if (roles == null || roles.length == 0) {
					Logger.debug(">>�����޲����ɫ������");
					continue;
				}

				//3.����Ƿ��й�����
				HashSet hsUserPKs = null;
				if (filter == null) {
					//4a.��ý�ɫ�������û�
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
					//4b.���������ص��û�PK����
					hsUserPKs = filter.filterUsers(paravo.m_billType, destBillType, paravo.m_preValueVo,
							roles);
				}

				//5.����Щ�û�����"��ʽ"��������Ϣ
				ArrayList alItems = new ArrayList();
				for (Iterator iter = hsUserPKs.iterator(); iter.hasNext();) {
					String userId = (String) iter.next();

					MessageinfoVO wi = new MessageinfoVO();
					wi.setPk_billtype(destBillbusiVO.getPk_billtype());
					wi.setBillid(paravo.m_billId); //���ε���ID
					wi.setPk_srcbilltype(paravo.m_billType);
					wi.setBillno(paravo.m_billNo);

					wi.setCheckman(userId);
					//FIXME::i18n
					wi.setTitle(Pfi18nTools.i18nBilltypeName(paravo.m_billType, null) + paravo.m_billNo
							+ "�Ѿ�����ͨ�����ɴ�����ʽ������" + Pfi18nTools.i18nBilltypeName(destBillType, null));
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
			//WARN::������־�쳣������Ӱ��ҵ������
			Logger.error(e.getMessage(), e);
		}
		Logger.debug(">>����ͨ��������ʽ��Ϣ=" + paravo.m_billType + "����");
	}

	/**
	 * ��������;�������ݵ�����״̬
	 * 
	 * @param paraVo
	 * @throws Exception
	 */
	private int processWorkFlow(PfParameterVO paraVo) throws Exception {
		Logger.info("****��������processWorkFlow��ʼ****");
		int intFlag = 0;
		try {
			// 1.������ǰ��
			intFlag = NCLocator.getInstance().lookup(IWorkflowMachine.class).forwardCheckFlow(paraVo);

			// 2.ҵ��״̬�޸ġ���ֻ������������Ҫ
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
		Logger.debug(">>��ǰ���ݵ�����״̬=" + intFlag);
		Logger.info("****��������processWorkFlow����****");
		return intFlag;
	}

	/**
	 * ������"APPROVE"�Ķ����ű�����,ʵ����������
	 * 
	 * @throws Exception
	 */
	public Hashtable procFlowBacth(PfParameterVO paraVo) throws Exception {
		// ���������ж�(�ṩ�������ִ��)
		Hashtable retHas = new Hashtable();

		PfUtilWorkFlowVO workFlow = paraVo.m_workFlow;

		// ��ʼĬ������
		UFBoolean isCheck = UFBoolean.TRUE;
		boolean isCheckPass = true;
		String checkNote = "����ͨ��";
		IServiceProviderSerivce iServProvider = (IServiceProviderSerivce) NCLocator.getInstance()
				.lookup(IServiceProviderSerivce.class.getName());
		UFDateTime tmpDateTime = iServProvider.getServerTime();
		boolean isBackToFirst = false;
		if (workFlow != null) {
			//��ȡǰ̨��������Ϣ������������Ϣ
			isCheck = workFlow.getIsCheck();
			isCheckPass = workFlow.getIsCheckPass();
			checkNote = workFlow.getCheckNote();
			tmpDateTime = workFlow.getDealDate();
			isBackToFirst = workFlow.getTaskInfo().getTask().isBackToFirstActivity();
		}
		// �Ƿ��һ��
		boolean isFirst = true;
		HashMap hmParam = new HashMap();
		for (int i = 0; i < paraVo.m_preValueVos.length; i++) {
			// ���¶������
			PfParameterVO tmpParaVo = new PfParameterVO();
			// ��������
			copyParaVo(tmpParaVo, paraVo);
			// ��ǰ��������
			tmpParaVo.m_preValueVo = paraVo.m_preValueVos[i];

			// ��ǰ�������
			if (paraVo.m_userObjs != null && paraVo.m_userObjs.length > 1) {
				tmpParaVo.m_userObj = paraVo.m_userObjs[i];
			}
			// �ӵ���VO�л�ȡ��������
			getHeadInfo(tmpParaVo);

			if (workFlow == null && !isFirst) {
				workFlow = NCLocator.getInstance().lookup(IWorkflowMachine.class).checkWorkFlow(
						tmpParaVo.m_actionName + tmpParaVo.m_operator, tmpParaVo.m_billType,
						tmpParaVo.m_currentDate, tmpParaVo.m_preValueVo, hmParam);

				if (workFlow != null) {
					//��������ʱ����Ҫǰ̨��������Ϣ
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
			// lj@ 2005-3-14 ������Ƶ�������ͨ��,�򲻴�������
			if (workFlow != null && !paraVo.m_autoApproveAfterCommit) {
				// ���빤��������
				tmpParaVo.m_workFlow = workFlow;
				int intFlag = processWorkFlow(tmpParaVo);
				if (intFlag != IPfRetCheckInfo.PASSING) {
					// δͨ��������еĴ���
					retHas.put(String.valueOf(i), String.valueOf(i));
					// continue;
				} else {
					// ͨ����Ĵ���-����"��ʽ"��Ϣ lj+2006-5-30
					insertPullWorkitems(tmpParaVo);
				}
			} else {
				// ִ�����ͨ�������
				execApprovePass(tmpParaVo);
				// ͨ����Ĵ���-����"��ʽ"��Ϣ lj+2006-5-30
				insertPullWorkitems(tmpParaVo);
			}

			//��������ʱ����Ҫ��ȡ���ݽ����Ϣ
			if (workFlow != null && IPFActionName.APPROVE.equals(tmpParaVo.m_actionName)) {
				/* �ӵ��ݾۺ�VO�л�ñ��֡�������� */
				CurrencyInfo cinfo = new CurrencyInfo();
				PfUtilBaseTools.fetchMoneyInfo(tmpParaVo.m_preValueVo, cinfo, tmpParaVo.m_billType);
				workFlow.putMoney(tmpParaVo.m_billId, cinfo);
			}
			workFlow = null;
		}// /{end for}
		return retHas;
	}

	/**
	 * �������ĳ������,������ʽ����:
	 * <li>������� :"AggressVo:20,pkbillType:String"
	 * <li>�������2:"&AggressVo:key,&key:DataType",DataTypeΪ������׼����
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
	 * @modifier �׾� 2005-6-20 ��ȫί��PfUtilTools��ִ����
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
	 * ������"UNAPPROVE"�����ű�����,ʵ�ֶԵ������ݵ�����
	 * 
	 * @param paraVo
	 * @return true-�������������̬���ص�����̬;false-�������
	 */
	public boolean procUnApproveFlow(PfParameterVO paraVo) throws BusinessException {
		return unApproveCheckFlow(paraVo);
	}

	/**
	 * ����
	 */
	private boolean unApproveCheckFlow(PfParameterVO paraVo) throws BusinessException {
		try {
			//������ leijun@2008-9
			RetBackWfVo backWfVo = NCLocator.getInstance().lookup(IWorkflowMachine.class).backCheckFlow(
					paraVo);

			// ���е���״̬�޸�
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
	 * �޸ķ���������״̬ ��������:(2002-12-9 15:36:11)
	 */
	private void unApproveState(PfParameterVO paraVo, int iBackState, String preCheckMan)
			throws BusinessException {
		Logger.info("****ִ�е��ݵ��𼶷������unApproveState��ʼ*****");
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
		Logger.info("****ִ�е��ݵ��𼶷������unApproveState����*****");
	}

	public String getCodeRemark() {
		return null;
	}
}