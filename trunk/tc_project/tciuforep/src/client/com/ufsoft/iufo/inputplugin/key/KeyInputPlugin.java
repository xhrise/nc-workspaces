package com.ufsoft.iufo.inputplugin.key;

import java.awt.Component;
import java.util.EventObject;

import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.CellRenderAndEditor;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * ¹Ø¼ü×ÖÂ¼Èë²å¼þ
 * 
 * @author zzl 2005-6-20
 */
public class KeyInputPlugin extends AbstractPlugIn {

	static {
		CellRenderAndEditor.registEditor( KeyFmt.EXT_FMT_KEYINPUT,
				new KeyInputEditor());
	}

	/*
	 * @see com.ufsoft.report.plugin.AbstractPlugIn#createDescriptor()
	 */
	protected IPluginDescriptor createDescriptor() {
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#startup()
	 */
	public void startup() {
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
	 */
	public void shutdown() {
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
	 */
	public String[] getSupportData() {
		return new String[] { KeyFmt.EXT_FMT_KEYINPUT };
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
	 */
	public SheetCellRenderer getDataRender(String extFmtName) {
		return new SheetCellRenderer() {
			public Component getCellRendererComponent(CellsPane cellsPane,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column, Cell cell) {
				return null;
			}

		};
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
	 */
	public SheetCellEditor getDataEditor(String extFmtName) {
		return new KeyInputEditor();
	}

	/*
	 * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
	 */
	public void actionPerform(UserUIEvent e) {
	}

	/*
	 * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
	 */
	public String isSupport(int source, EventObject e)
			throws ForbidedOprException {
		return null;
	}

}
