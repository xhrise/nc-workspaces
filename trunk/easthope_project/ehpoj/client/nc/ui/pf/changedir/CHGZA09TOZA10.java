package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA09TOZA10��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-4-29)
 * @author��ƽ̨�ű�����
 */
public class CHGZA09TOZA10 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA09TOZA10 ������ע�⡣
 */
public CHGZA09TOZA10() {
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
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_pk_psndoc->H_pk_psndoc",
		"H_pk_order->H_pk_order",
		"H_ladingdate->H_getdate",
		"H_pk_areacl->H_pk_areacl",
		"H_dmakedate->SYSDATE",
		"H_coperatorid->SYSOPERATOR",
		"H_pk_corp->SYSCORP",
		"H_vsourcebillid->H_pk_order",
		"H_def_7->H_secondamount",
		"H_def_9->H_secondamount",
		"H_vsourcebilltype->H_vbilltype",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pk_measdoc->B_pk_measdoc",
		"B_vsourcebilltype->DESTBILLTYPE",
		"B_vsourcebillid->B_pk_order_b",
		"B_vsourcebillrowid->B_pk_order",
		"B_pk_order->B_pk_order",
		"B_vsourcebillrowid->B_rowid",
		"B_orderamount->B_amount",
		"B_price->B_price",   
		"B_storeamount->B_def_10",
		"B_ytamount->B_def_6",
		"B_vunit->B_fzunit",
		"B_zamount->B_fzamount",
		"B_zprice->B_oneprice"
	};
}
/**
* ��ù�ʽ��
* @return java.lang.String[]
*/
@Override
public String[] getFormulas() {
//	return new String[] {
//		"B_pk_order->getColValue( eh_order_b,pk_order ,pk_order_b ,B_pk_order_b )"
//	};
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
