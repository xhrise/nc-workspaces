package com.ufsoft.iufo.fmtplugin.statusshow;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;

public class StatusBarPlugin extends AbstractPlugin{

	@Override
	protected IPluginAction[] createActions() {
       
		return new IPluginAction[]{new CellPositionStatusBarAction(),
				new RoundDigitStatusAction(),
				new FormatRightStatusAction(),
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
