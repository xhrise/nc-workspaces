package nc.vo.hr.alert;

/**
 * �˴���������������
 * �������ڣ�(2002-6-5 11:40:57)
 * @author��Administrator
 */
public class PsnalertVO extends nc.vo.pub.ValueObject {
	private java.lang.String pk;
	private java.lang.String name;//��Ա
	private java.lang.String value;//Ԥ������
	//
	private String deptname;//����
	private String jobname;//��λ
	private String psncode;
	private String[] others;//��������
	private java.lang.String pk_deptdoc;
	private java.lang.String pk_om_job;
/**
 * PsnalertVO ������ע�⡣
 */
public PsnalertVO() {
	super();
}
/**
 * �˴����뷽��������
 * �������ڣ�(2002-6-5 12:37:31)
 * @return java.lang.String
 */
public java.lang.String getDeptname() {
	return deptname;
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
 * �������ڣ�(2002-6-5 12:37:31)
 * @return java.lang.String
 */
public java.lang.String getJobname() {
	return jobname;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2002-6-5 12:38:38)
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2002-6-5 12:37:31)
 * @return java.lang.String[]
 */
public java.lang.String[] getOthers() {
	return others;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2002-6-5 11:41:20)
 * @return java.lang.String
 */
public java.lang.String getPk() {
	return pk;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-3-30 15:09:31)
 * @return java.lang.String
 */
public java.lang.String getPk_deptdoc() {
	return pk_deptdoc;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-3-30 15:17:20)
 * @return java.lang.String
 */
public java.lang.String getPk_om_job() {
	return pk_om_job;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2002-6-5 12:38:38)
 * @return java.lang.String
 */
public java.lang.String getPsncode() {
	return psncode;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2002-6-5 12:37:31)
 * @return java.lang.String
 */
public java.lang.String getValue() {
	return value;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2002-6-5 12:38:38)
 * @param newDeptname java.lang.String
 */
public void setDeptname(java.lang.String newDeptname) {
	deptname = newDeptname;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2002-6-5 12:38:38)
 * @param newJobname java.lang.String
 */
public void setJobname(java.lang.String newJobname) {
	jobname = newJobname;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2002-6-5 12:38:38)
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2002-6-5 12:38:38)
 * @param newOthers java.lang.String[]
 */
public void setOthers(java.lang.String[] newOthers) {
	others = newOthers;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2002-6-5 11:41:20)
 * @param newPk java.lang.String
 */
public void setPk(java.lang.String newPk) {
	pk = newPk;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-3-30 15:09:31)
 * @param newPk_deptdoc java.lang.String
 */
public void setPk_deptdoc(java.lang.String newPk_deptdoc) {
	pk_deptdoc = newPk_deptdoc;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-3-30 15:17:20)
 * @param newPk_om_job java.lang.String
 */
public void setPk_om_job(java.lang.String newPk_om_job) {
	pk_om_job = newPk_om_job;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2002-6-5 12:38:38)
 * @param newDeptname java.lang.String
 */
public void setPsncode(java.lang.String newPsncode) {
	psncode = newPsncode;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2002-6-5 12:37:31)
 * @param newValue java.lang.String
 */
public void setValue(java.lang.String newValue) {
	value = newValue;
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
