package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA44TOZA43的VO的动态转换类。
 *
 * 创建日期：(2008-5-7)
 * @author：平台脚本生成
 */
public class CHGZB22TOZB23 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA44TOZA43 构造子注解。
 */
public CHGZB22TOZB23() {
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
		"H_vsourcebillid->H_pk_apply",
		"H_coperatorid->SYSOPERATOR",
		"H_dmakedate->SYSDATE",
		"H_pk_psndoc->H_pk_psndoc",
		"H_pk_deptdoc->H_pk_deptdoc"
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
