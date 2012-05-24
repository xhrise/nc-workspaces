package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA10TOZA14的VO的动态转换类。
 *
 * 创建日期：(2008-5-30)
 * @author：平台脚本生成
 */
public class CHGZA10TOZA14 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA10TOZA14 构造子注解。
 */
public CHGZA10TOZA14() {
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
//		"H_pk_psndoc->H_pk_psndoc",
//		"H_pk_areacl->H_pk_areacl",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_coperatorid->SYSOPERATOR",
		"H_dmakedate->SYSDATE",
		"H_pk_corp->SYSCORP",
		"H_invoicedate->SYSDATE",
		"H_vsourcebillid->H_pk_ladingbill",
		"H_vsourcebilltype->H_vbilltype",
		"H_pk_areacl->H_pk_areacl",//add by houcq 2011-03-18
		"B_amount->B_zamount",
		"B_pj->B_zprice",
		"B_pk_measdoc->B_vunit",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_def_8->B_firstdiscount",
		"B_def_9->B_seconddiscount",
		"B_vsourcebillid->B_pk_ladingbill_b",
		"B_vsourcebilltype->H_vbilltype",
		"B_firstdiscount->B_firstdiscount",
		"B_def_4->B_def_4",
		"B_seconddiscount->B_seconddiscount"
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
