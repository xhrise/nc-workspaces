package nc.vo.hi.hi_301.validator;

import nc.vo.pub.lang.*;
/**
 * 此处插入类型描述。
 * 创建日期：(2004-8-2 16:42:03)
 * @author：Administrator
 */
public class TryoutValidator extends OrderlessSubsetValidator {
/**
 * TryoutValidator 构造子注解。
 */
public TryoutValidator() {
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
	 begindate = (UFDate) record.getAttributeValue("dtrybegin");
	try {
		UFDate enddate = (UFDate) record.getAttributeValue("dtryend");
		if (begindate != null && enddate != null)
			if (!begindate.before(enddate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000169")/*@res "试用起始时间必须小于试用终止时间！"*/);
	} catch (Exception ex) {
		rethrow(ex, "dtryend");
	}
}
}
