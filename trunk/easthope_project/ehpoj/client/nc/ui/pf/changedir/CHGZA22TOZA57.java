package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA22TOZA57��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-30)
 * @author��ƽ̨�ű�����
 */
public class CHGZA22TOZA57 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA22TOZA57 ������ע�⡣
 */
public CHGZA22TOZA57() {
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
		"H_pk_deptdoc->H_pk_deptodc",
		"H_pk_psndoc->H_pk_cgpsn",
		"H_pk_hth->H_pk_contract",
		"H_vsourcebilltype->H_vbilltype",
		"H_vsourcebillid->H_pk_in",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"B_vsourcebillid->B_pk_in_b",
		"B_vsourcebillrowid->B_pk_in",
		"B_rkmount->B_inamount",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pricemny->B_inprice"
		
		
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
