package com.ufsoft.report.fmtplugin.formula;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsPane;
/**
 * 
 * @author zhaopq
 * @created at 2009-4-16,上午10:23:13
 * @since v5.6
 */
public class AreaFormulaDefAction extends AbstractPluginAction {
	
	private CellsPane getCellsPane(){
		return ((ReportDesigner) getCurrentView()).getCellsPane();
	}

	@Override
	public void execute(ActionEvent e) {
		new AreaFormulaActionHandler(getCellsPane()).execute(null);
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(MultiLang.getString("format"), AreaFormulaDefPlugin.GROUP);
		descriptor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS,0));
		descriptor.setName(MultiLang.getString("miufo1000909"));//"单元公式"
		
		AreaFormulaDefPlugin plugin = (AreaFormulaDefPlugin)getPlugin();
		if(plugin.isPopMenuVisible()){
            descriptor.setExtensionPoints(new XPOINT[]{XPOINT.MENU,XPOINT.POPUPMENU});
        } else{
        	descriptor.setExtensionPoints(new XPOINT[]{XPOINT.MENU});
        }
		return descriptor;
	}

}
