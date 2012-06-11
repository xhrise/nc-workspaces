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
 *        �����ۺϲ�ѯ��ҵ��������Ҫ�ڱ�����ѡ�񱨱�Ļ��������Ӱ���λ��ѯ����
 *        ������ṩ����λ����������λ�����ݵ�λ��ѯ����
 * @end
 * 
 * ���������
 * @author chxw
 * 2007-09-04
 */
public class ChooseRepPanel extends UIPanel implements IUfoContextKey{
	
	private JTree jTreeRep = null;
	
	private UfoReport m_ufoReport = null;
	
	private JScrollPane jScrollPane = null;	

	/**
     * �Ϸ��Ĳ�ѯ�������
     */
    private ChooseCordPanel m_chooseCordPanel = null;
    
	/**
	 * ����������ģ��
	 */
	private RepTreeModel m_repTreeModel = null;
	
	/**
	 * ��λ������ģ��
	 */
	private UnitTreeModel m_unitTreeModel = null;
	
	/**
	 * ��ǰѡ��λ
	 */
	private String m_strOperUnit = null;
	
	/**
	 * ��ǰ��������
	 */
	private String m_strOperRep  = null;
	
	/**
	 * ChooseRepPanel ������
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
	 * ��ʼ���������ۣ��ߴ缰��������ּ�ģ������
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
	 * ��ʼ�����������	
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
	 * ��ʼ�����ؼ���ģ�����ݣ�������ģ�ͼ���ʼѡ�����ڵ�
	 * 
	 */
	private void initTreeDatas() {
		initTreeModel();
		initSelection();
	}
	
	/**
	 * ��������ģ�͵����ؼ���ˢ����ʾ
	 * 
	 */
	private void initTreeModel() {
		TreeSelectionModel model = getJTreeRep().getSelectionModel();
		model.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		if(GeneralQueryUtil.isShowRepTree(m_ufoReport)){
			//ɾ����λ��������
			if(unitMouseListener != null){
				getJTreeRep().removeMouseListener(unitMouseListener);
			}
			//���û�п�ѡ��������յ����˵�
			getJTreeRep().setModel(m_repTreeModel.getTreeModel());
//			if(!m_repTreeModel.hasReps()){
//				QueryNaviMenu menu = QueryNavigation.getSingleton().getCurWindow();
//				menu.clear();
//			}
		} else{
			//ɾ��������������
			if(repMouseListener != null){
				getJTreeRep().removeMouseListener(repMouseListener);
			}
			getJTreeRep().setModel(m_unitTreeModel.getTreeModel());
		}
		m_strOperUnit = null;
		m_strOperRep  = null;
		
	}

	/**
	 * �����������ʼѡ�����λ��ѡ���û���ѯ��λ��������ѡ���û���ѯ����
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
	 * ���ó�ʼѡ�еı���
	 * 1������Ӵ򿪱�����ڽ��룬��ָ������
	 * 2��������ǴӴ򿪱�����ڽ��룬��򿪵�һ�ű���
	 * 
	 * @return
	 */
	private void initDefSelectionRep(){
		TreeSelectionModel model = getJTreeRep().getSelectionModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) (getJTreeRep()
				.getModel().getRoot());
		
		//�õ����򿪱���ı���PK
		TableInputContextVO tableInputContextVO = (TableInputContextVO) m_ufoReport.getContextVo();
		Object tableInputTransObj = tableInputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
		String strSelRepPK = inputTransObj.getRepDataParam().getReportPK();
		
