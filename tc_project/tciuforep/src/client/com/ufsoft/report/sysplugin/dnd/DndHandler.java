package com.ufsoft.report.sysplugin.dnd;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
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
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * 两个问题没有搞明白： 1.怎样使光标根据鼠标位置自动改变。 2.Copy和Move的支持，到底怎么回事？
 * 
 * @author zzl 2005-4-19
 */
public class DndHandler implements DragGestureListener, DragSourceListener, DropTargetListener {

	protected Component m_com;
	protected IDndAdapter m_adapter;

	private DndHandler(Component com, IDndAdapter adapter) {
		m_com = com;
		m_adapter = adapter;
	}

	public static void enableDndDrag(Component com, IDndAdapter adapter, int dragType) {
		DndHandler handler = new DndHandler(com, adapter);
		DragSource dragSource = DragSource.getDefaultDragSource();
		DragGestureRecognizer dgr = dragSource.createDefaultDragGestureRecognizer(handler.getComponent(), dragType, handler);
		dgr.setSourceActions(dgr.getSourceActions() & ~InputEvent.BUTTON3_MASK);
	}

	public static void enableDndDrop(Component com, IDndAdapter adapter) {
		DndHandler handler = new DndHandler(com, adapter);
		int dropType = DnDConstants.ACTION_COPY_OR_MOVE;
		new DropTarget(handler.getComponent(), dropType, handler);
	}

	private IDndAdapter getDndAdapter() {
		return m_adapter;
	}

	private Component getComponent() {
		return m_com;
	}

	/*
	 * 拖拽源需要实现此方法。
	 * 
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
	 */
	public void dragGestureRecognized(DragGestureEvent dge) {
		Object obj = getDndAdapter().getSourceObject();
		if (obj == null) {
			return;
		}

		// ZJB+
		InputEvent event = dge.getTriggerEvent();
		if (event != null && event instanceof MouseEvent) {
			MouseEvent me = (MouseEvent) event;
			boolean bAltDown = me.isAltDown();
			// System.out.println("Is alt down? " + bAltDown);
			if (bAltDown) {
				// @edit by ll at 2009-2-12,上午10:29:38 增加对数组类型的处理
				String[] strObj = null;
				if (obj instanceof IDragObject) {
					if (((IDragObject) obj).isArray()) {
						Object[] oos = ((IDragObject) obj).getArrayObj();
						if (oos != null && oos.length > 0) {
							strObj = new String[oos.length];
							for (int i = 0; i < oos.length; i++) {
								strObj[i] = oos[i] == null ? "" : oos[i].toString();
							}
						}
					}
				}
				if (strObj != null)
					obj = strObj;
				else
					obj = obj.toString();
			}
		}

		Transferable transferable = new DndObjectTransferable(obj);
		Cursor cursor = DragSource.DefaultCopyDrop;
		int action = dge.getDragAction();
		if (action == DnDConstants.ACTION_MOVE)
			cursor = DragSource.DefaultMoveDrop;
		dge.startDrag(cursor, transferable, this);
	}

	/*
	 * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
	 */
	public void dragDropEnd(DragSourceDropEvent dsde) {
		if (dsde.getDropSuccess() && (dsde.getDropAction() & DnDConstants.ACTION_MOVE) != 0) {
			getDndAdapter().removeSourceNode();
		}
	}

	/*
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent dtde) {
		dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
	}

	/*
	 * 拖拽目标需要实现此方法。
	 * 
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent dtde) {
		dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		java.awt.Point ap = dtde.getLocation();
		Object obj;
		try {
			obj = dtde.getTransferable().getTransferData(DndObjectTransferable.DND_OBJ_FLAVOR);
			dtde.dropComplete(getDndAdapter().insertObject(ap, obj));
		} catch (UnsupportedFlavorException e) {
			// AppDebug.debug(e);
		} catch (IOException e) {
			AppDebug.debug(e);
		}
	}

	/*
	 * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dragEnter(DragSourceDragEvent dsde) {
	}

	/*
	 * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
	 */
	public void dragExit(DragSourceEvent dse) {
	}

	/*
	 * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dragOver(DragSourceDragEvent dsde) {
		// Cursor cursor;
		//        
		// if(m_com.getDropTarget() != null){
		// cursor = DragSource.DefaultCopyDrop;
		// if (dsde.getDropAction() == DnDConstants.ACTION_MOVE){
		// cursor = DragSource.DefaultMoveDrop;
		// }
		// }else{
		// cursor = DragSource.DefaultCopyNoDrop;
		// if (dsde.getDropAction() == DnDConstants.ACTION_MOVE){
		// cursor = DragSource.DefaultMoveNoDrop;
		// }
		// }
		// if(!dsde.getDragSourceContext().getCursor().equals(cursor)){
		// dsde.getDragSourceContext().setCursor(cursor);
		// }
	}

	/*
	 * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dropActionChanged(DragSourceDragEvent dsde) {
	}

	/*
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent dte) {
	}

	/*
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent dtde) {
	}

	/*
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}
}
