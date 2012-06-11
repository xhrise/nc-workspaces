package com.ufsoft.iufo.inputplugin.querynavigation;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;

public class UfoFormulaQueryNextPlugin extends AbstractPlugin{

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[]{new UfoFormulaQueryPreAction(),new UfoFormulaQueryNextAction()};
	}

	@Override
	public void shutdown() {
		
	}

	@Override
	public void startup() {
		
	}

}
