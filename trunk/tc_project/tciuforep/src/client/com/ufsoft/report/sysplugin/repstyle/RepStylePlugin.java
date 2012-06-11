/**
 * 
 */
package com.ufsoft.report.sysplugin.repstyle;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;

/**
 * @author wangyga
 * 报表显示风格插件，新框架插件机制替代以前的
 * @created at 2009-9-3,上午11:24:23
 *
 */
public class RepStylePlugin extends AbstractPlugin{

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[]{new RepStyleAction(),
				new RepDisplayPercentAction()};
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
