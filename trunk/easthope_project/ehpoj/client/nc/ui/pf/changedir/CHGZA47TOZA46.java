package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA47TOZA46的VO的动态转换类。
 *
 * 创建日期：(2008-5-15)
 * @author：平台脚本生成
 */
public class CHGZA47TOZA46 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA47TOZA46 构造子注解。
 */
public CHGZA47TOZA46() {
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
		"H_pk_corp->SYSCORP",
		"H_dmakedate->SYSDATE",
		"H_vsourcebillid->H_pk_rkd",
		"H_coperatorid->SYSOPERATOR",
		"B_pk_corp->SYSCORP",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_blmount->B_rkmount",
        "B_pk_unit->B_pk_unit",
		"B_pk_fjbom->B_vsourcebillid",
		"B_pk_cpinvbasdocs->B_memo"
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
