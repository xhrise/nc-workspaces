package com.ufsoft.iufo.inputplugin.biz.file;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.pub.iufo.cache.TaskCache;
import nc.pub.iufo.cache.base.BDCacheManager;
import nc.pub.iufo.cache.base.UnitCache;
import nc.pub.iufo.cache.base.UserCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.repdataright.RepDataRightUtil;
import nc.ui.pub.beans.UIPanel;
import nc.vo.iufo.repdataright.RepDataRightVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iufo.user.UserInfoVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iufo.inputplugin.biz.InputAutoCalcPlugIn;
import com.ufsoft.iufo.inputplugin.biz.InputCheckPlugIn;
import com.ufsoft.iufo.inputplugin.biz.InputFilePlugIn;
import com.ufsoft.iufo.inputplugin.biz.RepSelectionPlugIn;
import com.ufsoft.iufo.inputplugin.biz.WindowNavUtil;
import com.ufsoft.iufo.inputplugin.biz.data.CheckResultExt;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNaviMenu;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigation;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.userrole.UserRoleFuncInfo;
import com.ufsoft.iuforeport.tableinput.TableSearchCondVO;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputException;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.re.IDName;
import com.ufsoft.table.re.IDNameToStringWrapper;

/**
 * @update by chxw 2007-09-05
 *        根据综合查询的业务需求，需要在报表树选择报表的基础上增加按单位查询，即
 *        在左侧提供本单位及其下属单位，根据单位查询报表
 * @end
 * 
 * 报表树面板
 * @author chxw
 * 2007-09-04
 */
public class ChooseRepPanel extends UIPanel implements IUfoContextKey{
	
	private JTree jTreeRep = null;
	
	private UfoReport m_ufoReport = null;
	
	private JScrollPane jScrollPane = null;	

	/**
     * 上方的查询条件面板
     */
    private ChooseCordPanel m_chooseCordPanel = null;
    
	/**
	 * 报表树数据模型
	 */
	private RepTreeModel m_repTreeModel = null;
	
	/**
	 * 单位树数据模型
	 */
	private UnitTreeModel m_unitTreeModel = null;
	
	/**
	 * 当前选择单位
	 */
	private String m_strOperUnit = null;
	
	/**
	 * 当前操作报表
	 */
	private String m_strOperRep  = null;
	
	/**
	 * ChooseRepPanel 构造子
	 * @param ufoReport
	 * @param repTreeModel
	 * @param unitTreeModel
	 */
	public ChooseRepPanel(UfoReport ufoReport, RepTreeModel repTreeModel, UnitTreeModel unitTreeModel){
		super();
		m_ufoReport = ufoReport;
		m_repTreeModel = repTreeModel;
		m_unitTreeModel = unitTreeModel;
		initialize();
	}

