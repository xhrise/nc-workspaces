package com.ufsoft.report.sysplugin.cellpostil;

import com.ufida.zior.plugin.IPlugin;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;

/**
 * ±à¼­Åú×¢
 */
public class PostilEditAction extends AbstractPostilAction{

	public PostilEditAction(IPlugin p) {
		super(p);
	}

	@Override
	protected void doAction() {
		CellPosition cellPos = getPostilManager().getSelectedLastCell(getCellsModel());
		getPostilManager().editPostil(getCellsPane(),cellPos);
	}
	
	@Override
	public boolean isEnabled() {
		return getPostilManager().isEditeEnable(getCellsModel());
	}
	/*
	 * miufo1004051 : Åú×¢
	 */
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor desc = (PluginActionDescriptor)super.getPluginActionDescriptor();
		desc.setExtensionPoints(XPOINT.MENU,XPOINT.POPUPMENU);
		desc.setGroupPaths(new String[]{MultiLang.getString("format"),MultiLang.getString("miufo1004051"),CellPostilDefPlugin.GROUP});
		return desc;
	}

	@Override
	protected String getName() {
		return MultiLang.getString("uiuforep0001101");
	}

}
