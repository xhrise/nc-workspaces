package nc.ui.eh.stock.h0150220;



import java.util.Hashtable;

import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.eh.stock.h0150220.StockDecisionBVO;
import nc.vo.eh.stock.h0150220.StockDecisionCVO;
import nc.vo.eh.stock.h0150220.StockDecisionDVO;
import nc.vo.eh.stock.h0150220.StockDecisionEVO;
import nc.vo.pub.SuperVO;

/**
 * 采购决策
 * ZB19	
 * @author wangbing
 * 2008-12-29 8:57:25
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
        String whereSql = "pk_decision='"+key+"'";
		for(int i=0;i<len;i++){
			if ("eh_stock_decision_b".equals(tableCodes[i]))
				vos=queryByCondition(StockDecisionBVO.class, whereSql);
			else if ("eh_stock_decision_c".equals(tableCodes[i]))
				vos=queryByCondition(StockDecisionCVO.class, whereSql);
            else if ("eh_stock_decision_d".equalsIgnoreCase(tableCodes[i]))
                vos=queryByCondition(StockDecisionDVO.class,whereSql);
            else if ("eh_stock_decision_e".equalsIgnoreCase(tableCodes[i]))
                vos=queryByCondition(StockDecisionEVO.class,whereSql);
			if (vos!=null)
			    ht.put(tableCodes[i],vos);
		}
		return ht;
	}
}

