package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA56TOZA57��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-29)
 * @author��ƽ̨�ű�����
 */
public class CHGZA56TOZA57 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA56TOZA57 ������ע�⡣
 */
public CHGZA56TOZA57() {
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
		"H_pk_deptdoc->H_pk_deptdoc",
		"H_pk_psndoc->H_pk_psndoc",
		"H_cubasphone->H_cubasphone",
		"H_fkrq->H_yjfkrq",
		"H_cubasbank->H_cubasbank",
		
		"H_pk_hth->H_pk_hth",
		"H_vsourcebilltype->H_vbilltype",
		"H_memo->H_vapprovenote",
		"H_vsourcebillid->H_pk_fkqs",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_bankaccount->H_bankaccount",
		"H_pk_sfkfs->H_pk_sfkfs",
		"H_sqje->H_fkje",
		"H_def_6->H_def_6",
		"H_def_6->H_yfje",
        "B_rkbillno->B_vbillno",
		"B_vsourcebillid->B_vsourcebillid",
		"B_rkmount->B_rkmount",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pricemny->B_pricemny",
        "B_htbillno->B_htbillno",
        "B_sbamount->B_sbamount",
        "B_kzamount->B_kzamount",
        "B_kjprice->B_kjprice",
        "B_htamount->B_htmount",
        "B_price->B_fkmny",
        "B_yfkje->B_yfkje",
        "B_wfkje->B_wfkje",
        "B_vsourcebillrowid->B_vsourcebillrowid"
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
