package com.ufsoft.iufo.inputplugin.ufobiz.data;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.console.ActionHandler;
import com.ufsoft.iufo.check.vo.CheckResultVO;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoBizCmd;
import com.ufsoft.iufo.inputplugin.ufodynarea.AbsUfoDynAreaActionExt;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.util.UfoPublic;

public class UfoCheckRepCmd extends AbsUfoBizCmd {

	public UfoCheckRepCmd(RepDataEditor editor){
		super(editor);
	}
	
	protected void executeIUFOBizCmd(RepDataEditor editor, Object[] params) {
    	if (!stopCellEditing(editor))
    		return;
    	
    	if (!AbsUfoDynAreaActionExt.verifyBeforeSave(editor))
    		return;

		try{
    		CheckResultVO resultVO=(CheckResultVO)ActionHandler.execWithZip("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "checkReport",
					new Object[]{editor.getRepDataParam(),RepDataControler.getInstance(editor.getMainboard()).getLoginEnv(editor.getMainboard()),editor.getCellsModel(),!editor.isDirty()});
    		editor.setRepCheckResult(resultVO);

    		String strMessage=null;
    		if (resultVO.getCheckState()==CheckResultVO.PASS)
				strMessage = StringResource.getStringResource("miufo1000125");
			else
				strMessage = StringResource.getStringResource("miufo1000126");
    		UfoPublic.sendMessage(strMessage,editor.getMainboard());
    	}catch(Exception e){
    		UfoPublic.sendMessage(e.getMessage(), editor.getMainboard());
    	} 		
	}
}
