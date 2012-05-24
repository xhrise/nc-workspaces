package nc.ui.trade.businessaction;

import java.util.ArrayList;
import java.util.Vector;

import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.pf.IUIBeforeProcAction;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.pub.IExAggVO;

/**
 * Ĭ�ϵ�ҵ����ʵ�֡� �������ڣ�(2004-1-3 14:46:52)
 * 
 * @author�����ھ�
 */
public class BusinessAction extends DefaultBusinessAction {
	private AbstractBillUI m_billUI = null;

	/**
	 * BusinessAction ������ע�⡣ �ù�����е�Ԫ���Եĺ�̨����
	 */
	public BusinessAction() {
		super();
	}

	/**
	 * BusinessAction ������ע�⡣ �ù�����е�Ԫ���Եĺ�̨����
	 */
	public BusinessAction(AbstractBillUI ui) {
		super();
		m_billUI = ui;
	}

	/**
	 * approve ����ע�⡣
	 */
	public nc.vo.pub.AggregatedValueObject approve(AggregatedValueObject billVO, String billType, String billDate, Object userObj) throws Exception {
		return (AggregatedValueObject) PfUtilClient.processActionFlow(m_billUI, IPFACTION.APPROVE, billType, billDate, billVO, userObj);
	}

	/**
	 * approve ����ע�⡣
	 */
	public Object approveBatch(AggregatedValueObject[] billVO, String billType, String billDate, Object[] userObj) throws Exception {
		return PfUtilClient.processBatchFlow(m_billUI, IPFACTION.APPROVE, billType, billDate, billVO, userObj);
	}

	/**
	 * commit ����ע�⡣
	 */
	public ArrayList commit(AggregatedValueObject billVO, String billType, String billDate, Object userObj) throws Exception {
		java.util.ArrayList al = (java.util.ArrayList) PfUtilClient.processAction(getUI(), IPFACTION.COMMIT, billType, billDate, billVO, userObj);

		return al;
	}

	/**
	 * delete ����ע�⡣
	 */
	public nc.vo.pub.AggregatedValueObject delete(nc.vo.pub.AggregatedValueObject billVO, String billType, String billDate, Object userObj) throws Exception {
		return billVO = (AggregatedValueObject) PfUtilClient.processAction(getUI(), IPFACTION.DELETE, billType, billDate, billVO, userObj);
	}

	/**
	 * edit ����ע�⡣
	 */
	public nc.vo.pub.AggregatedValueObject edit(nc.vo.pub.AggregatedValueObject billVO, java.lang.String billType, java.lang.String billDate, java.lang.Object userObj) throws java.lang.Exception {
		return billVO = (AggregatedValueObject) PfUtilClient.processActionNoSendMessage(getUI(), IPFACTION.EDIT, billType, billDate, billVO, userObj, null, null);
	}

	/**
	 * ���ݷ��ص����ݽ���ƥ�����ݡ� �������ڣ�(2004-2-27 22:08:03)
	 * 
	 * @param retVo
	 *            nc.vo.pub.AggregatedValueObject
	 * @param oldVo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	private AggregatedValueObject fillUIData(AggregatedValueObject retVo, AggregatedValueObject oldVo) throws Exception {
		if (oldVo instanceof IExAggVO) {
			IExAggVO exAggVo = (IExAggVO) oldVo;
			IExAggVO exNewAggVo = (IExAggVO) retVo;
			exAggVo.setParentVO(exNewAggVo.getParentVO());
			for (int i = 0; i < exAggVo.getTableCodes().length; i++) {
				Vector v = new Vector();
				String tableCode = exAggVo.getTableCodes()[i];
				SuperVO[] items = (SuperVO[]) exAggVo.getTableVO(tableCode);
				SuperVO[] newitems = (SuperVO[]) exNewAggVo.getTableVO(tableCode);
				if (items != null)
					fillUITotalVO(items, newitems);

				// //SuperVO[] vos = null;
				// //if (v.size() > 0)
				// //{
				// //vos =
				// //(SuperVO[])
				// java.lang.reflect.Array.newInstance(v.get(0).getClass(),
				// v.size());
				// //v.copyInto(vos);
				// //}
				// exNewAggVo.setTableVO(tableCode, items);
			}
			return oldVo;
		}
		if (retVo.getChildrenVO() == null || retVo.getChildrenVO().length == 0)
			retVo.setChildrenVO(oldVo.getChildrenVO());
		return retVo;
	}

	/**
	 * ���UI�� �������ڣ�(2004-1-15 13:55:07)
	 */
	protected AbstractBillUI getUI() {
		return this.m_billUI;
	}

