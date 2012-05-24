package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA43TOZA47的VO的动态转换类。
 *
 * 创建日期：(2008-5-9)
 * @author：平台脚本生成
 */
public class CHGZA43TOZA47 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA43TOZA47 构造子注解。
 */
public CHGZA43TOZA47() {
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
		"H_vsourcebillid->H_pk_pgd",
		"H_vsourcebilltype->H_vbilltype",
		"H_pk_workshop->H_pk_workshop",
		"H_pk_team->H_pk_team",
		"H_def_1->H_frombillno",
		"B_pk_pgd->B_pk_pgd",
//		"B_pgmount->B_scmount",
		"B_pgmount->B_pgamount",
		"B_rkmount->B_pgamount",
		"B_pk_unit->B_pk_unit",
		"B_pk_invbasdoc->B_pk_invbasdoc",
        "B_ver->B_ver"
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
