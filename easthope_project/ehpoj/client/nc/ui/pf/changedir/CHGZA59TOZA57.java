package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA59TOZA57��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-6-3)
 * @author��ƽ̨�ű�����
 */
public class CHGZA59TOZA57 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA59TOZA57 ������ע�⡣
 */
public CHGZA59TOZA57() {
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
		"H_pk_deptdoc->H_pk_deptdoc",
		"H_pk_psndoc->H_pk_psndoc",
		"H_cubasphone->H_cubasphone",
		"H_fkrq->SYSDATE",
		"H_cubasbank->H_bank",
		"H_pk_hth->H_pk_hth",
		"H_vsourcebilltype->H_vbilltype",
		"H_vsourcebillid->H_pk_stockinvoice",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_fkje->H_exchangerate",
		"H_pk_corp->SYSCORP",
		"H_bankaccount->H_bankno",
		"B_vsourcebillid->H_pk_stockinvoice",
		"B_vsourcebillrowid->B_pk_stockinvoices_b",
		"B_rkmount->B_rkmount",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pricemny->B_taxinprice",
		"B_pk_in->B_vsourcebillid"
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
