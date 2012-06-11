package com.ufida.report.anareport.applet;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JLabel;

import com.ufida.report.anareport.model.AnaRepField;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.table.format.FontFactory;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.re.SheetCellRenderer;

public class AnaFieldRenderer implements SheetCellRenderer {
	private FieldComp  m_comp = null;
	private AnaReportPlugin m_plugin;
	public AnaFieldRenderer(AnaReportPlugin plugin){
		m_comp = new FieldComp();
		m_comp.setOpaque(false);
		
		m_plugin = plugin;
	}
	public Component getCellRendererComponent(CellsPane cellsPane, Object value, boolean isSelected, boolean hasFocus,
			int row, int column, Cell cell) {
		if(!m_plugin.getModel().isFormatState() || cell == null)
			return null;
		 
		AnaRepField fld = (AnaRepField)cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
		if(fld != null){
			ExAreaModel areaModel = ExAreaModel.getInstance(cellsPane.getDataModel());
			ExAreaCell area = areaModel.getExArea(CellPosition.getInstance(row, column));
			if(area != null)
				m_comp.hor = (area.getExMode() == ExAreaCell.EX_MODE_X);
			int type = fld.getFieldType();
			if(type == AnaRepField.TYPE_DETAIL_FIELD)
				m_comp.lineColor = Color.PINK;
			else if(type == AnaRepField.TYPE_GROUP_FIELD)
				m_comp.lineColor = Color.BLUE;
			
//			m_comp.setText(fld.getFieldLabel());
			Format f = cell.getFormat();
			Font cellFont = FontFactory.createFont(f.getFontname(), f.getFontstyle(), f.getFontsize());
			m_comp.setFont(cellFont);
			m_comp.setBackground(f.getBackgroundColor());
			m_comp.setForeground(f.getForegroundColor());
		}
		return m_comp;
	}
	static class FieldComp extends JLabel{//JComponent{//
		private static final long serialVersionUID = 1L;
		public boolean hor = false;//线条的绘制方向。
		public Color lineColor = Color.PINK;//线条颜色
		int stepVer = 12;
		int stepHor = 5;
		/**
		 * 按照动态区域的方向绘制效果。
		 */
		protected  void paintComponent(Graphics g){		
			super.paintComponent(g);
			Rectangle rect = this.getBounds();
			int width = rect.width;
			int height = rect.height;
			Color c = lineColor;
			Color oldColor = g.getColor();
			g.setColor(c);
			int stepNum = 1;
			if(hor){//水平方向的动态区域，绘制水平方向的线条。
				height-=stepHor;
				while(height>0){
					g.drawLine(0,stepHor*stepNum,width,stepHor*stepNum);
					height-=stepHor;
					stepNum++;
				}				
			}else {
				width-=stepVer;
				while(width>0){
					g.drawLine(stepVer*stepNum,0,stepVer*stepNum,height);
					width-=stepVer;
					stepNum++;
				}	
			}
	        g.setColor(oldColor);
		}
	}
}
