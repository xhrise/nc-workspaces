package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoOpenedRepBizMenuExt;
import com.ufsoft.iufo.resource.StringResource;

public class UfoAreaCalExt extends AbsUfoOpenedRepBizMenuExt {

	public void execute(ActionEvent e) {
		new UfoCalCmd(getRepDataEditor(),true).execute(null);
	}

	/**
	 * @i18n miufo1000033=º∆À„
	 */
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor pad = new PluginActionDescriptor(MultiLangInput.getString("uiufotableinput0003"));
		pad.setGroupPaths(doGetDataMenuPaths(StringResource.getStringResource("miufo1000033")));
		pad.setExtensionPoints(XPOINT.MENU);
		pad.setMemonic('A');
		return pad;
	}

	public boolean isEnabled() {
		boolean bEnabled=super.isEnabled();
		if (bEnabled==false)
			return bEnabled;
		
		RepDataEditor editor=(RepDataEditor)getCurrentView();
		return editor.getMenuState()!=null && editor.getMenuState().isCanAreaCal() && editor.getMenuState().isCommited()==false;
	}
}
 