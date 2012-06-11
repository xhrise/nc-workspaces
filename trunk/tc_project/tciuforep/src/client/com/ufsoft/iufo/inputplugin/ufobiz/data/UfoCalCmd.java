package com.ufsoft.iufo.inputplugin.ufobiz.data;

import javax.swing.SwingUtilities;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.comp.KStatusBar;
import com.ufida.zior.console.ActionHandler;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.i18n.MultiLangUtil;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoBizCmd;
import com.ufsoft.iufo.inputplugin.ufodynarea.AbsUfoDynAreaActionExt;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.repdatainput.RepDataOperResultVO;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellPosition;

public class UfoCalCmd extends AbsUfoBizCmd {
	private boolean m_bAreaCal=false;
	public UfoCalCmd(RepDataEditor editor,boolean bAreaCal){
		super(editor);
		m_bAreaCal=bAreaCal;
	}
	
	private void doExecute(){
		MultiLangUtil.saveLanguage((String)editor.getMainboard().getContext().getAttribute(IUfoContextKey.CURRENT_LANG));
		try{
			CellPosition anchorCell=null;
			if (editor.getCellsModel().getSelectModel()!=null)
				anchorCell=editor.getCellsModel().getSelectModel().getAnchorCell();
			
			while (true){
				boolean bCanCal=(Boolean)ActionHandler.exec("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "canStartCalc","");
				if (bCanCal)
					break;
				
    			synchronized(this){
    				try{
    					wait(1500);
    				}catch(Exception e){}
    			}
			}
			
			RepDataOperResultVO resultVO=(RepDataOperResultVO)ActionHandler.execWithZip("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "performCalc",
					new Object[]{editor.getCellsModel(),editor.getRepDataParam(),RepDataControler.getInstance(editor.getMainboard()).getLoginEnv(editor.getMainboard()),m_bAreaCal});
			
    		while(true){
    			if (resultVO.getThreadID()==null)
    				break;
    			synchronized(this){
    				try{
    					wait(1500);
    				}catch(Exception e){}
    			}
    			resultVO=(RepDataOperResultVO)ActionHandler.execWithZip("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "reCalcRepData",resultVO.getThreadID());
    		}
    		if (resultVO.isOperSuccess()){
    			resultVO.getCellsModel().setDirty(true);
    			editor.setCellsModel(resultVO.getCellsModel());
    			UfoPublic.sendMessage(StringResource.getStringResource("miufo1002854"),editor.getMainboard()); 
    		}else{
    			UfoPublic.sendErrorMessage(resultVO.getHintMessage(),editor.getMainboard(),null);
    		}
    		
    		SwingUtilities.invokeLater(new LocateRunnable(anchorCell));
    	}catch(Exception e){
    		UfoPublic.sendMessage(e.getMessage(), editor.getMainboard());
    	}
	}
	
	private class LocateRunnable implements Runnable{
		CellPosition anchorCell=null;
		LocateRunnable(CellPosition anchorCell){
			this.anchorCell=anchorCell;
		}
		
		public void run() {
	    	editor.requestFocus();
    		editor.doSetFirstAnchorCell(anchorCell);
		}
	}
	
	protected void executeIUFOBizCmd(RepDataEditor editor, Object[] params) {
		if(editor == null){
			return ;
		}
    	if (!stopCellEditing(editor))
    		return;
    	
    	if (!AbsUfoDynAreaActionExt.verifyBeforeSave(editor))
    		return;    	

		Runnable runnable=new Runnable() {
			public void run() {
				doExecute();
			}
		};
		// @edit by wangyga at 2009-8-24,ÏÂÎç12:56:02
		editor.getCellsPane().setEnabled(false);
		((KStatusBar)editor.getMainboard().getStatusBar()).processDisplay(StringResource.getStringResource("miufo1000033"),runnable);
		editor.getCellsPane().setEnabled(true);
	}
}
