package nc.vo.hr.xmldatainout;

/**
 * U8ϵͳ��һ����˾��һ�����ף���Hrϵͳһ���������ж����˾
   �����ͱ����ڴ���ʱ�ṩ������Ϣ��Hr��һ����˾��ӦU8��һ��
   �������ס�
 * �������ڣ�(2003-3-7 9:43:21)
 * @author��������
 */
public class Corp_U8SenderVO extends nc.vo.pub.ValueObject{
	//HR�Ĺ�˾����
	private java.lang.String m_strPK = null;
	//HR�Ĺ�˾����
	private java.lang.String m_strCorpName = null;
	//U8�ж�Ӧ��Sender��
	private java.lang.String m_strSenderNo = null;
	//��ע��Ϣ
	private java.lang.String m_strMemo;
/**
 * ChrCorp_U8Sender ������ע�⡣
 */
public Corp_U8SenderVO() {
	super();
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-7 9:44:23)
 * @return java.lang.String
 */
public java.lang.String getM_strCorpName() {
	return m_strCorpName;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-7 9:45:26)
 * @return java.lang.String
 */
public java.lang.String getM_strMemo() {
	return m_strMemo;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-7 9:43:57)
 * @return java.lang.String
 */
public java.lang.String getM_strPK() {
	return m_strPK;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-7 9:44:51)
 * @return java.lang.String
 */
public java.lang.String getM_strSenderNo() {
	return m_strSenderNo;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-7 9:44:23)
 * @param newM_strCorpName java.lang.String
 */
public void setM_strCorpName(java.lang.String newM_strCorpName) {
	m_strCorpName = newM_strCorpName;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-7 9:45:26)
 * @param newM_strMemo java.lang.String
 */
public void setM_strMemo(java.lang.String newM_strMemo) {
	m_strMemo = newM_strMemo;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-7 9:43:57)
 * @param newM_strPK java.lang.String
 */
public void setM_strPK(java.lang.String newM_strPK) {
	m_strPK = newM_strPK;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-7 9:44:51)
 * @param newM_strSenderNo java.lang.String
 */
public void setM_strSenderNo(java.lang.String newM_strSenderNo) {
	m_strSenderNo = newM_strSenderNo;
}

/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public java.lang.String getEntityName() {
	return "Corp_U8SenderVO";
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
