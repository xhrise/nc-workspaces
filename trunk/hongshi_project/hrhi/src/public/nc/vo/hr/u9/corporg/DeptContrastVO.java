package nc.vo.hr.u9.corporg;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * 部门对照VO
 * @author fengwei
 *
 */
public class DeptContrastVO extends SuperVO {

	/**
	 * 系统默认的UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String pk_contrast = null;//公司对照主键
	
	private String pk_corp = null;//HR公司
	
	private String pk_deptdoc = null;//HR部门
	
	private String orgcode = null;//组织编码
	
	private String deptcode = null;//U9部门编码
	
	private String unitname = null;//HR公司名称
	
	private String deptname = null;//HR部门名称
	
	private UFBoolean isorg = new UFBoolean('y');//是否组织

	/**
	 * 构造函数
	 */
	public DeptContrastVO() {
		
	}

	public String getPk_contrast() {
		return pk_contrast;
	}

	public void setPk_contrast(String pk_contrast) {
		this.pk_contrast = pk_contrast;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
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

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public UFBoolean getIsorg() {
		return isorg;
	}

	public void setIsorg(UFBoolean isorg) {
		this.isorg = isorg;
	}

	/**
	 * 返回表的主键
	 */
	@Override
	public String getPKFieldName() {
		
		return "pk_contrast";
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
		
		return "dept_contrast";
	}

}
