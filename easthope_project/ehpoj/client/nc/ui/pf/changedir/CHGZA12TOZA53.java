package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA14TOZA53��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-27)
 * @author��ƽ̨�ű�����
 */
public class CHGZA12TOZA53 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA14TOZA53 ������ע�⡣
 */
public CHGZA12TOZA53() {
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
		"H_pk_sbbill->H_pk_sbbill",	
		"H_vsourcebillid->H_pk_backbill",
        "H_vsourcebilltype->H_vbilltype",
		"B_price->B_def_6",
		"B_pk_unit->B_pk_measdoc",
		"B_pk_invbasdoc->B_pk_invbasdoc",
        "B_pgmount->B_backamount",
        "B_vsourcebillid->B_pk_backbill_b",
        "B_backamount->B_realbackamount",
        "B_rkmount->B_realbackamount"
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
