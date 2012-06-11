package com.ufsoft.iufo.fmtplugin.measure;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;
/**
 * 新的指标参照对话框
 * @author zzl
 */
public class MeasureRefDlg extends UfoDialog implements ActionListener{
	private static final long serialVersionUID = -9044062379007326497L;
	protected static String TITLE_LIST = "uiuforef0001";//=列表方式
	protected static String TITLE_SAMPLE = "uiuforef0002";//=表样方式
	protected ReportCache reportCache = CacheProxy.getSingleton().getReportCache();
	MeasureCache measureCache = CacheProxy.getSingleton().getMeasureCache();
	KeyGroupCache keyGroupCache = CacheProxy.getSingleton().getKeyGroupCache();
	RepFormatModelCache formatCache = CacheProxy.getSingleton().getRepFormatCache();

	private ReportVO m_oCurrentVO = null;//当前的报表vo
	protected ReportVO _selReportVO = null;//树节点选中的报表。
	protected KeyGroupVO m_oCurrentKeyGroupVO = null;//当前的关键字组合VO
	protected ArrayList<MeasureVO> excludeMeasuresList = null; //需要排除的不能被引用的指标Key为measurePK
	
	/**
	 * 操作用户, 3.1增加
	 */
	private String m_strUserPK = null;
	private boolean m_bRepMgr = true;
	protected boolean isContainsCurrentReport = true;
	/**
	 * 参照中是否要包含引用指标, 502增加
	 */
	private boolean m_bIncludeRefMeasures = true;
	private boolean m_bSampleEnabled = true;

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
//	private JRadioButton listSelRadioButton = null;
//	private JRadioButton sampleSelRadioButton = null;
	private JTabbedPane rightTabPane = null;
	protected JButton refButton = null;
	protected JButton closeButton = null;
	private JSplitPane jSplitPane = null;
	private JScrollPane leftScrollPane = null;
	private JTree leftTree = null;
	protected MeasureRefRightPanel rightMeasureListPanel = null;
	protected MeasureRefRightPanel rightMeasureSamplePanel = null;


	/***
	 * 
	 * @param parent
	 * @param currentRepVO
	 * @param currentKeyGroupVO
	 * @param strUserPK
	 * @param isContains 是否能使用本表的指标
	 * @param bRepMgr
	 */
	public MeasureRefDlg(JDialog parent, ReportVO currentRepVO,
			KeyGroupVO currentKeyGroupVO, String strUserPK, boolean isContains,
			boolean bRepMgr, boolean bIncludeRefMeas) {
		this(parent, currentRepVO, currentKeyGroupVO, strUserPK,isContains, bRepMgr, bIncludeRefMeas, new ArrayList<MeasureVO>());
	}
	/**
	 * 
	 * @param parent
	 * @param currentRepVO
	 * @param currentKeyGroupVO
	 * @param strUserPK
	 * @param isContains是否能使用本表的指标
	 * @param bRepMgr
	 * @param excludeMeasures 需要排除的不能被引用的指标 需要排除的不能被引用的指标Key为measurePK
	 */
	public MeasureRefDlg(JDialog parent, ReportVO currentRepVO,
			KeyGroupVO currentKeyGroupVO, String strUserPK, boolean isContains,
			boolean bRepMgr,  boolean bIncludeRefMeas, ArrayList<MeasureVO> excludeMeasures) {
		super(parent, "");
		if(excludeMeasures != null)
			{
			this.excludeMeasuresList =excludeMeasures;
			}else{
				this.excludeMeasuresList = new ArrayList<MeasureVO>();
			}
		init(currentRepVO, currentKeyGroupVO, strUserPK, isContains, bRepMgr,  bIncludeRefMeas);
	}
	
	private void init(ReportVO repVO, KeyGroupVO keyGroupVO, String strUserPK,
			boolean bContained, boolean bRepMgr, boolean bIncludeRefMeas) {
		this.m_oCurrentVO = repVO;
		this.isContainsCurrentReport = bContained;
		if (keyGroupVO != null) {
			this.m_oCurrentKeyGroupVO = CacheProxy.getSingleton()
					.getKeyGroupCache().getPkByKeyGroup(keyGroupVO);
		}
		this.m_strUserPK = strUserPK;
		this.m_bRepMgr = bRepMgr;
		this.m_bIncludeRefMeasures = bIncludeRefMeas;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(724, 493);
		this.setContentPane(getJContentPane());
		setTitle(StringResource.getStringResource("miufo1000770")); //"指标参照"
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	protected JPanel getJContentPane() {
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
//			cmdPanel.add(getListSelRadioButton(), null);
//			cmdPanel.add(getSampleSelRadioButton(), null);
			ButtonGroup buttonGroup = new ButtonGroup();
//			buttonGroup.add(getListSelRadioButton());
//			buttonGroup.add(getSampleSelRadioButton());
//			getListSelRadioButton().setSelected(true);
			cmdPanel.add(Box.createHorizontalGlue());
			cmdPanel.add(getRefButton(), null);
			cmdPanel.add(Box.createRigidArea(new Dimension(5,0)));
			cmdPanel.add(getCloseButton(), null);
			cmdPanel.add(Box.createRigidArea(new Dimension(50,0)));

		}
		return cmdPanel;
	}

