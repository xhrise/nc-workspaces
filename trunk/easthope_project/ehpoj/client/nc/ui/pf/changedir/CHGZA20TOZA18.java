package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA20TOZA18��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-4-22)
 * @author��ƽ̨�ű�����
 */
public class CHGZA20TOZA18 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA20TOZA18 ������ע�⡣
 */
public CHGZA20TOZA18() {
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
		"H_vsourcebilltype->H_vbilltype",
		"H_shname->H_retailinfo",
		"H_wrapperweight->H_def_8",
		"H_pk_corp->SYSCORP",
		"H_dmakedate->SYSDATE",
		"H_vsourcebillid->H_pk_receipt",
		"H_vsourcebillrowid->H_def_2",
		"H_carnumber->H_carnumber",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_pk_invbasdoc->H_def_1",
		"B_def_2->B_def_2",
		"B_def_1->B_def_1",
		"B_def_5->B_def_5",
		"B_def_4->B_def_4",
		"B_def_3->B_def_3"
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
