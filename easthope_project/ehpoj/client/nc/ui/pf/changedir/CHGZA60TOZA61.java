package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA60TOZA61��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-30)
 * @author��ƽ̨�ű�����
 */
public class CHGZA60TOZA61 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA60TOZA61 ������ע�⡣
 */
public CHGZA60TOZA61() {
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
		"B_hkrq->B_hkrq",
		"B_inmny->B_inmny",
		"H_vsourcebilltype->H_vbilltype",
		"H_vsourcebillid->H_pk_querymny",
		"H_pk_cubasdoc->H_pk_cubasdoc",
		"H_qr_flag->H_qr_flag",
        "H_pk_querymnys->H_pk_querymnys",
		"B_hth->B_hth",
		"B_hkarea->B_hkarea",
		"B_vsourcebillid->B_pk_querymny_b",
        "B_inno->B_inno",
        "B_inbank->B_inbank",
        "B_codeid->B_codeid",
        "B_pk_sfkfs->B_hkfs",
        "H_sktype->H_def_5"
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
