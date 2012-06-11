package com.ufsoft.iufo.view.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import nc.ui.iufo.unit.ITreeDataSearcher;
import nc.ui.iufo.unit.UFOLazyTreeSearcher;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.tree.TreeNodeSearcher;

import com.ufsoft.report.UfoTree;

public class IufoLazyTree extends UfoTree {

	private static final long serialVersionUID = -3982951175099471514L;
	
	public IufoLazyTree(ITreeDataSearcher searcher) {
		super(true);
		constructTreeModel();
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				TreePath path = getPathForLocation(e.getX(), e.getY());

				if (path == null) {
					return;
				}

				IufoLazyTreeNode treeNode = (IufoLazyTreeNode) path
						.getLastPathComponent();
				if (SwingUtilities.isLeftMouseButton(e)) {
					treeNode.getNodeModel().getMouseEventHandler()
							.doOneClicked(treeNode.getNodeModel());
					if (e.getClickCount() > 1) {
						treeNode.getNodeModel().getMouseEventHandler()
								.doDoubleClicked(treeNode.getNodeModel());
					}
				} else if (SwingUtilities.isRightMouseButton(e)) {
					treeNode.getNodeModel().getMouseEventHandler()
							.doRightClicked(treeNode.getNodeModel());
				}
			}
		});

		TreeNodeSearcher m_nodeSearcher = new UFOLazyTreeSearcher(this,searcher);
		m_nodeSearcher.setInputHint(NCLangRes.getInstance().getStrByID("smcomm","UPP1005-000139"))/* @res "输入名称搜索:" */;
	}

	protected IufoLazyTreeNode getRootNode() {
		return (IufoLazyTreeNode) getModel().getRoot();
	}

	/**
	 * 构造树模型
	 */
	public void constructTreeModel() {
		// 虚拟根节点
		DefaultTreeModel model = new SortTreeModel(new IufoLazyTreeNode(
				"Root"));
		setModel(model);
		this.setRootVisible(false);
	}

	public void initTree(TreeDataLoader loader, Object[] params) {
		IufoLazyTreeNodeModel nodeModel = new IufoLazyTreeNodeModel();
		nodeModel.setDataLoader(loader);
		nodeModel.setLoadSubNodeParams(params);
		getRootNode().setNodeModel(nodeModel);

		// 处理初始展开级次
		TreePath path = new TreePath(((DefaultTreeModel) getModel())
				.getPathToRoot(getRootNode()));
		treeExpanded(path);
	}

	public void treeExpanded(TreeExpansionEvent event) {
		TreePath path = event.getPath();
		if (path == null) {
			return;
		}
		treeExpanded(path);
		super.treeExpanded(event);
	}

	private void treeExpanded(TreePath path) {
		// 点击展开图标、双击树节点时都会展开树
		IufoLazyTreeNode node = (IufoLazyTreeNode) path.getLastPathComponent();

		if (node == null) {
			return;
		}
		if (node.isLeaf()) {
			return;
		}

		node.expanded(getTreeModel());
		this.expandPath(path);

	}

	private DefaultTreeModel getTreeModel() {
		return (DefaultTreeModel) getModel();
	}

//	@Override
//	public void setSelectedTreeNode(String[] strSelNodePKs) {
//		
//		TreePath treePath = findPathByPk(new TreePath(getModel().getRoot()),
//				strSelNodePKs,0);
//		if (treePath == null) {
//			return;
//		}
//		this.setSelectionPath(treePath);
//		this.scrollPathToVisible(treePath);
//	}

	private TreePath findPathByPk(TreePath treePath, String[] strPks, int iPos) {
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath
				.getLastPathComponent();
		if (treeNode == null) {
			return null;
		}
		
		if(treeNode instanceof IufoLazyTreeNode){
			IufoLazyTreeNodeModel nodeModel = ((IufoLazyTreeNode) treeNode).getNodeModel();;
			if (nodeModel.getId() != null && nodeModel.getId().equals(strPks[strPks.length - 1])) {
				return treePath;
			} else {
				Enumeration<DefaultMutableTreeNode> e = treeNode.children();
				while (e.hasMoreElements()) {
					DefaultMutableTreeNode node = e.nextElement();
					IufoLazyTreeNodeModel model = null;
					if(node instanceof IufoLazyTreeNode){
						model = ((IufoLazyTreeNode) node).getNodeModel();
						if((model.getId() != null && !model.getId().equals(strPks[iPos]))){
							continue;
						}
					}
					TreePath path = treePath.pathByAddingChild(node);
					treeExpanded(path);
					int j = ++iPos;
					if(j > strPks.length -1){
						j = strPks.length -1;
					}
					path = findPathByPk(path, strPks,model.getId() == null ? 0 : j);
					if (path != null) {
						return path;
					}
				}
			}
		}
			
		
		return null;
	}
	
	public void setSelectedTreeNode(String[] strSelNodePKs){
		if (strSelNodePKs==null || strSelNodePKs.length<=0)
			return;
		
		DefaultTreeModel model=(DefaultTreeModel)getModel();
		DefaultMutableTreeNode root= (DefaultMutableTreeNode)model.getRoot();
		TreeNode node=findSelectedTreeNode(new DefaultMutableTreeNode[]{root},strSelNodePKs,0);
		if (node!=null){
			TreePath path=new TreePath(((DefaultTreeModel)getModel()).getPathToRoot(node));
			setSelectionPath(path);
			scrollPathToVisible(path);
		}
	}
	
	private TreeNode findSelectedTreeNode(DefaultMutableTreeNode[] parentNodes,String[] strSelNodePKs,int iPos){
		if (parentNodes==null || parentNodes.length<=0)
			return null;
		
		for (int i=0;i<parentNodes.length;i++){
			IufoLazyTreeNodeModel nodeModel = null;
			if(parentNodes[i] instanceof IufoLazyTreeNode){
				nodeModel = ((IufoLazyTreeNode) parentNodes[i]).getNodeModel();;
			}
	
			if (nodeModel.getId() != null && nodeModel.getId().equals(strSelNodePKs[iPos])==false)
				continue;
			if (iPos==strSelNodePKs.length-1)
				return parentNodes[i];
			
			doDynLoadTreeNodes((IufoLazyTreeNode)parentNodes[i]);
			
			int iSubCount=parentNodes[i].getChildCount();
			IufoLazyTreeNode[] subNodes=new IufoLazyTreeNode[iSubCount];
			for (int j=0;j<iSubCount;j++)
				subNodes[j]=(IufoLazyTreeNode)parentNodes[i].getChildAt(j);
				
			
			return findSelectedTreeNode(subNodes,strSelNodePKs,nodeModel.getId() == null ? 0 : ++iPos);
		}
		return null;
	}
	
	private void doDynLoadTreeNodes(IufoLazyTreeNode parentNode){
		parentNode.expanded(getTreeModel());
//		if (parentNode.isLeaf()==false && parentNode.isHasLoad()==false){
//			ITreeNodeData[] datas=m_dataLoader.getSubNodeDatas(parentNode.getDataObj());
//			if (datas!=null && datas.length>0){
//				for (int i=0;i<datas.length;i++){
//					IUFOTreeNode subNode=new IUFOTreeNode(datas[i]);
//					((DefaultTreeModel) getModel()).insertNodeInto(subNode,parentNode,parentNode.getChildCount());
//				}
//			}
//			parentNode.setHasLoad(true);
//		}
	}
	
}
