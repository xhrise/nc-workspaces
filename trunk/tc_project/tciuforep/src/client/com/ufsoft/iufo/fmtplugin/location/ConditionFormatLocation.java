package com.ufsoft.iufo.fmtplugin.location;

import java.util.ArrayList;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.format.ConditionFormat;
/**
 * <pre>
 * </pre>  ��λ��������ʽ
 * @author �����
 * @version 
 * Create on 2008-5-19
 */
public class ConditionFormatLocation extends AbsLocation{
    public ConditionFormatLocation(UfoReport rep) {
		super(rep);
	}

	@Override
	protected int getConditionType() {
		return AbsLocation.CONDITION_FORMAT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void locationImpl(CellPosition cellPosition,CellsModel cellsModel) {
		if (cellPosition == null || cellsModel == null){
			throw new IllegalArgumentException(StringResource.getStringResource("miufo1000496"));//�������������Ϊ��
		}
		Cell cell = cellsModel.getCell(cellPosition);
		if (cell == null) {
			return;
		}
		ArrayList<ConditionFormat> conditionFormat = (ArrayList<ConditionFormat>) cell
				.getExtFmt(ConditionFormat.EXT_FMT_CONDITIONFMT);
		if (conditionFormat != null && conditionFormat.size() > 0) {
			getCellsPositionList().add(cellPosition);
		}
		
	}

}
