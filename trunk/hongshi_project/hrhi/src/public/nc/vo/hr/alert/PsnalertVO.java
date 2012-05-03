package nc.vo.hr.alert;

/**
 * 此处插入类型描述。
 * 创建日期：(2002-6-5 11:40:57)
 * @author：Administrator
 */
public class PsnalertVO extends nc.vo.pub.ValueObject {
	private java.lang.String pk;
	private java.lang.String name;//人员
	private java.lang.String value;//预警内容
	//
	private String deptname;//部门
	private String jobname;//岗位
	private String psncode;
	private String[] others;//其他内容
	private java.lang.String pk_deptdoc;
	private java.lang.String pk_om_job;
/**
 * PsnalertVO 构造子注解。
 */
public PsnalertVO() {
	super();
}
/**
 * 此处插入方法描述。
 * 创建日期：(2002-6-5 12:37:31)
 * @return java.lang.String
 */
public java.lang.String getDeptname() {
	return deptname;
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getEntityName() {
	return null;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2002-6-5 12:37:31)
 * @return java.lang.String
 */
public java.lang.String getJobname() {
	return jobname;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2002-6-5 12:38:38)
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2002-6-5 12:37:31)
 * @return java.lang.String[]
 */
public java.lang.String[] getOthers() {
	return others;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2002-6-5 11:41:20)
 * @return java.lang.String
 */
public java.lang.String getPk() {
	return pk;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-3-30 15:09:31)
 * @return java.lang.String
 */
public java.lang.String getPk_deptdoc() {
	return pk_deptdoc;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-3-30 15:17:20)
 * @return java.lang.String
 */
public java.lang.String getPk_om_job() {
	return pk_om_job;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2002-6-5 12:38:38)
 * @return java.lang.String
 */
public java.lang.String getPsncode() {
	return psncode;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2002-6-5 12:37:31)
 * @return java.lang.String
 */
public java.lang.String getValue() {
	return value;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2002-6-5 12:38:38)
 * @param newDeptname java.lang.String
 */
public void setDeptname(java.lang.String newDeptname) {
	deptname = newDeptname;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2002-6-5 12:38:38)
 * @param newJobname java.lang.String
 */
public void setJobname(java.lang.String newJobname) {
	jobname = newJobname;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2002-6-5 12:38:38)
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2002-6-5 12:38:38)
 * @param newOthers java.lang.String[]
 */
public void setOthers(java.lang.String[] newOthers) {
	others = newOthers;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2002-6-5 11:41:20)
 * @param newPk java.lang.String
 */
public void setPk(java.lang.String newPk) {
	pk = newPk;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-3-30 15:09:31)
 * @param newPk_deptdoc java.lang.String
 */
public void setPk_deptdoc(java.lang.String newPk_deptdoc) {
	pk_deptdoc = newPk_deptdoc;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-3-30 15:17:20)
 * @param newPk_om_job java.lang.String
 */
public void setPk_om_job(java.lang.String newPk_om_job) {
	pk_om_job = newPk_om_job;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2002-6-5 12:38:38)
 * @param newDeptname java.lang.String
 */
public void setPsncode(java.lang.String newPsncode) {
	psncode = newPsncode;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2002-6-5 12:37:31)
 * @param newValue java.lang.String
 */
public void setValue(java.lang.String newValue) {
	value = newValue;
}
/**
 * 验证对象各属性之间的数据逻辑正确性。
 * 
 * 创建日期：(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
 *     ValidationException，对错误进行解释。
 */
public void validate() throws nc.vo.pub.ValidationException {}
}
