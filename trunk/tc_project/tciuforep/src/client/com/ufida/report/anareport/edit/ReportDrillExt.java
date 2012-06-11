package com.ufida.report.anareport.edit;

import java.awt.Component;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import nc.ui.pub.beans.UIDialog;
import nc.vo.bi.report.manager.ReportResource;
import nc.vo.bi.report.manager.ReportVO;

import com.ufida.dataset.Context;
import com.ufida.dataset.metadata.Field;
import com.ufida.dataset.tracedata.TraceDataParam;
import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.multidimension.model.MultiReportSrvUtil;
import com.ufida.report.rep.model.BIRepParams;
import com.ufida.report.rep.model.IBIContextKey;
import com.ufsoft.iufo.fmtplugin.BDContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.freequery.CreateMeasQueryDesigner;
import com.ufsoft.iufo.fmtplugin.freequery.FreeQueryReport;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigation;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

/**
 * 分析表中报表穿透功能的设置和执行
 * 
 * @author ll
 * 
 */
public class ReportDrillExt extends AbsAnaReportExt {

	/**
	 * @i18n uibiadhoc00011=穿透设置
	 */
	static final String RESID_SET_DRILL_REPORT = "uibiadhoc00011";
	/**
	 * @i18n mbimulti00021=目标报表
	 */
	static final String RESID_CHOOSE_DRILL_REPORT = "mbimulti00021";

	/**
	 * @i18n mbimulti00026=报表穿透
	 */
	static final String RESID_DODRILL_REPORT = "mbimulti00026";
	/**
	 * @i18n miufo00188=区域设置
	 */
	static final String RESID_AREA_DRILL = "miufo00188";

	private boolean m_isSet = true;

	public ReportDrillExt(AnaReportPlugin plugin, boolean isSet) {
		super();
		setPlugIn(plugin);
		m_isSet = isSet;
	}

	@Override
	public UfoCommand getCommand() {
		return null;
	}

	@Override
	public Object[] getParams(UfoReport container) {
		AnaReportModel model = m_plugin.getModel();

		AreaDataModel aModel = getSelAreaModel(true);
		String aimReport = aModel.getDrillReport();
		if (m_isSet) {// set drillreport
			ReportDrillDlg dlg = new ReportDrillDlg(m_plugin.getReport(), (String) model.getContextVO().getAttribute(
					BDContextKey.CUR_USER_ID), (String) model.getContextVO().getAttribute(IUfoContextKey.CUR_UNIT_ID),
					aimReport);
			if (dlg.showModal() == UIDialog.ID_OK) {
				ReportVO report = dlg.getAimReportVO();
				if (report != null)
					aModel.setDrillReport(report.getID());
				else
					aModel.setDrillReport(null);
				m_plugin.setDirty(true);
			}
		} else {
			CellPosition pos = model.getCellsModel().getSelectModel().getAnchorCell();
			doReportDrill(aimReport, model.getCellsModel(), pos);
		}
		return null;
	}

	@Override
	public boolean isEnabled(Component focusComp) {
		if (!StateUtil.isAreaSel(m_plugin.getReport(), focusComp))
			return false;

		// @edit by ll at 2009-7-2,下午08:25:07 from QE，不提供穿透功能。
		Context con = m_plugin.getReport().getContextVo();
		Integer obj = (Integer) con.getAttribute(ReportResource.OPEN_IN_MODAL_DIALOG);
		if (obj != null && obj >= 0)
			return false;
		AreaDataModel areaModel = getSelAreaModel(true);
		if (areaModel == null)
			return false;

		if (m_plugin.getModel().isFormatState()) {// 格式态支持进行报表穿透设置
			return m_isSet;
		} else {// 数据态按照追踪参数执行报表穿透
			if (m_isSet)
				return true;

			String aimReport = areaModel.getDrillReport();
			if (aimReport == null)
				return false;

			CellsModel cellsModel = m_plugin.getModel().getCellsModel();
			Hashtable<CellPosition, TraceDataParam> traceRef = (Hashtable<CellPosition, TraceDataParam>) cellsModel
					.getExtProp(TraceDataParam.KEY);
			if (traceRef == null)
				return false;
			CellPosition pos = cellsModel.getSelectModel().getAnchorCell();
			TraceDataParam traceData = traceRef.get(pos);
			return (traceData != null);
		}
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes mainMenu = new ActionUIDes();
		mainMenu.setName(StringResource.getStringResource(RESID_DODRILL_REPORT));
		if (m_isSet)
			mainMenu.setPaths(new String[] { MultiLang.getString("data") });
		else
			mainMenu.setPopup(true);
		return new ActionUIDes[] { mainMenu };
	}

	private void doReportDrill(String aimReport, CellsModel cells, CellPosition pos) {

		Hashtable<CellPosition, TraceDataParam> traceRef = (Hashtable<CellPosition, TraceDataParam>) cells
				.getExtProp(TraceDataParam.KEY);
		if (traceRef == null)
			return;
		TraceDataParam traceData = traceRef.get(pos);
		if (traceData == null)
			return;

		ReportVO newReportVO = MultiReportSrvUtil.getReportByID(aimReport);
		if (newReportVO == null)
			return;

		// 1. 分析数据追踪参数
		Object[] rowData = traceData.getRowData();
		if (rowData == null && traceData.getRow() >= 0)
			rowData = traceData.getDataSet().getRowData(traceData.getRow());

		Map<String, String[]> map = new HashMap<String, String[]>();
		if (rowData != null) {// 存在追踪参数
			Field[] flds = traceData.getDataSet().getMetaData().getFields();
			if (flds != null && flds.length == rowData.length) {// 进一步校验追踪参数字段匹配
				for (int i = 0; i < flds.length; i++) {
					if (rowData[i] != null) {
						map.put(flds[i].getFldname(), new String[] { rowData[i].toString() });
					}
				}
			}
		}
		// 2.设置报表穿透参数
		BIRepParams drillParam = new BIRepParams(map, null);
		ContextVO context = (ContextVO) m_plugin.getContextVO().clone();
		context.setAttribute(IBIContextKey.BIDRILLREPORTPARAM, drillParam);
		context.setAttribute(ReportContextKey.REPORT_PK, aimReport);
		context.setAttribute(ReportContextKey.REPORT_CODE, newReportVO.getReportcode());
		context.setAttribute(ReportContextKey.REPORT_NAME, newReportVO.getReportname());
		context.setAttribute(ReportContextKey.OPERATION_STATE, ReportContextKey.OPERATION_INPUT);

		// 3.对于目标报表设置环境和执行
		AnaReportModel newModel = (AnaReportModel) newReportVO.getDefinition();
		if (newModel != null) {

			newModel.setContextVO(context);
			newModel.setOperationState(ReportContextKey.OPERATION_INPUT, aimReport, (String) context
					.getAttribute(BDContextKey.CUR_USER_ID));

			newModel.filterByParams(drillParam);

			// 4.切换目标环境中的信息
			context.setAttribute(IBIContextKey.REPORTMODEL, newModel);
		}
		// 5.执行打开新报表
		UfoReport newReport = FreeQueryReport.getAnaInstance(context, m_plugin.getReport());
		newReport.setOperationState(ReportContextKey.OPERATION_INPUT);
		QueryNavigation.showReport(newReport, StringResource.getStringResource("miufo00110"), true, false);// 分析报表
	}

}
