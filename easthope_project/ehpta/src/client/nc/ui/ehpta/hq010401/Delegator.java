package nc.ui.ehpta.hq010401;

import java.util.Hashtable;

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

		nc.vo.ehpta.hq010401.SaleContractBsVO[] bodyVOs1 = (nc.vo.ehpta.hq010401.SaleContractBsVO[]) queryByCondition(
				nc.vo.ehpta.hq010401.SaleContractBsVO.class,
				getBodyCondition(
						nc.vo.ehpta.hq010401.SaleContractBsVO.class, key));
		if (bodyVOs1 != null && bodyVOs1.length > 0) {

			dataHashTable.put("ehpta_sale_contract_bs", bodyVOs1);
		}

		return dataHashTable;

	}

	/**
	 * 
	 * �÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
	 * 
	 */
	public String getBodyCondition(Class bodyClass, String key) {

		if (bodyClass == nc.vo.ehpta.hq010401.SaleContractBsVO.class)
			return "pk_contract = '" + key + "' and isnull(dr,0)=0 ";

		return null;
	}

}
