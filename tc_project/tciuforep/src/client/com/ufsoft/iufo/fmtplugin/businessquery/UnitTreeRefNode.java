package com.ufsoft.iufo.fmtplugin.businessquery;

/**
 * ��λ�������ڵ㡣
 *
 * �������ڣ�(2003-9-17 15:01:42)
 * @author������Ƽ
 */

public class UnitTreeRefNode extends javax.swing.tree.DefaultMutableTreeNode {
	private static final long serialVersionUID = 2750245074263802740L;
	
	/**
     * ��λ����
     */
    private String m_strUnitName = null;
    /**
     * ��λ����
     */
    private String m_strUnitCode = null;
    /**
     * ��λ���α���
     */
    private String m_strUnitOrgCode = null;
/**
 * UnitTreeRefNode ������ע�⡣
 */
public UnitTreeRefNode() {
	super();
}
/**
 * UnitTreeRefNode ������ע�⡣
 * @param userObject java.lang.Object
 */
public UnitTreeRefNode(Object userObject) {
	super(userObject);
}
/**
 * UnitTreeRefNode ������ע�⡣
 * @param userObject java.lang.Object
 * @param allowsChildren boolean
 */
public UnitTreeRefNode(Object userObject, boolean allowsChildren) {
	super(userObject, allowsChildren);
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-17 15:37:15)
 * @author������Ƽ
 * @param unitInfoVO nc.vo.iufo.unit.UnitInfoVO
 */
public UnitTreeRefNode(nc.vo.iufo.unit.UnitInfoVO unitInfoVO,String strOrgPK) {
    this.m_strUnitCode = unitInfoVO.getCode();
    this.m_strUnitName = unitInfoVO.getUnitName();
    this.m_strUnitOrgCode = unitInfoVO.getPropValue(strOrgPK);
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-17 15:57:37)
 * @author������Ƽ
 * @return java.lang.String
 */
public java.lang.String getUnitCode() {
	return m_strUnitCode;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-17 15:57:37)
 * @author������Ƽ
 * @return java.lang.String
 */
public java.lang.String getUnitOrgCode() {
	return m_strUnitOrgCode;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-17 15:57:37)
 * @author������Ƽ
 * @return java.lang.String
 */
public java.lang.String getUnitName() {
	return m_strUnitName;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-17 15:35:50)
 * @author������Ƽ
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


