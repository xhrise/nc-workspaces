package nc.vo.hi.hi_312;

import nc.vo.pub.*;
/**
 * �˴���������˵����
 * �������ڣ�(2003-9-6 12:26:49)
 * @author������
 */
public class BlackListAllVO extends nc.vo.pub.CircularlyAccessibleValueObject {
	private PsndocBadHeaderVO header = null;
	private PsndocBadItemVO[] items = null;

	// ʱ�����ʾ��������δʹ�ã�
	long currentTimestamp; // ��ǰʱ���
	long initialTimestamp; // �����ݿ����ʱ��õ�ʱ���
/**
 * RoleAllVO ������ע�⡣
 */
public BlackListAllVO() {
	super();
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:26:03)
 * @return java.lang.String[]
 */
public java.lang.String[] getAttributeNames() {
	return null;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public Object getAttributeValue(String attributeName) {
	return null;
}
/**
 * <p>����ӱ��VO���顣
 * <p>
 * �������ڣ�(2003-8-26)
 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
 */
public CircularlyAccessibleValueObject[] getChildrenVO() {

	return items;
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
 * <p>���ĸ���VO��
 * <p>
 * �������ڣ�(2003-8-26)
 * @return nc.vo.pub.CircularlyAccessibleValueObject
 */
public CircularlyAccessibleValueObject getParentVO() {

	return header;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public void setAttributeValue(String name, Object value) {}
/**
 * <p>�����ӱ��VO���顣
 * <p>
 * �������ڣ�(2003-8-26)
 * @param children nc.vo.pub.CircularlyAccessibleValueObject[]
 */
public void setChildrenVO(CircularlyAccessibleValueObject[] children) {

	items = (PsndocBadItemVO[]) children;
}
/**
 * <p>����ĸ���VO��
 * <p>
 * �������ڣ�(2003-8-26)
 * @param parent nc.vo.pub.CircularlyAccessibleValueObject
 */
public void setParentVO(CircularlyAccessibleValueObject parent) {

	header = (PsndocBadHeaderVO) parent;
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
