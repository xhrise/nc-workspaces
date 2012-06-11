/**
 * 
 */
package com.ufida.report.anareport.applet;

import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.sysplugin.print.PrintSettingExt;

/**
 * @author ll
 *
 */
public class AnaReportPrintSettingExt extends PrintSettingExt{
	private AnaReportPlugin m_plugin = null;

	/**
	 * @param rep
	 */
	public AnaReportPrintSettingExt(AnaReportPlugin plugin) {
		super(plugin.getReport());
		this.m_plugin=plugin;
	}
	@Override
	public UfoCommand getCommand() {   
        return new AnaReportPrintSettingCmd(m_plugin);
	}
}
