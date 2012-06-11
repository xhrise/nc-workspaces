package com.ufsoft.iufo.fmtplugin.statusshow;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;

public class RepInputStatusBarPlugin extends AbstractPlugin{
	
	public static final String NAME = "statusbar";

	@Override
	protected IPluginAction[] createActions() {
       
		return new IPluginAction[]{new CellPositionStatusBarAction(),
				new RoundDigitStatusAction(),
				new DataSourceStatusAction()};
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
