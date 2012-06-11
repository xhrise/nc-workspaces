package com.ufida.report.free.applet;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.sysplugin.file.FileSaveCmd;

public class FreeRepSaveCmd extends FileSaveCmd {
	private FreeQueryDesignePlugin m_plugin;
	public FreeRepSaveCmd(UfoReport rep, FreeQueryDesignePlugin freePlugin) {
		super(rep);
		m_plugin = freePlugin;
	}

	public void execute(Object[] params) {
//		m_RepTool.store();
		m_plugin.store();
	}

}
