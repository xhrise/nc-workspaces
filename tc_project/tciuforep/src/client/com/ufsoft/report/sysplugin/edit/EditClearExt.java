package com.ufsoft.report.sysplugin.edit;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.KeyStroke;

import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.undo.CellUndo;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;

public abstract class EditClearExt extends AbsActionExt {// implements IMainMenuExt{

	private UfoReport _report;

	public EditClearExt(UfoReport report) {
		super();
		_report = report;
	}

	/* @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				AreaPosition[] areas = _report.getCellsModel().getSelectModel()
						.getSelectedAreas();
				CellUndo edit = new CellUndo();
				for (AreaPosition area : areas) {
					ArrayList<CellPosition> list = _report.getCellsModel()
							.getSeperateCellPos(area);
					for (CellPosition pos : list) {
						Cell cell = _report.getCellsModel().getCell(pos);
						if (cell != null) {
							cell = (Cell) cell.clone();
						}
						edit.addCell(pos, cell, null);
					}
				}

				clear(getClearType());

				if (areas.length > 0) {
					_report.getCellsModel().fireUndoHappened(edit);
				}

			}

		};
	}

	/**
	 * 清除指定区域的单元中的数据
	 * 
	 * @param clipType，参考UFOTable的CELL_ALL、CELL_CONTENT、CELL_FORMAT。
	 */
	public void clear(int clipType) {
		clear(clipType, clipType == UFOTable.CELL_ALL
				|| clipType == UFOTable.CELL_CONTENT);
	}

	/**
	 * 清除指定区域的单元中的数据
	 * @param clipType
	 * @param dispatch
	 */
	private void clear(int clipType, boolean dispatch) {
		CellsModel model = getCellsModel();
		AreaPosition[] areas = model.getSelectModel().getSelectedAreas();
		if (areas == null)
			return;
		//包装拷贝事件.
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.CLEAR, null,
				areas);

		UFOTable table = getReport().getTable();

		if (dispatch && table.checkEvent(event)) {
			table.fireEvent(event);
		}
		//删除数据和格式

		model.clearArea(clipType, areas);

	}

	/* @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		return new Object[] { container };
	}

	/*
	 * @see com.ufsoft.report.plugin.IMainMenuExt#getPath()
	 */
	public String[] getPath() {
		return new String[] { MultiLang.getString("miufo1000103") };
	}

	protected abstract int getClearType();

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
	 */
	public boolean isEnabled(Component focusComp) {
		return /*StateUtil.isCPane1THeader(null,focusComp) && */StateUtil
				.isAnchorEditable(_report.getCellsModel());
	}

	protected UfoReport getReport() {
		return _report;
	}

	protected CellsModel getCellsModel() {
		return _report.getCellsModel();
	}
}

class EditClearAllExt extends EditClearExt {

	public EditClearAllExt(UfoReport report) {
		super(report);
	}

	/* @see com.ufsoft.report.plugin.ICommandExt#getName()
	 */
	public String getName() {
		return MultiLang.getString("miufo1000362");//"全部";
	}

	protected int getClearType() {
		return CellsModel.CELL_ALL;
	}

	/*
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("miufo1000362"));
		uiDes.setPaths(new String[] { MultiLang.getString("edit"),
				MultiLang.getString("miufo1000103") });
		uiDes.setGroup(MultiLang.getString("edit"));
		return new ActionUIDes[] { uiDes };
	}

}

class EditClearContentExt extends EditClearExt {

	public EditClearContentExt(UfoReport report) {
		super(report);
	}

	/* @see com.ufsoft.report.plugin.ICommandExt#getName()
	 */
	public String getName() {
		return MultiLang.getString("uiuforep0001001");//"仅内容";
	}

	/*
	 * @see com.ufsoft.report.sysplugin.EditClearExt#getClearType()
	 */
	protected int getClearType() {
		return CellsModel.CELL_CONTENT;
	}

	/*
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("uiuforep0001001"));
		uiDes.setPaths(new String[] { MultiLang.getString("edit"),
				MultiLang.getString("miufo1000103") });
		uiDes.setGroup(MultiLang.getString("edit"));
		uiDes.setImageFile("reportcore/clear.gif");
		uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));

		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setName(MultiLang.getString("miufo1000103"));
		uiDes1.setImageFile("reportcore/clear.gif");
		uiDes1.setToolBar(true);
		uiDes1.setGroup(MultiLang.getString("edit"));
		uiDes1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));

		return new ActionUIDes[] { uiDes, uiDes1 };
	}
}

class EditClearFormatExt extends EditClearExt {

	public EditClearFormatExt(UfoReport report) {
		super(report);
	}

	/* @see com.ufsoft.report.plugin.ICommandExt#getName()
	 */
	public String getName() {
		return MultiLang.getString("uiuforep0001002");//"仅格式";
	}

	/*
	 * @see com.ufsoft.report.sysplugin.EditClearExt#getClearType()
	 */
	protected int getClearType() {
		return CellsModel.CELL_FORMAT;
	}

	/*
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("uiuforep0001002"));
		uiDes.setGroup(MultiLang.getString("edit"));
		uiDes.setPaths(new String[] { MultiLang.getString("edit"),
				MultiLang.getString("miufo1000103") });
		return new ActionUIDes[] { uiDes };
	}
}
