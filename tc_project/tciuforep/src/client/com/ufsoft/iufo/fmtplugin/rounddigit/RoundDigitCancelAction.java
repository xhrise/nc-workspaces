package com.ufsoft.iufo.fmtplugin.rounddigit;

import java.awt.event.ActionEvent;

import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.AbsIUFORptDesignerPluginAction;
import com.ufsoft.iufo.resource.StringResource;
/**
 * 设置非舍位区域Action
 * @created at 2009-4-23,下午02:28:32
 * @since v56
 */
public class RoundDigitCancelAction extends AbsIUFORptDesignerPluginAction{

	@Override
	public void execute(ActionEvent e) {
		if(Boolean.FALSE.equals(RoundDigitUtil.isUnRoundDigitArea(getCellsModel()))){
			RoundDigitUtil.setUnRoundDigitArea(true,getCellsModel());
		}
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(StringResource.getStringResource("miufo1000877"), RoundDigitPlugin.GROUP);//miufo1000877:格式
		descriptor.setName(StringResource.getStringResource("uiiufofmt00070"));//设置非舍位区域
		descriptor.setExtensionPoints(new XPOINT[]{XPOINT.MENU});
		return descriptor;
	}

}
