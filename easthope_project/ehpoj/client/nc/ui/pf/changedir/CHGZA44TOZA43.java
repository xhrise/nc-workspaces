package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA44TOZA43��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-7)
 * @author��ƽ̨�ű�����
 */
public class CHGZA44TOZA43 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA44TOZA43 ������ע�⡣
 */
public CHGZA44TOZA43() {
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
		"H_vsourcebilltype->H_vbilltype",
		"H_vbilltype->H_vbilltype",
		"H_pk_busitype->H_pk_busitype",
		"H_vsourcebillid->H_pk_posm",
		"H_coperatorid->SYSOPERATOR",
		"H_dmakedate->SYSDATE",
		"H_memo->H_memo",
		"B_scmount->B_scmount",
		"B_ver->B_ver",
		"B_pk_corp->SYSCORP",
		"B_pk_posm->H_pk_posm",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_color->B_color",
		"B_pk_unit->B_pk_unit",
		"B_hjinvbasdoc->B_hjinvbasdoc",
		"B_vsourcebillid->B_pk_posm_b",
		"B_vsourcebillrowid->B_pk_posm",
		"B_xh->B_xh"
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
