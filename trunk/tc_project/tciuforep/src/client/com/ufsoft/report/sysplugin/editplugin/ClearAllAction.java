package com.ufsoft.report.sysplugin.editplugin;

import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsModel;

public class ClearAllAction extends AbsClearAction{

	@Override
	protected int getClearType() {
		return CellsModel.CELL_ALL;
	}

	@Override
	protected String getName() {
		return MultiLang.getString("miufo1000362");
	}

}
