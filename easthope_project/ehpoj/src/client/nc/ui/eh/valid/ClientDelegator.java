package nc.ui.eh.valid;

import java.util.Hashtable;

/**
 * 
 * ����ҵ��������ȱʡʵ��
 * 
 * @author author
 * @version tempProject version
 */
public class ClientDelegator extends nc.ui.trade.bsdelegate.BDBusinessDelegator {

	/**
	 * 
	 * 
	 * �÷������ڻ�ȡ��ѯ�������û����Զ�������޸ġ�
	 * 
	 */

	public Hashtable loadChildDataAry(String[] tableCodes, String key)
			throws Exception {

		Hashtable dataHashTable = new Hashtable();
		return dataHashTable;

	}

	/**
	 * 
	 * �÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
	 * 
	 */
	public String getBodyCondition(Class bodyClass, String key) {

		return null;
	}

}