package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA23TOZA30的VO的动态转换类。
 *
 * 创建日期：(2008-4-24)
 * @author：平台脚本生成
 */
public class CHGZA23TOZA30 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA23TOZA30 构造子注解。
 */
public CHGZA23TOZA30() {
	super();
}
/**
* 获得后续类的全录经名称。
* @return java.lang.String[]
*/
@Override
public String getAfterClassName() {
	return null;
}
/**
* 获得另一个后续类的全录径名称。
* @return java.lang.String[]
*/
@Override
public String getOtherClassName() {
	return null;
}
/**
* 获得字段对应。
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
* 获得公式。
* @return java.lang.String[]
*/
@Override
public String[] getFormulas() {
	return null;
}
/**
* 返回用户自定义函数。
*/
@Override
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
