package com.ufsoft.report.sysplugin.editplugin;

import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.EditParameter;

public class CutFormatAction extends EditAction{

	@Override
	protected int getClipType() {
		return EditParameter.CELL_FORMAT;
	}

	@Override
	protected int getEditType() {
		return EditParameter.CUT;
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor des = new PluginActionDescriptor(MultiLang.getString("uiuforep0001002"));
		des.setExtensionPoints(XPOINT.MENU);
		des.setGroupPaths(new String[]{MultiLang.getString("edit"),MultiLang.getString("miufo1000654"),"edit"});
		return des;
	}

}
