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
 * ��ʽ��չ������Ⱦ�������������V56�¿�ܼ�����ȡ��v55֮ǰ��<code>FormulaRendererExt</code>
 * @author zhaopq
 * @created at 2009-4-20,����04:26:31
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
	 * miufo1000877 = ��ʽ
	 * miufo1004045=��ʾ��չ����
	 * uiufo20015=��ʽ
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
