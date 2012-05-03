package nc.vo.hi.hi_301;

import java.util.Vector;
import java.util.ArrayList;
import java.util.Hashtable;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 此处插入类型描述。
 * 创建日期：(2004-4-21 9:12:50)
 * @author：Administrator
 */
public class ExAggregatedPsnInfoVO extends nc.vo.pub.ExtendedAggregatedValueObject {
	private Vector vtableCodes = null;
	private Vector vtableNames = null;
	
/**
 * ExAggregatedPsnInfoVO 构造子注解。
 */
public ExAggregatedPsnInfoVO() {
	super();
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-21 9:14:31)
 * @param tableCodes java.lang.String[]
 * @param tableNames java.lang.String[]
 */
public ExAggregatedPsnInfoVO(Vector vtableCodes, Vector vtableNames) {
	this.vtableCodes = vtableCodes;
	this.vtableNames = vtableNames;
}
/**
 * 返回母表VO。
 * 创建日期：(01-3-20 17:32:28)
 * @return nc.vo.pub.ValueObject
 */
public nc.vo.pub.CircularlyAccessibleValueObject getParentVO() {
	if(headVO==null){
		headVO = new PsnInfoChildVO();
	}
	return headVO;
}
/**
 * 返回各个子表的编码。
 * 创建日期：(01-3-20 17:36:56)
 * @return String[]
 */
public java.lang.String[] getTableCodes() {
	String[] tableCodes = null;
	if(vtableCodes!=null&&vtableCodes.size()>0){
		tableCodes = new String[vtableCodes.size()];
		vtableCodes.copyInto(tableCodes);
	}
	return tableCodes;
}
/**
 * 返回各个子表的中文名称。
 * 创建日期：(01-3-20 17:36:56)
 * @return String[]
 */
public java.lang.String[] getTableNames() {
	String[] tableNames = null;
	if(vtableNames!=null&&vtableNames.size()>0){
		tableNames = new String[vtableCodes.size()];
		vtableNames.copyInto(tableNames);
	}
	return tableNames;
}
/**
 * 返回某个子表的VO数组。
 * 创建日期：(01-3-20 17:36:56)
 * @return nc.vo.pub.ValueObject[]
 * @param tableCode String 子表的编码
 */
public nc.vo.pub.CircularlyAccessibleValueObject[] getTableVO(String tableCode) {
	if(vtableCodes==null){
		return null;
	}
	int index = vtableCodes.indexOf(tableCode);
	if(index>=0){
		return (nc.vo.pub.CircularlyAccessibleValueObject[])m_alBodyVOs.get(index);
	}else
		return null;
}
/**
 * 母表VO的setter方法。
 * 创建日期：(01-3-20 17:32:28)
 * @param parent nc.vo.pub.ValueObject 母表VO
 */
public void setParentVO(nc.vo.pub.CircularlyAccessibleValueObject parent) {
	headVO = (PsnInfoChildVO)parent;
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
	//第一个VO
	if(m_alBodyVOs == null){
	    vtableCodes = new Vector();
	    m_alBodyVOs = new ArrayList();

	    vtableCodes.addElement(tableCode);
	    m_alBodyVOs.add(values);
	}else{
	    int index = vtableCodes.indexOf(tableCode);
	    if(index>=0){
		    m_alBodyVOs.set(index,values);
	    }else{
		    vtableCodes.addElement(tableCode);
		    m_alBodyVOs.add(values);
	    }
	}
}

	private PsnInfoChildVO headVO = null;
	private java.util.Hashtable htHeadVO = new Hashtable(); //主要用来存储人员信息集和信息子集，而且每个人员的这两个集合都是一条记录
	private ArrayList m_alBodyVOs = null;

/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-21 10:40:38)
 * @param tableCode java.lang.String
 * @param tableValue java.lang.String
 * @param bodyValues nc.vo.pub.CircularlyAccessibleValueObject[]
 */
public void addBodyValues(
	String tableCode,
	String tableName,
	CircularlyAccessibleValueObject[] bodyValues) {
	    
	if (m_alBodyVOs == null) {//第一个子表
		vtableCodes = new Vector();
		vtableNames = new Vector();
		m_alBodyVOs = new ArrayList();

		vtableCodes.addElement(tableCode);
		vtableNames.addElement(tableName);
		m_alBodyVOs.add(bodyValues);
	} else {
		int index = vtableCodes.indexOf(tableCode);
		if (index >= 0) {
			vtableNames.setElementAt(tableName, index);
			m_alBodyVOs.set(index, bodyValues);
		} else {
			vtableCodes.addElement(tableCode);
			vtableNames.addElement(tableName);
			m_alBodyVOs.add(bodyValues);
		}
	}
}

/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-21 14:23:39)
 * @return java.util.Hashtable
 */
public java.util.Hashtable getHtHeadVO() {
	return htHeadVO;
}

/**
 * 此处插入方法描述。
 * 创建日期：(2004-4-21 14:23:39)
 * @param newHtHeadVO java.util.Hashtable
 */
public void setHtHeadVO(java.util.Hashtable newHtHeadVO) {
	htHeadVO = newHtHeadVO;
}
}
