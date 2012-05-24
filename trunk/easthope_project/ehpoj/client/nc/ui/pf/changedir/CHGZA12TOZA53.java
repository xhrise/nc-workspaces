package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA14TOZA53的VO的动态转换类。
 *
 * 创建日期：(2008-5-27)
 * @author：平台脚本生成
 */
public class CHGZA12TOZA53 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA14TOZA53 构造子注解。
 */
public CHGZA12TOZA53() {
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
		"H_pk_sbbill->H_pk_sbbill",	
		"H_vsourcebillid->H_pk_backbill",
        "H_vsourcebilltype->H_vbilltype",
		"B_price->B_def_6",
		"B_pk_unit->B_pk_measdoc",
		"B_pk_invbasdoc->B_pk_invbasdoc",
        "B_pgmount->B_backamount",
        "B_vsourcebillid->B_pk_backbill_b",
        "B_backamount->B_realbackamount",
        "B_rkmount->B_realbackamount"
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
