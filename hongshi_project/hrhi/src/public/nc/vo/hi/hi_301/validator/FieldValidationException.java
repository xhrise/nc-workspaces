package nc.vo.hi.hi_301.validator;

import nc.bs.bank_cvp.compile.registry.BussinessMethods;
import nc.vo.pub.BusinessException;

/**
 * 此处插入类型描述。
 * 创建日期：(2004-6-5 10:37:08)
 * @author：Administrator
 */
public class FieldValidationException extends Exception {
	private java.lang.String fieldCode;

	private String errorCodeString = ""; 
 
/**
 * FieldValidationException 构造子注解。
 */
public FieldValidationException() {
	super();
}
/**
 * FieldValidationException 构造子注解。
 * @param s java.lang.String
 */
public FieldValidationException(String s) {
	super(s);
}
public FieldValidationException(BusinessException e) {
	//super(e);
	super(e.getMessage());
	this.setErrorCodeString(e.getErrorCodeString());
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-6-5 10:37:29)
 * @return java.lang.String
 */
public java.lang.String getFieldCode() {
	return fieldCode;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-6-5 10:37:29)
 * @param newFieldCode java.lang.String
 */
public void setFieldCode(java.lang.String newFieldCode) {
	fieldCode = newFieldCode;
}

public String getErrorCodeString() {
	return errorCodeString;
}

public void setErrorCodeString(String errorCode) {
	this.errorCodeString = errorCode;
}

}
