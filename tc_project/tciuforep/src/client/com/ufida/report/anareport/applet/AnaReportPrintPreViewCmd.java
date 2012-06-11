/**
 * 
 */
package com.ufida.report.anareport.applet;

import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.report.sysplugin.print.PrintPreViewCmd;

/**
 * @author guogang
 * @created at Mar 5, 2009,6:45:42 PM
 *
 */
public class AnaReportPrintPreViewCmd extends PrintPreViewCmd {

	private AnaReportPlugin m_plugin = null;
	
	public AnaReportPrintPreViewCmd(AnaReportPlugin plugin) {
		super(plugin.getReport());
		this.m_plugin=plugin;
	}

	@Override
	public void execute(Object[] params) {
		AnaReportModel reportModel=m_plugin.getModel();
		if(reportModel.loadAllData(false)){
			m_plugin.changeUIState(reportModel.getOperationState());
		}
		
		super.execute(params);
	}
	
	
}
