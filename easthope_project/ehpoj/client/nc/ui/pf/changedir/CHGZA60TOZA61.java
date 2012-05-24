package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA60TOZA61的VO的动态转换类。
 *
 * 创建日期：(2008-5-30)
 * @author：平台脚本生成
 */
public class CHGZA60TOZA61 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA60TOZA61 构造子注解。
 */
public CHGZA60TOZA61() {
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
		"B_hkrq->B_hkrq",
		"B_inmny->B_inmny",
		"H_vsourcebilltype->H_vbilltype",
		"H_vsourcebillid->H_pk_querymny",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_qr_flag->H_qr_flag",
        "H_pk_querymnys->H_pk_querymnys",
		"B_hth->B_hth",
		"B_hkarea->B_hkarea",
		"B_vsourcebillid->B_pk_querymny_b",
        "B_inno->B_inno",
        "B_inbank->B_inbank",
        "B_codeid->B_codeid",
        "B_pk_sfkfs->B_hkfs",
        "H_sktype->H_def_5"
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
