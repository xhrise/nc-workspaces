package com.ufsoft.iufo.inputplugin.biz;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;

public class UfoTotalSourcePlugin extends AbstractPlugin{
	protected IPluginAction[] createActions() {
		return new IPluginAction[]{new UfoTotalSourceLinkExt()};
	}

	public void shutdown() {
	}

	public void startup() {
	}
}
