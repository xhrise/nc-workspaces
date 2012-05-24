package nc.ui.eh.stock.h0150315;



import java.util.Hashtable;

import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.eh.stock.h0150315.StockWjdecisionBVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionCVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionDVO;
import nc.vo.pub.SuperVO;

/**
 * ���ɹ�����
 * ZB23
 * @author wangbing
 * 2009-1-7 17:38:34
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

