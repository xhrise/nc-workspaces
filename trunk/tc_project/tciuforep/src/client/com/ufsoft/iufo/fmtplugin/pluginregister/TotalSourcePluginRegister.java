package com.ufsoft.iufo.fmtplugin.pluginregister;

import com.ufsoft.iufo.inputplugin.biz.TotalSourcePlugin;
import com.ufsoft.report.PluginRegister;
import com.ufsoft.report.sysplugin.excel.ExcelExpPlugin;
import com.ufsoft.report.sysplugin.print.PrintPlugin;

public class TotalSourcePluginRegister extends PluginRegister {

	public TotalSourcePluginRegister(){
		
	}
	
	
	public void register() {
		getReport().addPlugIn(PrintPlugin.class.getName());        //��ӡ��ز˵���ҳ�����ã���ӡԤ������ӡ
		getReport().addPlugIn(TotalSourcePlugin.class.getName());
		getReport().addPlugIn(ExcelExpPlugin.class.getName());
	}
	
	 
}
