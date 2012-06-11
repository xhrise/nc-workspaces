package com.ufsoft.iufo.fmtplugin.datapreview;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
/**
 * 格式设计时的数据态浏览
 * @author zhaopq
 */
public class DataPreviewPlugin extends AbstractPlugin {


	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[]{new DataPreviewAction()};
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
