package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.event.ActionEvent;

import com.ufida.dataset.IContext;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.AbsIUFORptDesignerPluginAction;
import com.ufsoft.iufo.resource.StringResource;

public class BatchFormulaAction extends AbsIUFORptDesignerPluginAction {
	
	@Override
	public void execute(ActionEvent e) {
		if(!isEnabled()){
			return;
		}
		IContext context = getCurrentView().getContext();
        
        boolean isAnaRep = context.getAttribute(ANA_REP) == null ? false : Boolean.parseBoolean(context.getAttribute(ANA_REP).toString());
        BatchFmlDlg editDlg = new BatchFmlDlg(getCellsPane(), getCellsModel(),context, isAnaRep,null);
        editDlg.setModal(true);
        editDlg.setVisible(true);
    }

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(StringResource.getStringResource("miufo1001692"), FormulaPlugin.GROUP);
		descriptor.setName(StringResource.getStringResource("miufo1000966"));//"批量公式"
		descriptor.setExtensionPoints(new XPOINT[]{XPOINT.MENU});
		descriptor.setShowDialog(true);
		return descriptor;
	}

}
