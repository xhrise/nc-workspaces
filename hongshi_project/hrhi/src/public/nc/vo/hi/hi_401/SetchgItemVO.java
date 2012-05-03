/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product.                              *
\***************************************************************/

package nc.vo.hi.hi_401;

import java.util.ArrayList;
import nc.vo.pub.*;
import nc.vo.pub.lang.*;

/**
 * 此处插入类型说明。
 * 
 * 创建日期：(2002-3-20)
 * @author：tianhb
 */
public class SetchgItemVO extends CircularlyAccessibleValueObject {

	public String m_pk_fldofchgbill;
	public String m_pk_setchg;
	public String m_pk_flddict;
	public String m_fldcode;
	public String m_fldname;
	public Integer m_editpos;

	/**
	 *     描述上面属性的FieldObjects。主要用于系统工具中，
	 * 业务代码中不会用到下面的FieldObjects。
	 */
	private static StringField m_pk_fldofchgbillField;
	private static StringField m_pk_setchgField;
	private static StringField m_pk_flddictField;
	private static StringField m_fldcodeField;
	private static StringField m_fldnameField;
	private static IntegerField m_editposField;
/**
 * 使用主键字段进行初始化的构造子。
 *
 * 创建日期：(2002-3-20)
 */
public SetchgItemVO() {

}
/**
 * 使用主键进行初始化的构造子。
 *
 * 创建日期：(2002-3-20)
 * @param ??fieldNameForMethod?? 主键值
 */
public SetchgItemVO(String newPk_fldofchgbill) {

	// 为主键字段赋值:
	m_pk_fldofchgbill = newPk_fldofchgbill;
}
/**
 * 根类Object的方法,克隆这个VO对象。
 *
 * 创建日期：(2002-3-20)
 */
public Object clone() {

	// 复制基类内容并创建新的VO对象：
	Object o = null;
	try {
		o = super.clone();
	} catch (Exception e) {}
	SetchgItemVO fldofchgbill = (SetchgItemVO)o;

	// 你在下面复制本VO对象的所有属性：

	return fldofchgbill;
}
/**
 * 返回数值对象的显示名称。
 *
 * 创建日期：(2002-3-20)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getEntityName() {

	return "Fldofchgbill";
}
/**
 * 返回对象标识，用来唯一定位对象。
 *
 * 创建日期：(2002-3-20)
 * @return String
 */
public String getPrimaryKey() {

	return m_pk_fldofchgbill;
}
/**
 * 设置对象标识，用来唯一定位对象。
 *
 * 创建日期：(2002-3-20)
 * @param m_pk_fldofchgbill String 
 */
public void setPrimaryKey(String newPk_fldofchgbill) {

	m_pk_fldofchgbill = newPk_fldofchgbill;
}
/**
 * 属性m_pk_fldofchgbill的Getter方法。
 *
 * 创建日期：(2002-3-20)
 * @return String
 */
public String getPk_fldofchgbill() {
	return m_pk_fldofchgbill;
}
/**
 * 属性m_pk_setchg的Getter方法。
 *
 * 创建日期：(2002-3-20)
 * @return String
 */
public String getPk_setchg() {
	return m_pk_setchg;
}
/**
 * 属性m_pk_flddict的Getter方法。
 *
 * 创建日期：(2002-3-20)
 * @return String
 */
public String getPk_flddict() {
	return m_pk_flddict;
}
/**
 * 属性m_fldcode的Getter方法。
 *
 * 创建日期：(2002-3-20)
 * @return String
 */
public String getFldcode() {
	return m_fldcode;
}
/**
 * 属性m_fldname的Getter方法。
 *
 * 创建日期：(2002-3-20)
 * @return String
 */
public String getFldname() {
	return m_fldname;
}
/**
 * 属性m_editpos的Getter方法。
 *
 * 创建日期：(2002-3-20)
 * @return Integer
 */
public Integer getEditpos() {
	return m_editpos;
}
/**
 * 属性m_pk_fldofchgbill的setter方法。
 *
 * 创建日期：(2002-3-20)
 * @param newM_pk_fldofchgbill String
 */
public void setPk_fldofchgbill(String newPk_fldofchgbill) {

	m_pk_fldofchgbill = newPk_fldofchgbill;
}
/**
 * 属性m_pk_setchg的setter方法。
 *
 * 创建日期：(2002-3-20)
 * @param newM_pk_setchg String
 */
public void setPk_setchg(String newPk_setchg) {

	m_pk_setchg = newPk_setchg;
}
/**
 * 属性m_pk_flddict的setter方法。
 *
 * 创建日期：(2002-3-20)
 * @param newM_pk_flddict String
 */
public void setPk_flddict(String newPk_flddict) {

	m_pk_flddict = newPk_flddict;
}
/**
 * 属性m_fldcode的setter方法。
 *
 * 创建日期：(2002-3-20)
 * @param newM_fldcode String
 */
public void setFldcode(String newFldcode) {

	m_fldcode = newFldcode;
}

/**
 * 属性m_editpos的setter方法。
 *
 * 创建日期：(2002-3-20)
 * @param newM_editpos Integer
 */
public void setEditpos(Integer newEditpos) {

	m_editpos = newEditpos;
}
/**
 * 验证对象各属性之间的数据逻辑正确性。
 *
 * 创建日期：(2002-3-20)
 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
 *     ValidationException，对错误进行解释。
 */
public void validate() throws ValidationException {

	ArrayList errFields = new ArrayList(); // errFields record those null fields that cannot be null.
	// 检查是否为不允许空的字段赋了空值，你可能需要修改下面的提示信息：
	if (m_pk_fldofchgbill == null) {
		errFields.add(new String("m_pk_fldofchgbill"));
	}
	// construct the exception message:
	StringBuffer message = new StringBuffer();
	message.append("下列字段不能为空：");
	if (errFields.size() > 0) {
		String[] temp = (String[]) errFields.toArray(new String[0]);
		message.append(temp[0]);
		for ( int i= 1; i < temp.length; i++ ) {
			message.append("、");
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

	return new String[]{ "pk_setchg", "pk_flddict", "fldcode", "fldname", "editpos" };
}
/**
 *  <p>根据一个属性名称字符串该属性的值。
 *  <p>
 * 创建日期：(2002-3-20)
 * @param key java.lang.String
 */
public Object getAttributeValue(String attributeName) {

	if (attributeName.equals("pk_fldofchgbill")) {
		return m_pk_fldofchgbill;
	}
	else if (attributeName.equals("pk_setchg")) {
		return m_pk_setchg;
	}
	else if (attributeName.equals("pk_flddict")) {
		return m_pk_flddict;
	}
	else if (attributeName.equals("fldcode")) {
		return m_fldcode;
	}
	else if (attributeName.equals("fldname")) {
		return m_fldname;
	}
	else if (attributeName.equals("editpos")) {
		return m_editpos;
	}
	return null;
}
/**
 *  <p>对参数name对型的属性设置值。
 *  <p>
 * 创建日期：(2002-3-20)
 * @param key java.lang.String
 */
public void setAttributeValue(String name, Object value) {

	try {
		if (name.equals("pk_fldofchgbill")) {
			m_pk_fldofchgbill = (String) value;
		}
		else if (name.equals("pk_setchg")) {
			m_pk_setchg = (String) value;
		}
		else if (name.equals("pk_flddict")) {
			m_pk_flddict = (String) value;
		}
		else if (name.equals("fldcode")) {
			m_fldcode = (String) value;
		}
		else if (name.equals("fldname")) {
			m_fldname = (String) value;
		}
		else if (name.equals("editpos")) {
			m_editpos = (Integer) value;
		}
	}
	catch (ClassCastException e) {
		throw new ClassCastException("setAttributeValue方法中为 " + name + " 赋值时类型转换错误！（值：" + value + "）");
	}
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2002-3-20)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getPk_fldofchgbillField() {

	if(m_pk_fldofchgbillField == null){
		try{
			m_pk_fldofchgbillField = new StringField();
			// 属性的名称
			m_pk_fldofchgbillField.setName("pk_fldofchgbill");
			// 属性的描述
			m_pk_fldofchgbillField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_pk_fldofchgbillField;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2002-3-20)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getPk_setchgField() {

	if(m_pk_setchgField == null){
		try{
			m_pk_setchgField = new StringField();
			// 属性的名称
			m_pk_setchgField.setName("pk_setchg");
			// 属性的描述
			m_pk_setchgField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_pk_setchgField;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2002-3-20)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getPk_flddictField() {

	if(m_pk_flddictField == null){
		try{
			m_pk_flddictField = new StringField();
			// 属性的名称
			m_pk_flddictField.setName("pk_flddict");
			// 属性的描述
			m_pk_flddictField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_pk_flddictField;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2002-3-20)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getFldcodeField() {

	if(m_fldcodeField == null){
		try{
			m_fldcodeField = new StringField();
			// 属性的名称
			m_fldcodeField.setName("fldcode");
			// 属性的描述
			m_fldcodeField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_fldcodeField;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2002-3-20)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getFldnameField() {

	if(m_fldnameField == null){
		try{
			m_fldnameField = new StringField();
			// 属性的名称
			m_fldnameField.setName("fldname");
			// 属性的描述
			m_fldnameField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_fldnameField;
}
/**
 * FieldObject的Getter方法。
 *
 * 创建日期：(2002-3-20)
 * @return nc.vo.pub.FieldObject
 */
public static IntegerField getEditposField() {

	if(m_editposField == null){
		try{
			m_editposField = new IntegerField();
			// 属性的名称
			m_editposField.setName("editpos");
			// 属性的描述
			m_editposField.setLabel("null");
			// 请添加对本属性的期它描述：

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_editposField;
}
/**
 * 返回这个ValueObject类的所有FieldObject对象的集合。
 *
 * 创建日期：(2002-3-20)
 * @return nc.vo.pub.FieldObject[]
 */
public FieldObject[] getFields() {

	FieldObject[] fields = {getPk_fldofchgbillField(), getPk_setchgField(), getPk_flddictField(), getFldcodeField(), getFldnameField(), getEditposField()};

	return fields;
}

/**
 * 属性m_fldname的setter方法。
 *
 * 创建日期：(2002-3-20)
 * @param newM_fldname String
 */
public void setFldname( Object value) {

	if(value!=null)
	m_fldname = value.toString();
}
}