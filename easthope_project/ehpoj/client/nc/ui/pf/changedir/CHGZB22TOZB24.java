package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
import nc.bs.pf.change.ConversionPolicyEnum;
/**
 * 用于ZB22TOZB24的VO交换类。五金采购申请->五金入库
 *
 * @author 流程平台自动产生的VO交换类 2011-3-28
 * @since 5.5
 */
public class CHGZB22TOZB24 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZB22TOZB24 默认构造
 */
public CHGZB22TOZB24() {
	super();
}
/**
 * 获得交换后处理类 全名称
 * @return java.lang.String
 */
public String getAfterClassName() {
	return null;
}
/**
 * 获得交换后处理类 全名称
 * @return java.lang.String
 */
public String getOtherClassName() {
	return null;
}
/**
 * 返回交换类型枚举ConversionPolicyEnum，默认为单据项目-单据项目
 * @return ConversionPolicyEnum
 * @since 5.5
 */
public ConversionPolicyEnum getConversionPolicy() {
	return ConversionPolicyEnum.BILLITEM_BILLITEM;
}
/**
 * 获得映射类型的交换规则
 * @return java.lang.String[]
 */
public String[] getField() {
	return new String[] {
			"H_coperatorid->H_coperatorid",
			"H_vsourcebillid->H_pk_apply",
			"H_vsourcebillno->H_billno",
			"H_vsourcebilltype->H_vbilltype",
			"B_inamount->B_amount",
			"B_pk_invbasdoc->B_pk_invbasdoc",
			"H_pk_deptodc->H_pk_deptdoc",
			"B_vsourcebillid->B_pk_apply_b"
	};
}
/**
 * 获得赋值类型的交换规则
 * @return java.lang.String[]
 */
public String[] getAssign() {
	return null;
}
/**
 * 获得公式类型的交换规则
 * @return java.lang.String[]
 */
public String[] getFormulas() {
	return null;
}
/**
 * 返回用户自定义函数
 */
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
