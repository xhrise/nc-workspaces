package nc.ui.ehpta.hq010930;

import java.util.Hashtable;

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

		nc.vo.ehpta.hq010930.CalcUnderTransfeeBVO[] bodyVOs1 = (nc.vo.ehpta.hq010930.CalcUnderTransfeeBVO[]) queryByCondition(
				nc.vo.ehpta.hq010930.CalcUnderTransfeeBVO.class,
				getBodyCondition(
						nc.vo.ehpta.hq010930.CalcUnderTransfeeBVO.class, key));
		if (bodyVOs1 != null && bodyVOs1.length > 0) {

			dataHashTable.put("ehpta_calc_under_transfee_b", bodyVOs1);
		}

		return dataHashTable;

	}

	/**
	 * 
	 * �÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
	 * 
	 */
	public String getBodyCondition(Class bodyClass, String key) {

		if (bodyClass == nc.vo.ehpta.hq010930.CalcUnderTransfeeBVO.class)
			return "pk_transfee = '" + key + "' and isnull(dr,0)=0 ";

		return null;
	}

}
