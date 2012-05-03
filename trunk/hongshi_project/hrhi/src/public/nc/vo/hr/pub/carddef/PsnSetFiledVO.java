package nc.vo.hr.pub.carddef;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;

/**
 * 查询人员主集bd_psndoc和任职表中重复的字段VO
 * @SuppressWarnings("serial")
@author wangxing
 *
 */
public class PsnSetFiledVO extends SuperVO {
	
	public String pk_psncl = null;
	public String pk_deptdoc = null;
	public String pk_postdoc = null;
	public String pk_om_duty = null;
	public String pk_jobserial = null;
	public String pk_jobrank = null;
	public String pk_detytype = null;
	public String pk_dutyrank = null;

	// 加入人员信息里的两个主键
	public String pk_psndoc = null;
	public String pk_psnbasdoc = null;
	public String pk_corp = null;
	
	/**
	 * @return the pk_corp
	 */
	public String getPk_corp() {
		return pk_corp;
	}

	/**
	 * @param pk_corp the pk_corp to set
	 */
	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public PsnSetFiledVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public String getEntityName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}
	
	/**
	 * @return the pk_deptdoc
	 */
	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	/**
	 * @param pk_deptdoc the pk_deptdoc to set
	 */
	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}

	/**
	 * @return the pk_detytype
	 */
	public String getPk_detytype() {
		return pk_detytype;
	}

	/**
	 * @param pk_detytype the pk_detytype to set
	 */
	public void setPk_detytype(String pk_detytype) {
		this.pk_detytype = pk_detytype;
	}

	/**
	 * @return the pk_dutyrank
	 */
	public String getPk_dutyrank() {
		return pk_dutyrank;
	}

	/**
	 * @param pk_dutyrank the pk_dutyrank to set
	 */
	public void setPk_dutyrank(String pk_dutyrank) {
		this.pk_dutyrank = pk_dutyrank;
	}

	/**
	 * @return the pk_jobrank
	 */
	public String getPk_jobrank() {
		return pk_jobrank;
	}

	/**
	 * @param pk_jobrank the pk_jobrank to set
	 */
	public void setPk_jobrank(String pk_jobrank) {
		this.pk_jobrank = pk_jobrank;
	}

	/**
	 * @return the pk_jobserial
	 */
	public String getPk_jobserial() {
		return pk_jobserial;
	}

	/**
	 * @param pk_jobserial the pk_jobserial to set
	 */
	public void setPk_jobserial(String pk_jobserial) {
		this.pk_jobserial = pk_jobserial;
	}

	/**
	 * @return the pk_om_duty
	 */
	public String getPk_om_duty() {
		return pk_om_duty;
	}

	/**
	 * @param pk_om_duty the pk_om_duty to set
	 */
	public void setPk_om_duty(String pk_om_duty) {
		this.pk_om_duty = pk_om_duty;
	}

	/**
	 * @return the pk_postdoc
	 */
	public String getPk_postdoc() {
		return pk_postdoc;
	}

	/**
	 * @param pk_postdoc the pk_postdoc to set
	 */
	public void setPk_postdoc(String pk_postdoc) {
		this.pk_postdoc = pk_postdoc;
	}

	/**
	 * @return the pk_psncl
	 */
	public String getPk_psncl() {
		return pk_psncl;
	}

	/**
	 * @param pk_psncl the pk_psncl to set
	 */
	public void setPk_psncl(String pk_psncl) {
		this.pk_psncl = pk_psncl;
	}

	/**
	 * 
	 */
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the pk_psnbasdoc
	 */
	public String getPk_psnbasdoc() {
		return pk_psnbasdoc;
	}

	/**
	 * @param pk_psnbasdoc the pk_psnbasdoc to set
	 */
	public void setPk_psnbasdoc(String pk_psnbasdoc) {
		this.pk_psnbasdoc = pk_psnbasdoc;
	}

	/**
	 * @return the pk_psndoc
	 */
	public String getPk_psndoc() {
		return pk_psndoc;
	}

	/**
	 * @param pk_psndoc the pk_psndoc to set
	 */
	public void setPk_psndoc(String pk_psndoc) {
		this.pk_psndoc = pk_psndoc;
	}

}
