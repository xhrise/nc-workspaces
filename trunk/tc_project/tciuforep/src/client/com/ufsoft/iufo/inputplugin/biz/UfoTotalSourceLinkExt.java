package com.ufsoft.iufo.inputplugin.biz;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.inputplugin.biz.data.TotalSourceLinkCmd;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.iufo.resource.StringResource;

public class UfoTotalSourceLinkExt extends AbstractPluginAction{
	public void execute(ActionEvent e) {
		ReportDesigner view=(ReportDesigner)this.getCurrentView();
		if (view==null || view.getCellsModel()==null || view.getCellsModel().getSelectModel()==null 
				|| view.getCellsModel().getSelectModel().getAnchorCell()==null){
            String strAlert = MultiLangInput.getString("miufotableinput0004");//请选择一个单元格
            JOptionPane.showMessageDialog(view,strAlert);
            return;
		}
		TotalSourceLinkCmd.doTraceTotalSource(true, view.getContext(), view.getCellsModel(), view, view.getCellsModel().getSelectModel().getAnchorCell());
	}

	/**
	 * @i18n miufohbbb00104=联查报表
	 */
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor pad = new PluginActionDescriptor(StringResource.getStringResource("miufohbbb00104"));
		pad.setGroupPaths(new String[]{MultiLang.getString("data"),"linkReport"});
		pad.setExtensionPoints(XPOINT.MENU);
		pad.setMemonic('R');
		pad.setShowDialog(true);
		return pad;
	}

}
 