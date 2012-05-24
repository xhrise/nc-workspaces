package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA11TOZA14的VO的动态转换类。
 *
 * 创建日期：(2008-4-14)
 * @author：平台脚本生成
 */
public class CHGZA11TOZA14 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA11TOZA14 构造子注解。
 */
public CHGZA11TOZA14() {
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
		"H_pk_corp->H_pk_corp",
		"H_vsourcebilltype->H_vbilltype",
		"H_coperatorid->SYSOPERATOR",
		"H_dmakedate->SYSDATE",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_vsourcebillid->H_pk_icout",
		"H_pk_areacl->H_pk_areacl",
		"H_pk_corp->SYSCORP",
		"B_vsourcebillid->B_pk_icout_b",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pk_measdoc->B_pk_measdoc",
		"B_pj->B_price",
		"B_vsourcebilltype->H_vbilltype",
		"B_def_4->B_def_4",
		"B_amount->B_outamount",
		"B_firstdiscount->B_firstdiscount",
		"B_seconddiscount->B_seconddiscount",
		"B_def_8->B_firstdiscount",
		"B_def_9->B_seconddiscount",
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
