package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZB32TOZA44的VO的动态转换类。
 *
 * 创建日期：(2008-5-8)
 * @author：平台脚本生成
 */
public class CHGZB32TOZA44 extends nc.ui.pf.change.VOConversionUI {
/** 
 * CHGZA09TOZA44 构造子注解。
 */
public CHGZB32TOZA44() {
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
		"H_vsourcebilltype->H_vbilltype",
		"H_vsourcebillid->H_pk_mrp",
		"H_memo->H_memo",
		"B_pk_order->B_vsourcebillid",
		"B_vsourcebillrowid->B_pk_mrp_b",
		"B_vsourcebillid->B_pk_mrp",
		"B_yscmount->B_yzrwamount",		//已做生产任务数量
		"B_ordermount->B_bcamount",		//mrp总数量
		"B_scmount->B_yscamount",		//本次生产任务数量
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_memo->B_memo",
		"B_ver->B_dr",
		"B_kc->B_kcamount"
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
