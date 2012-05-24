package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA20TOZA22的VO的动态转换类。
 *
 * 创建日期：(2008-4-17)
 * @author：平台脚本生成
 */
public class CHGZA18TOZA22 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA20TOZA22 构造子注解。
 */
public CHGZA18TOZA22() {
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
			"H_vsourcebillid->H_pk_sbbill",
			"H_vsourcebillno->H_billno",
			"H_vsourcebilltype->H_vbilltype",
			"H_indate->SYSDATE",
			"H_pk_contract->H_vsourcebillid",
			"H_carnumber->H_carnumber",
			"H_pk_cubasdoc->H_pk_cubasdoc",
			"H_psninfo->H_shname",
			"H_def_1->H_pk_sbbill",	
			"H_vsourcebillno->H_billno",
			
			"B_vsourcebillid->B_pk_sbbill_b",
			"B_inprice->H_def_6",
			//"B_taxinprice->H_def_7",
			"B_taxinprice->B_def_10",//modify by houcq 2011-02-25通过司磅单生成采购入库单带不出收货单价
			"B_pk_unit->B_def_5",
			"B_packagweight->H_bzkz",
			"B_inamount->H_sumsuttle",
			"B_def_8->H_sumsuttle",
			"B_pk_invbasdoc->B_def_1",
			"B_def_3->H_def_1",
			"B_def_9->H_def_6",
			"B_poundamount->H_sumsuttle"
		
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
