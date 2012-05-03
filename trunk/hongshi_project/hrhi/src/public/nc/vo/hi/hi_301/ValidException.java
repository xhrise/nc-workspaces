package nc.vo.hi.hi_301;

/**
 * 有效性检验异常，用来定位和传递错误信息。
 * 创建日期：(2004-5-14 17:33:17)
 * @author：Administrator
 */
public class ValidException extends Exception {
	private String tableCode;
	private int lineNo;
	private java.lang.String fieldCode;
	private int level = 0;//异常级别。
	public static final int LEVEL_SERIOUS = 0;//严重，需要中止
	public static final int LEVEL_DECIDEBYUSER =1;//其次，用用户决定是否继续
	public static final int LEVEL_CANACCEPT =2;//再次，仅提示。
/**
 * ValidationException 构造子注解。
 */
public ValidException() {
	super();
}
/**
 * ValidationException 构造子注解。
 * @param s java.lang.String
 */
public ValidException(String s) {
	super(s);
}

public ValidException(String s,int level) {
	super(s);
	this.level = level;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-6-5 10:50:16)
 * @return java.lang.String
 */
public java.lang.String getFieldCode() {
	return fieldCode;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-14 17:48:19)
 * @return int
 */
public int getLineNo() {
	return lineNo;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-14 17:34:10)
 * @return java.lang.String
 */
public java.lang.String getTableCode() {
	return tableCode;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-6-5 10:50:16)
 * @param newFieldCode java.lang.String
 */
public void setFieldCode(java.lang.String newFieldCode) {
	fieldCode = newFieldCode;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-14 17:48:19)
 * @param newLineNo int
 */
public void setLineNo(int newLineNo) {
	lineNo = newLineNo;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-14 17:34:10)
 * @param newTableCode java.lang.String
 */
public void setTableCode(java.lang.String newTableCode) {
	tableCode = newTableCode;
}
/**
 * @return the level
 */
public int getLevel() {
	return level;
}
/**
 * @param level the level to set
 */
public void setLevel(int level) {
	this.level = level;
}
}
