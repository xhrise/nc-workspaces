package com.ufida.report.anareport.applet;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.ui.bi.query.manager.RptProvider;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITree;
import nc.vo.iufo.datasetmanager.DataSetDefVO;

import com.ufida.dataset.metadata.Field;
import com.ufida.report.rep.model.QueryModelTreeDragSource;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.component.RepToolBarPanelUI;

/**
 * 分析报表左侧的导航面板 包括查询对象和常用信息 对托拽源采用授权方式实现
 * 
 */
public class AnaDataSetPanel extends UIPanel {

	private static final long serialVersionUID = 1L;

	private JTree commonInfoTree = null;

	private JTree queryModelTree = null; // @jve:decl-index=0:visual-constraint="230,10"

	private transient Hashtable<String,JPopupMenu> rightMenus=new Hashtable<String,JPopupMenu>();
	private JPanel northPanel=null;
	private JScrollPane jScrollPane = null;

	private JScrollPane jScrollPane1 = null; // @jve:decl-index=0:visual-constraint="223,375"

	private JSplitPane jSplitPane = null; // @jve:decl-index=0:visual-constraint="295,77"

	/**
	 * This is the default constructor
	 */
	public AnaDataSetPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		BorderLayout gridLayout1 = new BorderLayout();
		this.setLayout(gridLayout1);
		this.setPreferredSize(new java.awt.Dimension(180, 200));
		this.setSize(new java.awt.Dimension(180, 200));
		this.add(getNorthPanel(), BorderLayout.NORTH);
		this.add(getJScrollPane(), BorderLayout.CENTER);
		initQueryModelTree();
	}
	
    public JPanel getNorthPanel(){
    	if(northPanel==null){
    		northPanel=new UIPanel(new FlowLayout(FlowLayout.TRAILING));
    		northPanel.setUI(new RepToolBarPanelUI());
    	}
    	return northPanel;
    }
	/**
	 * This method initializes jTree
	 * 
	 * @return javax.swing.JTree
	 */
	private JTree getCommonInfoTree() {
		if (commonInfoTree == null) {
			commonInfoTree = new UITree(createDefaultCommonInfoTreeModel());
			commonInfoTree.setPreferredSize(new java.awt.Dimension(50, 70));
			commonInfoTree.setName("commonInfoTree");
			commonInfoTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
//			initCommonInfoTree();
		}
		return commonInfoTree;
	}

	/**
	 * 初始化常用信息树 可以在次初始化常用信息树
	 */
//	protected void initCommonInfoTree() {
//		applyCommonInfoDragSource();
//	}

	// public void setQueryModels(QueryModelVO[] querys, boolean hasPrivateDim)
	// {
	// m_queryModelVOs = querys;
	// getQueryModelTree().setModel(createDefaultQueryModelTreeModel(querys,
	// hasPrivateDim));
	// initQueryModelTree();
	// }

	/**
	 * 添加parentObj作为一个子根节点，其余的字段都作为parentObj的子节点，
	 */
	public void addFieldNode(Object parentObj, Field[] biFields) {
		DefaultMutableTreeNode subRoot = new DefaultMutableTreeNode(parentObj);
		if (biFields != null) {
			for (int i = 0; i < biFields.length; i++) {
				subRoot.add(new DefaultMutableTreeNode(biFields[i]));
			}
		}
		DefaultTreeModel model = (DefaultTreeModel) getQueryModelTree().getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		model.insertNodeInto(subRoot, root, root.getChildCount());
		getQueryModelTree().expandPath(new TreePath(subRoot.getPath()));
	}

	public void addFieldNode(DefaultMutableTreeNode node) {
		((DefaultMutableTreeNode) getQueryModelTree().getModel().getRoot()).add(node);
		getQueryModelTree().expandPath(new TreePath(node.getPath()));
	}
	
    public void expandAll() {
		DefaultTreeModel model = (DefaultTreeModel) getQueryModelTree()
				.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		getQueryModelTree().expandPath(new TreePath(root.getPath()));
		for (int i = 0; i < root.getChildCount(); i++) {
			getQueryModelTree().expandPath(
					new TreePath(((DefaultMutableTreeNode) root.getChildAt(i))
							.getPath()));
		}
	}
    public void collapsePathAll(boolean containRoot) {
		DefaultTreeModel model = (DefaultTreeModel) getQueryModelTree()
				.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		for (int i = 0; i < root.getChildCount(); i++) {
			getQueryModelTree().collapsePath(
					new TreePath(((DefaultMutableTreeNode) root.getChildAt(i))
							.getPath()));
		}
		if(containRoot){
			getQueryModelTree().collapsePath(new TreePath(root.getPath()));
		}
		
	}
	/**
	 * 
	 * 对常用信息树应用托拽源 可以覆盖此方法来改变托拽源处理方式
	 */
//	public void applyCommonInfoDragSource() {
//		CommonInfoTreeDragSource source = new CommonInfoTreeDragSource(getCommonInfoTree());
//	}

