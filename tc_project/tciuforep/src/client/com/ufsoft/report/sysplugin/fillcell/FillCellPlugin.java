package com.ufsoft.report.sysplugin.fillcell;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
/**
 * 
 * @author wangyga
 *
 */
public class FillCellPlugin extends AbstractPlugin{

	private FillCellHandler handler = new FillCellHandler(this);
	
	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[]{
				new FillToUpAction(this),
				new FillToDownAction(this),
				new FillToLeftAction(this),
				new FillToRightAction(this),
		};
	}

	@Override
	public void shutdown() {
		
	}

	@Override
	public void startup() {
		getEventManager().addListener(handler);		
	}

	FillCellHandler getHandler() {
		return handler;
	}
}
