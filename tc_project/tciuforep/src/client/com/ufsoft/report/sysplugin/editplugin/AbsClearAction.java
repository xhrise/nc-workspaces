package com.ufsoft.report.sysplugin.editplugin;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.undo.CellUndo;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;

public abstract class AbsClearAction extends AbsEditAction {

	protected abstract int getClearType();
	
	protected abstract String getName();

	@Override
	public void execute(ActionEvent e) {
		CellsModel cellsModel = getCellsModel();
		AreaPosition[] areas = cellsModel.getSelectModel().getSelectedAreas();
		CellUndo edit = new CellUndo();
		for (AreaPosition area : areas) {
			ArrayList<CellPosition> list = cellsModel.getSeperateCellPos(area);
			for (CellPosition pos : list) {
				Cell cell = cellsModel.getCell(pos);
				if (cell != null) {
					cell = (Cell) cell.clone();
				}
				edit.addCell(pos, cell, null);
			}
		}

		doClear(getClearType());
		
		if (areas.length > 0) {
			cellsModel.fireUndoHappened(edit);
		}
	}

	public void doClear(int clipType) {
		clear(clipType, clipType == UFOTable.CELL_ALL
				|| clipType == UFOTable.CELL_CONTENT);
	}

	private void clear(int clipType, boolean dispatch) {
		CellsModel model = getCellsModel();
		AreaPosition[] areas = model.getSelectModel().getSelectedAreas();
		if (areas == null)
			return;
		// 包装拷贝事件.
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.CLEAR, null,
				areas);

		UFOTable table = getTable();
		if (table == null)
			return;

		if (dispatch && table.checkEvent(event)) {
			table.fireEvent(event);
		}
		// 删除数据和格式

		model.clearArea(clipType, areas);

	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor desc = new PluginActionDescriptor(getName());
		desc.setExtensionPoints(XPOINT.MENU);
		desc.setGroupPaths(new String[]{MultiLang.getString("edit"),MultiLang.getString("miufo1000103"),MultiLang.getString("edit")});
		return desc;
	}

}
