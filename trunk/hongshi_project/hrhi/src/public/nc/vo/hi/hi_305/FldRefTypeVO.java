package nc.vo.hi.hi_305;

/**
 * 人事的参照VO
 * 
 * 创建日期：(2002-5-10 16:23:03)
 * @author：zhonghaijing
 */
public class FldRefTypeVO extends nc.vo.pub.ValueObject {
	public String strTableCode; //表名
	public String strFieldCode; //字段名
	public String strRefTypePK; //参照类型主键
/**
 * FldRefTypeVO 构造子注解。
 */
public FldRefTypeVO() {
	super();
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getEntityName() {
	return "FldRefType";
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-5-10 16:27:08)
 * @return java.lang.String
 */
public String getFeildCode() {
	return strFieldCode;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-5-10 16:27:08)
 * @return java.lang.String
 */
public String getRefTypePK() {
	return strRefTypePK;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-5-10 16:27:08)
 * @return java.lang.String
 */
public String getTableCode() {
	return strTableCode;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-5-10 16:31:31)
 * @param newTableCode java.lang.String
 */
public void setFieldCode(String newFieldCode) {
	strFieldCode = newFieldCode;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-5-10 16:31:31)
 * @param newTableCode java.lang.String
 */
public void setRefTypePK(String newRefTypePK) {
	strRefTypePK = newRefTypePK;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-5-10 16:31:31)
 * @param newTableCode java.lang.String
 */
public void setTableCode(String newTableCode) {
	strTableCode = newTableCode;
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
