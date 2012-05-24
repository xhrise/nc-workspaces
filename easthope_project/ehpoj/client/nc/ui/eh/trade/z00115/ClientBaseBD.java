
package nc.ui.eh.trade.z00115;

import java.util.Hashtable;

import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.eh.trade.z00115.CubinvbasdocVO;
import nc.vo.eh.trade.z00115.CustaddrVO;
import nc.vo.eh.trade.z00115.CustkxlVO;
import nc.vo.eh.trade.z00115.CustyxdbVO;
import nc.vo.pub.SuperVO;

/**
 * ���̵���
 * @author ����
 * �������� 2008-4-1 16:09:43
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
