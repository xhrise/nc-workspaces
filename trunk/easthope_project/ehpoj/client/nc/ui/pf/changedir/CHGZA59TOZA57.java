package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA59TOZA57的VO的动态转换类。
 *
 * 创建日期：(2008-6-3)
 * @author：平台脚本生成
 */
public class CHGZA59TOZA57 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA59TOZA57 构造子注解。
 */
public CHGZA59TOZA57() {
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
		"H_pk_deptdoc->H_pk_deptdoc",
		"H_pk_psndoc->H_pk_psndoc",
		"H_cubasphone->H_cubasphone",
		"H_fkrq->SYSDATE",
		"H_cubasbank->H_bank",
		"H_pk_hth->H_pk_hth",
		"H_vsourcebilltype->H_vbilltype",
		"H_vsourcebillid->H_pk_stockinvoice",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_fkje->H_exchangerate",
		"H_pk_corp->SYSCORP",
		"H_bankaccount->H_bankno",
		"B_vsourcebillid->H_pk_stockinvoice",
		"B_vsourcebillrowid->B_pk_stockinvoices_b",
		"B_rkmount->B_rkmount",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pricemny->B_taxinprice",
		"B_pk_in->B_vsourcebillid"
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
