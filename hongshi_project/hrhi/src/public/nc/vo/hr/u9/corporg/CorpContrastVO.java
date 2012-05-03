package nc.vo.hr.u9.corporg;

import nc.vo.pub.SuperVO;

/**
 * ��˾����VO
 * @author fengwei
 *
 */
public class CorpContrastVO extends SuperVO {

	/**
	 * ϵͳĬ�ϵ�UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String pk_contrast = null;//��˾��������
	
	private String pk_corp = null;//HR��˾
	
	private String orgcode = null;//��֯����
	
	private String unitname = null;//��˾����

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
		
		return "corp_contrast";
	}

}
