package nc.vo.hr.u9.psn;

import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;

import nc.vo.pub.SuperVO;

public class PersonDataVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String pk_corp = null;//人事组织编码
	
	private String orgcode = null;//U9组织编码
	
	private String pk_psncl = null;//人员类别主键
	
	private String employeecategorycode = null;//人员类别
	
	private String psncode = null;//员工编号
	
	private String psnname = null;//员工姓名
	
	private String lastname = null;//姓
	
	private String firstname = null;//名
	
	private String pk_deptdoc = null;//部门主键
	
	private String hrdeptcode = null;//HR部门编码
	
	private String deptcode = null;//U9部门编码
	
	private String deptorgcode = null;//部门是组织的U9组织编码
	
	private String isOrg = null;//部门是否是U9组织
	
	private String pk_om_job = null;//岗位主键
	
	private String positioncode = null;//岗位编码
	
	private String dutyname = null;//职务名称
	
	private String dutycode = null;//职务编码
	
	private String sex = null;//性别
	
	private String id = null;//居民身份证
	
	private String country = null;//国家
	
	private XMLGregorianCalendar indutydate = null;//到职日期对应U9开始任职时间
	
	private XMLGregorianCalendar outdutydate = null;//离职时间
	
	private XMLGregorianCalendar onpostdate = null;//最新到岗日期
	
	private XMLGregorianCalendar joinworkdate = null;//参加工作日期对应U9入职时间
	
	private String ownerorgcode = null;//拥有组织编码
	
	private String createorgcode = null;//创建组织编码
	
	private String workingorgcode = null;//工作组织编码
	
	private String responsibilitytype = null;//责任类型
	
	private String superiorpositioncode = null;//上级岗位编码
	
	private String superiorworkingcode = null;//上级岗位组织编码
	
	private String businessorgcode = null;//业务组织编码
	
	private String superior = null;//上级岗位主键

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
