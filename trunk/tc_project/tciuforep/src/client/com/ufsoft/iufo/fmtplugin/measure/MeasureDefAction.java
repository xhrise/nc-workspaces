package com.ufsoft.iufo.fmtplugin.measure;

import java.awt.event.ActionEvent;

import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.AbsIUFORptDesignerPluginAction;
import com.ufsoft.iufo.fmtplugin.key.AbsEditorAction;
import com.ufsoft.iufo.resource.StringResource;
/**
 * ָ�궨�����������V56�¿�ܼ�����
 * @author zhaopq
 * @created at 2009-4-22,����02:01:24
 * @since v56
 */
public class MeasureDefAction extends AbsIUFORptDesignerPluginAction {

	@Override
	public void execute(ActionEvent e) {
		if(!isEnabled()){
			return;
		}
		AbsEditorAction editorAction = new MeasureDefEditorAction(getCellsPane());
    	editorAction.execute(editorAction.getParams());
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(StringResource.getStringResource("miufo1001692"), StringResource.getStringResource(MeasurePlugin.GROUP));
		descriptor.setName( StringResource.getStringResource("miufo1001689"));//ָ����ȡ
		descriptor.setIcon("images/reportcore/measure_def.png");
		descriptor.setExtensionPoints(new XPOINT[]{XPOINT.MENU,XPOINT.POPUPMENU,XPOINT.TOOLBAR});
		descriptor.setShowDialog(true);
		return descriptor;
	}
}
