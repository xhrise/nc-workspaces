package nc.vo.hr.alert;

/**
 * �˴���������������
 * �������ڣ�(2004-3-30 22:42:40)
 * @author��zhonghaijing
 */
public class Pk_psnAndDeptmanaVO extends nc.vo.pub.ValueObject {
	private java.lang.String pk_deptmana;
	private java.lang.String pk_psndoc;
	private java.lang.String deptmananame;
/**
 * Pk_psnAndDeptmanaVO ������ע�⡣
 */
public Pk_psnAndDeptmanaVO() {
	super();
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-3-31 9:50:25)
 * @return java.lang.String
 */
public java.lang.String getDeptmananame() {
	return deptmananame;
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
 * �������ڣ�(2004-3-30 22:46:41)
 * @return java.lang.String
 */
public java.lang.String getPk_deptmana() {
	return pk_deptmana;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-3-30 22:47:38)
 * @return java.lang.String
 */
public java.lang.String getPk_psndoc() {
	return pk_psndoc;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-3-31 9:50:25)
 * @param newDeptmananame java.lang.String
 */
public void setDeptmananame(java.lang.String newDeptmananame) {
	deptmananame = newDeptmananame;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-3-30 22:46:41)
 * @param newPk_deptmana java.lang.String
 */
public void setPk_deptmana(java.lang.String newPk_deptmana) {
	pk_deptmana = newPk_deptmana;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-3-30 22:47:38)
 * @param newPk_psndoc java.lang.String
 */
public void setPk_psndoc(java.lang.String newPk_psndoc) {
	pk_psndoc = newPk_psndoc;
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
