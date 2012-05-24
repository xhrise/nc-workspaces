package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZB32TOZA43的VO的动态转换类。MRP运算-->生产计划单录入
 *
 * 创建日期：(2009-11-13)
 * @author：平台脚本生成
 */
public class CHGZB32TOZA43 extends nc.ui.pf.change.VOConversionUI {
/** 
 * CHGZB32TOZA43 构造子注解。
 */
public CHGZB32TOZA43() {
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
		"H_frombillno->H_billno",
		"H_memo->H_memo",
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
