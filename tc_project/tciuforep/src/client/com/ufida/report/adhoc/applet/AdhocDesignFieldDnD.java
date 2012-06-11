package com.ufida.report.adhoc.applet;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
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

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import nc.vo.bi.query.manager.MetaDataVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.adhoc.model.PageDimField;
import com.ufida.report.rep.model.MetaDataSelection;
import com.ufida.report.rep.model.MetaDataVOSelection;
import com.ufida.report.rep.model.SortDescriptor;
import com.ufsoft.iufo.data.IMetaData;

/**
 * Adhoc设计向导中字段选择拖拽类
 * 
 * @author caijie
 * 
 */
public class AdhocDesignFieldDnD implements DragGestureListener, DragSourceListener, DropTargetListener {

	private JList m_dndList = null;

	private DragSource m_dragSource = null;

	private DragGestureRecognizer m_recognizer = null;

	private DropTarget m_dropTarget = null;

	private int m_listSelectElemIndex = -1;

	public AdhocDesignFieldDnD(JList dndList) {
		m_dndList = dndList;
		m_dragSource = DragSource.getDefaultDragSource();
		m_recognizer = m_dragSource.createDefaultDragGestureRecognizer(m_dndList, DnDConstants.ACTION_COPY_OR_MOVE,
				this);
		m_dropTarget = new DropTarget(m_dndList, DnDConstants.ACTION_COPY_OR_MOVE, this);
	}

	public void dragGestureRecognized(DragGestureEvent dge) {
		// Object obj = m_dndList.getSelectedValue();
		int select = m_dndList.getSelectedIndex();
		if (select == -1)
			return;
		m_listSelectElemIndex = select;
		dge.startDrag(DragSource.DefaultMoveDrop, new StringSelection(" "));
	}

	public void dragEnter(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub

	}

	public void dragOver(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub

	}

	public void dropActionChanged(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub

	}

	public void dragDropEnd(DragSourceDropEvent dsde) {
		// TODO Auto-generated method stub

	}

	public void dragExit(DragSourceEvent dse) {
		JOptionPane.showMessageDialog(m_dndList, " dragExit(DragSourceEvent dse)");
		DefaultListModel model = (DefaultListModel) m_dndList.getModel();
		model.removeElementAt(m_listSelectElemIndex);
		m_listSelectElemIndex = -1;
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	public void drop(DropTargetDropEvent dtde) {
		Transferable tr = dtde.getTransferable();
		IMetaData selectFldVO = null;
		if (tr.isDataFlavorSupported(MetaDataSelection.MetaDataFlavor)) {
			try {
				dtde.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
				selectFldVO = (IMetaData) tr.getTransferData(MetaDataSelection.MetaDataFlavor);
			} catch (Exception e) {
				AppDebug.debug(e);// @devTools AppDebug.debug(e);
				dtde.rejectDrop();
			}
		} else if (tr.isDataFlavorSupported(MetaDataVOSelection.ReportFieldFlavor)) {
			try {
				dtde.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
				selectFldVO = (IMetaData) tr.getTransferData(MetaDataVOSelection.ReportFieldFlavor);
			} catch (Exception e) {
				AppDebug.debug(e);// @devTools AppDebug.debug(e);
				dtde.rejectDrop();
			}
		}
		if (selectFldVO == null)
			return;
		DefaultListModel model = (DefaultListModel) m_dndList.getModel();
		Object[] objs = model.toArray();
		for (int i = 0; i < objs.length; i++) {
			if (objs[i] instanceof SortDescriptor) {
				SortDescriptor sd = (SortDescriptor) objs[i];
				if (sd.getField().equals(selectFldVO)) {
					dtde.rejectDrop();
					dtde.dropComplete(true);
					return;
				}
			} else if (objs[i] instanceof PageDimField) {
				PageDimField pd = (PageDimField) objs[i];
				if (pd.getField().equals(selectFldVO)) {
					dtde.rejectDrop();
					dtde.dropComplete(true);
					return;
				}
			}
			// else{
			// if(objs[i].equals(selectFldVO)){
			// dtde.rejectDrop();
			// dtde.dropComplete(true);
			// return;
			// }
			// }
		}
		model.addElement(selectFldVO);
		dtde.dropComplete(true);
	}

	public void dragExit(DropTargetEvent dte) {
		// JOptionPane.showMessageDialog(m_dndList, " dragExit(DropTargetEvent
		// dse)");
		// DefaultListModel model = (DefaultListModel) m_dndList.getModel();
		// model.removeElementAt(m_listSelectElemIndex);
		// m_listSelectElemIndex = -1;
	}

}
