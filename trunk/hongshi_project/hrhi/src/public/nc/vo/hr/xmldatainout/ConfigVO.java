package nc.vo.hr.xmldatainout;

/**
 * �˴���������������
 * �������ڣ�(2003-3-6 21:54:32)
 * @author��������
 */


 
/**
 * �˴���������������
 * �������ڣ�(2003-3-6 21:54:32)
 * @author��������
 */
public class ConfigVO extends nc.vo.pub.ValueObject {
	private boolean m_WithU8;
	private java.lang.String m_U8SoapAddr;
	private Corp_U8SenderVO[] m_CorpSenderAry;
/**
 * ConfigVO ������ע�⡣
 */
public ConfigVO() {
	super();
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getEntityName() {
	return "EAIConfigVO";
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-7 9:58:52)
 * @return nc.bs.hr.xmldatainout.Corp_U8SenderVO[]
 */
public Corp_U8SenderVO[] getM_CorpSenderAry() {
	return m_CorpSenderAry;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-7 9:31:32)
 * @return int
 */
public String getM_U8SoapAddr() {
	return m_U8SoapAddr;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-7 9:30:22)
 * @return boolean
 */
public boolean getM_WithU8() {
	return m_WithU8;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-7 9:30:22)
 * @return boolean
 */
public boolean isM_WithU8() {
	return m_WithU8;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-7 9:58:52)
 * @param newM_CorpSenderAry nc.bs.hr.xmldatainout.Corp_U8SenderVO[]
 */
public void setM_CorpSenderAry(Corp_U8SenderVO[] newM_CorpSenderAry) {
	m_CorpSenderAry = newM_CorpSenderAry;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-7 9:31:32)
 * @param newM_U8SoapAddr int
 */
public void setM_U8SoapAddr(String newM_U8SoapAddr) {
	m_U8SoapAddr = newM_U8SoapAddr;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-3-7 9:30:22)
 * @param newM_WithU8 boolean
 */
public void setM_WithU8(boolean newM_WithU8) {
	m_WithU8 = newM_WithU8;
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
