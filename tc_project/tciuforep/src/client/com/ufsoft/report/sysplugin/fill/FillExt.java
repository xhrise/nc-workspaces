package com.ufsoft.report.sysplugin.fill;

import java.awt.Component;

import javax.swing.KeyStroke;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;

public abstract class FillExt extends AbsActionExt{// implements IMainMenuExt,IPopupMenuExt{
	private UfoReport m_report;
	/**
	 * @param m_report//ERROR
	 */
	public FillExt(UfoReport report) {
		m_report = report;
	}

	/* @see com.ufsoft.report.plugin.ICommandExt#getName()
	 */
	abstract public String getName();
	

	/* @see com.ufsoft.report.plugin.ICommandExt#getHint()
	 */
	public String getHint() {
		return null;
	}

//	/* @see com.ufsoft.report.plugin.ICommandExt#getMenuSlot()
//	 */
//	public int getMenuSlot() {
//		return ReportMenuBar.EDIT_END;
//	}

	/* @see com.ufsoft.report.plugin.ICommandExt#getImageFile()
	 */
	abstract public String getImageFile();

	/* @see com.ufsoft.report.plugin.ICommandExt#getAccelerator()
	 */
	public KeyStroke getAccelerator() {
		return null;
	}

	/* @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new FillCmd(m_report);
	}

	/* @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		AreaPosition selArea =  m_report.getCellsModel().getSelectModel().getSelectedArea();
		return new Object[]{selArea,new Integer(getFillType())};
	}
	public String[] getPath(){
	 return new String[]{MultiLang.getString("uiuforep0000500")};    
	}
	protected abstract int getFillType();
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return true;
    }
    /*
     * @see com.ufsoft.report.plugin.IPopupMenuExt#isVisiable(java.awt.Component)
     */
    public boolean isVisiable(Component focusComp) {
        return true;
    }
}
