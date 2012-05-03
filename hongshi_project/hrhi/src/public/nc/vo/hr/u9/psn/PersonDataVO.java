package nc.vo.hr.u9.psn;

import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;

import nc.vo.pub.SuperVO;

public class PersonDataVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String pk_corp = null;//������֯����
	
	private String orgcode = null;//U9��֯����
	
	private String pk_psncl = null;//��Ա�������
	
	private String employeecategorycode = null;//��Ա���
	
	private String psncode = null;//Ա�����
	
	private String psnname = null;//Ա������
	
	private String lastname = null;//��
	
	private String firstname = null;//��
	
	private String pk_deptdoc = null;//��������
	
	private String hrdeptcode = null;//HR���ű���
	
	private String deptcode = null;//U9���ű���
	
	private String deptorgcode = null;//��������֯��U9��֯����
	
	private String isOrg = null;//�����Ƿ���U9��֯
	
	private String pk_om_job = null;//��λ����
	
	private String positioncode = null;//��λ����
	
	private String dutyname = null;//ְ������
	
	private String dutycode = null;//ְ�����
	
	private String sex = null;//�Ա�
	
	private String id = null;//�������֤
	
	private String country = null;//����
	
	private XMLGregorianCalendar indutydate = null;//��ְ���ڶ�ӦU9��ʼ��ְʱ��
	
	private XMLGregorianCalendar outdutydate = null;//��ְʱ��
	
	private XMLGregorianCalendar onpostdate = null;//���µ�������
	
	private XMLGregorianCalendar joinworkdate = null;//�μӹ������ڶ�ӦU9��ְʱ��
	
	private String ownerorgcode = null;//ӵ����֯����
	
	private String createorgcode = null;//������֯����
	
	private String workingorgcode = null;//������֯����
	
	private String responsibilitytype = null;//��������
	
	private String superiorpositioncode = null;//�ϼ���λ����
	
	private String superiorworkingcode = null;//�ϼ���λ��֯����
	
	private String businessorgcode = null;//ҵ����֯����
	
	private String superior = null;//�ϼ���λ����

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	public XMLGregorianCalendar getJoinworkdate() {
		return joinworkdate;
	}

	public void setJoinworkdate(XMLGregorianCalendar joinworkdate) {
		this.joinworkdate = joinworkdate;
	}

	public XMLGregorianCalendar getIndutydate() {
		return indutydate;
	}

	public void setIndutydate(XMLGregorianCalendar indutydate) {
		this.indutydate = indutydate;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getPk_psncl() {
		return pk_psncl;
	}

	public void setPk_psncl(String pk_psncl) {
		this.pk_psncl = pk_psncl;
	}

	public String getPsncode() {
		return psncode;
	}

	public void setPsncode(String psncode) {
		this.psncode = psncode;
	}

	public String getPsnname() {
		return psnname;
	}

	public void setPsnname(String psnname) {
		this.psnname = psnname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}

	public String getPk_om_job() {
		return pk_om_job;
	}

	public void setPk_om_job(String pk_om_job) {
		this.pk_om_job = pk_om_job;
	}

	public String getDutyname() {
		return dutyname;
	}

	public void setDutyname(String dutyname) {
		this.dutyname = dutyname;
	}

	public String getDutycode() {
		return dutycode;
	}

	public void setDutycode(String dutycode) {
		this.dutycode = dutycode;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

//	public Calendar getIndutydate() {
//		return indutydate;
//	}
//
//	public void setIndutydate(Calendar indutydate) {
//		this.indutydate = indutydate;
//	}

//	public Calendar getOutdutydate() {
//		return outdutydate;
//	}
//
//	public void setOutdutydate(Calendar outdutydate) {
//		this.outdutydate = outdutydate;
//	}
//
//	public Calendar getOnpostdate() {
//		return onpostdate;
//	}
//
//	public void setOnpostdate(Calendar onpostdate) {
//		this.onpostdate = onpostdate;
//	}

	public String getOwnerorgcode() {
		return ownerorgcode;
	}

	public void setOwnerorgcode(String ownerorgcode) {
		this.ownerorgcode = ownerorgcode;
	}

	public String getCreateorgcode() {
		return createorgcode;
	}

	public void setCreateorgcode(String createorgcode) {
		this.createorgcode = createorgcode;
	}

	public String getWorkingorgcode() {
		return workingorgcode;
	}

	public void setWorkingorgcode(String workingorgcode) {
		this.workingorgcode = workingorgcode;
	}

	public String getResponsibilitytype() {
		return responsibilitytype;
	}

	public void setResponsibilitytype(String responsibilitytype) {
		this.responsibilitytype = responsibilitytype;
	}

	public String getSuperiorpositioncode() {
		return superiorpositioncode;
	}

	public void setSuperiorpositioncode(String superiorpositioncode) {
		this.superiorpositioncode = superiorpositioncode;
	}

	public String getSuperiorworkingcode() {
		return superiorworkingcode;
	}

	public void setSuperiorworkingcode(String superiorworkingcode) {
		this.superiorworkingcode = superiorworkingcode;
	}

	public String getEmployeecategorycode() {
		return employeecategorycode;
	}

	public void setEmployeecategorycode(String employeecategorycode) {
		this.employeecategorycode = employeecategorycode;
	}

	public String getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	public String getPositioncode() {
		return positioncode;
	}

	public void setPositioncode(String positioncode) {
		this.positioncode = positioncode;
	}

	public String getBusinessorgcode() {
		return businessorgcode;
	}

	public void setBusinessorgcode(String businessorgcode) {
		this.businessorgcode = businessorgcode;
	}

	public String getHrdeptcode() {
		return hrdeptcode;
	}

	public void setHrdeptcode(String hrdeptcode) {
		this.hrdeptcode = hrdeptcode;
	}

	public String getDeptorgcode() {
		return deptorgcode;
	}

	public void setDeptorgcode(String deptorgcode) {
		this.deptorgcode = deptorgcode;
	}

	public String getIsOrg() {
		return isOrg;
	}

	public void setIsOrg(String isOrg) {
		this.isOrg = isOrg;
	}

	/*public String getIndutydate() {
		return indutydate;
	}

	public void setIndutydate(String indutydate) {
		this.indutydate = indutydate;
	}

	public String getOutdutydate() {
		return outdutydate;
	}

	public void setOutdutydate(String outdutydate) {
		this.outdutydate = outdutydate;
	}

	public String getOnpostdate() {
		return onpostdate;
	}

	public void setOnpostdate(String onpostdate) {
		this.onpostdate = onpostdate;
	}*/

	public String getSuperior() {
		return superior;
	}

	public void setSuperior(String superior) {
		this.superior = superior;
	}

	public XMLGregorianCalendar getOutdutydate() {
		return outdutydate;
	}

	public void setOutdutydate(XMLGregorianCalendar outdutydate) {
		this.outdutydate = outdutydate;
	}

	public XMLGregorianCalendar getOnpostdate() {
		return onpostdate;
	}

	public void setOnpostdate(XMLGregorianCalendar onpostdate) {
		this.onpostdate = onpostdate;
	}

}
