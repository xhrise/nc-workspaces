package com.ufsoft.iufo.fmtplugin.location;

import com.ufsoft.report.UfoReport;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
/**
 * <pre>
 * </pre>  ��λ�����һ����Ԫ��cell�ǿյ�����к�����ж�Ӧ�ĵ�Ԫ��
 * @author �����
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
