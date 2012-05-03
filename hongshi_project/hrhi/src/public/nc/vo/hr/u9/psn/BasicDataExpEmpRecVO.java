package nc.vo.hr.u9.psn;

import nc.vo.pub.SuperVO;

/**
 * 基础数据导出 任职记录VO类
 * @author fengwei
 *
 */
public class BasicDataExpEmpRecVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String pk_corp = null;
	
	private String pk_psndoc = null;
	
	private String begindate = null;
	
	private String pk_deptdoc = null;
	
	private String pk_postdoc = null;
	
	private String deptname = null;
	
	private String deptcode = null;
	
	private String createcorp = null;
	
	private String orgcode = null;
	
	private String psnname = null;
	
	private String jobname = null;
	
	private String busiorgcode = null;

	public BasicDataExpEmpRecVO() {
		
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getPsnname() {
		return psnname;
	}

	public void setPsnname(String psnname) {
		this.psnname = psnname;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_psndoc() {
		return pk_psndoc;
	}

	public void setPk_psndoc(String pk_psndoc) {
		this.pk_psndoc = pk_psndoc;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}

	public String getPk_postdoc() {
		return pk_postdoc;
	}

	public void setPk_postdoc(String pk_postdoc) {
		this.pk_postdoc = pk_postdoc;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	public String getCreatecorp() {
		return createcorp;
	}

	public void setCreatecorp(String createcorp) {
		this.createcorp = createcorp;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getBusiorgcode() {
		return busiorgcode;
	}

	public void setBusiorgcode(String busiorgcode) {
		this.busiorgcode = busiorgcode;
	}

	@Override
	public String getPKFieldName() {
		
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		
		return null;
	}

	@Override
	public String getTableName() {
		
		return "hi_psndoc_deptchg";
	}

}
