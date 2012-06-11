package com.ufsoft.iufo.inputplugin.dynarea;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.table.CellsModel;

/**
 * "删除一组"和"删除多组"的父类。
 * @author zzl 2005-6-30
 */
public abstract class AbsDynAreaDelRowExt extends AbsDynAreaActionExt {
    
    protected abstract int getDelCount();
    
    /**
     * @param plugin
     */
    public AbsDynAreaDelRowExt(DynAreaInputPlugin plugin) {
        super(plugin);
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
     */
    public UfoCommand getCommand() {
        return new DynAreaDelRowCmd();
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
     */
    public Object[] getParams(UfoReport container) {
        CellsModel cellsModel = getPlugin().getReport().getCellsModel();
        DynAreaCell dynAreaCell = getPlugin().getAnchorDynAreaCell();
        int delCount = getDelCount();
        return new Object[]{cellsModel,dynAreaCell,new Integer(delCount)};
    }
}
