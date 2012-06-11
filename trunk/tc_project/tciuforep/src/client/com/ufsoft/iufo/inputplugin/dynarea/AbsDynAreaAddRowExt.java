package com.ufsoft.iufo.inputplugin.dynarea;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.table.CellsModel;

/**
 * ����һ��(���Ϸ�)
 * ����һ��(���·�)
 * �������(���Ϸ�)
 * �������(���·�) 
 * ������չ�ĸ��ࡣ
 * @author zzl 2005-6-30
 */
public abstract class AbsDynAreaAddRowExt extends AbsDynAreaActionExt {
    /**
     * �Ƿ����е��Ϸ���ӣ��������·���ӡ�
     * @return boolean
     */
    protected abstract boolean isAboveRow();
    protected abstract int getAddCount();
    /**
     * @param plugin
     */
    public AbsDynAreaAddRowExt(DynAreaInputPlugin plugin) {
        super(plugin);
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
     */
    public UfoCommand getCommand() {
        return new DynAreaAddRowCmd();
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
     */
    public Object[] getParams(UfoReport container) {
        CellsModel cellsModel = getPlugin().getReport().getCellsModel();
        DynAreaCell dynAreaCell = getPlugin().getAnchorDynAreaCell();
        int addCount = getAddCount();
        boolean aboveRow = isAboveRow();
        return new Object[]{cellsModel,dynAreaCell,new Integer(addCount),Boolean.valueOf(aboveRow),
        		cellsModel.getSelectModel().getAnchorCell()};
    }
}
