package nc.vo.hi.hi_304;

/**
 * �Լ���������±���VO
 * 
 * �������ڣ�(2002-4-24 16:15:15)
 * @author��zhonghaijing
 */
public class PsnInfReportVO extends nc.vo.pub.CircularlyAccessibleValueObject {
	String[] fieldCodes = null; //�ֶα���
	int[] fieldTypes = null; //�ֶ�����
	Object[] fieldValues = null; //�ֶ�ȡֵ

/**
 * PsnInfReportVO ������ע�⡣
 */
public PsnInfReportVO() {
	super();
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:26:03)
 * @return java.lang.String[]
 */
public java.lang.String[] getAttributeNames() {
	return getFieldCodes();
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public Object getAttributeValue(String attributeName) {
	if (getFieldCodes() != null && getFieldCodes().length > 0) {
		for (int i = 0; i < getFieldCodes().length; i++) {
			if (attributeName.equals(getFieldCodes()[i]))
				return getFieldValues()[i];
		}
	}
	return null;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getEntityName() {
	return null;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-24 16:40:28)
 * @return java.lang.String[]
 */
public String[] getFieldCodes() {
	return fieldCodes;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-24 16:40:28)
 * @return java.lang.String[]
 */
public int[] getFieldTypes() {
	return fieldTypes;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-24 16:41:16)
 * @return java.lang.String[]
 */
public Object[] getFieldValues() {
	return fieldValues;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public void setAttributeValue(String name, Object value) {
	if (getFieldCodes() != null && getFieldCodes().length > 0) {
		for (int i = 0; i < getFieldCodes().length; i++) {
			if (name.equals(getFieldCodes()[i]))
				getFieldValues()[i] = value;
		}
	}
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-24 16:44:02)
 * @param newOthersFieldCodes java.lang.String[]
 */
public void setFieldCodes(String[] newFieldCodes) {
	fieldCodes = newFieldCodes;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-24 16:44:02)
 * @param newOthersFieldCodes java.lang.String[]
 */
public void setFieldTypes(int[] newFieldTypes) {
	fieldTypes = newFieldTypes;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-27 15:04:00)
 * @param i int
 * @param newValue java.lang.Object
 */
public void setFieldValue(int i, Object newValue) {
	if (i >= 0 && i < getFieldValues().length)
		getFieldValues()[i] = newValue;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-24 16:44:02)
 * @param newOthersFieldCodes java.lang.String[]
 */
public void setFieldValues(Object[] newFieldValues) {
	fieldValues = newFieldValues;
}
/**
 * ��֤���������֮��������߼���ȷ�ԡ�
 * 
 * �������ڣ�(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException �����֤ʧ�ܣ��׳�
 *     ValidationException���Դ�����н��͡�
 */
public void validate() throws nc.vo.pub.ValidationException {}

/**
 * �˴����뷽��������
 * �������ڣ�(2005-1-3 14:18:42)
 * @return java.lang.Object
 */
public Object clone() {
	// ���ƻ������ݲ������µ�VO����
	Object o = null;
	try {
		o = super.clone();
	}
	catch (Exception e) {
	}
	PsnInfReportVO psninfreportVO = (PsnInfReportVO) o;

	// �������渴�Ʊ�VO������������ԣ�
	Object[] newFiledValues = new Object[getFieldValues().length];
	for (int i = 0; i < fieldValues.length; i++){
		Object obj = (Object)getFieldValues()[i];
		if(obj !=null){
			newFiledValues[i] = obj;
		}
	}
	psninfreportVO.setFieldValues(newFiledValues);
	return psninfreportVO;
}
}
