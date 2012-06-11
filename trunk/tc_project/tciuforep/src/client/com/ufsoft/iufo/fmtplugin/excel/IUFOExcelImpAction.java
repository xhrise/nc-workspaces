package com.ufsoft.iufo.fmtplugin.excel;

import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.report.sysplugin.excel.ExcelImpAction;
import com.ufsoft.table.CellsModel;

public class IUFOExcelImpAction extends ExcelImpAction {

	@Override
	protected void dealAfterImpExcel(CellsModel cellsModel) {
		FormulaModel formulaModel = FormulaModel.getInstance(cellsModel);
		formulaModel.setUfoFmlExecutor(UfoFmlExecutor.getInstance(getContext(),
				cellsModel));
		CellsModelOperator.initModelProperties(getContext(), cellsModel);

	}

}
