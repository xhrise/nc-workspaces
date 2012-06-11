package com.ufida.report.adhoc.applet;

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.CellsPaneUIMouseActionHandler;

public class AdhocCellsPaneUIMouseActionHandler extends CellsPaneUIMouseActionHandler{	
	
	public AdhocCellsPaneUIMouseActionHandler(CellsPane cellsPane) {
		super(cellsPane);		
	}
	
//	public void mouseClicked(MouseEvent e) {
//		if(e.getClickCount() == 2){
//			AreaPosition area = getCellsPane().getDataModel().getSelectModel().getSelectedArea();
//			JOptionPane.showMessageDialog(getCellsPane(), area.toString());
//		}
//	}
	
	 public void mouseMoved(MouseEvent e) {	
	
     	AreaPosition area = getCellsPane().getDataModel().getSelectModel().getSelectedArea();
     	if(area == null || !area.isCell()) return;
         CellPosition anchor = getCellsPane().getDataModel().getSelectModel().getSelectedArea().getStart();
         if(anchor == null) return;
         Rectangle rectangle =getCellsPane().getCellRect(anchor, true);
         double distance = rectangle.getLocation().distance(e.getPoint());
       //  System.out.println("distance = " + distance);
         if( distance < DefaultCellsPaneDnD.DnDAreaHeight){
        	 getCellsPane().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        	
         }else {
        	 getCellsPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        	 
         }                	
   }
}
