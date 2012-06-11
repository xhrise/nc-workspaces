package com.ufsoft.report.fmtplugin.formula;

import com.ufsoft.script.AreaFmlExecutor;
import com.ufsoft.script.base.AreaFormulaModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.IArea;

/**
 * 
 * @author zhaopq
 * @created at 2009-4-19,����04:46:07
 * @since v56
 */
public class AreaFormulaActionHandler {

	private CellsPane cellsPane;

	AreaFormulaActionHandler(CellsPane cellsPane) {
		this.cellsPane = cellsPane;
	}

	public void execute(Object[] params) {
		AreaFormulaModel areaFormulaModel = AreaFormulaModel
				.getInstance(cellsPane.getDataModel());
		if (areaFormulaModel.getAreaFmlExecutor() == null) {

			// ��ʼ����ʽִ����
			AreaFormulaModel formulaModel = AreaFormulaModel
					.getInstance(cellsPane.getDataModel());
			formulaModel.setAreaFmlExecutor(new AreaFmlExecutor(cellsPane
					.getDataModel()));
		}
		// ���ѡ����Ԫ����ĵ�Ԫ��ʽ��ʽ����
		IArea anchorCell = cellsPane.getDataModel().getSelectModel()
				.getAnchorCell();
		if (anchorCell == null)
			return;
		AreaFmlEditDlg fmlEditDlg = new AreaFmlEditDlg(cellsPane,
				areaFormulaModel.getAreaFmlExecutor());

		fmlEditDlg.setArea(new IArea[] { anchorCell });
		fmlEditDlg.loadFormula(anchorCell);
		fmlEditDlg.show();
	}

}
