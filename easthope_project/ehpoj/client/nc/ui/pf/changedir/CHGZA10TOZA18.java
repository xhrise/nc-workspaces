package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA24TOZA26��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-4-23)
 * @author��ƽ̨�ű�����
 */
public class CHGZA10TOZA18 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA24TOZA26 ������ע�⡣
 */
public CHGZA10TOZA18() {
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
		"H_pk_invbasdoc->H_def_5",
        "H_vsourcebillid->H_pk_ladingbill"
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
