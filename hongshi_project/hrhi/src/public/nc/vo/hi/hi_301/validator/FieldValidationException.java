package nc.vo.hi.hi_301.validator;

import nc.bs.bank_cvp.compile.registry.BussinessMethods;
import nc.vo.pub.BusinessException;

/**
 * �˴���������������
 * �������ڣ�(2004-6-5 10:37:08)
 * @author��Administrator
 */
public class FieldValidationException extends Exception {
	private java.lang.String fieldCode;

	private String errorCodeString = ""; 
 
/**
 * FieldValidationException ������ע�⡣
 */
public FieldValidationException() {
	super();
}
/**
 * FieldValidationException ������ע�⡣
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
 * �˴����뷽��������
 * �������ڣ�(2004-6-5 10:37:29)
 * @return java.lang.String
 */
public java.lang.String getFieldCode() {
	return fieldCode;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-6-5 10:37:29)
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
