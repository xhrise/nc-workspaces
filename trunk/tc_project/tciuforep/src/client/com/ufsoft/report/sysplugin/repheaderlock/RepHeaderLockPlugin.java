package com.ufsoft.report.sysplugin.repheaderlock;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
/**
 * Ìæ»»V55ÀÏ²å¼þ
 * @author wangyga
 *
 */
public class RepHeaderLockPlugin extends AbstractPlugin{

	@Override
	protected IPluginAction[] createActions() {
		
		return new IPluginAction[]{
				new AreaSeparateAction(),
				new AreaLockAction()
				
		};
	}

	@Override
	public void shutdown() {
		
	}

	@Override
	public void startup() {

	}

}
