package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
import nc.bs.pf.change.ConversionPolicyEnum;
/**
 * ����ZB22TOZB24��VO�����ࡣ���ɹ�����->������
 *
 * @author ����ƽ̨�Զ�������VO������ 2011-3-28
 * @since 5.5
 */
public class CHGZB22TOZB24 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZB22TOZB24 Ĭ�Ϲ���
 */
public CHGZB22TOZB24() {
	super();
}
/**
 * ��ý��������� ȫ����
 * @return java.lang.String
 */
public String getAfterClassName() {
	return null;
}
/**
 * ��ý��������� ȫ����
 * @return java.lang.String
 */
public String getOtherClassName() {
	return null;
}
/**
 * ���ؽ�������ö��ConversionPolicyEnum��Ĭ��Ϊ������Ŀ-������Ŀ
 * @return ConversionPolicyEnum
 * @since 5.5
 */
public ConversionPolicyEnum getConversionPolicy() {
	return ConversionPolicyEnum.BILLITEM_BILLITEM;
}
/**
 * ���ӳ�����͵Ľ�������
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
 * ��ø�ֵ���͵Ľ�������
 * @return java.lang.String[]
 */
public String[] getAssign() {
	return null;
}
/**
 * ��ù�ʽ���͵Ľ�������
 * @return java.lang.String[]
 */
public String[] getFormulas() {
	return null;
}
/**
 * �����û��Զ��庯��
 */
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
