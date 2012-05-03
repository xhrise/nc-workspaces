package nc.vo.hi.hi_301.validator;

import nc.itf.hi.HIDelegator;
import nc.vo.pub.lang.*;
/**
 * 员工兼职情况。
 * 创建日期：(2004-5-18 8:44:29)
 * @author：Administrator
 */
public class PartValidator extends CurrentSubsetValidator {
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-18 8:45:43)
 */
public PartValidator() {
	super();
	setRules(new String[][]{
		{ "pk_psncl", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","upt600704-000042")/*@res "人员类别"*/, "notnull" },
		{ "pk_corp", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000053")/*@res "公司"*/, "notnull" },
		{ "pk_deptdoc", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","upt600704-000109")/*@res "任职部门"*/, "notnull" },
		{ "jobtype", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPT600704-000184")/*@res "任职类型"*/, "notnull" },
//		{ "begindate", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000144")/*@res "开始日期"*/, "notnull" }
	});

}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-8-2 16:31:20)
 * @param record nc.vo.pub.CircularlyAccessibleValueObject
 * @exception java.lang.Exception 异常说明。
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject record)
	throws java.lang.Exception {
	 super.validate(record);
	 UFDate begindate=null;
	 begindate = (UFDate) record.getAttributeValue("begindate");
	try {
		UFDate enddate = (UFDate) record.getAttributeValue("enddate");
		if (begindate != null && enddate != null)
			if (!begindate.before(enddate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000163")/*@res "兼职起始时间必须小于兼职终止时间！"*/);
	} catch (Exception ex) {
		rethrow(ex, "enddate");
	}
	//部门和岗位对应关系是否正确
	String deptpk = (String) ((nc.vo.hi.hi_301.GeneralVO)record).getFieldValue("pk_deptdoc");
	String jobpk = (String) ((nc.vo.hi.hi_301.GeneralVO)record).getFieldValue("pk_postdoc");
	if (deptpk!= null&&jobpk!=null) {
		boolean exists = HIDelegator.getIHRhiQBS().recordExists("om_job", "", null, "pk_om_job", jobpk, "pk_deptdoc='"+deptpk+"'");
		if (!exists)
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000147")/*@res "选择的岗位和部门不匹配，请重新选择！"*/);
	}
		// 结束的兼职记录结束日期不能为空！
		UFBoolean bendflag = (UFBoolean) ((nc.vo.hi.hi_301.GeneralVO) record)
				.getFieldValue("bendflag");
		if (bendflag.booleanValue()
				&& record.getAttributeValue("enddate") == null) {
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("600704", "UPT600704-000322")/*
																 * @res
																 * "结束的兼职记录结束日期不能为空！"
																 */);
		}
	}
}
