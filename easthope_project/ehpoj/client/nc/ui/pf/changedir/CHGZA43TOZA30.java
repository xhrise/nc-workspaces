package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA43TOZA30的VO的动态转换类。
 *
 * 创建日期：(2008-5-20)
 * @author：平台脚本生成
 */
public class CHGZA43TOZA30 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA43TOZA30 构造子注解。
 */
public CHGZA43TOZA30() {
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
		"H_vbilltype->H_vbilltype",
		"H_pk_corp->SYSCORP",
		"H_dmakedate->SYSDATE",
        "H_pk_invbasdoc->H_def_1",
		"H_vsourcebillid->H_pk_pgd",
		"B_vsourcebillid->B_pk_pgd_b",
        "B_pk_project->B_def_1",
        "B_ll_ceil->B_def_2",
        "B_ll_limit->B_def_3",
        "B_rece_ceil->B_def_4",
        "B_rece_limit->B_def_5"
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
