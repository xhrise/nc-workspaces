package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.IArea;
import com.ufsoft.table.TableStyle;
import com.ufsoft.table.format.DefaultFormatValue;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.re.SheetCellRenderer;

public class FormulaTraceRenderer implements SheetCellRenderer {
	private FormulaTraceNavPanel m_panel = null;

	public FormulaTraceRenderer(FormulaTraceNavPanel panel) {
		super();
		m_panel = panel;
	}
	private JComponent com = new JComponent() {
		private static final long serialVersionUID = 1L;
		protected void paintComponent(Graphics g) {
			Rectangle rect = this.getBounds();
			int width = rect.width;
			int height = rect.height;
			Color preColor = g.getColor();
			
			Color traceColor = getTraceColor();
			g.setColor(traceColor);
			
//			g.setColor(m_renderColor);
			g.fillRect(0, 0, width ,height);
			g.setColor(preColor);
		}
		private Color getTraceColor() {
//			return m_renderColor;
			int foreColor = com.getBackground().getRGB();
			int sColor = TableStyle.TRACE_BACKGROUND.getRGB();
			foreColor = foreColor != sColor ? foreColor ^ ~sColor : foreColor;
			return new Color(foreColor);
		}
	};

	public Component getCellRendererComponent(CellsPane cellsPane, Object obj,
		  boolean isSelected, boolean hasFocus, int row,
			int column, Cell cell) {
		if(m_panel==null || m_panel.getCurTracedPos() == null){
			return null;
		}
		int nTracedCount = m_panel.getCurTracedPos().length;
		IArea[] curTraecdPos = m_panel.getCurTracedPos();
		for(int i = 0; i < nTracedCount; i++){
			if(curTraecdPos[i] != null && curTraecdPos[i].contain(CellPosition.getInstance(row, column))){
				Format format = m_panel.getReport().getCellsModel().getRealFormat(CellPosition.getInstance(row, column));
				com.setBackground(format == null || format.getBackgroundColor()==null?DefaultFormatValue.BACK_COLOR:format.getBackgroundColor());
				return com;
			}
		}
		return null;		
	}

}
