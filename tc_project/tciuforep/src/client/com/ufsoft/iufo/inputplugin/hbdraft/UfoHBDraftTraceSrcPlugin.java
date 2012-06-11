package com.ufsoft.iufo.inputplugin.hbdraft;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;

public class UfoHBDraftTraceSrcPlugin extends AbstractPlugin{

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[]{new UfoHBDraftTraceSrcAction()};
	}

	@Override
	public void shutdown() {
		
	}

	@Override
	public void startup() {
		
	}

}
