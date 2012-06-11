/*
 * 创建日期 2005-6-22
 *
 */
package nc.ui.bi.query.designer;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import javax.swing.tree.TreePath;

import nc.ui.pub.beans.UITree;
import nc.vo.iuforeport.businessquery.SelectFldVO;

/**
 * @author zjb
 * 
 * 表字段树（拖动源）
 */
public class TableFldTree extends UITree implements DragGestureListener,
		DragSourceListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//查询字段面板实例
	private SetColumnPanel m_columnPanel = null;

	/**
	 * Construct the source tree
	 */
	public TableFldTree() {
		super();
		DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(
				this, DnDConstants.ACTION_COPY_OR_MOVE, this);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
	 */
	public void dragGestureRecognized(DragGestureEvent dge) {
		Cursor cursor = DragSource.DefaultCopyDrop; //set cursor
		Point jap = dge.getDragOrigin(); //drag point
		int x = (int) jap.getX();
		int y = (int) jap.getY();
		TreePath tp = this.getPathForLocation(x, y); //get
		// path
		if (tp != null) {
			//执行拖动
			SelectFldVO[] sfs = getColumnPanel().getGenFlds();
			dge.startDrag(cursor, new SelectFldTransferable(sfs), this);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dragEnter(DragSourceDragEvent dsde) {
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dragOver(DragSourceDragEvent dsde) {
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dropActionChanged(DragSourceDragEvent dsde) {
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
	 */
	public void dragDropEnd(DragSourceDropEvent dsde) {
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
	 */
	public void dragExit(DragSourceEvent dse) {
	}

	/*
	 * 获得父组件
	 */
	public SetColumnPanel getColumnPanel() {
		return m_columnPanel;
	}

	/*
	 * 设置父组件
	 */
	public void setColumnPanel(SetColumnPanel columnPanel) {
		m_columnPanel = columnPanel;
	}
}
