package com.ufsoft.iufo.fmtplugin.freequery;

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
import com.ufsoft.iufo.fmtplugin.formatcore.FreeReportContextKey;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigation;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.sysprop.ui.ISysProp;
import com.ufsoft.iufo.sysprop.ui.SysPropMng;
import com.ufsoft.iufo.sysprop.vo.SysPropVO;
import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.iuforeport.freequery.FreeQueryTranceObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

/**
 * 自由查询扩展的命令基类
 * 
 * @author caijie
 * @since 3.1
 */
public class FreeQueryCmd extends UfoCommand {
	private UfoReport m_RepTool = null;// 报表工具

	/**
	 * @param rep
	 *            UfoReport - 报表工具
	 */
	public FreeQueryCmd(UfoReport rep) {
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

		ReportCache repCache = IUFOUICacheManager.getSingleton()
				.getReportCache();
		ReportVO repVO = repCache.getByPK(context.getIufoRepID());

		MeasureQueryModelDef queryDef = CreateMeasQueryDesigner
				.createDefaultQueryDef(repVO, context.getTaskID(), context
						.getUnitValue(), context.getOrgID());
		queryDef.setMeasures(measures);
		queryDef.setExInfoVOs(unitInfos);
		queryDef.setVer(context.getVer());
		queryDef.setTaskID(context.getTaskID());
		queryDef.setQueryType(queryType);
		SysPropVO sysProp = SysPropMng
				.getSysProp(ISysProp.HB_REPDATA_RELATING_TASK);
		if (sysProp != null && sysProp.getValue() != null
				&& sysProp.getValue().equalsIgnoreCase("true"))
			queryDef.setHBByTask(true);

		// 4。设置自由查询的环境信息
		FreeQueryTranceObj obj = (FreeQueryTranceObj) context
				.getAttribute(FreeReportContextKey.FREEQEURY_TRANS_OBJ);
		if (obj == null)
			obj = new FreeQueryTranceObj();
		obj.setQueryDefs(new IFreeQueryModel[] { queryDef });
		obj
				.setQueryDesigners(new IFreeQueryDesigner[] { new CreateMeasQueryDesigner() });

		String strAloneID = context.getAloneId();
		if (strAloneID != null) {
			try {
				MeasurePubDataVO pubData = MeasurePubDataBO_Client
						.findByAloneID(strAloneID);
				Hashtable<String, String> filter = queryDef.getFilterMap();
				KeyVO[] keys = pubData.getKeyGroup().getKeys();
				for (int i = 0; i < keys.length; i++) {
					if (KeyVO.isUnitKeyVO(keys[i])) {
						UnitInfoVO unitInfo = IUFOUICacheManager.getSingleton()
								.getUnitCache().getUnitInfoByPK(
										pubData.getKeywordByName(keys[i]
												.getName()));
						if (unitInfo != null) {
							String unitValue = unitInfo.getPropValue(context
									.getOrgID());
							filter.put("unit_value", unitValue);
							filter.put("unit_code", unitInfo.getCode());
						}
					}
					filter.put(keys[i].getKeywordPK(), pubData
							.getKeywordByName(keys[i].getName()));
				}
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		}

		context.setAttribute(FreeReportContextKey.FREEQEURY_TRANS_OBJ, obj);

		// 5。弹出新的自由查询报表窗口
		if (queryType == MeasureQueryModelDef.QUERY_ANAREOIRT) {// 5.5版本新增加的分析报表
			UfoReport newReport = FreeQueryReport.getAnaInstance(context, m_RepTool);
			QueryNavigation.showReport(newReport, StringResource.getStringResource(FreeQueryExt.STRING_ID_ANAREPORT),
					true,true);// 分析报表

		} else {
			UfoReport newReport = FreeQueryReport.getFreeInstance(context, m_RepTool, obj.getQueryDesigners()[0]);
			QueryNavigation.showReport(newReport, StringResource.getStringResource("uibifreequery01"), true,false);// 自由查询


		}
	}

	/**
	 * @return 返回 UfoRepTool。
	 */
	public UfoReport getRepTool() {
		return m_RepTool;
	}
}
