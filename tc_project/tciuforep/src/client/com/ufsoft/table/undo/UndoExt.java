package com.ufsoft.table.undo;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

public class UndoExt extends AbsUndoExt {
	public UndoExt(AbstractPlugIn p) {
		super(p);
	}
	
	public boolean isEnabled(Component focusComp) {
		if (getPlugIn().getReport()==null || getPlugIn().getReport().getCellsModel()==null 
				|| getPlugIn().getReport().getCellsModel().getUndoManager()==null)
			return false;
		return getPlugIn().getReport().getCellsModel().getUndoManager().canUndo();
	}

	@Override
	public void excuteImpl(UfoReport container) {
		if(getPlugIn().getReport().getCellsModel().getUndoManager().canUndo())
			getPlugIn().getReport().getCellsModel().getUndoManager().undo();
	}

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("miufo00200046"));
		uiDes.setToolBar(true);
		uiDes.setGroup(MultiLang.getString("miufo00200048"));
		uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK));
		uiDes.setImageFile("reportcore/undo.gif");
		return new ActionUIDes[] { uiDes };
	}
	 
}
