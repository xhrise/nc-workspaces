package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA22TOZA28的VO的动态转换类。
 *
 * 创建日期：(2008-4-14)
 * @author：平台脚本生成
 */
public class CHGZA22TOZA28 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA22TOZA28 构造子注解。
 */
public CHGZA22TOZA28() {
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
		"H_vsourcebillid->H_vsourcebillid",
		"H_vsourcebilltype->H_vsourcebilltype",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_amount->B_inamount",
		"B_checkamount->B_inamount",
		"B_pk_unit->B_pk_unit"
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
