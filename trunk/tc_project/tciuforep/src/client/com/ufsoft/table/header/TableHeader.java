package com.ufsoft.table.header;

import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JViewport;

import com.ufsoft.table.CellsPane;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.TableUtilities;
import com.ufsoft.table.event.HeaderEvent;
import com.ufsoft.table.event.HeaderModelListener;

/**
 * <p>
 * Title: ���ͷ����չʾ���
 * </p>
 * <p>
 * Description: ����������չʾ��ͨ�ı�ͷ,�������гߴ�ĸı�����е�ѡ��
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: UFSOFT
 * </p>
 * 
 * @author wupeng
 * @version 1.0.0.1
 */

public class TableHeader extends JComponent implements HeaderModelListener {
 
	private static final long serialVersionUID = -4266826347622949503L;

	/**
	 * �л�����
	 */
	int type;

	/**
	 * ����Ϣ����ģ��
	 */
	private HeaderModel model;

	/**
	 * ͷ���Ƿ������޸ĳߴ磨�и��п�
	 */
	protected boolean resizingAllowed;

	/** ��ǰ���ڸ���ͷ��. */
	protected Header resizingHeader;

	/** ��ǰ�����϶�����. */
	protected Header draggedHeader;

	/** �϶��ľ���. */
	protected int draggedDistance;
	/** ��ͷ��Ӧ�ı�����*/
	private CellsPane _cellsPane;
	
	//��Ҫ���»��Ƶ�ͷ����
	private transient Integer[] m_changedIndex = null;

	/**
	 * ���캯��
	 * 
	 * @param model
	 *            RowHeaderModel ����ģ�͡����Ϊ�գ�����Excel�еĹ��򹹽�һ���򵥵�����
	 * @param type
	 *            int ���͡��ο�Header�еľ�̬����
	 * @param cellsPane 
	 */
	public TableHeader(HeaderModel model, int type, CellsPane cellsPane) {
		super();
		_cellsPane = cellsPane;
		if (type != Header.ROW && type != Header.COLUMN) {
			throw new IllegalArgumentException();
		}
		this.type = type;
		if (model == null) {
			throw new IllegalArgumentException();
		}
		setModel(model);
		initialize();
		setUI(TableHeaderUI.createUI(this));
	}
	
	Integer[] getChangedIndexs(){
		return m_changedIndex;
	}

	/**
	 * ����ͷ���Ƿ������޸ĳߴ磨�и��п�
	 * 
	 * @param resizingAllowed
	 *            ͷ���Ƿ������޸ĳߴ磨�и��п�
	 */
	public void setResizingAllowed(boolean resizingAllowed) {
		boolean old = this.resizingAllowed;
		this.resizingAllowed = resizingAllowed;
		firePropertyChange("resizingAllowed", old, resizingAllowed);
	}

	/**
	 * �õ�ͷ���Ƿ������޸ĳߴ磨�и��п�
	 * 
	 * @return boolean ͷ���Ƿ������޸ĳߴ磨�и��п�
	 */
	public boolean getResizingAllowed() {
		return resizingAllowed;
	}

	/**
	 * �õ�����
	 * 
	 * @return int ����
	 * @see Header
	 */
	public int getType() {
		return type;
	}

	/**
	 * �õ����϶�ͷ����ֻ�е��б��϶��Ĺ����У���������Ϣ������������ؿա�
	 * 
	 * @return Header �õ����϶�ͷ��
	 */
	public Header getDraggedHeader() {
		return draggedHeader;
	}

	/**
	 * �õ�����ʼλ�������϶��ľ���
	 * 
	 * @return int ����ʼλ�������϶��ľ���
	 */
	public int getDraggedDistance() {
		return draggedDistance;
	}

	/**
	 * �õ����ڸ��ĳߴ������
	 * 
	 * @return Header ���ڸ��ĳߴ������
	 */
	public Header getResizingHeader() {
		return resizingHeader;
	}

	/**
	 * ����Point���õ���ǰ�����е�λ�á���������е���Ч����֮�⣬����-1��
	 * 
	 * @param point
	 *            Point ����λ��
	 * @return int ���е�λ��
	 */
	public int indexAtPoint(Point point) {
		return model.getIndexByPosition(type == Header.ROW ? point.y : point.x);
	}

	/**
	 * �õ�ָ�����еĿռ�
	 * 
	 * @param index
	 *            int λ������
	 * @return �б���Ŀռ�
	 */
	//synchronized @edit by liuyy at 2008-12-25 ����02:31:57
	public  Rectangle getHeaderRect(int index) {
		//������ק��������������ʱ�������б�ͷ�ظ����⣬��ʱsynchronized����
		
		int size = 0;
		if (index >= model.getTempCount()) {
			size = model.getTotalSize();
		} else if (index > 0) {
			size  = model.getPosition(index);
		}
		
		
		Rectangle r = new Rectangle();
		if (type == Header.ROW) {
			r.width = getWidth();
			r.y = size;
			r.height = model.getSize(index);
		} else {
			r.height = getHeight();
			r.x  = size;
			r.width = model.getSize(index);
		}
		return r;
	}

