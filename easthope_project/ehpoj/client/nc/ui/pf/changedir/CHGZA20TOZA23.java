package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA20TOZA23��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-4-23)
 * @author��ƽ̨�ű�����
 */
public class CHGZA20TOZA23 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA20TOZA23 ������ע�⡣
 */
public CHGZA20TOZA23() {
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
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_shdate->H_receiptdate",
		"H_pk_invbasdoc->H_def_1",
		"H_pk_corp->SYSCORP",
		"H_rkamount->H_def_6",
		"H_dnum->H_def_6",
		"H_vsourcebilltype->H_vbilltype",
		"H_vsourcebillid->H_pk_receipt",
		"H_pk_receipt_b->H_def_2",
		"H_pk_receipt->H_pk_receipt",
		"H_vsbbilltype->H_vbilltype",
		"H_dmakedate->SYSDATE",
		"H_def_1->H_pk_receipt",
        "H_def_2->H_billno",            // ����Դ����
        "H_custname->H_def_5",
        "H_shname->H_def_4",
        "H_carnum->H_carnumber",
        "H_cpnum->H_def_3",
		"B_pk_corp->SYSCORP",
		"B_itemno->B_pk_unit",
		"B_pk_project->B_gg",
		"B_ll_ceil->B_def_1",
		"B_ll_limit->B_def_2",
		"B_rece_ceil->B_def_3",
		"B_rece_limit->B_def_4",
        "B_def_2->B_def_5",
        "B_checkresult->B_dr",
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
