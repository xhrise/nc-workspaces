package com.ufsoft.iufo.inputplugin.biz.data;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;

import com.ufsoft.table.re.SheetCellRenderer;

public class CheckRenderer implements SheetCellRenderer {
	private Color m_renderColor = null;
	private JComponent com = new JComponent() {
		private static final long serialVersionUID = 1L;

		protected void paintComponent(Graphics g) {
			Rectangle rect = this.getBounds();
			int width = rect.width;
			int height = rect.height;
			Color preColor = g.getColor();
			g.setColor(m_renderColor);
			g.fillRect(0, 0, width, height);
			g.setColor(preColor);
		}
	};

	private CheckResultPanel m_panel = null;

	public Component getCellRendererComponent(CellsPane cellsPane,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column, Cell cell) {
		Color color = m_panel.getCheckColor();
		if (color != null
				&& m_panel.getCheckCells() != null
				&& m_panel.getCheckCells().contains(
						CellPosition.getInstance(row, column))) {
			m_renderColor = color;
			return com;
		}
		return null;

	}

	public CheckRenderer(CheckResultPanel panel) {
		super();
		m_panel = panel;
	}

}
