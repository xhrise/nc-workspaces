package nc.vo.hi.hi_301.validator;

import nc.itf.hi.HIDelegator;
import nc.vo.pub.lang.*;
/**
 * 此处插入类型描述。
 * 创建日期：(2004-5-17 17:17:17)
 * @author：Administrator
 */
import nc.vo.hi.hi_301.*;

public class DeptchgValidator extends OrderlessSubsetValidator {
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-17 17:27:00)
 * @param record nc.vo.pub.CircularlyAccessibleValueObject
 * @exception java.lang.Exception 异常说明。
 */
private void checkRecord(nc.vo.pub.CircularlyAccessibleValueObject record) throws java.lang.Exception {
	//开始日期非空
	UFDate begindate = (UFDate) record.getAttributeValue("begindate");
	FieldValidator.validate(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000144")/*@res "开始日期"*/, "notnull", begindate);
	//检查结束日期
	Integer recordNum = (Integer) record.getAttributeValue("recordnum");
	UFBoolean lastflag = (UFBoolean) record.getAttributeValue("lastflag");
	UFDate enddate = (UFDate) record.getAttributeValue("enddate");
	if (recordNum != null && recordNum.intValue() > 1) {
		//非当前记录结束日期不能为空
		FieldValidator.validate(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000145")/*@res "结束日期"*/, "notnull", enddate);
	}
	//开始日期必须小于结束日期
	if (begindate != null && enddate != null)
		if (!begindate.before(enddate))
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000146")/*@res "开始日期必须小于结束日期！"*/);
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-17 10:06:00)
 * @return java.lang.String[][]
 */
public java.lang.String[][] getRules() {
	return new String[][] { 
			{ "pk_psncl", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","upt600704-000042")/*@res "人员类别"*/, "notnull" },			
	        {"pk_deptdoc", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","upt600704-000139")/*@res "部门"*/, "notnull" }
	};

}
/**
 * validate 方法注解。
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject[] records) throws Exception {
	if (records == null)
		return;
	GeneralVO vo = (GeneralVO)records[0];
	//部门和岗位对应关系是否正确
	String deptpk = (String) vo.getFieldValue("pk_deptdoc");
	String jobpk = (String) vo.getFieldValue("pk_postdoc");
	if (deptpk!= null&&jobpk!=null) {
		boolean exists = HIDelegator.getIHRhiQBS().recordExists("om_job", "", null, "pk_om_job", jobpk, "pk_deptdoc='"+deptpk+"'");
		if (!exists)
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000147")/*@res "选择的岗位和部门不匹配，请重新选择！"*/);
	}
	if (records.length > 1) {
	    //检查每一记录的正确性
		for(int i=0;i<records.length;i++){
			checkRecord(records[i]);
		}
		//检查记录之间有效性
		//super.validate(records);
	} else
		if (records.length > 0) {//唯一记录
			//开始日期非空
			UFDate begindate = (UFDate) records[0].getAttributeValue("begindate");
			//检查结束日期
			UFDate enddate = (UFDate) records[0].getAttributeValue("enddate");
			//开始日期必须小于结束日期
			if (begindate != null && enddate != null){
				if (!begindate.before(enddate))	throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000146")/*@res "开始日期必须小于结束日期！"*/);
				//--------------------
				//	判断是否为任职记录的修改
				boolean flag = HIDelegator.getIHRhiQBS().checkPsnWorkDate(
						(String)(records[0].getAttributeValue("$pk_corp")), (String)vo.getAttributeValue("pk_psndoc"),
						begindate, enddate);
				if(flag){
//					throw new Exception("任职日期和其他公司的任职日期有冲突");
					throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"600700", "UPP600700-000239")/*@res "任职日期和其他公司的任职日期有冲突"*/);
				}
				//  ----------------------
			}
		}
	for (int i = 0; i < records.length-1; i++) {
		UFDate firstbegindate = (UFDate)records[i].getAttributeValue("begindate");
		UFDate firstenddate = (UFDate)records[i].getAttributeValue("enddate");//wangkf add
		UFDate secondbegindate =(UFDate)records[i+1].getAttributeValue("begindate");//wangkf add
		UFDate secondenddate=(UFDate)records[i+1].getAttributeValue("enddate");
		if(firstenddate!=null&&secondbegindate!=null){
			if(!secondbegindate.after(firstenddate)){
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000091")/*@res "第"*/+(i+2)+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000148")/*@res "行记录的开始日期不能等于或者早于第"*/+(i+1)+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000149")/*@res "行记录的结束日期！"*/);
			}
		}
		if(firstbegindate!=null&&secondbegindate != null){//wangkf add 需要添加新的判断
			if(!secondbegindate.after(firstbegindate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000091")/*@res "第"*/+(i+2)+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000148")/*@res "行记录的开始日期不能等于或者早于第"*/+(i+1)+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100149")/*@res "行记录的开始日期！"*/);
		
		}
		if(firstbegindate!=null&&secondbegindate != null&&firstenddate==null){//wangkf add 需要添加新的判断
			if(!secondbegindate.getDateBefore(1).after(firstbegindate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000091")/*@res "第"*/+(i+2)+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000148")/*@res "行记录的开始日期不能等于或者早于第"*/+(i+1)+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100150")/*@res "行记录的开始日期后一天！"*/);
		
		}
	}

}
/**
 * 覆盖父类方法，恢复正常检查
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject record) throws Exception {
	String[][] rules = getRules();
	if (rules != null) {
		for (int i = 0; i < rules.length; i++) {
			try {
				FieldValidator.validate(rules[i][1], rules[i][2], ((GeneralVO)record).getFieldValue(rules[i][0]));
			} catch (Exception e) {
	            //重新组织，携带字段信息
	            rethrow(e,rules[i][0]);
			}
		}
	}
	
//	部门和岗位对应关系是否正确
	String deptpk = (String) ((GeneralVO)record).getFieldValue("pk_deptdoc");
	String jobpk = (String) ((GeneralVO)record).getFieldValue("pk_postdoc");
	if (deptpk!= null&&jobpk!=null) {
		boolean exists = HIDelegator.getIHRhiQBS().recordExists("om_job", "", null, "pk_om_job", jobpk, "pk_deptdoc='"+deptpk+"'");
		if (!exists)
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000147")/*@res "选择的岗位和部门不匹配，请重新选择！"*/);
	}
	
}
}
