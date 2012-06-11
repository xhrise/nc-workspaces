package com.ufsoft.report.sysplugin.cellpostil;

import com.ufida.zior.plugin.IPlugin;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;

/**
 * 对当前选中的批注进行隐藏控制的插件
 */
public class PostilOneHideAction extends AbstractPostilAction {

	public PostilOneHideAction(IPlugin p) {
		super(p);
	}

	@Override
	protected void doAction() {
		CellPosition cellPos = getPostilManager().getSelectedLastCell(getCellsModel());
		getPostilManager().hidePostil(getCellsModel(), cellPos);
	}

	@Override
	public boolean isEnabled() {
		return getPostilManager().isHideEnable(getCellsPane());
	}

	/*
	 * miufo1004051 : 批注
	 */
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor desc = (PluginActionDescriptor) super
				.getPluginActionDescriptor();
		desc.setExtensionPoints(XPOINT.MENU, XPOINT.POPUPMENU);
		desc
				.setGroupPaths(new String[] { MultiLang.getString("format"),
						MultiLang.getString("miufo1004051"),
						CellPostilDefPlugin.GROUP });
		return desc;
	}

	@Override
	protected String getName() {
		return MultiLang.getString("uiuforep0001115");
	}

}
