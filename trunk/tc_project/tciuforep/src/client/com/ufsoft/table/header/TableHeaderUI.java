package com.ufsoft.table.header;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.BorderFactory;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;

import com.ufsoft.report.component.RepHeaderUI;
import com.ufsoft.report.component.StyleUtil;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.SelectModel;
import com.ufsoft.table.TablePane;
import com.ufsoft.table.TableStyle;
import com.ufsoft.table.TableUtilities;
import com.ufsoft.table.UFOTable;

/**
 * <p>
 * Title: 表格头部的展示组件的UI代理
 * </p>
 * <p>
 * Description: 这个组件图象的绘制和鼠标事件的处理
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: UFSOFT
 * </p>
 * 
 * @author wupeng
 * @version 1.0.0.1 2004－03－05 在列的关联位置点击鼠标引起的行列宽度设置的错误
 */
public class TableHeaderUI extends ComponentUI {

	/**
	 * 列头部拖动光标常量
	 */
	private final static Cursor CURSOR_COLRESIZE = Cursor
			.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
	/**
	 * 行头部拖动光标常量
	 */
	private final static Cursor CURSOR_ROWRESIZE = Cursor
			.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
	/** 使用该代理的组件 */
	private TableHeader m_header;
	/**
	 * 参见JDK中的解释，它的功效是阻隔在一个使用Render的组件和它的Render之间，
	 * 否则repaint()和invalidate()方法在这两个组件上都会发生。
	 */
	private CellRendererPane rendererPane;

	private JLabel headerRender = new HeaderRenderer();
	
	{
		headerRender.setHorizontalAlignment(SwingConstants.CENTER);
		headerRender.setUI(new RepHeaderUI());

	}
	//	private static Border FIRST_BORDER = BorderFactory.createMatteBorder(1, 1,
	//			1, 1, Color.black);
	private final static Border ROW_BORDER = BorderFactory.createMatteBorder(0,
			1, 1, 1, StyleUtil.headerSeperatorColor);
	private final static Border COL_BORDER = BorderFactory.createMatteBorder(1,
			0, 1, 1, StyleUtil.headerSeperatorColor);

	/**
	 * 该组件的鼠标监听器
	 */
	private MouseInputListener mouseInputListener;

	//*******************************************************************//
	/**
	 * 构造函数
	 */
	public TableHeaderUI() {
		super();
	}

	//******************************以下代码处理UI的安装和监听器构建******************************************//

	/**
	 * 构建鼠标监听器
	 */
	private MouseInputListener createMouseInputListener() {
		return new MouseInputHandler();
	}

	/**
	 * 构建UI。
	 * 
	 * @param h
	 *            JComponent
	 * @return ComponentUI
	 */
	public static ComponentUI createUI(JComponent h) {
		return new TableHeaderUI();
	}

	/**
	 * 安装UI代理，该方法由使用UI的组件调用
	 * 
	 * @param c
	 *            JComponent 向UI中注册的组件。
	 */
	public void installUI(JComponent c) {
		m_header = (TableHeader) c;
		rendererPane = new CellRendererPane();
		m_header.add(rendererPane);
		installListeners();
	}

	/**
	 * 卸载UI代理，该方法由使用UI的组件调用
	 * 
	 * @param c
	 *            JComponent 向UI中注册的组件。
	 */
	public void uninstallUI(JComponent c) {
		uninstallListeners();
		m_header.remove(rendererPane);
		rendererPane = null;
		m_header = null;
	}

	/**
	 * 添加监听器
	 */
	private void installListeners() {
		mouseInputListener = createMouseInputListener();
		m_header.addMouseListener(mouseInputListener);
		m_header.addMouseMotionListener(mouseInputListener);
	}

	/**
	 * 卸载监听器
	 */
	private void uninstallListeners() {
		m_header.removeMouseListener(mouseInputListener);
		m_header.removeMouseMotionListener(mouseInputListener);
		mouseInputListener = null;
	}

