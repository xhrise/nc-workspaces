package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA11TOZA14��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-4-14)
 * @author��ƽ̨�ű�����
 */
public class CHGZA11TOZA14 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA11TOZA14 ������ע�⡣
 */
public CHGZA11TOZA14() {
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
		"H_pk_corp->H_pk_corp",
		"H_vsourcebilltype->H_vbilltype",
		"H_coperatorid->SYSOPERATOR",
		"H_dmakedate->SYSDATE",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_vsourcebillid->H_pk_icout",
		"H_pk_areacl->H_pk_areacl",
		"H_pk_corp->SYSCORP",
		"B_vsourcebillid->B_pk_icout_b",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pk_measdoc->B_pk_measdoc",
		"B_pj->B_price",
		"B_vsourcebilltype->H_vbilltype",
		"B_def_4->B_def_4",
		"B_amount->B_outamount",
		"B_firstdiscount->B_firstdiscount",
		"B_seconddiscount->B_seconddiscount",
		"B_def_8->B_firstdiscount",
		"B_def_9->B_seconddiscount",
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