//	public void setQueryModelTree(JTree tree){
//		queryModelTree = tree;
//	}
	
	/**
	 * This method initializes jTree1
	 * 
	 * @return javax.swing.JTree
	 * @i18n mbirep00013=字段导航
	 */
	public JTree getQueryModelTree() {
		if (queryModelTree == null) {

			queryModelTree = new UITree();
			queryModelTree.setName("queryModelTree");
			queryModelTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(StringResource
					.getStringResource("mbirep00013"))));
			queryModelTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
			queryModelTree.addMouseListener(new TreeMouseListener());
		}
		return queryModelTree;
	}

	private TreePath getNodePath(DataSetDefVO selNode) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) getQueryModelTree()
				.getModel().getRoot();
		Enumeration<DefaultMutableTreeNode> childrens = root.children();
		DefaultMutableTreeNode node = null;
		while (childrens.hasMoreElements()) {
			node = childrens.nextElement();
			if (node.getUserObject() instanceof DataSetDefVO)
				if (((DataSetDefVO) node.getUserObject()).getPk_datasetdef()
						.equals(selNode.getPk_datasetdef())) {
					return new TreePath(((DefaultTreeModel) getQueryModelTree()
							.getModel()).getPathToRoot(node));
				}
		}
		return null;
	}
	public void setSelectPath(DataSetDefVO selNode){
		if(selNode==null)
			return ;
		 getQueryModelTree().setSelectionPath(getNodePath(selNode));
	}
	/**
	 * 初始化查询对象树 可以在次初始化查询对象树
	 */
	protected void initQueryModelTree() {
		applyQueryModelDragSource();
	}

	/**
	 * 创建查询节点模型
	 * 
	 * @i18n mbirep00014=常用信息
	 * @i18n mbirep00015=公司名称
	 * @i18n mbirep00016=当前用户
	 * @i18n mbirep00017=系统时间
	 * 
	 */
	protected DefaultTreeModel createDefaultCommonInfoTreeModel() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(StringResource.getStringResource("mbirep00014"));

		root.add(new DefaultMutableTreeNode(StringResource.getStringResource("mbirep00015")));
		root.add(new DefaultMutableTreeNode(StringResource.getStringResource("mbirep00016")));
		root.add(new DefaultMutableTreeNode(StringResource.getStringResource("mbirep00017")));

		return new DefaultTreeModel(root);

	}

	/**
	 * 
	 * 对查询对象应用托拽源 可以覆盖此方法来改变托拽源处理方式
	 */
	public void applyQueryModelDragSource() {
		QueryModelTreeDragSource source = new QueryModelTreeDragSource(getQueryModelTree());
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane(getQueryModelTree());
			jScrollPane.setViewportView(getQueryModelTree());
			jScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane.setPreferredSize(new java.awt.Dimension(100, 200));
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new UIScrollPane(getCommonInfoTree());
			jScrollPane1.setViewportView(getCommonInfoTree());
			jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {

			jSplitPane = new UISplitPane();
			jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
			jSplitPane.setContinuousLayout(true);
			jSplitPane.setTopComponent(getJScrollPane());
			jSplitPane.setBottomComponent(getJScrollPane1());
			jSplitPane.setOneTouchExpandable(true);
			jSplitPane.setSize(169, 85);
			jSplitPane.setDividerSize(2);
			jSplitPane.setDividerLocation(200);
			jSplitPane.setPreferredSize(new java.awt.Dimension(75, 400));
			jSplitPane.setResizeWeight(0.5D);
		}
		return jSplitPane;
	}
	
	public JPopupMenu getPopUpMenu(String selClassName) {
		return rightMenus.get(selClassName);
	}
	/**
	 * 支持
	 * @param selTree
	 */
	public void initTreeRightMouseListener(String selClassName,JPopupMenu popMenu){
		rightMenus.put(selClassName, popMenu);
	}
	
	private class TreeMouseListener implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseReleased(MouseEvent e) {
			if (e.getSource() instanceof JTree) {
				JTree tree = (JTree) e.getSource();
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
				if (!e.isPopupTrigger()) {
					return;
				}
				if (selPath == null) {
					JPopupMenu popMenu = getPopUpMenu(AnaDataSetPanel.class.getName());
					if (popMenu != null) {
						popMenu.show(tree, e.getX(), e.getY());
					}
				} else {
					DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) selPath
							.getLastPathComponent();
					DataSetDefVO dvo = null;
					JPopupMenu popMenu = null;
					if (selNode != null) {
						tree.setSelectionPath(selPath);
						if (selNode.getUserObject() instanceof DataSetDefVO) {
							dvo = (DataSetDefVO) selNode.getUserObject();
						}
						if (dvo != null) {
							if (dvo.getDataSetDef().getProvider() instanceof RptProvider) {
								popMenu = getPopUpMenu(RptProvider.class
										.getName());
							} else {
								popMenu = getPopUpMenu(dvo.getClass().getName());
							}
						} else {
							popMenu = getPopUpMenu(selNode.getUserObject()
									.getClass().getName());
						}

						if (popMenu != null) {
							popMenu.show(tree, e.getX(), e.getY());
						}
					}
				}
			}

		}
		
	}
}
