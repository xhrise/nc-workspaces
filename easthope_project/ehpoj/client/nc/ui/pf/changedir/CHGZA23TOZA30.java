package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA23TOZA30��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-4-24)
 * @author��ƽ̨�ű�����
 */
public class CHGZA23TOZA30 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA23TOZA30 ������ע�⡣
 */
public CHGZA23TOZA30() {
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
		"H_vsourcebillid->H_pk_sample",
		"H_vbillstatus->H_vbillstatus",
		"H_pk_sample->H_billno",
		"H_pk_corp->H_pk_corp",
		"H_dmakedate->H_dmakedate",
		"H_rkbillno->H_rkbillno",
		"H_pk_busitype->H_pk_busitype",
		"H_pk_invbasdoc->H_pk_invbasdoc",
		"H_pk_receipt_b->H_pk_receipt_b",
		"H_pk_receipt->H_pk_receipt",
		"H_pk_sbbills->H_pk_sbbills",
		"H_vsbbilltype->H_vsbbilltype",
		"H_vsourcebilltype->H_vbilltype",
		//"H_rkamount->H_rkamount",
		"H_rkamount->H_dnum",//modify by houcq 2011-10-29
		"H_def_1->H_def_1",
		"H_def_6->H_def_6",
        "H_spnum->H_dnum",
        "H_cyperson->H_cyperson",
        "H_def_3->H_custcode",
        "H_def_2->H_custname",
        "H_def_5->H_shname",
		"B_itemno->B_itemno",
//		"B_checkresult->B_checkresult",
		"B_rece_ceil->B_rece_ceil",
		"B_rece_limit->B_rece_limit",
		"B_pk_corp->B_pk_corp",
		"B_vsourcebillid->B_pk_checkreport_b",
		"B_pk_project->B_pk_project",
		"B_ll_ceil->B_ll_ceil",
		"B_ll_limit->B_ll_limit",
        "B_fxtype->B_def_2",
        "B_def_8->B_checkresult",
        "H_dnum->H_dnum",
        "H_th_flag->H_th_flag",
        "H_pk_in->H_vsourcebillid"
        
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
