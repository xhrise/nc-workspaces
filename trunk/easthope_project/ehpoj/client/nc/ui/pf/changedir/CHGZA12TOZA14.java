package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA11TOZA14的VO的动态转换类。
 *
 * 创建日期：(2008-4-14)
 * @author：平台脚本生成
 */
public class CHGZA12TOZA14 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA11TOZA14 构造子注解。
 */
public CHGZA12TOZA14() {
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
//		"H_vsourcebilltype->H_vsourcebilltype",
		"H_coperatorid->SYSOPERATOR",
		"H_dmakedate->SYSDATE",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_vsourcebillid->H_pk_backbill",
		"H_pk_areacl->H_pk_areacl",
		"H_pk_corp->SYSCORP",
		"B_vsourcebillid->H_pk_backbill",
		"B_vsourcebillrowid->B_pk_backbill_b",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pk_measdoc->B_pk_measdoc",
		"B_pj->B_def_6",
		"B_def_4->B_def_4",
		"B_amount->B_realbackamount",
		"B_firstdiscount->B_firstcount",
		"B_seconddiscount->B_secondcount"
	};
}
/**
* 获得公式。
* @return java.lang.String[]
*/
@Override
public String[] getFormulas() {
	return new String[] {
//		"B_price->getColValue(eh_invbasdoc, price, pk_invbasdoc , pk_invbasdoc)"
	};
}
/**
* 返回用户自定义函数。
*/
@Override
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
