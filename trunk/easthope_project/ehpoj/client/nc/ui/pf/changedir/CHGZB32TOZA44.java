package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZB32TOZA44��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-8)
 * @author��ƽ̨�ű�����
 */
public class CHGZB32TOZA44 extends nc.ui.pf.change.VOConversionUI {
/** 
 * CHGZA09TOZA44 ������ע�⡣
 */
public CHGZB32TOZA44() {
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
		"H_vsourcebillid->H_pk_mrp",
		"H_memo->H_memo",
		"B_pk_order->B_vsourcebillid",
		"B_vsourcebillrowid->B_pk_mrp_b",
		"B_vsourcebillid->B_pk_mrp",
		"B_yscmount->B_yzrwamount",		//����������������
		"B_ordermount->B_bcamount",		//mrp������
		"B_scmount->B_yscamount",		//����������������
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_memo->B_memo",
		"B_ver->B_dr",
		"B_kc->B_kcamount"
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
