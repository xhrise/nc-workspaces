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
 * Title: ���ͷ����չʾ�����UI����
 * </p>
 * <p>
 * Description: ������ͼ��Ļ��ƺ�����¼��Ĵ���
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: UFSOFT
 * </p>
 * 
 * @author wupeng
 * @version 1.0.0.1 2004��03��05 ���еĹ���λ�õ�������������п�����õĴ���
 */
public class TableHeaderUI extends ComponentUI {

	/**
	 * ��ͷ���϶���곣��
	 */
	private final static Cursor CURSOR_COLRESIZE = Cursor
			.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
	/**
	 * ��ͷ���϶���곣��
	 */
	private final static Cursor CURSOR_ROWRESIZE = Cursor
			.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
	/** ʹ�øô������� */
	private TableHeader m_header;
	/**
	 * �μ�JDK�еĽ��ͣ����Ĺ�Ч�������һ��ʹ��Render�����������Render֮�䣬
	 * ����repaint()��invalidate()����������������϶��ᷢ����
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
	 * ���������������
	 */
	private MouseInputListener mouseInputListener;

	//*******************************************************************//
	/**
	 * ���캯��
	 */
	public TableHeaderUI() {
		super();
	}

	//******************************���´��봦��UI�İ�װ�ͼ���������******************************************//

	/**
	 * ������������
	 */
	private MouseInputListener createMouseInputListener() {
		return new MouseInputHandler();
	}

	/**
	 * ����UI��
	 * 
	 * @param h
	 *            JComponent
	 * @return ComponentUI
	 */
	public static ComponentUI createUI(JComponent h) {
		return new TableHeaderUI();
	}

	/**
	 * ��װUI�����÷�����ʹ��UI���������
	 * 
	 * @param c
	 *            JComponent ��UI��ע��������
	 */
	public void installUI(JComponent c) {
		m_header = (TableHeader) c;
		rendererPane = new CellRendererPane();
		m_header.add(rendererPane);
		installListeners();
	}

	/**
	 * ж��UI�����÷�����ʹ��UI���������
	 * 
	 * @param c
	 *            JComponent ��UI��ע��������
	 */
	public void uninstallUI(JComponent c) {
		uninstallListeners();
		m_header.remove(rendererPane);
		rendererPane = null;
		m_header = null;
	}

	/**
	 * ��Ӽ�����
	 */
	private void installListeners() {
		mouseInputListener = createMouseInputListener();
		m_header.addMouseListener(mouseInputListener);
		m_header.addMouseMotionListener(mouseInputListener);
	}

	/**
	 * ж�ؼ�����
	 */
	private void uninstallListeners() {
		m_header.removeMouseListener(mouseInputListener);
		m_header.removeMouseMotionListener(mouseInputListener);
		mouseInputListener = null;
	}

