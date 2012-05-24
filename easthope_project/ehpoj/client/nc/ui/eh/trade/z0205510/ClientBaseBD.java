/*
 * 创建日期 2006-6-27
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.trade.z0205510;

import java.util.Hashtable;

import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.eh.trade.z0205510.SeconddiscountCheckinvVO;
import nc.vo.eh.trade.z0205510.SeconddiscountPoliceVO;
import nc.vo.eh.trade.z0205510.SeconddiscountRangeVO;
import nc.vo.pub.SuperVO;

/**
 * @author answer
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class ClientBaseBD extends BusinessDelegator {
   

	public ClientBaseBD() {
		super();
		// TODO 自动生成构造函数存根
	}
    
	/*
	* 产品必须实现该方法。
	* 如果所有的数据在同一个表，可以查出数据进行分类，否则针对不同的页签进行数据库查询
	* 注意：主键=tableCode,值=VO[]
	* 对于多子表的UI,必须重载此方法
	* 创建日期：(2003-7-2 8:38:37)
	* @return java.util.Vector
	* @param tableCodes java.lang.String[]
	* @param key java.lang.String
	* @exception java.lang.Exception 异常说明。
	*/
	@Override
	public Hashtable loadChildDataAry(String[] tableCodes, String key)
		throws java.lang.Exception {
		Hashtable ht=new Hashtable();
		SuperVO[] vos=null;
		int len=tableCodes.length;
		if (len==0)
			return null;
		String whereSql="pk_seconddiscount='"+key+"'";

		for(int i=0;i<len;i++){
			if ("range".equals(tableCodes[i]))
				vos=queryByCondition(SeconddiscountRangeVO.class, whereSql);
			else if ("polic".equals(tableCodes[i]))
				vos=queryByCondition(SeconddiscountPoliceVO.class, whereSql);
			else if ("checkinv".equals(tableCodes[i]))
				vos=queryByCondition(SeconddiscountCheckinvVO.class, whereSql);
			if (vos!=null)
			    ht.put(tableCodes[i],vos);
		}
		return ht;
	}
}
