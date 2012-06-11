package com.ufida.report.anareport.edit;

import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

import nc.itf.iufo.freequery.IFreeQueryDesigner;
import nc.itf.iufo.freequery.IFreeQueryModel;
import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.TaskCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.ui.iufo.input.control.DataSourceConfig;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureQueryModelDef;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.measure.UnitExInfoVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iuforeport.rep.ReportVO;
import nc.vo.iufo.datasource.DataSourceLoginVO;

import com.ufida.bi.base.BIException;
import com.ufida.dataset.Context;
import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.anareport.applet.AnaQueryExt;
import com.ufida.report.free.IRptProviderCreator;
import com.ufida.report.free.RptProviderCreator;
import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.freequery.CreateMeasQueryDesigner;
import com.ufsoft.iufo.fmtplugin.freequery.FreeQueryReport;
import com.ufsoft.iufo.fmtplugin.freequery.MultiMeasureRefDlg;
import com.ufsoft.iufo.inputplugin.biz.file.MenuStateData;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigation;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.sysprop.ui.ISysProp;
import com.ufsoft.iufo.sysprop.ui.SysPropMng;
import com.ufsoft.iufo.sysprop.vo.SysPropVO;
import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.iuforeport.freequery.FreeQueryTranceObj;
import com.ufsoft.iuforeport.tableinput.TableInputOperUtil;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;

public class AnaQueryAction extends AbstractPluginAction{
	/**
	 * @i18n miufo00110=自由报表
	 */
	public static final String STRING_ID_ANAREPORT = "miufo00110";
	/**
	 * @i18n miufo00111=查询直接下级数据
	 */
	public static final String STRING_ID_NEXTCORP="miufo00111";
	
	// 查询下级数据,查询本年数据,自由查询
	public static final String[] MENU_NAMES = new String[] { "miufofreequery0002", "miufofreequery0003", "miufofreequery0001", "miufofreequery0004",STRING_ID_NEXTCORP};

	private int m_queryType;
	
	public AnaQueryAction(int queryType){
		m_queryType = queryType;
	}
	
