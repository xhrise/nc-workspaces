package com.ufsoft.report.sysplugin.cellpostil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;

public class CellPostilRenderer implements SheetCellRenderer {
	private CellPostilDefPlugin plugin;
	
	private PostilComponent component;
	
	public CellPostilRenderer() {
		super();
		component = new PostilComponent();
	}

	class PostilComponent extends JComponent {
		private static final long serialVersionUID = -3607113573898089013L;

		protected void paintComponent(Graphics g) {
			Rectangle rect = this.getBounds();
			int width = rect.width;
			Color preColor = g.getColor();
			g.setColor(new Color(241, 57, 36));
			g.fillPolygon(new int[] { width - 7, width, width }, new int[] { 0,
					0, 7 }, 3);
			g.setColor(preColor);
		}
	};

	

	public Component getCellRendererComponent(CellsPane cellsPane, Object obj,
			boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {

		return component;
	}
}
