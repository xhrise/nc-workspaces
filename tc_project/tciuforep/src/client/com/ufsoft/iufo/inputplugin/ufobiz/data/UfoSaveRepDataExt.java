package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.event.ActionEvent;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoOpenedRepBizMenuExt;
import com.ufsoft.report.util.MultiLang;

public class UfoSaveRepDataExt extends AbsUfoOpenedRepBizMenuExt {
	public void execute(ActionEvent e) {
		new UfoSaveRepDataCmd(getRepDataEditor()).execute(null);
	}

	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor pad = new PluginActionDescriptor(MultiLangInput.getString("uiuforep0000884"));
//		pad.setIcon("com/ufida/zior/resources/icon/undo.gif");
		pad.setGroupPaths(new String[]{MultiLang.getString("file")});
		pad.setExtensionPoints(XPOINT.MENU, XPOINT.TOOLBAR);
		
		return pad;
	}

	public boolean isEnabled() {
		boolean bEnabled=super.isEnabled();
		if (bEnabled==false)
			return bEnabled;
		
		RepDataEditor editor=getRepDataEditor();
		return editor.getMenuState()!=null && editor.getMenuState().isRepCanModify() && editor.getMenuState().isCommited()==false;
	}
}
