package com.ufida.report.complexrep.applet;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventListener;

import javax.swing.JMenuItem;

import nc.vo.bi.report.manager.ReportResource;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

import com.ufsoft.report.plugin.ActionUIDes;

public class OperationStateExt extends ComplexActionExt {

    public OperationStateExt(ComplexRepPlugin pi) {
        super(pi);
    }

    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(getMenuName());
        uiDes.setPaths(new String[]{StringResource.getStringResource(StringResConst.STR_MENU_MAIN)});
        return new ActionUIDes[]{uiDes};
    }

    public UfoCommand getCommand() {
        return new UfoCommand(){
            public void execute(Object[] params) {
                if(getPlugIn().getOperationState() == UfoReport.OPERATION_FORMAT){
                	getPlugIn().setOperationState(UfoReport.OPERATION_INPUT);    
                }else{
                	getPlugIn().setOperationState(UfoReport.OPERATION_FORMAT);   
                }                           
            }            
        };
    }

    public Object[] getParams(UfoReport container) {
        return null;
    }
    /*
     * @see com.ufsoft.report.plugin.IActionExt#getListeners(java.awt.Component)
     */
    public EventListener getListener(final Component stateChangeComp) {
        PropertyChangeListener lis = new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                ((JMenuItem)stateChangeComp).setText(getMenuName());
            }
            
        };
        getPlugIn().getModel().addPropertyChangeListener(ReportResource.OPERATE_TYPE,lis);
        return null;        
    }
    private String getMenuName(){
        boolean isFormatState = getPlugIn().getOperationState() == UfoReport.OPERATION_FORMAT;
        String strId= isFormatState?StringResConst.MENU_DATA:StringResConst.MENU_FORMAT;
        return StringResource.getStringResource(strId);
    }
    public boolean isEnabled(Component focusComp) {
    	return true;
    }
}
