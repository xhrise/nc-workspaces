package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA10TOZA11的VO的动态转换类。
 *
 * 创建日期：(2008-5-5)
 * @author：平台脚本生成
 */
public class CHGZA10TOZA11 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA10TOZA11 构造子注解。
 */
public CHGZA10TOZA11() {
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
		"H_outdate->H_ladingdate",
		"H_dmakedate->SYSDATE",
		"H_carnumber->H_carnumber",
		"H_coperatorid->SYSOPERATOR",
		"H_pk_corp->SYSCORP",
		"H_vsourcebilltype->H_vbilltype",
		"H_vsourcebillid->H_pk_ladingbill",
		"H_pk_sbbill->H_pk_sbbill",
		"B_vsourcebillrowid->H_pk_ladingbill",
		"B_ladingamount->B_zamount",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_vsourcebillid->B_pk_ladingbill_b",
		"B_firstdiscount->B_firstdiscount",
		"B_seconddiscount->B_seconddiscount",
		"B_pk_measdoc->B_vunit",
		"B_pk_ladingbill->B_pk_ladingbill",
		"B_price->B_zprice",
		"B_def_7->B_bcysje",
        "B_def_6->B_def_6"
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
