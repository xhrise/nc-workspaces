package nc.ui.pub.pf;

import java.awt.Container;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.itf.uap.pf.IplatFormEntry;
import nc.itf.uap.rbac.function.IFuncPower;
import nc.uap.pf.metadata.ActionClientParams;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.pub.pf.dispatch.WFStartDispatchDialog;
import nc.ui.pub.pf.dispatch.WFWorkitemAcceptDlg;
import nc.ui.querytemplate.IBillReferQuery;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.ui.querytemplate.meta.FilterMeta;
import nc.ui.querytemplate.valueeditor.IFieldValueElementEditor;
import nc.ui.querytemplate.valueeditor.IFieldValueElementEditorFactory;
import nc.ui.querytemplate.valueeditor.RefElementEditor;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtobill.BilltobillreferVO;
import nc.vo.pub.pf.IPfRetException;
import nc.vo.pub.pf.IPfRetExceptionStyle;
import nc.vo.pub.pf.IWorkFlowStatus;
import nc.vo.pub.pf.PfUtilActionVO;
import nc.vo.pub.pf.PfUtilBillActionVO;
import nc.vo.pub.pf.PfUtilWorkFlowVO;
import nc.vo.pub.pf.Pfi18nTools;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.pfflow01.BillbusinessVO;
import nc.vo.pub.template.ITemplateStyle;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.uap.pf.PFRuntimeException;
import nc.vo.uap.rbac.power.PowerResultVO;
import nc.vo.wfengine.definition.IApproveflowConst;
import nc.vo.wfengine.pub.WFTask;

/**
 * ����ƽ̨�ͻ��˹�����
 * 
 * @author fangj 2001-10
 * @modifier leijun 2005-5 ȡ����������UI����������<Y>��ͷ�ſ�ָ�ɵ�����
 * @modifier leijun 2006-7 ����ʱ��ָ�ɶԻ�������û����ȡ����������
 * @modifier leijun 2007-5 ʹ���µĲ�ѯģ��
 * @modifier leijun 2008-3 �ع����������API����һ������
 */
public class PfUtilClient_EH {

	/**
	 * �����������������true��֮false;
	 */
	private static boolean m_checkFlag = true;

	// ��ǰ��������
	private static String m_currentBillType = null;

	/** ��ǰ�����ڵ��������� */
	private static int m_iCheckResult = IApproveflowConst.CHECK_RESULT_PASS;

	private static boolean m_isOk = false;

	/** �ж�VO�Ƿ����ת�� */
	private static boolean m_isRetChangeVo = false;

	/** fgj2001-11-27 �жϵ�ǰ�����Ƿ�ִ�гɹ� */
	private static boolean m_isSuccess = true;

	/** Դ�������� */
	private static String m_sourceBillType = null;

	private static AggregatedValueObject m_tmpRetVo = null;

	private static AggregatedValueObject[] m_tmpRetVos = null;

	// �������Ʊ�־
	public static boolean makeFlag = false;

	private PfUtilClient_EH() {
		// Noop!
	}

	/**
	 * �ύ����ʱ,��Ҫ��ָ����Ϣ
	 * <li>ֻ��"SAVE","EDIT"�����ŵ���
	 */
	private static PfUtilWorkFlowVO checkOnSave(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject billVo, Stack dlgResult, HashMap hmPfExParams)
			throws BusinessException {
		PfUtilWorkFlowVO wfVo = NCLocator.getInstance().lookup(IWorkflowMachine.class)
				.checkWorkitemOnSave(actionName, billType, currentDate, billVo, hmPfExParams);

		if (wfVo != null) {
			// �õ���ָ�ɵ���������
			Vector assignInfos = wfVo.getTaskInfo().getAssignableInfos();
			if (assignInfos != null && assignInfos.size() > 0) {
				// ��ʾָ�ɶԻ����ռ�ʵ��ָ����Ϣ
				DispatchDialog dd = new DispatchDialog(parent);
				dd.initByWorkflowVO(wfVo);
				int iClose = dd.showModal();
				if (iClose == UIDialog.ID_CANCEL)
					dlgResult.push(new Integer(iClose));
			}
		}
		return wfVo;
	}

	/**
	 * ��������������ʱ,��Ҫ��ָ����Ϣ
	 * <li>����ѡ���̻�����ߡ�ѡ���̷�֧ת��
	 */
	private static PfUtilWorkFlowVO checkOnStart(Container parent, String actionName,
			String billType, String currentDate, AggregatedValueObject billVo, Stack dlgResult,
			HashMap hmPfExParams) throws BusinessException {
		PfUtilWorkFlowVO wfVo = NCLocator.getInstance().lookup(IWorkflowMachine.class)
				.checkWorkitemOnSave(actionName, billType, currentDate, billVo, hmPfExParams);

		if (wfVo != null) {
			// �õ���ָ�ɵ���Ϣ
			Vector assignInfos = wfVo.getTaskInfo().getAssignableInfos();
			Vector tSelectInfos = wfVo.getTaskInfo().getTransitionSelectableInfos();
			if (assignInfos.size() > 0 || tSelectInfos.size() > 0) {
				// ��ʾָ�ɶԻ����ռ�ʵ��ָ����Ϣ
				WFStartDispatchDialog wfdd = new WFStartDispatchDialog(parent, wfVo, billVo);
				int iClose = wfdd.showModal();
				if (iClose == UIDialog.ID_CANCEL)
					dlgResult.push(new Integer(iClose));
			}
		}
		return wfVo;
	}

	/**
	 * ��鵱ǰ�����Ƿ��ڹ��������У������н���
	 */
	private static PfUtilWorkFlowVO checkWorkitemWhenSignal(Container parent, String actionName,
			String billType, String currentDate, AggregatedValueObject billVo, HashMap hmPfExParams)
			throws BusinessException {
		PfUtilWorkFlowVO wfVo = null;
		WFWorkitemAcceptDlg clientWorkFlow = null;
		try {
			wfVo = NCLocator.getInstance().lookup(IWorkflowMachine.class).checkWorkFlow(actionName,
					billType, currentDate, billVo, hmPfExParams);
			if (wfVo == null) {
				m_checkFlag = true;
				return wfVo;
			} else {
				String billId = wfVo.getTaskInfo().getTask().getBillID();
				boolean bHasMoney = wfVo.getHmCurrency().get(billId) != null;
				clientWorkFlow = new WFWorkitemAcceptDlg(parent, wfVo, bHasMoney, billVo);

				if (clientWorkFlow.showModal() == UIDialog.ID_OK) {
					// ���ش����Ĺ�����
					m_checkFlag = true;
					wfVo = clientWorkFlow.getWorkFlow();
				} else {
					// �û�ȡ��
					m_checkFlag = false;
					wfVo = null;
				}
			}
		} finally {
			if (clientWorkFlow != null) {
				nc.ui.pub.beans.UIComponentUtil.removeAllComponentRefrence(clientWorkFlow);
			}
		}
		return wfVo;
	}

