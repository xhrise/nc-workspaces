package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA17TOZA20��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-12)
 * @author��ƽ̨�ű�����
 */
public class CHGZA17TOZA20 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA17TOZA20 ������ע�⡣
 */
public CHGZA17TOZA20() {
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
		"H_vsourcebillid->H_pk_contract",
		"H_pk_deptdoc->H_pk_deptdoc",
		"H_pk_psndoc->H_pk_psndoc",
		"H_pk_currency->H_pk_currency",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_vsourcetype->H_vbilltype",
		"B_pk_unit->B_pk_unit",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_amount->B_amount",
		"B_taxinprice->B_taxinprice",
		"B_vsourcebillid->B_pk_contract_b",
		"B_vsourcebillrowid->B_pk_contract",
		"B_ysamount->B_def_9",
		"B_packagweight->B_packagweight",
        "B_allcheck->B_def_5"
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
