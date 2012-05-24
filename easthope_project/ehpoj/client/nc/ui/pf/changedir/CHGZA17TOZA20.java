package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA17TOZA20的VO的动态转换类。
 *
 * 创建日期：(2008-5-12)
 * @author：平台脚本生成
 */
public class CHGZA17TOZA20 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA17TOZA20 构造子注解。
 */
public CHGZA17TOZA20() {
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
		"H_vsourcebillid->H_pk_contract",
		"H_pk_deptdoc->H_pk_deptdoc",
		"H_pk_psndoc->H_pk_psndoc",
		"H_pk_currency->H_pk_currency",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_vsourcetype->H_vbilltype",
		"B_pk_unit->B_pk_unit",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_amount->B_amount",
		"B_taxinprice->B_taxinprice",
		"B_vsourcebillid->B_pk_contract_b",
		"B_vsourcebillrowid->B_pk_contract",
		"B_ysamount->B_def_9",
		"B_packagweight->B_packagweight",
        "B_allcheck->B_def_5"
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
