package nc.vo.hi.hi_301.validator;

import nc.vo.pub.lang.*;
/**
 * 此处插入类型描述。
 * 创建日期：(2004-8-2 16:54:31)
 * @author：Administrator
 */
public class SpeitemValidator extends CurrentSubsetValidator {
/**
 * SpeitemValidator 构造子注解。
 */
public SpeitemValidator() {
	super();
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-8-2 16:31:20)
 * @param record nc.vo.pub.CircularlyAccessibleValueObject
 * @exception java.lang.Exception 异常说明。
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject record)
	throws java.lang.Exception {
	 UFDate begindate=null;
	 begindate = (UFDate) record.getAttributeValue("dspebegin");
	try {
		UFDate enddate = (UFDate) record.getAttributeValue("dspeend");
		if (begindate != null && enddate != null)
			if (!begindate.before(enddate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000167")/*@res "项目起始时间必须小于项目终止时间！"*/);
	} catch (Exception ex) {
		rethrow(ex, "dengend");
	}
}
}
