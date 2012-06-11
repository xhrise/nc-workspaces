package com.ufsoft.table.exarea;

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

import com.ufsoft.table.CellsPane;

/**
 * À©Õ¹ÇøÓòÍÏ×§
 * @author liuyy
 *
 */
public class ExAreaDnD implements DragGestureListener, DragSourceListener, DropTargetListener{   
    
    private CellsPane m_cellsPane = null;
    private DragSource dragSource = null;

    public ExAreaDnD(CellsPane cellsPane) {
        this.m_cellsPane = cellsPane;        
        dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this.m_cellsPane, DnDConstants.ACTION_COPY_OR_MOVE, this);        
        new DropTarget(this.m_cellsPane,DnDConstants.ACTION_COPY_OR_MOVE, this);
    }
    
	public void dragGestureRecognized(DragGestureEvent dge) {
		// TODO Auto-generated method stub
		
	}

	public void dragDropEnd(DragSourceDropEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	public void dragEnter(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	public void dragExit(DragSourceEvent dse) {
		// TODO Auto-generated method stub
		
	}

	public void dragOver(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	public void dropActionChanged(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub
		
	}

	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	public void drop(DropTargetDropEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

}