	@Override
	public void execute(ActionEvent e) {
		// 1。设置新的环境信息
		try{
		Integer nVer=(Integer)getRepDataEditor().getContext().getAttribute(IUfoContextKey.DATA_VERSION);
		if(nVer == null || (!TableInputOperUtil.isValidVer(nVer.intValue(),HBBBSysParaUtil.VER_SEPARATE)&&!TableInputOperUtil.isValidVer(nVer.intValue(),HBBBSysParaUtil.VER_HBBB)))
			throw new BIException(MultiLang.getString("uiuforep00130"));
		final FreeQueryContextVO context = createFreeQueryContextVO(getRepDataEditor().getContext());
		String strReportPK = context.getIufoRepID();
		// 2。获取当前选中区域中的指标
		MeasureVO[] selMeasures = getMeasureVOByPos(strReportPK);
		UnitExInfoVO[] selUnitInfos = null;
		// 3。若没有选中指标，从参照中选择
		if (selMeasures == null) {
			Object[] obj = getMeasureVOByRef(context, null);
			if(obj != null){
				selMeasures = (MeasureVO[])obj[0];
				selUnitInfos = (UnitExInfoVO[])obj[1];
			}
		}
		if(selMeasures==null||selMeasures.length==0){
			return ;
		}else{
			ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
			ReportVO repVO = repCache.getByPK(context.getIufoRepID());

			MeasureQueryModelDef queryDef = CreateMeasQueryDesigner.createDefaultQueryDef(repVO, context.getTaskID(),
					context.getUnitValue(), context.getOrgID());
			queryDef.setMeasures(selMeasures);
			queryDef.setExInfoVOs(selUnitInfos);
			if(context.getVer()!=null){
				queryDef.setVer(context.getVer());
			}else{
				queryDef.setVer(0);
			}
			queryDef.setTaskID(context.getTaskID());
			queryDef.setQueryType(m_queryType);
			SysPropVO sysProp = SysPropMng.getSysProp(ISysProp.HB_REPDATA_RELATING_TASK);
			if (sysProp != null && sysProp.getValue() != null && sysProp.getValue().equalsIgnoreCase("true"))
				queryDef.setHBByTask(true);

			// 4。设置自由查询的环境信息

			String strAloneID = context.getAloneId();
			if (strAloneID != null) {
				
					MeasurePubDataVO pubData = MeasurePubDataBO_Client.findByAloneID(strAloneID);
					Hashtable<String, String> filter = queryDef.getFilterMap();
					KeyVO[] keys = pubData.getKeyGroup().getKeys();
					for (int i = 0; i < keys.length; i++) {
						if (KeyVO.isUnitKeyVO(keys[i])) {
							UnitInfoVO unitInfo = IUFOUICacheManager.getSingleton().getUnitCache().getUnitInfoByPK(
									pubData.getKeywordByName(keys[i].getName()));
							if (unitInfo != null) {
								String unitValue = unitInfo.getPropValue(context.getOrgID());
								filter.put(IRptProviderCreator.COLUMN_ORGPK, unitValue);
								filter.put(IRptProviderCreator.COLUMN_UNITCODE, unitInfo.getCode());
							}
						}
						filter.put(RptProviderCreator.getDataSetColumnName(keys[i]), pubData.getKeywordByName(keys[i].getName()));
					}
				
			}

			context.setAttribute(FreeQueryContextVO.FREEQEURY_MODEL_OBJ, queryDef);
			context.setAttribute(FreeQueryContextVO.OPERATION_STATE, UfoReport.OPERATION_FORMAT);
			// 5。在新的线程中弹出新的自由查询报表窗口,避免新框架的EventManager中旧的事件监听
			new Thread(new Runnable(){
				public void run() {
					UfoReport newReport = FreeQueryReport.getAnaInstance(context, null);
				    QueryNavigation.showReport(newReport, StringResource.getStringResource(AnaQueryExt.STRING_ID_ANAREPORT),
									true,true);// 自由报表
				}
				
			}).start();
			
		}
		}catch(BIException ex){
			JOptionPane.showMessageDialog(getRepDataEditor(), ex.getMessage());
		}catch (Exception ex1) {
			AppDebug.debug(ex1);
		}
		
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
        PluginActionDescriptor des = new PluginActionDescriptor(MultiLangInput.getString(MENU_NAMES[m_queryType]));
        des.setExtensionPoints(XPOINT.MENU);
        des.setGroupPaths(new String[] {StringResource.getStringResource("miufo00110")});
		return des;
	
	}
	
	protected RepDataEditor getRepDataEditor(){
		if(getCurrentView() instanceof RepDataEditor){
			return (RepDataEditor)getCurrentView();
		}
    	return null;
    }
	/**
	 * @i18n uiuforep00130=不支持对此版本数据的自由查询。
	 */
	protected FreeQueryContextVO createFreeQueryContextVO(IContext contextVO) {
		String loginLevelCode=(String)contextVO.getAttribute(IUfoContextKey.LOGIN_UNIT_LEVELCODE);
		String levelCode=(String)contextVO.getAttribute(IUfoContextKey.ORG_PK);;
		String loginUnitId=(String)contextVO.getAttribute(IUfoContextKey.LOGIN_UNIT_ID);
		if(loginUnitId==null){
			loginUnitId=(String)contextVO.getAttribute(IUfoContextKey.CUR_UNIT_ID);
		}
		if(loginLevelCode==null&&loginUnitId!=null){
			UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();
			UnitInfoVO unitInfo=unitCache.getUnitInfoByPK(loginUnitId);
			if(unitInfo!=null){
				loginLevelCode=unitInfo.getPropValue(levelCode);
			}
			if(loginLevelCode!=null){
				contextVO.setAttribute(IUfoContextKey.LOGIN_UNIT_LEVELCODE, loginLevelCode);	
			}
		}
			FreeQueryContextVO freeContext = new FreeQueryContextVO(new ContextVO((Context)contextVO));
			
		if (freeContext.getAttribute(IUfoContextKey.CUR_UNIT_ID)!=null && freeContext.getAttribute(IUfoContextKey.DATA_SOURCE)!=null){
			String strCurUnitID=(String)freeContext.getAttribute(IUfoContextKey.CUR_UNIT_ID);
			DataSourceVO dataSource=(DataSourceVO)freeContext.getAttribute(IUfoContextKey.DATA_SOURCE);
			DataSourceConfig config=DataSourceConfig.getInstance(getMainboard());
			DataSourceLoginVO login=config.getOneSourceConfig(strCurUnitID);
			dataSource.setLoginUnit(login.getDSUnit());
			dataSource.setLoginName(login.getDSUser());
			dataSource.setLoginPassw(login.getDSPass());
		}
           
			return freeContext;
	}

