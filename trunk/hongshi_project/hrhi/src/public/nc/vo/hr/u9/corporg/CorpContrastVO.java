package nc.vo.hr.u9.corporg;

import nc.vo.pub.SuperVO;

/**
 * 公司对照VO
 * @author fengwei
 *
 */
public class CorpContrastVO extends SuperVO {

	/**
	 * 系统默认的UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String pk_contrast = null;//公司对照主键
	
	private String pk_corp = null;//HR公司
	
	private String orgcode = null;//组织编码
	
	private String unitname = null;//公司名称

	public CorpContrastVO() {
		
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

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
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
		
		return "corp_contrast";
	}

}
