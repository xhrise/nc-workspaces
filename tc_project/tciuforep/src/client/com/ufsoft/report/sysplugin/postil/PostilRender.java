package com.ufsoft.report.sysplugin.postil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * @author zzl Create on 2005-3-7
 */
public class PostilRender implements SheetCellRenderer {

	PostilComponent component;

	class PostilComponent extends JComponent {
		private static final long serialVersionUID = -3607113573898089013L;

		protected void paintComponent(Graphics g) {
			Rectangle rect = this.getBounds();
			int width = rect.width;
			// int height = rect.height;
			Color preColor = g.getColor();
			// g.setColor(Color.red);
			// g.fillPolygon(new int[]{11*width/12,width,width},
			// new int[]{0,0,height/5},
			// 3);
			// g.setColor(Color.green);
			g.setColor(new Color(241, 57, 36));
			// g.fillPolygon(new int[]{width-8,width,width},
			// new int[]{0,0,5},
			// 3);
			// modify by wangyga
			g.fillPolygon(new int[] { width - 7, width, width }, new int[] { 0,
					0, 7 }, 3);
			g.setColor(preColor);
		}
	};

	/**
	 * 
	 */
	public PostilRender() {
		super();
		component = new PostilComponent();
	}

	/*
	 * @param cellsPane @param value @param isSelected @param hasFocus @param
	 * row @param column @return
	 * 
	 * @see com.ufsoft.table.re.SheetCellRenderer#getCellRendererComponent(com.ufsoft.table.CellsPane,
	 *      java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getCellRendererComponent(CellsPane cellsPane, Object obj,
			boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
		return component;
	}
}
