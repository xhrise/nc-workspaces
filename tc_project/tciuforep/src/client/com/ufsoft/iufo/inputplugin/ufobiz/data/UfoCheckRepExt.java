package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoOpenedRepBizMenuExt;
import com.ufsoft.iufo.resource.StringResource;

public class UfoCheckRepExt extends AbsUfoOpenedRepBizMenuExt {

	public void execute(ActionEvent e) {
		new UfoCheckRepCmd(getRepDataEditor()).execute(null);
	}

	/**
	 * @i18n miufo1001626=…Û∫À
	 */
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor pad = new PluginActionDescriptor(MultiLangInput.getString("uiufotableinput0005"));
		pad.setGroupPaths(doGetDataMenuPaths(StringResource.getStringResource("miufo1001626")));
		pad.setExtensionPoints(XPOINT.MENU,XPOINT.TOOLBAR);
		pad.setIcon("images/reportcore/linkage.gif");
		pad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,KeyEvent.CTRL_MASK));
		pad.setMemonic('U');
		return pad;
	}

	public boolean isEnabled() {
		boolean bEnabled=super.isEnabled();
		if (bEnabled==false)
			return bEnabled;
		
		RepDataEditor editor=(RepDataEditor)getCurrentView();
		return editor.getMenuState()!=null && editor.getMenuState().isHasRepCheckFormula();
	}	
}
 