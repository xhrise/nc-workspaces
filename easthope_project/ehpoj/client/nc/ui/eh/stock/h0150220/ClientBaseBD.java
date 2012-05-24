package nc.ui.eh.stock.h0150220;



import java.util.Hashtable;

import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.eh.stock.h0150220.StockDecisionBVO;
import nc.vo.eh.stock.h0150220.StockDecisionCVO;
import nc.vo.eh.stock.h0150220.StockDecisionDVO;
import nc.vo.eh.stock.h0150220.StockDecisionEVO;
import nc.vo.pub.SuperVO;

/**
 * �ɹ�����
 * ZB19	
 * @author wangbing
 * 2008-12-29 8:57:25
 */
public class ClientBaseBD extends BusinessDelegator {
   

	public ClientBaseBD() {
		super();
		// TODO �Զ����ɹ��캯�����
	}
    
	/*
	* ��Ʒ����ʵ�ָ÷�����
	* ������е�������ͬһ�������Բ�����ݽ��з��࣬������Բ�ͬ��ҳǩ�������ݿ��ѯ
	* ע�⣺����=tableCode,ֵ=VO[]
	* ���ڶ��ӱ��UI,�������ش˷���
	* �������ڣ�(2003-7-2 8:38:37)
	* @return java.util.Vector
	* @param tableCodes java.lang.String[]
	* @param key java.lang.String
	* @exception java.lang.Exception �쳣˵����
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

