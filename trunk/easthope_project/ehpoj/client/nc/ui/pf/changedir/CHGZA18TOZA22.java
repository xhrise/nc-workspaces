package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����ZA20TOZA22��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2008-4-17)
 * @author��ƽ̨�ű�����
 */
public class CHGZA18TOZA22 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA20TOZA22 ������ע�⡣
 */
public CHGZA18TOZA22() {
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
			"H_vsourcebillid->H_pk_sbbill",
			"H_vsourcebillno->H_billno",
			"H_vsourcebilltype->H_vbilltype",
			"H_indate->SYSDATE",
			"H_pk_contract->H_vsourcebillid",
			"H_carnumber->H_carnumber",
			"H_pk_cubasdoc->H_pk_cubasdoc",
			"H_psninfo->H_shname",
			"H_def_1->H_pk_sbbill",	
			"H_vsourcebillno->H_billno",
			
			"B_vsourcebillid->B_pk_sbbill_b",
			"B_inprice->H_def_6",
			//"B_taxinprice->H_def_7",
			"B_taxinprice->B_def_10",//modify by houcq 2011-02-25ͨ��˾�������ɲɹ���ⵥ�������ջ�����
			"B_pk_unit->B_def_5",
			"B_packagweight->H_bzkz",
			"B_inamount->H_sumsuttle",
			"B_def_8->H_sumsuttle",
			"B_pk_invbasdoc->B_def_1",
			"B_def_3->H_def_1",
			"B_def_9->H_def_6",
			"B_poundamount->H_sumsuttle"
		
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
