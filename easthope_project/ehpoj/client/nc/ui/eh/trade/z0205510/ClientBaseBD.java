/*
 * �������� 2006-6-27
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