	private JTabbedPane getRightTabbedPane(){
		if(rightTabPane == null){
			rightTabPane = new JTabbedPane();
			rightTabPane.add(StringResource.getStringResource(TITLE_LIST), getRightPanelList());
			rightTabPane.add(StringResource.getStringResource(TITLE_SAMPLE), getRightPanelSample());
			rightTabPane.setSelectedIndex(0);
			rightTabPane.setEnabledAt(1, m_bSampleEnabled);
			rightTabPane.addChangeListener(new ChangeListener(){

				public void stateChanged(ChangeEvent e) {
					MeasureRefRightPanel rightPanel = getRightPanel();
					if(rightPanel != null)
						rightPanel.changeReport(_selReportVO);
				}
				
			});
		}
		return rightTabPane;
	}
	/**
	 * This method initializes listSelRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 * @i18n uiiufofmt00064=按指标选择
	 */    
//	protected JRadioButton getListSelRadioButton() {
//		if (listSelRadioButton == null) {
//			listSelRadioButton = new UIRadioButton();
//			listSelRadioButton.setText(StringResource.getStringResource("uiiufofmt00064"));
//			listSelRadioButton.setSelected(false);
//			listSelRadioButton.addItemListener(new ItemListener(){
//
//				public void itemStateChanged(ItemEvent e) {
//					if(getJSplitPane().getRightComponent() != getRightPanel()){
//						getJSplitPane().setRightComponent(getRightPanel());
//						getRightPanel().changeReport(_selReportVO);
//					}
//				}			
//			});
//		}
//		return listSelRadioButton;
//	}
	
	protected MeasureRefRightPanel getRightPanelSample() {
		if (rightMeasureSamplePanel == null) {
			rightMeasureSamplePanel = new MeasureRefRightPanelSample(this,isContainsCurrentReport,m_oCurrentKeyGroupVO,excludeMeasuresList, m_bIncludeRefMeasures);
		}
		return rightMeasureSamplePanel;
	}	
	
