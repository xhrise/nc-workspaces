package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA17TOZA20��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-12)
 * @author��ƽ̨�ű�����
 */
public class CHGZB19TOZA20 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA17TOZA20 ������ע�⡣
 */
public CHGZB19TOZA20() {
	super();
}
/**
* ��ú������ȫ¼�����ơ�
* @return java.lang.String[]
*/
@Override
public String getAfterClassName() {
	return null;
}
/**
* �����һ���������ȫ¼�����ơ�
* @return java.lang.String[]
*/
@Override
public String getOtherClassName() {
	return null;
}
/**
* ����ֶζ�Ӧ��
* @return java.lang.String[]
*/
@Override
public String[] getField() {
	return new String[] {
		"H_vsourcebillid->H_pk_decision",
		"B_pk_invbasdoc->H_pk_invbasdoc",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"B_inamount->H_xzcgamount",//modify by houcq 2011-03-18
		"H_vsourcetype->H_vbilltype",//add by houcq 2011-03-18 ��Դ��������
		"B_amount->H_xzcgamount",  //modify by houcq ��ͬ�������ղɹ�����
		"B_taxinprice->H_hsprice",
		"H_pk_psndoc->H_pk_psndoc",
		"B_allcheck->B_ischeck",
		
	};
}
/**
* ��ù�ʽ��
* @return java.lang.String[]
*/
@Override
public String[] getFormulas() {
	return null;
}
/**
* �����û��Զ��庯����
*/
@Override
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
