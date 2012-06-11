package com.ufsoft.report.sysplugin.headerlock;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.UFOTable;

/**
 * Ëø¶¨
 * @author zzl 2005-5-23
 */
public class AreaLockExt extends AbsActionExt {//,IStateChangeMenu {

    private UfoReport m_report;

    /**
     * @param m_report
     */
    public AreaLockExt(UfoReport report) {
        m_report = report;
    }

    private String getName() {
    	if(getUFOTable() == null){
    		return MultiLang.getString("area_lock");
    	}
        return getUFOTable().isFreezing() ? 
                MultiLang.getString("area_lock_cancel") : 
                MultiLang.getString("area_lock");
    }

    /**
     * 
     * @create by wangyga at 2008-12-30,ÉÏÎç09:09:02
     *
     * @return
     */
    private String getImagePath(){
    	if(getUFOTable() == null){
    		return "reportcore/freeze.png";
    	}
    	return getUFOTable().isFreezing() ? "reportcore/unfreeze.png" : "reportcore/freeze.png";
    }
    
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
     */
    public UfoCommand getCommand() {
        return new UfoCommand(){
            public void execute(Object[] params) {
            	UFOTable table = getUFOTable();
                if(table.isFreezing()){
                    if(table.isFrozenNoSplit()){
                    	table.cancelSeperate();
                    }
                    table.setFreezing(false);
                }else{
                    if(table.getSeperateRow() == 0 && table.getSeperateCol() == 0){
                        CellPosition anchor = (CellPosition) params[0];
                        table.setFrozenNoSplit(true);
                        table.setSeperatePos(anchor.getRow(),anchor.getColumn());
                    }
                    table.setFreezing(true);
                }

                m_report.resetGlobalPopMenuSupport();
                     		
            }            
        };
    }

    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#changeCompState(java.awt.Component)
     */
    public void initListenerByComp(final Component stateChangeComp) {
        getUFOTable().addPropertyChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals("seperate2lock")){
                    ((AbstractButton)stateChangeComp).setText(getName());
                    ((AbstractButton)stateChangeComp).setIcon(ResConst
							.getImageIcon(getImagePath()));
                }
            }
            
        });
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
     */
    public Object[] getParams(UfoReport container) {
        CellPosition anchor = getUFOTable().getCellsModel().getSelectModel().getAnchorCell();
        return new Object[]{anchor};
    }
    private UFOTable getUFOTable(){
        return m_report.getTable();
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return true;//StateUtil.isFormatState(m_report,focusComp);
    }

    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(getName());
        uiDes.setImageFile(getImagePath());
        uiDes.setPaths(new String[]{MultiLang.getString("window")});
        uiDes.setGroup("ViewFormat");
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setName(getName());
        uiDes1.setImageFile(getImagePath());
        uiDes1.setPopup(true);
        uiDes1.setGroup("ViewFormat");
        return new ActionUIDes[]{uiDes,uiDes1};
    }
}
