package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.event.ActionEvent;

import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoOpenedRepBizMenuExt;

public class UfoExportData2HtmlExt extends AbsUfoOpenedRepBizMenuExt {

	public void execute(ActionEvent e) {
		new UfoExpData2HtmlCmd(getRepDataEditor()).execute(null);
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor pad = new PluginActionDescriptor(MultiLangInput.getString("uiufotableinput0008"));
		pad.setGroupPaths(doGetExportMenuPaths("ddd"));
		pad.setExtensionPoints(XPOINT.MENU);
		pad.setMemonic('H');
		pad.setShowDialog(true);
		return pad;
	}

}
