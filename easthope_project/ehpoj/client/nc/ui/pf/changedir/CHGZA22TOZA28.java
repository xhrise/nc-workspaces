package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA22TOZA28��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-4-14)
 * @author��ƽ̨�ű�����
 */
public class CHGZA22TOZA28 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA22TOZA28 ������ע�⡣
 */
public CHGZA22TOZA28() {
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
		"H_vsourcebillid->H_vsourcebillid",
		"H_vsourcebilltype->H_vsourcebilltype",
		"B_pk_invbasdoc->B_pk_invbasdoc",
		"B_amount->B_inamount",
		"B_checkamount->B_inamount",
		"B_pk_unit->B_pk_unit"
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