	/**
	 * ��鵱ǰ�����Ƿ������������У������н���
	 */
	private static PfUtilWorkFlowVO checkWorkitemWhenApprove(Container parent, String actionName,
			String billType, String currentDate, AggregatedValueObject billVo, HashMap hmPfExParams)
			throws BusinessException {
		PfUtilWorkFlowVO wfVo = null;
		WorkFlowCheckDlg_EH clientWorkFlow = null;
		try {
			try {
				wfVo = NCLocator.getInstance().lookup(IWorkflowMachine.class).checkWorkFlow(actionName,
						billType, currentDate, billVo, hmPfExParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (wfVo == null) {
				m_checkFlag = true;
//				return wfVo;
				throw new BusinessException ("ע�⣺��ǰ����δ���������� ��");
			} else {
				String billId = wfVo.getTaskInfo().getTask().getBillID();
				boolean bHasMoney = wfVo.getHmCurrency().get(billId) != null;
				// �޸������ǵĲ���  modify by river for 2011-11-23
				clientWorkFlow = new WorkFlowCheckDlg_EH(parent, wfVo, bHasMoney);
				clientWorkFlow.onRadioRejectClicked();
				clientWorkFlow.onOk();
				if (1 == UIDialog.ID_OK) { // ����û����� // clientWorkFlow.showModal()
					// ���������Ĺ�����
					m_checkFlag = true;
					wfVo = clientWorkFlow.getWorkFlow();
					wfVo.setCheckNote("���ص��Ƶ���");
					
				} else { // �û�������
					m_checkFlag = false;
					wfVo = null;
				}
			}
		} finally {
			if (clientWorkFlow != null) {
				nc.ui.pub.beans.UIComponentUtil.removeAllComponentRefrence(clientWorkFlow);
			}
		}
		return wfVo;
	}

	/**
	 * �Ӳ˵���ť��Ӧ�¼����ø÷���������
	 * <li>1.��ѯ��Դ����
	 * <li>2.��ʾ��Դ���ݣ�������ѡ��
	 * <li>3.��ȡѡ�����Դ����
	 */
	public static void childButtonClicked(ButtonObject bo, String pkCorp, String FunNode,
			String pkOperator, String currentBillType, Container parent) {
		childButtonClicked(bo, pkCorp, FunNode, pkOperator, currentBillType, parent, null);
	}

	/**
	 * �Ӳ˵���ť��Ӧ�¼����ø÷���������
	 * <li>1.��ѯ��Դ����
	 * <li>2.��ʾ��Դ���ݣ�������ѡ��
	 * <li>3.��ȡѡ�����Դ����
	 * 
	 * @param btnObj
	 * @param pkCorp
	 * @param funNode
	 * @param pkOperator
	 * @param currentBillType
	 * @param parent
	 * @param userObj
	 */
	public static void childButtonClicked(ButtonObject btnObj, String pkCorp, String funNode,
			String pkOperator, String currentBillType, Container parent, Object userObj) {
		childButtonClicked(btnObj, pkCorp, funNode, pkOperator, currentBillType, parent, userObj, null);
	}

	/**
	 * �Ӳ˵���ť��Ӧ�¼����ø÷���������
	 * <li>1.��ȡ��Դ���ݵĲ�ѯ�Ի��򣬲���ѯ����Դ����
	 * <li>2.��ʾ��Դ���ݣ�������ѡ��
	 * <li>3.��ȡѡ�����Դ����
	 * <li>4.�����Դ����IDΪ�գ�����в�ѯ�����ĵ����û�ѡ������������ѡ����գ�����ֱ�ӽ���������ε��ݽ���.
	 * 
	 * @param btnObj
	 * @param pkCorp
	 * @param funNode
	 * @param pkOperator
	 * @param currentBillType
	 * @param parent
	 * @param userObj
	 * @param sourceBillId
	 */
	public static void childButtonClicked(ButtonObject btnObj, String pkCorp, String funNode,
			String pkOperator, String currentBillType, Container parent, Object userObj,
			String sourceBillId) {
		// ���BUTTON��TAG
		String tmpString = btnObj.getTag();
		int findIndex = tmpString.indexOf(":");
		// 1.TAG�б������ѡ���Դ��������PK
		String srcBillType = tmpString.substring(0, findIndex);
		// 2.TAG�б����ҵ������PK
		String businessType = tmpString.substring(findIndex + 1);
		if (businessType.trim().length() == 0) {
			businessType = null;
		}
		makeFlag = false;
		if (srcBillType.toUpperCase().equals("MAKEFLAG")) {
			Logger.debug("******���Ƶ���******");
			makeFlag = true;
			return;
		}

		Logger.debug("******������Դ����******");
		try {
			funNode = PfUIDataCache.getBillType(srcBillType).getNodecode();
			if (funNode == null || funNode.equals(""))
				throw new PFBusinessException("��ע�ᵥ��" + srcBillType + "�Ĺ��ܽڵ��");

			// �жϲ�ѯId����Ϣ����յ���Ϣ
			String strQueryTemplateId = null;
			String referClassName = null; // ��Դ���ݵ���ʾUI��
			String srcNodekey = null; // ���ڲ�ѯ��Դ���ݵĲ�ѯģ��Ľڵ��ʶ

			// ��õ��ݶԵ��ݵ���Դ��ϵVO
			BilltobillreferVO billtobillVO = PfUtilBaseTools.findBilltobill(currentBillType, srcBillType);
			if (billtobillVO != null) {
				strQueryTemplateId = billtobillVO.getQuerytemplateid();
				strQueryTemplateId = strQueryTemplateId == null ? null : strQueryTemplateId.trim();
				referClassName = billtobillVO.getReferclassname();
				srcNodekey = billtobillVO.getChangeclassname();

				if (srcNodekey != null && srcNodekey.startsWith("<") && srcNodekey.endsWith(">")) {
					srcNodekey = srcNodekey.substring(1, srcNodekey.length() - 1);
				} else {
					srcNodekey = null;
				}
			}
			// a.��ȡ��Դ���ݵĲ�ѯ�Ի��򣬼�Ϊm_condition��ֵ
			IBillReferQuery qcDLG = null;
			boolean isQueryRelationCorp = true;
			if (StringUtil.isEmptyWithTrim(strQueryTemplateId)) {
				Logger.debug("��ȡ��Դ�������Ͷ�Ӧ�ڵ�ʹ�õĲ�ѯģ��Ի���");
				strQueryTemplateId = PfUtilBO_Client.getTemplateId(ITemplateStyle.queryTemplate, pkCorp,
						funNode, pkOperator, businessType, srcNodekey);
				isQueryRelationCorp = false;
				qcDLG = setConditionClient(strQueryTemplateId, parent, isQueryRelationCorp, pkOperator,
						funNode, pkCorp);
			} else if (strQueryTemplateId.startsWith("<")) {
				Logger.debug("��ȡ��Ʒ�鶨�Ƶ���Դ���ݲ�ѯ�Ի���");
				strQueryTemplateId = strQueryTemplateId.substring(1, strQueryTemplateId.length() - 1);
				qcDLG = loadUserQuery(parent, strQueryTemplateId, pkCorp, pkOperator, funNode,
						businessType, currentBillType, srcBillType, srcNodekey, userObj);
			} else {
				Logger.debug("֧�ֶ൥λ�ʲ�ѯ�Ĳ�Ʒ���Զ����ѯ�Ի���");
				isQueryRelationCorp = true;
				qcDLG = setConditionClient(strQueryTemplateId, parent, isQueryRelationCorp, pkOperator,
						funNode, pkCorp);
			}

			if (sourceBillId == null) {
				//b.��ʾ��Դ���ݵĲ�ѯ�Ի���
				if (qcDLG.showModal() == UIDialog.ID_OK) {
					// c.��ʾ��Դ���ݣ�������ѡ��
					refBillSource(pkCorp, funNode, pkOperator, currentBillType, parent, userObj, srcBillType,
							businessType, strQueryTemplateId, referClassName, srcNodekey, isQueryRelationCorp,
							sourceBillId, qcDLG);

				} else {
					m_isOk = false;
					return;
				}
			} else {
				//b'.ֱ����ʾ��Դ����
				refBillSource(pkCorp, funNode, pkOperator, currentBillType, parent, userObj, srcBillType,
						businessType, strQueryTemplateId, referClassName, srcNodekey, isQueryRelationCorp,
						sourceBillId, qcDLG);
				return;
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			MessageDialog.showErrorDlg(parent, "����", "�������ε��ݳ����쳣=" + ex.getMessage());
		}

	}

	/**
	 * ������Դ���ݽ���ѡ��
	 * 
	 * @param pkCorp
	 * @param funNode
	 * @param pkOperator
	 * @param currentBillType
	 * @param parent
	 * @param userObj
	 * @param billType
	 * @param businessType
	 * @param strQueryTemplateId
	 * @param referClassName
	 * @param srcNodekey
	 * @param isQueryRelationCorp
	 * @param sourceBillId
	 * @param qcDLG
	 * @throws Exception
	 */
	private static void refBillSource(String pkCorp, String funNode, String pkOperator,
			String currentBillType, Container parent, Object userObj, String billType,
			String businessType, String strQueryTemplateId, String referClassName, String srcNodekey,
			boolean isQueryRelationCorp, String sourceBillId, IBillReferQuery qcDLG) throws Exception {
		// ��ȡ����Ĺؼ��ֶ�
		String pkField = PfUtilBaseTools.findPkField(billType);

		if (pkField == null || pkField.trim().length() == 0)
			throw new PFBusinessException("����ƽ̨���޷�ͨ���������ͻ�ȡ����ʵ�������PK�ֶ�");
		String whereString = null;
		if (sourceBillId == null) {
			whereString = qcDLG.getWhereSQL();
		} else
			whereString = pkField + "='" + sourceBillId + "'";

		// fgj2001-12-06���������ĺ������0(�빫˾�޹�)��1(�빫˾�й�)
		if (isQueryRelationCorp) {
			whereString = whereString + "1";
		} else {
			whereString = whereString + "0";
		}

		//������Դ����չ�ֶԻ���
		AbstractReferQueryUI billReferUI = null;
		if (!StringUtil.isEmptyWithTrim(referClassName)) {
			Logger.debug("��Ʒ�鶨�Ƶ���Դ����չ�ֶԻ��򣬱���̳���AbstractReferQueryUI");
			billReferUI = loadReferUI(referClassName, pkField, pkCorp, pkOperator, funNode, whereString,
					billType, businessType, strQueryTemplateId, currentBillType, srcNodekey, userObj, parent);
		} else {
			Logger.debug("ʹ��ͨ�õ���Դ����չ�ֶԻ���"); // BillSourceDLG BillSourceDLG2
			billReferUI = new BillSourceDLG(pkField, pkCorp, pkOperator, funNode, whereString, billType,
					businessType, strQueryTemplateId, currentBillType, srcNodekey, userObj, parent);
		}

		//��ʾ��Դ����չ�ֶԻ���
		if (billReferUI instanceof BillSourceDLG || billReferUI instanceof BillSourceDLG2) {
			// ��Ҫ����VOת��
			m_isRetChangeVo = false;
		} else {
			// ����Ҫ����VOת��
			m_isRetChangeVo = true;
		}
		// ******2003-05-03***�����ѯ������DLG
		billReferUI.setQueyDlg(qcDLG);
		// ����ģ��
		billReferUI.addBillUI();
		// ��������
		billReferUI.loadHeadData();
		// ***********************************
		if (billReferUI.showModal() == UIDialog.ID_OK) {
			m_sourceBillType = billType;
			m_currentBillType = currentBillType;
			m_tmpRetVo = billReferUI.getRetVo();
			m_tmpRetVos = billReferUI.getRetVos();
			m_isOk = true;
		} else {
			m_isOk = false;
		}
	}

	/**
	 * ���� ��ǰ�����ڵ�Ĵ����� lj+ 2005-1-20
	 */
	public static int getCurrentCheckResult() {
		return m_iCheckResult;
	}

	/**
	 * ���� �û�ѡ���VO
	 */
	public static AggregatedValueObject getRetOldVo() {
		return m_tmpRetVo;
	}

	/**
	 * ���� �û�ѡ��VO����.
	 */
	public static AggregatedValueObject[] getRetOldVos() {
		return m_tmpRetVos;
	}

	/**
	 * ���� �û�ѡ���VO�򽻻�����VO
	 * 
	 * @return
	 */
	public static AggregatedValueObject getRetVo() {
		try {
			if (!m_isRetChangeVo) {
				// ��Ҫ����VO����
				m_tmpRetVo = PfChangeBO_Client.pfChangeBillToBill(m_tmpRetVo, m_sourceBillType,
						m_currentBillType);
			}
		} catch (BusinessException ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFRuntimeException("VO��������" + ex.getMessage(), ex);
		}
		return m_tmpRetVo;
	}

	/**
	 * ���� �û�ѡ���VO�򽻻�����VO
	 * 
	 * @return
	 */
	public static AggregatedValueObject[] changeVos(AggregatedValueObject[] vos) {
		AggregatedValueObject[] tmpRetVos = null;

		try {
			tmpRetVos = PfChangeBO_Client.pfChangeBillToBillArray(vos, m_sourceBillType,
					m_currentBillType);
		} catch (BusinessException ex) {
			Logger.error(ex.getMessage(), ex);
			throw new PFRuntimeException("VO��������" + ex.getMessage(), ex);
		}

		return tmpRetVos;
	}

	/**
	 * ���� �û�ѡ��VO����򽻻�����VO����
	 * 
	 * @return
	 */
	public static AggregatedValueObject[] getRetVos() {
		if (!m_isRetChangeVo)
			// ��Ҫ����VO����
			m_tmpRetVos = changeVos(m_tmpRetVos);

		return m_tmpRetVos;
	}

	/**
	 * �ж��û��Ƿ����ˣ�ȡ������ť
	 * 
	 * @return boolean leijun+
	 */
	public static boolean isCanceled() {
		return !m_checkFlag;
	}

	/**
	 * ���� ���յ����Ƿ������ر�
	 * 
	 * @return boolean
	 */
	public static boolean isCloseOK() {
		return m_isOk;
	}

	/**
	 * ���� ��ǰ���ݶ���ִ���Ƿ�ɹ�
	 * 
	 * @return boolean
	 */
	public static boolean isSuccess() {
		return m_isSuccess;
	}

	/**
	 * ����DLG����
	 * 
	 * @param parent
	 * @param actionVo
	 * @param userObj
	 */
	private static void loadDLG(Container parent, PfUtilActionVO actionVo, Object userObj) {
		try {
			Class c = Class.forName(actionVo.getClassName());
			Class[] ArgsClass = new Class[] { Container.class };
			Object[] Arguments = new Object[] { parent };
			Constructor ArgsConstructor = c.getConstructor(ArgsClass);
			Object retObj = (Object) ArgsConstructor.newInstance(Arguments);
			if (retObj instanceof IinitData) {
				((IinitData) retObj).initData(userObj.toString());
			} else {
				((IinitData2) retObj).initData(userObj);
			}
			((UIDialog) retObj).showModal();
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * �����Ʒ���Լ���Դ���ݲ���UI
	 * 
	 * @param referClassName
	 *            ����������
	 * @param pkField
	 * @param pkCorp
	 * @param operator
	 * @param funNode
	 * @param queryWhere
	 * @param billType
	 * @param businessType
	 * @param templateId
	 * @param currentBillType
	 * @param nodeKey
	 * @param userObj
	 * @param parent
	 * @return
	 */
	public static AbstractReferQueryUI loadReferUI(String referClassName, String pkField,
			String pkCorp, String operator, String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType, String nodeKey,
			Object userObj, Container parent) {
		Class c = null;

		try {
			c = Class.forName(referClassName);
			// NC220�����Ĵ���
			Class[] Args220Class = new Class[] { String.class, String.class, String.class, String.class,
					String.class, String.class, String.class, String.class, String.class, String.class,
					Object.class, Container.class };
			Object[] Arguments = new Object[] { pkField, pkCorp, operator, funNode, queryWhere, billType,
					businessType, templateId, currentBillType, nodeKey, userObj, parent };
			// ʵ�������ι��췽��
			Constructor ArgsConstructor = c.getConstructor(Args220Class);
			return (AbstractReferQueryUI) ArgsConstructor.newInstance(Arguments);
		} catch (NoSuchMethodException ex) {
			// ������һ�����췽��
			try {
				Class[] ArgsClass = new Class[] { String.class, String.class, String.class, String.class,
						String.class, String.class, String.class, String.class, String.class, Container.class };
				Object[] Arguments = new Object[] { pkField, pkCorp, operator, funNode, queryWhere,
						billType, businessType, templateId, currentBillType, parent };
				Constructor ArgsConstructor = c.getConstructor(ArgsClass);
				return (AbstractReferQueryUI) ArgsConstructor.newInstance(Arguments);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
				MessageDialog.showErrorDlg(parent, "����", "��Ʒ���Զ�����մ���:" + ex.getMessage());
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			MessageDialog.showErrorDlg(parent, "����", "��Ʒ��220�Ժ��Զ�����մ���:" + ex.getMessage());
		}
		return null;
	}

	/**
	 * �����Ʒ���Լ���Դ���ݲ�ѯUI
	 * 
	 * @param parent
	 * @param className
	 *            ��Դ���ݲ�ѯUI����
	 * @param pkCorp
	 * @param pkOperator
	 * @param FunNode
	 * @param businessType
	 * @param currBillType
	 * @param sourceBillType
	 * @param nodeKey
	 * @param userObj
	 */
	private static IBillReferQuery loadUserQuery(Container parent, String className, String pkCorp,
			String pkOperator, String FunNode, String businessType, String currBillType,
			String sourceBillType, String nodeKey, Object userObj) {

		Class c = null;
		try {
			c = Class.forName(className);
			// ���ж��Ƿ�Ϊ�²�ѯģ��UI������
			Class[] ArgsClass2 = new Class[] { Container.class, TemplateInfo.class };
			Constructor ArgsConstructor2 = c.getConstructor(ArgsClass2);

			String qtId = PfUtilBO_Client.getTemplateId(ITemplateStyle.queryTemplate, pkCorp, FunNode,
					pkOperator, businessType, nodeKey);
			TemplateInfo ti = new TemplateInfo();
			ti.setTemplateId(qtId);
			ti.setPk_Org(pkCorp);
			ti.setUserid(pkOperator);
			ti.setCurrentCorpPk(pkCorp);
			ti.setFunNode(FunNode);
			Object[] Arguments = new Object[] { parent, ti };
			Object retObj = ArgsConstructor2.newInstance(Arguments);
			//�Բ�ѯģ��Ի����һЩ���Ƴ�ʼ��
			if (retObj instanceof IinitQueryData) {
				((IinitQueryData) retObj).initData(pkCorp, pkOperator, FunNode, businessType, currBillType,
						sourceBillType, userObj);
			} else {
				((IinitQueryData2) retObj).initData(pkCorp, pkOperator, FunNode, businessType,
						currBillType, sourceBillType, nodeKey, userObj);
			}

			return (IBillReferQuery) retObj;
		} catch (NoSuchMethodException e) {
			Logger.warn("����ƽ̨���Ҳ����²�ѯģ��UI�Ĺ��췽���������ж��Ƿ����ϲ�ѯģ��Ĺ��췽��", e);

			try {
				// Ӧ��Ϊ�ϲ�ѯģ��UI������
				Class[] ArgsClass = new Class[] { Container.class };
				Constructor ArgsConstructor = c.getConstructor(ArgsClass);

				Object[] Arguments = new Object[] { parent };
				Object retObj = ArgsConstructor.newInstance(Arguments);
				//�Բ�ѯģ��Ի����һЩ���Ƴ�ʼ��
				if (retObj instanceof IinitQueryData) {
					((IinitQueryData) retObj).initData(pkCorp, pkOperator, FunNode, businessType,
							currBillType, sourceBillType, userObj);
				} else {
					((IinitQueryData2) retObj).initData(pkCorp, pkOperator, FunNode, businessType,
							currBillType, sourceBillType, nodeKey, userObj);
				}
				return (IBillReferQuery) retObj;
			} catch (Exception ex) {
				Logger.error(ex.getMessage(), ex);
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * ���ݶ�������
	 * 
	 * @deprecated 5.5 �滻ΪrunAction����
	 */
	public static Object processAction(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject vo, Object userObj) throws Exception {
		return runAction(parent, actionName, billType, currentDate, vo, userObj, null, null, null);
	}

	/**
	 * ǰ̨���ݶ�������API���㷨���£�
	 * <li>1.����ִ��ǰ��ʾ�Լ���ǰ��������û�ȡ�����򷽷�ֱ�ӷ���
	 * <li>2.�鿴��չ�������ж��Ƿ���Ҫ��������ش������Ϊ�ύ�������������Ҫ�ռ��ύ�˵�ָ����Ϣ��
	 * ���Ϊ�����������������Ҫ�ռ������˵�������Ϣ
	 * <li>3.��ִ̨�ж�������������쳣�����һ���Ĳ�������
	 * <li>4.�Զ�������ķ���ֵ��һ������
	 * 
	 * @param parent ������
	 * @param actionName �������룬����"SAVE"
	 * @param billType �������ͣ��������ͣ�PK
	 * @param currentDate ҵ������
	 * @param billvo ���ݾۺ�VO
	 * @param userObj �û��Զ������
	 * @param strBeforeUIClass ǰ̨��ǰ������
	 * @param checkVo У�鵥�ݾۺ�VO
	 * @param eParam ��չ����
	 * @return ��������ķ��ؽ��
	 * @throws Exception
	 * @since 5.5
	 */
	public static Object runAction(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject billvo, Object userObj, String strBeforeUIClass,
			AggregatedValueObject checkVo, HashMap eParam) throws Exception {
		Logger.debug("*���ݶ������� ��ʼ");
		debugParams(actionName, billType, currentDate, billvo, userObj, strBeforeUIClass, checkVo);
		long start = System.currentTimeMillis();
		m_isSuccess = true;

		if (checkVo == null) {
			checkVo = billvo;
		}

		// 1.����ִ��ǰ��ʾ�Լ���ǰ����
		boolean isContinue = beforeProcessAction(parent, actionName, billType, checkVo, userObj,
				strBeforeUIClass);
		if (!isContinue) {
			Logger.debug("*����ִ��ǰ��ʾ�Լ���ǰ��������");
			m_isSuccess = false;
			return null;
		}

		// 2.�鿴��չ�������Ƿ���Ҫ��������صĽ�������
		PfUtilWorkFlowVO workflowVo = null;
		Object paramNoApprove = eParam == null ? null : eParam.get(PfUtilBaseTools.PARAM_NOAPPROVE);
		if (paramNoApprove == null && (isSaveAction(actionName) || isApproveAction(actionName))) {
			workflowVo = actionAboutApproveflow(parent, actionName, billType, currentDate, billvo, eParam);
			if (!m_isSuccess)
				return null;
		} else if (isStartAction(actionName) || isSignalAction(actionName)) {
			//3.��������صĽ�������
			workflowVo = actionAboutWorkflow(parent, actionName, billType, currentDate, billvo, eParam);
			if (!m_isSuccess)
				return null;
		}
		if (workflowVo == null) {
			//���û����������������������������������EngineService.startApproveflow���ٴβ�ѯ
			if (eParam == null)
				eParam = new HashMap<String, String>();
			eParam.put(PfUtilBaseTools.PARAM_NOAPPROVE, PfUtilBaseTools.PARAM_NOAPPROVE);
		}

		// 4.��ִ̨�ж���
		Object retObj = null;
		try {
			Logger.debug("*��̨�������� ��ʼ");
			long start2 = System.currentTimeMillis();
			IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator.getInstance().lookup(
					IplatFormEntry.class.getName());
			retObj = iIplatFormEntry.processAction(actionName, billType, currentDate, workflowVo, billvo,
					userObj, eParam);
			Logger.debug("*��̨�������� ����=" + (System.currentTimeMillis() - start2) + "ms");

			m_isSuccess = true;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);

			// 5.���������쳣��Ľ�һ������
			retObj = actionExceptionProcess(parent, null, actionName, billType, currentDate, userObj,
					retObj, ex);
		}
		// 5.���ض���ִ��
		retObjRun(parent, retObj);
		Logger.debug("*���ݶ������� ����=" + (System.currentTimeMillis() - start) + "ms");

		return retObj;
	}

	/**
	 * ��������صĽ�������
	 * @throws BusinessException 
	 */
	private static PfUtilWorkFlowVO actionAboutWorkflow(Container parent, String actionName,
			String billType, String currentDate, AggregatedValueObject billvo, HashMap eParam)
			throws BusinessException {
		PfUtilWorkFlowVO workflowVo = null;

		if (isStartAction(actionName)) {
			Logger.debug("*��������=" + actionName + "����鹤����");
			Stack dlgResult = new Stack();
			workflowVo = checkOnStart(parent, actionName, billType, currentDate, billvo, dlgResult,
					eParam);
			if (dlgResult.size() > 0) {
				m_isSuccess = false;
				Logger.debug("*�û�ָ��ʱ�����ȡ������ֹͣ����������");
			}
		} else if (isSignalAction(actionName)) {
			Logger.debug("*ִ�ж���=" + actionName + "����鹤����");
			// ���õ����Ƿ��ڹ�������
			workflowVo = checkWorkitemWhenSignal(parent, actionName, billType, currentDate, billvo,
					eParam);
			if (workflowVo != null) {
				if (workflowVo.getIsCheckPass()) {
					// XXX::����Ҳ��Ϊ����ͨ����һ��,��Ҫ�����ж� lj+
					WFTask currTask = workflowVo.getTaskInfo().getTask();
					if (currTask.getTaskType() == WFTask.TYPE_BACKWARD) {
						if (currTask.isBackToFirstActivity())
							m_iCheckResult = IApproveflowConst.CHECK_RESULT_REJECT_FIRST;
						else
							m_iCheckResult = IApproveflowConst.CHECK_RESULT_REJECT_LAST;
					} else
						m_iCheckResult = IApproveflowConst.CHECK_RESULT_PASS;
				} else
					m_iCheckResult = IApproveflowConst.CHECK_RESULT_NOPASS;
			} else if (!m_checkFlag) {
				m_isSuccess = false;
				Logger.debug("*�û�����������ʱ�����ȡ������ִֹͣ�й�����");
			}
		}
		return workflowVo;
	}

	/**
	 * ��������صĽ�������
	 * @throws BusinessException 
	 */
	private static PfUtilWorkFlowVO actionAboutApproveflow(Container parent, String actionName,
			String billType, String currentDate, AggregatedValueObject billvo, HashMap eParam)
			throws BusinessException {
		PfUtilWorkFlowVO workflowVo = null;

		if (isSaveAction(actionName)) {
			Logger.debug("*�ύ����=" + actionName + "�����������");
			// ���Ϊ�ύ������������Ҫ�ռ��ύ�˵�ָ����Ϣ������ͳһ�������� lj@2005-4-8
			Stack dlgResult = new Stack();
			workflowVo = checkOnSave(parent, IPFActionName.SAVE, billType, currentDate, billvo,
					dlgResult, eParam);
			if (dlgResult.size() > 0) {
				m_isSuccess = false;
				Logger.debug("*�û�ָ��ʱ�����ȡ������ֹͣ����");
			}
		} else if (isApproveAction(actionName)) {
			Logger.debug("*��������=" + actionName + "�����������");
			// ���õ����Ƿ����������У����ռ������˵�������Ϣ
			workflowVo = checkWorkitemWhenApprove(parent, actionName, billType, currentDate, billvo,
					eParam);
			if (workflowVo != null) {
				if (workflowVo.getIsCheckPass()) {
					// XXX::����Ҳ��Ϊ����ͨ����һ��,��Ҫ�����ж� lj+
					WFTask currTask = workflowVo.getTaskInfo().getTask();
					if (currTask.getTaskType() == WFTask.TYPE_BACKWARD) { //  4
						if (currTask.isBackToFirstActivity()) // 
							m_iCheckResult = IApproveflowConst.CHECK_RESULT_REJECT_FIRST;
						else
							m_iCheckResult = IApproveflowConst.CHECK_RESULT_REJECT_LAST;
					} else
						m_iCheckResult = IApproveflowConst.CHECK_RESULT_PASS;
				} else
					m_iCheckResult = IApproveflowConst.CHECK_RESULT_NOPASS;
			} else if (!m_checkFlag) {
				m_isSuccess = false;
				Logger.debug("*�û�����ʱ�����ȡ������ֹͣ����");
			}
		}
		return workflowVo;
	}

	/**
	 * ���ݶ�������
	 * 
	 * @deprecated 5.5 �滻ΪrunAction����
	 */
	public static Object processAction(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject billvo, Object userObj, String strBeforeUIClass,
			AggregatedValueObject checkVo) throws Exception {
		return runAction(parent, actionName, billType, currentDate, billvo, userObj, strBeforeUIClass,
				checkVo, null);
	}

	/**
	 * ��־һ�¶�������������Ĳ���
	 */
	private static void debugParams(String actionName, String billType, String currentDate,
			Object billEntity, Object userObj, String strBeforeUIClass, AggregatedValueObject checkVo) {
		Logger.debug("*********************************************");
		Logger.debug("* actionName=" + actionName);
		Logger.debug("* billType=" + billType);
		Logger.debug("* currentDate=" + currentDate);
		Logger.debug("* billEntity=" + billEntity);
		Logger.debug("* userObj=" + userObj);
		Logger.debug("* strBeforeUIClass=" + strBeforeUIClass);
		Logger.debug("* checkVo=" + checkVo);
		Logger.debug("*********************************************");
	}

	/**
	 * �ж�ĳ���ݶ��������Ƿ�Ϊ"�ύ"��"�༭"����
	 * <li>����"SAVE"��"EDIT"��β
	 * @param actionName ��������
	 * @return
	 */
	public static boolean isSaveAction(String actionName) {
		String strUpperName = actionName.toUpperCase();
		return strUpperName.endsWith(IPFActionName.SAVE) || strUpperName.endsWith(IPFActionName.EDIT);
	}

	/**
	 * �ж�ĳ���ݶ��������Ƿ�Ϊ"����������"����
	 * 
	 * @param actionName ��������
	 * @return
	 */
	private static boolean isStartAction(String actionName) {
		String strUpperName = actionName.toUpperCase();
		return strUpperName.endsWith(IPFActionName.START);
	}

	/**
	 * �ж�ĳ���ݶ��������Ƿ�Ϊ"ִ�й�����"����
	 * 
	 * @param actionName ��������
	 * @return
	 */
	private static boolean isSignalAction(String actionName) {
		int leng = IPFActionName.SIGNAL.length();
		return actionName.length() >= leng
				&& actionName.toUpperCase().substring(0, leng).equals(IPFActionName.SIGNAL);
	}

	/**
	 * Ԥ�㵥���ض��ı��涯������
	 * <li>ֱ�Ӵ������̶���PK������Ҫ�����Ƶ��˲������̶����ˡ�
	 * 
	 * @author leijun 2007-5-21
	 */
	public static Object processSaveActionWithDefPK(Container parent, String actionName,
			String billType, String currentDate, AggregatedValueObject billvo, Object userObj,
			String strBeforeUIClass, AggregatedValueObject checkVo, String flowDefPK) throws Exception {

		if (StringUtil.isEmptyWithTrim(flowDefPK))
			throw new PFBusinessException("�õ��ݶ�������API���봫�����̶���PK����");

		HashMap<String, String> eParam = new HashMap<String, String>();
		eParam.put(PfUtilBaseTools.PARAM_FLOWPK, flowDefPK);
		return runAction(parent, actionName, billType, currentDate, billvo, userObj, strBeforeUIClass,
				checkVo, eParam);
	}

	/**
	 * ���������쳣��Ľ�һ������
	 */
	private static Object actionExceptionProcess(Container parent, PfUtilWorkFlowVO workFlow,
			String actionName, String billType, String currentDate, Object userObj, Object retObj,
			Exception ex) throws Exception {
		if (ex instanceof IPfRetException) {
			IPfRetException retEx = (IPfRetException) ex;
			// �ж��Ƿ���д����쳣
			if (retEx.getBusiStyle() == IPfRetExceptionStyle.DEAL) {
				try {
					// ִ���º���
					retObj = runAfterActionException(parent, workFlow, actionName, billType, currentDate,
							retEx, userObj);
					// �ɹ�����
					m_isSuccess = true;
				} catch (Exception e) {
					m_isSuccess = false;
					throw e;
				}
			} else {
				m_isSuccess = false;
				throw ex;
			}
		} else {
			m_isSuccess = false;
			throw ex;
		}
		return retObj;
	}

	/**
	 * ���ݶ�������
	 * 
	 * @deprecated 5.5 �滻ΪrunAction����
	 */
	public static Object processAction(String actionName, String billType, String currentDate,
			AggregatedValueObject vo) throws Exception {
		return runAction(null, actionName, billType, currentDate, vo, null, null, null, null);
	}

	/**
	 * ���ݶ�������
	 * 
	 * @deprecated 5.5 �滻ΪrunAction����
	 */
	public static Object processAction(String actionName, String billType, String currentDate,
			AggregatedValueObject vo, Object userObj) throws Exception {
		return runAction(null, actionName, billType, currentDate, vo, userObj, null, null, null);
	}

	/**
	 * ���ݶ�������
	 * 
	 * @deprecated 5.5 �滻ΪrunAction����
	 */
	public static Object processActionFlow(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject vo, Object userObj) throws Exception {
		return runAction(parent, actionName, billType, currentDate, vo, userObj, null, null, null);
	}

	/**
	 * ���ݶ�������
	 * 
	 * @deprecated 5.5 �滻ΪrunAction����
	 */
	public static Object processActionFlow(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject billVo, Object userObj, String strBeforeUIClass)
			throws Exception {
		return runAction(parent, actionName, billType, currentDate, billVo, userObj, strBeforeUIClass,
				null, null);
	}

	/**
	 * �ж�ĳ���ݶ��������Ƿ�Ϊ"����"����
	 * <li>����"APPROVE"��ͷ
	 * @param actionName
	 * @return
	 */
	public static boolean isApproveAction(String actionName) {
		return actionName.length() >= 7
				&& actionName.toUpperCase().substring(0, 7).equals(IPFActionName.APPROVE);
	}

	/**
	 * ����ִ��ǰ��ʾ�Լ���ǰ����
	 */
	private static boolean beforeProcessAction(Container parent, String actionName, String billType,
			AggregatedValueObject billVo, Object userObj, String strBeforeUIClass) throws Exception {
		ActionClientParams acp = new ActionClientParams();
		acp.setUiBeforeClz(strBeforeUIClass);
		acp.setUiContainer(parent);
		acp.setActionCode(actionName);
		acp.setBillType(billType);
		acp.setBillEntity(billVo);
		acp.setUserObject(userObj);

		return PfUtilUITools.beforeAction(acp, false);
	}

	/**
	 * ���ݶ����ڲ����������������쳣��Ĳ�������
	 * <li> ��runAfterActionException()����
	 */
	private static Object processActionInner(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject vo, Object userObj, PfUtilWorkFlowVO workFlow)
			throws Exception {
		Logger.debug("*���ݶ����ڲ����� ��ʼ");
		debugParams(actionName, billType, currentDate, vo, userObj, null, null);
		long start = System.currentTimeMillis();

		// ���еķ��ض���
		Object retObj = null;
		try {
			// 1.��̨��������
			IplatFormEntry iIplatFormEntry = (IplatFormEntry) NCLocator.getInstance().lookup(
					IplatFormEntry.class.getName());
			retObj = iIplatFormEntry.processAction(actionName, billType, currentDate, workFlow, vo,
					userObj, null);

			m_isSuccess = true;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			// 2.�쳣��Ľ�һ������
			retObj = actionExceptionProcess(parent, null, actionName, billType, currentDate, userObj,
					retObj, ex);
		}

		// 3.���ض���ִ��
		retObjRun(parent, retObj);
		Logger.debug("*���ݶ����ڲ����� ����=" + (System.currentTimeMillis() - start) + "ms");
		return retObj;
	}

	/**
	 * ���ض���ִ��
	 * 
	 * @param parent
	 * @param retObj
	 */
	private static void retObjRun(Container parent, Object retObj) {
		if (retObj instanceof PfUtilActionVO) {
			PfUtilActionVO retVo = (PfUtilActionVO) retObj;
			if (retVo.getIsDLG()) {
				Logger.debug("*ִ��DLG");
				loadDLG(parent, retVo, retVo.getUIObj());
			} else {
				Logger.debug("*ִ��PANEL");
				BillQueryDLG bQDlg = new BillQueryDLG(parent, retVo, retVo.getUIObj());
				bQDlg.showModal();
			}
		}
	}

	/**
	 * ��ִ�е��ݶ���������������������ش���
	 * 
	 * @return
	 */
	public static Object processActionNoSendMessage(Container parent, String actionName,
			String billType, String currentDate, AggregatedValueObject billvo, Object userObj,
			String strBeforeUIClass, AggregatedValueObject checkVo) throws Exception {

		HashMap<String, String> eParam = new HashMap<String, String>();
		eParam.put(PfUtilBaseTools.PARAM_NOAPPROVE, PfUtilBaseTools.PARAM_NOAPPROVE);

		return runAction(parent, actionName, billType, currentDate, billvo, userObj, strBeforeUIClass,
				checkVo, eParam);
	}

	/**
	 * ���ݶ���������
	 * 
	 * @deprecated 5.5 �滻ΪrunBatch����
	 */
	public static Object[] processBatch(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry) throws Exception {
		return runBatch(parent, actionName, billType, currentDate, voAry, userObjAry, null, null);
	}

	/**
	 * ���ݶ�����������
	 * 
	 * @deprecated 5.5 �滻ΪrunBatch����
	 */
	public static Object[] processBatch(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry,
			String strBeforeUIClass) throws Exception {
		return runBatch(parent, actionName, billType, currentDate, voAry, userObjAry, strBeforeUIClass,
				null);
	}

	/**
	 * ǰ̨���ݶ���������API���㷨���£�
	 * <li>1.����ִ��ǰ��ʾ�Լ���ǰ��������û�ȡ�����򷽷�ֱ�ӷ���
	 * <li>2.�鿴��չ�������ж��Ƿ���Ҫ��������ش������Ϊ�ύ�������ҵ���VO������ֻ��һ�ŵ���ʱ������Ҫ�ռ��ύ�˵�ָ����Ϣ��
	 * ���Ϊ��������������Ե�һ�ŵ��ݿ�����Ҫ�ռ������˵�������Ϣ
	 * <li>3.��ִ̨������������������쳣�����һ���Ĳ�������
	 * 
	 * @param parent ������
	 * @param actionName �������룬����"SAVE"
	 * @param billType �������ͣ��������ͣ�PK
	 * @param currentDate ҵ������
	 * @param voAry ���ݾۺ�VO����
	 * @param userObjAry �û��Զ����������
	 * @param strBeforeUIClass ǰ̨��ǰ������
	 * @param eParam ��չ����
	 * @return ����������ķ��ؽ��
	 * @throws Exception
	 * @since 5.5
	 */
	public static Object[] runBatch(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry,
			String strBeforeUIClass, HashMap eParam) throws Exception {
		Logger.debug("*���ݶ��������� ��ʼ");
		debugParams(actionName, billType, currentDate, voAry, userObjAry, strBeforeUIClass, null);
		long start = System.currentTimeMillis();
		m_isSuccess = true;

		// 1.����ִ��ǰ��ʾ�Լ���ǰ����
		boolean isContinue = beforeProcessBatchAction(parent, actionName, billType, voAry, userObjAry,
				strBeforeUIClass);
		if (!isContinue) {
			Logger.debug("*����ִ��ǰ��ʾ�Լ���ǰ��������");
			m_isSuccess = false;
			return null;
		}

		// 2.�鿴��չ�������Ƿ���Ҫ��������ش���
		PfUtilWorkFlowVO workflowVo = null;
		Object paramNoApprove = eParam == null ? null : eParam.get(PfUtilBaseTools.PARAM_NOAPPROVE);
		if (paramNoApprove == null) {
			// ��Ҫ�������������� begin
			if (isSaveAction(actionName)) {
				Logger.debug("*�ύ���������������");
				if (voAry.length == 1) {
					// �������VO������ֻ����һ�ŵ���,���ύʱ��ָ����Ϣ lj+2005-6-7
					Stack dlgResult = new Stack();
					workflowVo = checkOnSave(parent, IPFActionName.SAVE, billType, currentDate, voAry[0],
							dlgResult, eParam);
					if (dlgResult.size() > 0) {
						m_isSuccess = false;
						Logger.debug("*�û�ָ��ʱ�����ȡ������ֹͣ����");
						return null;
					}
				}
			} else if (isApproveAction(actionName)) {
				Logger.debug("*�������������������");
				// ���������Ի���,��ȡ���������ָ����Ϣ->ֻ��Ե�һ�ŵ���?!
				workflowVo = checkWorkitemWhenApprove(parent, actionName, billType, currentDate, voAry[0],
						eParam);
				// ���������Ϊ�ղ��Ҳ�������˳���������
				if ((workflowVo == null) && (!m_checkFlag)) {
					Logger.debug("*�û�����ʱ�����ȡ������ֹͣ����");
					m_isSuccess = false;
					return null;
				}
			}

			if (workflowVo == null) {
				//���û����������������������������������EngineService.startApproveflow���ٴβ�ѯ
				if (eParam == null)
					eParam = new HashMap<String, String>();
				eParam.put(PfUtilBaseTools.PARAM_NOAPPROVE, PfUtilBaseTools.PARAM_NOAPPROVE);
			}
		}// ��Ҫ�������������� end

		// 3.��̨��������
		Object retObj = null;
		try {
			Logger.debug("*��̨���������� ��ʼ");
			long start2 = System.currentTimeMillis();
			retObj = NCLocator.getInstance().lookup(IplatFormEntry.class).processBatch(actionName,
					billType, currentDate, workflowVo, voAry, userObjAry, eParam);
			Logger.debug("*��̨���������� ����=" + (System.currentTimeMillis() - start2) + "ms");

			m_isSuccess = true;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			// 4.�����������쳣��Ľ�һ������
			retObj = batchActionExceptionProcess(parent, null, actionName, billType, currentDate,
					userObjAry, retObj, ex);
		}
		Logger.debug("*���ݶ��������� ����=" + (System.currentTimeMillis() - start) + "ms");
		return (Object[]) retObj;
	}

	/**
	 * ����ִ��ǰ��ʾ�Լ���ǰ����
	 */
	private static boolean beforeProcessBatchAction(Container parent, String actionName,
			String billType, AggregatedValueObject[] voAry, Object[] userObjAry, String strBeforeUIClass)
			throws Exception {

		ActionClientParams acp = new ActionClientParams();
		acp.setUiBeforeClz(strBeforeUIClass);
		acp.setUiContainer(parent);
		acp.setActionCode(actionName);
		acp.setBillType(billType);
		acp.setBillEntity(voAry);
		acp.setUserObject(userObjAry);

		return PfUtilUITools.beforeAction(acp, true);
	}

	/**
	 * ��������ִ�к���쳣����
	 */
	private static Object batchActionExceptionProcess(Container parent, PfUtilWorkFlowVO workflowVo,
			String actionName, String billType, String currentDate, Object[] userObjAry, Object retObj,
			Exception ex) throws Exception {
		// �ж��쳣�Ƿ���Ҫ����ҵ�����������ж�������
		if (ex instanceof IPfRetException) {
			IPfRetException retEx = (IPfRetException) ex;
			// �ж��Ƿ���д����쳣
			if (retEx.getBusiStyle() == IPfRetExceptionStyle.DEAL) {
				try {
					// ִ���º���
					retObj = runAfterBatchException(parent, workflowVo, actionName, billType, currentDate,
							retEx, userObjAry);
					// �ɹ�����
					m_isSuccess = true;
				} catch (Exception e) {
					m_isSuccess = false;
					throw e;
				}
			} else {
				m_isSuccess = false;
				throw ex;
			}
		} else {
			m_isSuccess = false;
			throw ex;
		}
		return retObj;
	}

	/**
	 * ���ݶ�����������
	 * 
	 * @deprecated 5.5 �滻ΪrunBatch����
	 */
	public static Object[] processBatch(String actionName, String billType, String currentDate,
			AggregatedValueObject[] voAry) throws Exception {
		return runBatch(null, actionName, billType, currentDate, voAry, null, null, null);
	}

	/**
	 * ���ݶ�����������
	 * 
	 * @deprecated 5.5 �滻ΪrunBatch����
	 */
	public static Object[] processBatch(String actionName, String billType, String currentDate,
			AggregatedValueObject[] voAry, Object[] userObjAry) throws Exception {
		return runBatch(null, actionName, billType, currentDate, voAry, userObjAry, null, null);
	}

	/**
	 * ���ݶ�����������
	 * 
	 * @deprecated 5.5 �滻ΪrunBatch����
	 */
	public static Object[] processBatchFlow(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry) throws Exception {
		return runBatch(parent, actionName, billType, currentDate, voAry, userObjAry, null, null);
	}

	/**
	 * ���ݶ����ڲ��������������������쳣��Ĳ�������
	 * <li> ��runAfterBatchException()����
	 */
	private static Object[] processBatchInner(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry,
			PfUtilWorkFlowVO workFlow) throws Exception {
		Object retObj = null;
		try {
			// ��������
			//retObj = NCLocator.getInstance().lookup(IPFBusiAction.class).processBatch(actionName,
			//		billType, currentDate, voAry, userObjAry, workFlow);
			retObj = NCLocator.getInstance().lookup(IplatFormEntry.class).processBatch(actionName,
					billType, currentDate, workFlow, voAry, userObjAry, null);

			// �������гɹ�
			m_isSuccess = true;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			// �ж��쳣�Ƿ���Ҫ����ҵ�����������ж�������
			retObj = batchActionExceptionProcess(parent, workFlow, actionName, billType, currentDate,
					userObjAry, retObj, ex);
		}
		return (Object[]) retObj;
	}

	/**
	 * ��ѯ��������������״̬
	 * 
	 * @param busiType
	 * @param billType
	 * @param billId
	 * @return
	 */
	public static int queryWorkFlowStatus(String busiType, String billType, String billId) {
		try {
			return NCLocator.getInstance().lookup(IPFWorkflowQry.class).queryWorkflowStatus(busiType,
					billType, billId);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return IWorkFlowStatus.ABNORMAL_WORKFLOW_STATUS;
		}
	}

	/**
	 * Ϊ"����"��ť����Ӳ˵���ť
	 * <li>��Щ�Ӳ˵���ť��Ϊ�ɲ��յ���Դ����
	 * <li>���ĳ��������(��������)��ĳҵ�����������õ�������Դ��������
	 * 
	 * @param boAdd "����"��ť
	 * @param corpId ��˾PK
	 * @param billType ��ǰ��������(��������)PK
	 * @param boBusiness "ҵ������"��ť(Ϊ��ʱ������е�������(��������)����Դ��������)
	 */
	public static void retAddBtn(ButtonObject boAdd, String corpId, String billType,
			ButtonObject boBusiness) {

		if (boBusiness != null) {
			String businessType = null;
			businessType = boBusiness.getTag();

			// ��øõ�������(��������)��ĳҵ�����������õ�������Դ��������
			BillbusinessVO[] billBusiVOs = PfUIDataCache.getSourceByCorpAndBillAndBusi(corpId, billType,
					businessType);
			boAdd.removeAllChildren();
			if (billBusiVOs == null)
				return;

			// ����Դ���ݶ���Ϊ�Ӳ˵���ť
			ButtonObject btnAddChild = null;
			for (int i = 0; i < billBusiVOs.length; i++) {
				String showName = Pfi18nTools.i18nBilltypeName(billBusiVOs[i].getPk_billtype(),
						billBusiVOs[i].getBilltypename());
				btnAddChild = new ButtonObject(showName, showName, showName);
				btnAddChild.setPowerContrl(false);
				// ���ð�ť��TAGΪ��3C:1001AA10000000004SG5��
				btnAddChild.setTag(billBusiVOs[i].getPk_billtype().trim() + ":"
						+ billBusiVOs[i].getPk_businesstype().trim());
				boAdd.addChildButton(btnAddChild);
			}
		} else {
			retAddBtn(boAdd, corpId, billType);
		}

	}

	/**
	 * ���ݵ������Ͳ���"����"��ť���Ӳ˵���ť
	 * 
	 * @param boAdd
	 *            "����"��ť
	 * @param billType
	 *            ��ǰ��������PK
	 * @throws BusinessException
	 */
	public static ButtonObject getRefAddBtn(ButtonObject boAdd, String billType)
			throws BusinessException {

		// ����Դ���ݶ���Ϊ�Ӳ˵���ť
		ButtonObject[] btnAddChild = boAdd.getChildButtonGroup();
		for (int i = 0; i < btnAddChild.length; i++) {
			ButtonObject bo = btnAddChild[i];
			if (bo.getTag().startsWith(billType))
				return bo;
		}
		throw new BusinessException("Not found billtype:" + billType + "'s Add Btn");
	}

	/**
	 * Ϊ"ҵ������"��ť����Ӳ˵���ť��
	 * <li>��ȡ�õ�������(��������)���ù�������ҵ�����ͣ������Ӱ�ť��ʽ���أ�
	 * <li>�������Ž��е��������ã�
	 * <li>����/��˾��ҵ�������Էָ����ָ���
	 * 
	 * @param inoutBoBusiness "ҵ������"���˵���ť
	 * @param corpId ��˾PK
	 * @param billType ��������(��������)PK
	 */
	public static void retBusinessBtn(ButtonObject inoutBoBusiness, String corpId, String billType) {
		try {
			// ��ѯ�õ����������õ�����ҵ�����̣��������źͱ���˾�ģ�������ǰ��
			BusitypeVO[] busiTypeVos = getBusiByCorpAndBill(corpId, billType);
			inoutBoBusiness.removeAllChildren();
			if (busiTypeVos == null || busiTypeVos.length == 0)
				return;

			ButtonObject btnBusitype = null;
			String strOldCorp = busiTypeVos[0].getPk_corp();// "@@@@";
			// //����ҵ�����ͣ���Ҫͨ��@@@@��Ϊ��ֵ
			String strCurrCorp = null;
			for (int i = 0; i < busiTypeVos.length; i++) {
				strCurrCorp = busiTypeVos[i].getPk_corp();
				if (!strCurrCorp.equals(strOldCorp)) {
					// �϶����ڼ���ҵ������
					btnBusitype = new ButtonObject("", "", "");
					btnBusitype.setSeperator(true);
					inoutBoBusiness.addChildButton(btnBusitype);
				}

				// FIXME::ҵ������û��i18n��
				String showName = null;
				// NCLangRes.getInstance().getStrByID("busitype", "D" +
				// billReferVo[i].getPk_busitype());
				// ҵ����������
				if (showName == null || showName.trim().length() == 0)
					showName = busiTypeVos[i].getBusiname();
				btnBusitype = new ButtonObject(showName, showName, showName);

				btnBusitype.setPowerContrl(false);
				// TAG=ҵ����������
				btnBusitype.setTag(busiTypeVos[i].getPrimaryKey());
				btnBusitype.setData(busiTypeVos[i]);
				inoutBoBusiness.addChildButton(btnBusitype);

				strOldCorp = strCurrCorp;
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * ��ѯ����������ĳ��������(��������)��Ȩ�޵�ҵ�����ͣ��������źͱ���˾�ģ�������ǰ��
	 * @param corpId
	 * @param billType ��������(��������)
	 * @return
	 * @throws BusinessException
	 */
	public static BusitypeVO[] getBusiByCorpAndBill(String corpId, String billType)
			throws BusinessException {
		BusitypeVO[] busitypeAll = PfUIDataCache.getBusiByCorpAndBill(corpId, billType);
		if (busitypeAll == null || busitypeAll.length == 0)
			return null;

		//��ѯ��ǰ��¼�û���Ȩ�޵�ҵ������
		String sUserid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		IPFConfig ipf = (IPFConfig) NCLocator.getInstance().lookup(IPFConfig.class.getName());
		PowerResultVO voPower = ipf.queryPowerBusiness(sUserid, corpId, billType);
		if (!voPower.isPowerControl())
			return busitypeAll;// ����Ȩ�޿��ƣ��򷵻ع�˾�ܹ�����������ҵ������
		String[] sBusinessOfPower = voPower.getPowerId();
		if (sBusinessOfPower == null || sBusinessOfPower.length == 0)
			return null;
		HashSet<String> setBusitypeOfPower = new HashSet<String>();
		for (int i = 0; i < sBusinessOfPower.length; i++) {
			setBusitypeOfPower.add(sBusinessOfPower[i]);
		}
		ArrayList<BusitypeVO> listBusitype = new ArrayList<BusitypeVO>();
		for (int i = 0; i < busitypeAll.length; i++) {
			if (setBusitypeOfPower.contains(busitypeAll[i].getPrimaryKey()))
				listBusitype.add(busitypeAll[i]);
		}
		return listBusitype.toArray(new BusitypeVO[0]);
	}

	/**
	 * ����ĳ��������(��������)ĳ����������ж���������Ϊ�Ӱ�ť���
	 * 
	 * @param boElse ������ť
	 * @param billType ��������(��������)PK
	 * @param actionStyle ���������
	 */
	public static void retElseBtn(ButtonObject boElse, String billType, String actionStyle) {
		if (boElse == null)
			return;
		//��õ�������(��������)�ĵ�������PK
		billType = PfUtilBaseTools.getRealBilltype(billType);

		PfUtilBillActionVO[] billActionVos = (PfUtilBillActionVO[]) PfUIDataCache
				.getButtonByBillAndGrp(billType, actionStyle);
		boElse.removeAllChildren();

		ButtonObject btnChild = null;
		for (int i = 0; i < (billActionVos == null ? 0 : billActionVos.length); i++) {
			// �������ﻯ
			String showName = Pfi18nTools.i18nActionName(billActionVos[i].getPkBillType(),
					billActionVos[i].getActionName(), billActionVos[i].getActionNote());
			btnChild = new ButtonObject(showName, showName, billActionVos[i].getActionName());
			btnChild.setPowerContrl(false);
			btnChild.setTag(billActionVos[i].getActionName().trim());
			boElse.addChildButton(btnChild);
		}

	}

	/**
	 * �����쳣��Ҫ�ٴν��д����ִ����
	 */
	private static Object runAfterActionException(Container parent, PfUtilWorkFlowVO workFlow,
			String actionName, String billType, String currentDate, IPfRetException retEx, Object userObj)
			throws Exception {
		Object retObj = null;
		try {
			// �쳣�еĴ�����
			String procClassName = retEx.getProcClass();
			// �쳣�еķ��ض���
			Object retValObj = retEx.getObject();
			// �������ʵ��
			Class c = Class.forName(procClassName);
			Object retRunObj = c.newInstance();
			if (retRunObj instanceof IRunClassAfterException) {
				IRunClassAfterException runAfter = (IRunClassAfterException) retRunObj;
				runAfter.runClass(parent, retValObj);
				// ǰ̨���еķ��ض���
				Object retProcObj = runAfter.getobj();
				// ���÷�������
				retObj = processActionInner(parent, actionName, billType, currentDate,
						(AggregatedValueObject) retProcObj, userObj, workFlow);
			}
		} catch (Exception ex) {
			throw ex;
		}
		return retObj;
	}

	/**
	 * �����쳣��Ҫ�ٴν��д����ִ����
	 * 
	 * @return
	 */
	private static Object runAfterBatchException(Container parent, PfUtilWorkFlowVO workFlow,
			String actionName, String billType, String currentDate, IPfRetException retEx,
			Object[] userObjAry) throws Exception {
		Object retObj = null;
		try {
			// �쳣�еĴ�����
			String procClassName = retEx.getProcClass();
			// �쳣�еķ��ض���
			Object retValObj = retEx.getObject();
			// �������ʵ��
			Class c = Class.forName(procClassName);
			Object retRunObj = c.newInstance();
			if (retRunObj instanceof IRunClassAfterException) {
				IRunClassAfterException runAfter = (IRunClassAfterException) retRunObj;
				runAfter.runClass(parent, retValObj);
				// ǰ̨���еķ��ض���
				Object retProcObj = runAfter.getobj();
				// ���÷�������
				retObj = processBatchInner(parent, actionName, billType, currentDate,
						(AggregatedValueObject[]) retProcObj, userObjAry, workFlow);
			}
		} catch (Exception ex) {
			throw ex;
		}
		return retObj;
	}

	/**
	 * ����һ����ѯ�Ի��򣬲�Ϊ�����ò�ѯģ��
	 * 
	 * @param templateId
	 * @param parent
	 * @param isRelationCorp
	 * @param pkOperator
	 * @param funNode
	 */
	private static IBillReferQuery setConditionClient(String templateId, Container parent,
			boolean isRelationCorp, final String pkOperator, final String funNode, String pkCorp) {
		nc.vo.querytemplate.TemplateInfo ti = new nc.vo.querytemplate.TemplateInfo();
		ti.setTemplateId(templateId);
		ti.setPk_Org(pkCorp);
		ti.setUserid(pkOperator);
		ti.setCurrentCorpPk(pkCorp);
		ti.setFunNode(funNode);
		
		QueryConditionDLG qcDlg = new QueryConditionDLG(parent, ti);

		if (isRelationCorp) {
			// FIXME::�๫˾����
			qcDlg.setPlatForm(isRelationCorp);

			qcDlg.registerFieldValueEelementEditorFactory(new IFieldValueElementEditorFactory() {
				public IFieldValueElementEditor createFieldValueElementEditor(FilterMeta meta) {

					if ("��˾Ŀ¼".equals(meta.getValueEditorDescription())) {
						UIRefPane refPane = new UIRefPane("��˾Ŀ¼");
						refPane.setWhereString(constructWherePart(pkOperator, funNode));
						return new RefElementEditor(refPane, meta.getDispType());

					}
					return null;
				}
			});

		} else {
			qcDlg.setVisibleNormalPanel(false);
		}
		// qcDlg.setTemplateID(templateId);
		return qcDlg;
	}

	/**
	 * ���조��˾Ŀ¼�����յ�ɸѡ����SQL
	 * 
	 * @param pkOperator
	 *            �û�PK
	 * @param funNode
	 *            ���ܽڵ����
	 * @return
	 */
	private static String constructWherePart(String pkOperator, String funNode) {
		String baseSql = " where (isseal is null or isseal<>'Y') and ishasaccount='Y' ";
		// ����ĳ�û���ĳ�ڵ���Ȩ�޵Ĺ�˾
		IFuncPower ifp = (IFuncPower) NCLocator.getInstance().lookup(IFuncPower.class.getName());
		String[] strPkCorps = null;
		try {
			strPkCorps = ifp.queryCorpByUserAndFunc(pkOperator, funNode);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		if (strPkCorps == null || strPkCorps.length == 0)
			return baseSql;
		String whereSQL = baseSql + " and pk_corp in (";
		for (int i = 0; i < strPkCorps.length; i++) {
			whereSQL += "'" + strPkCorps[i] + "',";
		}
		whereSQL = whereSQL.substring(0, whereSQL.length() - 1) + ")";

		// NC31��ʵ�ַ�ʽ WARN::��ͼv_sm_userpower��NC50�Ѿ�������
		// whereSQL = baseSql
		// + " and pk_corp in (select distinct pk_corp from v_sm_userpower where
		// userid = '"
		// + pkOperator + "' and fun_code = '" + funNode + "')";

		return whereSQL;
	}

	/**
	 * Ϊ"����"��ť����Ӳ˵���ť
	 * <li>��Щ�Ӳ˵���ť��Ϊ���е�������(��������)��ĳҵ�����������õ�������Դ�������Ϳɲ��յ���Դ����
	 * 
	 * @param boAdd "����"��ť
	 * @param corpId ��˾PK
	 * @param billType ��ǰ��������(��������)PK
	 */
	public static void retAddBtn(ButtonObject boAdd, String corpId, String billType) {
		retAddBtn2(boAdd, corpId, billType, null);
	}

	/**
	 * Ϊ"����"��ť����Ӳ˵���ť
	 * <li>��Щ�Ӳ˵���ť��Ϊ���е�������(��������)��ĳҵ�����������õ�������Դ�������Ϳɲ��յ���Դ����
	 * 
	 * @param boAdd "����"��ť
	 * @param corpId ��˾PK
	 * @param billType ��ǰ��������(��������)PK
	 */
	public static void retAddBtn2(ButtonObject boAdd, String corpId, String billType, String transType) {
		String businessType = null;

		try {
			// ��ѯ����������ĳ��������(��������)��Ȩ�޵�ҵ�����ͣ��������źͱ���˾�ģ�������ǰ��
			BusitypeVO[] types = PfUtilClient_EH.getBusiByCorpAndBill(corpId, billType);

			boAdd.removeAllChildren();

			HashMap<String, StringBuffer> refbills = new HashMap<String, StringBuffer>();

			ButtonObject btnAddChild = null;

			for (int i = 0; i < (types == null ? 0 : types.length); i++) {

				businessType = types[i].getPk_busitype();

				// ��øõ�������(��������)��ĳҵ�����������õ�������Դ��������
				BillbusinessVO[] billBusiVOs = PfUIDataCache.getSourceByCorpAndBillAndBusi2(corpId,
						billType, transType, businessType);

				// ����Դ���ݶ���Ϊ�Ӳ˵���ť
				for (int j = 0; j < billBusiVOs.length; j++) {

					String showName = Pfi18nTools.i18nBilltypeName(billBusiVOs[j].getPk_billtype(),
							billBusiVOs[j].getBilltypename());
					if (billBusiVOs[j].getPk_billtype().toUpperCase().equals("MAKEFLAG")) {
						showName = showName + "_[" + types[i].getBusiname() + "]";

						btnAddChild = new ButtonObject(showName, showName, showName);
						btnAddChild.setPowerContrl(false);
						// ���ð�ť��TAGΪ��MAKEFLAG:1001AA10000000004SG5��
						btnAddChild.setTag(billBusiVOs[j].getPk_billtype().trim() + ":" + businessType);
						boAdd.addChildButton(btnAddChild);
					} else {
						StringBuffer tag = null;
						if (refbills.get(showName) == null) {
							tag = new StringBuffer();
							tag.append(billBusiVOs[j].getPk_billtype().trim());
							tag.append(":");
						} else {
							tag = refbills.get(showName);
							tag.append(",");
						}
						tag.append(businessType);
						refbills.put(showName, tag);
					}
				}
			}

			for (String key : refbills.keySet()) {

				btnAddChild = new ButtonObject(key, key, key);
				btnAddChild.setPowerContrl(false);
				// ���ð�ť��TAGΪ��3C:1001AA10000000004SG5,1001AA10000000004SG6,1001AA10000000004SG7��
				btnAddChild.setTag(refbills.get(key).toString());
				boAdd.addChildButton(btnAddChild);

			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}

	}

	/**
	 * Ϊ"����"��ť����Ӳ˵���ť
	 * <li>��Щ�Ӳ˵���ť��Ϊ���е�������(��������)��ĳҵ�����������õ�������Դ�������Ϳɲ��յ���Դ����
	 * 
	 * @param boAdd "����"��ť
	 * @param corpId ��˾PK
	 * @param billType ��ǰ��������(��������)PK
	 */
	public static void retCopyBtn(ButtonObject boCopy, String corpId, String billType,
			String transType) {
		String businessType = null;

		try {
			// ��ѯ����������ĳ��������(��������)��Ȩ�޵�ҵ�����ͣ��������źͱ���˾�ģ�������ǰ��
			BusitypeVO[] types = PfUtilClient_EH.getBusiByCorpAndBill(corpId, billType);

			boCopy.removeAllChildren();

			ButtonObject btnAddChild = null;

			for (int i = 0; i < (types == null ? 0 : types.length); i++) {

				businessType = types[i].getPk_busitype();

				// ��øõ�������(��������)��ĳҵ�����������õ�������Դ��������
				BillbusinessVO[] billBusiVOs = PfUIDataCache.getSourceByCorpAndBillAndBusi2(corpId,
						billType, transType, businessType);

				// ����Դ���ݶ���Ϊ�Ӳ˵���ť
				for (int j = 0; j < billBusiVOs.length; j++) {

					if (billBusiVOs[j].getPk_billtype().toUpperCase().equals("MAKEFLAG")) {
						String showName = boCopy.getName() + "_[" + types[i].getBusiname() + "]";

						btnAddChild = new ButtonObject(showName, showName, showName);
						btnAddChild.setPowerContrl(false);
						// ���ð�ť��TAGΪ��MAKEFLAG:1001AA10000000004SG5��
						btnAddChild.setTag(billBusiVOs[j].getPk_billtype().trim() + ":" + businessType);
						boCopy.addChildButton(btnAddChild);
					}
				}
			}

		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
		}

	}
}