	/**
	 * processAction ����ע�⡣
	 */
	public Object processAction(String actionname, nc.vo.pub.AggregatedValueObject billVO, String billType, String billDate, Object userObj) throws Exception {
		return PfUtilClient.processAction(getUI(), actionname, billType, billDate, billVO, userObj);
	}

	/**
	 * processAction ����ע�⡣
	 */
	public Object processActionBatch(String actionname, nc.vo.pub.AggregatedValueObject[] billVO, String billType, String billDate, Object[] userObj) throws Exception {
		return PfUtilClient.processBatch(getUI(), actionname, billType, billDate, billVO, userObj);
	}

	/**
	 * ���������������ݲ�ѯ
	 */
	public SuperVO[] queryHeadAllData(Class headVoClass, String strBillType, String strWhere) throws Exception {
		BilltypeVO billVo = PfUIDataCache.getBillType(strBillType);
		if (billVo.getWherestring() != null && billVo.getWherestring().length() != 0) {
			if (strWhere != null)
				strWhere = strWhere + " and (" + billVo.getWherestring() + ")";
			else
				strWhere = billVo.getWherestring();
		}
		return HYPubBO_Client.queryByCondition(headVoClass, strWhere);
	}

	/**
	 * save ����ע�⡣
	 */
	public nc.vo.pub.AggregatedValueObject save(nc.vo.pub.AggregatedValueObject billVO, String billType, String billDate, Object userObj, AggregatedValueObject checkVo) throws Exception {

		setBillStatus(billVO);

		String strBeforeUIClass = null;
		if (userObj != null && userObj instanceof IUIBeforeProcAction)
			strBeforeUIClass = userObj.getClass().getName();

		ArrayList retAry = (ArrayList) PfUtilClient.processAction(getUI(), IPFACTION.SAVE, billType, billDate, billVO, userObj, strBeforeUIClass, checkVo);
		AggregatedValueObject retVo = (AggregatedValueObject) retAry.get(1);
		return fillUIData(retVo, checkVo);
	}

	/**
	 * saveAndCommit ����ע�⡣
	 */
	public nc.vo.pub.AggregatedValueObject saveAndCommit(nc.vo.pub.AggregatedValueObject billVO, java.lang.String billType, java.lang.String billDate, java.lang.Object userObj, nc.vo.pub.AggregatedValueObject checkVo) throws java.lang.Exception {
		setBillStatus(billVO);

		String strBeforeUIClass = null;
		if (userObj != null && userObj instanceof IUIBeforeProcAction)
			strBeforeUIClass = userObj.getClass().getName();

		ArrayList retAry = (ArrayList) PfUtilClient.processAction(getUI(), IPFACTION.COMMIT, billType, billDate, billVO, userObj, strBeforeUIClass, checkVo);
		AggregatedValueObject retVo = (AggregatedValueObject) retAry.get(1);
		return fillUIData(retVo, checkVo);
	}

	/**
	 * ����״̬�� �������ڣ�(2003-9-27 14:14:15)
	 * 
	 * @param billvo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	protected void setBillStatus(AggregatedValueObject billvo) {
		Integer billstatus = (Integer) billvo.getParentVO().getAttributeValue(getUI().getBillField().getField_BillStatus());

		if (billstatus == null || billstatus.intValue() == IBillStatus.NOPASS) {
			billvo.getParentVO().setAttributeValue(getUI().getBillField().getField_CheckMan(), null);
			billvo.getParentVO().setAttributeValue(getUI().getBillField().getField_CheckDate(), null);
			billvo.getParentVO().setAttributeValue(getUI().getBillField().getField_CheckNote(), null);
			billvo.getParentVO().setAttributeValue(getUI().getBillField().getField_BillStatus(), new Integer(IBillStatus.FREE));
		}

	}

	/**
	 * unapprove ����ע�⡣
	 */
	public nc.vo.pub.AggregatedValueObject unapprove(nc.vo.pub.AggregatedValueObject billVO, String billType, String billDate, Object userObj) throws Exception {
		return (AggregatedValueObject) PfUtilClient.processActionFlow(getUI(), IPFACTION.UNAPPROVE, billType, billDate, billVO, userObj);
	}
}
