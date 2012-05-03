package nc.vo.hi.hi_401;

/**
 * �˴���������˵����
 * �������ڣ�(2002-5-23 19:43:52)
 * @author���ﺣ��
 */
public class PostPrintDataVo extends nc.vo.pub.CircularlyAccessibleValueObject {


	//ְ��
	public String m_dutycode;
	public String m_dutyname;
	public String m_dutyseries;//��������
	public String m_dutyrank;//�ȼ�����
	//��λ
	public String m_jobcode;
	public String m_jobname;
	public String m_postdept;//������������
	public String m_supierior;//ֱ���ϼ�����


	
/**
 * PostPrintDataVo ������ע�⡣
 */
public PostPrintDataVo() {
	super();
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:26:03)
 * @return java.lang.String[]
 */
public java.lang.String[] getAttributeNames() {
	return null;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public Object getAttributeValue(String attributeName) {
   
	if (attributeName == null)
		return null;
	else {
		if (attributeName.equals("dutycode")) {
			return m_dutycode;
		} else
			if (attributeName.equals("dutyname")) {
				return m_dutyname;
			} else
				if (attributeName.equals("dutyseries")) {
					return m_dutyseries;
				} else
					if (attributeName.equals("dutyrank")) {
						return m_dutyrank;
					} else
						if (attributeName.equals("jobcode")) {
							return m_jobcode;
						} else
							if (attributeName.equals("jobname")) {
								return m_jobname;
							} else
								if (attributeName.equals("postdept")) {
									return m_postdept;
								} else
									if (attributeName.equals("supierior")) {
										return m_supierior;
									} else
										return null;
	}

}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public String getAttributeValueDes(String attributeName) {

	if(getAttributeValue(attributeName)==null)return null;
	else return getAttributeValue(attributeName).toString();
	
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getDutycode() {
	return m_dutycode;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getDutyname() {
	return m_dutyname;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getDutyrank() {
	return m_dutyrank;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getDutyseries() {
	return m_dutyseries;
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
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getJobcode() {
	return m_jobcode;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getJobname() {
	return m_jobname;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getPostdept() {
	return m_postdept;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getSupierior() {
	return m_supierior;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public void setAttributeValue(String name, Object value) {}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public void setDutycode(String code) {
	m_dutycode = code;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public void setDutyname(String name) {
	m_dutyname = name;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public void setDutyrank(String rank) {
	m_dutyrank = rank;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public void setDutyseries(String series) {
	m_dutyseries = series;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public void setJobcode(String code) {
	m_jobcode = code;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public void setJobname(String name) {
	m_jobname = name;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public void setPostdept(String dept) {
	m_postdept = dept;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public void setSuperior(String superior) {
	m_supierior = superior;
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
