package com.ufsoft.iufo.inputplugin.ufobiz;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoExcelExpExt;

public class UfoExcelExpPlugin extends AbstractPlugin {

	protected IPluginAction[] createActions() {
		return new IPluginAction[]{new UfoExcelExpExt()};
	}

	public void shutdown() {
	}

	public void startup() {
	}

}
