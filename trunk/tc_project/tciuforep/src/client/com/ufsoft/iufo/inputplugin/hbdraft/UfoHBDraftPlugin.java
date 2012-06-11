package com.ufsoft.iufo.inputplugin.hbdraft;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;

public class UfoHBDraftPlugin extends AbstractPlugin{

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[]{new UfoHBDraftAction()};
	}

	@Override
	public void shutdown() {
		
	}

	@Override
	public void startup() {
		
	}

}
