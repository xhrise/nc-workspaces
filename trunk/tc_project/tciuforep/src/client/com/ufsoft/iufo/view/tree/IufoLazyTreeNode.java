package com.ufsoft.iufo.view.tree;

import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class IufoLazyTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 6009016647810575279L;

	private IufoLazyTreeNodeModel nodeModel = new IufoLazyTreeNodeModel();

	private boolean hasLoadSubNode;

	public IufoLazyTreeNode(String label) {
		setUserObject(label);
	}

	public boolean isHasLoadSubNode() {
		return hasLoadSubNode;
	}

	public void setHasLoadSubNode(boolean hasLoadSubNode) {
		this.hasLoadSubNode = hasLoadSubNode;
	}

	public boolean isLeaf() {
		return nodeModel.isLeaf() && super.isLeaf();
	}

	public void expanded(DefaultTreeModel treeModel) {
		if (hasLoadSubNode) {
			return;
		}
		IufoLazyTreeNodeModel[] subNodeDatas = nodeModel.getSubNodeDatas();
		if (subNodeDatas == null) {
			return;
		}

		Map<String, IufoLazyTreeNode> hashTreeNode = new HashMap<String, IufoLazyTreeNode>();
		for (IufoLazyTreeNodeModel modelData : subNodeDatas) {
			if (modelData == null) {
				continue;
			}
			
			IufoLazyTreeNode treeNode = new IufoLazyTreeNode(modelData
					.getLabel());
			treeNode.setNodeModel(modelData);
			hashTreeNode.put(modelData.getId(), treeNode);
		}
		for (IufoLazyTreeNodeModel modelData : subNodeDatas) {
			if (modelData == null) {
				continue;
			}
			String id = modelData.getId();
			String parentId = modelData.getParentId();
			IufoLazyTreeNode treeNode = hashTreeNode.get(id);

			IufoLazyTreeNode parentNode = parentId == null || parentId.trim().length() == 0? this
					: hashTreeNode.get(parentId);

			// if(parentNode==null){
			// System.out.println();
			// }
			if (parentNode != null) {
				
				treeModel.insertNodeInto(treeNode, parentNode, parentNode
						.getChildCount());
				parentNode.setHasLoadSubNode(treeNode.isLeaf());
			}

		}
		hasLoadSubNode = true;
	}
	
	

	public IufoLazyTreeNodeModel getNodeModel() {
		return nodeModel;
	}

	public void setNodeModel(IufoLazyTreeNodeModel nodeModel) {
		this.nodeModel = nodeModel;
	}
}
