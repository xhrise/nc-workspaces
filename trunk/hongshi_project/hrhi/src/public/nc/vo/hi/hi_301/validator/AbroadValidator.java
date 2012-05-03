package nc.vo.hi.hi_301.validator;

import nc.vo.pub.lang.*;
/**
 * 此处插入类型描述。
 * 创建日期：(2004-8-2 16:28:10)
 * @author：Administrator
 */
public class AbroadValidator extends CurrentSubsetValidator {
/**
 * AbroadValidator 构造子注解。
 */
public AbroadValidator() {
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
	 super.validate(record);
	 UFDate begindate=null;
	 begindate = (UFDate) record.getAttributeValue("dabroaddate");//dabroaddate  begindate
	try {
		UFDate enddate = (UFDate) record.getAttributeValue("dabroadreturn");//dabroadreturn  enddate
		if (begindate != null && enddate != null)
			if (begindate.after(enddate)){
//				throw new Exception("出国(出境)时间必须小于回国时间!");
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPT600704-000243")/*@res "出国(出境)时间必须小于回国时间!"*/);
			}
	} catch (Exception ex) {
		rethrow(ex, "enddate");
	}
}
}
