package com.ufsoft.iufo.fmtplugin.measure;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

import com.ufida.zior.comp.KCheckBoxMenuItem;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.AbsIUFORptDesignerPluginAction;
import com.ufsoft.iufo.fmtplugin.formula.FormulaPlugin;
import com.ufsoft.iufo.resource.StringResource;
/**
 * 指标扩展属性渲染开关组件，基于V56新框架技术。
 * @author zhaopq
 * @created at 2009-4-22,下午03:52:44
 * @since v56
 */
public class MeasureRendererAction extends AbsIUFORptDesignerPluginAction {

	@Override
	public void execute(ActionEvent e) {
		if(!isEnabled()){
			return;
		}
		MeasureDefRender.setFmlRendererVisible(!MeasureDefRender.isFmlRendererVisible());
		getCellsModel().fireExtPropChanged(null);
	}

	/**
	 * miufo1000877 = 格式
	 * miufo1004045=显示扩展属性
	 * miufo1000172=指标
	 */
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(StringResource.getStringResource("miufo1000877"), StringResource.getStringResource("miufo1004045"), FormulaPlugin.GROUP);
		descriptor.setName(StringResource.getStringResource("miufo1000172"));//"指标"
		descriptor.setExtensionPoints(new XPOINT[]{XPOINT.MENU});
		descriptor.setCompentFactory(new DefaultCompentFactory() {
			@Override
			protected JComponent createMenuItem(String strGroup, AbstractAction action) {
				KCheckBoxMenuItem item = new KCheckBoxMenuItem();
				item.setSelected(true);
				item.setGroup(strGroup);
				item.setAction(action);
				return item;
			}
		});
		return descriptor;
	}

}
