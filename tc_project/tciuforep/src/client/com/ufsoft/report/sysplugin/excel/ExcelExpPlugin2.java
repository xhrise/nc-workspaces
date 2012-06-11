package com.ufsoft.report.sysplugin.excel;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
/**
 * 
 * @author zhaopq
 * @created at 2009-4-25,обнГ03:20:33
 * @since v56
 */
public class ExcelExpPlugin2 extends AbstractPlugin{

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[]{
				new ExcelExpAction()
		};
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void startup() {
	}

}
