package nc.vo.hr.alert;

/**
 * 此处插入类型描述。
 * 创建日期：(2004-3-30 22:42:40)
 * @author：zhonghaijing
 */
public class Pk_psnAndDeptmanaVO extends nc.vo.pub.ValueObject {
	private java.lang.String pk_deptmana;
	private java.lang.String pk_psndoc;
	private java.lang.String deptmananame;
/**
 * Pk_psnAndDeptmanaVO 构造子注解。
 */
public Pk_psnAndDeptmanaVO() {
	super();
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-3-31 9:50:25)
 * @return java.lang.String
 */
public java.lang.String getDeptmananame() {
	return deptmananame;
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
 * 创建日期：(2004-3-30 22:46:41)
 * @return java.lang.String
 */
public java.lang.String getPk_deptmana() {
	return pk_deptmana;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-3-30 22:47:38)
 * @return java.lang.String
 */
public java.lang.String getPk_psndoc() {
	return pk_psndoc;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-3-31 9:50:25)
 * @param newDeptmananame java.lang.String
 */
public void setDeptmananame(java.lang.String newDeptmananame) {
	deptmananame = newDeptmananame;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-3-30 22:46:41)
 * @param newPk_deptmana java.lang.String
 */
public void setPk_deptmana(java.lang.String newPk_deptmana) {
	pk_deptmana = newPk_deptmana;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-3-30 22:47:38)
 * @param newPk_psndoc java.lang.String
 */
public void setPk_psndoc(java.lang.String newPk_psndoc) {
	pk_psndoc = newPk_psndoc;
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
