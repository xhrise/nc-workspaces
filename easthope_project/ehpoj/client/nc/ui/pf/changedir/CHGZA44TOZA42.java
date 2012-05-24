package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA44TOZA42的VO的动态转换类。
 *
 * 创建日期：(2008-5-7)
 * @author：平台脚本生成
 */
public class CHGZA44TOZA42 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA44TOZA42 构造子注解。
 */
public CHGZA44TOZA42() {
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
		"H_pk_corp->SYSCORP",
		"H_pk_posm->H_billno",
		"H_dmakedate->SYSDATE",
		"H_vsourcebillid->H_pk_posm",
		"H_vsourcebilltype->H_vbilltype",
        "H_pk_posms->H_pk_posms",
		"B_pk_unit->B_pk_unit",
        "B_blmount->B_scmount",
		"B_pk_corp->SYSCORP",
		"B_pk_invbasdoc->B_pk_invbasdoc"
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
