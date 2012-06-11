package com.ufsoft.iufo.inputplugin.biz.formulatracenew;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JComponent;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.ui.iufo.input.view.KeyCondPanel;

import com.ufsoft.report.ReportContextKey;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.TableStyle;
import com.ufsoft.table.re.SheetCellRenderer;

public class UfoFormulaTraceRenderer implements SheetCellRenderer {
	private JComponent com = new JComponent() {
		private static final long serialVersionUID = 1L;

		protected void paintComponent(Graphics g) {
			Rectangle rect = this.getBounds();
			int width = rect.width;
			int height = rect.height;
			Color preColor = g.getColor();

			Color traceColor = getTraceColor();
			g.setColor(traceColor);

			g.fillRect(0, 0, width, height);
			g.setColor(preColor);
		}

		private Color getTraceColor() {
			int foreColor = com.getBackground().getRGB();
			int sColor = TableStyle.TRACE_BACKGROUND.getRGB();
			foreColor = foreColor != sColor ? foreColor ^ ~sColor : foreColor;
			return new Color(foreColor);
		}
	};

	public Component getCellRendererComponent(CellsPane cellsPane,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column, Cell cell) {

		// edit by wangyga 此版暂时这样处理
		if (cellsPane.getOperationState() == ReportContextKey.OPERATION_FORMAT) {
			return null;
		}
		RepDataEditor editor=getRepDataEditor(cellsPane);
		if (editor==null)
			return null;
		List<CellPosition> listCheckCell = editor.getTraceCells();

		if (listCheckCell != null
				&& listCheckCell
						.contains(CellPosition.getInstance(row, column))) {
			return com;
		}
		return null;

	}
	private RepDataEditor getRepDataEditor(CellsPane cellsPane){
		Component comp=cellsPane;
		while (comp!=null){
			if (comp instanceof RepDataEditor)
				return (RepDataEditor)comp;
			comp=comp.getParent();
		}
		return null;
	}

	public UfoFormulaTraceRenderer() {
		super();
	}
}
