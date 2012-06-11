package com.ufsoft.iufo.fmtplugin.freequery;

import nc.itf.iufo.freequery.IFreeQueryDesigner;
import nc.ui.iufo.query.datasetmanager.DataSetManagerPlugin;

import com.ufsoft.iufo.fmtplugin.monitor.MonitorPlugin;
import com.ufsoft.iufo.fmtplugin.window.NavPaneMngExt;
import com.ufsoft.iufo.fmtplugin.window.NavPaneMngToolBarExt;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigationPlugin;
import com.ufsoft.iuforeport.tableinput.applet.TableInputAuth;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.sysplugin.findreplace.FindReplacePlugin;
import com.ufsoft.report.sysplugin.headerlock.HeaderLockPlugin;
import com.ufsoft.report.sysplugin.help.HelpPlugin;
import com.ufsoft.report.sysplugin.print.PrintPlugin;
import com.ufsoft.report.sysplugin.trace.TraceDataPlugin;
import com.ufsoft.table.CellsModel;

public class FreeQueryReport extends UfoReport {
	private static final long serialVersionUID = 6531779325361747620L;

	public static FreeQueryReport getFreeInstance(ContextVO contextVO,
			UfoReport parentReport, IFreeQueryDesigner designer) {
		return new FreeQueryReport(contextVO, null, true);
	}

	public static FreeQueryReport getAnaInstance(ContextVO contextVO,
			UfoReport parentReport) {
		return new FreeQueryReport(contextVO, null, false);
	}

	private FreeQueryReport(ContextVO contextVO, CellsModel cellsModel,
			boolean isFree) {
		super(
				(contextVO.getAttribute(ReportContextKey.OPERATION_STATE) == null) ? OPERATION_FORMAT
						: (Integer) contextVO
								.getAttribute(ReportContextKey.OPERATION_STATE),
				contextVO, cellsModel);
		getCellsModel().setCellsAuth(new TableInputAuth(this));
		initPlugins(isFree);
		// 初始化表格控件的一些状态
		initTableInputCtrl(isFree);
	}

	/**
	 * 复制BIReportApplet中Adhoc类型的插件加载
	 */
	private void initPlugins(boolean isFree) {
		if (isFree) {
			addPlugIn(PrintPlugin.class.getName());// 打印相关菜单：页面设置，打印预览，打印
			// // 自由查询报表设计
			addPlugIn("com.ufida.report.adhoc.applet.AdhocPlugin");//
			addPlugIn("com.ufida.report.free.applet.FreeQueryDesignePlugin");// FreeQueryDesignePlugin.class.getName());
			addPlugIn(HeaderLockPlugin.class.getName());
		} else {
			if (getOperationState() == UfoReport.OPERATION_FORMAT)
				addPlugIn(DataSetManagerPlugin.class.getName());
			
			addPlugIn("com.ufida.report.anareport.applet.AnaExAreaPlugin");
			addPlugIn("com.ufida.report.anareport.applet.AnaReportPlugin");
			addPlugIn("com.ufida.report.anareport.applet.AnaCellAttrToolBarPlugIn");
			addPlugIn("com.ufida.report.anareport.edit.AnaEditPlugin");
			addPlugIn("com.ufida.report.anareport.fmtplugin.AnaAreaFormulaPlugin");
			addPlugIn("com.ufida.report.anareport.applet.AnaRenderEditorPlugin");
			addPlugIn("com.ufida.report.anareport.applet.AnaChartPlugin");
			addPlugIn("com.ufida.report.anareport.applet.AnaCellAttrPlugin");
			addPlugIn("com.ufida.report.anareport.applet.AnaInsertDeletePlugin");
			addPlugIn("com.ufida.report.anareport.applet.AnaFieldCountInfoPlugin");
			addPlugIn("com.ufida.report.anareport.applet.AnaCombineCellPlugin");
			
			addPlugIn(HeaderLockPlugin.class.getName());
			addPlugIn(FindReplacePlugin.class.getName());
			// 数据集的联查
			addPlugIn(TraceDataPlugin.class.getName());
	        addActionExt(new NavPaneMngExt(this));
//	        addActionExt(new NavPaneMngToolBarExt(this));
			addPlugIn(HelpPlugin.class.getName());
			addPlugIn(MonitorPlugin.class.getName());
			addPlugIn("com.ufida.report.anareport.applet.AnaReportToDataPlugin");
		}
		// 多窗口导航插件
		addPlugIn(QueryNavigationPlugin.class.getName());
	}

	/**
	 * 初始化表格控件的一些状态
	 */
	private void initTableInputCtrl(boolean isFree) {
		if (!isFree)
			setOperationState(UfoReport.OPERATION_INPUT);
	}

//	public static void registerSystemPlugin(UfoReport _report) {
//		_report.addPlugIn(HeaderLockPlugin.class.getName());
//		_report.addPlugIn(FindReplacePlugin.class.getName());
//
//		if (_report.getOperationState() == UfoReport.OPERATION_FORMAT)
//			_report.addPlugIn(DataSetManagerPlugin.class.getName());
//
//		// 数据集的联查
//		_report.addPlugIn(TraceDataPlugin.class.getName());
//
//		_report.addActionExt(new NavPaneMngExt(_report));
//		_report.addPlugIn(HelpPlugin.class.getName());
//		_report.addPlugIn(MonitorPlugin.class.getName());
//
//	}

}
