package com.ufsoft.iufo.fmtplugin.excel;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;

public class ExcelImpPlugin extends AbstractPlugin{

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[]{
				new IUFOExcelImpAction()
		};
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void startup() {
	}

}
