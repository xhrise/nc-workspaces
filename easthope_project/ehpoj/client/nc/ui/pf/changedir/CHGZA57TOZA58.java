package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA57TOZA58的VO的动态转换类。
 *
 * 创建日期：(2008-5-30)
 * @author：平台脚本生成
 */
public class CHGZA57TOZA58 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA57TOZA58 构造子注解。
 */
public CHGZA57TOZA58() {
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
		"H_fkrq->H_fkrq",
		"H_vsourcebilltype->H_vbilltype",
		"H_memo->H_memo",
		"H_qr_flag->H_qr_flag",
		"H_pk_psndoc->H_pk_psndoc",
		"H_def_4->H_def_4",
		"H_def_3->H_def_3",
		"H_vapprovenote->H_vapprovenote",
		"H_def_2->H_def_2",
		"H_def_1->H_def_1",
		"H_pk_deptdoc->H_pk_deptdoc",
		"H_billno->H_billno",
		"H_cubasbank->H_cubasbank",
		"H_pk_hth->H_pk_hth",
		"H_vapproveid->H_vapproveid",
		"H_fkje->H_fkje",
		"H_qr_rq->H_qr_rq",
		"H_dapprovedate->H_dapprovedate",
		"H_vsourcebillid->H_pk_fk",
		"H_coperatorid->H_coperatorid",
		"H_bankaccount->H_bankaccount",
		"H_qr_psndoc->H_qr_psndoc",
		"H_cubasphone->H_cubasphone",
		"H_pk_sfkfs->H_pk_sfkfs",
		"H_dr->H_dr",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"B_vsourcebillid->B_pk_fk_b",
		"B_rkmount->B_rkmount",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pricemny->B_pricemny"
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
