package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA10TOZA11的VO的动态转换类。
 *
 * 创建日期：(2008-5-5)
 * @author：平台脚本生成
 */
public class CHGZB29TOZB30 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA10TOZA11 构造子注解。
 */
public CHGZB29TOZB30() {
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
		"H_whdate->SYSDATE",
		"H_dmakedate->SYSDATE",
		"H_carnumber->H_carnumber",
		"H_coperatorid->SYSOPERATOR",
		"H_pk_corp->SYSCORP",
		"H_vsourcebilltype->ZB29",
		"H_vsourcebillid->H_pk_plan",
		"B_pk_equipment->B_pk_equipment",
		"B_pk_sb_b->B_pk_sb_b",
		"B_spk_sb->B_spk_sb",
		"B_whdate->B_plandate",
		"B_vsourcebillid->H_pk_plan",
		"B_vsoucebillrowid->B_pk_plan_b",
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
