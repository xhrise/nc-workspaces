package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA43TOZA47��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-9)
 * @author��ƽ̨�ű�����
 */
public class CHGZA43TOZA47 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA43TOZA47 ������ע�⡣
 */
public CHGZA43TOZA47() {
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
		"H_vsourcebillid->H_pk_pgd",
		"H_vsourcebilltype->H_vbilltype",
		"H_pk_workshop->H_pk_workshop",
		"H_pk_team->H_pk_team",
		"H_def_1->H_frombillno",
		"B_pk_pgd->B_pk_pgd",
//		"B_pgmount->B_scmount",
		"B_pgmount->B_pgamount",
		"B_rkmount->B_pgamount",
		"B_pk_unit->B_pk_unit",
		"B_pk_invbasdoc->B_pk_invbasdoc",
        "B_ver->B_ver"
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
