package com.ufsoft.report.sysplugin.editplugin;

import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsModel;

public class ClearFormatAction extends AbsClearAction{

	@Override
	protected int getClearType() {
		return CellsModel.CELL_FORMAT;
	}

	@Override
	protected String getName() {
		return MultiLang.getString("uiuforep0001002");
	}

}
