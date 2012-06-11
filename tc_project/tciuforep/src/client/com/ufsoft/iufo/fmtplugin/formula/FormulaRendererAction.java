package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

import com.ufida.zior.comp.KCheckBoxMenuItem;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.AbsIUFORptDesignerPluginAction;
import com.ufsoft.iufo.resource.StringResource;
/**
 * 公式扩展属性渲染开关组件，基于V56新框架技术。取代v55之前的<code>FormulaRendererExt</code>
 * @author zhaopq
 * @created at 2009-4-20,下午04:26:31
 * @since v56
 */
public class FormulaRendererAction extends AbsIUFORptDesignerPluginAction{
	
	@Override
	public void execute(ActionEvent e) {
		if(!isEnabled()){
			return;
		}
		FormulaDefRenderer.setFmlRendererVisible(!FormulaDefRenderer.isFmlRendererVisible());
		getCellsModel().fireExtPropChanged(null);
	}

	/**
	 * miufo1000877 = 格式
	 * miufo1004045=显示扩展属性
	 * uiufo20015=公式
	 */
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(StringResource.getStringResource("miufo1000877"), StringResource.getStringResource("miufo1004045"), FormulaPlugin.GROUP);
		descriptor.setName(StringResource.getStringResource("uiufo20015"));
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
