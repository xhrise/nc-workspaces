package nc.vo.hr.xmldatainout;

/**
 * �˴���������������
 * �������ڣ�(2003-3-9 16:40:00)
 * @author��������
 */
public class ExportWaPsnVO extends nc.vo.pub.ValueObject {
	//Ա�����
	private java.lang.String m_personno = null;
	//���ű��
	private java.lang.String m_departmentno = null;

	//��Ա�����
	private java.lang.String m_persongradeno = null;
	//Ա������
	private java.lang.String m_personname = null;

	//���ø���Entry��Ŀ�����确�Ա𡯡���ѧ��������ȱ����������
	private java.util.Hashtable m_entryHash = new java.util.Hashtable();
/**
 * ExportWaPsnVO ������ע�⡣
 */
public ExportWaPsnVO() {
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
 * �������ڣ�(2003-3-9 16:47:23)
 * @return java.lang.String
 */
public java.lang.String getM_departmentno() {
	return m_departmentno;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-10 16:10:35)
 * @return java.util.Hashtable
 */
public java.util.Hashtable getM_entryHash() {
	return m_entryHash;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-9 16:48:40)
 * @return java.lang.String
 */
public java.lang.String getM_persongradeno() {
	return m_persongradeno;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-9 16:49:54)
 * @return java.lang.String
 */
public java.lang.String getM_personname() {
	return m_personname;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-9 16:46:03)
 * @return java.lang.String
 */
public java.lang.String getM_personno() {
	return m_personno;
}


/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-9 16:47:23)
 * @param newDepartmentno java.lang.String
 */
public void setM_departmentno(java.lang.String newDepartmentno) {
	m_departmentno = newDepartmentno;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-10 16:10:35)
 * @param newM_entryHash java.util.Hashtable
 */
public void setM_entryHash(java.util.Hashtable newM_entryHash) {
	m_entryHash.clear();
	m_entryHash.putAll(newM_entryHash);
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-9 16:48:40)
 * @param newM_persongradeno java.lang.String
 */
public void setM_persongradeno(java.lang.String newM_persongradeno) {
	m_persongradeno = newM_persongradeno;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-9 16:49:54)
 * @param newM_personname java.lang.String
 */
public void setM_personname(java.lang.String newM_personname) {
	m_personname = newM_personname;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-9 16:46:03)
 * @param newM_personno java.lang.String
 */
public void setM_personno(java.lang.String newM_personno) {
	m_personno = newM_personno;
}


/**
 * ��֤���������֮��������߼���ȷ�ԡ�
 * 
 * �������ڣ�(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException �����֤ʧ�ܣ��׳�
 *     ValidationException���Դ�����н��͡�
 */
public void validate() throws nc.vo.pub.ValidationException {}

	//U8�ж�Ӧ��Sender��
	private java.lang.String m_strSenderNo = null;

/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-11 16:29:37)
 * @return java.lang.String
 */
public java.lang.String getM_strSenderNo() {
	return m_strSenderNo;
}

/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-11 16:29:37)
 * @param newM_strSenderNo java.lang.String
 */
public void setM_strSenderNo(java.lang.String newM_strSenderNo) {
	m_strSenderNo = newM_strSenderNo;
}
}
