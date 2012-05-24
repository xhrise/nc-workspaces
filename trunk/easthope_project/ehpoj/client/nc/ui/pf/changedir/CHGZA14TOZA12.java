package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA14TOZA12的VO的动态转换类。
 *
 * 创建日期：(2008-4-14)
 * @author：平台脚本生成
 */
public class CHGZA14TOZA12 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA14TOZA12 构造子注解。
 */
public CHGZA14TOZA12() {
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
		"H_pk_corp->SYSCORP",
		"H_vsourcebilltype->H_vbilltype",
		"H_pk_areacl->H_pk_areacl",
		"H_coperatorid->SYSOPERATOR",
		"H_dmakedate->SYSDATE",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_vsourcebillid->H_pk_invoice",
		"H_def_6->H_totalprice",
        "H_yxdb->H_def_1",
        "H_vyxdb->H_def_2",
		"B_vsourcebillid->H_pk_invoice",
		"B_vsourcebillrowid->B_pk_invoice_b",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pk_measdoc->B_pk_measdoc",
		"B_backamount->B_amount",
		"B_realbackamount->B_amount",
		"B_firstcount->B_firstdiscount",
		"B_secondcount->B_seconddiscount",
		"B_def_6->B_pj",              //牌价
		"B_def_7->B_taxrate",         //税率
		"B_def_8->B_price",           //不含税单价
		"B_def_11->B_firstdiscount",
		"B_def_12->B_seconddiscount",
		"B_def_9->B_def_8",          //不含税金额
		"B_def_13->B_def_8",          //不含税金额
		"B_def_10->B_tax",          //税额
		"B_def_14->B_tax",          //税额
		"B_def_15->B_hsprice",      // 应收金额
		"B_def_16->B_hsprice",      // 应收金额
		"B_backprice->B_hsprice"      // 应收金额
	};
}
/**
* 获得公式。
* @return java.lang.String[]
*/
@Override
public String[] getFormulas() {
	return new String[]{"B_def_16->B_def15"};
}
/**
* 返回用户自定义函数。
*/
@Override
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