	//***************************以下为绘制代码**************************//
	/**
	 * 以下部分为绘制代码，参见父类注释
	 * 
	 * @param g
	 * @param c
	 */
	public void paint(Graphics g, JComponent c) {
		HeaderModel hm = m_header.getModel();
		int tempCount = hm.getTempCount();

		if (tempCount <= 0) { //没有绘制的行。
			return;
		}

		//局部绘制暂时有问题
		//		Integer[] indexs = m_header.getChangedIndexs();
		//		if(indexs != null){
		//			for(Integer index: indexs){
		//				paintCell(g,  index);
		//			}
		////			rendererPane.removeAll();
		//			return;
		//		}

		Rectangle clip = g.getClipBounds();
		//得到绘制区域的起点和终点
		Point start = clip.getLocation();
		Point end = isRowHeader() ? new Point(clip.x, clip.y + clip.height - 1)
				: new Point(clip.x + clip.width - 1, clip.y);

		//得到当前区域容纳的行列范围.如果返回值为负数，表示越界
		int min = m_header.indexAtPoint(start);
		int max = m_header.indexAtPoint(end);
		if (min < 0) {
			min = 0;
		}
		if (max < 0) {
			max = hm.getCount() - 1;
		}

//		boolean bInf = this.m_header.getModel().getCellsModel().isInfinite();
		if (max > tempCount) {
			max = tempCount - 1;
		}
		Header hDrag = m_header.getDraggedHeader();
		Header h;
		
//		if(this.m_header.getModel().getType() == Header.COLUMN){
//			System.out.println("Max:" + max);
//		}

		//绘制区域所有的单元
		for (int i = min; i <= max; i++) {
			h = hm.getHeader(i);
			if(h == null || h == hDrag){
				continue;
			}
			paintCell(g, i);
		}

		rendererPane.removeAll();
	}

	/**
	 * 得到某个行列标题的渲染器
	 * 
	 * @param index
	 * @return Component
	 */
	private  Component getHeaderRenderer(int index) {
		Header h = m_header.getModel().getHeader(index);
		
		String value;
	 
			if (h == null || (h instanceof TempHeader) || h.getValue() == null) {
				value = m_header.getType() == Header.ROW ? TableUtilities
						.getRowName(index) : TableUtilities.getColName(index);
			} else {
				Object oValue = h.getValue();
				value = oValue == null ? null : oValue.toString();
			}
			
			
			//  设置边框
			Border border = null;
			if (m_header.type == Header.ROW) {
				border = ROW_BORDER;
			} else {
				border = COL_BORDER;
			}
			headerRender.setName(isRowHeader() ? "rowHeader" : "colHeader");
//			AppDebug.debug("========" + index + ": " + value + "=====");
			headerRender.setText(value);
			headerRender.setBorder(border);
		 
		
//		// 设置锚点标记
		setHeaderAnchor(h, index);

		//		headerRender.setOpaque(true);
		//headerRender.setBackground(getbgColor((HeaderRenderer)headerRender));

		return headerRender;
	}

	private Color getbgColor(HeaderRenderer c) {
		Color color = null;
		if (c.isSelected()) {
			color = StyleUtil.headerBackground;

		} else if ("colHeader".equals(c.getName())) {
			Color startColor = StyleUtil.colHeaderStartColor;
			Color endColor = StyleUtil.colHeaderEndColor;

			color = endColor;

		} else {
			color = StyleUtil.rowHeaderColor;

		}
		return color;
	}

	/**
	 * 设置行头列头选中标记状态
	 * @param h 行头/列头
	 * @param index 行头/列头编号
	 */
	private void setHeaderAnchor(Header h, int index) {
		if (h == null) {
			return;
		}
		if (m_header.getModel().isAllSelHeader()) {
			((HeaderRenderer) headerRender).setSelected(true);
			return;
		}
		((HeaderRenderer) headerRender).setSelected(m_header.getModel()
				.isSelected(index));

	}

	/**
	 * 绘制表头单元
	 * @param g
	 * @param cellRect
	 * @param index
	 */
	private void paintCell(Graphics g, int index) {
		Rectangle cellRect = m_header.getHeaderRect(index);
		Component component = getHeaderRenderer(index);
		rendererPane.paintComponent(g, component, m_header, cellRect.x,
				cellRect.y, cellRect.width, cellRect.height, false);// //liyyy， 参数改为false，绘制时不作组件校验。
	}

