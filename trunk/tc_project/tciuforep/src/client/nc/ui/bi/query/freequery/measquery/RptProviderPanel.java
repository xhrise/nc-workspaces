package nc.ui.bi.query.freequery.measquery;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.itf.iufo.exproperty.IExPropConstants;
import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.ui.bi.query.manager.RptProvider;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.exproperty.ExPropOperator;
import nc.ui.iufo.exproperty.IExPropOperator;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.dsmanager.AbstractProviderPanel;
import nc.vo.iufo.exproperty.ExPropertyVO;
import nc.vo.iufo.measure.MeasureQueryModelDef;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.measure.UnitExInfoVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iuforeport.rep.ReportVO;
import nc.vo.pub.dsmanager.DataSetDesignObject;

import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.free.IRptProviderCreator;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.freequery.CreateMeasQueryDesigner;
import com.ufsoft.iufo.fmtplugin.freequery.MultiSelMeasureRefRightPanelList;
import com.ufsoft.iufo.fmtplugin.freequery.MultiSelMeasureRefRightPanelSample;
import com.ufsoft.iufo.fmtplugin.freequery.UniqueList;
import com.ufsoft.iufo.fmtplugin.freequery.UnitExInfoPanel;
import com.ufsoft.iufo.fmtplugin.measure.MeasRefTreeNode;
import com.ufsoft.iufo.fmtplugin.measure.MeasureRefDlg;
import com.ufsoft.iufo.fmtplugin.measure.MeasureRefRightPanel;
import com.ufsoft.iufo.fmtplugin.measure.MeasureTreeModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.sysprop.ui.ISysProp;
import com.ufsoft.iufo.sysprop.ui.SysPropMng;
import com.ufsoft.iufo.sysprop.vo.SysPropVO;
import com.ufsoft.report.util.UfoPublic;

public class RptProviderPanel extends AbstractProviderPanel {

	private static final long serialVersionUID = 1L;

	/** 指标参照Pane,包括左树右表两部分*/
	private JSplitPane jSplitPane = null;
	/** 左边报表树Panel*/
	private JScrollPane leftScrollPane = null;
	/** 报表树*/
	private JTree leftTree;
	
	/** 右表的TabbedPane*/
	private JTabbedPane rightTabPane = null;
	protected static String TITLE_LIST = "uiuforef0001";//=列表方式
	protected static String TITLE_SAMPLE = "uiuforef0002";//=表样方式
	/** 列表Panel*/
	protected MultiSelMeasureRefRightPanelList rightMeasureListPanel = null;
	/** 表样Panel*/
	protected MeasureRefRightPanel rightMeasureSamplePanel = null;
	/** 单位额外信息*/
	private UnitExInfoPanel m_unitPanel = null;
	
	/** 指标操作Panel,包括操作Panel和结果列表*/
	private JPanel m_measOperPanel = null;
	private JPanel m_measCmdPanel = null;
	private JButton m_btnListAdd = null;
	private JButton m_btnListRemove = null;
	private JButton m_btnListUp = null;
	private JButton m_btnListDown = null;
	private JButton m_btnListTop = null;
	private JButton m_btnListBottom=null;
	
	private JScrollPane  m_measListPanel= null;
	private JScrollPane jScrollPaneUnit = null;
	private static String TITLE_UNITINFO = "miufoiufoddc012";
	private UniqueList m_measList = null;
	private DefaultListModel m_measListModel = null;
	
	UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();
	ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
	KeyGroupCache keyGroupCache = IUFOUICacheManager.getSingleton().getKeyGroupCache();
	private ReportVO m_selReportVO=null;
	private String m_selKeyCombPk=null;//用来限制多次add(先list后sample)的时候所选择的指标关键字组合不同,一次add的时候MultiSelMeasureTableModel.updateSelectedMeasure会限制
	protected ArrayList<MeasureVO> excludeMeasuresList = new ArrayList<MeasureVO>(); //需要排除的不能被引用的指标Key为measurePK
	
	protected boolean isContainsCurrentReport = true;
	private boolean m_bIncludeRefMeasures = true;
	private boolean m_bSampleEnabled = true;
	private boolean m_bRepMgr = true;
	
