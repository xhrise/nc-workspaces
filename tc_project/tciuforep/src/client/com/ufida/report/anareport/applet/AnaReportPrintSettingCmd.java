/**
 * 
 */
package com.ufida.report.anareport.applet;

import com.ufsoft.report.sysplugin.print.PrintSettingCmd;

/**
 * @author ll
 *
 */
public class AnaReportPrintSettingCmd extends PrintSettingCmd{
	private AnaReportPlugin m_plugin = null;
	
	public AnaReportPrintSettingCmd(AnaReportPlugin plugin) {
		super(plugin.getReport());
		this.m_plugin=plugin;
	}
	
}
