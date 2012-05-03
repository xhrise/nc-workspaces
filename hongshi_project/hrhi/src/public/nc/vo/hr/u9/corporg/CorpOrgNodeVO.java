package nc.vo.hr.u9.corporg;

import nc.vo.pub.SuperVO;

/**
 * 公司部门节点VO
 * @author fengwei
 *
 */
public class CorpOrgNodeVO extends SuperVO {

	/**
	 * 系统默认的UID
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int TYPE_ROOT = -1;

	public static final int TYPE_CORP = 0;

	public static final int TYPE_DEPT = 1;

	/** 公司主键 */
	private String primaryKey = null;

	/** 编码 */
	private String code = null;

	/** 名字 */
	private String name = null;

	private boolean controlled = true;

	private String fatherCorp = null;

	private String pkFather = null;

	private String corpOrder = null;

	private int nodeType = -1;

	private String innerCode = null;

	private String corpPK = null;
	
	//-------------------------
	private String orgcode = null;
	
	private String deptcode = null;
	//-------------------------

	/**
	 * 构造函数
	 */
	public CorpOrgNodeVO() {
		super();
	}

	@Override
	public String getPKFieldName() {
		
		return "primaryKey";
	}

	@Override
	public String getParentPKFieldName() {
		
		return "fathercorp";
	}

	@Override
	public String getTableName() {
		
		return null;
	}

	/**
	 * 覆盖#toString()用于树型菜单的显示
	 */
	@Override
	public String toString() {
		
		return code + " " + name;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public void setPK(String pk){
		this.primaryKey = pk;
	}
	
	public String getPk(){
		return primaryKey;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isControlled() {
		return controlled;
	}

	public void setControlled(boolean controlled) {
		this.controlled = controlled;
	}

	public String getFatherCorp() {
		return fatherCorp;
	}

	public void setFatherCorp(String fatherCorp) {
		this.fatherCorp = fatherCorp;
	}

	public String getPkFather() {
		return pkFather;
	}

	public void setPkFather(String pkFather) {
		this.pkFather = pkFather;
	}

	public String getCorpOrder() {
		return corpOrder;
	}

	public void setCorpOrder(String corpOrder) {
		this.corpOrder = corpOrder;
	}

	public int getNodeType() {
		return nodeType;
	}

	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}

	public String getInnerCode() {
		return innerCode;
	}

	public void setInnerCode(String innerCode) {
		this.innerCode = innerCode;
	}

	public String getCorpPK() {
		return corpPK;
	}

	public void setCorpPK(String corpPK) {
		this.corpPK = corpPK;
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

}
