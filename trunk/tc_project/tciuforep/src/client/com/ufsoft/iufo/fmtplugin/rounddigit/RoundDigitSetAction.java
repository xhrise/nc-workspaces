package com.ufsoft.iufo.fmtplugin.rounddigit;

import java.awt.event.ActionEvent;

import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.AbsIUFORptDesignerPluginAction;
import com.ufsoft.iufo.resource.StringResource;

/**
 * ������λ����Action
 * 
 * @created at 2009-4-23,����02:28:32
 * @since v56
 */
public class RoundDigitSetAction extends AbsIUFORptDesignerPluginAction {

	@Override
	public void execute(ActionEvent e) {
		if (Boolean.TRUE.equals(RoundDigitUtil
				.isUnRoundDigitArea(getCellsModel()))) {
			RoundDigitUtil.setUnRoundDigitArea(false, getCellsModel());
		}
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(StringResource
				.getStringResource("miufo1000877"), RoundDigitPlugin.GROUP);// miufo1000877:��ʽ
		descriptor.setName(StringResource.getStringResource("uiiufofmt00071"));// ������λ����
		descriptor.setExtensionPoints(new XPOINT[] { XPOINT.MENU });
		return descriptor;
	}
}
