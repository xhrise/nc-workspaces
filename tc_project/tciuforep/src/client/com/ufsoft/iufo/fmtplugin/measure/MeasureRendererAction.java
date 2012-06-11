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
 * ָ����չ������Ⱦ�������������V56�¿�ܼ�����
 * @author zhaopq
 * @created at 2009-4-22,����03:52:44
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
	 * miufo1000877 = ��ʽ
	 * miufo1004045=��ʾ��չ����
	 * miufo1000172=ָ��
	 */
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(StringResource.getStringResource("miufo1000877"), StringResource.getStringResource("miufo1004045"), FormulaPlugin.GROUP);
		descriptor.setName(StringResource.getStringResource("miufo1000172"));//"ָ��"
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
