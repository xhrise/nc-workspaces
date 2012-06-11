package com.ufida.report.anareport.applet;

import nc.itf.iufo.freequery.IFreeQueryModel;

import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class AnaReportToDataPlugin extends AbstractPlugIn {

	@Override
	protected IPluginDescriptor createDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startup() {
		AnaReportPlugin plugin=getAanPlugin();
		if(plugin==null)
			return ;
		
		if (plugin.isFromQueryReport() || !plugin.isAegisFormat()){
			plugin.setOperationState(UfoReport.OPERATION_INPUT);
		}
		
	}
    
	private AnaReportPlugin getAanPlugin(){
		return (AnaReportPlugin)getReport().getPluginManager().getPlugin(AnaReportPlugin.class.getName());
	}
}
