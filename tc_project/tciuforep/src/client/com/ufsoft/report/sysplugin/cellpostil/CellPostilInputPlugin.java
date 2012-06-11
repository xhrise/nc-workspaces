package com.ufsoft.report.sysplugin.cellpostil;


import com.ufida.zior.plugin.IPluginAction;
/**
 * ����¼��̬ʱ����ע���
 * @author zhaopq
 * @created at 2009-4-21,����03:56:19
 * @since v56
 */
public class CellPostilInputPlugin extends CellPostilDefPlugin{
	
	public CellPostilInputPlugin() {
		super(false);//����¼��̬���ܱ༭��ע
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
