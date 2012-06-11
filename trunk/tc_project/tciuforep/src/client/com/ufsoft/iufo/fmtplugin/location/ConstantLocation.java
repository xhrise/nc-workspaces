package com.ufsoft.iufo.fmtplugin.location;

import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

/**
 * <pre>
 * </pre>  ��λ������������ڹ�ʽ��ָ�꣩
 * @author �����
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
			throw new IllegalArgumentException(StringResource.getStringResource("miufo1000496"));//�������������Ϊ��
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
