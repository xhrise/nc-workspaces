package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA43TOZA30��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-20)
 * @author��ƽ̨�ű�����
 */
public class CHGZA43TOZA30 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA43TOZA30 ������ע�⡣
 */
public CHGZA43TOZA30() {
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
		"H_vbilltype->H_vbilltype",
		"H_pk_corp->SYSCORP",
		"H_dmakedate->SYSDATE",
        "H_pk_invbasdoc->H_def_1",
		"H_vsourcebillid->H_pk_pgd",
		"B_vsourcebillid->B_pk_pgd_b",
        "B_pk_project->B_def_1",
        "B_ll_ceil->B_def_2",
        "B_ll_limit->B_def_3",
        "B_rece_ceil->B_def_4",
        "B_rece_limit->B_def_5"
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
