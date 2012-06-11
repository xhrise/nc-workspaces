package com.ufsoft.table.exarea;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.ufsoft.table.CellsPane;
import com.ufsoft.table.CellsPaneUIMouseActionHandler;

public class ExAreaMouseActionHandler extends CellsPaneUIMouseActionHandler {

//	private ExAreaCell m_cell = null;/
	public ExAreaMouseActionHandler(CellsPane cellsPane, ExAreaCell cell) {
		super(cellsPane);
//		m_cell = cell;
	}
	 

	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		int row = getCellsPane().rowAtPoint(p);
		int column = getCellsPane().columnAtPoint(p);
		if ((column == -1) || (row == -1)) {
			return;
		}
		
		ExAreaModel areaModel = ExAreaModel.getInstance(getCellsPane().getDataModel());
		ExAreaCell exCell = areaModel.getExArea(row, column);
		if(exCell == null){
			return;
		}
		
//		System.out.println(exCell);
		
		Rectangle rect = getCellsPane().getCellRect(exCell.getArea(), true);
		
		
		double distance = rect.getLocation().distance(e.getPoint());
		if (distance < 5) {
			getCellsPane().setCursor(
					Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

		} else {	
			getCellsPane().setCursor(
					Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		}
//
//		AreaPosition area = getCellsPane().getDataModel().getSelectModel()
//				.getSelectedArea();
//		if (area == null || !area.isCell())
//			return;
//		CellPosition anchor = getCellsPane().getDataModel().getSelectModel()
//				.getSelectedArea().getStart();
//		if (anchor == null)
//			return;
//		Rectangle rectangle = getCellsPane().getCellRect(anchor, true);
//		double distance = rectangle.getLocation().distance(e.getPoint());
//		//  System.out.println("distance = " + distance);
//		if (distance < 5) {
//			getCellsPane().setCursor(
//					Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
//
//		} else {
//			getCellsPane().setCursor(
//					Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//
//		}
	}

	/**
	 * 鼠标拖动的事件,目前处理区域选择.
	 * 
	 * @param e
	 */
	public void mouseDragged(MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) {
			return;
		}
		Point p = e.getPoint();
		int row = getCellsPane().rowAtPoint(p);
		int column = getCellsPane().columnAtPoint(p);
		if ((column == -1) || (row == -1)) {
			return;
		}
		//如果新的区域和上次拖动的区域不同，将新的区域放入，将上次的区域删除。
		getCellsPane().changeSelectionByUser(row, column, false, false, true);
	}
	
	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
//		if (selectedOnPress) {
//			if (shouldIgnore(e)) {
//				return;
//			}
//			repostEvent(e);
//			dispatchComponent = null;
//		} else {
//			adjustFocusAndSelection(e);
//		}
	}

}
