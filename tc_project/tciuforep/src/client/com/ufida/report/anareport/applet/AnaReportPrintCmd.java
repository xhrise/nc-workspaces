/**
 * 
 */
package com.ufida.report.anareport.applet;

import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.report.sysplugin.print.PrintCmd;

/**
 * @author guogang
 * @created at Mar 5, 2009,6:42:02 PM
 *
 */
public class AnaReportPrintCmd extends PrintCmd {

    private AnaReportPlugin m_plugin = null;
	
	public AnaReportPrintCmd(AnaReportPlugin plugin){
		super(plugin.getReport());
		this.m_plugin=plugin;
	}

	@Override
	public void execute(Object[] params) {
		AnaReportModel reportModel=m_plugin.getModel();
		boolean changeData = reportModel.loadAllData(false);
		if(changeData){
			m_plugin.changeUIState(reportModel.getOperationState());
		}
		super.execute(params);
		
		if(changeData){
			reportModel.changeState(reportModel.isFormatState(), false);
			m_plugin.changeUIState(reportModel.getOperationState());
		}
		
	}
	
	
}