	private MeasureVO[] getMeasureVOByPos(String strCurrRepPK) {
		CellPosition[] pos = getRepDataEditor().getCellsModel().getSelectModel().getSelectedCells();
		Vector<MeasureVO> vec = new Vector<MeasureVO>();
		if (pos != null) {
			DynAreaModel dynModel = DynAreaModel.getInstance(getRepDataEditor().getCellsModel());
			for (int i = 0; i < pos.length; i++) {
				CellPosition formatPos = DynAreaCell.getFormatArea(pos[i], getRepDataEditor().getCellsModel()).getStart();
 				MeasureVO mVO = dynModel.getMeasureModel().getMeasureVOByPos(formatPos);
				if (mVO != null) {
					mVO.setSelReportPK(strCurrRepPK);
					if(!vec.contains(mVO) && isSameKeyGroup(vec, mVO))
						vec.addElement(mVO);
				}
			}
		}
		if (vec.size() > 0)
			return vec.toArray(new MeasureVO[0]);
		return null;
	}
	private boolean isSameKeyGroup(Vector<MeasureVO> vec, MeasureVO mVO){
		if(vec.size() == 0)
			return true;
		MeasureVO m0 = vec.get(0);
		return m0.getKeyCombPK().equals(mVO.getKeyCombPK());
	}

	private Object[] getMeasureVOByRef(FreeQueryContextVO context, MeasureVO[] selMeasures) {
		ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
		ReportVO repVO = repCache.getByPK(context.getIufoRepID());
		if (repVO == null) {// 创建虚拟的报表，以便弹出参照
			repVO = new ReportVO();
			repVO.setModel(false);
		}
		// 从报表环境中获取参数
		TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
		TaskVO taskVO = taskCache.getTaskVO(context.getTaskID());

		String strKeyGroupPk = taskVO.getKeyGroupId();
		context.setKeyGroupID(strKeyGroupPk);

		KeyGroupCache keyGroupCache = IUFOUICacheManager.getSingleton().getKeyGroupCache();
		KeyGroupVO keyGroupVO = keyGroupCache.getByPK(context.getKeyGroupID());

		MultiMeasureRefDlg refDialog = new MultiMeasureRefDlg(new UfoDialog(getRepDataEditor()), repVO, keyGroupVO, context
				.getCurUserID(), true, true, true, selMeasures);
		refDialog.setModal(true);
		refDialog.show();

		if (refDialog.getResult() == UfoDialog.ID_OK) {
			Object[] result = new Object[2];
			result[0] = refDialog.getSelMeasureVOs();
			result[1] = refDialog.getSelUnitexVOs();
			return result;
		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		RepDataEditor editor=getRepDataEditor();
		if(editor==null){
			return false;
		}
		MenuStateData menuState=editor.getMenuState();
		if (menuState != null) {
			if (m_queryType == MeasureQueryModelDef.QUERYBY_CORP
					|| m_queryType == MeasureQueryModelDef.QEURY_NEXTCORP) {
                 return menuState.isHasUnitKey();
			}
			if (m_queryType == MeasureQueryModelDef.QUERYBY_YEAR) {
                 return menuState.isHasTimeKey();
			}
		}else{
			return false;
		}
		return true;
	}
	
}
