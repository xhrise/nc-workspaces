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

public class UfoCalExt extends AbsUfoOpenedRepBizMenuExt {
	public void execute(ActionEvent e) {
		new UfoCalCmd(getRepDataEditor(),false).execute(null);
	}

	/**
	 * @i18n miufo1000033=º∆À„
	 */
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor pad = new PluginActionDescriptor(MultiLangInput.getString("uiufotableinput0004"));
		pad.setGroupPaths(doGetDataMenuPaths(StringResource.getStringResource("miufo1000033")));
		pad.setIcon("/images/reportcore/calculator.gif");
		pad.setExtensionPoints(XPOINT.MENU,XPOINT.TOOLBAR);
		pad.setMemonic('C');
		pad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, KeyEvent.SHIFT_MASK));
		return pad;
	}

	public boolean isEnabled() {
		boolean bEnabled=super.isEnabled();
		if (bEnabled==false)
			return bEnabled;
		
		RepDataEditor editor=getRepDataEditor();
		return editor.getMenuState()!=null && editor.getMenuState().isCanCal() && editor.getMenuState().isCommited()==false;
	}
}
 