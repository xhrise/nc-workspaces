package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA20TOZA23的VO的动态转换类。
 *
 * 创建日期：(2008-4-23)
 * @author：平台脚本生成
 */
public class CHGZA20TOZA23 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA20TOZA23 构造子注解。
 */
public CHGZA20TOZA23() {
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
        "H_def_2->H_billno",            // 保存源单号
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
