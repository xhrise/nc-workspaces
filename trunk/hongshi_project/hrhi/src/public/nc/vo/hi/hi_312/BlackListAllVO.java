package nc.vo.hi.hi_312;

import nc.vo.pub.*;
/**
 * 此处插入类型说明。
 * 创建日期：(2003-9-6 12:26:49)
 * @author：王飞
 */
public class BlackListAllVO extends nc.vo.pub.CircularlyAccessibleValueObject {
	private PsndocBadHeaderVO header = null;
	private PsndocBadItemVO[] items = null;

	// 时间戳标示，现在暂未使用：
	long currentTimestamp; // 当前时间戳
	long initialTimestamp; // 从数据库读入时获得的时间戳
/**
 * RoleAllVO 构造子注解。
 */
public BlackListAllVO() {
	super();
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:26:03)
 * @return java.lang.String[]
 */
public java.lang.String[] getAttributeNames() {
	return null;
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public Object getAttributeValue(String attributeName) {
	return null;
}
/**
 * <p>获得子表的VO数组。
 * <p>
 * 创建日期：(2003-8-26)
 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
 */
public CircularlyAccessibleValueObject[] getChildrenVO() {

	return items;
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
 * <p>获得母表的VO。
 * <p>
 * 创建日期：(2003-8-26)
 * @return nc.vo.pub.CircularlyAccessibleValueObject
 */
public CircularlyAccessibleValueObject getParentVO() {

	return header;
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public void setAttributeValue(String name, Object value) {}
/**
 * <p>设置子表的VO数组。
 * <p>
 * 创建日期：(2003-8-26)
 * @param children nc.vo.pub.CircularlyAccessibleValueObject[]
 */
public void setChildrenVO(CircularlyAccessibleValueObject[] children) {

	items = (PsndocBadItemVO[]) children;
}
/**
 * <p>设置母表的VO。
 * <p>
 * 创建日期：(2003-8-26)
 * @param parent nc.vo.pub.CircularlyAccessibleValueObject
 */
public void setParentVO(CircularlyAccessibleValueObject parent) {

	header = (PsndocBadHeaderVO) parent;
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
