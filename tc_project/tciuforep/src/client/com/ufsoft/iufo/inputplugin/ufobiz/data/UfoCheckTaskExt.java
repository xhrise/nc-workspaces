package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.event.ActionEvent;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoOpenedRepBizMenuExt;
import com.ufsoft.iufo.resource.StringResource;

public class UfoCheckTaskExt extends AbsUfoOpenedRepBizMenuExt {

	public void execute(ActionEvent e) {
		new UfoCheckTaskCmd(getRepDataEditor()).execute(null);
	}

	/**
	 * @i18n miufo1001626=…Û∫À
	 */
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor pad = new PluginActionDescriptor(MultiLangInput.getString("uiufotableinput0006"));
//		pad.setIcon("com/ufida/zior/resources/icon/undo.gif");
		pad.setGroupPaths(doGetDataMenuPaths(StringResource.getStringResource("miufo1001626")));
		pad.setExtensionPoints(XPOINT.MENU);
		pad.setMemonic('E');
		
		return pad;
	}
	
	public boolean isEnabled() {
		boolean bEnabled=super.isEnabled();
		if (bEnabled==false)
			return bEnabled;
		
		RepDataEditor editor=(RepDataEditor)getCurrentView();
		return editor.getMenuState()!=null && editor.getMenuState().isHasTaskCheckFormula();
	}	

}
 