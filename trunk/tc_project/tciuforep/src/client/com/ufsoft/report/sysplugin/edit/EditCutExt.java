 package com.ufsoft.report.sysplugin.edit;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;

/**
 * <pre>
 * </pre>
 * @author zzl
 * @version 5.0
 * Create on 2004-10-21
 * @deprecated by 2008-4-28 王宇光 复制，剪切，等编辑功能统一接口：EditExt
 */
public abstract class EditCutExt  extends AbsActionExt{
	protected UfoReport _report;

	public EditCutExt(UfoReport report) {
		super();
		_report  = report;
	}

	/* @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand(){
		return new UfoCommand(){
			public void execute(Object[] params) {
				UfoReport rep = (UfoReport)params[0];
				// add by 王宇光 2008-4-28 此种调用剪切的方法，现在已经不用，此类编辑的功能都在插件：editPlugin
//				rep.getTable().cut(getCutType());
			}			
		};    
	}

	/* @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		return new Object[]{container};
	}

	abstract protected int getCutType();
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return /*StateUtil.isCellsPane(null,focusComp) && */StateUtil.isAnchorEditable(_report.getCellsModel());
    }
}

class EditCutAllExt extends EditCutExt{

    public EditCutAllExt(UfoReport report) {
		super(report);
	}
	/*
     * @see com.ufsoft.report.sysplugin.EditCutExt#getCutType()
     */
    protected int getCutType() {
        return UFOTable.CELL_ALL;
    }
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("miufo1000362"));
        uiDes.setPaths(new String[]{MultiLang.getString("edit"),MultiLang.getString("miufo1000654")});
        return new ActionUIDes[]{uiDes};
    }
    
}
class EditCutContentExt extends EditCutExt{
	
    public EditCutContentExt(UfoReport report) {
		super(report);
	}
	/*
     * @see com.ufsoft.report.sysplugin.EditCutExt#getCutType()
     */
    protected int getCutType() {
        return UFOTable.CELL_CONTENT;
    }
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("uiuforep0001001"));
        uiDes.setPaths(new String[]{MultiLang.getString("edit"),MultiLang.getString("miufo1000654")});
        uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
        return new ActionUIDes[]{uiDes};
    }
}
class EditCutFormatExt extends EditCutExt{
	
    public EditCutFormatExt(UfoReport report) {
		super(report);
	}
	/*
     * @see com.ufsoft.report.sysplugin.EditCutExt#getCutType()
     */
    protected int getCutType() {
        return UFOTable.CELL_FORMAT;
    }	
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("uiuforep0001002"));
        uiDes.setPaths(new String[]{MultiLang.getString("edit"),MultiLang.getString("miufo1000654")});
        return new ActionUIDes[]{uiDes};
    }
}
