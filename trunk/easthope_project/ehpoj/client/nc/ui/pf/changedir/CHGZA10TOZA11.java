package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA10TOZA11��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-5)
 * @author��ƽ̨�ű�����
 */
public class CHGZA10TOZA11 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA10TOZA11 ������ע�⡣
 */
public CHGZA10TOZA11() {
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
		"H_outdate->H_ladingdate",
		"H_dmakedate->SYSDATE",
		"H_carnumber->H_carnumber",
		"H_coperatorid->SYSOPERATOR",
		"H_pk_corp->SYSCORP",
		"H_vsourcebilltype->H_vbilltype",
		"H_vsourcebillid->H_pk_ladingbill",
		"H_pk_sbbill->H_pk_sbbill",
		"B_vsourcebillrowid->H_pk_ladingbill",
		"B_ladingamount->B_zamount",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_vsourcebillid->B_pk_ladingbill_b",
		"B_firstdiscount->B_firstdiscount",
		"B_seconddiscount->B_seconddiscount",
		"B_pk_measdoc->B_vunit",
		"B_pk_ladingbill->B_pk_ladingbill",
		"B_price->B_zprice",
		"B_def_7->B_bcysje",
        "B_def_6->B_def_6"
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
