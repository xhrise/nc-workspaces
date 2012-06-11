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
     * ִ�б��棬����ʾ״̬����
     * �����л��������ã����ڽ���߳�ִ��˳�����⡣
     * @param ufoReport
     * @param withProcess
     */
    public static boolean doComfirmSave(RepDataEditor editor, boolean withProcess){
        if(editor == null){
            return false;
        }
        
        //��ʾ�����Ѵ򿪵ı���
        if (editor.getTable().getCellEditor()!=null)
        	editor.getTable().getCellEditor().stopCellEditing();
        
        boolean isSucceedSave = true;
        boolean bDirty = editor.isDirty();
        if(bDirty){
            String strSaveAlert = MultiLangInput.getString("uiufotableinput0023");//"�Ѵ򿪵ı������޸ģ���ȷ���Ƿ񱣴棿";
            int bConfirmReturn = JOptionPane.showConfirmDialog(
            		editor,
                    strSaveAlert,
                    MultiLangInput.getString("uiufotableinput0024"),//"�Ƿ񱣴浱ǰ����",
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
     * ִ��IUFO��ҵ������
     * @param ufoReport
     * @param params 
     */
    protected abstract void executeIUFOBizCmd(RepDataEditor editor, Object[] params);
}
