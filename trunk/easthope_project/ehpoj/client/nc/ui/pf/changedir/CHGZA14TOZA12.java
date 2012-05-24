package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA14TOZA12��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-4-14)
 * @author��ƽ̨�ű�����
 */
public class CHGZA14TOZA12 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA14TOZA12 ������ע�⡣
 */
public CHGZA14TOZA12() {
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
		"H_pk_corp->SYSCORP",
		"H_vsourcebilltype->H_vbilltype",
		"H_pk_areacl->H_pk_areacl",
		"H_coperatorid->SYSOPERATOR",
		"H_dmakedate->SYSDATE",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_vsourcebillid->H_pk_invoice",
		"H_def_6->H_totalprice",
        "H_yxdb->H_def_1",
        "H_vyxdb->H_def_2",
		"B_vsourcebillid->H_pk_invoice",
		"B_vsourcebillrowid->B_pk_invoice_b",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_pk_measdoc->B_pk_measdoc",
		"B_backamount->B_amount",
		"B_realbackamount->B_amount",
		"B_firstcount->B_firstdiscount",
		"B_secondcount->B_seconddiscount",
		"B_def_6->B_pj",              //�Ƽ�
		"B_def_7->B_taxrate",         //˰��
		"B_def_8->B_price",           //����˰����
		"B_def_11->B_firstdiscount",
		"B_def_12->B_seconddiscount",
		"B_def_9->B_def_8",          //����˰���
		"B_def_13->B_def_8",          //����˰���
		"B_def_10->B_tax",          //˰��
		"B_def_14->B_tax",          //˰��
		"B_def_15->B_hsprice",      // Ӧ�ս��
		"B_def_16->B_hsprice",      // Ӧ�ս��
		"B_backprice->B_hsprice"      // Ӧ�ս��
	};
}
/**
* ��ù�ʽ��
* @return java.lang.String[]
*/
@Override
public String[] getFormulas() {
	return new String[]{"B_def_16->B_def15"};
}
/**
* �����û��Զ��庯����
*/
@Override
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
