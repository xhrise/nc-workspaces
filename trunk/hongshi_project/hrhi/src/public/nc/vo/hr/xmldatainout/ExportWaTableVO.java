package nc.vo.hr.xmldatainout;

/**
 * �˴���������������
 * �������ڣ�(2003-3-11 15:48:59)
 * @author��������
 */
public class ExportWaTableVO extends nc.vo.pub.ValueObject {
	//Ա������
	private java.lang.String m_pk_psn = null;
	//Ա�����
	private java.lang.String m_personno = null;
	//Ա������
	private java.lang.String m_personname = null;
	//��������
	private java.lang.String m_departmentname = null;
	//��������
	private java.lang.String m_pk_dept = null;
	//Ա���������
	private java.lang.String m_pk_psngrade = null;
/**
 * ExportWaAllVO ������ע�⡣
 */
public ExportWaTableVO() {
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
 * �������ڣ�(2003-3-12 17:05:54)
 * @return java.lang.String
 */
public java.lang.String getM_departmentname() {
	return m_departmentname;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-12 17:03:11)
 * @return java.lang.String
 */
public java.lang.String getM_personname() {
	return m_personname;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-12 17:03:11)
 * @return java.lang.String
 */
public java.lang.String getM_personno() {
	return m_personno;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-12 18:13:23)
 * @return java.lang.String
 */
public java.lang.String getM_pk_dept() {
	return m_pk_dept;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-12 19:07:28)
 * @return java.lang.String
 */
public java.lang.String getM_pk_psn() {
	return m_pk_psn;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-12 18:13:23)
 * @return java.lang.String
 */
public java.lang.String getM_pk_psngrade() {
	return m_pk_psngrade;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-12 17:05:54)
 * @param newM_departmentname java.lang.String
 */
public void setM_departmentname(java.lang.String newM_departmentname) {
	m_departmentname = newM_departmentname;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-12 17:03:11)
 * @param newM_personname java.lang.String
 */
public void setM_personname(java.lang.String newM_personname) {
	m_personname = newM_personname;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-12 17:03:11)
 * @param newM_personno java.lang.String
 */
public void setM_personno(java.lang.String newM_personno) {
	m_personno = newM_personno;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-12 18:13:23)
 * @param newM_pk_dept java.lang.String
 */
public void setM_pk_dept(java.lang.String newM_pk_dept) {
	m_pk_dept = newM_pk_dept;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-12 19:07:28)
 * @param newM_pk_psn java.lang.String
 */
public void setM_pk_psn(java.lang.String newM_pk_psn) {
	m_pk_psn = newM_pk_psn;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-12 18:13:23)
 * @param newM_pk_psngrade java.lang.String
 */
public void setM_pk_psngrade(java.lang.String newM_pk_psngrade) {
	m_pk_psngrade = newM_pk_psngrade;
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
