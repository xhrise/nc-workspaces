package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA30TOZA11��VO�Ķ�̬ת���ࡣ
 * ��ⱨ��->�ػ�����
 * �������ڣ�(2008-5-5)
 * @author��ƽ̨�ű�����
 */
public class CHGZA30TOZA11 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA10TOZA11 ������ע�⡣
 */
public CHGZA30TOZA11() {
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
		"H_outdate->H_dmakedate",
		"H_dmakedate->SYSDATE",
		"H_coperatorid->SYSOPERATOR",
		"H_pk_corp->SYSCORP",
		"H_vsourcebilltype->H_vbilltype",
		"H_vsourcebillid->H_pk_checkreport"
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
