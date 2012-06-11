package com.ufida.report.anareport.applet;

import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.report.sysplugin.combinecell.CombineCellPlugin;
import com.ufsoft.table.CellsModel;

public class AnaCombineCellPlugin extends CombineCellPlugin{

	@Override
	public CellsModel getCellsModel() {
		return getAanRepModel().getFormatModel();
	}

	private AnaReportModel getAanRepModel() {
		return getAanPlugin().getModel();
	}

	private AnaReportPlugin getAanPlugin() {
		return (AnaReportPlugin) getReport().getPluginManager().getPlugin(
				AnaReportPlugin.class.getName());
	}
}