	private Context ufoContext=null;
	//创建指标树要用到
	private String m_strUserPK = null;
	//创建查询模型要用到单位信息
	private String orgPK=null;
	private String m_strUnitValue=null;//单位级次编码
	//以上三者是必须的
	private MeasureQueryModelDef mqm=null;
	private boolean isDirty=false;
	
	
	/**
	 * Create the panel
	 */
	public RptProviderPanel(DataSetDesignObject dsdo) {
		super(dsdo, RptProvider.class.getName());
		setLayout(null);
		
		RptProvider rptProvider=null;
		if(dsdo.getCurDataSetDef()!=null){
			
			if(dsdo.getCurDataSetDef().getDataSetDef().getProvider() instanceof RptProvider){
				rptProvider=(RptProvider)dsdo.getCurDataSetDef().getDataSetDef().getProvider();
			
			}
			if(rptProvider!=null){
				this.mqm=rptProvider.getMeasQeuryModelDef();
			}
			isDirty=false;
			
		}
		
        this.ufoContext=dsdo.getContext();
        initData();
           
        //修改时将context付给Provider
        if(rptProvider!=null&&this.ufoContext!=null){
        	rptProvider.setContext(ufoContext);
        }
        initUI();
		
	}

	/**
	 * 校验
	 * @i18n miufo00113=请选择指标
	 */
	public String check() {
		String error=null;
		if(this.getMeasureQueryModel()==null||this.getMeasureQueryModel().getMeasures()==null){
			error=StringResource.getStringResource("miufo00113");
		}
		return error;
	}

	public boolean canFinish() {
		return true;
	}


	protected Object getProviderConstructorParameter() {
		Object[] parames=new Object[]{getMeasureQueryModel(),this.ufoContext};
		return parames;
	}
	
	private void initUI(){
		setLayout(new BorderLayout());
		add(getJSplitPane(),BorderLayout.CENTER);
		add(getMeasOperPanel(),BorderLayout.EAST);
		addUnitInfo();
	}
	
	/**
	 * @i18n miufo00401=不允许新加指标与已加指标关键字组合不同
	 */
	private void initData(){
		
		String taskId=null;
		if (ufoContext != null) {
			m_strUserPK=(String)ufoContext.getAttribute(IUfoContextKey.CUR_USER_ID);
			String m_strUnitId=(String)ufoContext.getAttribute(IUfoContextKey.LOGIN_UNIT_ID);
			if(orgPK==null){
				orgPK=IRptProviderCreator.COLUMN_ORGPK;
			}
			UnitInfoVO unitInfo = unitCache.getUnitInfoByPK(m_strUnitId);
			if(unitInfo!=null){
			   m_strUnitValue=unitInfo.getPropValue(orgPK);
			}
			
			if(mqm==null&&m_strUserPK!=null&&m_strUnitValue!=null){
				
				mqm=CreateMeasQueryDesigner.createDefaultQueryDef(null, taskId, m_strUnitValue, orgPK);
				//默认为个别报表
//				mqm.setVer(HBBBSysParaUtil.VER_SEPARATE);
				mqm.setTaskID(taskId);
				mqm.setQueryType(MeasureQueryModelDef.QUERY_FREE);
				SysPropVO sysProp = SysPropMng.getSysProp(ISysProp.HB_REPDATA_RELATING_TASK);
				if (sysProp != null && sysProp.getValue() != null && sysProp.getValue().equalsIgnoreCase("true"))
					mqm.setHBByTask(true);
			}
		}
		
		
		if(this.mqm!=null){
			MeasureVO[] selMeasures=mqm.getMeasures();
			boolean isAllAdd=true;
			if (selMeasures != null) {				
				for (MeasureVO meas : selMeasures) {					
					isAllAdd=isAllAdd&&addObjToSelList(meas);
				}
			}
			
			UnitExInfoVO[] selUnitInfo=mqm.getExInfoVOs();
			if (selUnitInfo != null) {
				for (UnitExInfoVO vo : selUnitInfo) {
					isAllAdd=isAllAdd&&addObjToSelList(vo);
				}
			}
			if(!isAllAdd){
				UfoPublic.sendWarningMessage(
						StringResource.getStringResource("miufo00401"), null);
				return ;
			}
		}
		
	}
	
	
	public MeasureQueryModelDef getMeasureQueryModel() {
		
		MeasureVO[] selVOs =getSelMeasureVOs();
		if (selVOs != null && selVOs.length > 0) {//没有选择任何指标，视同取消
			mqm.setExInfoVOs(getSelUnitexVOs());
			mqm.setMeasureDef(m_strUnitValue, orgPK, null, keyGroupCache.getByPK(m_selKeyCombPk), selVOs);
		}
		return mqm;
	}

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
	private JScrollPane getLeftScrollPane() {
		if (leftScrollPane == null) {
			leftScrollPane = new UIScrollPane();
			leftScrollPane.setViewportView(getLeftTree());
			leftScrollPane.setSize(300, 300);
		}
		return leftScrollPane;
	}
	private JTabbedPane getRightTabbedPane(){
		if(rightTabPane == null){
			rightTabPane = new JTabbedPane(){

				@Override
				public void setSelectedIndex(int index) {
					super.setSelectedIndex(index);
					if(index==1){
						getBtnAdd().setEnabled(true);
					}else{
						getBtnAdd().setEnabled(false);
					}
				}

				
				
			};
			rightTabPane.add(StringResource.getStringResource(TITLE_LIST), getRightPanelList());
			rightTabPane.add(StringResource.getStringResource(TITLE_SAMPLE), getRightPanelSample());
			rightTabPane.setSelectedIndex(0);
			rightTabPane.setEnabledAt(1, m_bSampleEnabled);
			rightTabPane.addChangeListener(new ChangeListener(){

				public void stateChanged(ChangeEvent e) {
					MeasureRefRightPanel rightPanel = getRightPanel();
					if(rightPanel != null)
						rightPanel.changeReport(m_selReportVO);
				}
				
			});
		}
		return rightTabPane;
	}
	
