package com.ufsoft.report.sysplugin.editplugin;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.EditParameter;

public class CopyAllAction extends EditAction{

	@Override
	protected int getClipType() {
		return EditParameter.CELL_ALL;
	}

	@Override
	protected int getEditType() {
		return EditParameter.COPY;
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
//		PluginActionDescriptor des = new PluginActionDescriptor(MultiLang.getString("miufo1000362"));
		PluginActionDescriptor des = new PluginActionDescriptor(MultiLang.getString("miufo1000653"));
		des.setExtensionPoints(XPOINT.MENU,XPOINT.TOOLBAR,XPOINT.POPUPMENU);
//		des.setGroupPaths(new String[]{MultiLang.getString("edit"),MultiLang.getString("miufo1000653"),MultiLang.getString("edit")});
		des.setGroupPaths(new String[]{MultiLang.getString("edit"),MultiLang.getString("edit")});
		des.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
					KeyEvent.CTRL_MASK));
		des.setMemonic('C');
		des.setIcon("/images/reportcore/copy.gif");
		des.setToolTipText(MultiLang.getString("miufo1000653"));
		return des;
	}

}
