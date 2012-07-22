package nc.vo.yto.org;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class OrganizeVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5274174016585943430L;
	
	private String pk_organizational;
	private String proc;
	private String unitcode;
	private String unitname;
	private String parentcode;
	private String type;
	private Integer dr;
	private UFDateTime ts;
	private String description;
//	private String status;
	private	UFBoolean regularchain;
	private UFBoolean used;
	private String orgstatus;
	
	public String getOrgstatus() {
		return orgstatus;
	}

	public void setOrgstatus(String orgstatus) {
		this.orgstatus = orgstatus;
	}

	public UFBoolean getUsed() {
		return used;
	}

	public void setUsed(UFBoolean used) {
		this.used = used;
	}

	public String getPk_organizational() {
		return pk_organizational;
	}

	public void setPk_organizational(String pk_organizational) {
		this.pk_organizational = pk_organizational;
	}

	public String getProc() {
		return proc;
	}

	public void setProc(String proc) {
		this.proc = proc;
	}

	public String getUnitcode() {
		return unitcode;
	}

	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getParentcode() {
		return parentcode;
	}

	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}

	public UFBoolean getRegularchain() {
		return regularchain;
	}

	public void setRegularchain(UFBoolean regularchain) {
		this.regularchain = regularchain;
	}

	@Override
	public String getPKFieldName() {
		return "pk_organizational";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "bd_organizational";
	}
	
	

}
