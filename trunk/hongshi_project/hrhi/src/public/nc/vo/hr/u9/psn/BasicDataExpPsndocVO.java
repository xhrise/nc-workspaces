package nc.vo.hr.u9.psn;

import nc.vo.pub.SuperVO;

/**
 * 基本数据导出 人员管理档案VO
 * @author Owner
 *
 */
public class BasicDataExpPsndocVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String pk_corp = null;
	
	private String indutydate = null;
	
	private String clerkcode = null;
	
	private String pk_om_job = null;
	
	private String pk_deptdoc = null;
	
	private String dutyname = null;
	
	private String outdutydate = null;
	
	private String orgcode = null;
	
	private String deptcode = null;
	
	private String pk_psncl = null;
	
	private String psnname = null;
	
	private String psncode = null;
	
	private String pk_psnbasdoc = null;
	
	private String pk_psndoc = null;
	
	private String busiorgcode = null;

	public BasicDataExpPsndocVO() {
		
	}

	public String getPsncode() {
		return psncode;
	}

	public void setPsncode(String psncode) {
		this.psncode = psncode;
	}

	public String getPk_psnbasdoc() {
		return pk_psnbasdoc;
	}

	public void setPk_psnbasdoc(String pk_psnbasdoc) {
		this.pk_psnbasdoc = pk_psnbasdoc;
	}

	public String getPk_psndoc() {
		return pk_psndoc;
	}

	public void setPk_psndoc(String pk_psndoc) {
		this.pk_psndoc = pk_psndoc;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getIndutydate() {
		return indutydate;
	}

	public void setIndutydate(String indutydate) {
		this.indutydate = indutydate;
	}

	public String getClerkcode() {
		return clerkcode;
	}

	public void setClerkcode(String clerkcode) {
		this.clerkcode = clerkcode;
	}

	public String getPk_om_job() {
		return pk_om_job;
	}

	public void setPk_om_job(String pk_om_job) {
		this.pk_om_job = pk_om_job;
	}

	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}

	public String getDutyname() {
		return dutyname;
	}

	public void setDutyname(String dutyname) {
		this.dutyname = dutyname;
	}

	public String getOutdutydate() {
		return outdutydate;
	}

	public void setOutdutydate(String outdutydate) {
		this.outdutydate = outdutydate;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	public String getPk_psncl() {
		return pk_psncl;
	}

	public void setPk_psncl(String pk_psncl) {
		this.pk_psncl = pk_psncl;
	}

	@Override
	public String getPKFieldName() {
		
		return "pk_psndoc";
	}

	public String getPsnname() {
		return psnname;
	}

	public void setPsnname(String psnname) {
		this.psnname = psnname;
	}

//	public UFDate getIndutydate() {
//		return indutydate;
//	}
//
//	public void setIndutydate(UFDate indutydate) {
//		this.indutydate = indutydate;
//	}
//
//	public UFDate getOutdutydate() {
//		return outdutydate;
//	}
//
//	public void setOutdutydate(UFDate outdutydate) {
//		this.outdutydate = outdutydate;
//	}

	public String getBusiorgcode() {
		return busiorgcode;
	}

	public void setBusiorgcode(String busiorgcode) {
		this.busiorgcode = busiorgcode;
	}

	@Override
	public String getParentPKFieldName() {
		
		return null;
	}

	@Override
	public String getTableName() {
		
		return "bd_psndoc";
	}

}
