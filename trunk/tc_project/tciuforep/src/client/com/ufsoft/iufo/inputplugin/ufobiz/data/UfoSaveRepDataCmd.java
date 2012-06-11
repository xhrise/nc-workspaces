package com.ufsoft.iufo.inputplugin.ufobiz.data;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.comp.KStatusBar;
import com.ufida.zior.console.ActionHandler;
import com.ufsoft.iufo.check.vo.CheckResultVO;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.i18n.MultiLangUtil;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoBizCmd;
import com.ufsoft.iufo.inputplugin.ufodynarea.AbsUfoDynAreaActionExt;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.repdatainput.RepDataOperResultVO;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellPosition;

public class UfoSaveRepDataCmd extends AbsUfoBizCmd{
	public UfoSaveRepDataCmd(RepDataEditor editor){
		super(editor);
	}
	
	private boolean isSucceedSave = true;
	
    /**
     * @i18n i18n save=保存
     */
    protected void executeIUFOBizCmd(final RepDataEditor editor, Object[] params) {
		Runnable runnable=new Runnable() {
			public void run() {
				try{
					MultiLangUtil.saveLanguage((String)UfoSaveRepDataCmd.this.getRepDataEditor().getMainboard().getContext().getAttribute(IUfoContextKey.CURRENT_LANG));
					save(editor);
				} catch(Throwable e){
					UfoPublic.sendMessage(e, editor.getMainboard());
				}
			}
		};
		
		((KStatusBar)editor.getMainboard().getStatusBar()).processDisplay(MultiLang.getString("save"),runnable);
    }
    
	/**
	 * @i18n uiuforep00136=保存成功，审核未通过
	 * @i18n uiuforep00137=保存成功，审核通过
	 * @i18n uiuforep00115=审核结果
	 */
	public boolean save(RepDataEditor editor) {
		if (!stopCellEditing(editor))
			return false;
		
    	if (!AbsUfoDynAreaActionExt.verifyBeforeSave(editor))
    		return false;		
		
		isSucceedSave=false;
    	try{
			CellPosition anchorCell=null;
			if (editor.getCellsModel().getSelectModel()!=null)
				anchorCell=editor.getCellsModel().getSelectModel().getAnchorCell();
			
    		RepDataOperResultVO resultVO=(RepDataOperResultVO)ActionHandler.execWithZip("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "saveRepData",
					new Object[]{editor.getCellsModel(),editor.getRepDataParam(),RepDataControler.getInstance(editor.getMainboard()).getLoginEnv(editor.getMainboard()),editor.getHashDynAloneID()});
    		while(true){
    			if (resultVO.getThreadID()==null)
    				break;
    			synchronized(this){
    				try{
    					wait(3*1000);
    				}catch(Exception e){}
    			}
    			resultVO=(RepDataOperResultVO)ActionHandler.execWithZip("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "reSaveRepData",resultVO.getThreadID());
    		}
    		
    		if (resultVO.isOperSuccess()){
    			resultVO.getCellsModel().setDirty(false);
    			editor.setCellsModel(resultVO.getCellsModel());
    			editor.setHashDynAloneID(resultVO.getHashDynAloneID());
    			
    			if (resultVO.getCheckResult()!=null)
    				editor.setRepCheckResult(resultVO.getCheckResult());
    			
				String strMessage=null;
				if (resultVO.getCheckResult()==null)
					strMessage = StringResource.getStringResource("miufopublic391"); 
				else if (resultVO.getCheckResult().getCheckState()==CheckResultVO.NOPASS)
					strMessage = MultiLang.getString("uiuforep00136");
				else if (resultVO.getCheckResult().getCheckState()==CheckResultVO.PASS)
					strMessage = MultiLang.getString("uiuforep00137");  
					
    			editor.setHintMessage(strMessage); 
    		}else{
    			UfoPublic.sendErrorMessage(resultVO.getHintMessage(),editor.getMainboard(),null);
    		}
    		isSucceedSave=resultVO.isOperSuccess();
    		editor.doSetFirstAnchorCell(anchorCell);
    		
    		editor.revalidate();
    		editor.repaint();
    	}catch(Exception e){
    		UfoPublic.sendMessage(StringResource.getStringResource("miufopublic398")+":"+e.getMessage(), editor.getMainboard());
    	}
		return isSucceedSave;
	}
	
	public boolean isSucceedSave() {
		return isSucceedSave;
	}
}
