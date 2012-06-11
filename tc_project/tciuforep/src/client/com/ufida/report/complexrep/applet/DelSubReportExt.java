package com.ufida.report.complexrep.applet;



import javax.swing.JOptionPane;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.ActionUIDes;

public class DelSubReportExt extends ComplexActionExt {

   

    public DelSubReportExt(ComplexRepPlugin plugin) {
        super(plugin);
    }

    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(StringResource.getStringResource(StringResConst.STR_DEL_REPORT));
        uiDes.setPopup(true);
        
        ActionUIDes uiDesTool = new ActionUIDes();
        uiDesTool.setTooltip(StringResource.getStringResource(StringResConst.STR_DEL_REPORT));
        uiDesTool.setToolBar(true);
        uiDesTool.setImageFile("reportcore/delete.gif");
        
        ActionUIDes uiDesMenu = new ActionUIDes();
        uiDesMenu.setName(StringResource.getStringResource(StringResConst.STR_DEL_REPORT));
        uiDesMenu.setPaths(new String[]{StringResource.getStringResource(StringResConst.STR_MENU_MAIN)});
        
        return new ActionUIDes[]{uiDes,uiDesTool,uiDesMenu};
    }

    public UfoCommand getCommand() {
        return new UfoCommand(){
            public void execute(Object[] params) {
                if(params != null && params.length > 0 && params[0].equals(Boolean.FALSE)){
                    return;
                }
                getPlugIn().delSubReport();
            }            
        };
    }

    public Object[] getParams(UfoReport container) {
        int isOK = JOptionPane.showConfirmDialog(container.getFrame(),StringResource.getStringResource(StringResConst.STR_DEL_MSG));
        if(isOK != JOptionPane.OK_OPTION){
            return new Object[]{Boolean.FALSE};
        }
        return null;
    }
    /*
     * @see com.ufsoft.report.plugin.IActionExt#isEnabled(java.awt.Component)
     */
    

}
