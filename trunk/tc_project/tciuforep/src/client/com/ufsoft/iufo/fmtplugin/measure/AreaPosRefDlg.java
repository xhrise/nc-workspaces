package com.ufsoft.iufo.fmtplugin.measure;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.MeasureCache;
import nc.pub.iufo.cache.RepFormatModelCache;
import nc.pub.iufo.cache.ReportCache;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITree;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.table.CellPosition;
import com.ufsoft.report.util.MultiLang;

/**
 * 区域参照对话框
 * @author chxw
 */
public class AreaPosRefDlg extends UfoDialog implements ActionListener{
	static final long serialVersionUID = 3017621795235079366L;
	
	ReportCache reportCache = CacheProxy.getSingleton().getReportCache();
	MeasureCache measureCache = CacheProxy.getSingleton().getMeasureCache();
	KeyGroupCache keyGroupCache = CacheProxy.getSingleton().getKeyGroupCache();
	RepFormatModelCache formatCache = CacheProxy.getSingleton().getRepFormatCache();

	private ReportVO m_curReportVO = null;//当前的报表vo
	private ReportVO m_selReportVO = null;//树节点选中的报表。
	private KeyGroupVO m_curKeyGroupVO = null;//当前的关键字组合VO
	private ArrayList<CellPosition> excludeCellPosList = null; //需要排除的不能被引用的单元编号
	
	/**
	 * 操作用户, 3.1增加
	 */
	private String m_strUserPK = null;
	private boolean m_bRepMgr = true;
	private boolean isContainsCurrentReport = true;

	//节点图标
	public final static String[] ICONS = {"report.gif", "reportdir.gif",
			"reportroot.gif", "unit.gif"};
	public static final ImageIcon ICON_REPORT = ResConst
			.getImageIcon(ICONS[0]);
	public static final ImageIcon ICON_REPORTDIR = ResConst
			.getImageIcon(ICONS[1]);
	public static final ImageIcon ICON_REPORTROOTDIR = ResConst
			.getImageIcon(ICONS[2]);
	public static final ImageIcon ICON_UNIT = ResConst.getImageIcon(ICONS[3]);
	
	private JPanel jContentPane = null;
	private JPanel cmdPanel = null;
	private JButton refButton = null;
	private JButton closeButton = null;
	private JSplitPane jSplitPane = null;
	private JScrollPane leftScrollPane = null;
	private JTree leftTree = null;
	private AreaPosRefRightPanel rightAreaPosRefPanel = null;

	/***
	 * 
	 * @param parent
	 * @param currentRepVO
	 * @param currentKeyGroupVO
	 * @param strUserPK
	 * @param isContains 是否能使用本表的指标
	 * @param bRepMgr
	 */
	public AreaPosRefDlg(JDialog parent, ReportVO currentRepVO,
			KeyGroupVO currentKeyGroupVO, String strUserPK, boolean isContains,
			boolean bRepMgr) {
		this(parent, currentRepVO, currentKeyGroupVO, strUserPK,isContains, bRepMgr, new ArrayList());
	}
	/**
	 * 
	 * @param parent
	 * @param currentRepVO
	 * @param currentKeyGroupVO
	 * @param strUserPK
	 * @param isContains是否能使用本表的指标
	 * @param bRepMgr
	 * @param excludeCellPosList 需要排除的不能被引用的单元编号
	 */
	public AreaPosRefDlg(JDialog parent, ReportVO currentRepVO,
			KeyGroupVO currentKeyGroupVO, String strUserPK, boolean isContains,
			boolean bRepMgr, ArrayList<CellPosition> excludeCellPosList) {
		super(parent, "");
		if(excludeCellPosList != null){
			this.excludeCellPosList = excludeCellPosList;
		} else{
			this.excludeCellPosList = new ArrayList<CellPosition>();
		}
		init(currentRepVO, currentKeyGroupVO, strUserPK, isContains, bRepMgr);
	}
	
