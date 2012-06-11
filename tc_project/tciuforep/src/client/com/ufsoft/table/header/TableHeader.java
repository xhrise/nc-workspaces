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
 * Title: 表格头部的展示组件
 * </p>
 * <p>
 * Description: 这个组件负责展示普通的表头,处理行列尺寸的改变和行列的选择
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
	 * 行还是列
	 */
	int type;

	/**
	 * 行信息数据模型
	 */
	private HeaderModel model;

	/**
	 * 头部是否允许修改尺寸（行高列宽）
	 */
	protected boolean resizingAllowed;

	/** 当前正在更改头部. */
	protected Header resizingHeader;

	/** 当前正在拖动的行. */
	protected Header draggedHeader;

	/** 拖动的距离. */
	protected int draggedDistance;
	/** 表头对应的表格组件*/
	private CellsPane _cellsPane;
	
	//需要重新绘制的头部。
	private transient Integer[] m_changedIndex = null;

	/**
	 * 构造函数
	 * 
	 * @param model
	 *            RowHeaderModel 数据模型。如果为空，按照Excel中的规则构建一个简单的行列
	 * @param type
	 *            int 类型。参考Header中的静态常量
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
	 * 设置头部是否允许修改尺寸（行高列宽）
	 * 
	 * @param resizingAllowed
	 *            头部是否允许修改尺寸（行高列宽）
	 */
	public void setResizingAllowed(boolean resizingAllowed) {
		boolean old = this.resizingAllowed;
		this.resizingAllowed = resizingAllowed;
		firePropertyChange("resizingAllowed", old, resizingAllowed);
	}

	/**
	 * 得到头部是否允许修改尺寸（行高列宽）
	 * 
	 * @return boolean 头部是否允许修改尺寸（行高列宽）
	 */
	public boolean getResizingAllowed() {
		return resizingAllowed;
	}

	/**
	 * 得到类型
	 * 
	 * @return int 类型
	 * @see Header
	 */
	public int getType() {
		return type;
	}

	/**
	 * 得到被拖动头部。只有当行被拖动的过程中，返回行信息，其他情况返回空。
	 * 
	 * @return Header 得到被拖动头部
	 */
	public Header getDraggedHeader() {
		return draggedHeader;
	}

	/**
	 * 得到从起始位置算起，拖动的距离
	 * 
	 * @return int 从起始位置算起，拖动的距离
	 */
	public int getDraggedDistance() {
		return draggedDistance;
	}

	/**
	 * 得到正在更改尺寸的行列
	 * 
	 * @return Header 正在更改尺寸的行列
	 */
	public Header getResizingHeader() {
		return resizingHeader;
	}

	/**
	 * 根据Point，得到当前的行列的位置。如果在行列的有效区域之外，返回-1。
	 * 
	 * @param point
	 *            Point 物理位置
	 * @return int 行列的位置
	 */
	public int indexAtPoint(Point point) {
		return model.getIndexByPosition(type == Header.ROW ? point.y : point.x);
	}

	/**
	 * 得到指定行列的空间
	 * 
	 * @param index
	 *            int 位置索引
	 * @return 行标题的空间
	 */
	//synchronized @edit by liuyy at 2008-12-25 下午02:31:57
	public  Rectangle getHeaderRect(int index) {
		//快速拖拽滚动条绘制行列时，有行列表头重复问题，暂时synchronized处理，
		
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
	 * 得到当前组件的UI代理
	 * 
	 * @return TableHeaderUI 当前组件的UI代理
	 */
	public TableHeaderUI getUI() {
		return (TableHeaderUI) ui;
	}

	/**
	 * 设置当前组件的UI代理
	 * 
	 * @param ui
	 *            TableHeaderUI 当前组件的UI代理
	 */
	public void setUI(TableHeaderUI ui) {
		if (this.ui != ui) {
			super.setUI(ui);
			repaint();
		}
	}

	/**
	 * 设置行模型
	 * 
	 * @param hm
	 *            HeaderModel行模型
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
	 * 得到行头数据模型
	 * 
	 * @return HeaderModel 行头数据模型
	 */
	public HeaderModel getModel() {
		return model;
	}

	
	
	
	//**********************实现数据模型的监听器*****************//

	// **************私有方法********************//

	/**
	 * 数据初始化
	 */
	private void initialize() {
		setOpaque(true);
		resizingAllowed = true;
		draggedHeader = null;
		draggedDistance = 0;
		resizingHeader = null;
	}

	/**
	 * 重新布局绘制组件
	 */
	private void resizeAndRepaint() {
		// @edit by wangyga at 2009-1-19,下午03:37:44 必须重新布局，因为改变行高或列宽时
		//后边的行或列需要重新绘制
		revalidate();
		repaint();
	}

	/**
	 * 设置正在拖动的行列
	 * @param h
	 */
	public void setDraggedHeader(Header h) {
		draggedHeader = h;
	}

	/**
	 * 设置拖动的距离
	 * 
	 * @param distance
	 *            int 拖动的距离
	 */
	public void setDraggedDistance(int distance) {
		draggedDistance = distance;
	}

	/**
	 * 设置当前正在修改尺寸的行列
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
	 * 根据其在对应的JViewPort的位置确定其临时表头的大小.
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
	 * HeaderModelListener的表头数量改变的事件处理
	 */
	public void headerCountChanged(HeaderEvent e) {
		resizeAndRepaint();
		TableUtilities.newFireEvent(e);
	}

	/**
	 * HeaderModelListener的表头属性改变的事件处理
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
			
			//liuyy, size事件。
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