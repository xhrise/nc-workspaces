package com.ufsoft.iufo.inputplugin.biz.file;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import nc.pub.iufo.cache.ReportCache;
import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.ui.iufo.input.table.TableInputParam;
import nc.vo.iufo.data.MeasurePubDataVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.console.ActionHandler;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.inputcore.RefData;
import com.ufsoft.iufo.inputplugin.inputcore.RefInfo;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.RepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.ReportNavPanel;
import com.ufsoft.report.UICloseableTabbedPane;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbstractNavExt;
import com.ufsoft.report.util.MultiLang;

public class ChooseRepExt2 extends AbstractNavExt implements IUfoContextKey{
	/**
	 * @i18n uiuforep00009=按单位组织
	 */
	public final static String LABEL_UNITTREE  = MultiLang.getString("uiuforep00009"); 
	/**
	 * @i18n uiuforep00010=按报表组织
	 */
	public final static String LABEL_REPTREE = MultiLang.getString("uiuforep00010");   
	/**
	 * @i18n uiuforep00011=单位列表
	 */
	public final static String LABEL_UNITLIST  = MultiLang.getString("uiuforep00011");
	/**
	 * @i18n uiuforep00012=报表列表
	 */
	public final static String LABEL_REPLIST = MultiLang.getString("uiuforep00012");
	
	private String m_extName = LABEL_REPLIST;
	private UfoReport m_ufoReport = null;
	private ChooseRepPanel m_chooseRepNavPanel = null;
	
	public ChooseRepExt2(UfoReport ufoReport) {
		m_ufoReport = ufoReport;
		if(GeneralQueryUtil.isShowRepTree(ufoReport)){
			setExtName(LABEL_REPLIST);
		}else{
			setExtName(LABEL_UNITLIST);
		}
	}


	public int getNavPanelPos() {
		return ReportNavPanel.WEST_NAV;
	}
	
	@Override
	/**
	 * 根据操作报表的数据权限，初始化报表树面板
	 * @param 报表树面板
	 */
	public JPanel getPanel() {
		TableInputContextVO inputContextVO  = (TableInputContextVO)m_ufoReport.getContextVo();
		if(inputContextVO != null){
			Object tableInputTransObj = inputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
			TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
			
			RepDataParam repDataParam = (RepDataParam)inputTransObj.getRepDataParam();
			if(repDataParam != null && 
					(repDataParam.getOperType().equals(TableInputParam.OPERTYPE_REPDATA_EDIT_REP) ||
							repDataParam.getOperType().equals(TableInputParam.OPERTYPE_REPDATA_EDIT_TASK) ||
								repDataParam.getOperType().equals(TableInputParam.OPERTYPE_REPDATA_INPUT))){
				initRepNavPanel();
			}
		}
		return m_chooseRepNavPanel;
	}

	/**
	 * 加载当前任务用户可查看及可操作报表到报表树或单位树
	 * @return 可操作报表个数
	 */
	public int initRepNavPanel(){
		if(m_chooseRepNavPanel == null){
			m_chooseRepNavPanel=createPanel();
			m_chooseRepNavPanel.setBorder(null);
		}
		
		return 1;
	}
	
	/**
	 * 创建并设置报表树数据模型
	 * @return RepTreeModel
	 */
	private RepTreeModel createRepTreeModel(){
		ChooseRepData[] chooseRepDatas = doGetChooseRepDatas(m_ufoReport,false);
		if (chooseRepDatas == null || chooseRepDatas.length <= 0) {
			JOptionPane.showMessageDialog(m_ufoReport, MultiLangInput
					.getString("uiufotableinput0025"));//该任务可能没有可选择的报表！
			
		}
		return new RepTreeModel(chooseRepDatas);
		
	}
	
