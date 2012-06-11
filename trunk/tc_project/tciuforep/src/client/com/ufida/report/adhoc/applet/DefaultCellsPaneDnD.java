/*
 * Created on 2005-6-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.adhoc.applet;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.rep.model.AreaPositionSelection;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;

/**
 * 报表工具面板默认的内部DnD操作
 * 
 * @author caijie
 */
public class DefaultCellsPaneDnD implements DragGestureListener, DragSourceListener, DropTargetListener{   
    /**
     * 实现拖拽的区域的长度和宽度，单位像素
     */
    public static final int DnDAreaHeight = 5;
    
    private CellsPane cellsPane = null;
    private DragSource dragSource = null;
   
    public DefaultCellsPaneDnD(CellsPane cellsPane) {
        this.cellsPane = cellsPane;        
        dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this.cellsPane, DnDConstants.ACTION_COPY_OR_MOVE, this);        
        new DropTarget(this.cellsPane,DnDConstants.ACTION_COPY_OR_MOVE, this);
    }
    
    
   /**
    * 传入选中的区域字符串
    */
    public void dragGestureRecognized(DragGestureEvent dge) {
        // TODO Auto-generated method stub
        int row = this.cellsPane.rowAtPoint(dge.getDragOrigin());
        int col = this.cellsPane.columnAtPoint(dge.getDragOrigin());
        Rectangle rect = this.cellsPane.getCellRect(CellPosition.getInstance(row,col), true);
		Point startPoint = new Point(rect.x, rect.y);
		
		if(startPoint.distance(dge.getDragOrigin()) < DefaultCellsPaneDnD.DnDAreaHeight){	
		    if(this.cellsPane.getSelectionModel().getSelectedArea() != null) {
		        AreaPosition areaPosition = this.cellsPane.getSelectionModel().getSelectedArea();
		        if(areaPosition == null) return;
			    dge.startDrag(DragSource.DefaultCopyDrop, new AreaPositionSelection(areaPosition), this);
		    }
		    
		}
        
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
     */
    public void dragEnter(DragSourceDragEvent dsde) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
     */
    public void dragOver(DragSourceDragEvent dsde) {
    	
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
     */
    public void dropActionChanged(DragSourceDragEvent dsde) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
     */
    public void dragDropEnd(DragSourceDropEvent dsde) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
     */
    public void dragExit(DragSourceEvent dse) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
     */
    public void dragEnter(DropTargetDragEvent dtde) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
     */
    public void dragOver(DropTargetDragEvent dtde) {  
        int row = this.cellsPane.rowAtPoint(dtde.getLocation());
        int col = this.cellsPane.columnAtPoint(dtde.getLocation()); 
        cellsPane.changeSelectionByUser(row, col, false, false, false);
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
     */
    public void dropActionChanged(DropTargetDragEvent dtde) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
     */
    public void drop(DropTargetDropEvent dtde) {
        if(dtde.getSource() == this) { //在面板内部托拽
            try {
                Transferable tr = dtde.getTransferable();
                if (tr.isDataFlavorSupported(AreaPositionSelection.AreaPositionFlavor)) {
                    moveCell(dtde);                    
                }
                dtde.dropComplete(true);
            } catch (Exception e) {
AppDebug.debug(e);//@devTools                 AppDebug.debug(e);
                dtde.rejectDrop();
            }
        }       
    }

    /**
     * 移动单元
     * @param dtde
     * @param tr
     * @throws UnsupportedFlavorException
     * @throws IOException
     */
    private void moveCell(DropTargetDropEvent dtde) throws UnsupportedFlavorException, IOException {
        Transferable tr = dtde.getTransferable();
        dtde.acceptDrop(java.awt.dnd.DnDConstants.ACTION_MOVE); 
        
        AreaPosition oldArea = (AreaPosition) tr.getTransferData(AreaPositionSelection.AreaPositionFlavor);        
        
        int row = this.cellsPane.rowAtPoint(dtde.getLocation());
        int col = this.cellsPane.columnAtPoint(dtde.getLocation());
        AreaPosition newArea = AreaPosition.getInstance(row, col, oldArea.getWidth(), oldArea.getHeigth());
        CellPosition newPos = newArea.getStart();
        
        this.cellsPane.getDataModel().moveCells(oldArea,newPos);
        
        // 回马一枪:利用选择模型的重新绘制机制清除旧单元格的值
	    getCellsPane().changeSelectionByUser(oldArea.getStart().getRow(), oldArea.getStart().getColumn(), false, false, false);
        cellsPane.changeSelectionByUser(newPos.getRow(), newPos.getColumn(), false, false, false);
    }


    /* (non-Javadoc)
     * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
     */
    public void dragExit(DropTargetEvent dte) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * @return Returns the cellsPane.
     */
    public CellsPane getCellsPane() {
        return cellsPane;
    }
}