	/**
	 * 初始化树面板外观，尺寸及各组件布局及模型数据
	 * @return
	 */
	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.setRows(1);
		this.setLayout(gridLayout);
		this.setBorder(null);
		this.setSize(100, 400);
		this.add(getJScrollPane(), null);
		initTreeDatas();
		initTreeSelectionModel();
	}
	
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTreeRep());
		}
		return jScrollPane;
	}

	/**
	 * 初始化报表树组件	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getJTreeRep() {
		if (jTreeRep == null) {
			jTreeRep = new JTree();
		}
		return jTreeRep;
	}

	/**
	 * 初始化树控件的模型数据，包括树模型及初始选择树节点
	 * 
	 */
	private void initTreeDatas() {
		initTreeModel();
		initSelection();
	}
	
	/**
	 * 加载数据模型到树控件并刷新显示
	 * 
	 */
	private void initTreeModel() {
		TreeSelectionModel model = getJTreeRep().getSelectionModel();
		model.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		if(GeneralQueryUtil.isShowRepTree(m_ufoReport)){
			//删除单位树监听器
			if(unitMouseListener != null){
				getJTreeRep().removeMouseListener(unitMouseListener);
			}
			//如果没有可选报表，则清空导航菜单
			getJTreeRep().setModel(m_repTreeModel.getTreeModel());
//			if(!m_repTreeModel.hasReps()){
//				QueryNaviMenu menu = QueryNavigation.getSingleton().getCurWindow();
//				menu.clear();
//			}
		} else{
			//删除报表树监听器
			if(repMouseListener != null){
				getJTreeRep().removeMouseListener(repMouseListener);
			}
			getJTreeRep().setModel(m_unitTreeModel.getTreeModel());
		}
		m_strOperUnit = null;
		m_strOperRep  = null;
		
	}

	/**
	 * 设置树组件初始选中项，单位树选中用户查询单位，报表树选中用户查询报表
	 * @return
	 */
	private void initSelection(){
		if(GeneralQueryUtil.isShowRepTree(m_ufoReport)){
			initDefSelectionRep();
		} else{
			initDefSelectionUnit();
		}
		
	}
	
	/**
	 * 设置初始选中的报表
	 * 1、如果从打开报表入口进入，打开指定报表；
	 * 2、如果不是从打开报表入口进入，则打开第一张报表；
	 * 
	 * @return
	 */
	private void initDefSelectionRep(){
		TreeSelectionModel model = getJTreeRep().getSelectionModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) (getJTreeRep()
				.getModel().getRoot());
		
		//得到待打开报表的报表PK
		TableInputContextVO tableInputContextVO = (TableInputContextVO) m_ufoReport.getContextVo();
		Object tableInputTransObj = tableInputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
		String strSelRepPK = inputTransObj.getRepDataParam().getReportPK();
		
		//如果不是从选择报表入口进入，则打开第一张报表
		Object selRepNode = getInitSelRepNode(root, strSelRepPK);
		if(selRepNode != null){
			model.setSelectionPath(new TreePath(selRepNode));
		} else if(root.getChildCount()>0){
			Object objFirst = root.getFirstChild();
			model.setSelectionPath(new TreePath(objFirst));
		}
		
	}
	
	/**
	 * 设置初始选择报表节点
	 * 
	 * @param root
	 * @param strSelRepPK
	 * @return TreeNode
	 */
	private TreeNode getInitSelRepNode(DefaultMutableTreeNode root, String strSelRepPK){
		for(int index=0; index<root.getChildCount(); index++){
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)root.getChildAt(index);
			ChooseRepData objRepData = (ChooseRepData)childNode.getUserObject();
			if(objRepData.getReportPK().equals(strSelRepPK)){
				return childNode;
			}
		}
		return null;
	}
	
	/**
	 * 设置初始选中的查询单位
	 * 
	 */
	private void initDefSelectionUnit(){
		TreeSelectionModel model = getJTreeRep().getSelectionModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) (getJTreeRep()
				.getModel().getRoot());
		
		//得到已打开报表的操作单位PK
		TableInputContextVO tableInputContextVO = (TableInputContextVO) m_ufoReport.getContextVo();
		
		Object tableInputTransObj = tableInputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
		String strOperUnitPK = inputTransObj.getRepDataParam().getOperUnitPK();
		
		//在单位树上查找该单位节点
		Object selUnitNode = getSelectedUnitNode(root, "hz");
		if(selUnitNode != null){
			model.setSelectionPath(new TreePath(selUnitNode));
		} else if(root.getChildCount()>0){
			Object objFirst = root.getFirstChild();
			model.setSelectionPath(new TreePath(objFirst));
		}
		
	}
	
	/**
	 * 根据单位编码在单位树上查找该单位所在节点
	 * @param root
	 * @param strUnitID
	 * @return
	 */
	private TreeNode getSelectedUnitNode(DefaultMutableTreeNode root, String strUnitID){
		if(strUnitID == null){
			return null;
		}

		for(int index = 0; index < root.getChildCount(); index++){
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)root.getChildAt(index);
			IDNameToStringWrapper objUnitData = (IDNameToStringWrapper)childNode.getUserObject();
			IDName unit = (IDName)objUnitData.getValue();
			if(unit.getID().equals(strUnitID)){
				return childNode;
			}
			
			getSelectedUnitNode(childNode, strUnitID);
		}
		return null;
	}
	
	/**
	 * 设置树组件选择模型TreeSelectionModel事件处理
	 * 
	 */
	private void initTreeSelectionModel() {
		TreeSelectionModel model = getJTreeRep().getSelectionModel();
		model.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		if(GeneralQueryUtil.isShowRepTree(m_ufoReport)){
			if(repMouseListener == null){
				repMouseListener = new RepTreeMouseListener();
			}
			getJTreeRep().addMouseListener(repMouseListener);
		} else{
			if(unitMouseListener == null){
				unitMouseListener = new UnitTreeMouseListener();
			}
			getJTreeRep().addMouseListener(unitMouseListener);
		}

	}
	
	private RepTreeMouseListener  repMouseListener = null;
	private UnitTreeMouseListener unitMouseListener = null;
	
	///////////////////////////////////////////////////////////////////////////
	//报表树组件报表切换监听器
	class RepTreeMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			int selRow = getJTreeRep().getRowForLocation(e.getX(), e.getY());
			TreePath selPath = getJTreeRep().getPathForLocation(e.getX(), e.getY());
			if(selRow != -1) {
				if(e.getClickCount() == 1) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
					if (node.isRoot() == true) {
						return;
					}
		
					ChooseRepData obj = (ChooseRepData) node.getUserObject();
					m_strOperRep = obj.getReportPK();
					try {
						switchRep(obj.getReportPK());
					} catch (Exception e1) {
						// TODO: handle exception
					}
					// @edit by wangyga at 2009-1-21,下午03:45:22 报表切换时清楚状态栏显示的信息
					clearStatusBarMessage();				
				}
			}
		}
		
	}
	
	//单位树组件单位切换监听器
	class UnitTreeMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			int selRow = getJTreeRep().getRowForLocation(e.getX(), e.getY());
			TreePath selPath = getJTreeRep().getPathForLocation(e.getX(), e.getY());
			if(selRow != -1) {
				if(e.getClickCount() == 1) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
					IDNameToStringWrapper obj = (IDNameToStringWrapper) node.getUserObject();
					if(obj.getValue() != null){	
						m_strOperUnit = obj.getValue().getID();
						switchUnit(obj.getValue().getID());
					}
				}
			}
		}
		
	}
	///////////////////////////////////////////////////////////////////////////
	
	/**
	 * 切换报表
	 * 
	 * @param strNewReportPK 报表PK
	 * @return
	 * @throws Exception 
	 * @i18n uiuforep00129=查询关键字不得为空
	 * @i18n miufo01054=对不起,没有查看该报表数据的权限
	 */
	public void switchRep(String strNewReportPK){
		if(strNewReportPK == null)
			return;

		TableInputContextVO tableInputContextVO = (TableInputContextVO) m_ufoReport.getContextVo();
		Object tableInputTransObj = tableInputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
		IRepDataParam repDataParam = inputTransObj.getRepDataParam();
		String strOldRepPK = repDataParam.getReportPK();
		TableSearchCondVO searchCondVO = getChooseCordPanel().getTableSearchCondObj();
	
		if(!checkDataRight(searchCondVO.getStrOperUnitPK(),repDataParam,strNewReportPK)){
			UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo01054"), m_ufoReport);
			return;
		}
		
		//提示保存报表
		if(strOldRepPK != null && !isCommit())
			AbsIufoBizCmd.doComfirmSave(m_ufoReport, false);
		
		//设置新报表PK到传递参数中
		preChangeRep(strNewReportPK);
		
		inputTransObj.getRepDataParam().setReportPK(strNewReportPK);
		
		searchCondVO.setStrOperRepPK(strNewReportPK);
		boolean hasInput = GeneralQueryUtil.checkKeyInput(searchCondVO.getStrKeyValues());
		if(!hasInput){
			javax.swing.JOptionPane.showMessageDialog(m_ufoReport, MultiLang.getString("uiuforep00129"));
			return;
		}
		
		//打开新选择的报表
//		OpenRepCmd openRepCmd = new OpenRepCmd(m_ufoReport);
//		openRepCmd.setNeedCheckAloneID(false);
//		openRepCmd.setNeedCheckReportPK(false);
//		openRepCmd.execute(null);
//		openRepCmd.setNeedCheckAloneID(true);
//		openRepCmd.setNeedCheckReportPK(true);
		
		Boolean isGenralQuery = new Boolean(GeneralQueryUtil.isGeneralQuery(m_ufoReport.getContext()));
		IInputBizOper inputMenuOper = new TableInputSearchOper(m_ufoReport, new Object[]{searchCondVO, isGenralQuery});
		Object objReturn = inputMenuOper.performBizTask(ITableInputMenuType.BIZ_TYPE_SEARCH_REPDATA); 
		if(objReturn != null && objReturn.toString().length() > 0 && !objReturn.toString().equals("true")){
			javax.swing.JOptionPane.showMessageDialog(m_ufoReport, objReturn.toString());
			return;
		}
		
		//触发anchor_Change事件,以启动公式追踪功能
		WindowNavUtil.startFormulaTrace(m_ufoReport,null,true);
		
		regMenu();
	}
	
	/**
	 * add by wangyga 2008-8-11 切换报表时，校验数据权限
	 * @param strDataUnitCode:单位code
	 * @param repDataParam:报表数据对象
	 * @param strRepPk:报表PK
	 * @return
	 */
	private boolean checkDataRight(String strDataUnitCode,IRepDataParam repDataParam,String strRepPk){	
		if(strDataUnitCode == null)//和liulp确定，无关键字时，当作有权限
			return true;
        if(repDataParam == null)
        	return false;
        String strOrgPk = repDataParam.getOrgPK();
        String strUserPk = repDataParam.getOperUserPK();
        String strTaskPk = repDataParam.getTaskPK();
               
        TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
        TaskVO task=taskCache.getTaskVO(strTaskPk);
            
        UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();
        UnitInfoVO unitInfoVo = unitCache.getUnitInfoByCode(strDataUnitCode);
        if(unitInfoVo == null)
        	return true;
		String strDataUnitID = unitInfoVo.getPK();
		
		try {
			UserInfoVO userInfo = getUserInfoVO(strUserPk);
			String [] strRepIDs = RepDataRightUtil.loadRepsByRight(userInfo,
	                task, RepDataRightVO.RIGHT_TYPE_VIEW,
	                new String[]{strRepPk},strDataUnitID,strOrgPk);
	        if(strRepIDs == null || strRepIDs.length <= 0){
	        	return false;
	        }
		} catch (Exception e) {
			AppDebug.debug(e);
			return false;
		}
        
		return true;
	}
	
	/**
     * 获得包含用户功能权限信息的用户信息
     * @param strUserPK
     * @return
     * @throws TableInputException 
     */
    private UserInfoVO getUserInfoVO(String strUserPK) throws TableInputException{
        UserCache userCache = BDCacheManager.getUserCache(true);
        UserInfoVO userInfoVO = userCache.getUserById(strUserPK);
        if(userInfoVO == null){
            throw new TableInputException(StringResource.getStringResource("uiufo50tinput001"));//用户可能已被删除，请检查！
        }
        //获得并设置用户功能树节点和功能权限URL
        UserRoleFuncInfo userRoleFuncInfo = new UserRoleFuncInfo(
                userInfoVO.getID());
        try{
            userRoleFuncInfo.init();    
        } catch(Exception e){
            
        }                        
        userInfoVO.setUserRoleFuncInfo(userRoleFuncInfo);   
        return userInfoVO;
    }
    
	/**
	 * 切换单位
	 * 
	 * @param strNewUnitPK 报表编码
	 * @return
	 * @i18n uiuforep00129=查询关键字不得为空
	 */
	public void switchUnit(String strNewUnitPK) {
		//提示保存报表
		TableInputContextVO tableInputContextVO = (TableInputContextVO) m_ufoReport.getContextVo();
		
		Object tableInputTransObj = tableInputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
		String strOldRepPK = inputTransObj.getRepDataParam().getReportPK();
		if(strOldRepPK != null && !isCommit())
			AbsIufoBizCmd.doComfirmSave(m_ufoReport, false);
		
		//检查查询关键字是否为空
		TableSearchCondVO searchCondVO = getChooseCordPanel().getTableSearchCondObj(strNewUnitPK);
		String oldOperRepPK = searchCondVO.getStrOperRepPK();
		boolean hasInput = GeneralQueryUtil.checkKeyInput(searchCondVO.getStrKeyValues());
		if(!hasInput){
			javax.swing.JOptionPane.showMessageDialog(m_ufoReport, MultiLang.getString("uiuforep00129"));
			return;
		}
		
		Boolean isGenralQuery = new Boolean(GeneralQueryUtil.isGeneralQuery(m_ufoReport.getContext()));
		IInputBizOper inputMenuOper = new TableInputSearchOper(m_ufoReport, new Object[]{searchCondVO, isGenralQuery});
        Object objReturn = inputMenuOper.performBizTask(ITableInputMenuType.BIZ_TYPE_SEARCH_REPDATA); 
        if(objReturn != null && objReturn.toString().length() >0 && !objReturn.toString().equals("true")){
			javax.swing.JOptionPane.showMessageDialog(m_ufoReport, objReturn.toString());
			return;
		}
        
        //设置综合查询缺省条件
        getChooseCordPanel().setSelItemOfCordPanel(strNewUnitPK, oldOperRepPK);

		//触发anchor_Change事件,以启动公式追踪功能
		WindowNavUtil.startFormulaTrace(m_ufoReport,null,true);
		
        regMenu();
        
	}

	private void regMenu() {
		QueryNaviMenu menu = QueryNavigation.getSingleton().getCurWindow(this);
		if(menu == null){
			AppDebug.debug("menu is null!!!! call liuyy. ");
			return;
		}
//		menu.clear();
    	menu.add(m_ufoReport, "");
    	QueryNavigation.refreshMenu(m_ufoReport);
	}

	/**
	 * 报表是否已上报
	 * 
	 */
	public boolean isCommit() {
		InputFilePlugIn plugIn = (InputFilePlugIn)m_ufoReport.getPluginManager().getPlugin(InputFilePlugIn.class.getName());
		if(plugIn != null){
			IExtension[] exts = plugIn.getDescriptor().getExtensions();
			SaveRepDataExt saveRepDataExt = (SaveRepDataExt)exts[0];
	    	return !saveRepDataExt.isEnabledSelf();
		}
		return false;
		
	}
	
    /**
     * 设置是否显示报表树
     * @param ufoReport
     * @param showRepTree
     */
	public static void setShowRepTree(UfoReport ufoReport, boolean showRepTree) {
		TableInputContextVO inputContextVO = (TableInputContextVO)ufoReport.getContextVo();
    	inputContextVO.setAttribute(SHOW_REP_TREE, Boolean.valueOf(showRepTree));
	}
	
	/**
	 * 切换报表时，处理审核结果区域内容变化
	 * 
	 * @param strNewReportPK
	 * @return
	 */
	private void preChangeRep(String strNewReportPK){
		resetCheckArea(strNewReportPK,false);
		resetAutoCalcUtil();
		
	}
	/**
	 * 查询报表时，处理审核结果区域内容变化
	 * 
	 * @param strNewReportPK
	 * @return
	 */
	public void preChangeRep(){
		resetCheckArea(null,true);
		resetAutoCalcUtil();
		
	}
	
	/**
	 * 处理审核结果区域内容变化
	 * 
	 * @param strNewRepPK
	 * @return
	 */
	private void resetCheckArea(String strNewRepPK,boolean isFresh){
		InputCheckPlugIn checkPlugIn = (InputCheckPlugIn)m_ufoReport.getPluginManager().getPlugin(InputCheckPlugIn.class.getName());
		if(checkPlugIn!=null){
			IExtension[] extensions = checkPlugIn.getDescriptor().getExtensions();
			((CheckResultExt)extensions[0]).getResultPanel().changeReport(strNewRepPK,isFresh);
		}
	}
	
	/**
	 * 重新初始化自动计算工具类计算参数
	 * 
	 */
	private void resetAutoCalcUtil(){
		InputAutoCalcPlugIn autoCalcPlug=(InputAutoCalcPlugIn)m_ufoReport.getPluginManager().getPlugin(InputAutoCalcPlugIn.class.getName());
		if(autoCalcPlug!=null)
			autoCalcPlug.initCalcUtil();
	}
    
	/**
     * 返回综合查询条件面板
     * @return
     */
    public ChooseCordPanel getChooseCordPanel(){
    	if(m_chooseCordPanel == null){
    		RepSelectionPlugIn selectionPlugIn = (RepSelectionPlugIn)
    		m_ufoReport.getPluginManager().getPlugin(RepSelectionPlugIn.class.getName());
    		
    		ChooseCordExt chooseCordExt = (ChooseCordExt)(selectionPlugIn.getDescriptor().getExtensions()[1]);
    		m_chooseCordPanel = (ChooseCordPanel)chooseCordExt.getPanel();	
    	}
    	return m_chooseCordPanel;

    }
    
    /**
     * 设置导航树缺省选中节点
     *
     */
    private void setSelNodeOfNavTree(TreePath path){
    	JTree jNavTree = getJTreeRep(); 
    	jNavTree.scrollPathToVisible(path);
    	jNavTree.setSelectionPath(path);
    	jNavTree.expandPath(path);
    	
    }
    
    /**
     * 返回导航树上选中节点
     * @param strOperRepPK
	 * @param strOperUnitPK 
     * @return
     */
    private TreePath getTreePath(String strOperRepPK, String strOperUnitPK){
    	if(GeneralQueryUtil.isShowRepTree(m_ufoReport)){
    		String operRepPK = strOperRepPK;
    		DefaultMutableTreeNode root = (DefaultMutableTreeNode)m_repTreeModel.getTreeModel().getRoot();
    		return findTreePathByRepPK(root, operRepPK);
    	} else{
    		String operUnitPK = strOperUnitPK;
    		DefaultMutableTreeNode root = (DefaultMutableTreeNode)m_unitTreeModel.getTreeModel().getRoot();
    		if(root.getUserObject() != null){
    			IDNameToStringWrapper unit = (IDNameToStringWrapper)root.getUserObject();
    			if(unit.getValue().getID().equals(operUnitPK)){
    				return new TreePath(root.getPath());
    			}
    		}
    		return findTreePathByUnitPK(root, operUnitPK);
    	}
 
    }
    
    /**
     * 迭代查询选中报表
     * @param root
     * @param strRepCode
     * @return
     */
    private TreePath findTreePathByRepPK(DefaultMutableTreeNode root, String strRepPk){
    	DefaultMutableTreeNode node = null;
		int nCount = root.getChildCount();
		for(int index = 0; index < nCount; index++){
			node = (DefaultMutableTreeNode)root.getChildAt(index);
			if(node.getUserObject() != null){
				ChooseRepData rep = (ChooseRepData)node.getUserObject();
				if(rep.getReportPK().equals(strRepPk)){
					return new TreePath(node.getPath());
				}
			}
			if(node.getChildCount() > 0){
				return findTreePathByRepPK(node, strRepPk);
			}
		}
		
		return null;
    }
    
    /**
     * 迭代查询选中单位
     * @param root
     * @param strRepCode
     * @return
     */
    private TreePath findTreePathByUnitPK(DefaultMutableTreeNode root, String strUnitCode){
    	if(root == null){
    		return null;
    	}
    	
    	DefaultMutableTreeNode node = null;
		int nCount = root.getChildCount();
		for(int index = 0; index < nCount; index++){
			node = (DefaultMutableTreeNode)root.getChildAt(index);
			if(node.getUserObject() != null){
				IDNameToStringWrapper unit = (IDNameToStringWrapper)node.getUserObject();
				if(unit.getValue().getID().equals(strUnitCode)){
					return new TreePath(node.getPath());
				}
			}
			//如果有下级单位
			if(node.getChildCount() > 0){
				TreePath tmpTreePath = findTreePathByUnitPK(node, strUnitCode);
				if(tmpTreePath != null){
					return tmpTreePath;
				}
			}
		}
		
		return null;
    }
    
    /**
     * 报表切换时清楚状态栏显示的信息
     * @create by wangyga at 2009-1-21,下午03:45:58
     *
     */
    private void clearStatusBarMessage(){
    	m_ufoReport.getStatusBar().setHintMessage("");	
    }
    
	/**
	 * 切换树组件显示视图，报表树或单位树
	 *
	 */
	public void switchTreeView(){
		Boolean isShowRepTree = GeneralQueryUtil.isShowRepTree(m_ufoReport);
		setShowRepTree(m_ufoReport, !isShowRepTree.booleanValue());
		initTreeDatas();
		TableSearchCondVO searchCondVO = getChooseCordPanel().getTableSearchCondObj();
		setSelNodeOfNavTree(searchCondVO.getStrOperRepPK(), searchCondVO.getStrOperUnitPK());
		initTreeSelectionModel();
	}
	
	/**
	 * 设置导航树选中节点
	 * @param strOperRepPK
	 * @param strOperUnitPK
	 */
	public void setSelNodeOfNavTree(String strOperRepPK, String strOperUnitPK){
		TreePath treePath = getTreePath(strOperRepPK, strOperUnitPK);
		setSelNodeOfNavTree(treePath);
	}
	
	/**
	 * 是否选择导航树上的节点
	 * @return
	 */
	public boolean isSelectionNode(){
		return getJTreeRep().getSelectionRows() != null;
	}
	
	public RepTreeModel getRepTreeModel() {
		return m_repTreeModel;
	}

	public void setRepTreeModel(RepTreeModel treeModel) {
		m_repTreeModel = treeModel;
	}

	public UnitTreeModel getUnitTreeModel() {
		return m_unitTreeModel;
	}

	public void setUnitTreeModel(UnitTreeModel treeModel) {
		m_unitTreeModel = treeModel;
	}

	public String getOperUnit() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeRep.getSelectionPath().getLastPathComponent();
		if (node != null && node.getUserObject() != null && node.getUserObject() instanceof IDNameToStringWrapper) {
			IDNameToStringWrapper objUnitData = (IDNameToStringWrapper)node.getUserObject();
			IDName unit = (IDName)objUnitData.getValue();
			m_strOperUnit = unit.getID();
		}
		return m_strOperUnit;
	}

	public String getOperRep() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeRep.getSelectionPath().getLastPathComponent();
		if (node != null && node.getUserObject() != null && node.getUserObject() instanceof ChooseRepData) {
			ChooseRepData obj = (ChooseRepData) node.getUserObject();
			m_strOperRep = obj.getReportPK();
		}
		return m_strOperRep;
	}
	
	public String getRootUnit(){
		String strRootUnit = null;
		if(m_unitTreeModel != null){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) m_unitTreeModel.getTreeModel().getRoot();
			IDNameToStringWrapper objUnitData = (IDNameToStringWrapper)node.getUserObject();
			IDName unit = (IDName)objUnitData.getValue();
			strRootUnit = unit.getID();
		}
		return strRootUnit;
		
	}
	
}
 