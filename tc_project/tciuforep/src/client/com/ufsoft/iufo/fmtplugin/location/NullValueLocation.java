package com.ufsoft.iufo.fmtplugin.location;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
/**
 * <pre>
 * </pre>  定位：空值
 * @author 王宇光
 * @version 
 * Create on 2008-5-19
 */
public class NullValueLocation extends AbsLocation{

	public NullValueLocation(UfoReport rep) {
		super(rep);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getConditionType() {
		// TODO Auto-generated method stub
		return AbsLocation.NULL_VALUE;
	}

	@Override
	protected void locationImpl(CellPosition cellPosition,CellsModel cellsModel) {
		if (cellPosition == null || cellsModel == null){
			throw new IllegalArgumentException(StringResource.getStringResource("miufo1000496"));//输入参数不允许为空
		}	
		Cell cell = cellsModel.getCellIfNullNew(cellPosition.getRow(),cellPosition.getColumn());
		if (cell == null) {
			return;
		}
		if(cell.getValue() == null){
			getCellsPositionList().add(cellPosition);
		}
		
	}

}
