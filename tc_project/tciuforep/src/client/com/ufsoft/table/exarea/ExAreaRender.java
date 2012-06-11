package com.ufsoft.table.exarea;

import java.awt.Component;

import javax.swing.JComponent;

import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;

public class ExAreaRender implements SheetCellRenderer{

	public ExAreaRender(String extFmtName){		
	}
	
	public Component getCellRendererComponent(CellsPane cellsPane,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column, Cell cell) {
		if (value == null) {
			return null;
		}
		final ExAreaCell exCell = (ExAreaCell) value;
		JComponent comp = new ExAreaComponent(exCell, cellsPane);
		return comp;
	}

}