	/**
	 * 有表头（参数）获取其位置序号
	 * @param head
	 * @return
	 */
	private int viewIndexForHeader(Header head) {
		HeaderModel hm = m_header.getModel();
		int count = hm.getCount();
		for (int i = 0; i < count; i++) {
			if (hm.getHeader(i) == head) {
				return i;
			}
		}
		return -1;
	}

	//
	// Size Methods
	//

	/**
	 * 根据头部的渲染器，获得头部的另外一个纬度的尺寸。例如，行头它的高度是用户设置，但是它的宽度 是由渲染器控制的。
	 */
	/*
	 * private int getHeaderRectSize() { int size = 0; HeaderModel model =
	 * header.getModel(); for (int h = 0; h < model.getCount(); h++) { Header
	 * header = model.getHeader(h); Component comp = getHeaderRenderer(h); size =
	 * isRowHeader() ? comp.getPreferredSize().width :
	 * comp.getPreferredSize().height; if (size > 0) { break; } } return size; }
	 */

	private Dimension createHeaderSize(long size) {
		if (size > Integer.MAX_VALUE) {
			size = Integer.MAX_VALUE;
		}
		return isRowHeader() ? new Dimension(TableStyle.ROW_HEADER_WIDTH,
				(int) size) : new Dimension((int) size,
				TableStyle.COL_HEADER_HEIGTH);
	}

	/**
	 * 得到当前组件的最小的尺寸.
	 * 
	 * @param c
	 * @return Dimension
	 */
	public Dimension getMinimumSize(JComponent c) {
//		long size = 0;
//		Enumeration enumeration = m_header.getModel().getHeaders();
//		while (enumeration.hasMoreElements()) {
//			Header h = (Header) enumeration.nextElement();
//			size = size + h.getMinSize();
//		}
		int min = 0;
		Header[] headers = m_header.getModel().getHeaders();
		for(Header h: headers){
			if (h != null && h.isVisible()) {
				min += h.getMinSize();
			}
		}
		return createHeaderSize(min);
	}

	/**
	 * 计算组件的缺省尺寸
	 * 
	 * @param c
	 * @return Dimension
	 *  
	 */
	public Dimension getPreferredSize(JComponent c) {
		HeaderModel hm = m_header.getModel();
		return createHeaderSize(hm.getTotalSize());
	}

	/**
	 * 计算组件最大尺寸
	 * 
	 * @param c
	 * @return Dimension
	 */
	public Dimension getMaximumSize(JComponent c) {
		HeaderModel hm = m_header.getModel();
		long size = hm.getPosition(hm.getCount());
//		Enumeration enumeration = m_header.getModel().getHeaders();
//		while (enumeration.hasMoreElements()) {
//			Header h = (Header) enumeration.nextElement();
//			size = size + h.getMaxSize();
//		}
		return createHeaderSize(size);
	}

	/**
	 * 是否是行模型
	 */
	private boolean isRowHeader() {
		return m_header.getType() == Header.ROW;
	}

	/**
	 * 行列的高宽是否允许调整
	 */
	private boolean isResizable() {
		return m_header.getResizingAllowed();
	}

	/**
	 * 计算选中行列时，缺省选中的单元格，即单个单元格，如果前面存在组合单元，则向下查找
	 * 
	 * @param type
	 * @param index
	 * @return
	 */
	private int getAnchorCell(int type, int index) {
		CellsModel cellsModel = m_header.getModel().getSelModel()
				.getCellsModel();
		int tmpHeaderNum = (type == Header.ROW) ? cellsModel.getRowTempNum()
				: cellsModel.getColTempNum();

		CellPosition cellPos = null;
		for (int i = 0; i < tmpHeaderNum; i++) {
			cellPos = (type == Header.ROW) ? CellPosition.getInstance(index, i)
					: CellPosition.getInstance(i, index);
			CombinedCell combCell = cellsModel.getCombinedAreaModel()
					.belongToCombinedCell(cellPos);
			if (combCell == null) {
				return i;
			}
		}
		return 0;

	}

