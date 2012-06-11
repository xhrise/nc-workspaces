package com.ufsoft.report.sysplugin.cellpostil;


import com.ufida.zior.plugin.IPluginAction;
/**
 * 数据录入态时的批注插件
 * @author zhaopq
 * @created at 2009-4-21,下午03:56:19
 * @since v56
 */
public class CellPostilInputPlugin extends CellPostilDefPlugin{
	
	public CellPostilInputPlugin() {
		super(false);//数据录入态不能编辑批注
	}

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[]{
				new PostilControlAction(this),
				new PostilOneShowAction(this),
				new PostilOneHideAction(this)
		};
	}
}