	/**
	 * �õ���ǰ�����UI����
	 * 
	 * @return TableHeaderUI ��ǰ�����UI����
	 */
	public TableHeaderUI getUI() {
		return (TableHeaderUI) ui;
	}

	/**
	 * ���õ�ǰ�����UI����
	 * 
	 * @param ui
	 *            TableHeaderUI ��ǰ�����UI����
	 */
	public void setUI(TableHeaderUI ui) {
		if (this.ui != ui) {
			super.setUI(ui);
			repaint();
		}
	}

	/**
	 * ������ģ��
	 * 
	 * @param hm
	 *            HeaderModel��ģ��
	 */
	public void setModel(HeaderModel hm) {
		if (hm == null) {
			throw new IllegalArgumentException(
					"Cannot set a null RowHeaderModel");
		}
		HeaderModel old = this.model;
		if (hm != old) {
			if (old != null) {
				old.removeHeaderModelListener(this);
			}
			this.model = hm;
			hm.addHeaderModelListener(this);
			firePropertyChange("headerModel", old, hm);
			resizeAndRepaint();
		}
	}

	/**
	 * �õ���ͷ����ģ��
	 * 
	 * @return HeaderModel ��ͷ����ģ��
	 */
	public HeaderModel getModel() {
		return model;
	}

	
	
	
	//**********************ʵ������ģ�͵ļ�����*****************//

	// **************˽�з���********************//

	/**
	 * ���ݳ�ʼ��
	 */
	private void initialize() {
		setOpaque(true);
		resizingAllowed = true;
		draggedHeader = null;
		draggedDistance = 0;
		resizingHeader = null;
	}

	/**
	 * ���²��ֻ������
	 */
	private void resizeAndRepaint() {
		// @edit by wangyga at 2009-1-19,����03:37:44 �������²��֣���Ϊ�ı��и߻��п�ʱ
		//��ߵ��л�����Ҫ���»���
		revalidate();
		repaint();
	}

	/**
	 * ���������϶�������
	 * @param h
	 */
	public void setDraggedHeader(Header h) {
		draggedHeader = h;
	}

	/**
	 * �����϶��ľ���
	 * 
	 * @param distance
	 *            int �϶��ľ���
	 */
	public void setDraggedDistance(int distance) {
		draggedDistance = distance;
	}

	/**
	 * ���õ�ǰ�����޸ĳߴ������
	 * 
	 * @param h
	 */
	public void setResizingHeader(Header h) {
		resizingHeader = h;
	}

	public Object clone() {
		return new TableHeader(model, type, _cellsPane);
	}

	/**
	 * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
	 */
	public String isSupport(int source, EventObject e)
			throws ForbidedOprException {
		return TableUtilities.newCheckEvent(source, e);
	}

	/**
	 * �������ڶ�Ӧ��JViewPort��λ��ȷ������ʱ��ͷ�Ĵ�С.
	 */
	public void reDefineCount() {
		JViewport view = (JViewport) getParent();
		if (view != null) {
			Rectangle rect = view.getViewRect();
			int size = type == Header.ROW ? rect.y + rect.height : rect.x
					+ rect.width;
			model.setTempCountBySize(size);
		}
	}

	/**
	 * HeaderModelListener�ı�ͷ�����ı���¼�����
	 */
	public void headerCountChanged(HeaderEvent e) {
		resizeAndRepaint();
		TableUtilities.newFireEvent(e);
	}

	/**
	 * HeaderModelListener�ı�ͷ���Ըı���¼�����
	 */
	public void headerPropertyChanged(PropertyChangeEvent e) {
		if (e.getPropertyName().equals(Header.HEADER_AUTORESIZE)) {
			int index = ((Integer) e.getNewValue()).intValue();
			HeaderModel headerModel = ((HeaderModel) e.getSource());
			boolean isUserDef = ((Boolean) e.getOldValue()).booleanValue();
			boolean isRow = headerModel.getType() == Header.ROW;
			int fitSize = _cellsPane.getFitHeaderSize(isRow, index);
			if (fitSize > 0) {
				fitSize++;
			}
			Header header = headerModel.getHeader(index);
			if(header == null){
				return;
			}
			

			int oldSize = header.getSize();
			
			if (isUserDef) {
				header.setSize(fitSize);
			} else {
				header.autoSetSize(fitSize);
			}
			
			//undo
			HeaderSizeUndo undo = new HeaderSizeUndo(headerModel, index, oldSize, fitSize);
			headerModel.getCellsModel().fireUndoHappened(undo);
			
			//liuyy, size�¼���
			PropertyChangeEvent evt = new PropertyChangeEvent(
					new Header[] { header }, Header.HEADER_SIZE_PROPERTY, null,
					null);
			model.fireHeaderPropertyChanged(evt);

		}
		
		try{
			Object src = e.getSource();
			if(src instanceof Integer[]){
				m_changedIndex = (Integer[]) src;
			} else {
				m_changedIndex = null;
			}
			resizeAndRepaint();
		
		} finally {
			
		}
		
		
	}

	public int getPriority() {
		return HeaderModelListener.MAX_PRIORITY;
	}

}