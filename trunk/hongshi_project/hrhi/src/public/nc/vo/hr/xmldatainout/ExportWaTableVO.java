package nc.vo.hr.xmldatainout;

/**
 * 此处插入类型描述。
 * 创建日期：(2003-3-11 15:48:59)
 * @author：常晓晖
 */
public class ExportWaTableVO extends nc.vo.pub.ValueObject {
	//员工主键
	private java.lang.String m_pk_psn = null;
	//员工编号
	private java.lang.String m_personno = null;
	//员工姓名
	private java.lang.String m_personname = null;
	//部门名称
	private java.lang.String m_departmentname = null;
	//部门主键
	private java.lang.String m_pk_dept = null;
	//员工类别主键
	private java.lang.String m_pk_psngrade = null;
/**
 * ExportWaAllVO 构造子注解。
 */
public ExportWaTableVO() {
	super();
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
 * 创建日期：(2003-3-12 17:05:54)
 * @return java.lang.String
 */
public java.lang.String getM_departmentname() {
	return m_departmentname;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-12 17:03:11)
 * @return java.lang.String
 */
public java.lang.String getM_personname() {
	return m_personname;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-12 17:03:11)
 * @return java.lang.String
 */
public java.lang.String getM_personno() {
	return m_personno;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-12 18:13:23)
 * @return java.lang.String
 */
public java.lang.String getM_pk_dept() {
	return m_pk_dept;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-12 19:07:28)
 * @return java.lang.String
 */
public java.lang.String getM_pk_psn() {
	return m_pk_psn;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-12 18:13:23)
 * @return java.lang.String
 */
public java.lang.String getM_pk_psngrade() {
	return m_pk_psngrade;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-12 17:05:54)
 * @param newM_departmentname java.lang.String
 */
public void setM_departmentname(java.lang.String newM_departmentname) {
	m_departmentname = newM_departmentname;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-12 17:03:11)
 * @param newM_personname java.lang.String
 */
public void setM_personname(java.lang.String newM_personname) {
	m_personname = newM_personname;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-12 17:03:11)
 * @param newM_personno java.lang.String
 */
public void setM_personno(java.lang.String newM_personno) {
	m_personno = newM_personno;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-12 18:13:23)
 * @param newM_pk_dept java.lang.String
 */
public void setM_pk_dept(java.lang.String newM_pk_dept) {
	m_pk_dept = newM_pk_dept;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-12 19:07:28)
 * @param newM_pk_psn java.lang.String
 */
public void setM_pk_psn(java.lang.String newM_pk_psn) {
	m_pk_psn = newM_pk_psn;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-12 18:13:23)
 * @param newM_pk_psngrade java.lang.String
 */
public void setM_pk_psngrade(java.lang.String newM_pk_psngrade) {
	m_pk_psngrade = newM_pk_psngrade;
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
