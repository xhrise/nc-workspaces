package com.ufida.report.spreedsheet.applet;

import com.ufida.report.spreedsheet.model.SpreadCellPropertyVO;
import com.ufida.report.spreedsheet.model.SpreadSheetUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.sysplugin.cellattr.SetCellAttrCmd;
import com.ufsoft.table.Cell;

public class SpreadCellAttrCmd extends SetCellAttrCmd {

	public SpreadCellAttrCmd(UfoReport rep) {
		super(rep);
	}

	public void execute(Object[] params) {
		super.execute(params);
		if (params != null && params.length >= 9) {
			Object exProperty = params[8];
			if (exProperty != null) {
				SpreadCellPropertyVO cellVO = (SpreadCellPropertyVO)exProperty;
				Cell cell = SpreadSheetUtil.getSelectedCell(getReport().getCellsModel());
				if(cell != null){
					cell.setExtFmt(SpreadCellPropertyVO.KEY_CELL_SPREAD_PROP, cellVO);
				}
			}
		}
	}
}
