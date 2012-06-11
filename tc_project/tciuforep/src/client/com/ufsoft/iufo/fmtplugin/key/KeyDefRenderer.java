package com.ufsoft.iufo.fmtplugin.key;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import nc.vo.iufo.keydef.KeyVO;

import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.format.FontFactory;
import com.ufsoft.table.re.SheetCellRenderer;

public class KeyDefRenderer implements SheetCellRenderer {
	com.ufsoft.table.beans.UFOLabel lblKeyComp = 
    	new com.ufsoft.table.beans.UFOLabel() {
    	private static final long serialVersionUID = 1L;
    	protected  void paintComponent(Graphics g){
    		Rectangle rect = getBounds();
    		int width = rect.width;
    		int height = rect.height;
    		Color preColor = g.getColor();
    		g.setColor(Color.RED);
    		
    		Font preFont = g.getFont();
    		Font font = FontFactory.createFont(preFont.getFontName(), Font.BOLD, preFont.getSize());
    		g.setFont(font);
    		g.drawString("k", width-7, (height-1));
    		g.setFont(preFont);
    		g.setColor(preColor);
    	}
    };
    
	public Component getCellRendererComponent(CellsPane cellsPane,   Object obj,
			boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
		// @edit by ll at 2009-5-14,ÉÏÎç10:19:58
		if(cellsPane.getOperationState() != ReportContextKey.OPERATION_FORMAT && cellsPane.getOperationState() != ReportContextKey.OPERATION_REF){
			return null;
		}
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsPane.getDataModel());
		KeyVO vo = dynAreaModel.getKeyVOAfterDataProcess(row,column);
		if(vo == null) return null;
		return lblKeyComp;
	}
}
