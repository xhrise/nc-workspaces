/*
 * �������� 2005-6-22
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
 * ��ѯ�ֶα���϶�Ŀ�꣩
 */
public class QueryFldTable extends UITable implements DragGestureListener,
		DropTargetListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//������
	private int m_iCount = 0;

	//��ѯ�ֶ����ʵ��
	private SetColumnPanel m_columnPanel = null;

	//��ͷ(�ɽ����϶�)
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
		//���ñ�ͷ
		//		m_th = new QueryFldTableHeader(getColumnModel());
		//		m_th.setColumnPanel(getColumnPanel());
		//		setTableHeader(m_th);
	}

	/*
	 * ���� Javadoc��
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
			//�������������ק�ͱ��ѡ�������¼���
			dge.startDrag(cursor, new StringSelection("point" + x + y));
		}
		m_iCount++;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent dtde) {
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent dtde) {
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent dtde) {
		dtde.acceptDrop(DnDConstants.ACTION_COPY); //accept drop
		//Point ap = dtde.getLocation(); // get event point
		//int row = rowAtPoint(ap);
		//int col = columnAtPoint(ap);
		try {
			//����϶�����
			Object obj = dtde.getTransferable().getTransferData(
					DataFlavor.stringFlavor);
			//setValueAt(obj.toString(), row, col);
			//ִ���ͷ�
			doDrop((SelectFldVO[]) obj);
		} catch (java.lang.Exception e) {
			AppDebug.debug(e.toString());//@devTools System.out.println(e.toString());
		}
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent dte) {
	}

	/*
	 * ��ø����
	 */
	public SetColumnPanel getColumnPanel() {
		return m_columnPanel;
	}

	/*
	 * ���ø����
	 */
	public void setColumnPanel(SetColumnPanel columnPanel) {
		m_columnPanel = columnPanel;
	}

	/*
	 * ִ���ͷ�
	 */
	public void doDrop(SelectFldVO[] sfs) {
		//�Ϸ��Լ��
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
	 * ��ñ�ͷ
	 */
	public QueryFldTableHeader getHeader() {
		return m_th;
	}

	/*
	 * ���ñ�ͷ
	 */
	public void setHeader(QueryFldTableHeader th) {
		m_th = th;
		setTableHeader(th);
		th.setColumnPanel(getColumnPanel());
	}
}