	protected MultiSelMeasureRefRightPanelList getRightPanelList() {
		if (rightMeasureListPanel == null) {
			rightMeasureListPanel = new MultiSelMeasureRefRightPanelList(null,
					isContainsCurrentReport, keyGroupCache.getByPK(m_selKeyCombPk), excludeMeasuresList,
					m_bIncludeRefMeasures){

						@Override
						protected void initSelectedAction(ChangeEvent e) {
							DefaultCellEditor editor = getCheckBoxEditor();
					        if (editor != null) {
					            Object value = editor.getCellEditorValue();
					            boolean isAdd=false;
					            if(value instanceof Boolean){
					            	if(((Boolean) value).booleanValue()){
					            		isAdd=addObjToSelList(getSelectedMeasureVO());
					            	}else{
					            		isAdd=removeObjFromSelList(getSelectedMeasureVO());
					            		
					            	}
					            	if(isAdd)
					            	isDirty=true;
					            }
					        }
						}
				
			};
		}
		return rightMeasureListPanel;
	}
	
	protected MeasureRefRightPanel getRightPanelSample() {
		if (rightMeasureSamplePanel == null) {
			rightMeasureSamplePanel = new MultiSelMeasureRefRightPanelSample(null,isContainsCurrentReport,keyGroupCache.getByPK(m_selKeyCombPk),excludeMeasuresList, m_bIncludeRefMeasures);
		}
		return rightMeasureSamplePanel;
	}
	protected JTree getLeftTree() {
		if (leftTree == null) {
			/**
			 * 初始化树
			 */
			//初始化树模型
			ReportVO initReportVO=null;
			if(ufoContext!=null){
				initReportVO=repCache.getByPK((String)ufoContext.getAttribute(IUfoContextKey.REPORT_PK));
			}
			if (initReportVO == null) {
				initReportVO = new ReportVO();
				initReportVO.setModel(false);
			}
			MeasureTreeModel refModel = new MeasureTreeModel(initReportVO, null,
					isContainsCurrentReport, m_strUserPK, m_bRepMgr);
			//初始化根节点
			MeasRefTreeNode m_root = refModel.getRootNode();
//			m_root.expand(m_root);

			DefaultTreeModel m_treeModel = new DefaultTreeModel(m_root);
			leftTree = new UITree(m_treeModel);
//			leftTree = new UITree(m_treeModel) {
//				
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
//						else if(node.getUserObject() instanceof MeasRefTreeNode){
//							rnode = (MeasRefTreeNode) node.getUserObject();
//						}else
//							rnode=null;
//						if (rnode == null)
//							return null;
//
//						return ("ID:" + rnode.getPk());
//					}
//					return null;
//				}
//
//			};
			ToolTipManager.sharedInstance().registerComponent(leftTree);
			
			leftTree.addTreeSelectionListener(new TreeSelectionListener(){

				public void valueChanged(TreeSelectionEvent arg0) {
					doTreeValueChanged();
					
				}
				
			});
			
			leftTree.getSelectionModel().setSelectionMode(
					TreeSelectionModel.SINGLE_TREE_SELECTION);
			leftTree.setShowsRootHandles(true);
			leftTree.setRootVisible(true);
			leftTree.setEditable(false);
		}
		return leftTree;
	}
	
	/**
	 * 为左边的树增加单位信息，并增加相应处理
	 */
	private void addUnitInfo() {
		DefaultTreeModel treeModel = (DefaultTreeModel) getLeftTree().getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
		DefaultMutableTreeNode unitInfoNode = new DefaultMutableTreeNode();
		unitInfoNode.setUserObject(StringResource.getStringResource(TITLE_UNITINFO));// miufoiufoddc012=单位信息
		root.insert(unitInfoNode, 0);
		treeModel.setRoot(root);

	}
	
