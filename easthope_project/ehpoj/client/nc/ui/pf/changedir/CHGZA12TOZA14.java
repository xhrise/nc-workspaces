package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA11TOZA14��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-4-14)
 * @author��ƽ̨�ű�����
 */
public class CHGZA12TOZA14 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA11TOZA14 ������ע�⡣
 */
public CHGZA12TOZA14() {
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
//		"H_vsourcebilltype->H_vsourcebilltype",
		"H_coperatorid->SYSOPERATOR",
		"H_dmakedate->SYSDATE",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_vsourcebillid->H_pk_backbill",
		"H_pk_areacl->H_pk_areacl",
		"H_pk_corp->SYSCORP",
		"B_vsourcebillid->H_pk_backbill",
		"B_vsourcebillrowid->B_pk_backbill_b",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pk_measdoc->B_pk_measdoc",
		"B_pj->B_def_6",
		"B_def_4->B_def_4",
		"B_amount->B_realbackamount",
		"B_firstdiscount->B_firstcount",
		"B_seconddiscount->B_secondcount"
	};
}
/**
* ��ù�ʽ��
* @return java.lang.String[]
*/
@Override
public String[] getFormulas() {
	return new String[] {
//		"B_price->getColValue(eh_invbasdoc, price, pk_invbasdoc , pk_invbasdoc)"
	};
}
/**
* �����û��Զ��庯����
*/
@Override
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
