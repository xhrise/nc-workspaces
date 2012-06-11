package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.AbsIUFORptDesignerPluginAction;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 公式定义组件，基于V56新框架技术。
 * 
 * @author zhaopq
 * @created at 2009-4-16,上午10:36:35
 * @since v56
 */
public class FormulaEditAction extends AbsIUFORptDesignerPluginAction {

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(StringResource
				.getStringResource("miufo1001692"), FormulaPlugin.GROUP);
		descriptor
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0));
		descriptor.setIcon("/images/reportcore/calculate.gif");
		descriptor.setName(StringResource.getStringResource("miufo1000909"));// "单元公式"
		descriptor.setExtensionPoints(new XPOINT[] { XPOINT.MENU,
				XPOINT.POPUPMENU });
		descriptor.setShowDialog(true);
		descriptor.setCompentFactory(new DefaultCompentFactory() {

			@Override
			protected JComponent createMenuItem(String strGroup,
					AbstractAction action) {
				JComponent stateChangeComp = super.createMenuItem(strGroup,
						action);
				stateChangeComp.putClientProperty(
						"nc.hotkey.display", "=");
				return stateChangeComp;
			}

		});

		return descriptor;
	}

	@Override
	public void execute(ActionEvent e) {
		if (!isEnabled()) {
			return;
		}
		new FormulaActionHandler(getCellsPane()).execute(null);
	}
}
