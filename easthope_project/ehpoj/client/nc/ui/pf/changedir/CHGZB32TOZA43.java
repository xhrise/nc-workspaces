package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZB32TOZA43��VO�Ķ�̬ת���ࡣMRP����-->�����ƻ���¼��
 *
 * �������ڣ�(2009-11-13)
 * @author��ƽ̨�ű�����
 */
public class CHGZB32TOZA43 extends nc.ui.pf.change.VOConversionUI {
/** 
 * CHGZB32TOZA43 ������ע�⡣
 */
public CHGZB32TOZA43() {
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
		"H_vsourcebillid->H_pk_mrp",
		"H_frombillno->H_billno",
		"H_memo->H_memo",
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
