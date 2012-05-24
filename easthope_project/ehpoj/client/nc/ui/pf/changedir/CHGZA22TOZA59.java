package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA22TOZA59的VO的动态转换类。
 *
 * 创建日期：(2008-5-29)
 * @author：平台脚本生成
 */
public class CHGZA22TOZA59 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA22TOZA59 构造子注解。
 */
public CHGZA22TOZA59() {
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
		"H_pk_deptdoc->H_pk_deptodc",
		"H_pk_psndoc->H_pk_cgpsn",
		"H_def_1->H_pk_stock",//add by houcq 2011-05-20
		"H_vsourcebilltype->H_vbilltype",
		"H_vsourcebillid->H_pk_in",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_shinfo->H_psninfo",
		"H_pk_hth->H_pk_contract",
		"B_vsourcebillid->B_pk_in",
		"B_vsourcebillrowid->B_pk_in_b",
		"B_taxinprice->B_inprice",
		"B_pk_unit->B_pk_unit",
		"B_pk_invbasdoc->B_pk_invbasdoc",
//		"B_pk_rkd->H_billno",
		"B_kpmount->B_def_9",
		"B_pk_rkd->B_def_1",
		"B_rkmount->B_inamount",
		"B_fpmount->B_def_8",
//		"B_taxinmony->B_def_7",  	//含税金额
//		"B_notaxmoney->B_def_6", 	//不含税金额
//		"B_taxmny->B_def_10"		//税额
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
