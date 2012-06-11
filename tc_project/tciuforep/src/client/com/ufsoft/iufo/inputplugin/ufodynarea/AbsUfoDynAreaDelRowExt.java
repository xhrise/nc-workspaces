package com.ufsoft.iufo.inputplugin.ufodynarea;

import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaDelRowCmd;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.table.CellsModel;

/**
 * "删除一组"和"删除多组"的父类。
 * @author zzl 2005-6-30
 */
public abstract class AbsUfoDynAreaDelRowExt extends AbsUfoDynAreaActionExt {
    protected abstract int getDelCount();
    
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
     */
    public UfoCommand getCommand() {
        return new DynAreaDelRowCmd();
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
     */
    public Object[] getParams() {
    	ReportDesigner editor=getReportDesigner();
        CellsModel cellsModel = editor.getCellsModel();
        DynAreaCell dynAreaCell =getAnchorDynAreaCell(editor);
        int delCount = getDelCount();
        return new Object[]{cellsModel,dynAreaCell,new Integer(delCount)};
    }
}
