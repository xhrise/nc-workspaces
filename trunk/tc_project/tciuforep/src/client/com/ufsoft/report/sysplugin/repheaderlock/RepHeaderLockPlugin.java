package com.ufsoft.report.sysplugin.repheaderlock;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
/**
 * �滻V55�ϲ��
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
