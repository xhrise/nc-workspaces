package com.ufsoft.iufo.fmtplugin.measure;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import com.ufsoft.iufo.resource.StringResource;


class MeasureIconCellRenderer 
	extends    JLabel 
	implements TreeCellRenderer
{
	protected Color m_textSelectionColor;
	protected Color m_textNonSelectionColor;
	protected Color m_bkSelectionColor;
	protected Color m_bkNonSelectionColor;
	protected Color m_borderSelectionColor;

	protected boolean m_selected;

/**
  *
  */

  
public MeasureIconCellRenderer()
{
	super();

	//因JDK1.1.7太低，不支持，所以暂时替换
	m_textSelectionColor = UIManager.getColor(
		"Tree.selectionForeground");	
	m_textNonSelectionColor = UIManager.getColor(
		"Tree.textForeground");	
	m_bkSelectionColor = UIManager.getColor(
		"Tree.selectionBackground");	
	m_bkNonSelectionColor = UIManager.getColor(
		"Tree.textBackground");	
	m_borderSelectionColor = UIManager.getColor(
		"Tree.selectionBorderColor");

	
	setOpaque(false);
}
/**
 * getTreeCellRendererComponent 方法注解。
 */
public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree tree, java.lang.Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
	DefaultMutableTreeNode node = 
		(DefaultMutableTreeNode)value;
	Object obj = node.getUserObject();
	setText(node.toString());
	

	if (obj instanceof Boolean)
		  setText(StringResource.getStringResource("miufo1000762"));  //"装载数据..."

	if (node instanceof MeasRefTreeNode)
	{
		MeasRefTreeNode idata = (MeasRefTreeNode)node;
		if (expanded)
			setIcon(idata.getExpandedIcon());
		else
			setIcon(idata.getIcon());
	}
	else
		setIcon(null);

	
	setForeground(selected ? m_textSelectionColor : 
		m_textNonSelectionColor);
	setBackground(selected ? m_bkSelectionColor : 
		m_bkNonSelectionColor);
	m_selected = selected;
	return this;
}
/**
  *
  */

  
public void paintComponent(Graphics g) 
{
	Color bColor = getBackground();
	Icon icon = getIcon();

	g.setColor(bColor);
	int offset = 0;
	if(icon != null && getText() != null) 
		offset = (icon.getIconWidth() + getIconTextGap());
	g.fillRect(offset, 0, getWidth() - 1 - offset,
		getHeight() - 1);
		
	if (m_selected) 
	{
		g.setColor(m_borderSelectionColor);
		g.drawRect(offset, 0, getWidth()-1-offset, getHeight()-1);
	}
	super.paintComponent(g);
}
}
