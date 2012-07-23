package nc.ui.ehpta.hq010101;

import java.util.Hashtable;

import nc.vo.ehpta.hq010101.TransportContractBVO;

/**
 * 
 * �����ҵ�������
 * 
 * @author author
 * @version tempProject version
 */
public class Delegator extends nc.ui.trade.bsdelegate.BusinessDelegator {

	public Hashtable loadChildDataAry(String[] tableCodes, String key)
			throws Exception {

		Hashtable dataHashTable = new Hashtable();

		TransportContractBVO[] bodyVOs1 = (TransportContractBVO[]) queryByCondition( TransportContractBVO.class, getBodyCondition( TransportContractBVO.class, key));
		
		if (bodyVOs1 != null && bodyVOs1.length > 0) {

			dataHashTable.put("ehpta_transport_contract_b", bodyVOs1);
		}

		return dataHashTable;

	}

	/**
	 * 
	 * �÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
	 * 
	 */
	public String getBodyCondition(Class bodyClass, String key) {

		if (bodyClass == nc.vo.ehpta.hq010101.TransportContractBVO.class)
			return "pk_transport = '" + key + "' and isnull(dr,0)=0 ";

		return null;
	}

}
