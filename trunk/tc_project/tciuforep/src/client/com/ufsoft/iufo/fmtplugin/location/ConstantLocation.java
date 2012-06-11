package com.ufsoft.iufo.fmtplugin.location;

import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

/**
 * <pre>
 * </pre>  定位：常量（相对于公式和指标）
 * @author 王宇光
 * @version 
 * Create on 2008-5-20
 */
public class ConstantLocation extends AbsLocation {

	public ConstantLocation(UfoReport rep) {
		super(rep);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getConditionType() {
		// TODO Auto-generated method stub
		return AbsLocation.CONSTANT;
	}

	@Override
	protected void locationImpl(CellPosition cellPosition, CellsModel cellsModel) {
		if (cellPosition == null || cellsModel == null){
			throw new IllegalArgumentException(StringResource.getStringResource("miufo1000496"));//输入参数不允许为空
		}
		Cell cell = cellsModel.getCell(cellPosition);
		if (cell == null) {
			return;
		}
		boolean isHaveMeasure = getMeasureModel(cellsModel)
				.getMainMeasureVOByPos(cellPosition) != null ? true : false;
		FormulaModel formulaModel = getFormulaModel(cellsModel);
		boolean isHaveFormula = formulaModel.getDirectFml(cellPosition, true) != null
				|| formulaModel.getDirectFml(cellPosition, false) != null ? true
				: false;

		if (cell.getValue() != null && !isHaveMeasure && !isHaveFormula) {
			getCellsPositionList().add(cellPosition);
		}
	}

}
