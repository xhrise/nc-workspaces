package com.ufsoft.report.sysplugin.viewmanager;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;

public class ViewManagerPlugin extends AbstractPlugin{

	@Override
	protected IPluginAction[] createActions() {
		
		return new IPluginAction[]{
				new ToolBarsViewManagerAction(),new StatusViewMngAction()
		};
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		
	}

}
