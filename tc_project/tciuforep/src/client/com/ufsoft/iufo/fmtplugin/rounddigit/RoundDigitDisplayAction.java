package com.ufsoft.iufo.fmtplugin.rounddigit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

import com.ufida.zior.comp.KCheckBoxMenuItem;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.AbsIUFORptDesignerPluginAction;
import com.ufsoft.iufo.fmtplugin.rounddigitarea.RoundDigitAreaModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.CellsModel;
/**
 * 显示非舍位区域Action
 * @created at 2009-4-23,下午02:28:32
 * @since v56
 */
public class RoundDigitDisplayAction extends AbsIUFORptDesignerPluginAction{

	@Override
	public void execute(ActionEvent e) {
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(StringResource.getStringResource("miufo1000877"), RoundDigitPlugin.GROUP);//miufo1000877:格式
		descriptor.setName(StringResource.getStringResource("uiiufofmt00004"));//显示非舍位区
		descriptor.setExtensionPoints(new XPOINT[]{XPOINT.MENU});
		descriptor.setCompentFactory(new DefaultCompentFactory() {
			@Override
			protected JComponent createMenuItem(String strGroup, AbstractAction action) {
				KCheckBoxMenuItem item = new KCheckBoxMenuItem();
				item.setSelected(false);
				item.setGroup(strGroup);
				item.setAction(action);
				item.addActionListener(createActionListener(item));
				return item;
			}
		});
		return descriptor;
	}
	
	private ActionListener createActionListener(final JMenuItem checkBox) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CellsModel cellsModel = getCellsModel();
				RoundDigitAreaModel model = RoundDigitAreaModel.getInstance(cellsModel);						
				model.setDisplay(!model.isDisplay());
				checkBox.setSelected(model.isDisplay());
				cellsModel.fireExtPropChanged(null);
			}

		};
	}

}