	protected void doTreeValueChanged() {
		TreePath path = getLeftTree().getSelectionPath();
		if (path == null)
			return;
		DefaultMutableTreeNode node = getTreeNode(path);
		boolean isNowReport = (isSampleEnabled());
		if (node.getUserObject() instanceof String) {// 选择单位信息节点后，更新右边的列表显示，并且控制表样形式不可选
			if (isNowReport) {// 从报表切换到单位信息
				if (!isCurrShowList())
					setCurrSelList();// 先修改成列表显示
				setIsSampleEnabled(false);// 控制表样形式不可选
				changeUnitInfoPanel(true);
			}
		} else {
			if (!isNowReport) {// 从单位信息切换到报表
				setIsSampleEnabled(true);
				changeUnitInfoPanel(false);
			}
			MeasRefTreeNode rnode;
			if (node instanceof MeasRefTreeNode)
				rnode = (MeasRefTreeNode) node;//.getObject();
			else
				rnode = (MeasRefTreeNode) node.getUserObject();

			if (rnode != null) {
				if (rnode.isLeaf()) {//报表
					String repid = rnode.getPk();//从节点中取得
					//装载报表
					ReportVO repvo = (ReportVO) repCache.get(repid);
					m_selReportVO = repvo;
					if (repvo == null) {
						return;
					}
					getRightPanel().changeReport(repvo);
				}else{
					m_selReportVO=null;
				}
			}
		}
	}
	
	protected MeasureRefRightPanel getRightPanel() {
		Component rightPanel = getRightTabbedPane().getSelectedComponent();
		if(rightPanel instanceof MeasureRefRightPanel)
			return (MeasureRefRightPanel)rightPanel;
		return null;	
	}
	
	private DefaultMutableTreeNode getTreeNode(TreePath path) {
		return (DefaultMutableTreeNode) (path.getLastPathComponent());
	}
	