	//*******************以下代码是鼠标监听器的实现***************************//
	/**
	 * 内部类实现鼠标监听器
	 */
	private class MouseInputHandler implements MouseInputListener {
		/**
		 * 拖拽单元的起始点
		 */
		private int headerStart;
		/**
		 * 记录拖动过程中采用XOR模式绘制时,上次绘制的位置.
		 */
		private int lastDraw = -1;
		/** 记录移动行列的索引位置 */
		private int dragIndex = 0;

		private Cursor otherCursor = isRowHeader() ? CURSOR_ROWRESIZE
				: CURSOR_COLRESIZE;

		/**
		 * 鼠标点击主要是激发行列的选择,和自动调整行高列宽.
		 */
		public void mouseClicked(MouseEvent e) {
		 // 当处于调整头部大小的区域时，不激发选择事件
			if (getResizingHeader(e.getPoint()) != null) {
				if (e.getClickCount() == 2) {
					m_header.getModel().setFitSize(
							m_header.indexAtPoint(e.getPoint()));
					return;
				}
			}

			int type = m_header.getType();
			int index = m_header.indexAtPoint(e.getPoint());
			HeaderModel model = m_header.getModel();
			SelectModel selModel = model.getSelModel();
			
			//liuyy+, 2008-09-12 对已经选中的多行点击右键，直接显示右键。
			if(SwingUtilities.isRightMouseButton(e) && model.isSelected(index)){
				return;
			}

			if (e.isControlDown()) { //Ctrl只是添加
				//设置锚点
				if (type == Header.ROW) {
					selModel.setAnchorCell(index, 0);
				} else {
					selModel.setAnchorCell(0, index);
				}
				model.setSelectedHeader(index, index);
				selModel.setSelectedHeader(new int[] { index, index }, type);
			} else if (e.isShiftDown()) { //需要删除上次Shift添加的内容
				CellPosition anchorCell = selModel.getAnchorCell();
				int anchorIndex = type == Header.ROW ? anchorCell.getRow()
						: anchorCell.getColumn();
				model.setSelectedHeader(index, anchorIndex);
				selModel.setSelectedHeader(new int[] { index, anchorIndex },
						type);
			} else {
				//鼠标单击时先设置新的选中头,再清除之前行列锚点的选中标记,顺序不能颠倒
				//				model.clearAnchors();
				//				model.getOtherRowOrColHeaderModel().clearAnchors();

				//设置锚点
				int anchorPos = getAnchorCell(type, index);
				if (type == Header.ROW) {
					selModel.setAnchorCell(index, anchorPos);
				} else {
					selModel.setAnchorCell(anchorPos, index);
				}

				//				model.setSelectedHeader(new int[]{index,index});
				selModel.setSelectedHeader(new int[] { index, index }, type);
				model.setSelectAllHeader(model.getOtherRowOrColHeaderModel());
			}

		}

		private Header getResizingHeader(Point p) {
			int h = m_header.indexAtPoint(p);
			int index = getResizingIndex(p, h);
			return index > -1 ? m_header.getModel().getHeader(index) : null;
		}

		private int getResizingIndex(Point p, int h) {
			if (h == -1) {
				return -1;
			}
			Rectangle r = m_header.getHeaderRect(h);
			if (isRowHeader()) {
				r.grow(0, -3);
			} else {
				r.grow(-3, 0);
			}
			if (r.contains(p)) {
				return -1;
			}

			//当最后一列隐藏时，鼠标在头视图尾部时都认为是要改变最后一列。
			//最后一列隐藏时，要想改变倒数第二列的尺寸，需要先将最后一列拖出来成非隐藏状态。
			int headerCount = m_header.getModel().getTempCount();
			if (h == headerCount - 2
					&& m_header.getModel().getSize(headerCount - 1) == 1) {
				return headerCount - 1;
			}

			int midPoint = isRowHeader() ? r.y + r.height / 2 : r.x + r.width
					/ 2;
			int index;
			if (isRowHeader()) {
				index = (p.y < midPoint) ? h - 1 : h;
			} else {
				index = (p.x < midPoint) ? h - 1 : h;
			}
			return index;
		}

