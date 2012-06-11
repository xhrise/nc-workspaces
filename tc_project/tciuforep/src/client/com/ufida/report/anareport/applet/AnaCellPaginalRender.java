package com.ufida.report.anareport.applet;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

import nc.ui.pub.beans.UILabel;

import com.ufida.report.anareport.model.AnaCellPagination;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;

public class AnaCellPaginalRender implements SheetCellRenderer {
	private JLabel paginalComponent = null;

	public AnaCellPaginalRender() {

	}

	public Component getCellRendererComponent(CellsPane cellsPane, Object value, boolean isSelected, boolean hasFocus,
			int row, int column, Cell cell) {
		if (cell == null || !(cellsPane.getOperationState() == ReportContextKey.OPERATION_FORMAT))
			return null;
		AnaRepField field = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
		if (field == null) {
			return null;
		}
		Object obj = field.getDimInfo().getCellPagination();
		if (obj == null) {
			return null;
		} else {
//			return null;
			 return getPaintComponent();
		}
	}

	private JLabel getPaintComponent() {
		if (paginalComponent == null) {
			paginalComponent = new UILabel();
			Border line = BorderFactory.createLineBorder(Color.PINK, 2);
			paginalComponent.setBorder(line);
			paginalComponent.setOpaque(false);
			paginalComponent.setVerticalAlignment(JLabel.TOP);
		}
		return paginalComponent;
	}
}
