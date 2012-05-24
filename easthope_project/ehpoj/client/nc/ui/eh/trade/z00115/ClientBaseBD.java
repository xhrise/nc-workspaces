
package nc.ui.eh.trade.z00115;

import java.util.Hashtable;

import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.eh.trade.z00115.CubinvbasdocVO;
import nc.vo.eh.trade.z00115.CustaddrVO;
import nc.vo.eh.trade.z00115.CustkxlVO;
import nc.vo.eh.trade.z00115.CustyxdbVO;
import nc.vo.pub.SuperVO;

/**
 * 客商档案
 * @author 王明
 * 创建日期 2008-4-1 16:09:43
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
        String whereSql = "pk_cust = '"+key+"'";
		for(int i=0;i<len;i++){
			if ("ADDR".equals(tableCodes[i]))
				vos=queryByCondition(CustaddrVO.class, whereSql);
			else if ("kxl".equals(tableCodes[i]))
				vos=queryByCondition(CustkxlVO.class, whereSql);
            else if ("yxdb".equalsIgnoreCase(tableCodes[i]))
                vos=queryByCondition(CustyxdbVO.class,whereSql);
            else if ("eh_cubinvbasdoc".equalsIgnoreCase(tableCodes[i]))
                vos=queryByCondition(CubinvbasdocVO.class,whereSql);
			if (vos!=null)
			    ht.put(tableCodes[i],vos);
		}
		return ht;
	}
}
