package com.ufsoft.iufo.inputplugin.formula;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
/**
 * 数据录入态的公式插件
 * @author zhaopq
 * @created at 2009-4-15,上午09:19:41
 * @since v5.6
 */
public class FormulaInputPlugin extends AbstractPlugin{

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[] { 
				new FormulaInputToolBarAction() // 公式工具条
		};
	}
	@Override
	public void shutdown() {
		
	}

	@Override
	public void startup() {
	}

}
