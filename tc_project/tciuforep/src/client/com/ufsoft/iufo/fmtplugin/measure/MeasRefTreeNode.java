package com.ufsoft.iufo.fmtplugin.measure;

/**
 *	指标参照数的节点
 * 创建日期：(2003-8-26 10:43:22)
 * @author：王海涛
 */
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;
public class MeasRefTreeNode extends DefaultMutableTreeNode 
{
	private String m_nodeName;
	private String m_pk;
	private String m_repCode;
	//展开
	protected Icon   m_expandedIcon;
	//正常
	protected Icon   m_icon;
/**
 * MeasRefTreeNode 构造子注解。
 */
public MeasRefTreeNode(Icon icon) {
	super();
	m_icon = icon;
}
/**
 * MeasRefTreeNode 构造子注解。
 */
public MeasRefTreeNode(Icon icon, Icon expandedIcon) {
	super();
	m_icon = icon;
	m_expandedIcon = expandedIcon;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-8-26 11:46:02)
 * @return boolean
 * @param subNode java.lang.Object
 */
public void addSubNode(javax.swing.tree.DefaultMutableTreeNode subNode) {

	this.add(subNode);
}
	/**
	展开该节点
	*/

	public boolean expand(MeasRefTreeNode parent)
	{

		//DefaultMutableTreeNode flag = (DefaultMutableTreeNode)parent.getFirstChild();
		//if (flag==null)	  // No flag
			//return true;
		//Object obj = flag.getUserObject();
		////if (!(obj instanceof Boolean))
		return true;
	}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-8-26 10:48:42)
 * @return java.lang.String[]
 */
public Vector getAllSubNode() {
	return this.children;
}
/**
  *得到展开图标  
  */

public Icon getExpandedIcon() 
{ 
	return m_expandedIcon!=null ? m_expandedIcon : m_icon;
}
/**
 * 得到图标
 */

public Icon getIcon() 
{ 
	return m_icon;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-8-26 11:45:05)
 * @return java.lang.String
 */
public java.lang.String getName() {
	return m_nodeName;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-8-27 11:23:43)
 * @return java.lang.String
 */
public java.lang.String getPk() {
	return m_pk;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-8-26 11:45:05)
 * @return java.lang.String
 */
public java.lang.String getReportCode() {
	return m_repCode;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-8-26 11:45:05)
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	m_nodeName = newName;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-8-27 11:23:43)
 * @param newM_pk java.lang.String
 */
public void setPk(java.lang.String newpk) {
	m_pk = newpk;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-8-26 11:45:05)
 * @param newName java.lang.String
 */
public void setReportCode(java.lang.String newRepCode) {
	m_repCode = newRepCode;
}
	public String toString()
	{
		if(m_repCode != null){
			return "("+m_repCode+")" + m_nodeName ;
		}else{
			return m_nodeName;
		}
	}
	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		if (obj instanceof MeasRefTreeNode) {
			MeasRefTreeNode otherMeas = (MeasRefTreeNode) obj;
			if (getPk() != null && otherMeas.getPk() != null) {
				if (getPk().equals(otherMeas.getPk())) {
					isEqual = true;
				}
			} else if (getPk() == null && otherMeas.getPk() == null) {
				if (getName().equals(otherMeas.getName())) {
					isEqual = true;
				}
			}
		}

		return isEqual;
	}
	/**
	 * 使树节点与报表PK的hashcode对应,便与由reportPK反向定位树节点
	 */
	@Override
	public int hashCode() {
		if(getPk()!=null){
			return getPk().hashCode();
		}else if(getName()!=null){
			return getName().hashCode();
		}else
			return 0;
	}
	
	
}
