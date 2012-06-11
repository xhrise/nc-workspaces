package com.ufsoft.table.re;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;

import javax.swing.JComponent;

import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.format.LineFactory;
import com.ufsoft.table.format.TableConstant;

public class SelectedAreaRender extends DefaultSheetCellRenderer {

	private static final long serialVersionUID = -8018501411030048426L;

	public Component getCellRendererComponent(CellsPane table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
		//liuyy+
		if(!isSelected){
			return null;
		}
//
//		JComponent editorComp = (JComponent) super.getCellRendererComponent(
//				table, value, isSelected, hasFocus, row, column, cell);
//		return editorComp;
		return new SelectedAreaComp(table);
		
	}

	private class SelectedAreaComp extends JComponent {
		private static final long serialVersionUID = 1L;

		CellsPane m_cp = null;
		SelectedAreaComp(CellsPane cp){
			m_cp = cp;
		}
	
		protected void paintComponent(Graphics g) {

			this.setOpaque(true);

			Graphics2D g2d = (Graphics2D) g;
			
//			parintAnchor(g2d);
			
			if(m_cp.getDataModel().getSelectModel().isMultiSelected()){
				Stroke lineStroke = LineFactory
				.createLineStroke(TableConstant.L_SOLID1);
				g2d.setStroke(lineStroke);
				g2d.setColor(Color.WHITE);
				g2d.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
				
				return;
			}
			
			
			Paint oriPaint = g2d.getPaint();
			g2d.setPaint(Color.BLACK);

			int x = 1;
			int y = 1;
			int width = this.getWidth() - 3;
			int height = this.getHeight() - 3;
			
//
//			g2d.setColor(Color.WHITE);
//			g2d.drawLine(x - 2, y + 1, x + width + 2, y + 1);
//			g2d.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
//			g2d.drawLine(x + width + 2, y + height - 1, x - 2, y + height - 1);
//			g2d.drawLine(x + 1, y + height, x + 1, y + 1);
//			
			Stroke lineStroke = LineFactory
			.createLineStroke(TableConstant.L_SOLID3);
			g2d.setStroke(lineStroke);
			g2d.setColor(Color.BLACK);
			g2d.drawLine(x - 2, y, x + width + 2, y);
			g2d.drawLine(x + width, y, x + width, y + height - 1);
			g2d.drawLine(x + width + 2, y + height, x - 2, y + height);
			g2d.drawLine(x, y + height, x, y + 1);
			
			lineStroke = LineFactory
			.createLineStroke(TableConstant.L_SOLID1);
			g2d.setStroke(lineStroke);
			g2d.setColor(Color.WHITE);
			g2d.drawRect(3, 3, this.getWidth() - 7, this.getHeight() - 7);
			
			g2d.setPaint(oriPaint);

		}

	}

}
