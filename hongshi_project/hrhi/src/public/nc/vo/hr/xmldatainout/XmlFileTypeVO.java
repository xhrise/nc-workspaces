package nc.vo.hr.xmldatainout;

/**
 * �˴���������������
 * �������ڣ�(2003-01-15 14:51:27)
 * @author����  ɭ
 */
public class XmlFileTypeVO extends nc.vo.pub.ValueObject 
{
	private java.lang.String stTypeName = null;
	private java.lang.String stTypeCode = null;
	private java.lang.String[] descFileName = null;
/**
 * XmlFileTypeVO ������ע�⡣
 */
public XmlFileTypeVO() {
	super();
}
/**
 * XmlFileTypeVO ������ע�⡣
 */
public XmlFileTypeVO(String typeName,String typeCode,String[] descFile) 
{
	super();
	setStTypeName(typeName);
	setStTypeCode(typeCode);
	setDescFileName(descFile);
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-01-15 14:54:44)
 * @return java.lang.String[]
 */
public java.lang.String[] getDescFileName() {
	return descFileName;
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
 * �˴����뷽��������
 * �������ڣ�(2003-01-15 14:52:54)
 * @return java.lang.String
 */
public java.lang.String getStTypeCode() {
	return stTypeCode;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-01-15 14:52:25)
 * @return java.lang.String
 */
public java.lang.String getStTypeName() {
	return stTypeName;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-01-15 14:54:44)
 * @param newDescFileName java.lang.String[]
 */
public void setDescFileName(java.lang.String[] newDescFileName) {
	descFileName = newDescFileName;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-01-15 14:52:54)
 * @param newStTypeCode java.lang.String
 */
public void setStTypeCode(java.lang.String newStTypeCode) {
	stTypeCode = newStTypeCode;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-01-15 14:52:25)
 * @param newStTypeName java.lang.String
 */
public void setStTypeName(java.lang.String newStTypeName) {
	stTypeName = newStTypeName;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-01-15 15:04:54)
 * @return java.lang.String
 */
public String toString() 
{
	return getStTypeName();
}
/**
 * ��֤���������֮��������߼���ȷ�ԡ�
 * 
 * �������ڣ�(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException �����֤ʧ�ܣ��׳�
 *     ValidationException���Դ�����н��͡�
 */
public void validate() throws nc.vo.pub.ValidationException {}
}
