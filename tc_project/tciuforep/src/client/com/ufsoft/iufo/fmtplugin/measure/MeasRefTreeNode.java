package com.ufsoft.iufo.fmtplugin.measure;

/**
 *	ָ��������Ľڵ�
 * �������ڣ�(2003-8-26 10:43:22)
 * @author��������
 */
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;
public class MeasRefTreeNode extends DefaultMutableTreeNode 
{
	private String m_nodeName;
	private String m_pk;
	private String m_repCode;
	//չ��
	protected Icon   m_expandedIcon;
	//����
	protected Icon   m_icon;
/**
 * MeasRefTreeNode ������ע�⡣
 */
public MeasRefTreeNode(Icon icon) {
	super();
	m_icon = icon;
}
/**
 * MeasRefTreeNode ������ע�⡣
 */
public MeasRefTreeNode(Icon icon, Icon expandedIcon) {
	super();
	m_icon = icon;
	m_expandedIcon = expandedIcon;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-8-26 11:46:02)
 * @return boolean
 * @param subNode java.lang.Object
 */
public void addSubNode(javax.swing.tree.DefaultMutableTreeNode subNode) {

	this.add(subNode);
}
	/**
	չ���ýڵ�
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
 * �˴����뷽��������
 * �������ڣ�(2003-8-26 10:48:42)
 * @return java.lang.String[]
 */
public Vector getAllSubNode() {
	return this.children;
}
/**
  *�õ�չ��ͼ��  
  */

public Icon getExpandedIcon() 
{ 
	return m_expandedIcon!=null ? m_expandedIcon : m_icon;
}
/**
 * �õ�ͼ��
 */

public Icon getIcon() 
{ 
	return m_icon;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-8-26 11:45:05)
 * @return java.lang.String
 */
public java.lang.String getName() {
	return m_nodeName;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-8-27 11:23:43)
 * @return java.lang.String
 */
public java.lang.String getPk() {
	return m_pk;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-8-26 11:45:05)
 * @return java.lang.String
 */
public java.lang.String getReportCode() {
	return m_repCode;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-8-26 11:45:05)
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	m_nodeName = newName;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-8-27 11:23:43)
 * @param newM_pk java.lang.String
 */
public void setPk(java.lang.String newpk) {
	m_pk = newpk;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-8-26 11:45:05)
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
	 * ʹ���ڵ��뱨��PK��hashcode��Ӧ,������reportPK����λ���ڵ�
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
