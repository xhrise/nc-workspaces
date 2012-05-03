/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product.                              *
\***************************************************************/

package nc.vo.hi.hi_301;

import java.util.ArrayList;

import nc.vo.hr.utils.ml.HRPubRes;
import nc.vo.pub.*;
import nc.vo.pub.lang.*;

/**
 * 此处插入类型说明。
 * 
 * 创建日期：(2004-6-5)
 * @author：
 */
public class PsndocFlagVO extends CircularlyAccessibleValueObject {

	public String m_pk_psndoc;
	public Integer m_showorder;
	public UFBoolean m_regular;

	/**
	 *     描述上面属性的FieldObjects。主要用于系统工具中，
	 * 业务代码中不会用到下面的FieldObjects。
	 */
	private static StringField m_pk_psndocField;
	private static IntegerField m_showorderField;
	private static UFBooleanField m_regularField;
/**
 * 使用主键字段进行初始化的构造子。
 *
 * 创建日期：(2004-6-5)
 */
public PsndocFlagVO() {

}
/**
 * 使用主键进行初始化的构造子。
 *
 * 创建日期：(2004-6-5)
 * @param ??fieldNameForMethod?? 主键值
 */
public PsndocFlagVO(String newPk_psndoc) {

	// 为主键字段赋值:
	m_pk_psndoc = newPk_psndoc;
}
/**
 * 根类Object的方法,克隆这个VO对象。
 *
 * 创建日期：(2004-6-5)
 */
public Object clone() {

	// 复制基类内容并创建新的VO对象：
	Object o = null;
	try {
		o = super.clone();
	} catch (Exception e) {}
	PsndocFlagVO psndocFlag = (PsndocFlagVO)o;

	// 你在下面复制本VO对象的所有属性：

	return psndocFlag;
}
/**
 * 返回数值对象的显示名称。
 *
 * 创建日期：(2004-6-5)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getEntityName() {

	return "PsndocFlag";
}
/**
 * 返回对象标识，用来唯一定位对象。
 *
 * 创建日期：(2004-6-5)
 * @return String
 */
public String getPrimaryKey() {

	return m_pk_psndoc;
}
/**
 * 设置对象标识，用来唯一定位对象。
 *
 * 创建日期：(2004-6-5)
 * @param m_pk_psndoc String 
 */
public void setPrimaryKey(String newPk_psndoc) {

	m_pk_psndoc = newPk_psndoc;
}
/**
 * 属性m_pk_psndoc的Getter方法。
 *
 * 创建日期：(2004-6-5)
 * @return String
 */
public String getPk_psndoc() {
	return m_pk_psndoc;
}
/**
 * 属性m_showorder的Getter方法。
 *
 * 创建日期：(2004-6-5)
 * @return Integer
 */
public Integer getShoworder() {
	return m_showorder;
}
/**
 * 属性m_regular的Getter方法。
 *
 * 创建日期：(2004-6-5)
 * @return UFBoolean
 */
public UFBoolean getRegular() {
	return m_regular;
}
/**
 * 属性m_pk_psndoc的setter方法。
 *
 * 创建日期：(2004-6-5)
 * @param newM_pk_psndoc String
 */
public void setPk_psndoc(String newPk_psndoc) {

	m_pk_psndoc = newPk_psndoc;
}
/**
 * 属性m_showorder的setter方法。
 *
 * 创建日期：(2004-6-5)
 * @param newM_showorder Integer
 */
public void setShoworder(Integer newShoworder) {

	m_showorder = newShoworder;
}
/**
 * 属性m_regular的setter方法。
 *
 * 创建日期：(2004-6-5)
 * @param newM_regular UFBoolean
 */
public void setRegular(UFBoolean newRegular) {

	m_regular = newRegular;
}
/**
 * 验证对象各属性之间的数据逻辑正确性。
 *
 * 创建日期：(2004-6-5)
 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
 *     ValidationException，对错误进行解释。
 */
public void validate() throws ValidationException {

	ArrayList errFields = new ArrayList(); // errFields record those null fields that cannot be null.
	// 检查是否为不允许空的字段赋了空值，你可能需要修改下面的提示信息：
	if (m_pk_psndoc == null) {
		errFields.add(new String("m_pk_psndoc"));
	}
	// construct the exception message:
	StringBuffer message = new StringBuffer();
	message.append(nc.vo.bd.IBDMsg.MSG_NULL_FIELD);//message.append("下列字段不能为空：");
	if (errFields.size() > 0) {
		String[] temp = (String[]) errFields.toArray(new String[0]);
		message.append(temp[0]);
		for ( int i= 1; i < temp.length; i++ ) {
			message.append(",");//message.append("、");
			message.append(temp[i]);
		}
		// throw the exception:
		throw new NullFieldException(message.toString());
	}
}
/**
 * <p>需要在一个循环中访问的属性的名称数组。
 * <p>
 * 创建日期：(??Date??)
 * @return java.lang.String[]
 */
public java.lang.String[] getAttributeNames() {

	return new String[]{ "showorder", "regular" };
}
/**
 *  <p>根据一个属性名称字符串该属性的值。
 *  <p>
 * 创建日期：(2004-6-5)
 * @param key java.lang.String
 */
public Object getAttributeValue(String attributeName) {

	if (attributeName.equals("pk_psndoc")) {
		return m_pk_psndoc;
	}
	else if (attributeName.equals("showorder")) {
		return m_showorder;
	}
	else if (attributeName.equals("regular")) {
		return m_regular;
	}
	return null;
}
/**
 *  <p>对参数name对型的属性设置值。
 *  <p>
 * 创建日期：(2004-6-5)
 * @param key java.lang.String
 */
public void setAttributeValue(String name, Object value) {

	try {
		if (name.equals("pk_psndoc")) {
			m_pk_psndoc = (String) value;
		}
		else if (name.equals("showorder")) {
			m_showorder = (Integer) value;
		}
		else if (name.equals("regular")) {
			m_regular = (UFBoolean) value;
		}
	}
	catch (ClassCastException e) {
		throw new ClassCastException(HRPubRes.getClassCastErrMsg(name,value));//throw new ClassCastException("setAttributeValue方法中为 " + name + " 赋值时类型转换错误！（值：" + value + "）");
	}
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2004-6-5)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getPk_psndocField() {

	if(m_pk_psndocField == null){
		try{
			m_pk_psndocField = new StringField();
			// 属性的名称
			m_pk_psndocField.setName("pk_psndoc");
			// 属性的描述
			m_pk_psndocField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_pk_psndocField;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2004-6-5)
 * @return nc.vo.pub.FieldObject
 */
public static IntegerField getShoworderField() {

	if(m_showorderField == null){
		try{
			m_showorderField = new IntegerField();
			// 属性的名称
			m_showorderField.setName("showorder");
			// 属性的描述
			m_showorderField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_showorderField;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2004-6-5)
 * @return nc.vo.pub.FieldObject
 */
public static UFBooleanField getRegularField() {

	if(m_regularField == null){
		try{
			m_regularField = new UFBooleanField();
			// 属性的名称
			m_regularField.setName("regular");
			// 属性的描述
			m_regularField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_regularField;
}
/**
 * 返回这个ValueObject类的所有FieldObject对象的集合。
 *
 * 创建日期：(2004-6-5)
 * @return nc.vo.pub.FieldObject[]
 */
public FieldObject[] getFields() {

	FieldObject[] fields = {getPk_psndocField(), getShoworderField(), getRegularField()};

	return fields;
}
}