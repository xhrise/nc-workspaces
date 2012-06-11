package com.ufsoft.table.exarea;

import java.awt.Component;

import com.ufsoft.report.StateUtil;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.report.util.MultiLang;

public class ExAreaSeperateByRowExt extends ExAreaSeperateByColExt {
	
	public ExAreaSeperateByRowExt(ExAreaPlugin plugin) {
		super(plugin);
	}
	
@Override
	public boolean isEnabled(Component focusComp) {
		CellPosition anchorCell = getPlugIn().getCellsModel().getSelectModel().getAnchorCell(); 

		if(anchorCell == null || !anchorCell.equals(getPlugIn().getCellsModel().getSelectModel().getSelectedArea())){
			return false;
		}
		
		ExAreaCell exCell = getPlugIn().getExAreaModel().getExArea(anchorCell);
		if(exCell == null){
			return false;
		}
		AreaPosition area = exCell.getArea();
		if(area.getStart().getRow() == anchorCell.getRow()){ 
//				|| area.getEnd().getRow() == anchorCell.getRow()){
			return false;
		}
		
		if(!StateUtil.isFormatState(getPlugIn().getReport(), focusComp))
			return false;
		return true;
	}
 
	/**
	 * @i18n miufo00087=按行分割可扩展区域
	 */
	@Override
	public String getDesName() {
		return MultiLang.getString("miufo00087");
	}

	@Override
	protected AreaPosition[] seperateArea(AreaPosition area, CellPosition anchorCell){
		
		CellPosition start = area.getStart();
		CellPosition end = CellPosition.getInstance(anchorCell.getRow() - 1, area.getEnd().getColumn());
		
		AreaPosition newArea1 = AreaPosition.getInstance(start, end);
		
		start = CellPosition.getInstance(anchorCell.getRow(), area.getStart().getColumn());
		
		AreaPosition newArea2 = AreaPosition.getInstance(start, area.getEnd());
		
		return new AreaPosition[]{newArea1, newArea2};
		
	}
} 