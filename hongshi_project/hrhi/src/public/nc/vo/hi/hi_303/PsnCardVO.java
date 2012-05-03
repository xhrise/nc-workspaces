package nc.vo.hi.hi_303;

/**
 * ���¿�ƬVO
 * 
 * �������ڣ�(2002-3-29 16:43:49)
 * @author��zhonghaijing
 */
public class PsnCardVO extends nc.vo.pub.ExtendedAggregatedValueObject {
	String[] tableCodes = null;
	//String[] tableNames = null;
	nc.vo.hi.hi_301.HRMainVO mainVO = null;
	nc.vo.hi.hi_401.PsnDataVO[][] childVOss = null;
	java.util.Hashtable htIndex = null;
/**
 * PsnCardVO ������ע�⡣
 */
public PsnCardVO() {
	super();
}
/**
 * PsnCardVO ������ע�⡣
 */
public PsnCardVO(
	String[] sTableCodes,
	nc.vo.hi.hi_301.HRMainVO vo,
	nc.vo.hi.hi_401.PsnDataVO[][] voss) {
	super();
	tableCodes = sTableCodes;
	mainVO = vo;
	childVOss = voss;
	if (tableCodes != null && mainVO != null && childVOss != null)
		initVOData();
}
/**
 * ����ĸ��VO��
 * �������ڣ�(01-3-20 17:32:28)
 * @return nc.vo.pub.ValueObject
 */
public nc.vo.pub.CircularlyAccessibleValueObject getParentVO() {
	return mainVO;
}
/**
 * ���ظ����ӱ�ı��롣
 * �������ڣ�(01-3-20 17:36:56)
 * @return String[]
 */
public java.lang.String[] getTableCodes() {
	return tableCodes;
}
/**
 * ���ظ����ӱ���������ơ�
 * �������ڣ�(01-3-20 17:36:56)
 * @return String[]
 */
public java.lang.String[] getTableNames() {
	//return tableNames;
	return null;
}
/**
 * ����ĳ���ӱ��VO���顣
 * �������ڣ�(01-3-20 17:36:56)
 * @return nc.vo.pub.ValueObject[]
 * @param tableCode String �ӱ�ı���
 */
public nc.vo.pub.CircularlyAccessibleValueObject[] getTableVO(
	String tableCode) {
	if (htIndex == null)
		return null;
	else
		return childVOss[((Integer) htIndex.get(tableCode)).intValue()];
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-4-1 15:23:27)
 */
public void initVOData() {
	if (tableCodes.length == childVOss.length) {
		htIndex = new java.util.Hashtable();
		for (int i = 0; i < tableCodes.length; i++) {
			htIndex.put(tableCodes[i], new Integer(i));
		}
	}
}
/**
 * ĸ��VO��setter������
 * �������ڣ�(01-3-20 17:32:28)
 * @param parent nc.vo.pub.ValueObject ĸ��VO
 */
public void setParentVO(nc.vo.pub.CircularlyAccessibleValueObject parent) {
	//ûʹ��
	mainVO = (nc.vo.hi.hi_301.HRMainVO) parent;
}
/**
 * ĳ���ӱ�VO�����setter������
 * �������ڣ�(01-3-20 17:36:56)
 * @param tableCode String �ӱ����
 * @param values nc.vo.pub.ValueObject[] �ӱ�VO����
 */
public void setTableVO(
	String tableCode,
	nc.vo.pub.CircularlyAccessibleValueObject[] values) {
	//ûʹ��
}
}
