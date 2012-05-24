package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA18TOZA23的VO的动态转换类。
 *
 * 创建日期：(2008-4-24)
 * @author：平台脚本生成
 */
public class CHGZA18TOZA23 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA18TOZA23 构造子注解。
 */
public CHGZA18TOZA23() {
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
		"H_pk_invbasdoc->H_pk_invbasdoc",
		"H_pk_corp->SYSCORP",
		"H_vsourcebilltype->H_vbilltype",
//		"H_sbbillnolist->H_def_11",
		"H_vsourcebillid->H_pk_sbbill",
		"H_pk_receipt_b->H_vsourcebillrowid",
		"H_pk_receipt->H_vsourcebillid",
		"H_pk_sbbills->H_def_2",
		"H_vsbbilltype->H_vbilltype",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_dmakedate->SYSDATE",
        "H_def_2->H_def_1",
		"H_def_6->H_suttle",
        "H_custname->H_def_11",
//        "H_custcode->H_def_10",
        "H_shname->H_def_12",
        "H_carnum->H_def_13",
        "H_cpnum->H_def_14",
		"B_itemno->B_def_1",
		"B_pk_project->B_def_2",
		"B_ll_ceil->B_def_3",
		"B_ll_limit->B_def_4",
		"B_rece_ceil->B_def_5",
		"B_rece_limit->B_def_6",
        "B_def_2->B_def_7",
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
