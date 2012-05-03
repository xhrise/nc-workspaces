package nc.vo.hi.hi_301.validator;

import nc.vo.pub.lang.*;
/**
 * 此处插入类型描述。
 * 创建日期：(2004-5-15 13:46:18)
 * @author：Administrator
 */
import nc.vo.hi.hi_301.*;
import nc.vo.pub.*;
//import nc.ui.hr.global.*;
public class AccpsndocValidator extends AbstractValidator{
/**
 * AccpsndocValidator 构造子注解。
 */
public AccpsndocValidator() {
	super();

	setRules(new String[][] {
	    {"psnname", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000064")/*@res "人员姓名"*/, "notnull" },
	});
}
/**
 * validate 方法注解。
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject[] records) throws Exception {
	CircularlyAccessibleValueObject accpsndocVO = records[0];
	int jobtype = (((Integer)((GeneralVO)accpsndocVO).getAttributeValue("jobtypeflag")).intValue());//wangkf fixed jobtype->jobtypeflag
	if(jobtype!=0){
	    return;
	}
	//检查社会保障号是否唯一
//	String pk_psndoc = (String) accpsndocVO.getAttributeValue("pk_psndoc");
//	String pk_psnbasdoc = (String) accpsndocVO.getAttributeValue("pk_psnbasdoc");
//	String ssnum = (String) accpsndocVO.getAttributeValue("ssnum");
//	if (ssnum != null && ssnum.trim().length() > 0) {
////		boolean exists = nc.ui.hi.pub.PsndocMainBO_Client.recordExists("bd_psnbasdoc inner join bd_psndoc on bd_psnbasdoc.pk_psnbasdoc = bd_psndoc.pk_psnbasdoc", "bd_psndoc.pk_psndoc", pk_psndoc, "ssnum", ssnum, "bd_psndoc.pk_corp = '"+nc.ui.hr.global.Global.getCorpPK()+"'");
//		boolean exists = nc.ui.hi.pub.PsndocMainBO_Client.recordExists("bd_psnbasdoc inner join bd_psndoc on bd_psnbasdoc.pk_psnbasdoc = bd_psndoc.pk_psnbasdoc", "bd_psnbasdoc.pk_psnbasdoc", pk_psnbasdoc, "ssnum", ssnum, "bd_psndoc.pk_corp = '"+nc.ui.hr.global.Global.getCorpPK()+"'");
//		if (exists)
//			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000122")/*@res "社会保障编码："*/ + ssnum + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000123")/*@res "已经存在！"*/);
//	}
	//考勤卡号唯一 移动到PsndocValidator中
//	String timecardid = (String) accpsndocVO.getAttributeValue("timecardid");
//	if (timecardid != null && timecardid.trim().length() > 0) {
//		boolean exists = nc.ui.hi.pub.PsndocMainBO_Client.recordExists("bd_psndoc", "bd_psndoc.pk_psndoc", pk_psndoc, "timecardid", timecardid, "bd_psndoc.pk_corp = '"+nc.ui.hr.global.Global.getCorpPK()+"'");
//		if (exists)
//			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000124")/*@res "考勤卡号："*/ + timecardid + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000123")/*@res "已经存在！"*/);
//	}
}
/**
 * validate 方法注解。
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject record) throws Exception {
	super.validate(record);
	int jobtype = (((Integer)((GeneralVO)record).getAttributeValue("jobtypeflag")).intValue());//wangkf fixed jobtype->jobtypeflag
	if(jobtype!=0){
	    return;
	}
	try {
		CircularlyAccessibleValueObject accpsndocVO = record;
		//检查身份证号码的格式的正确性
		String pk_psnbasdoc = (String)record.getAttributeValue("pk_psnbasdoc");
		String pk_psndoc = (String)record.getAttributeValue("pk_psndoc");
		String id = (String) record.getAttributeValue("id");
		UFDate birth = (UFDate) record.getAttributeValue("birthdate");
		String sex = (String) accpsndocVO.getAttributeValue("sex");
		String psnname = (String)accpsndocVO.getAttributeValue("psnname");
//		
		FieldValidator.validate(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000125")/*@res "身份证"*/, "id", new String[] { id, birth == null ? null : birth.toString(), sex });
		//del by zhyan 2006-04-06 默认按照人员姓名＋身份证 唯一
//		if (id != null && id.trim().length() > 0) {
////			boolean exists = nc.ui.hi.pub.PsndocMainBO_Client.recordExists("bd_psnbasdoc inner join bd_psndoc on bd_psndoc.pk_psnbasdoc=bd_psnbasdoc.pk_psnbasdoc", "bd_psndoc.pk_psndoc", pk_psndoc, "id", id, psnname);
//			boolean exists = nc.ui.hi.pub.PsndocMainBO_Client.recordExists("bd_psnbasdoc", "pk_psnbasdoc", pk_psnbasdoc, "id", id, psnname);
//			if (exists){
//				//wangkf add 提示信息加上：部门、岗位
////				pk_psndoc = (pk_psndoc == null?"":pk_psndoc);
//				GeneralVO vo = nc.ui.hi.pub.PsndocMainBO_Client.queryPsnInfo(pk_psndoc,id,psnname);				
//				String errordisplayText = "  ";//无
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
////				本公司已有XX记录，现在XX部门，XX岗位
////				throw new Exception("本公司已有身份证号码为 "+ id +",人员姓名为 "+psnname+",人员编码为 "+psncode+"的记录,现在在 "+deptname+"部门,"+omjob+"岗位");
//				throw new Exception(
//						unitname+
//						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000126")/*@res "中已有身份证号码为 "*/+ id +
//						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000127")/*@res ",人员姓名为 "*/+psnname+
//						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000128")/*@res ",人员编码为  "*/+psncode+
//						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100001")/*@res "的记录,现在在 "*/+deptname+
//						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100002")/*@res "部门, "*/+omjob+
//						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100003")/*@res "岗位 "*/);			
////				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000126")/*@res "身份证号码为："*/ + id + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000127")/*@res ",人员姓名为："*/+psnname+",其人员编码为:"+psncode+",其部门为："+deptname+",其岗位为："+omjob+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000128")/*@res ",在本公司已经存在！"*/);
//			}
//	}
	} catch (Exception e) {
		rethrow(e, "id");
	}
}
/**
 * validate 方法注解。
 */
public void validate(nc.vo.pub.ExtendedAggregatedValueObject person) throws Exception {}
}