		//������Ǵ�ѡ�񱨱���ڽ��룬��򿪵�һ�ű���
		Object selRepNode = getInitSelRepNode(root, strSelRepPK);
		if(selRepNode != null){
			model.setSelectionPath(new TreePath(selRepNode));
		} else if(root.getChildCount()>0){
			Object objFirst = root.getFirstChild();
			model.setSelectionPath(new TreePath(objFirst));
		}
		
	}
	
	/**
	 * ���ó�ʼѡ�񱨱�ڵ�
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
	 * ���ó�ʼѡ�еĲ�ѯ��λ
	 * 
	 */
	private void initDefSelectionUnit(){
		TreeSelectionModel model = getJTreeRep().getSelectionModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) (getJTreeRep()
				.getModel().getRoot());
		
		//�õ��Ѵ򿪱���Ĳ�����λPK
		TableInputContextVO tableInputContextVO = (TableInputContextVO) m_ufoReport.getContextVo();
		
		Object tableInputTransObj = tableInputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
		String strOperUnitPK = inputTransObj.getRepDataParam().getOperUnitPK();
		
		//�ڵ�λ���ϲ��Ҹõ�λ�ڵ�
		Object selUnitNode = getSelectedUnitNode(root, "hz");
		if(selUnitNode != null){
			model.setSelectionPath(new TreePath(selUnitNode));
		} else if(root.getChildCount()>0){
			Object objFirst = root.getFirstChild();
			model.setSelectionPath(new TreePath(objFirst));
		}
		
	}
	
	/**
	 * ���ݵ�λ�����ڵ�λ���ϲ��Ҹõ�λ���ڽڵ�
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
	 * ���������ѡ��ģ��TreeSelectionModel�¼�����
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
	//��������������л�������
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
					// @edit by wangyga at 2009-1-21,����03:45:22 �����л�ʱ���״̬����ʾ����Ϣ
					clearStatusBarMessage();				
				}
			}
		}
		
	}
	
	//��λ�������λ�л�������
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
	 * �л�����
	 * 
	 * @param strNewReportPK ����PK
	 * @return
	 * @throws Exception 
	 * @i18n uiuforep00129=��ѯ�ؼ��ֲ���Ϊ��
	 * @i18n miufo01054=�Բ���,û�в鿴�ñ������ݵ�Ȩ��
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
		
		//��ʾ���汨��
		if(strOldRepPK != null && !isCommit())
			AbsIufoBizCmd.doComfirmSave(m_ufoReport, false);
		
		//�����±���PK�����ݲ�����
		preChangeRep(strNewReportPK);
		
		inputTransObj.getRepDataParam().setReportPK(strNewReportPK);
		
		searchCondVO.setStrOperRepPK(strNewReportPK);
		boolean hasInput = GeneralQueryUtil.checkKeyInput(searchCondVO.getStrKeyValues());
		if(!hasInput){
			javax.swing.JOptionPane.showMessageDialog(m_ufoReport, MultiLang.getString("uiuforep00129"));
			return;
		}
		
		//����ѡ��ı���
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
		
		//����anchor_Change�¼�,��������ʽ׷�ٹ���
		WindowNavUtil.startFormulaTrace(m_ufoReport,null,true);
		
		regMenu();
	}
	
	/**
	 * add by wangyga 2008-8-11 �л�����ʱ��У������Ȩ��
	 * @param strDataUnitCode:��λcode
	 * @param repDataParam:�������ݶ���
	 * @param strRepPk:����PK
	 * @return
	 */
	private boolean checkDataRight(String strDataUnitCode,IRepDataParam repDataParam,String strRepPk){	
		if(strDataUnitCode == null)//��liulpȷ�����޹ؼ���ʱ��������Ȩ��
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
     * ��ð����û�����Ȩ����Ϣ���û���Ϣ
     * @param strUserPK
     * @return
     * @throws TableInputException 
     */
    private UserInfoVO getUserInfoVO(String strUserPK) throws TableInputException{
        UserCache userCache = BDCacheManager.getUserCache(true);
        UserInfoVO userInfoVO = userCache.getUserById(strUserPK);
        if(userInfoVO == null){
            throw new TableInputException(StringResource.getStringResource("uiufo50tinput001"));//�û������ѱ�ɾ�������飡
        }
        //��ò������û��������ڵ�͹���Ȩ��URL
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
	 * �л���λ
	 * 
	 * @param strNewUnitPK �������
	 * @return
	 * @i18n uiuforep00129=��ѯ�ؼ��ֲ���Ϊ��
	 */
	public void switchUnit(String strNewUnitPK) {
		//��ʾ���汨��
		TableInputContextVO tableInputContextVO = (TableInputContextVO) m_ufoReport.getContextVo();
		
		Object tableInputTransObj = tableInputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
		String strOldRepPK = inputTransObj.getRepDataParam().getReportPK();
		if(strOldRepPK != null && !isCommit())
			AbsIufoBizCmd.doComfirmSave(m_ufoReport, false);
		
		//����ѯ�ؼ����Ƿ�Ϊ��
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
        
        //�����ۺϲ�ѯȱʡ����
        getChooseCordPanel().setSelItemOfCordPanel(strNewUnitPK, oldOperRepPK);

		//����anchor_Change�¼�,��������ʽ׷�ٹ���
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
	 * �����Ƿ����ϱ�
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
     * �����Ƿ���ʾ������
     * @param ufoReport
     * @param showRepTree
     */
	public static void setShowRepTree(UfoReport ufoReport, boolean showRepTree) {
		TableInputContextVO inputContextVO = (TableInputContextVO)ufoReport.getContextVo();
    	inputContextVO.setAttribute(SHOW_REP_TREE, Boolean.valueOf(showRepTree));
	}
	
	/**
	 * �л�����ʱ��������˽���������ݱ仯
	 * 
	 * @param strNewReportPK
	 * @return
	 */
	private void preChangeRep(String strNewReportPK){
		resetCheckArea(strNewReportPK,false);
		resetAutoCalcUtil();
		
	}
	/**
	 * ��ѯ����ʱ��������˽���������ݱ仯
	 * 
	 * @param strNewReportPK
	 * @return
	 */
	public void preChangeRep(){
		resetCheckArea(null,true);
		resetAutoCalcUtil();
		
	}
	
	/**
	 * ������˽���������ݱ仯
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
	 * ���³�ʼ���Զ����㹤����������
	 * 
	 */
	private void resetAutoCalcUtil(){
		InputAutoCalcPlugIn autoCalcPlug=(InputAutoCalcPlugIn)m_ufoReport.getPluginManager().getPlugin(InputAutoCalcPlugIn.class.getName());
		if(autoCalcPlug!=null)
			autoCalcPlug.initCalcUtil();
	}
    
	/**
     * �����ۺϲ�ѯ�������
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
     * ���õ�����ȱʡѡ�нڵ�
     *
     */
    private void setSelNodeOfNavTree(TreePath path){
    	JTree jNavTree = getJTreeRep(); 
    	jNavTree.scrollPathToVisible(path);
    	jNavTree.setSelectionPath(path);
    	jNavTree.expandPath(path);
    	
    }
    
    /**
     * ���ص�������ѡ�нڵ�
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
     * ������ѯѡ�б���
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
     * ������ѯѡ�е�λ
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
			//������¼���λ
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
     * �����л�ʱ���״̬����ʾ����Ϣ
     * @create by wangyga at 2009-1-21,����03:45:58
     *
     */
    private void clearStatusBarMessage(){
    	m_ufoReport.getStatusBar().setHintMessage("");	
    }
    
	/**
	 * �л��������ʾ��ͼ����������λ��
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
	 * ���õ�����ѡ�нڵ�
	 * @param strOperRepPK
	 * @param strOperUnitPK
	 */
	public void setSelNodeOfNavTree(String strOperRepPK, String strOperUnitPK){
		TreePath treePath = getTreePath(strOperRepPK, strOperUnitPK);
		setSelNodeOfNavTree(treePath);
	}
	
	/**
	 * �Ƿ�ѡ�񵼺����ϵĽڵ�
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
 