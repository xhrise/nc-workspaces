package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA17TOZA20的VO的动态转换类。
 *
 * 创建日期：(2008-5-12)
 * @author：平台脚本生成
 */
public class CHGZB19TOZA20 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA17TOZA20 构造子注解。
 */
public CHGZB19TOZA20() {
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
		"H_vsourcebillid->H_pk_decision",
		"B_pk_invbasdoc->H_pk_invbasdoc",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"B_inamount->H_xzcgamount",//modify by houcq 2011-03-18
		"H_vsourcetype->H_vbilltype",//add by houcq 2011-03-18 来源单据类型
		"B_amount->H_xzcgamount",  //modify by houcq 合同数量对照采购数量
		"B_taxinprice->H_hsprice",
		"H_pk_psndoc->H_pk_psndoc",
		"B_allcheck->B_ischeck",
		
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
