package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA22TOZA57的VO的动态转换类。
 *
 * 创建日期：(2008-5-30)
 * @author：平台脚本生成
 */
public class CHGZA30TOZA21 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA22TOZA57 构造子注解。
 */
public CHGZA30TOZA21() {
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
		"H_vsourcebilltype->H_def_5",
		"H_pk_in->H_pk_in",
		"H_vsourcebillid->H_def_7",
		"H_pk_cubasdoc->H_def_1",
        "H_pk_psndoc->H_def_2",
//        "H_carnumber->H_def_3",
//        "H_tranno->H_def_4",
		"B_pk_invbasdoc->B_def_1",
        "B_vsourcebilltype->B_vbilltype",
        "B_vsourcebillid->B_pk_checkreport_b",
        "B_taxinprice->B_def_10",
        "B_ratrate->B_def_11",
        "B_amount->B_def_6",
        
		
		
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
