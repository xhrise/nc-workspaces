package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA30TOZA22的VO的动态转换类。
 *
 * 创建日期：(2008-4-21)
 * @author：平台脚本生成
 */
public class CHGZA30TOZA22 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA30TOZA22 构造子注解。
 */
public CHGZA30TOZA22() {
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
		"H_vsourcebilltype->H_vbilltype",
		"H_indate->SYSDATE",
		"H_vsourcebillid->H_pk_checkreport",
		"H_vsourcebillno->H_billno",
		"H_memo->H_memo",
		"H_pk_cubasdoc->H_def_3",
		"H_psninfo->H_def_1",
		"H_carnumber->H_def_2",
		"H_pk_deptodc->H_def_4",
		"H_pk_cgpsn->H_def_5",
		"H_pk_contract->H_note",
		"H_def_1->H_vsourcebillid",	
		
		//****************扣重扣价
		"B_kz->B_def_11",
		"B_kj->B_def_12",
		"B_tkj->B_def_13",
		"B_deduamount->B_def_11",
		"B_deduprice->B_def_12",
		//**************
		//"B_poundamount->B_def_9",
		"B_poundamount->B_def_7",//modify by houcq 2011-02-18
		"B_vsourcebillid->B_pk_project",
		"B_inprice->B_def_2",
		"B_taxinprice->B_def_2",
		"B_pk_unit->B_def_4",
		"B_packagweight->B_def_6",
		"B_inamount->B_def_7",
		"B_def_8->B_def_7",
		
		"B_pk_invbasdoc->B_def_5",
		"B_def_3->B_def_3",
		"B_def_9->B_def_8",
		"B_def_6->B_def_10"
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
