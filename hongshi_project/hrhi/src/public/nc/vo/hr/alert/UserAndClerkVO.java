package nc.vo.hr.alert;

/**
 * �˴���������������
 * �������ڣ�(2004-4-2 9:47:34)
 * @author��zhonghaijing
 */
public class UserAndClerkVO extends nc.vo.pub.ValueObject{
	private java.lang.String userid;
	private java.lang.String pk_psndoc;
	private java.lang.String pk_corp;
	private java.lang.String pk_userandclerk;
	private java.lang.String psnname;
	private String pk_deptmana;
/**
 * UserAndClerk ������ע�⡣
 */
public UserAndClerkVO() {
	super();
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
 * �������ڣ�(2004-4-2 10:22:55)
 * @return java.lang.String
 */
public java.lang.String getPk_corp() {
	return pk_corp;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-6-3 18:19:52)
 * @return int
 */
public String getPk_deptmana() {
	return pk_deptmana;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-4-2 9:58:08)
 * @return java.lang.String
 */
public java.lang.String getPk_psndoc() {
	return pk_psndoc;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-4-2 10:23:16)
 * @return java.lang.String
 */
public java.lang.String getPk_userandclerk() {
	return pk_userandclerk;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-4-2 11:34:25)
 * @return java.lang.String
 */
public java.lang.String getPsnname() {
	return psnname;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-4-2 9:52:49)
 * @return java.lang.String
 */
public java.lang.String getUserid() {
	return userid;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-4-2 10:22:55)
 * @param newPk_corp java.lang.String
 */
public void setPk_corp(java.lang.String newPk_corp) {
	pk_corp = newPk_corp;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-6-3 18:19:52)
 * @param newPk_deptmana int
 */
public void setPk_deptmana(String newPk_deptmana) {
	pk_deptmana = newPk_deptmana;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-4-2 9:58:08)
 * @param newPk_psndoc java.lang.String
 */
public void setPk_psndoc(java.lang.String newPk_psndoc) {
	pk_psndoc = newPk_psndoc;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-4-2 10:23:16)
 * @param newPk_userandclerk java.lang.String
 */
public void setPk_userandclerk(java.lang.String newPk_userandclerk) {
	pk_userandclerk = newPk_userandclerk;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-4-2 11:34:25)
 * @param newPsnname java.lang.String
 */
public void setPsnname(java.lang.String newPsnname) {
	psnname = newPsnname;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-4-2 9:52:49)
 * @param newUserid java.lang.String
 */
public void setUserid(java.lang.String newUserid) {
	userid = newUserid;
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
