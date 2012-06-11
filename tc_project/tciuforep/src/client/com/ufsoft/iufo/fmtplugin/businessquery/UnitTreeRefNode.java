package com.ufsoft.iufo.fmtplugin.businessquery;

/**
 * 单位参照树节点。
 *
 * 创建日期：(2003-9-17 15:01:42)
 * @author：刘良萍
 */

public class UnitTreeRefNode extends javax.swing.tree.DefaultMutableTreeNode {
	private static final long serialVersionUID = 2750245074263802740L;
	
	/**
     * 单位名称
     */
    private String m_strUnitName = null;
    /**
     * 单位编码
     */
    private String m_strUnitCode = null;
    /**
     * 单位级次编码
     */
    private String m_strUnitOrgCode = null;
/**
 * UnitTreeRefNode 构造子注解。
 */
public UnitTreeRefNode() {
	super();
}
/**
 * UnitTreeRefNode 构造子注解。
 * @param userObject java.lang.Object
 */
public UnitTreeRefNode(Object userObject) {
	super(userObject);
}
/**
 * UnitTreeRefNode 构造子注解。
 * @param userObject java.lang.Object
 * @param allowsChildren boolean
 */
public UnitTreeRefNode(Object userObject, boolean allowsChildren) {
	super(userObject, allowsChildren);
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-17 15:37:15)
 * @author：刘良萍
 * @param unitInfoVO nc.vo.iufo.unit.UnitInfoVO
 */
public UnitTreeRefNode(nc.vo.iufo.unit.UnitInfoVO unitInfoVO,String strOrgPK) {
    this.m_strUnitCode = unitInfoVO.getCode();
    this.m_strUnitName = unitInfoVO.getUnitName();
    this.m_strUnitOrgCode = unitInfoVO.getPropValue(strOrgPK);
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-17 15:57:37)
 * @author：刘良萍
 * @return java.lang.String
 */
public java.lang.String getUnitCode() {
	return m_strUnitCode;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-17 15:57:37)
 * @author：刘良萍
 * @return java.lang.String
 */
public java.lang.String getUnitOrgCode() {
	return m_strUnitOrgCode;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-17 15:57:37)
 * @author：刘良萍
 * @return java.lang.String
 */
public java.lang.String getUnitName() {
	return m_strUnitName;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-17 15:35:50)
 * @author：刘良萍
 * @return java.lang.String
 */
public String toString() {
    return new StringBuffer(m_strUnitName)
        .append("(")
        .append(m_strUnitCode)
        .append(")")
        .toString();
}
}


