package com.ufsoft.iufo.inputplugin.dynarea;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsAuthorization;

/**
 * 所有添加删除行扩展的父类。
 * @author zzl 2005-6-30
 */
public abstract class AbsDynAreaActionExt extends AbsActionExt {

    private DynAreaInputPlugin m_plugin;
    
    protected abstract String getMenuName();
    
    /**
     * @param plugin
     */
    public AbsDynAreaActionExt(DynAreaInputPlugin plugin) {
        m_plugin = plugin;
    }
    /*
     * @see com.ufsoft.report.plugin.IActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(getMenuName());
        uiDes.setPopup(true);
        return new ActionUIDes[]{uiDes};
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
     */
    public abstract UfoCommand getCommand();

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
     */
    public abstract Object[] getParams(UfoReport container);
    /*
     * @see com.ufsoft.report.plugin.IActionExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
    	if ( m_plugin.getReport().getCellsModel()==null)
    		return false;
    	
        CellPosition anchorPos = m_plugin.getReport().getCellsModel().getSelectModel().getAnchorCell();
        CellsAuthorization auth = m_plugin.getReport().getCellsModel().getCellsAuth();
        if(auth == null){
        	return false;
        }
        if(auth.isWritable(anchorPos.getRow(), anchorPos.getColumn())){
        	return m_plugin.isInDynArea(anchorPos.getRow(),anchorPos.getColumn());
        }else{
        	return false;
        }
    }
    protected DynAreaInputPlugin getPlugin(){
        return m_plugin;
    }
    protected int getInputCount(){
        while(true){
            try{
                String count = JOptionPane.showInputDialog(getPlugin().getReport(),MultiLangInput.getString("input_count"));//"请输入数量：");
                if(count == null){//cancel
                    return 0;
                }
                int num = Integer.parseInt(count);
                if(num < 0){
                    throw new RuntimeException();
                }
                return num;
            }catch(Exception e){       
                JOptionPane.showConfirmDialog(getPlugin().getReport(),MultiLangInput.getString("input_valid_positive_number"),UIManager.getString("OptionPane.titleText"),JOptionPane.YES_NO_OPTION);
            }
        }
    }
}
