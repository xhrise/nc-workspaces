/*
 * Created on 2005-6-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.rep.applet;

import java.awt.GridLayout;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITree;
import nc.vo.bi.query.manager.MetaDataVO;

import com.ufida.report.rep.model.CommonInfoTreeDragSource;
import com.ufida.report.rep.model.IBIField;
import com.ufida.report.rep.model.QueryModelTreeDragSource;
import com.ufsoft.iufo.resource.StringResource;

/**
 * BI左侧的导航面板 包括查询对象和常用信息 对托拽源采用授权方式实现
 * 
 * @author caijie
 */
public class BINavigationPanel extends nc.ui.pub.beans.UIPanel {

	private static final long serialVersionUID = 1L;

	private JTree commonInfoTree = null;

	private JTree queryModelTree = null; // @jve:decl-index=0:visual-constraint="230,10"

	private JScrollPane jScrollPane = null;

	private JScrollPane jScrollPane1 = null; // @jve:decl-index=0:visual-constraint="223,375"

	private JSplitPane jSplitPane = null; // @jve:decl-index=0:visual-constraint="295,77"

//	private QueryModelVO[] m_queryModelVOs = null;
	

//	private boolean m_isShowPrivateDim = false;

	/**
	 * This is the default constructor
	 */
	public BINavigationPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridLayout gridLayout1 = new GridLayout(1, 1);
		this.setLayout(gridLayout1);
		this.setPreferredSize(new java.awt.Dimension(180, 200));
		this.setSize(new java.awt.Dimension(180, 200));
		this.add(getJScrollPane(), null);
		initQueryModelTree();
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
			initCommonInfoTree();
		}
		return commonInfoTree;
	}

	/**
	 * 初始化常用信息树 可以在次初始化常用信息树
	 */
	protected void initCommonInfoTree() {
		applyCommonInfoDragSource();
	}

//	public void setQueryModels(QueryModelVO[] querys, boolean hasPrivateDim) {
//		m_queryModelVOs = querys;		
//		getQueryModelTree().setModel(createDefaultQueryModelTreeModel(querys, hasPrivateDim));
//		initQueryModelTree();
//	}
	
	/**
	 * 添加parentObj作为一个子根节点，其余的字段都作为parentObj的子节点，
	 */
	public void addFieldNode(Object parentObj, IBIField[] biFields){
		DefaultMutableTreeNode subRoot = new DefaultMutableTreeNode(parentObj);		
		if(biFields != null){
			for(int i = 0; i < biFields.length; i++){
				subRoot.add(new DefaultMutableTreeNode(biFields[i]));
			}
		}
		DefaultTreeModel		model = (DefaultTreeModel)getQueryModelTree().getModel();
		DefaultMutableTreeNode	root = (DefaultMutableTreeNode)model.getRoot();
		model.insertNodeInto(subRoot, root, root.getChildCount());	
		getQueryModelTree().expandPath(new TreePath(subRoot.getPath()));
	}
	
	
	public void addFieldNode(DefaultMutableTreeNode node){		
		((DefaultMutableTreeNode)getQueryModelTree().getModel().getRoot()).add(node);	
		getQueryModelTree().expandPath(new TreePath(node.getPath()));
	}
	
	/**
	 * 创建查询节点模型
	 * 
	 */
//	protected DefaultTreeModel createDefaultQueryModelTreeModel(QueryModelVO[] querys, boolean hasPrivateDim) {
//		// TODO
//		// 多语言
//		if (querys != null) {
//			for (int i = 0; i < querys.length; i++) {
//				DefaultMutableTreeNode queryRoot = new DefaultMutableTreeNode(querys[i]);
//
//				MetaDataVO[] flds = QueryModelSrv.getSelectFlds(querys[i].getID());
//				if (flds == null || flds.length == 0) {
//
//				} else {
//					for (int j = 0; j < flds.length; j++) {
//						if (isValidField(flds[j], hasPrivateDim))
//							queryRoot.add(new DefaultMutableTreeNode(new DefaultReportField(flds[j])));
//					}
//				}
//				root.add(queryRoot);
//			}
//		}
//		return new DefaultTreeModel(root);
//	}
	
//	public QueryModelVO[] getQueryModels(){
//		return m_queryModelVOs;
//	}
	
	/**
	 * 
	 * 对常用信息树应用托拽源 可以覆盖此方法来改变托拽源处理方式
	 */
	public void applyCommonInfoDragSource() {
		CommonInfoTreeDragSource source = new CommonInfoTreeDragSource(getCommonInfoTree());
	}

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
			queryModelTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(StringResource.getStringResource("mbirep00013"))));
			queryModelTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);			
		}
		return queryModelTree;
	}

	/**
	 * 初始化查询对象树 可以在次初始化查询对象树
	 */
	protected void initQueryModelTree() {
		applyQueryModelDragSource();
	}



	private boolean isValidField(MetaDataVO field, boolean hasPrivateDim) {
		if (hasPrivateDim)
			return true;
		else {
			if (field.getDimflag() && (field.getPk_dimdef() == null || field.getPk_dimdef().length() == 0))
				return false;
		}
		return true;
	}

	/**
	 * 创建查询节点模型
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

//	/**
//	 * @return Returns the queryModelVO.
//	 */
//	private QueryModelVO getQueryModelVO() {
//		return queryModelVO;
//	}
} // @jve:decl-index=0:visual-constraint="149,56"
