package nc.vo.hi.hi_301.validator;

import nc.bs.logging.Logger;
import nc.vo.pub.lang.*;
/**
 * 此处插入类型描述。
 * 创建日期：(2004-8-2 11:01:44)
 * @author：Administrator
 */
public class AssValidator extends OrderlessSubsetValidator {
/**
 * AssValidator 构造子注解。
 */
public AssValidator() {
	super();
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-8-2 11:05:50)
 * @param record nc.vo.pub.CircularlyAccessibleValueObject
 * @exception java.lang.Exception 异常说明。
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject record)
	throws java.lang.Exception {
		setRules(new String[][] {
			{"begindate",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"common", "UC000-0003900")/* @res "起始日期" */,"notnull" },
			{"enddate",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"common", "UC000-0003203")/* @res "终止日期" */,"notnull" } });
		super.validate(record);
	UFDate begindate=null;
	 begindate = (UFDate) record.getAttributeValue("begindate");
	try {
		UFDate enddate = (UFDate) record.getAttributeValue("enddate");
		if (begindate != null && enddate != null)
			if (begindate.after(enddate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000129")/*@res "考核期起始时间不能晚于考核期终止时间！"*/);
	} catch (Exception ex) {
		rethrow(ex, "enddate");
	}
	if(record.getAttributeValue("nassessscore")!=null){
		Logger.error(record.getAttributeValue("nassessscore").getClass().getName());
		UFDouble value = (UFDouble)record.getAttributeValue("nassessscore");
		if((value.compareTo(new nc.vo.pub.lang.UFDouble(0.0))<0)||(value.compareTo(new UFDouble(100.0))>0)){
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000130")/*@res "考核成绩值的范围应该是0-100"*/);
		}
	}
	}
}
