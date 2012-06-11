package com.ufsoft.iufo.inputplugin.ufodynarea;

import com.ufsoft.iufo.inputplugin.dynarea.DynAreaAddRowCmd;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

/**
 * ����һ��(���Ϸ�)
 * ����һ��(���·�)
 * �������(���Ϸ�)
 * �������(���·�) 
 * ������չ�ĸ��ࡣ
 * @author zzl 2005-6-30
 */
public abstract class AbsUfoDynAreaAddRowExt extends AbsUfoDynAreaActionExt {
    /**
     * �Ƿ����е��Ϸ���ӣ��������·���ӡ�
     * @return boolean
     */
    protected abstract boolean isAboveRow();
    protected abstract int getAddCount();
    
    protected boolean isAddCopy(){
    	return false;
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
    public Object[] getParams() {
    	ReportDesigner editor=getReportDesigner();
        CellsModel cellsModel = editor.getCellsModel();
        DynAreaCell dynAreaCell =getAnchorDynAreaCell(editor);
        int addCount = getAddCount();
        boolean aboveRow = isAboveRow();
        CellPosition addPos=cellsModel.getSelectModel().getAnchorCell();
        if(aboveRow){
        	addPos=cellsModel.getSelectModel().getSelectedArea().getStart();
        }else{
        	addPos=cellsModel.getSelectModel().getSelectedArea().getEnd();
        }
        return new Object[]{cellsModel,dynAreaCell,new Integer(addCount),Boolean.valueOf(aboveRow),addPos
        		,Boolean.valueOf(isAddCopy())};
    }
}
