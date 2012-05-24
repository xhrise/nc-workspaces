package nc.ui.eh.valid;

import java.util.Hashtable;

/**
 * 
 * 抽象业务代理类的缺省实现
 * 
 * @author author
 * @version tempProject version
 */
public class ClientDelegator extends nc.ui.trade.bsdelegate.BDBusinessDelegator {

	/**
	 * 
	 * 
	 * 该方法用于获取查询条件，用户可以对其进行修改。
	 * 
	 */

	public Hashtable loadChildDataAry(String[] tableCodes, String key)
			throws Exception {

		Hashtable dataHashTable = new Hashtable();
		return dataHashTable;

	}

	/**
	 * 
	 * 该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
	 * 
	 */
	public String getBodyCondition(Class bodyClass, String key) {

		return null;
	}

}