package com.ufsoft.report.sysplugin.pagination;

import java.awt.Component;

import javax.swing.KeyStroke;

import com.ufsoft.report.ReportMenuBar;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.header.Header;

/**
 * 强制分页扩展
 * @author zzl
 */
public class InsertPaginationExt extends AbsActionExt{// implements IMainMenuExt {

    /* （非 Javadoc）
     * @see com.ufsoft.report.plugin.ICommandExt#getName()
     */
    public String getName() {
        return MultiLang.getString("miufo1001541");
    }

    /* （非 Javadoc）
     * @see com.ufsoft.report.plugin.ICommandExt#getHint()
     */
    public String getHint() {
        return null;
    }

//    /* （非 Javadoc）
//     * @see com.ufsoft.report.plugin.ICommandExt#getMenuSlot()
//     */
//    public int getMenuSlot() {
//        return ReportMenuBar.EDIT_END;
//    }

    /* （非 Javadoc）
     * @see com.ufsoft.report.plugin.ICommandExt#getImageFile()
     */
    public String getImageFile() {
        return null;
    }

    /* （非 Javadoc）
     * @see com.ufsoft.report.plugin.ICommandExt#getAccelerator()
     */
    public KeyStroke getAccelerator() {
        return null;
    }

   
    /* （非 Javadoc）
     * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
     */
    public UfoCommand getCommand() {
        return new UfoCommand(){
            public void execute(Object[] params) {
                UfoReport report = (UfoReport) params[0];
                CellsModel cm = report.getCellsModel();
                CellPosition anchorPos = cm.getSelectModel().getAnchorCell();
                if(anchorPos.getRow() != 0){
                    cm.addPage(Header.ROW,anchorPos.getRow());
                }
                if(anchorPos.getColumn() != 0){
                    cm.addPage(Header.COLUMN,anchorPos.getColumn());
                }
            }            
        };
    }

    /* （非 Javadoc）
     * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
     */
    public Object[] getParams(UfoReport container) {
        return new Object[]{container};
    }

    /*
     * @see com.ufsoft.report.plugin.IMainMenuExt#getPath()
     */
    public String[] getPath() {
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return true;
    }
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setGroup(MultiLang.getString("printToolBar"));
        uiDes.setName(MultiLang.getString("miufo1001541"));
        uiDes.setPaths(new String[]{MultiLang.getString("format")});
        uiDes.setGroup("paginationExt");
        return new ActionUIDes[]{uiDes};
    }
}
