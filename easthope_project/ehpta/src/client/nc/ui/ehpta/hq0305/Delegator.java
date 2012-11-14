package nc.ui.ehpta.hq0305;

import java.util.Hashtable;

import nc.vo.ehpta.hq0305.SaleBalanceBVO;

/**
 * 
 * 抽象的业务代理类
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
	 * 该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
	 * 
	 */
	public String getBodyCondition(Class bodyClass, String key) {

		if (bodyClass == SaleBalanceBVO.class)
			return "pk_sale_balance = '" + key + "' and isnull(dr,0)=0 ";

		return null;
	}

}