		public void mousePressed(MouseEvent e) {
			m_header.setDraggedHeader(null);
			m_header.setResizingHeader(null);
			m_header.setDraggedDistance(0);
			//			m_header.
			Point p = e.getPoint();
			int index = m_header.indexAtPoint(p);
			
			 if(index < 0){
				 return;
			 }
			 
			 //wangyga+ 使组件获得焦点
			 if(m_header.isRequestFocusEnabled()){
				 m_header.requestFocus();
			 }
			 
			 //@edit by liuyy at 2008-12-26 上午11:28:20 处理有限表情况。
			 HeaderModel headerModel = m_header.getModel();
			if(!headerModel.getCellsModel().isInfinite() && index >= headerModel.getCount()){
				return;
			 }
				 
			// The last 3 pixels + 3 pixels of next column are for resizing
			dragIndex = getResizingIndex(p, index);
			if (dragIndex != -1) { //此处处理拖动改变行高列宽的代码。
				Header hResizing = headerModel
						.initHeader(dragIndex);
				if (isResizable() && hResizing != null) {
					m_header.setResizingHeader(hResizing);
					headerStart = (isRowHeader() ? p.y : p.x)
							- (int) (hResizing.getSize() * TablePane
									.getViewScale());
				}
			} else { //此处处理拖动选择的代码
				mouseClicked(e);
			}
			 
		}

		/**
		 * 转换拖动状态和正常状态的光标
		 */
		private void swapCursor() {
			Cursor tmp = m_header.getCursor();
			m_header.setCursor(otherCursor);
			otherCursor = tmp;
		}

		public void mouseMoved(MouseEvent e) {
			Point p = e.getPoint();
			Header h = getResizingHeader(p);
			if ((h != null && isResizable()) != (m_header.getCursor() == CURSOR_ROWRESIZE || m_header
					.getCursor() == CURSOR_COLRESIZE)) {
				swapCursor();
			}
		}

