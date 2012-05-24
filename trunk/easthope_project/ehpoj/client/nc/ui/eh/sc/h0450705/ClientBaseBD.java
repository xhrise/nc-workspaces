package nc.ui.eh.sc.h0450705;



import java.util.Hashtable;

import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.eh.sc.h0450705.ScMrpBVO;
import nc.vo.eh.sc.h0450705.ScMrpCVO;
import nc.vo.pub.SuperVO;

/**
 * MRP����
 * ZB32
 * @author wangbing
 * 2008-12-20 16:18:55
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