	//***************************����Ϊ���ƴ���**************************//
	/**
	 * ���²���Ϊ���ƴ��룬�μ�����ע��
	 * 
	 * @param g
	 * @param c
	 */
	public void paint(Graphics g, JComponent c) {
		HeaderModel hm = m_header.getModel();
		int tempCount = hm.getTempCount();

		if (tempCount <= 0) { //û�л��Ƶ��С�
			return;
		}

		//�ֲ�������ʱ������
		//		Integer[] indexs = m_header.getChangedIndexs();
		//		if(indexs != null){
		//			for(Integer index: indexs){
		//				paintCell(g,  index);
		//			}
		////			rendererPane.removeAll();
		//			return;
		//		}

		Rectangle clip = g.getClipBounds();
		//�õ���������������յ�
		Point start = clip.getLocation();
		Point end = isRowHeader() ? new Point(clip.x, clip.y + clip.height - 1)
				: new Point(clip.x + clip.width - 1, clip.y);

		//�õ���ǰ�������ɵ����з�Χ.�������ֵΪ��������ʾԽ��
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

		//�����������еĵ�Ԫ
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
	 * �õ�ĳ�����б������Ⱦ��
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
			
			
			//  ���ñ߿�
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
		 
		
//		// ����ê����
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
	 * ������ͷ��ͷѡ�б��״̬
	 * @param h ��ͷ/��ͷ
	 * @param index ��ͷ/��ͷ���
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
	 * ���Ʊ�ͷ��Ԫ
	 * @param g
	 * @param cellRect
	 * @param index
	 */
	private void paintCell(Graphics g, int index) {
		Rectangle cellRect = m_header.getHeaderRect(index);
		Component component = getHeaderRenderer(index);
		rendererPane.paintComponent(g, component, m_header, cellRect.x,
				cellRect.y, cellRect.width, cellRect.height, false);// //liyyy�� ������Ϊfalse������ʱ�������У�顣
	}

	/**
	 * �б�ͷ����������ȡ��λ�����
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
	 * ����ͷ������Ⱦ�������ͷ��������һ��γ�ȵĳߴ硣���磬��ͷ���ĸ߶����û����ã��������Ŀ�� ������Ⱦ�����Ƶġ�
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
	 * �õ���ǰ�������С�ĳߴ�.
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
	 * ���������ȱʡ�ߴ�
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
	 * ����������ߴ�
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
	 * �Ƿ�����ģ��
	 */
	private boolean isRowHeader() {
		return m_header.getType() == Header.ROW;
	}

	/**
	 * ���еĸ߿��Ƿ��������
	 */
	private boolean isResizable() {
		return m_header.getResizingAllowed();
	}

	/**
	 * ����ѡ������ʱ��ȱʡѡ�еĵ�Ԫ�񣬼�������Ԫ�����ǰ�������ϵ�Ԫ�������²���
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

	//*******************���´���������������ʵ��***************************//
	/**
	 * �ڲ���ʵ����������
	 */
	private class MouseInputHandler implements MouseInputListener {
		/**
		 * ��ק��Ԫ����ʼ��
		 */
		private int headerStart;
		/**
		 * ��¼�϶������в���XORģʽ����ʱ,�ϴλ��Ƶ�λ��.
		 */
		private int lastDraw = -1;
		/** ��¼�ƶ����е�����λ�� */
		private int dragIndex = 0;

		private Cursor otherCursor = isRowHeader() ? CURSOR_ROWRESIZE
				: CURSOR_COLRESIZE;

		/**
		 * �������Ҫ�Ǽ������е�ѡ��,���Զ������и��п�.
		 */
		public void mouseClicked(MouseEvent e) {
		 // �����ڵ���ͷ����С������ʱ��������ѡ���¼�
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
			
			//liuyy+, 2008-09-12 ���Ѿ�ѡ�еĶ��е���Ҽ���ֱ����ʾ�Ҽ���
			if(SwingUtilities.isRightMouseButton(e) && model.isSelected(index)){
				return;
			}

			if (e.isControlDown()) { //Ctrlֻ�����
				//����ê��
				if (type == Header.ROW) {
					selModel.setAnchorCell(index, 0);
				} else {
					selModel.setAnchorCell(0, index);
				}
				model.setSelectedHeader(index, index);
				selModel.setSelectedHeader(new int[] { index, index }, type);
			} else if (e.isShiftDown()) { //��Ҫɾ���ϴ�Shift��ӵ�����
				CellPosition anchorCell = selModel.getAnchorCell();
				int anchorIndex = type == Header.ROW ? anchorCell.getRow()
						: anchorCell.getColumn();
				model.setSelectedHeader(index, anchorIndex);
				selModel.setSelectedHeader(new int[] { index, anchorIndex },
						type);
			} else {
				//��굥��ʱ�������µ�ѡ��ͷ,�����֮ǰ����ê���ѡ�б��,˳���ܵߵ�
				//				model.clearAnchors();
				//				model.getOtherRowOrColHeaderModel().clearAnchors();

				//����ê��
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

			//�����һ������ʱ�������ͷ��ͼβ��ʱ����Ϊ��Ҫ�ı����һ�С�
			//���һ������ʱ��Ҫ��ı䵹���ڶ��еĳߴ磬��Ҫ�Ƚ����һ���ϳ����ɷ�����״̬��
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
			 
			 //wangyga+ ʹ�����ý���
			 if(m_header.isRequestFocusEnabled()){
				 m_header.requestFocus();
			 }
			 
			 //@edit by liuyy at 2008-12-26 ����11:28:20 �������ޱ������
			 HeaderModel headerModel = m_header.getModel();
			if(!headerModel.getCellsModel().isInfinite() && index >= headerModel.getCount()){
				return;
			 }
				 
			// The last 3 pixels + 3 pixels of next column are for resizing
			dragIndex = getResizingIndex(p, index);
			if (dragIndex != -1) { //�˴������϶��ı��и��п�Ĵ��롣
				Header hResizing = headerModel
						.initHeader(dragIndex);
				if (isResizable() && hResizing != null) {
					m_header.setResizingHeader(hResizing);
					headerStart = (isRowHeader() ? p.y : p.x)
							- (int) (hResizing.getSize() * TablePane
									.getViewScale());
				}
			} else { //�˴������϶�ѡ��Ĵ���
				mouseClicked(e);
			}
			 
		}

		/**
		 * ת���϶�״̬������״̬�Ĺ��
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
		 * ��������϶������������и��п�ĸı䣬�Լ���������ѡ��
		 * 
		 * @param e
		 */
		public void mouseDragged(MouseEvent e) {
			int mouseStart = isRowHeader() ? e.getY() : e.getX();
			Header hResizing = m_header.getResizingHeader();
			if (hResizing != null) {
				UFOTable table = (UFOTable) m_header.getParent().getParent();
				CellsPane pane = table.getCells();
				//�϶������в��ı����еĳߴ�,��������XOR��ʽ�ڱ��ⲿ�ֽ��л���.
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

				if (lastDraw >= 0) { //�����ϴλ��Ƶ�����
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
				 * //����Ƕ���һ����ʱ���иı������ԣ���Ҫ�������ʱ���е���Ϣд��
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
				//�������һ�λ��Ƶı����������µĳߴ�
				Graphics g = m_header.getGraphics();
				g.setXORMode(TableStyle.RESIZING_COLOR);
				if (isRowHeader()) {
					int width = m_header.getBounds().width;
					int x = m_header.getBounds().x;
					if (lastDraw >= 0) { //�����ϴλ��Ƶ�����
						g.fillRect(x, lastDraw, width, TableStyle.LINEDEEP);
					}
				} else {
					int height = m_header.getBounds().height;
					int y = m_header.getBounds().y;
					if (lastDraw >= 0) { //�����ϴλ��Ƶ�����
						g.fillRect(lastDraw, y, TableStyle.LINEDEEP, height);
					}
				}
				g.setPaintMode();
				UFOTable table = (UFOTable) m_header.getParent().getParent();
				CellsPane pane = table.getCells();
				pane.getUI().drawDraggingLine(lastDraw, isRowHeader());

				if (lastDraw != -1 && headerStart != -1) {//���еĹ���λ�õ�������������п�����õĴ���
					int size = (int) ((lastDraw - headerStart) / TablePane
							.getViewScale());
					/** ���һ�����б�ѡ�У���ô�϶���������е�ѡ�����о�����ͬ�ĳߴ硣 */
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
						//ֻ�б��϶�������Ҳ����ѡ�������ʱ�򣬲���Ч��
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
					
					//�����ޱ���Ҫ�޸�CellsPanel�Ŀ������� liuyy+
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