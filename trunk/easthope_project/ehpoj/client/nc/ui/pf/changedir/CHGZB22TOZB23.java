package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA44TOZA43��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-7)
 * @author��ƽ̨�ű�����
 */
public class CHGZB22TOZB23 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA44TOZA43 ������ע�⡣
 */
public CHGZB22TOZB23() {
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
		"H_vsourcebillid->H_pk_apply",
		"H_coperatorid->SYSOPERATOR",
		"H_dmakedate->SYSDATE",
		"H_pk_psndoc->H_pk_psndoc",
		"H_pk_deptdoc->H_pk_deptdoc"
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
