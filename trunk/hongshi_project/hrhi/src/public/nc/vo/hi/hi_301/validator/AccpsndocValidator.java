package nc.vo.hi.hi_301.validator;

import nc.vo.pub.lang.*;
/**
 * �˴���������������
 * �������ڣ�(2004-5-15 13:46:18)
 * @author��Administrator
 */
import nc.vo.hi.hi_301.*;
import nc.vo.pub.*;
//import nc.ui.hr.global.*;
public class AccpsndocValidator extends AbstractValidator{
/**
 * AccpsndocValidator ������ע�⡣
 */
public AccpsndocValidator() {
	super();

	setRules(new String[][] {
	    {"psnname", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000064")/*@res "��Ա����"*/, "notnull" },
	});
}
/**
 * validate ����ע�⡣
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject[] records) throws Exception {
	CircularlyAccessibleValueObject accpsndocVO = records[0];
	int jobtype = (((Integer)((GeneralVO)accpsndocVO).getAttributeValue("jobtypeflag")).intValue());//wangkf fixed jobtype->jobtypeflag
	if(jobtype!=0){
	    return;
	}
	//�����ᱣ�Ϻ��Ƿ�Ψһ
//	String pk_psndoc = (String) accpsndocVO.getAttributeValue("pk_psndoc");
//	String pk_psnbasdoc = (String) accpsndocVO.getAttributeValue("pk_psnbasdoc");
//	String ssnum = (String) accpsndocVO.getAttributeValue("ssnum");
//	if (ssnum != null && ssnum.trim().length() > 0) {
////		boolean exists = nc.ui.hi.pub.PsndocMainBO_Client.recordExists("bd_psnbasdoc inner join bd_psndoc on bd_psnbasdoc.pk_psnbasdoc = bd_psndoc.pk_psnbasdoc", "bd_psndoc.pk_psndoc", pk_psndoc, "ssnum", ssnum, "bd_psndoc.pk_corp = '"+nc.ui.hr.global.Global.getCorpPK()+"'");
//		boolean exists = nc.ui.hi.pub.PsndocMainBO_Client.recordExists("bd_psnbasdoc inner join bd_psndoc on bd_psnbasdoc.pk_psnbasdoc = bd_psndoc.pk_psnbasdoc", "bd_psnbasdoc.pk_psnbasdoc", pk_psnbasdoc, "ssnum", ssnum, "bd_psndoc.pk_corp = '"+nc.ui.hr.global.Global.getCorpPK()+"'");
//		if (exists)
//			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000122")/*@res "��ᱣ�ϱ��룺"*/ + ssnum + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000123")/*@res "�Ѿ����ڣ�"*/);
//	}
	//���ڿ���Ψһ �ƶ���PsndocValidator��
//	String timecardid = (String) accpsndocVO.getAttributeValue("timecardid");
//	if (timecardid != null && timecardid.trim().length() > 0) {
//		boolean exists = nc.ui.hi.pub.PsndocMainBO_Client.recordExists("bd_psndoc", "bd_psndoc.pk_psndoc", pk_psndoc, "timecardid", timecardid, "bd_psndoc.pk_corp = '"+nc.ui.hr.global.Global.getCorpPK()+"'");
//		if (exists)
//			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000124")/*@res "���ڿ��ţ�"*/ + timecardid + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000123")/*@res "�Ѿ����ڣ�"*/);
//	}
}
/**
 * validate ����ע�⡣
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject record) throws Exception {
	super.validate(record);
	int jobtype = (((Integer)((GeneralVO)record).getAttributeValue("jobtypeflag")).intValue());//wangkf fixed jobtype->jobtypeflag
	if(jobtype!=0){
	    return;
	}
	try {
		CircularlyAccessibleValueObject accpsndocVO = record;
		//������֤����ĸ�ʽ����ȷ��
		String pk_psnbasdoc = (String)record.getAttributeValue("pk_psnbasdoc");
		String pk_psndoc = (String)record.getAttributeValue("pk_psndoc");
		String id = (String) record.getAttributeValue("id");
		UFDate birth = (UFDate) record.getAttributeValue("birthdate");
		String sex = (String) accpsndocVO.getAttributeValue("sex");
		String psnname = (String)accpsndocVO.getAttributeValue("psnname");
//		
		FieldValidator.validate(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000125")/*@res "���֤"*/, "id", new String[] { id, birth == null ? null : birth.toString(), sex });
		//del by zhyan 2006-04-06 Ĭ�ϰ�����Ա���������֤ Ψһ
