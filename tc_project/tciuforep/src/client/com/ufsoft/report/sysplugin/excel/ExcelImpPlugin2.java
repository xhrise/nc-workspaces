package com.ufsoft.report.sysplugin.excel;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufsoft.report.util.MultiLang;

public class ExcelImpPlugin2 extends AbstractPlugin{//export
	
	public static final String GROUP = MultiLang.getString("import")+"/"+MultiLang.getString("export");

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[] {
				new ExcelImpAction()
		};
	}

	@Override
	public void shutdown() {
		
	}

	@Override
	public void startup() {
		
	}

}
