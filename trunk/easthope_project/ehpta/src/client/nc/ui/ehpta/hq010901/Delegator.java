package nc.ui.ehpta.hq010901;

import java.util.Hashtable;

import nc.vo.ehpta.hq010901.CalcInterestBVO;

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

		CalcInterestBVO[] bodyVOs1 = (CalcInterestBVO[]) queryByCondition(CalcInterestBVO.class,getBodyCondition(CalcInterestBVO.class, key));
		if (bodyVOs1 != null && bodyVOs1.length > 0) {

			dataHashTable.put("ehpta_calc_interest_b", bodyVOs1);
		}

		return dataHashTable;

	}

	/**
	 * 
	 * �÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
	 * 
	 */
	public String getBodyCondition(Class bodyClass, String key) {

		if (bodyClass == CalcInterestBVO.class)
			return "pk_calcinterest = '" + key + "' and isnull(dr,0)=0 ";

		return null;
	}

}
