package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA20TOZA22��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-4-17)
 * @author��ƽ̨�ű�����
 */
public class CHGZA21TOZA22 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA20TOZA22 ������ע�⡣
 */
public CHGZA21TOZA22() {
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
			"H_psninfo->H_retailinfo",
			"H_carnumber->H_carnumber",
			"H_pk_cgpsn->H_pk_psndoc",
			"H_pk_deptodc->H_pk_deptdoc",
			"H_vsourcebillno->H_billno",
			"H_ypk_in->H_pk_in",
			"H_vsourcebillid->H_pk_back",
			"H_pk_contract->H_def_1",
			
			"B_pk_invbasdoc->B_pk_invbasdoc",
			"B_vsourcebillrowid->B_pk_back_b",
			"B_yinamount->B_amount",
			"B_poundamount->B_spweight",
			"B_inprice->B_taxinprice",
			//"B_inamount->B_weight",
			"B_inamount->B_amount",//2009-11-10�޸�
			"B_def_6->B_taxmoney",
			"B_packagweight->B_packagweight",
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