//		if (id != null && id.trim().length() > 0) {
////			boolean exists = nc.ui.hi.pub.PsndocMainBO_Client.recordExists("bd_psnbasdoc inner join bd_psndoc on bd_psndoc.pk_psnbasdoc=bd_psnbasdoc.pk_psnbasdoc", "bd_psndoc.pk_psndoc", pk_psndoc, "id", id, psnname);
//			boolean exists = nc.ui.hi.pub.PsndocMainBO_Client.recordExists("bd_psnbasdoc", "pk_psnbasdoc", pk_psnbasdoc, "id", id, psnname);
//			if (exists){
//				//wangkf add ��ʾ��Ϣ���ϣ����š���λ
////				pk_psndoc = (pk_psndoc == null?"":pk_psndoc);
//				GeneralVO vo = nc.ui.hi.pub.PsndocMainBO_Client.queryPsnInfo(pk_psndoc,id,psnname);				
//				String errordisplayText = "  ";//��
//				String psncode = errordisplayText;
//				String deptname =errordisplayText;
//				String omjob =errordisplayText;
//				String unitname = errordisplayText;
//				if(vo != null){
//					psncode = (String)vo.getAttributeValue("psncode");
//					deptname = (String)vo.getAttributeValue("deptname");
//					deptname = (deptname == null?errordisplayText:deptname.trim());
//					omjob = (String)vo.getAttributeValue("jobname");
//					omjob = (omjob== null?errordisplayText:omjob.trim());
//					unitname = (String)vo.getAttributeValue("unitname");
//					unitname = (unitname== null?errordisplayText:unitname.trim());
//				}
//				if(psncode == null || psncode.length() == 0){//
//					if(id != null && id.trim().length()== 18 ){
//						id = id.substring(0,6)+id.substring(8,17);					
//						vo = nc.ui.hi.pub.PsndocMainBO_Client.queryPsnInfo(pk_psndoc,id,psnname);
//						if(vo != null ){
//							psncode = (String)vo.getAttributeValue("psncode");
//							deptname = (String)vo.getAttributeValue("deptname");
//							deptname = (deptname == null?errordisplayText:deptname.trim());
//							omjob = (String)vo.getAttributeValue("jobname");
//							omjob = (omjob== null?errordisplayText:omjob.trim());
//							unitname = (String)vo.getAttributeValue("unitname");
//							unitname = (unitname== null?errordisplayText:unitname.trim());
//						}
//					}
//				}
//				//-----------------------------
////				����˾����XX��¼������XX���ţ�XX��λ
////				throw new Exception("����˾�������֤����Ϊ "+ id +",��Ա����Ϊ "+psnname+",��Ա����Ϊ "+psncode+"�ļ�¼,������ "+deptname+"����,"+omjob+"��λ");
//				throw new Exception(
//						unitname+
//						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000126")/*@res "���������֤����Ϊ "*/+ id +
//						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000127")/*@res ",��Ա����Ϊ "*/+psnname+
//						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000128")/*@res ",��Ա����Ϊ  "*/+psncode+
//						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100001")/*@res "�ļ�¼,������ "*/+deptname+
//						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100002")/*@res "����, "*/+omjob+
//						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100003")/*@res "��λ "*/);			
////				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000126")/*@res "���֤����Ϊ��"*/ + id + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000127")/*@res ",��Ա����Ϊ��"*/+psnname+",����Ա����Ϊ:"+psncode+",�䲿��Ϊ��"+deptname+",���λΪ��"+omjob+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000128")/*@res ",�ڱ���˾�Ѿ����ڣ�"*/);
//			}
//	}
	} catch (Exception e) {
		rethrow(e, "id");
	}
}
/**
 * validate ����ע�⡣
 */
public void validate(nc.vo.pub.ExtendedAggregatedValueObject person) throws Exception {}
}
