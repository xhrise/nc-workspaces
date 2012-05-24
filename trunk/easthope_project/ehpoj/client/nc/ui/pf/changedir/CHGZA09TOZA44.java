package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA09TOZA44的VO的动态转换类。
 *
 * 创建日期：(2008-5-8)
 * @author：平台脚本生成
 */
public class CHGZA09TOZA44 extends nc.ui.pf.change.VOConversionUI {
/** 
 * CHGZA09TOZA44 构造子注解。
 */
public CHGZA09TOZA44() {
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
		"H_vsourcebillid->H_pk_order",
		"B_pk_order->B_vsourcebillid",
		"B_vsourcebillrowid->B_pk_order_b",
		"B_vsourcebillid->B_pk_order",
		"B_yscmount->B_def_8",
//		"B_pk_order->B_pk_order",
		"B_ordermount->B_fzamount",
		"B_scmount->B_def_6",
		"B_pk_invbasdoc->B_pk_invbasdoc",
//		"B_pk_unit->B_pk_measdoc",
		"B_ver->B_dr",
		"B_kc->B_def_9"
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
