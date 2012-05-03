package nc.vo.hi.hi_303;

import nc.vo.hi.pub.CommonValue;
import nc.vo.hr.tools.pub.StringUtils;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * 人员简明信息VO
 * @author wangxing
 *
 */
public class PsnLiteInfoVO extends SuperVO {

	/**
	 * 默认序列化ID
	 */
	private static final long serialVersionUID = 1L;

	// 人员管理主键
	private String pk_psndoc = null;
	// 人员基本主键
	private String pk_psnbasdoc = null;
	// 人员对应的任职表主键
	private String pk_psndoc_sub = null;
	// 姓名
	private String psnname = null;
	// 编码
	private String psncode = null;
	// 身份证号码
	private String id = null;
	// 是否进入了人员档案
	private UFBoolean indocflag = UFBoolean.TRUE;
	// 任职公司主键
	private String pk_corp = null;
	// 人员公司主键
	private String pk_corp_man = null;
	// 所属公司主键
	private String pk_corp_belong = null;
	// 岗位主键
	private String pk_om_job = null;
	// 任职类型，默认在职
	private Integer jobtype = Integer.valueOf(0);
	// 人员类别
	private String pk_psncl = null;
	// 人员归属类别，默认在职
	private Integer psnclscope = Integer.valueOf(CommonValue.PSNCLSCOPE_WORK);
	// 人员部门
	private String pk_deptdoc = null;
	
	// 显示用字段
	
	// 岗位相关
	private String jobname = null;
	private String jobcode = null;
	
	// 部门相关
	private String deptname = null;
	private String deptcode = null;
	
	// 公司相关
	private String corpname = null;
	private String corpcode = null;

	/**
	 * @return the corpcode
	 */
	public String getCorpcode() {
		return corpcode;
	}

	/**
	 * @param corpcode the corpcode to set
	 */
	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}

	/**
	 * 构造函数
	 */
	public PsnLiteInfoVO() {
		super();
	}

	/**
	 * 主键字段
	 */
	public String getPKFieldName() {
		return "bd_psndoc";
	}

	/**
	 * 父主键字段
	 */
	public String getParentPKFieldName() {
		return null;
	}

	/**
	 * 表名
	 */
	public String getTableName() {
		return "bd_psndoc";
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
	 * @return the pk_psndoc_sub
	 */
	public String getPk_psndoc_sub() {
		return pk_psndoc_sub;
	}

	/**
	 * @param pk_psndoc_sub the pk_psndoc_sub to set
	 */
	public void setPk_psndoc_sub(String pk_psndoc_sub) {
		this.pk_psndoc_sub = pk_psndoc_sub;
	}

	/**
	 * @return the psnname
	 */
	public String getPsnname() {
		return psnname;
	}

	/**
	 * @param psnname the psnname to set
	 */
	public void setPsnname(String psnname) {
		this.psnname = psnname;
	}

	/**
	 * @return the psncode
	 */
	public String getPsncode() {
		return psncode;
	}

	/**
	 * @param psncode the psncode to set
	 */
	public void setPsncode(String psncode) {
		this.psncode = psncode;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the indocflag
	 */
	public UFBoolean getIndocflag() {
		return indocflag;
	}

	/**
	 * @param indocflag the indocflag to set
	 */
	public void setIndocflag(UFBoolean indocflag) {
		this.indocflag = indocflag;
	}

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

	/**
	 * @return the pk_om_job
	 */
	public String getPk_om_job() {
		return pk_om_job;
	}

	/**
	 * @param pk_om_job the pk_om_job to set
	 */
	public void setPk_om_job(String pk_om_job) {
		this.pk_om_job = pk_om_job;
	}

	/**
	 * @return the jobtype
	 */
	public Integer getJobtype() {
		return jobtype;
	}

	/**
	 * @param jobtype the jobtype to set
	 */
	public void setJobtype(Integer jobtype) {
		this.jobtype = jobtype;
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
	 * @return the psnclscope
	 */
	public Integer getPsnclscope() {
		return psnclscope;
	}

	/**
	 * @param psnclscope the psnclscope to set
	 */
	public void setPsnclscope(Integer psnclscope) {
		this.psnclscope = psnclscope;
	}

	/**
	 * @return the jobname
	 */
	public String getJobname() {
		return jobname;
	}

	/**
	 * @param jobname the jobname to set
	 */
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	/**
	 * @return the jobcode
	 */
	public String getJobcode() {
		return jobcode;
	}

	/**
	 * @param jobcode the jobcode to set
	 */
	public void setJobcode(String jobcode) {
		this.jobcode = jobcode;
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
	 * @return the deptname
	 */
	public String getDeptname() {
		return deptname;
	}

	/**
	 * @param deptname the deptname to set
	 */
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	/**
	 * @return the deptcode
	 */
	public String getDeptcode() {
		return deptcode;
	}

	/**
	 * @param deptcode the deptcode to set
	 */
	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	/**
	 * @return the pk_corp_man
	 */
	public String getPk_corp_man() {
		return pk_corp_man;
	}

	/**
	 * @param pk_corp_man the pk_corp_man to set
	 */
	public void setPk_corp_man(String pk_corp_man) {
		this.pk_corp_man = pk_corp_man;
	}

	/**
	 * @return the pk_corp_belong
	 */
	public String getPk_corp_belong() {
		return pk_corp_belong;
	}

	/**
	 * @param pk_corp_belong the pk_corp_belong to set
	 */
	public void setPk_corp_belong(String pk_corp_belong) {
		this.pk_corp_belong = pk_corp_belong;
	}

	/**
	 * @return the corpname
	 */
	public String getCorpname() {
		return corpname;
	}

	/**
	 * @param corpname the corpname to set
	 */
	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}
	
	/**
	 * 
	 */
	public String toString() {
		if(StringUtils.hasText(psncode) && StringUtils.hasText(psnname))
			return psncode+" "+psnname;
		else
			return super.toString();
	}

	
}
