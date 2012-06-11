package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoOpenedRepBizMenuExt;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.CellPosition;

public class UfoTraceSubExt extends AbsUfoOpenedRepBizMenuExt {

	public void execute(ActionEvent e) {
		RepDataEditor editor=getRepDataEditor();
		CellPosition[] cells=editor.getCellsModel().getSelectModel().getSelectedCells();
		new UfoTraceSubCmd(editor).execute(cells);
	}

	/**
	 * @i18n miufohbbb00101=查看汇总下级
	 */
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor pad = new PluginActionDescriptor(StringResource.getStringResource("miufohbbb00101"));
		pad.setGroupPaths(doGetDataMenuPaths("traceDataGroup"));
		pad.setCompentFactory(new ComponentFactory());
		pad.setExtensionPoints(XPOINT.MENU);
		pad.setMemonic('T');
		pad.setShowDialog(true);
		return pad;
	}

	public boolean isEnabled() {
		boolean bEnabled=super.isEnabled();
		if (bEnabled==false)
			return bEnabled;
		
		RepDataEditor editor=getRepDataEditor();
		String strGeneralQuery=(String)editor.getContext().getAttribute(IUfoContextKey.GENRAL_QUERY);
		if ("true".equalsIgnoreCase(strGeneralQuery)==false)
			return false;
		
		return editor.getMenuState()!=null && editor.getMenuState().getDataVer()==0;
	}
	
	private class ComponentFactory extends DefaultCompentFactory{

		@Override
		protected JComponent createToolBarItem(String[] paths, JComponent root,
				AbstractAction action) {
			return super.createToolBarItem(paths, root, action);
		}

		
		
	}

}
 