package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.event.ActionEvent;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoOpenedRepBizMenuExt;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.CellPosition;

public class UfoTraceDataExt extends AbsUfoOpenedRepBizMenuExt {

	public void execute(ActionEvent e) {
		RepDataEditor editor=getRepDataEditor();
		boolean bTotal=editor.getMenuState().getDataVer()==350;
		
    	CellPosition[] cells=editor.getCellsModel().getSelectModel().getSelectedCells();
    	new UfoTraceDataCmd(editor).execute(new Object[]{Boolean.valueOf(bTotal),cells});
	}

	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor pad = new PluginActionDescriptor(StringResource.getStringResource("miufotableinput0003"));
		pad.setGroupPaths(doGetDataMenuPaths("traceDataGroup"));
		pad.setExtensionPoints(XPOINT.MENU);
		pad.setMemonic('S');
		pad.setShowDialog(true);
		return pad;
	}

	public boolean isEnabled() {
		boolean bEnabled=super.isEnabled();
		if (bEnabled==false)
			return bEnabled;
		
		RepDataEditor editor=getRepDataEditor();
		return editor.getMenuState()!=null && (editor.getMenuState().getDataVer()==350 || editor.getMenuState().isCanTraceData());
	}
}
