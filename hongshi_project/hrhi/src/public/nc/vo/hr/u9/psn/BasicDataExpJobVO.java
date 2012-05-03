package nc.vo.hr.u9.psn;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * 岗位、职务、人员对照 职务VO
 * @author fengwei
 *
 */
public class BasicDataExpJobVO extends SuperVO {

	/**
	 * 系统默认的UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String pk_om_job = null;
	
	private String jobcode = null;
	
	private String jobname = null;
	
	private String pk_deptdoc = null;
	
	private String pk_om_duty = null;
	
	private String suporior = null;
	
	private String pk_corp = null;
	
	private String jobseries = null;
	
	private String jobrank = null;
	
	private String unitname = null;
	
	private String deptname = null;
	
	private String orgcode = null;
	
	private String deptcode = null;
	
	private String builddate = null;
	
	private String abortdate = null;
	
	private String worksumm = null;
	
	private String createcorp = null;
	
	private String bespvised = null;
	
	private String tospvise = null;
	
	private String incontact = null;
	
	private String outcontact = null;
	
	private String horisitu = null;
	
	private String pk_jobdoc = null;
	
	private String junior = null;
	
	private String filepath = null;
	
	private String superiorbusiorg = null;
	
	private String supriororg = null;
	
	private UFBoolean isdeptrespon = new UFBoolean('Y');
	
	private UFBoolean isAbort = new UFBoolean('y');
	
	private String isorg = null;
	
	private String superiorcode = null;
	
	private String hrorgcode = null;

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
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

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getPk_om_job() {
		return pk_om_job;
	}

	public void setPk_om_job(String pk_om_job) {
		this.pk_om_job = pk_om_job;
	}

	public String getJobcode() {
		return jobcode;
	}

	public void setJobcode(String jobcode) {
		this.jobcode = jobcode;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}

	public String getPk_om_duty() {
		return pk_om_duty;
	}

	public void setPk_om_duty(String pk_om_duty) {
		this.pk_om_duty = pk_om_duty;
	}

	public String getSuporior() {
		return suporior;
	}

	public void setSuporior(String suporior) {
		this.suporior = suporior;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getJobseries() {
		return jobseries;
	}

	public void setJobseries(String jobseries) {
		this.jobseries = jobseries;
	}

	public String getJobrank() {
		return jobrank;
	}

	public void setJobrank(String jobrank) {
		this.jobrank = jobrank;
	}

	public String getBuilddate() {
		return builddate;
	}

	public void setBuilddate(String builddate) {
		this.builddate = builddate;
	}

	public String getAbortdate() {
		return abortdate;
	}

	public void setAbortdate(String abortdate) {
		this.abortdate = abortdate;
	}

	public String getWorksumm() {
		return worksumm;
	}

	public void setWorksumm(String worksumm) {
		this.worksumm = worksumm;
	}

	public String getCreatecorp() {
		return createcorp;
	}

	public void setCreatecorp(String createcorp) {
		this.createcorp = createcorp;
	}

	public String getBespvised() {
		return bespvised;
	}

	public void setBespvised(String bespvised) {
		this.bespvised = bespvised;
	}

	public String getTospvise() {
		return tospvise;
	}

	public void setTospvise(String tospvise) {
		this.tospvise = tospvise;
	}

	public String getIncontact() {
		return incontact;
	}

	public void setIncontact(String incontact) {
		this.incontact = incontact;
	}

	public String getOutcontact() {
		return outcontact;
	}

	public void setOutcontact(String outcontact) {
		this.outcontact = outcontact;
	}

	public String getHorisitu() {
		return horisitu;
	}

	public void setHorisitu(String horisitu) {
		this.horisitu = horisitu;
	}

	public String getPk_jobdoc() {
		return pk_jobdoc;
	}

	public void setPk_jobdoc(String pk_jobdoc) {
		this.pk_jobdoc = pk_jobdoc;
	}

	public String getJunior() {
		return junior;
	}

	public void setJunior(String junior) {
		this.junior = junior;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public UFBoolean getIsdeptrespon() {
		return isdeptrespon;
	}

	public void setIsdeptrespon(UFBoolean isdeptrespon) {
		this.isdeptrespon = isdeptrespon;
	}

	public UFBoolean getIsAbort() {
		return isAbort;
	}

	public void setIsAbort(UFBoolean isAbort) {
		this.isAbort = isAbort;
	}

	public String getSuperiorbusiorg() {
		return superiorbusiorg;
	}

	public void setSuperiorbusiorg(String superiorbusiorg) {
		this.superiorbusiorg = superiorbusiorg;
	}

	public String getSupriororg() {
		return supriororg;
	}

	public void setSupriororg(String supriororg) {
		this.supriororg = supriororg;
	}

	public String getIsorg() {
		return isorg;
	}

	public void setIsorg(String isorg) {
		this.isorg = isorg;
	}

	public String getSuperiorcode() {
		return superiorcode;
	}

	public void setSuperiorcode(String superiorcode) {
		this.superiorcode = superiorcode;
	}

	public String getHrorgcode() {
		return hrorgcode;
	}

	public void setHrorgcode(String hrorgcode) {
		this.hrorgcode = hrorgcode;
	}

	/**
	 * 构造函数
	 */
	public BasicDataExpJobVO() {
		
	}

	/**
	 * 返回表的主键
	 */
	@Override
	public String getPKFieldName() {
		
		return "pk_om_job";
	}

	@Override
	public String getParentPKFieldName() {
		
		return null;
	}

	/**
	 * 返回表的名字
	 */
	@Override
	public String getTableName() {
		
		return "om_job";
	}

}
