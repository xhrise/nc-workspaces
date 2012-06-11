package com.ufsoft.iufo.fmtplugin.rounddigitarea;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;

public class RoundDigitAreaRender implements SheetCellRenderer{

	   RonudDigitAreaComponent component;
	    
	    class RonudDigitAreaComponent extends JComponent{
			private static final long serialVersionUID = 8018494711428822161L;

			protected  void paintComponent(Graphics g){
				Rectangle rect = this.getBounds();
				int width = rect.width;
				int height = rect.height;
				Color preColor = g.getColor();
				g.setColor(Color.BLUE);
				g.drawLine(0, height/3, height/3, 0);
		        g.setColor(preColor);
			}
		};
		
	    
	    /**
	     * 
	     */
	    public RoundDigitAreaRender() {
	        super();
	        component = new RonudDigitAreaComponent();
	    }

	    /**
	     * @param cellsPane
	     * @param value
	     * @param isSelected
	     * @param hasFocus
	     * @param row
	     * @param column
	     * @return 
	     * @see com.ufsoft.table.re.SheetCellRenderer#getCellRendererComponent(com.ufsoft.table.CellsPane, java.lang.Object, boolean, boolean, int, int)
	     */
	    public Component getCellRendererComponent(CellsPane cellsPane, Object obj, boolean isSelected, boolean hasFocus,
	            int row, int column, Cell cell) {
	    	RoundDigitAreaModel model = RoundDigitAreaModel.getInstance(cellsPane.getDataModel());
	    	if(model.isDisplay() && model.isUnRoundDigitPos(CellPosition.getInstance(row,column))){
	    		return component;
	    	}else{
	    		return null;
	    	}
	    }

}
