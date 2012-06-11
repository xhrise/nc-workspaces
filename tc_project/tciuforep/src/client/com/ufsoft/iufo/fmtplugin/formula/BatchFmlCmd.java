package com.ufsoft.iufo.fmtplugin.formula;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;

public class BatchFmlCmd extends UfoCommand implements IUfoContextKey{

    public void execute(Object[] params) {
        UfoReport report = (UfoReport) params[0];
        UfoContextVO contextVO = (UfoContextVO) report.getContextVo();
//        LogWindow logWindow = ((LogPlugin)report.getPluginManager().getPlugin(LogPlugin.class.getName())).getLogWindow();
        
        boolean isAnaRep = contextVO.getAttribute(ANA_REP) == null ? false : Boolean.parseBoolean(contextVO.getAttribute(ANA_REP).toString());
        BatchFmlDlg editDlg = new BatchFmlDlg(report, report.getCellsModel(),report.getContext() ,isAnaRep,null);
        editDlg.setModal(true);
        editDlg.setVisible(true);
        FormulaDefPlugin pi = (FormulaDefPlugin) report.getPluginManager().getPlugin(FormulaDefPlugin.class.getName());
        if(editDlg.getResult()!=UfoDialog.ID_CANCEL){
            pi.setDirty(true);
        }
    }

}
