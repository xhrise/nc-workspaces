package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA47TOZA46��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-15)
 * @author��ƽ̨�ű�����
 */
public class CHGZA47TOZA46 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA47TOZA46 ������ע�⡣
 */
public CHGZA47TOZA46() {
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
		"H_pk_corp->SYSCORP",
		"H_dmakedate->SYSDATE",
		"H_vsourcebillid->H_pk_rkd",
		"H_coperatorid->SYSOPERATOR",
		"B_pk_corp->SYSCORP",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_blmount->B_rkmount",
        "B_pk_unit->B_pk_unit",
		"B_pk_fjbom->B_vsourcebillid",
		"B_pk_cpinvbasdocs->B_memo"
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
