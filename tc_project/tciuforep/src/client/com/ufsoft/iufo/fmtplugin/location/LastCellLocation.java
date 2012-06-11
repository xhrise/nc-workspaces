package com.ufsoft.iufo.fmtplugin.location;

import com.ufsoft.report.UfoReport;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
/**
 * <pre>
 * </pre>  定位：最后一个单元格（cell非空的最大行和最大列对应的单元）
 * @author 王宇光
 * @version 
 * Create on 2008-5-19
 */
public class LastCellLocation extends AbsLocation{

	public LastCellLocation(UfoReport rep) {
		super(rep);
	}

	protected void location() {
		CellPosition cellPosition = CellPosition.getInstance(getMaxRowCount()-1, getMaxColumnCount()-1);
		locationImpl(cellPosition,getCellsModel());
	}
	
	@Override
	protected int getConditionType() {
		return AbsLocation.LAST_CELL;
	}

	@Override
	protected void locationImpl(CellPosition cellPosition,CellsModel cellsModel) {
		if (cellPosition == null || cellsModel == null){
			return;
		}			
		cellsModel.getSelectModel().setAnchorCell(cellPosition);
	}
	
	

}
