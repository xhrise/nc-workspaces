package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA30TOZA11的VO的动态转换类。
 * 检测报告->回机出库
 * 创建日期：(2008-5-5)
 * @author：平台脚本生成
 */
public class CHGZA30TOZA11 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA10TOZA11 构造子注解。
 */
public CHGZA30TOZA11() {
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
		"H_outdate->H_dmakedate",
		"H_dmakedate->SYSDATE",
		"H_coperatorid->SYSOPERATOR",
		"H_pk_corp->SYSCORP",
		"H_vsourcebilltype->H_vbilltype",
		"H_vsourcebillid->H_pk_checkreport"
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
