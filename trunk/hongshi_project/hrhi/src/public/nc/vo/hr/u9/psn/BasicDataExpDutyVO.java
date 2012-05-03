package nc.vo.hr.u9.psn;

import nc.vo.pub.SuperVO;

/**
 * 岗位、职务、人员对照 岗位VO
 * @author fengwei
 *
 */
public class BasicDataExpDutyVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String pk_om_duty = null;
	
	private String dutycode = null;
	
	private String dutyname = null;
	
	private String series = null;
	
	private String vdutysumm = null;
	
	private String pk_corp = null;
	
	private String dutyrank = null;
	
	private String createcorp = null;
	
	private String unitname = null;
	
	private String orgcode = null;
	
	private String doccode = null;
	
	private String docname = null;

	/**
	 * 构造函数
	 */
	public BasicDataExpDutyVO() {
		
	}

	public String getPk_om_duty() {
		return pk_om_duty;
	}

	public void setPk_om_duty(String pk_om_duty) {
		this.pk_om_duty = pk_om_duty;
	}

	public String getDutycode() {
		return dutycode;
	}

	public void setDutycode(String dutycode) {
		this.dutycode = dutycode;
	}

	public String getDutyname() {
		return dutyname;
	}

	public void setDutyname(String dutyname) {
		this.dutyname = dutyname;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getVdutysumm() {
		return vdutysumm;
	}

	public void setVdutysumm(String vdutysumm) {
		this.vdutysumm = vdutysumm;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getDutyrank() {
		return dutyrank;
	}

	public void setDutyrank(String dutyrank) {
		this.dutyrank = dutyrank;
	}

	public String getCreatecorp() {
		return createcorp;
	}

	public void setCreatecorp(String createcorp) {
		this.createcorp = createcorp;
	}

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

	public String getDoccode() {
		return doccode;
	}

	public void setDoccode(String doccode) {
		this.doccode = doccode;
	}

	public String getDocname() {
		return docname;
	}

	public void setDocname(String docname) {
		this.docname = docname;
	}

	@Override
	public String getPKFieldName() {
		
		return "pk_om_duty";
	}

	@Override
	public String getParentPKFieldName() {
		
		return null;
	}

	@Override
	public String getTableName() {
		
		return "om_duty";
	}

}
