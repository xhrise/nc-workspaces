package nc.ui.ehpta.hq0305;

import java.util.Hashtable;

import nc.vo.ehpta.hq0305.SaleBalanceBVO;

/**
 * 
 * �����ҵ�������
 * 
 * @author author
 * @version tempProject version
 */
public class Delegator extends
		nc.ui.trade.bsdelegate.BusinessDelegator {

	public Hashtable loadChildDataAry(String[] tableCodes, String key)
			throws Exception {

		Hashtable dataHashTable = new Hashtable();

		SaleBalanceBVO[] bodyVOs1 = (SaleBalanceBVO[]) queryByCondition(
				SaleBalanceBVO.class,
				getBodyCondition(SaleBalanceBVO.class, key));
		if (bodyVOs1 != null && bodyVOs1.length > 0) {

			dataHashTable.put("ehpta_calc_sale_balance_b", bodyVOs1);
		}

		return dataHashTable;

	}

	/**
	 * 
	 * �÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
	 * 
	 */
	public String getBodyCondition(Class bodyClass, String key) {

		if (bodyClass == SaleBalanceBVO.class)
			return "pk_sale_balance = '" + key + "' and isnull(dr,0)=0 ";

		return null;
	}

}
