package com.ufsoft.table.re;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITree;

public class GeneralTreeRefComp extends UIPanel implements IRefComp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UITree treeRefComp=null; 
	private String m_strTitle = null;

	public GeneralTreeRefComp(TreeModel model, String strTitle) {
		treeRefComp=new UITree(model);
		m_strTitle=strTitle;
		UIScrollPane treePanel = new UIScrollPane(treeRefComp);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(300,500));
		this.add(treePanel,BorderLayout.CENTER);
	}

	public Object getSelectValue() {
		if(treeRefComp.getSelectionPath()!=null){
			DefaultMutableTreeNode node = ((DefaultMutableTreeNode)treeRefComp.getSelectionPath().getLastPathComponent());
	        return node.getUserObject();
		}else
			return null;
		
	}

	public String getTitleValue() {
		return m_strTitle;
	}

	public Object getValidateValue(String text) {
		return text;
	}

	public void setDefaultValue(Object obj) {
		if(obj instanceof String){
			treeRefComp.getTreeNodeSearcher().locationTree((String)obj);
		}else{
			DefaultMutableTreeNode node=(DefaultMutableTreeNode)treeRefComp.getModel().getRoot();
			TreePath path = new TreePath(((DefaultTreeModel)treeRefComp.getModel()).getPathToRoot(node));
		    treeRefComp.getSelectionModel().setSelectionPath(path);
		}
		
	}

	public JTree getTreeRefComp() {
		return treeRefComp;
	}
    

}
