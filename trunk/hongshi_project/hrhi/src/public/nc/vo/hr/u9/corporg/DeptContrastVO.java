package nc.vo.hr.u9.corporg;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * ���Ŷ���VO
 * @author fengwei
 *
 */
public class DeptContrastVO extends SuperVO {

	/**
	 * ϵͳĬ�ϵ�UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String pk_contrast = null;//��˾��������
	
	private String pk_corp = null;//HR��˾
	
	private String pk_deptdoc = null;//HR����
	
	private String orgcode = null;//��֯����
	
	private String deptcode = null;//U9���ű���
	
	private String unitname = null;//HR��˾����
	
	private String deptname = null;//HR��������
	
	private UFBoolean isorg = new UFBoolean('y');//�Ƿ���֯

	/**
	 * ���캯��
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
	 * ���ر������
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
	 * ���ر������
	 */
	@Override
	public String getTableName() {
		
		return "dept_contrast";
	}

}
