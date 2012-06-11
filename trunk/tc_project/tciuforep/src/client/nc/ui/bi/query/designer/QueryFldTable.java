/*
 * 创建日期 2005-6-22
 *
 */
package nc.ui.bi.query.designer;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.Hashtable;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UITable;
import nc.vo.iuforeport.businessquery.SelectFldVO;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * @author zjb
 * 
 * 查询字段表格（拖动目标）
 */
public class QueryFldTable extends UITable implements DragGestureListener,
		DropTargetListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//计数器
	private int m_iCount = 0;

	//查询字段面板实例
	private SetColumnPanel m_columnPanel = null;

	//表头(可接收拖动)
	QueryFldTableHeader m_th = null;

	/**
	 * Construct the target tree
	 * 
	 * @param treeNode
	 *            DefaultMutableTreeNode
	 */
	public QueryFldTable() {
		super();
		@SuppressWarnings("unused")
		DropTarget dropTarget = new DropTarget(this,
				DnDConstants.ACTION_COPY_OR_MOVE, this);
		DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(
				this, DnDConstants.ACTION_COPY_OR_MOVE, this);
		//设置表头
		//		m_th = new QueryFldTableHeader(getColumnModel());
		//		m_th.setColumnPanel(getColumnPanel());
		//		setTableHeader(m_th);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
	 */
	public void dragGestureRecognized(DragGestureEvent dge) {
		Cursor cursor = null;
		cursor = DragSource.DefaultCopyDrop; //set cursor
		Point jap = dge.getDragOrigin(); //drag point
		int x = (int) jap.getX();
		int y = (int) jap.getY();
		if ((m_iCount & 1) == 0) {
			//测试如何区分拖拽和表格选择的鼠标事件。
			dge.startDrag(cursor, new StringSelection("point" + x + y));
		}
		m_iCount++;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent dtde) {
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent dtde) {
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent dtde) {
		dtde.acceptDrop(DnDConstants.ACTION_COPY); //accept drop
		//Point ap = dtde.getLocation(); // get event point
		//int row = rowAtPoint(ap);
		//int col = columnAtPoint(ap);
		try {
			//获得拖动数据
			Object obj = dtde.getTransferable().getTransferData(
					DataFlavor.stringFlavor);
			//setValueAt(obj.toString(), row, col);
			//执行释放
			doDrop((SelectFldVO[]) obj);
		} catch (java.lang.Exception e) {
			AppDebug.debug(e.toString());//@devTools System.out.println(e.toString());
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent dte) {
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

	/*
	 * 执行释放
	 */
	public void doDrop(SelectFldVO[] sfs) {
		//合法性检查
		Hashtable hashFldAlias = getColumnPanel().getFldPanel()
				.getHashFldAlias(-1);
		String strErr = getColumnPanel().checkMultiSelect(sfs, hashFldAlias);
		if (strErr != null) {
			MessageDialog.showWarningDlg(this, "UFBI", strErr);
			return;
		}
		getColumnPanel().getFldPanel().doAdd(sfs);
	}

	/*
	 * 获得表头
	 */
	public QueryFldTableHeader getHeader() {
		return m_th;
	}

	/*
	 * 设置表头
	 */
	public void setHeader(QueryFldTableHeader th) {
		m_th = th;
		setTableHeader(th);
		th.setColumnPanel(getColumnPanel());
	}
}
