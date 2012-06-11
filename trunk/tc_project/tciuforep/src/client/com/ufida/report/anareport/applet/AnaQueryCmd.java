package com.ufida.report.anareport.applet;

import java.util.Hashtable;

import nc.itf.iufo.freequery.IFreeQueryDesigner;
import nc.itf.iufo.freequery.IFreeQueryModel;
import nc.pub.iufo.cache.ReportCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureQueryModelDef;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.measure.UnitExInfoVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.free.IRptProviderCreator;
import com.ufida.report.free.RptProviderCreator;
import com.ufsoft.iufo.fmtplugin.freequery.CreateMeasQueryDesigner;
import com.ufsoft.iufo.fmtplugin.freequery.FreeQueryReport;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigation;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.sysprop.ui.ISysProp;
import com.ufsoft.iufo.sysprop.ui.SysPropMng;
import com.ufsoft.iufo.sysprop.vo.SysPropVO;
import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.iuforeport.freequery.FreeQueryTranceObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

public class AnaQueryCmd extends UfoCommand {
	private UfoReport m_RepTool = null;// 报表工具

	/**
	 * @param rep
	 *            UfoReport - 报表工具
	 */
	public AnaQueryCmd(UfoReport rep) {
		super();
		this.m_RepTool = rep;
	}

	public void execute(Object[] params) {
		if (params == null)
			return;
		// 3。根据指标创建指标查询定义
		FreeQueryContextVO context = (FreeQueryContextVO) params[0];
		MeasureVO[] measures = (MeasureVO[]) params[1];
		UnitExInfoVO[] unitInfos = (UnitExInfoVO[]) params[2];
		int queryType = Integer.parseInt(params[3].toString());

		ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
		ReportVO repVO = repCache.getByPK(context.getIufoRepID());

		MeasureQueryModelDef queryDef = CreateMeasQueryDesigner.createDefaultQueryDef(repVO, context.getTaskID(),
				context.getUnitValue(), context.getOrgID());
		queryDef.setMeasures(measures);
		queryDef.setExInfoVOs(unitInfos);
		if(context.getVer()!=null){
			queryDef.setVer(context.getVer());
		}else{
			queryDef.setVer(0);
		}
		queryDef.setTaskID(context.getTaskID());
		queryDef.setQueryType(queryType);
		SysPropVO sysProp = SysPropMng.getSysProp(ISysProp.HB_REPDATA_RELATING_TASK);
		if (sysProp != null && sysProp.getValue() != null && sysProp.getValue().equalsIgnoreCase("true"))
			queryDef.setHBByTask(true);

		// 4。设置自由查询的环境信息

		String strAloneID = context.getAloneId();
		if (strAloneID != null) {
			try {
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
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		}

		context.setAttribute(FreeQueryContextVO.FREEQEURY_MODEL_OBJ, queryDef);

		// 5。弹出新的自由查询报表窗口
			UfoReport newReport = FreeQueryReport.getAnaInstance(context, m_RepTool);
			QueryNavigation.showReport(newReport, StringResource.getStringResource(AnaQueryExt.STRING_ID_ANAREPORT),
					true,true);// 自由报表

	}

	/**
	 * @return 返回 UfoRepTool。
	 */
	public UfoReport getRepTool() {
		return m_RepTool;
	}
}
