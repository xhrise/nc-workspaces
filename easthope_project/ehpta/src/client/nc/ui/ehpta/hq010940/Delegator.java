package nc.ui.ehpta.hq010940;

import java.util.Hashtable;

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

		nc.vo.ehpta.hq010940.CalcStorfeeBVO[] bodyVOs1 = (nc.vo.ehpta.hq010940.CalcStorfeeBVO[]) queryByCondition(
				nc.vo.ehpta.hq010940.CalcStorfeeBVO.class,
				getBodyCondition(nc.vo.ehpta.hq010940.CalcStorfeeBVO.class, key));
		if (bodyVOs1 != null && bodyVOs1.length > 0) {

			dataHashTable.put("ehpta_calc_storfee_b", bodyVOs1);
		}

		return dataHashTable;

	}

	/**
	 * 
	 * 该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
	 * 
	 */
	public String getBodyCondition(Class bodyClass, String key) {

		if (bodyClass == nc.vo.ehpta.hq010940.CalcStorfeeBVO.class)
			return "pk_storfee = '" + key + "' and isnull(dr,0)=0 ";

		return null;
	}

}
