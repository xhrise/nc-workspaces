package nc.vo.hi.hi_304;

import nc.vo.pub.report.QryOrderVO;
/**
 * ��������VO
 * 
 * �������ڣ�(2002-4-26 11:24:34)
 * @author��zhonghaijing
 */
public class ReportConditionVO extends nc.vo.pub.ValueObject {
	String[] selectFields = null;
	String[] sumFields = null;
	String[] formulaeFields = null;
	int[] dataTypes = null;
	QryOrderVO[] groupFieldVOs = null;
	QryOrderVO[] orderFieldVOs = null;
	//��ֹʱ���趨
	String startDate = null;
	String endDate = null;
	//
/**
 * ReportConditionVO ������ע�⡣
 */
public ReportConditionVO() {
	super();
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-28 12:55:59)
 * @return int[]
 */
public int[] getDataTypes() {
	return dataTypes;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-4-16 18:49:59)
 * @return java.lang.String
 */
public java.lang.String getEndDate() {
	return endDate;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getEntityName() {
	return null;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-26 11:34:49)
 * @return java.lang.String[]
 */
public String[] getFormulaeFields() {
	return formulaeFields;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-26 11:36:32)
 * @return nc.vo.pub.report.QryOrderVO[]
 */
public QryOrderVO[] getGroupFieldVOs() {
	return groupFieldVOs;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-26 11:36:32)
 * @return nc.vo.pub.report.QryOrderVO[]
 */
public QryOrderVO[] getOrderFieldVOs() {
	return orderFieldVOs;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-26 11:34:49)
 * @return java.lang.String[]
 */
public String[] getSelectFields() {
	return selectFields;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-4-16 18:49:59)
 * @return java.lang.String
 */
public java.lang.String getStartDate() {
	return startDate;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-26 11:34:49)
 * @return java.lang.String[]
 */
public String[] getSumFields() {
	return sumFields;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-28 12:56:29)
 * @param newDataTypes int[]
 */
public void setDataTypes(int[] newDataTypes) {
	dataTypes = newDataTypes;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-4-16 18:49:59)
 * @param newEndDate java.lang.String
 */
public void setEndDate(java.lang.String newEndDate) {
	endDate = newEndDate;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-26 11:52:57)
 * @param newSelectFields java.lang.String
 */
public void setFormulaeFields(String[] newFormulaeFields) {
	formulaeFields = newFormulaeFields;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-26 12:34:39)
 * @param groupFieldVOs nc.vo.pub.report.QryOrderVO[]
 */
public void setGroupFieldVOs(QryOrderVO[] newGroupFieldVOs) {
	groupFieldVOs = newGroupFieldVOs;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-26 12:34:39)
 * @param groupFieldVOs nc.vo.pub.report.QryOrderVO[]
 */
public void setOrderFieldVOs(QryOrderVO[] newOrderFieldVOs) {
	orderFieldVOs = newOrderFieldVOs;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-26 11:52:57)
 * @param newSelectFields java.lang.String
 */
public void setSelectFields(String[] newSelectFields) {
	selectFields = newSelectFields;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-4-16 18:49:59)
 * @param newStartDate java.lang.String
 */
public void setStartDate(java.lang.String newStartDate) {
	startDate = newStartDate;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-26 11:52:57)
 * @param newSelectFields java.lang.String
 */
public void setSumFields(String[] newSumFields) {
	sumFields = newSumFields;
}
/**
 * ��֤���������֮��������߼���ȷ�ԡ�
 * 
 * �������ڣ�(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException �����֤ʧ�ܣ��׳�
 *     ValidationException���Դ�����н��͡�
 */
public void validate() throws nc.vo.pub.ValidationException {}
}
