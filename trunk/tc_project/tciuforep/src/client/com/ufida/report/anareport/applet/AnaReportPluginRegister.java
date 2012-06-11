package com.ufida.report.anareport.applet;

import nc.ui.iufo.query.datasetmanager.DataSetManagerPlugin;
import nc.vo.bi.report.manager.ReportResource;

import com.ufida.report.anareport.edit.AnaEditPlugin;
import com.ufida.report.anareport.fmtplugin.AnaAreaFormulaPlugin;
import com.ufsoft.iufo.fmtplugin.monitor.MonitorPlugin;
import com.ufsoft.iufo.fmtplugin.window.NavPaneMngExt;
import com.ufsoft.iufo.fmtplugin.window.NavPaneMngToolBarExt;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.sysplugin.findreplace.FindReplacePlugin;
import com.ufsoft.report.sysplugin.headerlock.HeaderLockPlugin;
import com.ufsoft.report.sysplugin.help.HelpPlugin;
import com.ufsoft.report.sysplugin.trace.TraceDataPlugin;

/**
 * 分析报表的插件注册管理
 * 
 * @author ll
 * 
 */
public class AnaReportPluginRegister {
	private UfoReport _report;

	public AnaReportPluginRegister(UfoReport report) {
		_report = report;
	}

	public void registerPlugIn() {
		registerAnaPlugin();
	}

	private void registerAnaPlugin() {
		if (_report.getOperationState() == UfoReport.OPERATION_FORMAT)
			_report.addPlugIn(DataSetManagerPlugin.class.getName());
		// 保证插件的加载顺序
		_report.addPlugIn(AnaExAreaPlugin.class.getName());
		_report.addPlugIn(AnaReportPlugin.class.getName());
		// 数据集的联查
		Object obj=_report.getContext().getAttribute(ReportResource.OPEN_IN_MODAL_DIALOG);
		if(obj==null||(Integer)obj<0){
			_report.addPlugIn(TraceDataPlugin.class.getName());
			
		}
		_report.addActionExt(new NavPaneMngToolBarExt(_report));
		_report.addActionExt(new NavPaneMngExt(_report));
		
		_report.addPlugIn(AnaCellAttrToolBarPlugIn.class.getName());
		_report.addPlugIn(AnaEditPlugin.class.getName());
		_report.addPlugIn(AnaAreaFormulaPlugin.class.getName());
		_report.addPlugIn(AnaRenderEditorPlugin.class.getName());
		_report.addPlugIn(AnaChartPlugin.class.getName()); // 图表插件注册了绘制器。
		_report.addPlugIn(AnaCellAttrPlugin.class.getName());
		_report.addPlugIn(AnaInsertDeletePlugin.class.getName());
		_report.addPlugIn(AnaFieldCountInfoPlugin.class.getName());
		_report.addPlugIn(AnaCombineCellPlugin.class.getName());
		_report.addPlugIn(HeaderLockPlugin.class.getName());
		_report.addPlugIn(FindReplacePlugin.class.getName());
		_report.addPlugIn(HelpPlugin.class.getName());
		_report.addPlugIn(MonitorPlugin.class.getName());
		_report.addPlugIn(AnaReportToDataPlugin.class.getName());
	} 

}
