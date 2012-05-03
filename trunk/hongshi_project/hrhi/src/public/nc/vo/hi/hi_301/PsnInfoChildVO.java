package nc.vo.hi.hi_301;

import java.util.Vector;

/**
 * 子信息集VO。
 * 创建日期：(2004-4-21 10:25:34)
 * @author：Administrator
 */
public class PsnInfoChildVO extends nc.vo.pub.CircularlyAccessibleValueObject {
	private java.lang.String tableCode = null;
/**
 * PsnInfoChildVO 构造子注解。
 */
public PsnInfoChildVO() {
	super();
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:26:03)
 * @return java.lang.String[]
 */
public java.lang.String[] getAttributeNames() {
	String[] names = new String[fldCode.size()];
	fldCode.copyInto(names);
	return names;
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public Object getAttributeValue(String attributeName) {
	int index = fldCode.indexOf(attributeName);
	if(index>=0){
		return values.elementAt(index);
	}else{
		return null;
	}
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getEntityName() {
	return tableName;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-21 10:29:30)
 * @return java.lang.String
 */
public java.lang.String getTableCode() {
	return tableCode;
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public void setAttributeValue(String name, Object value) {
	int index = fldCode.indexOf(name);
	if(index>=0){
		values.setElementAt(value,index);
	}else{
		fldCode.addElement(name);
		values.addElement(value);
	}
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-21 10:29:30)
 * @param newTableCode java.lang.String
 */
public void setTableCode(java.lang.String newTableCode) {
	tableCode = newTableCode;
}
/**
 * 验证对象各属性之间的数据逻辑正确性。
 * 
 * 创建日期：(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
 *     ValidationException，对错误进行解释。
 */
public void validate() throws nc.vo.pub.ValidationException {}

	private final static int ADD_FLAG = 0;
	private Vector fldCode = new Vector();
	private java.lang.String fPk;
	private java.lang.String pk;
	private java.lang.String tableName = null;
	private final static int UPDATE_FLAG = 1;
	private int updateFlag = ADD_FLAG;
	private Vector values = new Vector();

/**
 * 此处插入方法说明。
 * 创建日期：(2004-4-30 10:35:05)
 * @return int
 */
public int getFlag() {
	return updateFlag;
}

/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-21 11:38:16)
 * @return java.lang.String
 */
public java.lang.String getFPk() {
	return fPk;
}

/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-21 11:37:46)
 * @return java.lang.String
 */
public java.lang.String getPk() {
	return pk;
}

/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-21 10:55:11)
 * @return java.lang.String
 */
public java.lang.String getTableName() {
	return tableName;
}

/**
 * 此处插入方法说明。
 * 创建日期：(2004-4-30 10:35:05)
 * @param newFlag int
 */
public void setFlag(int newFlag) {
	updateFlag = newFlag;
}

/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-21 11:38:16)
 * @param newFPk java.lang.String
 */
public void setFPk(java.lang.String newFPk) {
	fPk = newFPk;
}

/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-21 11:37:46)
 * @param newPk java.lang.String
 */
public void setPk(java.lang.String newPk) {
	pk = newPk;
}

/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-21 10:55:11)
 * @param newTableName java.lang.String
 */
public void setTableName(java.lang.String newTableName) {
	tableName = newTableName;
}
}
