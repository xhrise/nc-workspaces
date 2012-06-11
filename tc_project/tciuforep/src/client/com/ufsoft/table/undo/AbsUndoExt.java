package com.ufsoft.table.undo;

import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;

public abstract class AbsUndoExt extends AbsActionExt {
	private AbstractPlugIn m_plugin = null;
 
	public AbsUndoExt(AbstractPlugIn p) {
		m_plugin = p;
	}

	@Override
	public UfoCommand getCommand() {
		return null;
	}

	@Override
	public Object[] getParams(UfoReport container) {
		try {
			excuteImpl(container);
		} catch (CannotUndoException e) {

		} catch (CannotRedoException e2) {
		}
		
		m_plugin.getReport().refershPluginState(UndoPlugin.class.getName());
		
		return null;
	}

	protected AbstractPlugIn getPlugIn() {
		return m_plugin;
	}

	public void initListenerByComp(final Component stateChangeComp) {
		
		if (!(stateChangeComp instanceof AbstractButton)) {
			return;
		}
		final AbstractButton btn = ((AbstractButton)stateChangeComp);
		
//		btn.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e) {
//				btn.setEnabled(AbsUndoExt.this.isEnabled(stateChangeComp));
//			}
//			
//		});
//		
//		final CellsModel cellsModel = m_plugin.getReport().getCellsModel();
//		cellsModel.getSelectModel().addSelectModelListener(
//				new SelectListener() {
//					public void selectedChanged(SelectEvent e) {
//						if (e.getProperty() == SelectEvent.ANCHOR_CHANGED) {
//							btn.setEnabled(AbsUndoExt.this.isEnabled(stateChangeComp));
//						}
//
//					}
//				});
	}

	public abstract void excuteImpl(UfoReport container);

}
