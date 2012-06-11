
package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JTextField;

import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.DefaultSheetCellEditor;

final class FormulaDefEditor extends DefaultSheetCellEditor {
	FormulaDefEditor(JTextField field) {
		super(field);

	}

	public Component getTableCellEditorComponent(CellsPane cellsPane, Object value,
			boolean isSelected, int row, int column) {

		FormulaModel formulaModel = FormulaModel.getInstance(cellsPane
				.getDataModel());
		CellPosition cellPos = CellPosition.getInstance(row, column);
		Object[] objs = formulaModel.getRelatedFmlVO(cellPos, true);
		FormulaVO fmlVO = (FormulaVO) objs[1];
		if (fmlVO == null) {
			objs = formulaModel.getRelatedFmlVO(cellPos, false);
			fmlVO = (FormulaVO) objs[1];
		}
		if (fmlVO != null) {
			new FormulaActionHandler(cellsPane).execute(null);
		}
		return null;
	}

	public int getEditorPRI() {
		return 3;
	}

	public boolean isEnabled(CellsModel cellsModel, CellPosition cellPos) {
		FormulaModel formulaModel = CellsModelOperator
				.getFormulaModel(cellsModel);
		return formulaModel.getRelatedFmlVO(cellPos, true)[0] != null
				|| formulaModel.getRelatedFmlVO(cellPos, false)[0] != null;
	}

	public boolean isCellEditable(EventObject anEvent) {
		if (super.isCellEditable(anEvent) && anEvent != null
				&& anEvent instanceof MouseEvent) {
			return true;
		}
		return false;
	}
}