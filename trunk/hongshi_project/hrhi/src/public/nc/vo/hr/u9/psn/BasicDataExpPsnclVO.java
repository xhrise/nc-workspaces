package nc.vo.hr.u9.psn;

import nc.vo.pub.SuperVO;

/**
 * 基础数据导出 员工类别VO类
 * @author fengwei
 *
 */
public class BasicDataExpPsnclVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String pk_corp = null;
	
	private String orgcode = null;
	
	private String psnclasscode = null;
	
	private String psnclassname = null;
	
	private String createdate = null;
	
	private String dr = null;
	
	private String memo = null;
	
	private String pk_psncl = null;
	
	private String pk_psncl1 = null;
	
	private String psnclscope = null;
	
	private String sealflag = null;
	
	private String ts = null;
	
	private String unitname = null;

	public BasicDataExpPsnclVO() {
		
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

	public String getPsnclasscode() {
		return psnclasscode;
	}

	public void setPsnclasscode(String psnclasscode) {
		this.psnclasscode = psnclasscode;
	}

	public String getPsnclassname() {
		return psnclassname;
	}

	public void setPsnclassname(String psnclassname) {
		this.psnclassname = psnclassname;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getDr() {
		return dr;
	}

	public void setDr(String dr) {
		this.dr = dr;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPk_psncl() {
		return pk_psncl;
	}

	public void setPk_psncl(String pk_psncl) {
		this.pk_psncl = pk_psncl;
	}

	public String getPk_psncl1() {
		return pk_psncl1;
	}

	public void setPk_psncl1(String pk_psncl1) {
		this.pk_psncl1 = pk_psncl1;
	}

	public String getPsnclscope() {
		return psnclscope;
	}

	public void setPsnclscope(String psnclscope) {
		this.psnclscope = psnclscope;
	}

	public String getSealflag() {
		return sealflag;
	}

	public void setSealflag(String sealflag) {
		this.sealflag = sealflag;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	@Override
	public String getPKFieldName() {
		
		return "pk_psncl";
	}

	@Override
	public String getParentPKFieldName() {
		
		return null;
	}

	@Override
	public String getTableName() {
		
		return "bd_psncl";
	}

}
