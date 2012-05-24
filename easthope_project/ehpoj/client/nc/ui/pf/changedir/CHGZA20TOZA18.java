package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA20TOZA18的VO的动态转换类。
 *
 * 创建日期：(2008-4-22)
 * @author：平台脚本生成
 */
public class CHGZA20TOZA18 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA20TOZA18 构造子注解。
 */
public CHGZA20TOZA18() {
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
		"H_shname->H_retailinfo",
		"H_wrapperweight->H_def_8",
		"H_pk_corp->SYSCORP",
		"H_dmakedate->SYSDATE",
		"H_vsourcebillid->H_pk_receipt",
		"H_vsourcebillrowid->H_def_2",
		"H_carnumber->H_carnumber",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_pk_invbasdoc->H_def_1",
		"B_def_2->B_def_2",
		"B_def_1->B_def_1",
		"B_def_5->B_def_5",
		"B_def_4->B_def_4",
		"B_def_3->B_def_3"
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
