/**
 * 
 */
package com.ufsoft.report;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import nc.ui.pub.beans.UITree;

/**
 * @author wangyga
 * @created at 2009-7-21,����08:49:11
 *
 */
public class UfoTree extends UITree {

private static final long serialVersionUID = 4799566681509408307L;
	
	public UfoTree(boolean bNeedEnter){
		super(bNeedEnter);
	}

	public boolean isCollapsed() {
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) getModel()
				.getRoot();
		int iCount = rootNode.getChildCount();
		for (int i = 0; i < iCount; i++) {
			DefaultMutableTreeNode subNode = (DefaultMutableTreeNode) rootNode
					.getChildAt(i);
			TreePath treePath = new TreePath(new Object[] { rootNode,
					subNode });
			if (!isCollapsed(treePath)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * չ�����ڵ�
	 */
	public void expandedTreeOneDegree() {
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) getModel()
				.getRoot();
		int iCount = rootNode.getChildCount();
		for (int i = 0; i < iCount; i++) {
			DefaultMutableTreeNode subNode = (DefaultMutableTreeNode) rootNode
					.getChildAt(i);
			if (subNode.getChildCount() > 0) {
				TreePath treePath = new TreePath(new Object[] { rootNode,
						subNode });
				this.expandPath(treePath);
			}
		}
	}

	/**
	 * �۵�ȫ��
	 * 
	 * 
	 */
	public void collapseWholeTree() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getModel()
				.getRoot();
		collapseTreeNode(node, true);
	}

	private void collapseTreeNode(DefaultMutableTreeNode node, boolean bRootNode) {
		int iCount = node.getChildCount();
		for (int i = 0; i < iCount; i++) {
			DefaultMutableTreeNode subNode = (DefaultMutableTreeNode) node
					.getChildAt(i);
			if (subNode.getChildCount() > 0)
				collapseTreeNode(subNode, false);
		}
		if (!bRootNode)
			this.collapsePath(new TreePath(((DefaultTreeModel) getModel())
					.getPathToRoot(node)));
	}

	/**
	 * �����㶨λ��ĳ�����ڵ�
	 * 
	 * @create by zhaopq at 2009-5-14,����02:26:31
	 * 
	 * @param label
	 *            ���ڵ����ʾ����
	 */
	public void focusTreeNode(String label) {

		TreePath treePath = findPathByLabel(new TreePath(getModel().getRoot()),
				label);
		if (treePath == null) {
			return;
		}
		this.setSelectionPath(treePath);
		this.scrollPathToVisible(treePath);
	}

	@SuppressWarnings("unchecked")
	private TreePath findPathByLabel(TreePath treePath, Object label) {
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath
				.getLastPathComponent();
		if (treeNode == null) {
			return null;
		}
		if (treeNode.getUserObject().equals(label)) {
			return treePath;
		} else {
			Enumeration<DefaultMutableTreeNode> e = treeNode.children();
			while (e.hasMoreElements()) {
				DefaultMutableTreeNode node = e.nextElement();
				TreePath path = treePath.pathByAddingChild(node);
				path = findPathByLabel(path, label);
				if (path != null) {
					return path;
				}
			}
		}
		return null;
	}

	public void setSelectedTreeNode(String[] strSelNodePKs){
		
	}
}
