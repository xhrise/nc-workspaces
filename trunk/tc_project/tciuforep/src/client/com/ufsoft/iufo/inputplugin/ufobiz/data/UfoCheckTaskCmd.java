package com.ufsoft.iufo.inputplugin.ufobiz.data;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.console.ActionHandler;
import com.ufsoft.iufo.check.vo.CheckResultVO;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoBizCmd;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.util.UfoPublic;

public class UfoCheckTaskCmd extends AbsUfoBizCmd {
	public UfoCheckTaskCmd(RepDataEditor editor){
		super(editor);
	}
	
	protected void executeIUFOBizCmd(RepDataEditor editor, Object[] params) {
		try{
			RepDataControler controler=RepDataControler.getInstance(editor.getMainboard());
    		CheckResultVO resultVO=(CheckResultVO)ActionHandler.exec("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "checkTask",
					new Object[]{editor.getRepDataParam(),controler.getLoginEnv(editor.getMainboard())});
    		controler.addTaskCheckResult(editor,resultVO);
			
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
