package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA14TOZA10的VO的动态转换类。
 *
 * 创建日期：(2009-11-17)
 * @author：平台脚本生成
 */
public class CHGZA14TOZA10 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA14TOZA10 构造子注解。
 */
public CHGZA14TOZA10() {
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
		"H_pk_psndoc->H_pk_psndoc",
		"H_pk_order->H_pk_invoice",
		"H_ladingdate->H_getdate",
		"H_pk_areacl->H_pk_areacl",
		"H_dmakedate->SYSDATE",
		"H_coperatorid->SYSOPERATOR",
		"H_pk_corp->SYSCORP",
		"H_vsourcebillid->H_pk_invoice",
		"H_vsourcebilltype->H_vbilltype",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pk_measdoc->B_pk_measdoc",
		"B_vsourcebilltype->DESTBILLTYPE",
		"B_vsourcebillid->B_pk_invoice_b",
		"B_vsourcebillrowid->B_pk_invoice",
		"B_pk_order->B_pk_invoice",
		"B_orderamount->B_amount",//订单量
		"B_price->B_price",   //牌价（辅）
		"B_ytamount->B_def_6",//已提货量
		"B_vunit->B_fzunit",//主单位
		"B_zprice->B_pj",//牌价
		"B_def_10->B_firstdiscount",//一次折扣
		"B_def_9->B_seconddiscount",//二次折扣
		"B_firstdiscount->B_def_8",          //不含税金额
		"B_seconddiscount->B_def_7",          //不含税金额
		"B_ladingamount->B_def_10",//辅助单位发票数量
		"B_zamount->B_def_9",//主单位数量
		"B_bcysje->B_tax",//本次应收金额
		"H_pk_areacl->H_pk_areacl",//片区 modify 不用houcq 2011-03-14
		

	};
}
/**
* 获得公式。
* @return java.lang.String[]
*/
public String[] getFormulas() {
	return null;
}
/**
* 返回用户自定义函数。
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
