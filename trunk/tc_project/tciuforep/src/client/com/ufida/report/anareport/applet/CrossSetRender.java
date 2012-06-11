package com.ufida.report.anareport.applet;

import java.awt.Component;

import javax.swing.JComponent;

import com.ufida.report.anareport.model.AreaDataModel;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaComponent;
import com.ufsoft.table.re.SheetCellRenderer;

public class CrossSetRender implements SheetCellRenderer{

	public Component getCellRendererComponent(CellsPane cellsPane,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column, Cell cell) {


		if (value == null) {
			return null;
		}
		final ExAreaCell exCell = (ExAreaCell) value;
		JComponent comp = null;
		if (exCell.getModel() instanceof AreaDataModel) {
			AreaDataModel areaModel = (AreaDataModel) exCell.getModel();
			if (areaModel.getCrossSet() != null && areaModel.getCrossSet().getCrossArea() != null
					&& areaModel.getCrossSet().getCrossPoint() != null) {
				comp = new ExAreaComponent(exCell, cellsPane, areaModel.getCrossSet().getCrossArea(), areaModel.getCrossSet()
						.getCrossPoint());
			}
		}
		if (comp == null) {
			comp = new ExAreaComponent(exCell, cellsPane);
		}

		return comp;

	
	}

}
