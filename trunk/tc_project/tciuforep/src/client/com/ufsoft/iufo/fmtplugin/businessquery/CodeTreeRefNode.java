package com.ufsoft.iufo.fmtplugin.businessquery;

/**
 * ������Ϣ�Ĳ������ڵ㡣
 * 
 * �������ڣ�(2003-9-20 15:54:38)
 * @author������Ƽ
 */
public class CodeTreeRefNode
    extends javax.swing.tree.DefaultMutableTreeNode {
    /**
     * ��ʾ�ı�����Ϣ����
     */
    private String m_strDisplayText = null;
    /**
     * ����������CodeInfoVO��ֵ
     */
    private String m_strReturnValue = null;
/**
 * CodeInfoTreeRefNode ������ע�⡣
 */
public CodeTreeRefNode() {
	super();
}
/**
 * CodeInfoTreeRefNode ������ע�⡣
 * @param userObject java.lang.Object
 */
public CodeTreeRefNode(Object userObject) {
	super(userObject);
}
/**
 * CodeInfoTreeRefNode ������ע�⡣
 * @param userObject java.lang.Object
 * @param allowsChildren boolean
 */
public CodeTreeRefNode(Object userObject, boolean allowsChildren) {
	super(userObject, allowsChildren);
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-20 16:28:53)
 * @author������Ƽ
 * @param strDisplayText java.lang.String
 * @param strReturnValue java.lang.String
 */
public CodeTreeRefNode(String strDisplayText, String strReturnValue) {
    this.m_strDisplayText = strDisplayText;
    this.m_strReturnValue = strReturnValue;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-20 16:24:50)
 * @author������Ƽ
 * @return java.lang.String
 */
public java.lang.String getDisplayText() {
	return m_strDisplayText;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-20 16:24:50)
 * @author������Ƽ
 * @return java.lang.String
 */
public java.lang.String getReturnValue() {
	return m_strReturnValue;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-20 16:24:50)
 * @author������Ƽ
 * @param newDisplayText java.lang.String
 */
public void setDisplayText(java.lang.String newDisplayText) {
	m_strDisplayText = newDisplayText;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-20 16:24:50)
 * @author������Ƽ
 * @param newReturnValue java.lang.String
 */
public void setReturnValue(java.lang.String newReturnValue) {
	m_strReturnValue = newReturnValue;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-20 20:21:57)
 * @author������Ƽ
 * @return java.lang.String
 */
public String toString() {
    return m_strDisplayText;
}
}


