package com.ufsoft.report.sysplugin.editplugin;

import com.ufsoft.report.AbstractRepPluginAction;
import com.ufsoft.report.StateUtil;
import com.ufsoft.table.CellsModel;

public abstract class AbsEditAction extends AbstractRepPluginAction{

	@Override
	public boolean isEnabled() {
		CellsModel cellsModel = getCellsModel();
		if (cellsModel == null)
			return false;

		return StateUtil.isAnchorEditable(cellsModel);
	}
}
