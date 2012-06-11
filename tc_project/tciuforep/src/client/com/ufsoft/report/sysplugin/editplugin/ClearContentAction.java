package com.ufsoft.report.sysplugin.editplugin;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsModel;

public class ClearContentAction extends AbsClearAction{

	@Override
	protected int getClearType() {
		return CellsModel.CELL_CONTENT;
	}

	@Override
	protected String getName() {
		return MultiLang.getString("uiuforep0001001");
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
        PluginActionDescriptor desc = (PluginActionDescriptor)super.getPluginActionDescriptor();
        desc.setExtensionPoints(XPOINT.MENU,XPOINT.TOOLBAR);
        desc.setIcon("/images/reportcore/clear.gif");
        desc.setToolTipText(MultiLang.getString("miufo1000103"));
        desc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        desc.setMemonic('A');
		return desc;
	}	
}
