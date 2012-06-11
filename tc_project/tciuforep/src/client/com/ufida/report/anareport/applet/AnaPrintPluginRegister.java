/**
 * 
 */
package com.ufida.report.anareport.applet;

import com.ufsoft.iufo.fmtplugin.pluginregister.BatchPrintEditPluginRegister;
import com.ufsoft.report.UfoReport;
/**
 * @author guogang
 * @created at Feb 11, 2009,3:35:52 PM
 *
 */
public class AnaPrintPluginRegister extends BatchPrintEditPluginRegister{

	@Override
	public void register() {
		super.register();
        UfoReport report = getReport();
	    report.addPlugIn(AnaRenderEditorPlugin.class.getName());
	}

}
