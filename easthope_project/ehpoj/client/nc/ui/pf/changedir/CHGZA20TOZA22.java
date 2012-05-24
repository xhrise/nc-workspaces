package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA20TOZA22的VO的动态转换类。
 *
 * 创建日期：(2008-4-17)
 * @author：平台脚本生成
 */
public class CHGZA20TOZA22 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA20TOZA22 构造子注解。
 */
public CHGZA20TOZA22() {
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

		
		
		"H_pk_deptodc->H_pk_deptdoc",
		"H_vsourcebillid->H_pk_receipt",
		"H_vsourcebillno->H_billno",
		"H_vsourcebilltype->H_vbilltype",
		"H_indate->SYSDATE",
		"H_pk_contract->H_vsourcebillid",
		"H_carnumber->H_carnumber",
		"H_pk_cgpsn->H_pk_psndoc",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_psninfo->H_retailinfo",
		"H_def_1->H_pk_receipt",	
		"H_vsourcebillno->H_billno",
		
		"B_vsourcebillid->B_pk_receipt_b",
		"B_inprice->B_taxinprice",
		"B_taxinprice->B_taxinprice",
		"B_pk_unit->B_pk_unit",
		"B_packagweight->B_sumpackagweight",
		"B_inamount->B_inamount",
		"B_def_8->B_inamount",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_def_3->B_taxinprice",
		"B_def_9->B_taxinprice",
		"B_poundamount->B_inamount",//modify by houcq 2011-02-18
		
		
		
		"B_def_6->B_def_6",
		"B_ratrate->B_ratrate",
		
		
		
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
