package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.ui.iufo.query.datasetmanager.DataSetManagerDlg;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.dsmanager.AbstractTreeForEdit;
import nc.ui.pub.dsmanager.BasicWizardStepPanel;
import nc.ui.pub.dsmanager.DatasetTree;
import nc.ui.pub.querytoolize.AbstractWizardListPanel;
import nc.ui.pub.querytoolize.WizardShareObject;
import nc.ui.pub.util.NCTreeNode;
import nc.vo.iufo.datasetmanager.DataSetDefVO;
import nc.vo.iufo.datasetmanager.DataSetDirVO;
import nc.vo.pub.core.ObjectNode;

import com.ufida.dataset.metadata.MetaData;
import com.ufsoft.report.UfoReport;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;

/*
 * 数据集设置面板.
 * Creation date: (2008-06-24 15:39:08)
 * @author: chxw
 */
public class DataSetFuncProviderPanel extends BasicWizardStepPanel implements
		ItemListener,IUfoContextKey {
	private static final long serialVersionUID = 1L;

	//当前报表
	private String ownerID = null;
	
	private String oldID = null;

	private UIButton ivjbtnMng = null;
	
	private UIButton ivjbtnEdit = null;
	
	private UIButton ivjbtnRefresh = null;
	
	private UIPanel ivjPnEast = null;
	
	private UIScrollPane ivjSclPnTree = null;

	// ID-节点哈希表
	private Hashtable<String, NCTreeNode> m_hashIdNode = null;
	
	// 数据集对象树对象
	private AbstractTreeForEdit m_tree = null;
	
	// 选中节点
	private ObjectNode m_selDataSetNode = null;
	private DataSetFuncDesignWizardListPn m_dswlp = null;
	
	/**
	 * 返回 ivjbtnMng 特性值。
	 * 
	 * @return UIButton
	 * @i18n miufo1001609=管理
	 */
	private UIButton getbtnMng() {
		if (ivjbtnMng == null) {
			try {
				ivjbtnMng = new UIButton();
				ivjbtnMng.setName("ivjbtnMng");
				ivjbtnMng.setText(StringResource.getStringResource("miufo1001609"));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjbtnMng;
	}
	
	/**
	 * 返回 ivjbtnEdit 特性值。
	 * 
	 * @return UIButton
	 * @i18n miufo1001396=修改
	 */
	private UIButton getbtnEdit() {
		if (ivjbtnEdit == null) {
			try {
				ivjbtnEdit = new UIButton();
				ivjbtnEdit.setName("ivjbtnEdit");
				ivjbtnEdit.setText(StringResource.getStringResource("miufo1001396"));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjbtnEdit;
	}
	
	/**
	 * 返回 ivjbtnRefresh 特性值。
	 * 
	 * @return UIButton
	 * @i18n miufo00848=更新
	 */
	private UIButton getbtnRefresh() {
		if (ivjbtnRefresh == null) {
			try {
				ivjbtnRefresh = new UIButton();
				ivjbtnRefresh.setName("ivjbtnRefresh");
				ivjbtnRefresh.setText(StringResource.getStringResource("miufo00848"));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjbtnRefresh;
	}
	
	/**
	 * 返回 SclPnTree 特性值。
	 * 
	 * @return UIScrollPane
	 */
	private UIScrollPane getSclPnTree() {
		if (ivjSclPnTree == null) {
			try {
				ivjSclPnTree = new UIScrollPane();
				ivjSclPnTree.setName("SclPnTree");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjSclPnTree;
	}

	/**
	 * 返回 PnEast 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getPnEast() {
		if (ivjPnEast == null) {
			try {
				ivjPnEast = new UIPanel();
				ivjPnEast.setName("PnEast");
				ivjPnEast.setPreferredSize(new Dimension(160, 10));
				ivjPnEast.setLayout(new GridBagLayout());

				GridBagConstraints constraintsbtnAdd = new GridBagConstraints();
				constraintsbtnAdd.gridx = 1;
				constraintsbtnAdd.gridy = 1;
				constraintsbtnAdd.ipadx = 50;
				constraintsbtnAdd.insets = new Insets(52, 20, 26, 20);
				getPnEast().add(getbtnMng(), constraintsbtnAdd);

				GridBagConstraints constraintsbtnEdit = new GridBagConstraints();
				constraintsbtnEdit.gridx = 1;
				constraintsbtnEdit.gridy = 2;
				constraintsbtnEdit.ipadx = 50;
				constraintsbtnEdit.insets = new Insets(52, 20, 26, 20);
				getPnEast().add(getbtnEdit(), constraintsbtnEdit);
				
				GridBagConstraints constraintsbtnRefresh = new GridBagConstraints();
				constraintsbtnRefresh.gridx = 1;
				constraintsbtnRefresh.gridy = 3;
				constraintsbtnRefresh.ipadx = 50;
				constraintsbtnRefresh.insets = new Insets(52, 20, 26, 20);
				getPnEast().add(getbtnRefresh(), constraintsbtnRefresh);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjPnEast;
	}
	
	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		exception.printStackTrace(System.out);
	}

	/**
	 * 初始化类。
	 */
	private void initialize() {
		try {
			setName("DataSetProviderPanel");
			setLayout(new BorderLayout());
			setSize(720, 480);
//			add(getPnEast(), "East");
			add(getSclPnTree(), "Center");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	/**
	 * DataSetFuncProviderPanel 工具化构造子注解
	 */
	public DataSetFuncProviderPanel(DataSetFuncDesignObject dsdo, AbstractWizardListPanel awlp) {
		super(dsdo);
		m_dswlp = (DataSetFuncDesignWizardListPn)awlp;
		String strRepId = m_dswlp.getContextVo().getAttribute(REPORT_PK) == null ? null : (String)m_dswlp.getContextVo().getAttribute(REPORT_PK);
		
//		String ownerID = 
//			DataSetFuncProviderPanel.this.getUfoReport().getContextVo().getContextId()==null?"":
//			DataSetFuncProviderPanel.this.getUfoReport().getContextVo().getContextId();
		this.ownerID = strRepId;
		initialize();
		initEvents();
		//填充数据集对象界面
		DataSetDefVO vo = getSharedObjectImpl().getCurDataSetDef();
		load(vo);
		if(vo != null) setSelectedNodeID(vo.getPk_datasetdef());		
	}

	public void initEvents() {
		getbtnMng().addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				String strRepId = m_dswlp.getContextVo().getAttribute(REPORT_PK) == null ? null : (String)m_dswlp.getContextVo().getAttribute(REPORT_PK);
				
//				String ownerID = 
//					DataSetFuncProviderPanel.this.getUfoReport().getContextVo().getContextId()==null?"":
//					DataSetFuncProviderPanel.this.getUfoReport().getContextVo().getContextId();
				DataSetManagerDlg dlg = new DataSetManagerDlg(DataSetFuncProviderPanel.this.getParent(),strRepId);
				dlg.showModal();
			}
		});

		getbtnEdit().addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {

			}
		});
	}
	
	/**
	 * 校验
	 */
	public String check() {
		return null;
	}

	public boolean canFinish() {
		return false;
	}

	public boolean completeWizard() {
		// 执行校验
		String strErr = check();
		if (strErr != null) {
//			MessageDialog.showWarningDlg(this,
//					NCLangRes.getInstance().getStrByID("10241201",
//							"UPP10241201-000099"), strErr);
			return false;
		}
		return true;
	}

	/**
	 * @i18n miufo00241=数据集
	 */
	public String getStepTitle() {
		return StringResource.getStringResource("miufo00241");
	}

	/**
	 * 初始化查询模型树
	 */
	private void initTree() {
		// 构造数据集树模型实例
		if (m_tree == null){
			m_tree = getEditTree(WizardShareObject.DSNAME_IUFO, ownerID);
			m_hashIdNode = m_tree.getHashIdNode();
			if (m_tree.getSelectionCount() == 0) {
				m_tree.setSelectionRow(0);
			}
		}
		// 显示树
		m_tree.expandRow(0);
		// 树行高
		m_tree.setRowHeight(22);
		m_tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		m_tree.setSelectionRow(0);
		getSclPnTree().setViewportView(m_tree);
	}

	/**
	 * 返回 Tree 特性值。
	 * 
	 * @return UITree
	 */
	public AbstractTreeForEdit getTree() {
		return m_tree;
	}
	
	/**
	 * 获得选中树节点
	 */
	public NCTreeNode getSelTreeNode() {
		NCTreeNode selNode = null;
		TreePath path = m_tree.getSelectionPath();
		if (path != null) {
			selNode = (NCTreeNode) path.getLastPathComponent();
		}
		return selNode;
	}

	/**
	 * 设置选中树节点
	 */
	public void setSelTreeNodeId(String id) {
		if (id != null && getHashIdNode().containsKey(id)) {
			NCTreeNode selNode = (NCTreeNode) getHashIdNode().get(id);
			m_tree.setSelectionPath(new TreePath(selNode.getPath()));
			m_tree.expandPath(new TreePath(selNode.getPath()));
			m_tree.updateUI();
		}
	}

	/**
	 * 获得选中非目录树节点（多选）
	 */
	public NCTreeNode[] getSelTreeNodes() {
		TreePath[] selPaths = m_tree.getSelectionPaths();
		int iLen = (selPaths == null) ? 0 : selPaths.length;
		ArrayList<NCTreeNode> alNode = new ArrayList<NCTreeNode>();
		for (int i = 0; i < iLen; i++) {
			NCTreeNode selNode = (NCTreeNode) selPaths[i]
					.getLastPathComponent();
			if (!isDir(selNode)) {
				alNode.add(selNode);
			}
		}
		NCTreeNode[] selNodes = alNode.toArray(new NCTreeNode[0]);
		return selNodes;
	}
	
	/**
	 * 是否目录
	 */
	public boolean isDir(NCTreeNode node) {
		if (node.getDataVO() == null
				|| node.getDataVO() instanceof DataSetDirVO) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获得ID-VO哈希表
	 */
	public Hashtable<String, NCTreeNode> getHashIdNode() {
		return m_hashIdNode;
	}
	
	/**
	 * 设置ID-VO哈希表
	 */
	public void setHashIdNode(Hashtable<String, NCTreeNode> hashIdNode) {
		m_hashIdNode = hashIdNode;
	}
	
	/**
	 * 获得选中的数据集节点ID
	 * 
	 */
	public String getSelectedNodeID() {
		// 获得选中节点
		NCTreeNode node = getSelTreeNode();
		if (node != null && !isDir(node)) {
			return node.getId();
		}
		return null;
	}

	/**
	 * 设置选中的数据集节点
	 * @param id
	 */
	public void setSelectedNodeID(String id) {
		setSelTreeNodeId(id);
	}

	public boolean isDirty() {
		String selNodeId = getSelectedNodeID();
		if (selNodeId != null && selNodeId.equals(oldID)) {
			MetaData meta = getDataSetVO().getDataSetDef().getMetaData();
			if (meta != null && meta.getFieldNum() == 0) {
				// 需加载
				return true;
			}
			// 未改变
			return false;
		}
		return true;
	}

	public void load(DataSetDefVO datasetVo) {
		// 初始化查询树
		initTree();
		setSelectedNodeID(oldID);
	}
	
	protected Object getProviderConstructorParameter() {
		String[] params = new String[] { getSelectedNodeID()};
		return params;
	}

	/**
	 * @i18n miufo00849=数据集函数设计
	 * @i18n miufo00850=请选择数据集对象
	 */
	@Override
	public boolean completeStep() {
		// 校验
		if (getSelectedNodeID() == null) {
			MessageDialog.showWarningDlg(this, StringResource.getStringResource("miufo00849"), StringResource.getStringResource("miufo00850"));
			return false;
		}
		boolean bCompleteStep = super.completeStep();
		NCTreeNode treeNode = getSelTreeNode();
		if(oldID == null || !oldID.equals(treeNode.getId())){
			DataSetDefVO datasetVO = new DataSetDefVO();
			datasetVO.setCode(treeNode.getNodeCode());
			datasetVO.setName(treeNode.getNodeName());
			datasetVO.setPk_datasetdef(treeNode.getId());
			if(getDataSetVO() != null){
				datasetVO.getDataSetDef().getMetaData().addField(
						getDataSetVO().getDataSetDef().getMetaData().getFields());	
			}
			setDataSetVO(datasetVO);
			oldID = getDataSetVO().getPk_datasetdef();
		}
		return bCompleteStep;
	}

	public DataSetDefVO getDataSetVO() {
		return getSharedObjectImpl().getCurDataSetDef();
	}

	public void setDataSetVO(DataSetDefVO dsdo) {
		getSharedObjectImpl().setCurDataSetDef(dsdo);
	}

	public DataSetFuncDesignObject getSharedObjectImpl() {
		return (DataSetFuncDesignObject) getWizardShareObject();
	}
	
	public AbstractTreeForEdit getEditTree(String dsName, String ownerID) {
		return DatasetTree.getInstance(dsName, ownerID, true, false);
	}
	
	public void itemStateChanged(ItemEvent e) {
		initTree();
	}
	
//	public UfoReport getUfoReport(){
//		return m_dswlp.getUfoReport();
//	}
	
	/**
	 * 获得选中节点
	 */
	public ObjectNode getSelNode() {
		return m_selDataSetNode;
	}
	
	/**
	 * 设置选中节点
	 */
	public void setSelNode(ObjectNode node) {
		m_selDataSetNode = node;;
	}

	public String getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}
	
}
 