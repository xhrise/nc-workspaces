/*
 * �������� 2006-6-27
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.sc.h0451505;

import java.util.Hashtable;

import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.eh.sc.h0451505.ScPgdBVO;
import nc.vo.eh.sc.h0451505.ScPgdPsnVO;
import nc.vo.pub.SuperVO;

/**
 * ����:�ɹ���
 * @author ����
 * 2008��5��7��9:42:04
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
		String whereSql="pk_pgd='"+key+"'";

		for(int i=0;i<len;i++){
			if ("eh_sc_pgd_b".equals(tableCodes[i]))
				vos=queryByCondition(ScPgdBVO.class, whereSql);
			else if ("eh_sc_pgd_psn".equals(tableCodes[i]))
				vos=queryByCondition(ScPgdPsnVO.class, whereSql);
           
			if (vos!=null)
			    ht.put(tableCodes[i],vos);
		}
		return ht;
	}
}
