package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA44TOZA42��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-7)
 * @author��ƽ̨�ű�����
 */
public class CHGZA44TOZA42 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA44TOZA42 ������ע�⡣
 */
public CHGZA44TOZA42() {
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
		"H_pk_posm->H_billno",
		"H_dmakedate->SYSDATE",
		"H_vsourcebillid->H_pk_posm",
		"H_vsourcebilltype->H_vbilltype",
        "H_pk_posms->H_pk_posms",
		"B_pk_unit->B_pk_unit",
        "B_blmount->B_scmount",
		"B_pk_corp->SYSCORP",
		"B_pk_invbasdoc->B_pk_invbasdoc"
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
