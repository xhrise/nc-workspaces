package nc.vo.hi.hi_301;

/**
 * ��Ч�Լ����쳣��������λ�ʹ��ݴ�����Ϣ��
 * �������ڣ�(2004-5-14 17:33:17)
 * @author��Administrator
 */
public class ValidException extends Exception {
	private String tableCode;
	private int lineNo;
	private java.lang.String fieldCode;
	private int level = 0;//�쳣����
	public static final int LEVEL_SERIOUS = 0;//���أ���Ҫ��ֹ
	public static final int LEVEL_DECIDEBYUSER =1;//��Σ����û������Ƿ����
	public static final int LEVEL_CANACCEPT =2;//�ٴΣ�����ʾ��
/**
 * ValidationException ������ע�⡣
 */
public ValidException() {
	super();
}
/**
 * ValidationException ������ע�⡣
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
 * �˴����뷽��������
 * �������ڣ�(2004-6-5 10:50:16)
 * @return java.lang.String
 */
public java.lang.String getFieldCode() {
	return fieldCode;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-14 17:48:19)
 * @return int
 */
public int getLineNo() {
	return lineNo;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-14 17:34:10)
 * @return java.lang.String
 */
public java.lang.String getTableCode() {
	return tableCode;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-6-5 10:50:16)
 * @param newFieldCode java.lang.String
 */
public void setFieldCode(java.lang.String newFieldCode) {
	fieldCode = newFieldCode;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-14 17:48:19)
 * @param newLineNo int
 */
public void setLineNo(int newLineNo) {
	lineNo = newLineNo;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-14 17:34:10)
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