	/**
	 * This method initializes sampleSelRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 * @i18n uiiufofmt00065=按表样选择
	 */    
//	protected JRadioButton getSampleSelRadioButton() {
//		if (sampleSelRadioButton == null) {
//			sampleSelRadioButton = new UIRadioButton();
//			sampleSelRadioButton.setText(StringResource.getStringResource("uiiufofmt00065"));
//		}
//		return sampleSelRadioButton;
//	}

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
	protected JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new UISplitPane();
			jSplitPane.setOneTouchExpandable(true);
			jSplitPane.setLeftComponent(getLeftScrollPane());
			jSplitPane.setDividerLocation(180);
			jSplitPane.setRightComponent(getRightTabbedPane());
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
	protected JTree getLeftTree() {
		if (leftTree == null) {
			/**
			 * 初始化树
			 */
			//初始化树模型
			MeasureTreeModel refModel = new MeasureTreeModel(m_oCurrentVO, m_oCurrentKeyGroupVO,
					isContainsCurrentReport, m_strUserPK, m_bRepMgr);
			//初始化根节点
			MeasRefTreeNode m_root = refModel.getRootNode();
//			m_root.expand(m_root);

			DefaultTreeModel m_treeModel = new DefaultTreeModel(m_root);
			leftTree = new UITree(m_treeModel);
			// @edit by wangyga at 2009-8-24,上午09:50:05 去掉节点的提示信息
//			{
//				private static final long serialVersionUID = -3094836342478796175L;
//				public String getToolTipText(MouseEvent ev) {
//					if (ev == null)
//						return null;
//					TreePath path = leftTree.getPathForLocation(ev.getX(), ev
//							.getY());
//					if (path != null) {
//						DefaultMutableTreeNode node = getTreeNode(path);
//						MeasRefTreeNode rnode;
//						if (node instanceof MeasRefTreeNode)
//							rnode = (MeasRefTreeNode) node;//.getObject();
//						else
//							rnode = (MeasRefTreeNode) node.getUserObject();
//						if (rnode == null)
//							return null;
//
//						return ("ID:" + rnode.getPk());
//					}
//					return null;
//				}
//			};
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

	/**
	 * This method initializes rightPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	protected MeasureRefRightPanel getRightPanelList() {
		if (rightMeasureListPanel == null) {
			rightMeasureListPanel = new MeasureRefRightPanelList(this,isContainsCurrentReport,m_oCurrentKeyGroupVO,excludeMeasuresList, m_bIncludeRefMeasures);
		}
		return rightMeasureListPanel;
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
	public MeasureVO getRefMeasure() {
		return getRightPanel().getSelectedMeasureVO();
	}
	/**
	 * 返回指标,按如下格式 报表名称->指标编码 创建日期：(2003-9-19 10:53:07)
	 * 
	 * @return java.lang.String
	 */
	public String getStrRefMeasure() {
		return getRightPanel().getStrRefMeasure();		
	}
	public DefaultMutableTreeNode getTreeNode(TreePath path) {
		return (DefaultMutableTreeNode) (path.getLastPathComponent());
	}

	/**
	 * 响应按钮操作 创建日期：(2003-3-5 15:20:36)
	 * 
	 * @param event
	 *            java.awt.event.ActionEvent
	 * @i18n miufo00113=请选择指标
	 */
	public void actionPerformed(java.awt.event.ActionEvent event) {
		if (event.getSource() == getRefButton()) {
			MeasureVO selVO = getRightPanel().getSelectedMeasureVO();
			if(selVO == null){
				UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo00113"), this);
				return;
			}				
			if(selVO != null){
				excludeMeasuresList.add(selVO);
				setResult(ID_OK);
				if (!isContainsCurrentReport) {
					if(getParent() instanceof DialogRefListener)
						((DialogRefListener) getParent()).onRef(event);
				}
			}
			if(isContainsCurrentReport){
				setResult(ID_OK);
				this.close();
				return;
			}

		} else if (event.getSource() == getCloseButton()) {
//			if (isContainsCurrentReport) {			
//				setResult(ID_OK);
//			} else {
				setResult(ID_CANCEL);
				if(getParent() instanceof DialogRefListener)
		            ((DialogRefListener) getParent()).beforeDialogClosed(this);
//				((DialogRefListener) getParent()).beforeDialogClosed(this);
//			}
			this.close();
		}
	}


	
	protected MeasureRefRightPanel getRightPanel() {
		Component rightPanel = getRightTabbedPane().getSelectedComponent();
		if(rightPanel instanceof MeasureRefRightPanel)
			return (MeasureRefRightPanel)rightPanel;
		return null;
//		if(getListSelRadioButton().isSelected()){
//			return getRightPanelList();
//		}else{
//			return getRightPanelSample();
//		}		
	}



	//节点编辑器
	class DirExpansionListener implements TreeExpansionListener {
		public void treeExpanded(TreeExpansionEvent event) {
		}

		public void treeCollapsed(TreeExpansionEvent event) {
		}
	}

	class DirSelectionListener implements TreeSelectionListener {


		public void valueChanged(TreeSelectionEvent event) {
			doTreeValueChanged();
		}
	}

	public void setResult(int n) {
		super.setResult(n);
	}
	protected boolean isIncludeRefMeasures(){
		return m_bIncludeRefMeasures;
	}
	protected void doTreeValueChanged(){
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
				_selReportVO = repvo;
				getRightPanel().changeReport(repvo);
			}
		}		
	
	}
	protected boolean isSampleEnabled(){
		return m_bSampleEnabled;
	}
	protected void setIsSampleEnabled(boolean isEnable){
		m_bSampleEnabled = isEnable;
		rightTabPane.setEnabledAt(1, m_bSampleEnabled);
	}
	protected boolean isCurrShowList(){
		return getRightTabbedPane().getSelectedIndex() == 0;
	}
	protected void setCurrSelList(){
		getRightTabbedPane().setSelectedIndex(0);
	}
	protected void setTabComponent(int index, Component comp, String title){
		if(index != 0)
			return;
		getRightTabbedPane().removeTabAt(index);
		getRightTabbedPane().insertTab(title, null, comp, title, index);
		setCurrSelList();
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
  