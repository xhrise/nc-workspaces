package nc.ui.eh.stock.h0150315;



import java.util.Hashtable;

import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.eh.stock.h0150315.StockWjdecisionBVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionCVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionDVO;
import nc.vo.pub.SuperVO;

/**
 * 五金采购决策
 * ZB23
 * @author wangbing
 * 2009-1-7 17:38:34
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
	@SuppressWarnings("unchecked")
	public Hashtable loadChildDataAry(String[] tableCodes, String key)
		throws java.lang.Exception {
		Hashtable ht=new Hashtable();
		SuperVO[] vos=null;
		int len=tableCodes.length;
		if (len==0)
			return null;	
        String whereSql = "pk_wjdecision='"+key+"'";
		for(int i=0;i<len;i++){
			if ("eh_stock_wjdecision_b".equals(tableCodes[i]))
				vos=queryByCondition(StockWjdecisionBVO.class, whereSql);
			else if ("eh_stock_wjdecision_c".equals(tableCodes[i]))
				vos=queryByCondition(StockWjdecisionCVO.class, whereSql);
			else if ("eh_stock_wjdecision_d".equals(tableCodes[i]))
				vos=queryByCondition(StockWjdecisionDVO.class, whereSql);
			if (vos!=null)
			    ht.put(tableCodes[i],vos);
		}
		return ht;
	}
}

