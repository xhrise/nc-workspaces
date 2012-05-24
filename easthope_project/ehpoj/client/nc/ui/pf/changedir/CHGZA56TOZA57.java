package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA56TOZA57的VO的动态转换类。
 *
 * 创建日期：(2008-5-29)
 * @author：平台脚本生成
 */
public class CHGZA56TOZA57 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA56TOZA57 构造子注解。
 */
public CHGZA56TOZA57() {
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
		"H_fkrq->H_yjfkrq",
		"H_cubasbank->H_cubasbank",
		
		"H_pk_hth->H_pk_hth",
		"H_vsourcebilltype->H_vbilltype",
		"H_memo->H_vapprovenote",
		"H_vsourcebillid->H_pk_fkqs",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_bankaccount->H_bankaccount",
		"H_pk_sfkfs->H_pk_sfkfs",
		"H_sqje->H_fkje",
		"H_def_6->H_def_6",
		"H_def_6->H_yfje",
        "B_rkbillno->B_vbillno",
		"B_vsourcebillid->B_vsourcebillid",
		"B_rkmount->B_rkmount",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pricemny->B_pricemny",
        "B_htbillno->B_htbillno",
        "B_sbamount->B_sbamount",
        "B_kzamount->B_kzamount",
        "B_kjprice->B_kjprice",
        "B_htamount->B_htmount",
        "B_price->B_fkmny",
        "B_yfkje->B_yfkje",
        "B_wfkje->B_wfkje",
        "B_vsourcebillrowid->B_vsourcebillrowid"
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
