package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA47TOZA28的VO的动态转换类。
 *
 * 创建日期：(2008-5-9)
 * @author：平台脚本生成
 */
public class CHGZA47TOZA28 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA47TOZA28 构造子注解。
 */
public CHGZA47TOZA28() {
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
		"H_vsourcebillid->H_pk_rkd",
		"H_vsourcebilltype->H_vbilltype",
		"H_pk_deptdoc->H_pk_deptdoc",
        "H_pk_cprkds->H_pk_cprkds",
        "H_def_1->H_billno",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_amount->B_rkmount",
		"B_checkamount->B_rkmount",//功能：给检验数量设值。时间：2009-12-21.作者：张志远
		"B_pk_unit->B_pk_unit",
		"B_pk_unit->B_pk_unit",
		"B_instalment->H_pc",
        "B_billno->B_def_1",
        "B_vsourcebillid->B_vsourcebillid"
        
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
