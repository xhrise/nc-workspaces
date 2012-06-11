package com.ufsoft.table.undo;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

public class RedoExt extends AbsUndoExt {

	public RedoExt(AbstractPlugIn p) {
		super(p);
	}
	
	public boolean isEnabled(Component focusComp) {
		if (getPlugIn().getReport()==null || getPlugIn().getReport().getCellsModel()==null 
				|| getPlugIn().getReport().getCellsModel().getUndoManager()==null)
			return false;
		
		return  getPlugIn().getReport().getCellsModel().getUndoManager().canRedo();
	}

	@Override
	public void excuteImpl(UfoReport container) {
		if(getPlugIn().getReport().getCellsModel().getUndoManager().canRedo())
			getPlugIn().getReport().getCellsModel().getUndoManager().redo();
	}
	
	/**
	 * @i18n miufo00200047=»Ö¸´
	 * @i18n miufo00200048=³·Ïú»Ö¸´
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("miufo00200047"));
		uiDes.setToolBar(true);
		uiDes.setGroup(MultiLang.getString("miufo00200048"));
		uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK));
		uiDes.setImageFile("reportcore/redo.gif");
		return new ActionUIDes[] { uiDes };
	}
	
	
}
 