/**
 * 
 */
package com.ufida.report.anareport.applet;

import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.sysplugin.print.PrintExt;

/**
 * @author guogang
 * @created at Mar 5, 2009,6:40:14 PM
 *
 */
public class AnaReportPrintExt extends PrintExt {

	private AnaReportPlugin m_plugin = null;
	
	public AnaReportPrintExt(AnaReportPlugin plugin){
		super(plugin.getReport());
		this.m_plugin=plugin;
	}

	@Override
	public UfoCommand getCommand() {
		return new AnaReportPrintCmd(m_plugin);
	}
	
	
}
