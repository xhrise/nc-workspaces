package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA14TOZA10��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2009-11-17)
 * @author��ƽ̨�ű�����
 */
public class CHGZA14TOZA10 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA14TOZA10 ������ע�⡣
 */
public CHGZA14TOZA10() {
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
		"H_pk_order->H_pk_invoice",
		"H_ladingdate->H_getdate",
		"H_pk_areacl->H_pk_areacl",
		"H_dmakedate->SYSDATE",
		"H_coperatorid->SYSOPERATOR",
		"H_pk_corp->SYSCORP",
		"H_vsourcebillid->H_pk_invoice",
		"H_vsourcebilltype->H_vbilltype",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pk_measdoc->B_pk_measdoc",
		"B_vsourcebilltype->DESTBILLTYPE",
		"B_vsourcebillid->B_pk_invoice_b",
		"B_vsourcebillrowid->B_pk_invoice",
		"B_pk_order->B_pk_invoice",
		"B_orderamount->B_amount",//������
		"B_price->B_price",   //�Ƽۣ�����
		"B_ytamount->B_def_6",//�������
		"B_vunit->B_fzunit",//����λ
		"B_zprice->B_pj",//�Ƽ�
		"B_def_10->B_firstdiscount",//һ���ۿ�
		"B_def_9->B_seconddiscount",//�����ۿ�
		"B_firstdiscount->B_def_8",          //����˰���
		"B_seconddiscount->B_def_7",          //����˰���
		"B_ladingamount->B_def_10",//������λ��Ʊ����
		"B_zamount->B_def_9",//����λ����
		"B_bcysje->B_tax",//����Ӧ�ս��
		"H_pk_areacl->H_pk_areacl",//Ƭ�� modify ����houcq 2011-03-14
		

	};
}
/**
* ��ù�ʽ��
* @return java.lang.String[]
*/
public String[] getFormulas() {
	return null;
}
/**
* �����û��Զ��庯����
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
