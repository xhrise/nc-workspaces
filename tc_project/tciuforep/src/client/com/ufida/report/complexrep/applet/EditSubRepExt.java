package com.ufida.report.complexrep.applet;


import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;

import com.ufida.report.rep.applet.BIReportApplet;
import com.ufida.report.rep.model.BIContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UIUtilities;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.ActionUIDes;

public class EditSubRepExt extends ComplexActionExt {



    public EditSubRepExt(ComplexRepPlugin pi) {
        super(pi);
    }

    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        String strName=StringResource.getStringResource(StringResConst.STR_EDIT_REPORT);
        uiDes.setName(strName);
        uiDes.setPopup(true);
        
        ActionUIDes uiDesTool = new ActionUIDes();
        uiDesTool.setTooltip(strName);
        uiDesTool.setToolBar(true);
        uiDesTool.setImageFile("reportcore/ufheart.gif");
        
        ActionUIDes uiDesMenu = new ActionUIDes();
        uiDesMenu.setName(strName);
        uiDesMenu.setPaths(new String[]{StringResource.getStringResource(StringResConst.STR_MENU_MAIN)});
        
        return new ActionUIDes[]{uiDes,uiDesTool,uiDesMenu};
    }

    public UfoCommand getCommand() {
        return null;
    }

    public Object[] getParams(UfoReport container) {
        final UfoReport curSubRep = getPlugIn().getFocusSubReport();
        class SubRepDialog extends UfoDialog{
            public SubRepDialog(UfoReport container) {
                super(container.getFrame());
                UfoReport report = new UfoReport(getPlugIn().getReport().getOperationState(),
                        curSubRep.getContextVo());
                int reportType = ((BIContextVO)curSubRep.getContextVo()).getBaseReportModel().getReportType().intValue();
                BIReportApplet.initPlugins(report,reportType);
                
                UIUtilities.ufoReport2JRootPane(report, getRootPane());
                
//                gsetRootPane(report);
            }            
        }
        JDialog dialog = new SubRepDialog(container);
        dialog.setModal(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation(0,0);
        dialog.setSize(dim);
        dialog.setResizable(true);
        dialog.setVisible(true);
        getPlugIn().getReport().getReportNavPanel().revalidate();
        getPlugIn().getReport().getReportNavPanel().repaint();
        return null;
    }

}
