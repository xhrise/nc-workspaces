package nc.vo.hr.xmldatainout;

/**
 * 此处插入类型描述。
 * 创建日期：(2003-3-9 16:40:00)
 * @author：常晓晖
 */
public class ExportWaPsnVO extends nc.vo.pub.ValueObject {
	//员工编号
	private java.lang.String m_personno = null;
	//部门编号
	private java.lang.String m_departmentno = null;

	//人员类别编号
	private java.lang.String m_persongradeno = null;
	//员工姓名
	private java.lang.String m_personname = null;

	//放置各个Entry项目，例如‘性别’、‘学历’、‘缺勤天数’等
	private java.util.Hashtable m_entryHash = new java.util.Hashtable();
/**
 * ExportWaPsnVO 构造子注解。
 */
public ExportWaPsnVO() {
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
 * 创建日期：(2003-3-9 16:47:23)
 * @return java.lang.String
 */
public java.lang.String getM_departmentno() {
	return m_departmentno;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-10 16:10:35)
 * @return java.util.Hashtable
 */
public java.util.Hashtable getM_entryHash() {
	return m_entryHash;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-9 16:48:40)
 * @return java.lang.String
 */
public java.lang.String getM_persongradeno() {
	return m_persongradeno;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-9 16:49:54)
 * @return java.lang.String
 */
public java.lang.String getM_personname() {
	return m_personname;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-9 16:46:03)
 * @return java.lang.String
 */
public java.lang.String getM_personno() {
	return m_personno;
}


/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-9 16:47:23)
 * @param newDepartmentno java.lang.String
 */
public void setM_departmentno(java.lang.String newDepartmentno) {
	m_departmentno = newDepartmentno;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-10 16:10:35)
 * @param newM_entryHash java.util.Hashtable
 */
public void setM_entryHash(java.util.Hashtable newM_entryHash) {
	m_entryHash.clear();
	m_entryHash.putAll(newM_entryHash);
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-9 16:48:40)
 * @param newM_persongradeno java.lang.String
 */
public void setM_persongradeno(java.lang.String newM_persongradeno) {
	m_persongradeno = newM_persongradeno;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-9 16:49:54)
 * @param newM_personname java.lang.String
 */
public void setM_personname(java.lang.String newM_personname) {
	m_personname = newM_personname;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-9 16:46:03)
 * @param newM_personno java.lang.String
 */
public void setM_personno(java.lang.String newM_personno) {
	m_personno = newM_personno;
}


/**
 * 验证对象各属性之间的数据逻辑正确性。
 * 
 * 创建日期：(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
 *     ValidationException，对错误进行解释。
 */
public void validate() throws nc.vo.pub.ValidationException {}

	//U8中对应的Sender号
	private java.lang.String m_strSenderNo = null;

/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-11 16:29:37)
 * @return java.lang.String
 */
public java.lang.String getM_strSenderNo() {
	return m_strSenderNo;
}

/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-11 16:29:37)
 * @param newM_strSenderNo java.lang.String
 */
public void setM_strSenderNo(java.lang.String newM_strSenderNo) {
	m_strSenderNo = newM_strSenderNo;
}
}
