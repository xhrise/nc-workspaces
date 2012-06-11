package com.ufsoft.iufo.fmtplugin.businessquery;

/**
 * 编码信息的参照树节点。
 * 
 * 创建日期：(2003-9-20 15:54:38)
 * @author：刘良萍
 */
public class CodeTreeRefNode
    extends javax.swing.tree.DefaultMutableTreeNode {
    /**
     * 显示的编码信息内容
     */
    private String m_strDisplayText = null;
    /**
     * 返回所参照CodeInfoVO的值
     */
    private String m_strReturnValue = null;
/**
 * CodeInfoTreeRefNode 构造子注解。
 */
public CodeTreeRefNode() {
	super();
}
/**
 * CodeInfoTreeRefNode 构造子注解。
 * @param userObject java.lang.Object
 */
public CodeTreeRefNode(Object userObject) {
	super(userObject);
}
/**
 * CodeInfoTreeRefNode 构造子注解。
 * @param userObject java.lang.Object
 * @param allowsChildren boolean
 */
public CodeTreeRefNode(Object userObject, boolean allowsChildren) {
	super(userObject, allowsChildren);
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-20 16:28:53)
 * @author：刘良萍
 * @param strDisplayText java.lang.String
 * @param strReturnValue java.lang.String
 */
public CodeTreeRefNode(String strDisplayText, String strReturnValue) {
    this.m_strDisplayText = strDisplayText;
    this.m_strReturnValue = strReturnValue;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-20 16:24:50)
 * @author：刘良萍
 * @return java.lang.String
 */
public java.lang.String getDisplayText() {
	return m_strDisplayText;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-20 16:24:50)
 * @author：刘良萍
 * @return java.lang.String
 */
public java.lang.String getReturnValue() {
	return m_strReturnValue;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-20 16:24:50)
 * @author：刘良萍
 * @param newDisplayText java.lang.String
 */
public void setDisplayText(java.lang.String newDisplayText) {
	m_strDisplayText = newDisplayText;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-20 16:24:50)
 * @author：刘良萍
 * @param newReturnValue java.lang.String
 */
public void setReturnValue(java.lang.String newReturnValue) {
	m_strReturnValue = newReturnValue;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-20 20:21:57)
 * @author：刘良萍
 * @return java.lang.String
 */
public String toString() {
    return m_strDisplayText;
}
}


