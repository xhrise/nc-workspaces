package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA57TOZA58��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-30)
 * @author��ƽ̨�ű�����
 */
public class CHGZA57TOZA58 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA57TOZA58 ������ע�⡣
 */
public CHGZA57TOZA58() {
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
		"H_fkrq->H_fkrq",
		"H_vsourcebilltype->H_vbilltype",
		"H_memo->H_memo",
		"H_qr_flag->H_qr_flag",
		"H_pk_psndoc->H_pk_psndoc",
		"H_def_4->H_def_4",
		"H_def_3->H_def_3",
		"H_vapprovenote->H_vapprovenote",
		"H_def_2->H_def_2",
		"H_def_1->H_def_1",
		"H_pk_deptdoc->H_pk_deptdoc",
		"H_billno->H_billno",
		"H_cubasbank->H_cubasbank",
		"H_pk_hth->H_pk_hth",
		"H_vapproveid->H_vapproveid",
		"H_fkje->H_fkje",
		"H_qr_rq->H_qr_rq",
		"H_dapprovedate->H_dapprovedate",
		"H_vsourcebillid->H_pk_fk",
		"H_coperatorid->H_coperatorid",
		"H_bankaccount->H_bankaccount",
		"H_qr_psndoc->H_qr_psndoc",
		"H_cubasphone->H_cubasphone",
		"H_pk_sfkfs->H_pk_sfkfs",
		"H_dr->H_dr",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"B_vsourcebillid->B_pk_fk_b",
		"B_rkmount->B_rkmount",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pricemny->B_pricemny"
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
