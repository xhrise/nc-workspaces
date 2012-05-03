package nc.vo.hi.hi_305;

/**
 * ���µĲ���VO
 * 
 * �������ڣ�(2002-5-10 16:23:03)
 * @author��zhonghaijing
 */
public class FldRefTypeVO extends nc.vo.pub.ValueObject {
	public String strTableCode; //����
	public String strFieldCode; //�ֶ���
	public String strRefTypePK; //������������
/**
 * FldRefTypeVO ������ע�⡣
 */
public FldRefTypeVO() {
	super();
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getEntityName() {
	return "FldRefType";
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-5-10 16:27:08)
 * @return java.lang.String
 */
public String getFeildCode() {
	return strFieldCode;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-5-10 16:27:08)
 * @return java.lang.String
 */
public String getRefTypePK() {
	return strRefTypePK;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-5-10 16:27:08)
 * @return java.lang.String
 */
public String getTableCode() {
	return strTableCode;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-5-10 16:31:31)
 * @param newTableCode java.lang.String
 */
public void setFieldCode(String newFieldCode) {
	strFieldCode = newFieldCode;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-5-10 16:31:31)
 * @param newTableCode java.lang.String
 */
public void setRefTypePK(String newRefTypePK) {
	strRefTypePK = newRefTypePK;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-5-10 16:31:31)
 * @param newTableCode java.lang.String
 */
public void setTableCode(String newTableCode) {
	strTableCode = newTableCode;
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
