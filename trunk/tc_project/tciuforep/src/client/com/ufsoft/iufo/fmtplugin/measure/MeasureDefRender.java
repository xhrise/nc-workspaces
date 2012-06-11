package com.ufsoft.iufo.fmtplugin.measure;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;

class MeasureDefRender implements SheetCellRenderer{

	/** 格式态下公式扩展属性是否显示 */
	private static boolean m_bFmlRendererVisible = true;
	
	protected static boolean isM_bFmlRendererVisible() {
		return m_bFmlRendererVisible;
	}

	public static boolean isFmlRendererVisible() {
		return m_bFmlRendererVisible;
	}

	public static void setFmlRendererVisible(boolean fmlRendererVisible) {
		m_bFmlRendererVisible = fmlRendererVisible;
	}
	
	JComponent com = new JComponent(){
		private static final long serialVersionUID = 1L;
		protected  void paintComponent(Graphics g){
//			Rectangle rect = this.getBounds();
//			int width = rect.width;
//			int height = rect.height;
//			Color preColor = g.getColor();
//			g.setColor(Color.red);
//	        g.fillRect(
//	        		width - 8,
//	        		height - 4,
//	        		8,
//	        		4);
//	        g.setColor(preColor);
			Rectangle rect = getBounds();
    		int width = rect.width;
    		int height = rect.height;
//    		super.setOpaque(false);
    		Color preColor = g.getColor();
    		g.setColor(Color.RED);
    		g.drawLine(width - 7, height - 5, width - 1, height - 5);
    		g.drawLine(width - 7, height - 4, width - 1, height - 4);
    		
    		g.drawLine(width - 3, height - 4, width - 3, height);
    		g.drawLine(width - 4, height - 4, width - 4, height);
    		g.setColor(preColor);
		}
	};

	public Component getCellRendererComponent(CellsPane cellsPane,  Object obj, boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
		if(!m_bFmlRendererVisible) return null;
		// @edit by ll at 2009-5-14,上午10:19:58
		if(cellsPane.getOperationState() != ReportContextKey.OPERATION_FORMAT && cellsPane.getOperationState() != ReportContextKey.OPERATION_REF){
			return null;
		}

		CellsModel cellsModel = cellsPane.getDataModel();
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		if(dynAreaModel.hasMeasureAfterDataProcess(row,column)){
			return com;
		}
		return null;		
	}
}
