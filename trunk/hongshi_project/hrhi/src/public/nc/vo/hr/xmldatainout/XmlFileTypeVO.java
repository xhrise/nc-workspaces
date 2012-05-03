package nc.vo.hr.xmldatainout;

/**
 * 此处插入类型描述。
 * 创建日期：(2003-01-15 14:51:27)
 * @author：张  森
 */
public class XmlFileTypeVO extends nc.vo.pub.ValueObject 
{
	private java.lang.String stTypeName = null;
	private java.lang.String stTypeCode = null;
	private java.lang.String[] descFileName = null;
/**
 * XmlFileTypeVO 构造子注解。
 */
public XmlFileTypeVO() {
	super();
}
/**
 * XmlFileTypeVO 构造子注解。
 */
public XmlFileTypeVO(String typeName,String typeCode,String[] descFile) 
{
	super();
	setStTypeName(typeName);
	setStTypeCode(typeCode);
	setDescFileName(descFile);
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-01-15 14:54:44)
 * @return java.lang.String[]
 */
public java.lang.String[] getDescFileName() {
	return descFileName;
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
 * 此处插入方法描述。
 * 创建日期：(2003-01-15 14:52:54)
 * @return java.lang.String
 */
public java.lang.String getStTypeCode() {
	return stTypeCode;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-01-15 14:52:25)
 * @return java.lang.String
 */
public java.lang.String getStTypeName() {
	return stTypeName;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-01-15 14:54:44)
 * @param newDescFileName java.lang.String[]
 */
public void setDescFileName(java.lang.String[] newDescFileName) {
	descFileName = newDescFileName;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-01-15 14:52:54)
 * @param newStTypeCode java.lang.String
 */
public void setStTypeCode(java.lang.String newStTypeCode) {
	stTypeCode = newStTypeCode;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-01-15 14:52:25)
 * @param newStTypeName java.lang.String
 */
public void setStTypeName(java.lang.String newStTypeName) {
	stTypeName = newStTypeName;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-01-15 15:04:54)
 * @return java.lang.String
 */
public String toString() 
{
	return getStTypeName();
}
/**
 * 验证对象各属性之间的数据逻辑正确性。
 * 
 * 创建日期：(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
 *     ValidationException，对错误进行解释。
 */
public void validate() throws nc.vo.pub.ValidationException {}
}
