package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA47TOZA28��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-9)
 * @author��ƽ̨�ű�����
 */
public class CHGZA47TOZA28 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA47TOZA28 ������ע�⡣
 */
public CHGZA47TOZA28() {
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
		"H_vsourcebillid->H_pk_rkd",
		"H_vsourcebilltype->H_vbilltype",
		"H_pk_deptdoc->H_pk_deptdoc",
        "H_pk_cprkds->H_pk_cprkds",
        "H_def_1->H_billno",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_amount->B_rkmount",
		"B_checkamount->B_rkmount",//���ܣ�������������ֵ��ʱ�䣺2009-12-21.���ߣ���־Զ
		"B_pk_unit->B_pk_unit",
		"B_pk_unit->B_pk_unit",
		"B_instalment->H_pc",
        "B_billno->B_def_1",
        "B_vsourcebillid->B_vsourcebillid"
        
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
