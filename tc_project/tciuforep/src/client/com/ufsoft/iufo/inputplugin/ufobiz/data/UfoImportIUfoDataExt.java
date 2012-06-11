package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.event.ActionEvent;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.inputplugin.biz.file.ChangeKeywordsExt;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoOpenedRepBizMenuExt;
import com.ufsoft.iuforeport.repdatainput.LoginEnvVO;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellsModel;

public class UfoImportIUfoDataExt extends AbsUfoOpenedRepBizMenuExt {

	@Override
	public void execute(ActionEvent e) {
		RepDataEditor editor=getRepDataEditor();
		try{
			LoginEnvVO loginEnv=RepDataControler.getInstance(editor.getMainboard()).getLoginEnv(editor.getMainboard());
			String[] strKeyVals=ChangeKeywordsExt.doGetInputKeyValus_New(editor.getRepDataParam(),loginEnv,editor);
			if (strKeyVals==null || strKeyVals.length<=0)
				return;
			
			CellsModel cellsModel=(CellsModel)ActionHandler.execWithZip("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "importIUFOData",
					new Object[]{editor.getRepDataParam(),loginEnv,strKeyVals});
			cellsModel.setDirty(true);
			editor.setCellsModel(cellsModel);
		}catch(Exception te){
			AppDebug.debug(te);
			UfoPublic.sendErrorMessage(te.getMessage(),editor,null);
		}
	}

	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor pad = new PluginActionDescriptor(MultiLangInput.getString("uiufotableinput0010"));
		pad.setGroupPaths(doGetImportMenuPaths("ddd"));
		pad.setExtensionPoints(XPOINT.MENU);
		pad.setMemonic('U');
		pad.setShowDialog(true);
		return pad;
	}

	public boolean isEnabled() {
		boolean bEnabled=super.isEnabled();
		if (bEnabled==false)
			return bEnabled;
		
		RepDataEditor editor=(RepDataEditor)getCurrentView();
		return editor.getMenuState()!=null && editor.getMenuState().isRepCanModify() && editor.getMenuState().isCommited()==false;
	}

}
