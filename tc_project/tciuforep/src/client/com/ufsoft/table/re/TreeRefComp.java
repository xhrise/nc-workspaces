package com.ufsoft.table.re;

import java.awt.EventQueue;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.ufsoft.table.beans.UFOTree;

/**
 * 
 * @author zzl 2005-6-22
 */
public abstract class TreeRefComp extends UFOTree implements IRefComp {
	private String m_strTitle = null;
    /**
     * @param root
     */
    public TreeRefComp(TreeModel model,String strTitle) {
        super(model);
        m_strTitle = strTitle;
    }

    /*
     * @see com.ufsoft.table.re.IRefComp#getSelectIDName()
     */
    public Object getSelectValue() {
        DefaultMutableTreeNode node = ((DefaultMutableTreeNode)getSelectionPath().getLastPathComponent());
        return node.getUserObject();
    }

    /*
     * @see com.ufsoft.table.re.IRefComp#setDefaultIDName(com.ufsoft.table.re.IDName)
     */
    public void setDefaultValue(Object obj) {
        DefaultTreeModel model=(DefaultTreeModel)getModel();
        DefaultMutableTreeNode node = null;
        node=getTreeNodeByValue(model,obj);
        if(node==null){
        	node=(DefaultMutableTreeNode)model.getRoot();
        }
        if (node!=null){
	        TreePath path = new TreePath(((DefaultTreeModel)getModel()).getPathToRoot(node));
	        EventQueue.invokeLater(new Runnable(){
				public void run() { 
					requestFocus();
				}
	        	
	        });
	        getSelectionModel().setSelectionPath(path);
	        scrollPathToVisible(path);
        }
    }
    
    private DefaultMutableTreeNode getTreeNodeByValue(DefaultTreeModel model,Object objValue){
    	if (objValue==null || objValue instanceof Comparable==false || model.getRoot() instanceof DefaultMutableTreeNode==false)
    		return null;
    	DefaultMutableTreeNode root=(DefaultMutableTreeNode)model.getRoot();
    	return innerGetTreeNodeByValue(root,objValue);
    }
    
    private DefaultMutableTreeNode innerGetTreeNodeByValue(DefaultMutableTreeNode parent,Object objValue){
    	for (int i=0;i<parent.getChildCount();i++){
    		DefaultMutableTreeNode node=(DefaultMutableTreeNode)parent.getChildAt(i);
    		Object obj=node.getUserObject();
    		if (obj!=null && obj instanceof IDNameToStringWrapper){
    			obj=((IDNameToStringWrapper)obj).getValue();
    		}
    		if (obj==objValue || ((Comparable)objValue).compareTo(obj)==0)
    			return node;
    		
    		DefaultMutableTreeNode retNode=innerGetTreeNodeByValue(node,objValue);
    		if (retNode!=null)
    			return retNode;
    	}
    	return null;
    }

    /*
     * @see com.ufsoft.table.re.IRefComp#isValidate(java.lang.String)
     */
    public abstract Object getValidateValue(String text);
    /**
     * 缺省标题为空，子类需重载该方法
     */
	public String getTitleValue() {
		return m_strTitle;
	}
}