		/**
		 * 处理鼠标拖动。包括处理行高列宽的改变，以及处理行列选择
		 * 
		 * @param e
		 */
		public void mouseDragged(MouseEvent e) {
			int mouseStart = isRowHeader() ? e.getY() : e.getX();
			Header hResizing = m_header.getResizingHeader();
			if (hResizing != null) {
				UFOTable table = (UFOTable) m_header.getParent().getParent();
				CellsPane pane = table.getCells();
				//拖动过程中不改变行列的尺寸,而是利用XOR方式在标题部分进行绘制.
				Graphics g = m_header.getGraphics();
				g.setXORMode(TableStyle.RESIZING_COLOR);
				int width = m_header.getBounds().width - 1;
				int x = m_header.getBounds().x;
				int height = m_header.getBounds().height - 1;
				int y = m_header.getBounds().y;

				int startX = isRowHeader() ? x : lastDraw;
				int startY = isRowHeader() ? lastDraw : y;
				int areaWidth = isRowHeader() ? width : TableStyle.LINEDEEP;
				int areaHeight = isRowHeader() ? TableStyle.LINEDEEP : height;

				if (lastDraw >= 0) { //擦除上次绘制的内容
					g.fillRect(startX, startY, areaWidth, areaHeight);
					;
					pane.getUI().drawDraggingLine(lastDraw, isRowHeader());
				}
				lastDraw = mouseStart;
				startX = isRowHeader() ? x : lastDraw;
				startY = isRowHeader() ? lastDraw : y;
				g.fillRect(startX, startY, areaWidth, areaHeight);
				;
				pane.getUI().drawDraggingLine(lastDraw, isRowHeader());
				g.setPaintMode();
				/*
				 * hResizing.setSize(newSize); if
				 * (!header.getModel().isExist(resizePos)) {
				 * //如果是对于一个临时行列改变了属性，需要将这个临时行列的信息写入
				 * header.getModel().realizeHeader(resizePos, hResizing); }
				 */
			} else {
				SelectModel selModel = m_header.getModel().getSelModel();
				int type = m_header.getType();
				CellPosition anchorCell = selModel.getAnchorCell();
				Point p = e.getPoint();
				int index = m_header.indexAtPoint(p);
				int endIndex = type == Header.ROW ? anchorCell.getRow()
						: anchorCell.getColumn();
				int[] datas = new int[] { index, endIndex };
				if (index != -1 && m_header.getModel().intersection(datas)) {
					m_header.getModel().setSelectedHeader(index, endIndex);
					selModel.setSelectedHeader(datas, type);
					m_header.getModel().getOtherRowOrColHeaderModel()
							.setAllSelHeader(true);
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			 
//			Runnable run = new Runnable() {
//				public void run() {
					doRender();
//				}
//			};
//			try {
//				SwingUtilities.invokeLater(run);
//			} catch (Throwable e1) {
//				AppDebug.debug(e1);
//			}

		}

		private void doRender() {
			Header hResizing = m_header.getResizingHeader();
			if (hResizing != null) {
				//擦除最后一次绘制的背景，设置新的尺寸
				Graphics g = m_header.getGraphics();
				g.setXORMode(TableStyle.RESIZING_COLOR);
				if (isRowHeader()) {
					int width = m_header.getBounds().width;
					int x = m_header.getBounds().x;
					if (lastDraw >= 0) { //擦除上次绘制的内容
						g.fillRect(x, lastDraw, width, TableStyle.LINEDEEP);
					}
				} else {
					int height = m_header.getBounds().height;
					int y = m_header.getBounds().y;
					if (lastDraw >= 0) { //擦除上次绘制的内容
						g.fillRect(lastDraw, y, TableStyle.LINEDEEP, height);
					}
				}
				g.setPaintMode();
				UFOTable table = (UFOTable) m_header.getParent().getParent();
				CellsPane pane = table.getCells();
				pane.getUI().drawDraggingLine(lastDraw, isRowHeader());

				if (lastDraw != -1 && headerStart != -1) {//在列的关联位置点击鼠标引起的行列宽度设置的错误
					int size = (int) ((lastDraw - headerStart) / TablePane
							.getViewScale());
					/** 如果一组行列被选中，那么拖动结果是所有的选中行列具有相同的尺寸。 */
					SelectModel selModel = pane.getSelectionModel();
					int[] selHeader = null;
					HeaderModel hModel = null;
					if (isRowHeader()) {
						selHeader = selModel.getSelectedRow();
						hModel = pane.getDataModel().getRowHeaderModel();
					} else {
						selHeader = selModel.getSelectedCol();
						hModel = pane.getDataModel().getColumnHeaderModel();
					}
					if (selHeader != null) {
						//只有被拖动的行列也属于选中区域的时候，才有效。
						boolean contain = false;
						for (int i = 0; i < selHeader.length; i++) {
							if (dragIndex == selHeader[i]) {
								contain = true;
								break;
							}
						}
						if (contain) {
							for (int i = 0; i < selHeader.length; i++) {
								Header h = hModel.getHeader(selHeader[i]);
								h.setSize(size);
							}
						}
					}
					
					int oldSize = hResizing.getSize();
					hResizing.setSize(size);
					HeaderSizeUndo undo = new HeaderSizeUndo(hModel, dragIndex, oldSize, size);
				
					hModel.getCellsModel().fireUndoHappened(undo);
					
					//对有限表，需要修改CellsPanel的可视区域 liuyy+
					if(!pane.getDataModel().isInfinite()){
						pane.setBounds(0, 0, pane.getDataModel().getColumnHeaderModel().getTotalSize(), pane.getDataModel().getRowHeaderModel().getTotalSize());
					}

					PropertyChangeEvent evt = new PropertyChangeEvent(
							new Header[] { hResizing },
							Header.HEADER_SIZE_PROPERTY, null, dragIndex);
					m_header.getModel().fireHeaderPropertyChanged(evt);

				}
				lastDraw = -1;
			}
			setDraggedDistance(0, viewIndexForHeader(m_header
					.getDraggedHeader()));
			m_header.setResizingHeader(null);
			m_header.setDraggedHeader(null);
		
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		private void setDraggedDistance(int draggedDistance, int column) {
			m_header.setDraggedDistance(draggedDistance);
			if (column != -1) {
				//        header.getModel().moveHeader(column, column);
			}
		}
	}

}