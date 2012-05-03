package nc.vo.hi.hi_303;

/**
 * 人事卡片VO
 * 
 * 创建日期：(2002-3-29 16:43:49)
 * @author：zhonghaijing
 */
public class PsnCardVO extends nc.vo.pub.ExtendedAggregatedValueObject {
	String[] tableCodes = null;
	//String[] tableNames = null;
	nc.vo.hi.hi_301.HRMainVO mainVO = null;
	nc.vo.hi.hi_401.PsnDataVO[][] childVOss = null;
	java.util.Hashtable htIndex = null;
/**
 * PsnCardVO 构造子注解。
 */
public PsnCardVO() {
	super();
}
/**
 * PsnCardVO 构造子注解。
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
 * 返回母表VO。
 * 创建日期：(01-3-20 17:32:28)
 * @return nc.vo.pub.ValueObject
 */
public nc.vo.pub.CircularlyAccessibleValueObject getParentVO() {
	return mainVO;
}
/**
 * 返回各个子表的编码。
 * 创建日期：(01-3-20 17:36:56)
 * @return String[]
 */
public java.lang.String[] getTableCodes() {
	return tableCodes;
}
/**
 * 返回各个子表的中文名称。
 * 创建日期：(01-3-20 17:36:56)
 * @return String[]
 */
public java.lang.String[] getTableNames() {
	//return tableNames;
	return null;
}
/**
 * 返回某个子表的VO数组。
 * 创建日期：(01-3-20 17:36:56)
 * @return nc.vo.pub.ValueObject[]
 * @param tableCode String 子表的编码
 */
public nc.vo.pub.CircularlyAccessibleValueObject[] getTableVO(
	String tableCode) {
	if (htIndex == null)
		return null;
	else
		return childVOss[((Integer) htIndex.get(tableCode)).intValue()];
}
/**
 * 此处插入方法说明。
 * 创建日期：(2002-4-1 15:23:27)
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
 * 母表VO的setter方法。
 * 创建日期：(01-3-20 17:32:28)
 * @param parent nc.vo.pub.ValueObject 母表VO
 */
public void setParentVO(nc.vo.pub.CircularlyAccessibleValueObject parent) {
	//没使用
	mainVO = (nc.vo.hi.hi_301.HRMainVO) parent;
}
/**
 * 某个子表VO数组的setter方法。
 * 创建日期：(01-3-20 17:36:56)
 * @param tableCode String 子表编码
 * @param values nc.vo.pub.ValueObject[] 子表VO数组
 */
public void setTableVO(
	String tableCode,
	nc.vo.pub.CircularlyAccessibleValueObject[] values) {
	//没使用
}
}
