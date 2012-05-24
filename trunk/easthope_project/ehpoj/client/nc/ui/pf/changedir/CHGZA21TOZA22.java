package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA20TOZA22的VO的动态转换类。
 *
 * 创建日期：(2008-4-17)
 * @author：平台脚本生成
 */
public class CHGZA21TOZA22 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA20TOZA22 构造子注解。
 */
public CHGZA21TOZA22() {
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
			"H_pk_cubasdoc->H_pk_cubasdoc",
			"H_psninfo->H_retailinfo",
			"H_carnumber->H_carnumber",
			"H_pk_cgpsn->H_pk_psndoc",
			"H_pk_deptodc->H_pk_deptdoc",
			"H_vsourcebillno->H_billno",
			"H_ypk_in->H_pk_in",
			"H_vsourcebillid->H_pk_back",
			"H_pk_contract->H_def_1",
			
			"B_pk_invbasdoc->B_pk_invbasdoc",
			"B_vsourcebillrowid->B_pk_back_b",
			"B_yinamount->B_amount",
			"B_poundamount->B_spweight",
			"B_inprice->B_taxinprice",
			//"B_inamount->B_weight",
			"B_inamount->B_amount",//2009-11-10修改
			"B_def_6->B_taxmoney",
			"B_packagweight->B_packagweight",
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
