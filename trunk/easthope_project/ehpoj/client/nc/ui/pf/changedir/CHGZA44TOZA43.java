package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA44TOZA43的VO的动态转换类。
 *
 * 创建日期：(2008-5-7)
 * @author：平台脚本生成
 */
public class CHGZA44TOZA43 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA44TOZA43 构造子注解。
 */
public CHGZA44TOZA43() {
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
		"H_vbilltype->H_vbilltype",
		"H_pk_busitype->H_pk_busitype",
		"H_vsourcebillid->H_pk_posm",
		"H_coperatorid->SYSOPERATOR",
		"H_dmakedate->SYSDATE",
		"H_memo->H_memo",
		"B_scmount->B_scmount",
		"B_ver->B_ver",
		"B_pk_corp->SYSCORP",
		"B_pk_posm->H_pk_posm",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_color->B_color",
		"B_pk_unit->B_pk_unit",
		"B_hjinvbasdoc->B_hjinvbasdoc",
		"B_vsourcebillid->B_pk_posm_b",
		"B_vsourcebillrowid->B_pk_posm",
		"B_xh->B_xh"
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
