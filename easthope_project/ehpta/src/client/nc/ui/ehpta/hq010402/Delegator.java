package nc.ui.ehpta.hq010402;

import java.util.Hashtable;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;

/**
 * 
 * @author author
 * @version tempProject version
 */
public class Delegator extends BDBusinessDelegator {

	public Hashtable loadChildDataAry(String[] tableCodes, String key)
			throws Exception {

		Hashtable dataHashTable = new Hashtable();

		nc.vo.ehpta.hq010402.SaleContractBVO[] bodyVOs1 = (nc.vo.ehpta.hq010402.SaleContractBVO[]) queryByCondition(
				nc.vo.ehpta.hq010402.SaleContractBVO.class,
				getBodyCondition(
						nc.vo.ehpta.hq010402.SaleContractBVO.class, key));
		if (bodyVOs1 != null && bodyVOs1.length > 0) {

			dataHashTable.put("ehpta_sale_contract_b", bodyVOs1);
		}

		nc.vo.ehpta.hq010402.AidcustVO[] bodyVOs2 = (nc.vo.ehpta.hq010402.AidcustVO[]) queryByCondition(
				nc.vo.ehpta.hq010402.AidcustVO.class,
				getBodyCondition(nc.vo.ehpta.hq010402.AidcustVO.class, key));
		if (bodyVOs2 != null && bodyVOs2.length > 0) {

			dataHashTable.put("ehpta_aidcust", bodyVOs2);
		}

//		nc.vo.ehpta.hq010402.PrepolicyVO[] bodyVOs3 = (nc.vo.ehpta.hq010402.PrepolicyVO[]) queryByCondition(
//				nc.vo.ehpta.hq010402.PrepolicyVO.class,
//				getBodyCondition(nc.vo.ehpta.hq010402.PrepolicyVO.class,
//						key));
//		if (bodyVOs3 != null && bodyVOs3.length > 0) {
//
//			dataHashTable.put("ehpta_prepolicy", bodyVOs3);
//		}

		return dataHashTable;

	}

	/**
	 * 
	 * 该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
	 * 
	 */
	public String getBodyCondition(Class bodyClass, String key) {

		if (bodyClass == nc.vo.ehpta.hq010402.SaleContractBVO.class)
			return "pk_contract = '" + key + "' and isnull(dr,0)=0 ";
		if (bodyClass == nc.vo.ehpta.hq010402.AidcustVO.class)
			return "pk_contract = '" + key + "' and isnull(dr,0)=0 ";
//		if (bodyClass == nc.vo.ehpta.hq010402.PrepolicyVO.class)
//			return "pk_contract = '" + key + "' and isnull(dr,0)=0 ";

		return null;
	}

}
