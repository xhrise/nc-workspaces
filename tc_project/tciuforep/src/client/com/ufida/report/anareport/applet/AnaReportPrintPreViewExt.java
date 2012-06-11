/**
 * 
 */
package com.ufida.report.anareport.applet;

import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.sysplugin.print.PrintPreViewExt;

/**
 * @author guogang
 * @created at Mar 5, 2009,6:43:32 PM
 *
 */
public class AnaReportPrintPreViewExt extends PrintPreViewExt {
	private AnaReportPlugin m_plugin = null;

	public AnaReportPrintPreViewExt(AnaReportPlugin plugin){
		super(plugin.getReport());
		this.m_plugin=plugin;
	}

	@Override
	public UfoCommand getCommand() {   
        return new AnaReportPrintPreViewCmd(m_plugin);
	}
	
	
}
