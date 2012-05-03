package nc.vo.hi.hi_304;

import nc.vo.pub.report.QryOrderVO;
/**
 * 报表条件VO
 * 
 * 创建日期：(2002-4-26 11:24:34)
 * @author：zhonghaijing
 */
public class ReportConditionVO extends nc.vo.pub.ValueObject {
	String[] selectFields = null;
	String[] sumFields = null;
	String[] formulaeFields = null;
	int[] dataTypes = null;
	QryOrderVO[] groupFieldVOs = null;
	QryOrderVO[] orderFieldVOs = null;
	//起止时间设定
	String startDate = null;
	String endDate = null;
	//
/**
 * ReportConditionVO 构造子注解。
 */
public ReportConditionVO() {
	super();
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-28 12:55:59)
 * @return int[]
 */
public int[] getDataTypes() {
	return dataTypes;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-16 18:49:59)
 * @return java.lang.String
 */
public java.lang.String getEndDate() {
	return endDate;
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
 * 此处插入方法说明。
 * 创建日期：(2002-4-26 11:34:49)
 * @return java.lang.String[]
 */
public String[] getFormulaeFields() {
	return formulaeFields;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-26 11:36:32)
 * @return nc.vo.pub.report.QryOrderVO[]
 */
public QryOrderVO[] getGroupFieldVOs() {
	return groupFieldVOs;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-26 11:36:32)
 * @return nc.vo.pub.report.QryOrderVO[]
 */
public QryOrderVO[] getOrderFieldVOs() {
	return orderFieldVOs;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-26 11:34:49)
 * @return java.lang.String[]
 */
public String[] getSelectFields() {
	return selectFields;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-16 18:49:59)
 * @return java.lang.String
 */
public java.lang.String getStartDate() {
	return startDate;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-26 11:34:49)
 * @return java.lang.String[]
 */
public String[] getSumFields() {
	return sumFields;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-28 12:56:29)
 * @param newDataTypes int[]
 */
public void setDataTypes(int[] newDataTypes) {
	dataTypes = newDataTypes;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-16 18:49:59)
 * @param newEndDate java.lang.String
 */
public void setEndDate(java.lang.String newEndDate) {
	endDate = newEndDate;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-26 11:52:57)
 * @param newSelectFields java.lang.String
 */
public void setFormulaeFields(String[] newFormulaeFields) {
	formulaeFields = newFormulaeFields;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-26 12:34:39)
 * @param groupFieldVOs nc.vo.pub.report.QryOrderVO[]
 */
public void setGroupFieldVOs(QryOrderVO[] newGroupFieldVOs) {
	groupFieldVOs = newGroupFieldVOs;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-26 12:34:39)
 * @param groupFieldVOs nc.vo.pub.report.QryOrderVO[]
 */
public void setOrderFieldVOs(QryOrderVO[] newOrderFieldVOs) {
	orderFieldVOs = newOrderFieldVOs;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-26 11:52:57)
 * @param newSelectFields java.lang.String
 */
public void setSelectFields(String[] newSelectFields) {
	selectFields = newSelectFields;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-16 18:49:59)
 * @param newStartDate java.lang.String
 */
public void setStartDate(java.lang.String newStartDate) {
	startDate = newStartDate;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-26 11:52:57)
 * @param newSelectFields java.lang.String
 */
public void setSumFields(String[] newSumFields) {
	sumFields = newSumFields;
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
