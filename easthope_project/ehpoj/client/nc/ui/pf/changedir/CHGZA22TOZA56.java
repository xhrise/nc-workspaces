package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA22TOZA56��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-29)
 * @author��ƽ̨�ű�����
 */
public class CHGZA22TOZA56 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA22TOZA56 ������ע�⡣
 */
public CHGZA22TOZA56() {
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
//		"H_pk_hth->H_pk_contract",
		"H_vsourcebilltype->H_vbilltype",
		"H_vsourcebillid->H_pk_in",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"B_vsourcebillrowid->B_pk_in_b",
		"B_vsourcebillid->B_pk_ins",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_htmount->B_def_7",
		"B_rkmount->B_def_6",
		"B_pricemny->B_inprice",
//		"B_yztmount->B_def_8",
        "B_ztmount->B_def_8"
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