	/**
	 * 查询用户可查看(只读)或可操作报表(可修改或可只可计算保存)
	 * @param container
	 * @return
	 */
	public static ChooseRepData[] doGetChooseRepDatas(RepDataEditor editor,boolean isImport) {
		try{
			ChooseRepData[] chooseRepDatas = null;
			if(isImport){
				chooseRepDatas=(ChooseRepData[])ActionHandler.exec("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "loadTableImportReps", editor.getRepDataParam());
			}else{
				chooseRepDatas=(ChooseRepData[])ActionHandler.exec("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "loadTableChoosetReps", editor.getRepDataParam());
			}

			if (chooseRepDatas!=null && editor.getPubData()!=null && editor.getPubData().getVer()==HBBBSysParaUtil.VER_HBBB){
				chooseRepDatas = filterRepDatas(chooseRepDatas);
			}
			return chooseRepDatas;
		}catch(Exception e){
			AppDebug.debug(e);
			return null;
		}
	}
	
	/**
	 * 查询用户可查看(只读)或可操作报表(可修改或可只可计算保存)
	 * @param container
	 * @return
	 */
	public static ChooseRepData[] doGetChooseRepDatas(UfoReport container,boolean isImport) {
		ChooseRepData[] chooseRepDatas = null;
		Object returnObj = null;
		if(isImport){
		returnObj=InputBizOper.doLinkServletTask(
					ITableInputMenuType.BIZ_TYPE_IMPORTABLEREP, container, false);
		}else{
		returnObj=InputBizOper.doLinkServletTask(
				ITableInputMenuType.BIZ_TYPE_CHOOSEREP, container, false);
		}
		returnObj = ChangeKeywordsExt.getBizReturnObj(returnObj);
			
		if (returnObj != null && returnObj instanceof ChooseRepData[]) {
			if(isHBBBInput(container)){
				chooseRepDatas = filterRepDatas((ChooseRepData[])returnObj);
			}else
				chooseRepDatas = (ChooseRepData[])returnObj;
		}
		return chooseRepDatas;
	}
	
	private static boolean isHBBBInput(UfoReport container){
		boolean isGeneralQuery = GeneralQueryUtil.isGeneralQuery(container.getContext());
		if(isGeneralQuery)
			return false;
		Object tableInputTransObj = ((TableInputContextVO)container.getContextVo()).getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
		boolean isHBBBData = inputTransObj.getRepDataParam().isHBBBData();
		if(isHBBBData)
			return true;
		String aloneId = inputTransObj.getRepDataParam().getAloneID();
		int ver = 0;
		try {
			if(aloneId==null)
				return false;
			
			MeasurePubDataVO pubdata = MeasurePubDataBO_Client.findByAloneID(aloneId);
			
			if(pubdata==null)
				return false;
			ver = pubdata.getVer();
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		
		return ver== HBBBSysParaUtil.VER_HBBB;
	}
	
	private static ChooseRepData[] filterRepDatas(ChooseRepData[] chooseRepDatas){
		ArrayList<ChooseRepData> lstRepDatas = new ArrayList<ChooseRepData>();
		ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
		for(int i=0;i<chooseRepDatas.length;i++){
			String reportPK = chooseRepDatas[i].getReportPK();
			if(!repCache.getByPK(reportPK).isIntrade())
				lstRepDatas.add(chooseRepDatas[i]);
		}
		
		ChooseRepData[] returnObj = null;
		if(lstRepDatas.size()>0){
			returnObj = new ChooseRepData[lstRepDatas.size()];
			returnObj = lstRepDatas.toArray(returnObj);
		}
		return returnObj;
		
	}
	
	/**
	 * 创建并设置报表树数据模型
	 * @return UnitTreeModel
	 */
	private UnitTreeModel createUnitTreeModel() {
		TableInputContextVO inputContextVO  = (TableInputContextVO)m_ufoReport.getContextVo();
		String strOrgPK = inputContextVO.getAttribute(ORG_PK) == null ? null : (String)inputContextVO.getAttribute(ORG_PK);

		RefInfo refInfo = new RefInfo(RefInfo.TYPE_UNIT);
		refInfo.setCurUnitCode((String)inputContextVO.getAttribute(CUR_UNIT_CODE));
		refInfo.setOrgPK(strOrgPK);
		
		UnitTreeModel unitTreeModel = new UnitTreeModel((String)inputContextVO.getAttribute(CUR_UNIT_CODE),
				null, RefData.getData(refInfo, false), true);
		return unitTreeModel;
		
	}

	public ChooseRepPanel getChooseRepNavPanel() {
		return m_chooseRepNavPanel;
	}

	public String getExtName() {
		return m_extName;
	}

	public void setExtName(String name) {
		String oldName=m_extName;
		m_extName = name;
		//同时更新tab的Name
		if(name!=null&&!name.equals(oldName)){
			if(getPanel()!=null&&getPanel().getParent() instanceof UICloseableTabbedPane){
				UICloseableTabbedPane tab=(UICloseableTabbedPane)(getPanel().getParent());
				int titleId=tab.indexOfTab(oldName);
				if(titleId>=0){
					tab.setTitleAt(titleId, name);
				}
			}
		}
	}

	@Override
	protected ChooseRepPanel createPanel() {
		//创建报表树数据模型及单位树数据模型
		RepTreeModel repTreeModel = createRepTreeModel();
		UnitTreeModel unitTreeModel = createUnitTreeModel();
		return new ChooseRepPanel(m_ufoReport, repTreeModel, unitTreeModel);
	}
	@Override
	public void setPanel(JPanel m_panel) {
		if(m_panel instanceof ChooseRepPanel){
			m_chooseRepNavPanel=(ChooseRepPanel)m_panel;
		}
	}

	public String getName() {
		// TODO Auto-generated method stub
		return getExtName();
	}
	
}
 