	private void init(ReportVO repVO, KeyGroupVO keyGroupVO, String strUserPK,
			boolean bContained, boolean bRepMgr) {
		this.m_curReportVO = repVO;
		this.isContainsCurrentReport = bContained;
		if (keyGroupVO != null) {
			this.m_curKeyGroupVO = CacheProxy.getSingleton()
					.getKeyGroupCache().getPkByKeyGroup(keyGroupVO);
		}
		this.m_strUserPK = strUserPK;
		this.m_bRepMgr = bRepMgr;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @i18n uiuforep00062=单元编号参照
	 */
	private void initialize() {
		this.setSize(724, 493);
		this.setContentPane(getJContentPane());
		setTitle(MultiLang.getString("uiuforep00062")); //"单元编号参照"
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new UIPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getCmdPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes cmdPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getCmdPanel() {
		if (cmdPanel == null) {
			cmdPanel = new UIPanel();
			cmdPanel.setLayout(new BoxLayout(cmdPanel, BoxLayout.X_AXIS));
			cmdPanel.add(Box.createRigidArea(new Dimension(5,0)));
			getJSplitPane().setRightComponent(getRightPanel());
			cmdPanel.add(Box.createHorizontalGlue());
			cmdPanel.add(getRefButton(), null);
			cmdPanel.add(Box.createRigidArea(new Dimension(5,0)));
			cmdPanel.add(getCloseButton(), null);
			cmdPanel.add(Box.createRigidArea(new Dimension(50,0)));
		}
		return cmdPanel;
	}

	private AreaPosRefRightPanel getRightPanelSample() {
		if (rightAreaPosRefPanel == null) {
			rightAreaPosRefPanel = new AreaPosRefRightPanelSample(this, isContainsCurrentReport, m_curKeyGroupVO, excludeCellPosList);
		}
		return rightAreaPosRefPanel;
	}	

	/**
	 * This method initializes refButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getRefButton() {
		if (refButton == null) {
			refButton = new UIButton();
			refButton.setName("BOK");
			refButton.setText(StringResource.getStringResource("miufo1000766")); //"参 照"
			if (this.getParent() instanceof DialogRefListener) {
				((DialogRefListener) this.getParent())
						.setRefDialogAndRefOper(this, refButton);
				if (((DialogRefListener) this.getParent()).getRefOper() != this.refButton) {
					refButton.setEnabled(false);
				}
			}
			refButton.addActionListener(this);
		}
		return refButton;
	}

	/**
	 * This method initializes closeButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCloseButton() {
		if (closeButton == null) {			
			closeButton = new nc.ui.pub.beans.UIButton();
			closeButton.setName("BCancel");
			closeButton.setText(StringResource
					.getStringResource("miufo1000764")); //"关 闭"
			closeButton.addActionListener(this);
		}
		return closeButton;
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */    
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new UISplitPane();
			jSplitPane.setOneTouchExpandable(true);
			jSplitPane.setLeftComponent(getLeftScrollPane());
			jSplitPane.setDividerLocation(180);
//			jSplitPane.setRightComponent(getRightPanel());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes leftScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getLeftScrollPane() {
		if (leftScrollPane == null) {
			leftScrollPane = new UIScrollPane();
			leftScrollPane.setViewportView(getLeftTree());
			leftScrollPane.setSize(300, 300);
		}
		return leftScrollPane;
	}

	/**
	 * This method initializes leftTree	
	 * 	
	 * @return javax.swing.JTree	
	 */    
	private JTree getLeftTree() {
		if (leftTree == null) {
			//初始化树模型
			MeasureTreeModel refModel = new MeasureTreeModel(m_curReportVO, m_curKeyGroupVO,
					isContainsCurrentReport, m_strUserPK, m_bRepMgr);
			//初始化根节点
			MeasRefTreeNode m_root = refModel.getRootNode();
			DefaultTreeModel m_treeModel = new DefaultTreeModel(m_root);
			leftTree = new UITree(m_treeModel) {
				private static final long serialVersionUID = -3094836342478796175L;
				public String getToolTipText(MouseEvent ev) {
					if (ev == null)
						return null;
					TreePath path = leftTree.getPathForLocation(ev.getX(), ev
							.getY());
					if (path != null) {
						DefaultMutableTreeNode node = getTreeNode(path);
						MeasRefTreeNode rnode;
						if (node instanceof MeasRefTreeNode)
							rnode = (MeasRefTreeNode) node;//.getObject();
						else
							rnode = (MeasRefTreeNode) node.getUserObject();
						if (rnode == null)
							return null;

						return ("ID:" + rnode.getPk());
					}
					return null;
				}
			};
//			leftTree.setSize(300, 300);
			ToolTipManager.sharedInstance().registerComponent(leftTree);
			leftTree.addTreeSelectionListener(new DirSelectionListener());
			leftTree.addTreeExpansionListener(new DirExpansionListener());
			TreeCellRenderer renderer = new MeasureIconCellRenderer();
			leftTree.setCellRenderer(renderer);
			leftTree.getSelectionModel().setSelectionMode(
					TreeSelectionModel.SINGLE_TREE_SELECTION);
			leftTree.setShowsRootHandles(true);
			leftTree.setRootVisible(true);
			leftTree.setEditable(false);
		}
		return leftTree;
	}

	public void dispose() {
		if (getResult() != ID_OK)
			setResult(ID_CANCEL);
		try {
			((DialogRefListener) getParent()).beforeDialogClosed(this);
		} catch (ClassCastException cce) {
		}
		super.dispose();
	}
	/**
	 * 此处插入方法描述。 创建日期：(2003-3-5 14:47:07)
	 */
	protected void customInit() {
	}

	/**
	 * 此处插入方法描述。 创建日期：(2003-8-27 16:56:25)
	 * 
	 * @return nc.vo.iufo.measure.MeasureVO
	 */
	public CellPosition getRefAreaPosition() {
		return getRightPanel().getSelectedCellPosition();
	}
	/**
	 * 返回指标,按如下格式 报表名称->指标编码 创建日期：(2003-9-19 10:53:07)
	 * 
	 * @return java.lang.String
	 */
	public String getStrRefCellPos() {
		return getRightPanel().getStrRefCellPos();		
	}
	public DefaultMutableTreeNode getTreeNode(TreePath path) {
		return (DefaultMutableTreeNode) (path.getLastPathComponent());
	}

	/**
	 * 响应按钮操作 创建日期：(2003-3-5 15:20:36)
	 * 
	 * @param event
	 *            java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent event) {
		if (event.getSource() == getRefButton()) {
			CellPosition selCellPos = getRightPanel().getSelectedCellPosition();			
			if(selCellPos != null){
				excludeCellPosList.add(selCellPos);
				setResult(ID_OK);
				if (!isContainsCurrentReport) {
					((DialogRefListener) getParent()).onRef(event);
				}
			}
		} else if (event.getSource() == getCloseButton()) {
			if (isContainsCurrentReport) {			
				setResult(ID_OK);
			} else {
				setResult(ID_CANCEL);
				((DialogRefListener) getParent()).beforeDialogClosed(this);
			}
			this.close();
		}
	}

	private AreaPosRefRightPanel getRightPanel() {
		return getRightPanelSample();
	}
	
	public void setResult(int n) {
		super.setResult(n);
	}
	
	class DirExpansionListener implements TreeExpansionListener {
		public void treeExpanded(TreeExpansionEvent event) {
		}

		public void treeCollapsed(TreeExpansionEvent event) {
		}
	}

	class DirSelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent event) {
			/**
			 * 当用户选择某一节点后，在此处根据报表id加在指标
			 */
			TreePath path = getLeftTree().getSelectionPath();
			if (path == null)
				return;
			DefaultMutableTreeNode node = getTreeNode(path);
			MeasRefTreeNode rnode;
			if (node instanceof MeasRefTreeNode)
				rnode = (MeasRefTreeNode) node;//.getObject();
			else
				rnode = (MeasRefTreeNode) node.getUserObject();

			if (rnode != null) {
				if (rnode.isLeaf()) {//报表
					String repid = rnode.getPk();//从节点中取得
					//装载报表
					ReportVO repvo = (ReportVO) reportCache.get(repid);
					if (repvo == null) {
						return;
					}
					m_selReportVO = repvo;
					getRightPanel().changeReport(repvo);
				}
			}		
		}
	}

}
 