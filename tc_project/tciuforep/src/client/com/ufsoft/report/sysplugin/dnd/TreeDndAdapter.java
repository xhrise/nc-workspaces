package com.ufsoft.report.sysplugin.dnd;

import java.awt.Point;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * 
 * @author zzl 2005-4-25
 */
public class TreeDndAdapter implements IDndAdapter {
    

    private JTree m_tree;

    /**
     * 
     */
    public TreeDndAdapter(JTree tree) {
        m_tree = tree;
    }
    /*
     * @see dnd.IDndAdapter#getSourceObject()
     */
    public Object getSourceObject() {
        DefaultMutableTreeNode dragNode = (DefaultMutableTreeNode) m_tree.getSelectionPath().getLastPathComponent();
        return dragNode.getUserObject();
    }

    /*
     * @see dnd.IDndAdapter#removeSourceNode()
     */
    public void removeSourceNode() {
        DefaultMutableTreeNode dragNode = (DefaultMutableTreeNode) m_tree.getSelectionPath().getLastPathComponent();
        ((DefaultTreeModel)m_tree.getModel()).removeNodeFromParent(dragNode);
    }

    /*
     * @see dnd.IDndAdapter#insertObject(java.awt.Point, java.lang.Object)
     */
    public boolean insertObject(Point ap, Object obj) {
        DefaultMutableTreeNode parnode;
        javax.swing.tree.TreePath tp = m_tree.getPathForLocation(ap.x, ap.y);
        if (tp != null) {
            try { 
            parnode = (DefaultMutableTreeNode) tp.getLastPathComponent();
            int index = parnode.getChildCount();
            DefaultTreeModel tm = (DefaultTreeModel)m_tree.getModel();           
            DefaultMutableTreeNode dragNode = new DefaultMutableTreeNode(obj);
            tm.insertNodeInto(dragNode, parnode, index);
            return true;
          }
          catch (java.lang.Exception e) {
            AppDebug.debug(e);
          }
        }   
        return false;
    }

}
