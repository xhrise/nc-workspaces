package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA22TOZA57��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-5-30)
 * @author��ƽ̨�ű�����
 */
public class CHGZA30TOZA21 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA22TOZA57 ������ע�⡣
 */
public CHGZA30TOZA21() {
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
		"H_vsourcebilltype->H_def_5",
		"H_pk_in->H_pk_in",
		"H_vsourcebillid->H_def_7",
		"H_pk_cubasdoc->H_def_1",
        "H_pk_psndoc->H_def_2",
//        "H_carnumber->H_def_3",
//        "H_tranno->H_def_4",
		"B_pk_invbasdoc->B_def_1",
        "B_vsourcebilltype->B_vbilltype",
        "B_vsourcebillid->B_pk_checkreport_b",
        "B_taxinprice->B_def_10",
        "B_ratrate->B_def_11",
        "B_amount->B_def_6",
        
		
		
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
