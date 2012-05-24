package nc.ui.eh.sc.h0450705;



import java.util.Hashtable;

import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.eh.sc.h0450705.ScMrpBVO;
import nc.vo.eh.sc.h0450705.ScMrpCVO;
import nc.vo.pub.SuperVO;

/**
 * MRP运算
 * ZB32
 * @author wangbing
 * 2008-12-20 16:18:55
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
        String whereSql = " pk_mrp='"+key+"'";
		for(int i=0;i<len;i++){
			if ("eh_sc_mrp_b".equals(tableCodes[i]))
				vos=queryByCondition(ScMrpBVO.class, whereSql);
			else if ("eh_sc_mrp_c".equals(tableCodes[i]))
				vos=queryByCondition(ScMrpCVO.class, whereSql);
			if (vos!=null)
			    ht.put(tableCodes[i],vos);
		}
		return ht;
	}
}

