package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA10TOZA11��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-5)
 * @author��ƽ̨�ű�����
 */
public class CHGZB29TOZB30 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA10TOZA11 ������ע�⡣
 */
public CHGZB29TOZB30() {
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
		"H_whdate->SYSDATE",
		"H_dmakedate->SYSDATE",
		"H_carnumber->H_carnumber",
		"H_coperatorid->SYSOPERATOR",
		"H_pk_corp->SYSCORP",
		"H_vsourcebilltype->ZB29",
		"H_vsourcebillid->H_pk_plan",
		"B_pk_equipment->B_pk_equipment",
		"B_pk_sb_b->B_pk_sb_b",
		"B_spk_sb->B_spk_sb",
		"B_whdate->B_plandate",
		"B_vsourcebillid->H_pk_plan",
		"B_vsoucebillrowid->B_pk_plan_b",
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
