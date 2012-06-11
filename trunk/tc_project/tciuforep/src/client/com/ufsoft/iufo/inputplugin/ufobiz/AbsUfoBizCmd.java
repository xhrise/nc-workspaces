package com.ufsoft.iufo.inputplugin.ufobiz;

import javax.swing.JOptionPane;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoSaveRepDataCmd;
import com.ufsoft.report.command.UfoCommand;

public abstract class AbsUfoBizCmd extends UfoCommand {
	protected RepDataEditor editor=null;
	
    protected AbsUfoBizCmd(RepDataEditor editor){
    	this.editor=editor;
    }
    
    public void execute(Object[] params) {
        if(isNeedCheckParams()){
            if(!isValidParams(params)){
                return;
            }
        }

        if(isNeedComfirmSave()){
            doComfirmSave(editor);
        }
        executeIUFOBizCmd(editor,params);
    }
    
    protected RepDataEditor getRepDataEditor(){
    	return editor;
    }
    
    protected boolean isValidParams(Object[] params) {
        return true;
    }

    protected boolean isNeedCheckParams() {
        return false;
    }
    
    protected  boolean isNeedComfirmSave(){
        return false;        
    }
    
    public static boolean stopCellEditing(RepDataEditor editor){
    	if (editor.getTable().getCellEditor()!=null)
    		return editor.getTable().getCellEditor().stopCellEditing();
    	return true;
    }
    
    public static boolean doComfirmSave(RepDataEditor editor){
    	return doComfirmSave(editor, true);
    }
    
    /**
     * 执行保存，不显示状态栏，
     * 对于切换报表适用，用于解决线程执行顺序问题。
     * @param ufoReport
     * @param withProcess
     */
    public static boolean doComfirmSave(RepDataEditor editor, boolean withProcess){
        if(editor == null){
            return false;
        }
        
        //提示保存已打开的报表
        if (editor.getTable().getCellEditor()!=null)
        	editor.getTable().getCellEditor().stopCellEditing();
        
        boolean isSucceedSave = true;
        boolean bDirty = editor.isDirty();
        if(bDirty){
            String strSaveAlert = MultiLangInput.getString("uiufotableinput0023");//"已打开的报表已修改，请确认是否保存？";
            int bConfirmReturn = JOptionPane.showConfirmDialog(
            		editor,
                    strSaveAlert,
                    MultiLangInput.getString("uiufotableinput0024"),//"是否保存当前报表",
                    JOptionPane.OK_CANCEL_OPTION);
            if(bConfirmReturn == JOptionPane.OK_OPTION){
                if(withProcess){
                	UfoSaveRepDataCmd saveRepCmd = new UfoSaveRepDataCmd(editor);
                	saveRepCmd.execute(null);
                	isSucceedSave = saveRepCmd.isSucceedSave();
                } else{
                	isSucceedSave = new UfoSaveRepDataCmd(editor).save(editor);
                }
            } 
        }
        
        return isSucceedSave;
    }
    /**
     * 执行IUFO的业务命令
     * @param ufoReport
     * @param params 
     */
    protected abstract void executeIUFOBizCmd(RepDataEditor editor, Object[] params);
}
