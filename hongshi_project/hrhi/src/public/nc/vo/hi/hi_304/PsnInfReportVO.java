package nc.vo.hi.hi_304;

/**
 * 自己定义的人事报表VO
 * 
 * 创建日期：(2002-4-24 16:15:15)
 * @author：zhonghaijing
 */
public class PsnInfReportVO extends nc.vo.pub.CircularlyAccessibleValueObject {
	String[] fieldCodes = null; //字段编码
	int[] fieldTypes = null; //字段类型
	Object[] fieldValues = null; //字段取值

/**
 * PsnInfReportVO 构造子注解。
 */
public PsnInfReportVO() {
	super();
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:26:03)
 * @return java.lang.String[]
 */
public java.lang.String[] getAttributeNames() {
	return getFieldCodes();
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
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
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getEntityName() {
	return null;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-24 16:40:28)
 * @return java.lang.String[]
 */
public String[] getFieldCodes() {
	return fieldCodes;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-24 16:40:28)
 * @return java.lang.String[]
 */
public int[] getFieldTypes() {
	return fieldTypes;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-24 16:41:16)
 * @return java.lang.String[]
 */
public Object[] getFieldValues() {
	return fieldValues;
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
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
 * 此处插入方法说明。
 * 创建日期：(2002-4-24 16:44:02)
 * @param newOthersFieldCodes java.lang.String[]
 */
public void setFieldCodes(String[] newFieldCodes) {
	fieldCodes = newFieldCodes;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-24 16:44:02)
 * @param newOthersFieldCodes java.lang.String[]
 */
public void setFieldTypes(int[] newFieldTypes) {
	fieldTypes = newFieldTypes;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-27 15:04:00)
 * @param i int
 * @param newValue java.lang.Object
 */
public void setFieldValue(int i, Object newValue) {
	if (i >= 0 && i < getFieldValues().length)
		getFieldValues()[i] = newValue;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-24 16:44:02)
 * @param newOthersFieldCodes java.lang.String[]
 */
public void setFieldValues(Object[] newFieldValues) {
	fieldValues = newFieldValues;
}
/**
 * 验证对象各属性之间的数据逻辑正确性。
 * 
 * 创建日期：(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
 *     ValidationException，对错误进行解释。
 */
public void validate() throws nc.vo.pub.ValidationException {}

/**
 * 此处插入方法描述。
 * 创建日期：(2005-1-3 14:18:42)
 * @return java.lang.Object
 */
public Object clone() {
	// 复制基类内容并创建新的VO对象：
	Object o = null;
	try {
		o = super.clone();
	}
	catch (Exception e) {
	}
	PsnInfReportVO psninfreportVO = (PsnInfReportVO) o;

	// 你在下面复制本VO对象的所有属性：
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
