package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA09TOZA44��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-8)
 * @author��ƽ̨�ű�����
 */
public class CHGZA09TOZA44 extends nc.ui.pf.change.VOConversionUI {
/** 
 * CHGZA09TOZA44 ������ע�⡣
 */
public CHGZA09TOZA44() {
	super();
}
/**
* ��ú������ȫ¼�����ơ�
* @return java.lang.String[]
*/
@Override
public String getAfterClassName() {
	return null;
}
/**
* �����һ���������ȫ¼�����ơ�
* @return java.lang.String[]
*/
@Override
public String getOtherClassName() {
	return null;
}
/**
* ����ֶζ�Ӧ��
* @return java.lang.String[]
*/
@Override
public String[] getField() {
	return new String[] {
		"H_vsourcebilltype->H_vbilltype",
		"H_vsourcebillid->H_pk_order",
		"B_pk_order->B_vsourcebillid",
		"B_vsourcebillrowid->B_pk_order_b",
		"B_vsourcebillid->B_pk_order",
		"B_yscmount->B_def_8",
//		"B_pk_order->B_pk_order",
		"B_ordermount->B_fzamount",
		"B_scmount->B_def_6",
		"B_pk_invbasdoc->B_pk_invbasdoc",
//		"B_pk_unit->B_pk_measdoc",
		"B_ver->B_dr",
		"B_kc->B_def_9"
	};
}
/**
* ��ù�ʽ��
* @return java.lang.String[]
*/
@Override
public String[] getFormulas() {
	return null;
}
/**
* �����û��Զ��庯����
*/
@Override
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
