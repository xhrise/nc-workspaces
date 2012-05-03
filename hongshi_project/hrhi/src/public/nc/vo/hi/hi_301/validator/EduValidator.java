package nc.vo.hi.hi_301.validator;

import nc.vo.hi.hi_301.GeneralVO;
import nc.vo.pub.lang.*;
/**
 * 此处插入类型描述。
 * 创建日期：(2004-8-2 16:28:10)
 * @author：Administrator
 */
public class EduValidator extends CurrentSubsetValidator {
/**
 * EduValidator 构造子注解。
 */
public EduValidator() {
	super();
	setRules(new String[][]{
//		{"education","学历","notnull"}
	});
}

/**
 * validate 方法注解。
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject[] records) throws Exception {
	if (records == null){
		return;
	}
	int count = 0;
	for (int i = 0; i < records.length; i++) {
		Object obj = records[i].getAttributeValue("lasteducation");
		UFBoolean isLasteducation = (UFBoolean)obj;//是否最高学历
		if(isLasteducation != null && isLasteducation.booleanValue()){
			count++;
		}
		if(count > 1){
//			throw new Exception("最高学历只能有一个");//测试用
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100151")/*@res "最高学历只能有一个"*/);
		}
	}
	
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
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000150")/*@res "入学日期必须小于毕业日期！"*/);
	} catch (Exception ex) {
		rethrow(ex, "enddate");
	}
}
}
