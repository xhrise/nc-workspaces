package nc.vo.hr.u9.psn;

import nc.vo.pub.SuperVO;

/**
 * 基本数据导出 人员信息VO
 * @author Owner
 *
 */
public class BasicDataExpPsnbasdoc extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String joinsysdate = null;
	
	private String psnname = null;
	
	private String id = null;
	
	private String email = null;
	
	private String sex = null;
	
	private String pk_corp = null;
	
	private String unitname = null;
	
	private String orgcode = null;
	
	private String joinworkdate = null;
	
	private String addr = null;
	
	private String country = null;

	/**
	 * 构造函数
	 */
	public BasicDataExpPsnbasdoc() {
		
	}

	public String getJoinsysdate() {
		return joinsysdate;
	}

	public void setJoinsysdate(String joinsysdate) {
		this.joinsysdate = joinsysdate;
	}

	public String getPsnname() {
		return psnname;
	}

	public void setPsnname(String psnname) {
		this.psnname = psnname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
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

	public String getJoinworkdate() {
		return joinworkdate;
	}

	public void setJoinworkdate(String joinworkdate) {
		this.joinworkdate = joinworkdate;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String getPKFieldName() {
		
		return "pk_psnbasdoc";
	}

	@Override
	public String getParentPKFieldName() {
		
		return null;
	}

	@Override
	public String getTableName() {
		
		return "bd_psnbasdoc";
	}

}