	private JPanel getMeasOperPanel() {
		if (m_measOperPanel == null) {
			m_measOperPanel = new JPanel();
			m_measOperPanel.setPreferredSize(new Dimension(200, 400));
			m_measOperPanel.setLayout(new BorderLayout());
			JLabel lblList = new JLabel();
			lblList.setText(StringResource.getStringResource("uiufomquery0006"));// uiufomquery0006=已选指标:
			m_measOperPanel.add(getMeasCmdPanel(), BorderLayout.WEST);
			m_measOperPanel.add(lblList, BorderLayout.NORTH);
			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout());
			centerPanel.add(lblList, BorderLayout.NORTH);
			centerPanel.add(getMeasListPanel(), BorderLayout.CENTER);

			m_measOperPanel.add(centerPanel, BorderLayout.CENTER);
		}
		return m_measOperPanel;
	}
	
	private JPanel getMeasCmdPanel() {
		if (m_measCmdPanel == null) {
			m_measCmdPanel = new UIPanel();
			Box cmdBox=new Box(BoxLayout.Y_AXIS);
			cmdBox.add(getBtnAdd());
			cmdBox.add(Box.createVerticalStrut(10));
			cmdBox.add(getBtnRemove());
			cmdBox.add(Box.createVerticalStrut(10));
			cmdBox.add(getBtnUp());
			cmdBox.add(Box.createVerticalStrut(10));
			cmdBox.add(getBtnDown());
			cmdBox.add(Box.createVerticalStrut(10));
			cmdBox.add(getBtnTop());
			cmdBox.add(Box.createVerticalStrut(10));
			cmdBox.add(getBtnBottom());
			m_measCmdPanel.add(cmdBox);
		}
		return m_measCmdPanel;
	}
	private JScrollPane getMeasListPanel() {
		if (m_measListPanel == null) {
			m_measListPanel = new UIScrollPane();
			m_measListPanel.setViewportView(getMeasureList());
		}
		return m_measListPanel;
	}
	
	private UniqueList getMeasureList() {
		if (m_measList == null) {
			m_measList = new UniqueList() {
				private static final long serialVersionUID = 1L;

				@Override
				public int getObjIndex(Object obj) {
					DefaultListModel model = (DefaultListModel) getModel();
					for (int i = 0; i < model.size(); i++) {
						Object elem = model.get(i);
						if (elem.equals(obj))
							return i;

					}
					return -1;
				}
			};
			m_measList.setModel(getMeasureListModel());
			
			m_measList.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					Object obj = m_measList.getSelectedValue();
					ReportVO measReportVO=null;
					if (obj instanceof UnitExInfoVO) {
						setIsSampleEnabled(false);// 控制表样形式不可选
						changeUnitInfoPanel(true);
						UnitExInfoVO selUnits =(UnitExInfoVO) obj ;
						getUnitInfoPanel().setRowSelectFromVO(selUnits);
						getUnitInfoPanel().setSelUnitInfo(getSelUnitexVOs());
					} else if (obj instanceof MeasureVO) {
						if (m_selReportVO != null
								&& m_selReportVO.getReportPK().equals(
										((MeasureVO) obj).getReportPK())) {
							
						} else {
							measReportVO = (ReportVO) repCache
									.get(((MeasureVO) obj).getReportPK());
							if (rightListToLeftTree(measReportVO)) {
								m_selReportVO = measReportVO;
							}
						}
						
						if (!isSampleEnabled()) {
							setIsSampleEnabled(true);
							changeUnitInfoPanel(false);
						}

						MeasureVO selMeas = (MeasureVO) obj;
						getRightPanelList().setRowSelectFromVO(selMeas);
						getRightPanelList()
								.setSelMeasureVOs(getSelMeasureVOs());
						getRightPanelList().repaint();
					}
				}

			});
		}
		return m_measList;
	}
	
	/**
	 * @i18n iufobi00010=指标所引用的报表不存在，请确认你是否有权限查看报表：
	 */
	private boolean rightListToLeftTree(ReportVO reportVo){
		if(reportVo==null){
			return false;
		}
//		ArrayList<MeasRefTreeNode> dirnodes=new ArrayList<MeasRefTreeNode>();
//		MeasRefTreeNode dirnode=null;
//		MeasRefTreeNode repnode=null;
//		String dirId=reportVo.getRepDir();
//		if(dirId!=null){
//			ReportDirCache  dirCache=IUFOUICacheManager.getSingleton().getReportDirCache();
//			ReportDirVO dirVO=dirCache.getReportDir(dirId);
//			boolean rootbeging=false;
//            while (dirVO != null) {
//				dirnode = new MeasRefTreeNode(MeasureRefDlg.ICON_REPORTDIR);
//				if(dirVO.getDirName().indexOf("_")>0){
//					dirnode.setName(StringResource.getStringResource("uiuforep000102"));
//					rootbeging=true;
//				}else{
//					dirnode.setName(dirVO.getDirName());
//				}
//				if(!rootbeging){
//				   dirnode.setPk(dirVO.getDirId());
//				}
//				dirnodes.add(0, dirnode);
//				if (dirVO.getParentDirPK() != null) {
//					dirVO = dirCache.getReportDir(dirVO.getParentDirPK());
//				} else {
//					dirVO = null;
//				}
//
//			}
//            
//			repnode= new MeasRefTreeNode(
//					MeasureRefDlg.ICON_REPORT);
//					repnode.setName(reportVo.getName());
//					repnode.setPk(reportVo.getReportPK());
//					repnode.setReportCode(reportVo.getCode());
//
//			if (dirnodes.size() > 0) {
//				dirnode = dirnodes.get(0);
//				for (int i = 1; i < dirnodes.size(); i++) {
//					dirnode.addSubNode(dirnodes.get(i));
//					dirnode=dirnodes.get(i);
//				}
//				dirnode.addSubNode(repnode);
//			}
//
//	        TreePath path=new TreePath(((DefaultTreeModel)getLeftTree().getModel()).getPathToRoot(repnode));						       
//			getLeftTree().setSelectionPath(path);
//		}
		TreePath path = getLeftTree().getSelectionPath();
		if(path!=null){
			DefaultMutableTreeNode node = getTreeNode(path);
			if(node instanceof MeasRefTreeNode){
				if(((MeasRefTreeNode)node).getPk().equals(reportVo.getReportPK())){
                     return false;
                }
			}
		}
		MeasRefTreeNode repnode= new MeasRefTreeNode(
		MeasureRefDlg.ICON_REPORT);
		repnode.setName(reportVo.getName());
		repnode.setPk(reportVo.getReportPK());
		repnode.setReportCode(reportVo.getCode());
		MeasRefTreeNode m_root=null;
		if(getLeftTree().getModel().getRoot() instanceof MeasRefTreeNode){
			m_root=(MeasRefTreeNode)getLeftTree().getModel().getRoot();
		}
		MeasRefTreeNode node=getTreeNode(m_root,repnode);
		if(node!=null){
			changeUnitInfoPanel(false);
			path=new TreePath(((DefaultTreeModel)getLeftTree().getModel()).getPathToRoot(node));						       
			getLeftTree().setSelectionPath(path);
			return true;
		}else{
			UfoPublic.sendWarningMessage(StringResource.getStringResource("iufobi00010")+repnode.toString(), this);
			return false;
		}
	}
	
	/**
	 * 从树模型中查找对应的节点
	 * @create by guogang at Feb 25, 2009,4:09:09 PM
	 *
	 * @param parent
	 * @param reportNode
	 * @return
	 */
	private MeasRefTreeNode getTreeNode(MeasRefTreeNode parent,MeasRefTreeNode reportNode){
		MeasRefTreeNode treeNode=null;
		if (parent != null) {
			if (parent.equals(reportNode)) {
				treeNode = parent;
			} else {
				Vector childrens = parent.getAllSubNode();
				if (childrens != null && childrens.size() > 0) {
					for (int i = 0; i < childrens.size(); i++) {
						if (childrens.get(i) instanceof MeasRefTreeNode) {
							treeNode = getTreeNode((MeasRefTreeNode) childrens
									.get(i), reportNode);
							if (treeNode != null)
								break;
						}
					}
				}
			}
		}
		return treeNode;
	}
	
	private DefaultListModel getMeasureListModel() {
		if (m_measListModel == null) {
			m_measListModel = new DefaultListModel();
		}
		return m_measListModel;
	}
	private JButton getBtnAdd() {
		if (m_btnListAdd == null) {
			m_btnListAdd = new JButton();
			m_btnListAdd.setText(StringResource.getStringResource("miufo1000950"));// miufo1000950=添加
			m_btnListAdd.addActionListener(new ListOperListener());
		}
		return m_btnListAdd;
	}

	private JButton getBtnRemove() {
		if (m_btnListRemove == null) {
			m_btnListRemove = new JButton();
			m_btnListRemove.setText(StringResource.getStringResource("miufo1001641")); // miufo1001641=删除
			m_btnListRemove.addActionListener(new ListOperListener());
		}
		return m_btnListRemove;
	}

	private JButton getBtnUp() {
		if (m_btnListUp == null) {
			m_btnListUp = new JButton();
			m_btnListUp.setText(StringResource.getStringResource("miufo1001650"));// miufo1001650=向上
			m_btnListUp.addActionListener(new ListOperListener());
		}
		return m_btnListUp;
	}

	private JButton getBtnDown() {
		if (m_btnListDown == null) {
			m_btnListDown = new JButton();
			m_btnListDown.setText(StringResource.getStringResource("miufo1001648"));// miufo1001648=向下
			m_btnListDown.addActionListener(new ListOperListener());
		}
		return m_btnListDown;
	}
	/**
	 * @i18n iufobi00001=置顶
	 */
	private JButton getBtnTop() {
		if (m_btnListTop == null) {
			m_btnListTop = new JButton();
			m_btnListTop.setText(StringResource.getStringResource("iufobi00001"));// miufo1001650=向上
			m_btnListTop.addActionListener(new ListOperListener());
		}
		return m_btnListTop;
	}

	/**
	 * @i18n iufobi00002=置底
	 */
	private JButton getBtnBottom() {
		if (m_btnListBottom == null) {
			m_btnListBottom = new JButton();
			m_btnListBottom.setText(StringResource.getStringResource("iufobi00002"));// miufo1001648=向下
			m_btnListBottom.addActionListener(new ListOperListener());
		}
		return m_btnListBottom;
	}
	
	private class ListOperListener implements ActionListener {

		/**
		 * @i18n miufo00401=不允许新加指标与已加指标关键字组合不同
		 */
		public void actionPerformed(ActionEvent e) {
			UniqueList list = getMeasureList();
			DefaultListModel model = (DefaultListModel) list.getModel();
			if (e.getSource() == getBtnAdd()) {
				boolean isUnitInfo = !isSampleEnabled();
				if (isUnitInfo) {
					UnitExInfoVO[] selUnitInfo = getUnitInfoPanel()
							.getSelPropVOs();
					if (selUnitInfo != null) {
						for (UnitExInfoVO vo : selUnitInfo) {
							addObjToSelList(vo);
						}
						isDirty = true;
					}
				} else {
					MeasureVO[] selMeasures = null;
					if (isCurrShowList()) {// 当前是列表选择
						selMeasures = getRightPanelList().getSelMeasureVOs();
					} else {// 表样选择
						selMeasures = getRightPanelSample().getSelMeasureVOs();
					}
					boolean isAllAdd = true;
					boolean isAdd = false;
					if (selMeasures != null && selMeasures[0] != null) {
						for (MeasureVO meas : selMeasures) {
							isAdd = addObjToSelList(meas);
							isAllAdd = isAllAdd && isAdd;
							if (isAdd) {
								isDirty = true;
							}
							
						}

					}
					if (!isAllAdd) {
						UfoPublic.sendWarningMessage(StringResource
								.getStringResource("miufo00401"), null);
						return;
					}
				}
			} else if (e.getSource() == getBtnRemove()) {
				int[] index = list.getSelectedIndices();

				if (index != null && index.length > 0) {
					for (int i = index.length - 1; i >= 0; i--) {
						if (removeObjFromSelList(model.elementAt(index[i]))) {
							isDirty = true;
						}
					}
					if (index[0] < model.size())
						list.setSelectedIndex(index[0]);
					else
						list.setSelectedIndex(model.size() - 1);

				}
			} else if (e.getSource() == getBtnUp()) {
				int[] index = list.getSelectedIndices();
				if (index != null && index.length > 0&&index[0]!=0) {
					for (int i = 0; i < index.length; i++) {
						Object obj = model.get(index[i]);
						model.remove(index[i]);
						model.insertElementAt(obj, index[i]-1);
						list.setSelectedIndex(index[i]-1);
					}
				}
			} else if (e.getSource() == getBtnDown()) {
				int[] index = list.getSelectedIndices();
				if (index != null && index.length > 0
						&& index[index.length - 1] != model.getSize() - 1) {
					for (int i = index.length - 1; i >= 0; i--) {
						Object obj = model.get(index[i]);
						model.remove(index[i]);
						model.insertElementAt(obj, index[i] + 1);
						list.setSelectedIndex(index[i] + 1);
					}
				}

			} else if (e.getSource() == getBtnTop()) {
				int[] index = list.getSelectedIndices();
				if (index != null && index.length > 0) {
					for (int i = 0; i < index.length; i++) {
						Object obj = model.get(index[i]);
						model.remove(index[i]);
						model.insertElementAt(obj, 0+i);
						list.setSelectedIndex(0);
					}
				}
			} else if (e.getSource() == getBtnBottom()) {
				int[] index = list.getSelectedIndices();
				if (index != null && index.length > 0) {
					ArrayList elems=new ArrayList();
					for (int i = index.length - 1; i >= 0; i--) {
						elems.add(0,model.get(index[i]));
						model.remove(index[i]);
					}
					for(int i=0;i<elems.size();i++){
						model.addElement(elems.get(i));
						list.setSelectedIndex(model.size() - 1);
					}
				}
			}
		}
	}
	
	private MeasureVO[] getSelMeasureVOs() {
		return (MeasureVO[]) getSelVOsFromList(true);
		
	}
	private Object[] getSelVOsFromList(boolean isMeasure) {
		DefaultListModel model = (DefaultListModel) getMeasureList().getModel();
		int size = model.getSize();
		if (size == 0)
			return null;
		if (isMeasure) {
			ArrayList<MeasureVO> al_obj = new ArrayList<MeasureVO>();
			for (int i = 0; i < size; i++) {
				Object obj = model.get(i);
				if (obj instanceof MeasureVO)
					al_obj.add((MeasureVO) obj);
			}
			return al_obj.toArray(new MeasureVO[0]);
		} else {
			ArrayList<UnitExInfoVO> al_obj = new ArrayList<UnitExInfoVO>();
			for (int i = 0; i < size; i++) {
				Object obj = model.get(i);
				if (obj instanceof UnitExInfoVO)
					al_obj.add((UnitExInfoVO) obj);
			}
			return al_obj.toArray(new UnitExInfoVO[0]);
		}
	}
	private UnitExInfoPanel getUnitInfoPanel() {
		if (m_unitPanel == null) {
			m_unitPanel = new UnitExInfoPanel(){

				@Override
				protected void initSelectedAction(ChangeEvent e) {
					TableCellEditor editor = getCheckBoxEditor();
			        if (editor != null) {
			            Object value = editor.getCellEditorValue();
			            boolean isAdd=false;
			            if(value instanceof Boolean){
			            	if(((Boolean) value).booleanValue()){
			            		isAdd=addObjToSelList(getSelectedVO());
			            	}else{
			            		isAdd=removeObjFromSelList(getSelectedVO());
			            	}
			            	if(isAdd)
			            	isDirty=true;
			            }
			        }
				
				}
				
			};

			// 得到所有的自定义属性
			try {
				IExPropOperator exPropOper = ExPropOperator.getExPropOper(IExPropConstants.EXPROP_MODULE_UNIT);
				ExPropertyVO[] vos = exPropOper.loadAllExProp("");
				if (vos != null && vos.length > 0) {
					ArrayList<UnitExInfoVO> al_unit = new ArrayList<UnitExInfoVO>();

					for (int i = 0; i < vos.length; i++) {
						if (vos[i].getUsedTag() == ExPropertyVO.USED_TAG_START&&!IRptProviderCreator.COLUMN_ORGPK.equals(vos[i].getDBColumnName()))
							al_unit.add(new UnitExInfoVO(vos[i]));
					}
					if (al_unit.size() > 0) {
						m_unitPanel.setUnitInfo(al_unit.toArray(new UnitExInfoVO[0]));
					}
				}
			
			} catch (Exception ex) {
				AppDebug.debug(ex);
			}
		}
		return m_unitPanel;
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
	
	private boolean addObjToSelList(Object obj) {
		if (obj instanceof MeasureVO) {
			if (m_selKeyCombPk == null) {
				m_selKeyCombPk = ((MeasureVO) obj).getKeyCombPK();
				getRightPanelList().setCurrentKeyGroupVO(
						keyGroupCache.getByPK(m_selKeyCombPk));
				getRightPanelSample().setCurrentKeyGroupVO(
						keyGroupCache.getByPK(m_selKeyCombPk));
			}
			if (!m_selKeyCombPk.equals(((MeasureVO) obj).getKeyCombPK())) {
				return false;
			}
		}
		if (!getMeasureListModel().contains(obj)){
			if (obj instanceof MeasureVO) {
				getRightPanelList().addSelectedMeasure((MeasureVO) obj);
			}
			 getMeasureListModel().addElement(obj);
		}
		
		return true;
	}
	
	private boolean removeObjFromSelList(Object obj) {
		boolean isOk = getMeasureListModel().removeElement(obj);
		if (isOk) {
			if(obj instanceof MeasureVO){
				getRightPanelList().removeSelectedMeasure((MeasureVO) obj);
			}else if(obj instanceof UnitExInfoVO){
				getUnitInfoPanel().removeSelectedPropVO((UnitExInfoVO)obj);
			}
			
		}
		if (getMeasureListModel().size() == 0) {
			m_selKeyCombPk = null;
			getRightPanelList().setCurrentKeyGroupVO(null);
			getRightPanelSample().setCurrentKeyGroupVO(null);
		}
		return isOk;
	}
	
	private UnitExInfoVO[] getSelUnitexVOs() {
		return (UnitExInfoVO[]) getSelVOsFromList(false);
	}
	
	private void setCurrSelList(){
		getRightTabbedPane().setSelectedIndex(0);
	}
	private void changeUnitInfoPanel(boolean bUnit) {
		if (bUnit) {
			setTabComponent(0, getJScrollPaneUnit(), StringResource.getStringResource(TITLE_UNITINFO));
		} else {
			setTabComponent(0, getRightPanelList(), StringResource.getStringResource(TITLE_LIST));
		}
	}
	private JScrollPane getJScrollPaneUnit() {
		if (jScrollPaneUnit == null) {
			jScrollPaneUnit = new UIScrollPane();
			jScrollPaneUnit.setViewportView(getUnitInfoPanel());
		}
		return jScrollPaneUnit;
	}
	protected void setTabComponent(int index, Component comp, String title){
		if(index != 0)
			return;
		getRightTabbedPane().removeTabAt(index);
		getRightTabbedPane().insertTab(title, null, comp, title, index);
		setCurrSelList();
	}
	
	public void setSelUnitexVOs(UnitExInfoVO[] vos) {
		if (vos != null) {
			for (UnitExInfoVO vo : vos)
				addObjToSelList(vo);
		}
		
		getUnitInfoPanel().setSelUnitInfo(vos);
	}

	/**
	 * @i18n uiufofurl0071=查询引擎
	 * @i18n miufo00113=请选择指标
	 */
	@Override
	/**
	 * 校验是否定义了指标,并指定是否要进行级次Key的拆分
	 */
	public boolean completeStep() {
		//@edit by guogang at Feb 27, 2009 3:24:33 PM 解决修改的时候删除所有指标字段等同取消
		
		if(getSelMeasureVOs()==null||getMeasureQueryModel().getMeasures()==null||getSelMeasureVOs().length<1){
			MessageDialog.showWarningDlg(this, null, StringResource.getStringResource("miufo00113"));
			return false;
		}else{
			if(getMeasureQueryModel().getCodeVos().size()>0){
	              
			}
			
			return super.completeStep();
		}
		
	}

	@Override
	public boolean completeWizard() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getStepDescripton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initStep() {
		isDirty = false;
		
	}
	
	@Override
	public boolean isDirty() {
		return isDirty;
	}
	
}
  