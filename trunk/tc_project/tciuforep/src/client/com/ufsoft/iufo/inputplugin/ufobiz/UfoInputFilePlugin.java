package com.ufsoft.iufo.inputplugin.ufobiz;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoDSInfoSetExt;

public class UfoInputFilePlugin extends AbstractPlugin {

	protected IPluginAction[] createActions() {
		return new IPluginAction[]{new UfoDSInfoSetExt()};
	}

	public void shutdown() {
	}

	public void startup() {
	}
}
