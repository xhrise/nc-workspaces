package com.ufsoft.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.print.Printable;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.CellEditor;
import javax.swing.CellRendererPane;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.table.format.DefaultFormatValue;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.LineFactory;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.header.Header;
import com.ufsoft.table.header.HeaderModel;
import com.ufsoft.table.re.BorderPlayRender;
import com.ufsoft.table.re.CellRenderAndEditor;

/**
 * 
 * <p>
 * Title: ��������UI����
 * </p>
 * <p>
 * Description: �����񲿷ֵĻ��ƺ��¼���Ӧ
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: UFSOFT
 * </p>
 * 
 * @author wupeng
 * @version 1.0.0.1</br> 2004-04-01 ����������������ΪLine.L_NULLʱ��������Ȼ���ƵĴ���</br>
 *          2004-12-6 ��Ӷ���͸��������������ʾ�Ĺ��ܡ�
 *  
 */

public class CellsPaneUI extends ComponentUI {
	/** ������������ */
	private CellsPane m_cellsPane;
	/** ʵ�ʻ��Ƶ���� */
	private CellRendererPane m_rendererPane;
	/** ������ */
	private FocusListener focusListener;
 
	/** ���Ƹ��ֱ��ߵ����������ȡ� */
	int MaxLineDeep = 5;
//
//	boolean doneFullScreenDraw = false;
	
	/**��������*/
	CellsPaneUIMouseActionHandler mouseActionHandler = null;
	//�������������������������������������·�����������������.��������������������������������������������
	/**
	 * ����CellsPaneUI�Ľ����¼�������
	 */
	private FocusListener createFocusListener() {
		return new FocusHandler();
	}
   /**
    * ����CellsPaneUI������ƶ��¼�������
    * @return
    */
	public CellsPaneUIMouseActionHandler createMouseInputListener() {
	    if(mouseActionHandler == null) {
	        mouseActionHandler = new CellsPaneUIMouseActionHandler(this.m_cellsPane);
	    }
		return mouseActionHandler;
	}
	/**
	 * ����CellsPaneUI�µ��������������Ƴ��ϵļ�����
	 * @param newMouseActionHandler
	 */
	public void setCellsPaneUIMouseActionHandler(CellsPaneUIMouseActionHandler newMouseActionHandler) {
	    if(newMouseActionHandler == null) {
	    	return;
	    }
	    
        m_cellsPane.removeMouseListener(this.mouseActionHandler);
		m_cellsPane.removeMouseMotionListener(this.mouseActionHandler);
//			table.removeMouseWheelListener(mouseInputListener);
		
		m_cellsPane.addMouseMotionListener(newMouseActionHandler);
		m_cellsPane.addMouseListener(newMouseActionHandler);
//			table.addMouseWheelListener(newMouseActionHandler);
        this.mouseActionHandler = newMouseActionHandler;
	    
	}
	//���������������������������������������������������� ��װ��ж��UI������������������������������������������������
	/**
	 * ����UI����
	 */
	public static ComponentUI createUI(JComponent c) {
		return new CellsPaneUI();
	}
    /**
     * ������ ComponentUI �е� installUI
     * ��ʼ��CellsPane��ʵ�ʻ��Ƶ����CellRendererPane����˳����� installDefaults()��installListeners() �� installKeyboardActions() ����ʼ��CellsPaneUI��Ӧ��CellsPane
     */
	public void installUI(JComponent c) {
		m_cellsPane = (CellsPane) c;
		m_rendererPane = new CellRendererPane();
		m_cellsPane.add(m_rendererPane);
		installDefaults();
		installListeners();
		installKeyboardActions();
	}

	/**
	 * ���õ�ǰUI��һЩȱʡ�����ԡ�
	 */
	protected void installDefaults() {
		m_cellsPane.setBackground(Color.white);
		m_cellsPane.setForeground(Color.black);
//		table.setFont(Font.getFont("Dialog"));
		m_cellsPane.setGridColor(TableStyle.GRID_COLOR);
	}

	/**
	 * ��Ӽ�����.
	 */
	protected void installListeners() {
		focusListener = createFocusListener();
		m_cellsPane.addFocusListener(focusListener);

        setCellsPaneUIMouseActionHandler(createMouseInputListener());
		
	}

	/**
	 * ע����̶���
	 */
	protected void installKeyboardActions() {
		SwingUtilities.replaceUIActionMap(m_cellsPane, createActionMap());
		//������JTAble�ж���ļ����¼�.
		InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		SwingUtilities.replaceUIInputMap(m_cellsPane,
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inputMap);
	}

	/**
	 * ����һ��ActionMap.Ϊ�˼����,����JTable�Ѿ�����Ķ�������.ע��:��Щ���ƵĶ���
	 * ����UI��������,���ϣ���޸�,��Ҫ�޸���Ӧ��getInputMap�ķ���. Ŀǰ֧�ֵļ��̶�������
	 * <ol>
	 * <li>�������Ҽ�:��Ӧ��ǰѡ�񽹵�ı任
	 * </ol>
	 * 
	 * @return ActionMap
	 */
	private ActionMap createActionMap() {
		ActionMap map = new ActionMap();
		//�������Ҽ���֧��
		map.put("selectNextColumn", new NavigationalAction(1, 0));
		map.put("selectPreviousColumn", new NavigationalAction(-1, 0));
		map.put("selectNextRow", new NavigationalAction(0, 1));
		map.put("selectPreviousRow", new NavigationalAction(0, -1));

		//Pageup,Pagedown�Ĵ���
		map.put("Pageup", new PagingAction(false));
		map.put("Pagedown", new PagingAction(true));
		//�س������ƶ�
		map.put("nextCell", new NavigationalAction(0, 1));

		//Home,End,Ctrl+Home,Ctrl+End
		map.put("Home", new HomeEndAction(false, true));
		map.put("End", new HomeEndAction(false, false));
		map.put("CtrlHome", new HomeEndAction(true, true));
		map.put("CtrlEnd", new HomeEndAction(true, false));

		//��ĳ��ѡ�е�Ԫ������ĸ��������,��ʼ�༭.

		//����ȫѡ,ȡ����Ԫ�༭,��Ԫ�༭
		map.put("selectAll", new SelectAllAction());
		map.put("cancel", new CancelEditingAction());
		map.put("startEditing", new StartEditingAction());
		
		map.put("objectView", new ObjectViewAction());

//		//������,����,ճ��
//		map.put("copy", new TransferAction("copy"));
//		map.put("cut", new TransferAction("cut"));
//		map.put("paste", new TransferAction("paste"));
//		map.put("delete", new TransferAction("delete"));
		return map;
	}

	/**
	 * ��������ʱ�¼���Ӧ֮��Ĺ�ϵ.
	 * 
	 * @param condition
	 *            int
	 * @return InputMap
	 */
	private InputMap getInputMap(int condition) {
		if (condition == JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT) {
			InputMap keyMap = new InputMap();
			//�������������
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
					"selectPreviousColumn");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
					"selectNextColumn");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
					"selectPreviousRow");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
					"selectNextRow");
			//�س����ƶ�����һ����Ԫ���س���һ��Ҫ�Ӳ���true.��Ϊ�༭״̬ʱ,��press�¼����༭������.
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),//, true),
							"nextCell");
			//Esc��Ctrl��A��F2
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
			keyMap.put(KeyStroke
					.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK),
					"selectAll");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0),
					"startEditing");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), "Home");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), "End");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME,
					KeyEvent.CTRL_MASK), "CtrlHome");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_END,
					KeyEvent.CTRL_MASK), "CtrlEnd");
			keyMap
					.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0),
							"Pageup");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0),
					"Pagedown");
 
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK), "objectView");
			
//			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK), "copy");
//			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK), "cut");
//			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK), "paste");
//			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");

			return keyMap;
		}
		return null;
	}

	/**
	 * �������ʵ�ʳߴ硣������ֻ������ĸ߶ȡ�
	 * 
	 * @param width
	 *            ���Ŀ�ȡ�
	 * @return Dimension
	 */
	private Dimension createTableSize(long width) {
		int height = 0;
		int rowCount = m_cellsPane.getRowCount();
		if (rowCount > 0 && m_cellsPane.getColumnCount() > 0) {
			Rectangle r = m_cellsPane.getCellRect(CellPosition.getInstance(rowCount - 1, 0), true);
			height = r.y + r.height;
		}
		return new Dimension((int) width, height);
	}

	/**
	 * �õ���ǰ�������С�߶ȡ����ظ��෽��
	 * 
	 * @param c
	 * @return Dimension
	 */
	public Dimension getMinimumSize(JComponent c) {
		int min = 0;
		Header[] headers = getColModel().getHeaders();
		for(Header h: headers){
			if (h != null && h.isVisible()) {
				min += h.getMinSize();
			}
		}
//		while (enumeration.hasMoreElements()) {
//			Header h = (Header) enumeration.nextElement();
//			if (h != null && h.isVisible()) {
//				min += h.getMinSize();
//			}
//		}
		return createTableSize(min);
	}

	/**
	 * �õ������ȱʡ�ߴ硣���ظ��෽��
	 * 
	 * @param c
	 * @return Dimension
	 */
	public Dimension getPreferredSize(JComponent c) {
		long size = getColModel().getPosition(getColModel().getCount());
//		Enumeration enumeration = getColModel().getHeaders();
//		while (enumeration.hasMoreElements()) {
//			Header h = (Header) enumeration.nextElement();
//			if (h != null && h.isVisible())
//				size = size + h.getSize();
//		}
		return createTableSize(size);
	}

	/**
	 * �õ����ߴ硣���ظ��෽��
	 * 
	 * @param c
	 * @return Dimension
	 */
	public Dimension getMaximumSize(JComponent c) {
		 
		long max = getColModel().getPosition(getColModel().getCount());
		
//		Enumeration enumeration = getColModel().getHeaders();
//		while (enumeration.hasMoreElements()) {
//			Header h = (Header) enumeration.nextElement();
//			if (h != null && h.isVisible()) {
//				max = max + h.getMaxSize();
//			}
//		}
		return createTableSize(max);
	}

	//************************************************************
	//****************** ����Ϊ���Ʊ��Ĵ��� ****************
	//************************************************************
	/**
	 * ʵ�ִ�ӡ���õĻ��ƽӿ�.�����������ӡԤ���ʹ�ӡ��ͬ����.
	 * 
	 * @param g
	 *            Graphics
	 * @param rect
	 *            Rectangle ����ϱ��λ�������
	 * @param xscale
	 *            double ��ӡ����Ԥ����ͼ��Ҫx�������ŵı���
	 * @param yscale
	 *            double ��ӡ����Ԥ����ͼ��Ҫy�������ŵı���
	 * @return int
	 * modify by guogang 2007-9-25 �����˴�ӡԽ�������
	 */
	public int print(Graphics g, Rectangle rect, double xscale,double yscale) {
		//�����ӡ����
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform transform = g2d.getTransform();
		
//		double scalex=transform.getScaleX();
//		double scaley=transform.getScaleY();
//		int linex=(int)(1*scalex);
//		int liney=(int)(1*scaley);
//		
		transform.setToScale(xscale, yscale);
		g2d.transform(transform);
		//���ô�ӡ����
		g2d.translate(-rect.x, -rect.y);
		
		//��������
		
		Point upperLeft = rect.getLocation();
		//Ϊ�˻�ȡ��ȷ�Ľ�������,��-1
		Point lowerRight = new Point(rect.x + rect.width-1, rect.y
				+ rect.height - 1);
		
		int rMin = m_cellsPane.rowAtPoint(upperLeft);
		int rMax = m_cellsPane.rowAtPoint(lowerRight);
		int cMin = m_cellsPane.columnAtPoint(upperLeft);
		int cMax = m_cellsPane.columnAtPoint(lowerRight);
		// ���Խ�磬������β����
		if (rMin == -1) {
			rMin = 0;
		}
		if (rMax == -1) {
			rMax = m_cellsPane.getRowCount() - 1;
		}
		if (cMin == -1) {
			cMin = 0;
		}
		if (cMax == -1) {
			cMax = m_cellsPane.getColumnCount() - 1;
		}
		
        //ȷ��Ҫ��ӡ�ĵ�Ԫ�񣨰������ߣ��ڼ���������,ʹ��x-1,x-1��Ϊ��ȡ��������width+1
		//modify by guogang 2008-4-9 ������б��ߵ��߿�>1�Ļ�����������1�����ǲ�����
//		Rectangle newR = new Rectangle(rect.x-linex , rect.y-liney, rect.width + linex,
//				rect.height + liney);
		Rectangle newR = new Rectangle(rect.x-1 , rect.y-1, rect.width + 1,
				rect.height + 1);
		g.setClip(newR);

//		g2d.setClip(rect);
		
		draw(g2d, true,rMin,rMax,cMin,cMax);
		g2d.dispose();
		return Printable.PAGE_EXISTS;
	}

	/**
	 * ���Ʊ��
	 * 
	 * @param g
	 *            Graphics
	 * @param cc
	 *            JComponent
	 */
	public void paint(Graphics g, JComponent cc) {
		// @edit by wangyga at 2009-2-24,����07:46:31
		if(getCellsModel() == null){
			return;
		}
		//��������
		Rectangle clip = g.getClipBounds();
		Point upperLeft = clip.getLocation();
		Point lowerRight = new Point(clip.x + clip.width, clip.y
				+ clip.height );
		int rMin = m_cellsPane.rowAtPoint(upperLeft);
		int rMax = m_cellsPane.rowAtPoint(lowerRight);
		int cMin = m_cellsPane.columnAtPoint(upperLeft);
		int cMax = m_cellsPane.columnAtPoint(lowerRight);
		// ���Խ�磬������β����
		if (rMin == -1) {
			rMin = 0;
		}
		if (rMax == -1) {
			rMax = m_cellsPane.getRowCount() - 1;
		}
		if (cMin == -1) {
			cMin = 0;
		}
		if (cMax == -1) {
			cMax = m_cellsPane.getColumnCount() - 1;
		}
		draw(g, false,rMin,rMax,cMin,cMax);
	}

	/**
	 * ��ӡ����Ļ���ƹ�ͬ���õĴ���
	 * 
	 * @param g
	 *            Graphics ����ͼ��
	 * @param isPrint
	 *            boolean �Ƿ����ڴ�ӡ������Ǵ�ӡ����ô�����������ߺ�����ѡ�� Anchor�ڵ�Ԫ����ӵ�ͼ��
	 *  modify by guogang �����˴�ӡԽ�������
	 */
	private void draw(Graphics g, boolean isPrint,int rMin, int rMax, int cMin, int cMax) {
		// ���ޱ�
		if (!getCellsModel().isInfinite()
				&& (m_cellsPane.getRowCount() <= 0 || m_cellsPane
						.getColumnCount() <= 0)) {
			return;
		}
		
//		AreaPosition area = AreaPosition.getInstance(CellPosition.getInstance(
//				rMin, cMin), CellPosition.getInstance(rMax, cMax));
		 
		
		// ����������.
		if (!isPrint) {
			paintGrid(g, rMin, rMax, cMin, cMax);
		}
		
		// ���Ƶ�Ԫ.
		paintCells(g, rMin, rMax, cMin, cMax, isPrint);
		 
//		//���ƽ��㵥Ԫ��Ч��.
//		if (!isPrint) {
//			paintAnchor(g);
//		}
		
		//���»��Ʊ༭��Ԫ
		if (m_cellsPane.isEditing()) {
			Component component = m_cellsPane.getTablePane().getEditorComp();
			component.repaint();
			component.validate();
		}
		
		//���ƺ�ɾ��������Ⱦ��
		m_rendererPane.removeAll();
		
		//���ƴ�ӡ�ķָ�����
		drawPrintLine(g,rMin,rMax,cMin,cMax,isPrint);
		 
	}

	/**
	 * ����������
	 * @param g ͼ��������
	 * @param rMin �������������к�
	 * @param rMax ���������յ���к�
	 * @param cMin �������������к�
	 * @param cMax ���������յ���к�
	 */
	private void paintGrid(Graphics g, int rMin, int rMax, int cMin, int cMax) {
 
		if(!m_cellsPane.isShowRowHeader() && !m_cellsPane.isShowColHeader()){
			return;
		}
		
		g.setColor(m_cellsPane.getGridColor());
		//    Rectangle minCell = table.getCellRect(rMin, cMin,
		// true);//�������������ϵ�Ԫʱ����ȡ����ϵ�Ԫ�ľ��Σ���˳���
		//    Rectangle maxCell = table.getCellRect(rMax, cMax, true);
		//    Rectangle damagedArea = minCell.union(maxCell);
		Rectangle damagedArea = new Rectangle();
		damagedArea.x = getColModel().getPosition(cMin);
		damagedArea.y = getRowModel().getPosition(rMin);
		damagedArea.width = getColModel().getPosition(cMax) + getColModel().getSize(cMax)
				- damagedArea.x;
		damagedArea.height = getRowModel().getPosition(rMax)
				+ getRowModel().getSize(rMax) - damagedArea.y;

		if(m_cellsPane.isShowRowHeader()){
			//����ˮƽ��
			int tableWidth = damagedArea.x + damagedArea.width;
			int y = damagedArea.y;
			for (int row = rMin; row <= rMax; row++) {
				y += m_cellsPane.getRowHeight(row);
				g.drawLine(damagedArea.x, y - 1, tableWidth - 1, y - 1);
			}
		}
		
		if(m_cellsPane.isShowColHeader()){
			//���ƴ�ֱ��
			int tableHeight = damagedArea.y + damagedArea.height;
			int x;
			x = damagedArea.x;
			for (int column = cMin; column <= cMax; column++) {
				int w = m_cellsPane.getColumnWidth(column);
				x += w;
				g.drawLine(x - 1, 0, x - 1, tableHeight - 1);
			}
		}
	}

	/**
	 * �������еĵ�Ԫ.���ȹ���ÿ����Ԫ��Ӧ�ĵ�Ԫ�����Ȼ���鵥Ԫ�Ƿ��б��ߣ��б�����Ҫ������ǰ���� ��Grid��
	 * @param g    ͼ��������
	 * @param rMin �������������к�
	 * @param rMax ���������յ���к�
	 * @param cMin �������������к�
	 * @param cMax ���������յ���к� 
	 * @param isPrint
	 *            �Ƿ��ǻ��ƴ�ӡЧ��
	 */
	private void paintCells(Graphics g, int rMin, int rMax, int cMin, int cMax,
			boolean isPrint) {
	
		Rectangle cellRect; //���Ʒ�Χ
		
		//���Ƹ���Ԫ
//		AppDebug.debug("��ʼ����:("+rMin+","+cMin+")-->("+rMax+","+cMax+")");
		
//		if(rMin == 0 && cMin == 0 && rMax > 30 && cMax > 10){
			//AppDebug.debug("ȫ������:("+rMin+","+cMin+")-->("+rMax+","+cMax+")");  //�ر�
//			AppDebug.debug(Thread.currentThread().getStackTrace());
//		}
		
		for (int row = rMin; row <= rMax; row++) {
			for (int column = cMin; column <= cMax; column++) {
				CellPosition pos = CellPosition.getInstance(row, column);
				CombinedCell cc = getCellsModel().getCombinedAreaModel().belongToCombinedCell(pos);
				//�ϲ��������������Ƶ�ʱ�������û��ƺϲ�����ĵ�Ԫ��liuyy. 2008-08-25
				if(cc != null){// && !pos.equals(cc.getArea().getStart())){
					continue;
				}
				cellRect = m_cellsPane.getCellRect(pos, false);
				paintCell(g, cellRect, row, column, isPrint);
				  
			}
		}
		
		/**
		 * �õ���ǰ��Ҫ���Ƶ����򡣾������������λ��Ϊ���������ܰ�����������ϵ�Ԫ��
		 * ע�⣺������ϵ�Ԫ��������С��Ԫ֮�⣬��������ĵ�Ԫ�ᵽ���������
		 */
		IArea validArea = AreaPosition.getInstance(CellPosition.getInstance(rMin, cMin), CellPosition.getInstance(rMax, cMax));
		ArrayList<AreaCell> areaCells = getCellsModel().getAreaCell(validArea);//rMax, cMax);
		ArrayList<AreaCell>[] filter = filterCells(areaCells);
		ArrayList<AreaCell> opaque = filter[0];
		ArrayList<AreaCell> disopaque = filter[1];
		
		//��������
		drawArea(g, rMin, rMax, cMin, cMax, isPrint, opaque,true);
		
		//��ʼ����͸��������
		drawArea(g, rMin, rMax, cMin, cMax, isPrint, disopaque,false);
		
		
		//Ϊ�����Ԫ��ֱ������ٽ���Ԫ�񱳾����Ƹ������⣬�Ȼ������е�Ԫ���ٻ��Ƶ�Ԫ����ߣ�����~~``  liuyy. 2007-04-25
		//modify by guogang 2008-4-9 Ϊ�����ϵ�Ԫ�����ٽ����ߵ�����,�Ȼ��������ٻ��Ƶ�Ԫ�����
		CellPosition cellPos=null;
		for (int row = rMin; row <= rMax; row++) {
			for (int column = cMin; column <= cMax; column++) {
				cellPos=CellPosition.getInstance(row, column);
				Format f = getCellsModel().getRealFormat(cellPos);
				
				if (f != null) { 
					cellRect = m_cellsPane.getCellRect(cellPos, false);
					paintCellLine(g, f, cellRect, true, true);
				}
				
			}
		}

	}
	/**
	 * ���������϶����ж�������������
	 * 
	 * @param pos
	 *            int �϶������λ��
	 * @param isRowHeader
	 *            boolean �Ƿ����϶���
	 */
	public void drawDraggingLine(int pos, boolean isRowHeader) {

		Graphics g = m_cellsPane.getGraphics();
		g.setXORMode(TableStyle.RESIZING_COLOR);

		int width = m_cellsPane.getBounds().width - 1;
		int x = m_cellsPane.getBounds().x;
		int height = m_cellsPane.getBounds().height - 1;
		int y = m_cellsPane.getBounds().y;

		int startX = isRowHeader ? x : pos;
		int startY = isRowHeader ? pos : y;
		int areaWidth = isRowHeader ? width : TableStyle.LINEDEEP;
		int areaHeight = isRowHeader ? TableStyle.LINEDEEP : height;

		g.fillRect(startX, startY, areaWidth, areaHeight);
		g.setPaintMode();
	}

	/**
	 * ���Ʊ����ҳ����������ֽ�������
	 * @param g    ͼ��������
	 * @param rMin �������������к�
	 * @param rMax ���������յ���к�
	 * @param cMin �������������к�
	 * @param cMax ���������յ���к� 
	 * @param isPrint �Ƿ��ǻ��ƴ�ӡЧ��
	 */
	private void drawPrintLine(Graphics g, int rMin, int rMax, int cMin, int cMax, boolean isPrint) {
	    if(isPrint) return;
		int[] rowPages = getCellsModel().getPages(Header.ROW);
		int[] colPages = getCellsModel().getPages(Header.COLUMN);
		int line = TableConstant.L_DASHDOT;
		Color color = Color.RED;
		//draw row print line
		for(int i=0;i<rowPages.length;i++){
		    if(rowPages[i] >= rMin && rowPages[i] <= rMax +1){
		        int x1 = getColModel().getPosition(cMin);
		        int y1 = getRowModel().getPosition(rowPages[i]);
		        int x2 = getColModel().getPosition(cMax+1);
		        int y2 = y1;
		        draw2DLine((Graphics2D)g,line,color,x1,y1,x2,y2,Format.LEFTLINE);
		    }
		}
		//draw col print line.
		for(int i=0;i<colPages.length;i++){
		    if(colPages[i] >= cMin && colPages[i] <= cMax+1){
		        int x1 = getColModel().getPosition(colPages[i]);
		        int y1 = getRowModel().getPosition(rMin);
		        int x2 = x1;
		        int y2 = getRowModel().getPosition(rMax+1);
		        draw2DLine((Graphics2D)g,line,color,x1,y1,x2,y2,Format.TOPLINE);
		    }
		}
	}
	
	/**
	 * ��������Ч��
	 * @param g    ͼ��������
	 * @param rMin �������������к�
	 * @param rMax ���������յ���к�
	 * @param cMin �������������к�
	 * @param cMax ���������յ���к� 
	 * @param isPrint �Ƿ��ǻ��ƴ�ӡЧ��
	 * @param opaque ��Ԫ��������
	 * @param isOpaque ��Ԫ�����еĵ�Ԫ���������Ƿ��ǲ�͸���ģ����򲻻��ƣ���������Ҫ����
	 */
	private void drawArea(Graphics g, int rMin, int rMax, int cMin, int cMax,
			boolean isPrint, ArrayList<AreaCell> opaque,boolean isOpaque) {
		Rectangle cellRect;
		//��ʼ��������
		if (opaque == null) {
			return;
		}
		 
		AreaPosition range = AreaPosition.getInstance(
				CellPosition.getInstance(rMin, cMin),
				CellPosition.getInstance(rMax, cMax));
		
		for (AreaCell cCell: opaque) {
				AreaPosition aCell = cCell.getArea();
				if (range.intersection(aCell)) {
					cellRect = m_cellsPane.getCellRect(aCell, false);
					paintArea(g, cellRect, cCell, isPrint,isOpaque);
				}
			 
		}
		
	}

	/**
	 * ���˶�������Ԫ�����ոõ�Ԫ��Ӧ�Ļ������Ƿ�͸����Ϊ�������顣ǰ��Ϊ��͸����Ⱦ��������Ϊ ͸����Ⱦ����
	 * 
	 * @param list
	 *            Ҫ���˵Ķ�������Ԫ
	 * @return ArrayList[] �������飬ǰ��Ϊ��͸����Ⱦ��������Ϊ ͸����Ⱦ����
	 */
	private ArrayList<AreaCell>[] filterCells(ArrayList<AreaCell> list) {
		ArrayList<AreaCell>[] result = new ArrayList[2];
		if (list == null) {
			return result;
		}

		ArrayList<AreaCell> opaque = new ArrayList<AreaCell>();
		ArrayList<AreaCell> disopaque = new ArrayList<AreaCell>();
		result[0] = opaque;
		result[1] = disopaque;
		// CellRenderAndEditor re = table.getReanderAndEditor();
		for (AreaCell cell : list) {
			if (cell.isOpaque()) {
				opaque.add(cell);
			} else {
				disopaque.add(cell);
			}
			// value = cell == null ? null : cell.getValue();
			// if (value == null) {
			// opaque.add(cell);
			// } else {
			// //ע�⣬���������޷������Ⱦ���ĵ�Ԫ����Ϊ�Ƿ�͸����Ԫ��������ķ�����Ԫ���ᱻ���ơ�
			// //���ԣ����⽫��ֵ��͸������Ԫ��������ģ���С�
			// Component cmp = re.getRender(value==null?null:value.getClass())
			// .getCellRendererComponent(null, null, false, false,
			// 0, 0);
			// boolean opq = cmp == null ? true : cmp.isOpaque();
			// if (opq) {
			// opaque.add(cell);
			// } else {
			// disopaque.add(cell);
			// }
			// }
		}

		return result;
	}
	

	//ѡ��������Ƹ�ʽ
	private static final IufoFormat SELECTED_FORMAT = new IufoFormat(); 
	private static final IufoFormat SELECTED_FORMAT2 = new IufoFormat(); 
	static {
		for(int i = 0; i < 6; i++){
			SELECTED_FORMAT.setLineColor(i, Color.BLACK);
			SELECTED_FORMAT.setLineType(i, TableConstant.L_SOLID2);
			SELECTED_FORMAT2.setLineColor(i, Color.WHITE);
			SELECTED_FORMAT2.setLineType(i, TableConstant.L_SOLID1);
		}
	};
	
	
	/**
	 * ����ê�㡣
	 * @deprecated
	 */
	private void paintAnchor(Graphics g) {
		CellPosition cellPos = getCellsModel().getSelectModel()
				.getAnchorCell();
		if (cellPos == null) {
			return;
		}
		
		//�������Ԫ���ڲ��������һ����ɫ�Ľ��ޱ�ʾ��һ��Anchor��Ԫ.
//		 AreaPosition area = m_cellsPane.getDataModel().getCombinedCellArea(cellPos);
		
		 Rectangle rect = m_cellsPane.getCellRect(getCellsModel().getSelectModel().getSelectedArea(), false);
		 
		//����б���,������ɫΪ���ߵ���ɫ���ȸı�,��������Ϊ��ɫ�ı߿�.
		if (rect == null || rect.height <= 0 || rect.width <= 0) {
			return;
		}
		rect.x += 1;
		rect.y += 1;
		rect.width -= 2;
		rect.height -= 2;
//		g.setColor(Color.BLACK); 
//		g.drawRect(rect.x, rect.y, rect.width, rect.height);
		 
		paintCellLine(g, SELECTED_FORMAT, rect, true, true);
		
		rect.x += 1;
		rect.y += 1;
		rect.width -= 3;
		rect.height -= 3;
		paintCellLine(g, SELECTED_FORMAT2, rect, true, true);
		
//		
//		Format format = getCellsModel().getRealFormat(cellPos);
//		int topLine = format == null ? 0 : format
//				.getLineType(Format.TOPLINE);
//		int bottomLine = format == null ? 0 : format
//				.getLineType(Format.BOTTOMLINE);
//		int leftLine = format == null ? 0 : format
//				.getLineType(Format.LEFTLINE);
//		int rightLine = format == null ? 0 : format
//				.getLineType(Format.RIGHTLINE);
//		Graphics2D g2 = (Graphics2D) g;
//		int x1 = rect.x , x2 = rect.x + rect.width, y1 = rect.y , y2 = rect.y
//				+ rect.height;
//		drawAnchorLine(g2, topLine, format == null ? null : format
//				.getLineColor(Format.TOPLINE), x1, y1 + 1, x2, y1 + 1);
//		drawAnchorLine(g2, bottomLine, format == null ? null : format
//				.getLineColor(Format.BOTTOMLINE), x1, y2 - 1, x2, y2 -1);
//		drawAnchorLine(g2, leftLine, format == null ? null : format
//				.getLineColor(Format.LEFTLINE), x1 + 1, y1, x1 + 1, y2);
//		drawAnchorLine(g2, rightLine, format == null ? null : format
//				.getLineColor(Format.RIGHTLINE), x2 - 1, y1, x2 - 1, y2);
//		
	}
	
	
	private void paintAnchorBorder(Graphics g, Rectangle r){
		 
		Graphics2D g2d = (Graphics2D) g;
//		CellPosition anchor = m_cellsPane.getDataModel().getSelectModel().getAnchorCell();
		Stroke lineStroke = LineFactory
		.createLineStroke(TableConstant.L_SOLID1);
		g2d.setStroke(lineStroke);
		g2d.setColor(Color.BLACK);
		g2d.drawRect(r.x + 1, r.y + 1, r.width - 3, r.height - 3);
	}
	
	/**
	 * ���Ƶ�����Ԫ
	 * @param g ͼ��������
	 * @param cellRect ��Ԫ�ı߿�
	 * @param row ��Ԫ��
	 * @param column ��Ԫ��
	 * @param bPrint �Ƿ��ӡ
	 */
	private void paintCell(Graphics g, Rectangle cellRect, int row, int column, 
			boolean bPrint) {
		 
		Component[] components = m_cellsPane.prepareRenderer(row, column, bPrint);
		if (components == null) {
			return;
		}
			
		int w = cellRect.width;
		int h = cellRect.height;
		
		if (!m_cellsPane.isShowRowHeader()) {
			h += 1;
		}
		if (!m_cellsPane.isShowColHeader()) {
			w += 1;
		}
		for (Component component : components) {
			try {
				m_rendererPane.paintComponent(g, component, m_cellsPane,
						cellRect.x, cellRect.y, w, h, false);  //liyyy�� ������Ϊfalse������ʱ�������У�顣

			} catch (Throwable e) {
				// ���ﲶ׽���⣬���jdk��bug��http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4138921
			}
		}
		
		if (!bPrint
				&& m_cellsPane.getDataModel().getSelectModel()
						.isMultiSelected()
				&& m_cellsPane.isCellAnchor(row, column)) {
			paintAnchorBorder(g, cellRect);
		}
		 
	}
  
	
	/**
	 * ��������Ԫ��
	 * 
	 * @param g
	 *            ͼ��������
	 * @param cellRect
	 *            ����߿�
	 * @param cc
	 *            ����Ԫ
	 * @param bPrint
	 *            �Ƿ��ӡ
	 * @param opaque
	 *            �Ƿ�͸��
	 */
	private void paintArea(Graphics g, Rectangle cellRect, AreaCell cc,
			boolean bPrint,boolean opaque) {
//		AreaPosition aPos = cc.getArea();
//		int row = aPos.getStart().getRow();
//		int col = aPos.getStart().getColumn();
		
		//���������ʱ����Ҫ�������������߲�����������ư�ɫ�����ʱ��JLable�����������ߡ�
		if(opaque){
			Color oldColor = g.getColor();
			g.setColor(m_cellsPane.getBackground());
			g.fillRect(cellRect.x, cellRect.y, cellRect.width, cellRect.height);
			g.setColor(oldColor);
		}
		
		//���Ƶ�Ԫ����
//		if (table.isEditing() && table.getTablePane().getEditingRow() == row
//				&& table.getTablePane().getEditingColumn() == col) {
//			Component component = table.getTablePane().getEditorComp();
//			component.repaint();
//			component.validate();
//		} else {
			if(opaque){
				Component[] components = m_cellsPane.prepareRenderer(cc, bPrint);
				if(components != null){
					for (Component comp: components) {
						try{
							m_rendererPane.paintComponent(g, comp, m_cellsPane,
									cellRect.x, cellRect.y, cellRect.width,
									cellRect.height, false); //liyyy�� ������Ϊfalse������ʱ�������У�顣
						} catch(Throwable e) {
							AppDebug.debug(e);
						}
					}
				}
			} else {
			CellRenderAndEditor re = m_cellsPane.getReanderAndEditor();
			Object value = cc == null ? null : cc.getValue();
			// TODO ��Ҫ��ϸ����
			if (value != null) {
				boolean isSelected = false;
				boolean hasFocus = false;
				if (!bPrint && (value instanceof SelectedArea)) {
					isSelected = true;
				}
				Component cmp = re.getRender(
						value == null ? null : value.getClass())
						.getCellRendererComponent(m_cellsPane, value, isSelected,
								hasFocus, cc.getRow(), cc.getCol(), cc);
				if (cmp != null) {
					try {
						m_rendererPane.paintComponent(g, cmp, m_cellsPane,
								cellRect.x, cellRect.y, cellRect.width,
								cellRect.height, false); // liyyy��
						// ������Ϊfalse������ʱ�������У�顣
					} catch (Throwable e) {
						AppDebug.debug(e);
					}

				}
			}
		}

//		}
			
		//��ʼ���Ʊ��ߡ�   //.getCellFormat(aPos.getStart());//
//		Format format = getCellsModel().getRealFormat(aPos.getStart());//.getConbineFormat(aPos.getStart());
//		if (format != null && cc instanceof CombinedCell) {
//		    //����Ļ��ƺ��������ŵ�,���Ի���ȫ���� �Ϻ��� ����.
//			paintCellLine(g, format, cellRect, true, true);
//		}
	}

	/**
	 * ����һ����Ԫ�ı���
	 * 
	 * @param g����ͼ���
	 * @param format,��Ԫ�ĸ�ʽ��Ϣ
	 * @param r,��Ԫռ�ݵ����������������������������֮�ڡ�
	 * @param paintCeiling,�Ƿ�����϶�������Ϊ����߻���Ч�ʣ�ͨ��ֻ�����Ҳ���·������������Ƶ�һ��Ϊtrue
	 * @param paintLeft,��������������Ϊ����߻���Ч�ʣ�ͨ��ֻ�����Ҳ���·������������Ƶ�һ��Ϊtrue
	 * @param colMargin,�м��
	 * @param rowMargin,�м��
	 */
	private void paintCellLine(Graphics g, Format format, Rectangle r,
			boolean paintCeiling, boolean paintLeft) {
		
		if (format == null) {
			return;
		}
			
		int colMargin = getCellsModel().getColumnHeaderModel().getMargin();
		int rowMargin = getCellsModel().getRowHeaderModel().getMargin();
		
		Graphics2D g2d = (Graphics2D) g;
		
		int startX, startY, endX, endY, linetype;
		
		startX = r.x - ((colMargin + 1) >> 1);
		startY = r.y - ((rowMargin + 1) >> 1);
		endX = r.x + r.width - 1 + ((colMargin + 1) >> 1);
		endY = r.y + r.height - 1 + ((rowMargin + 1) >> 1);
		
//		startX = r.x;
//		startY = r.y;
//		
//		endX = r.x + r.width ;
//		endY = r.y + r.height;
		
		if (startY < 0)
			startY = 0;
		if (startX < 0)
			startX = 0;
		linetype = format.getLineType(Format.TOPLINE);
		if (paintCeiling){ //|| linetype == TableConstant.L_SOLID2
				//|| linetype == TableConstant.L_SOLID3
				//|| linetype == TableConstant.L_SOLID4){
			
			draw2DLine(g2d, linetype, format.getLineColor(Format.TOPLINE),
					startX, startY, endX, startY, Format.TOPLINE);
		}
		
		linetype = format.getLineType(Format.LEFTLINE);
		if (paintLeft){// || linetype == TableConstant.L_SOLID2
				//|| linetype == TableConstant.L_SOLID3
			//	|| linetype == TableConstant.L_SOLID4) {
			
			draw2DLine(g2d, linetype, format.getLineColor(Format.LEFTLINE),
					startX, startY, startX, endY, Format.LEFTLINE);
		}
		
		draw2DLine(g2d, format.getLineType(Format.RIGHTLINE), format
				.getLineColor(Format.RIGHTLINE), endX, startY, endX, endY,
				Format.RIGHTLINE);
		
		draw2DLine(g2d, format.getLineType(Format.BOTTOMLINE), format
				.getLineColor(Format.BOTTOMLINE), startX, endY, endX, endY,
				Format.BOTTOMLINE);
		
		// ����б�ߺͷ�б��
		draw2DLine(g2d, format.getLineType(Format.DIAGONAL_LINE), format
				.getLineColor(Format.DIAGONAL_LINE), startX, startY, endX, endY,
				Format.DIAGONAL_LINE);		
		
		draw2DLine(g2d, format.getLineType(Format.DIAGONAL2_LINE), format
				.getLineColor(Format.DIAGONAL2_LINE), endX, startY, startX, endY,
				Format.DIAGONAL2_LINE);				
		
	}
	/**
	 * ���Ʊ߿�����������ͨ����ʵ�ߣ�ֱ�ӻ��ƻ�����䣬��������Ч����������ʹ��Stroke���ơ� ԭ����Stroke���Ƶ�Ч�ʱȽϵ͡�
	 * 
	 * @param g ͼ��������
	 * @param line������
	 * @param lineColor��������ɫ
	 * @param x1�����
	 * @param y1�����
	 * @param x2���յ�
	 * @param y2���յ�
	 */
	private void draw2DLine(Graphics2D g, int line, Color lineColor, int x1,
			int y1, int x2, int y2, int type) {
		if (line == TableConstant.UNDEFINED) {
			line = DefaultFormatValue.LINETYPE;
		}
		if (line == TableConstant.L_NULL) {
			return;
		}
		Color oldColor = g.getColor();
		if (lineColor == null) {
			lineColor = Color.BLACK;
		}
		g.setColor(lineColor);
		
		if(line == TableConstant.L_SOLID1 ||  type == Format.DIAGONAL_LINE || type == Format.DIAGONAL2_LINE){
			g.drawLine(x1, y1, x2, y2);
			g.setColor(oldColor);
			return;
		}
				
		/**
		 * ������µķ�֧�жϣ�Ŀ������߻��Ƶ�Ч�ʡ�
		 */
		if (line == TableConstant.L_DASH   
				|| line == TableConstant.L_DOT
				|| line == TableConstant.L_DASHDOT) {
			//������������ͣ���ô�Ȳ��������ߣ���������Stroke
			Stroke oldStroke = g.getStroke();
				g.setColor(Color.white);
				g.drawLine(x1, y1, x2, y2);
				g.setColor(lineColor == null ? oldColor : lineColor);
				Stroke lineStroke = LineFactory.createLineStroke(line);
				if (!oldStroke.equals(lineStroke)) {
					g.setStroke(lineStroke);
				}
			g.drawLine(x1, y1, x2, y2);
			g.setStroke(oldStroke);
			g.setColor(oldColor);
			return;
		} 
		int offset = 0;
		int deep = 1;
		switch (line) {
			case TableConstant.L_SOLID2 :
				deep = 2;
				offset = 1;
				break;
			case TableConstant.L_SOLID3 :
				deep = 3;
				offset = 1;
				break;
			case TableConstant.L_SOLID4 :
				deep = 4;
				offset = 2;
				break;
		}
		
		int w = 0, h = 0, x = x1, y = y1;
		  
		switch(type){
			case Format.TOPLINE:
			case Format.BOTTOMLINE:
				x = x1 - offset;
				w = x2 - x1 + offset + 1;
				y = y1 - offset;
				h = deep;
				break;
			case Format.RIGHTLINE:
			case Format.LEFTLINE:
				x = x1 - offset;
				w = deep;
				y = y1;
				h = y2 - y1  + offset;
				break;					
				
//				case Format.BOTTOMLINE:
//					x = x1 - offset;
//					w = x2 - x1 + 1 + offset;
//					y = y1 - offset;
//					h = deep;
//					break;				
//				case Format.LEFTLINE:
//					x = x1 - offset;
//					w = deep;
//					y = y1;
//					h = y2 - y1 + 1;
//					break;
		}
		
		if(x < 0) x = 0;
		if(y < 0) y = 0;
		if(w < 0) w = 0;
		if(h < 0) h = 0;
		
		g.fillRect(x, y, w, h);
	 
		g.setColor(oldColor);

	}

	
	
	/**
	 * ����һ��Anchor��.���Ĺ��������ԭ����������Line.L_SOLID2,Line.L_SOLID3,Line.L_SOLID4,
	 * ���Ȳ�����ǰ���Ƶı���,Ȼ�����һ�����������;�������ƴ��߼���.
	 * 
	 * @param g ͼ��������
	 *            Graphics2D
	 * @param lineStyle ����
	 *            int 
	 * @param x1�����
	 * @param y1�����
	 * @param x2���յ�
	 * @param y2���յ�
	 */
	private void drawAnchorLine(Graphics2D g, int lineStyle, Color linecolor,
			int x1, int y1, int x2, int y2) {
		//		g = (Graphics2D)g.create(x1,y1,x2-x1+1,y2-y1+1);
		//		Stroke oldStroke = g.getStroke();
		//		Color oldColor = g.getColor();
		if (lineStyle == TableConstant.L_SOLID2
				|| lineStyle == TableConstant.L_SOLID3
				|| lineStyle == TableConstant.L_SOLID4) {
			Stroke lineStroke = LineFactory
					.createLineStroke(TableConstant.L_SOLID2);
			if (TableStyle.ANCHOR_COLOR.equals(linecolor)) {
				g.setColor(TableUtilities.reverseColor(TableStyle.ANCHOR_COLOR));
			} else {
				g.setColor(TableStyle.ANCHOR_COLOR);
			}
			g.setStroke(lineStroke);
			g.drawLine(x1, y1, x2, y2);
			//			lineStroke = LineFactory.createLineStroke(TableConstant.L_DASH);
			//			g.setColor(oldColor);
			//			g.setStroke(lineStroke);
			//			g.drawLine(x1, y1, x2, y2);
		} else {
			Stroke lineStroke = LineFactory
					.createLineStroke(TableConstant.L_SOLID2);
			g.setColor(TableStyle.ANCHOR_COLOR);
			g.setStroke(lineStroke);
			g.drawLine(x1, y1, x2, y2);
		}
		//		g.setColor(oldColor);
		//		g.setStroke(oldStroke);
		//		g.dispose();
	}

	//**************************��꣬���̶�����Ӧ���¼���Ӧ�Ĵ���********************//

	/**
	 * <p>
	 * Title:������Ӧ�������ҵ��ƶ��¼�
	 * </p>
	 * <p>
	 * Description:
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
	private static class NavigationalAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		/** ˮƽ�ƶ��� */
		protected int dx;

		/** ��ֱ�ƶ��� */
		protected int dy;

		/**
		 * @param dx
		 *            ˮƽ�ƶ���Ԫ������.
		 * @param dy
		 *            ��ֱ�ƶ��ĵ�Ԫ������.
		 */
		protected NavigationalAction(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}
		/**
		 * �����ƶ���
		 * 
		 * @param x
		 * @param y
		 */
		protected void setOffset(int x, int y) {
			this.dx = x;
			this.dy = y;
		}
        /**
         * ��������ʱ�Ĵ�����
         */
		public void actionPerformed(ActionEvent e) {
			CellsPane table = (CellsPane) e.getSource();
			SelectModel selModel = table.getSelectionModel();
			CellsModel dataModel = table.getDataModel();
			
			//�õ���ǰ��ê���λ��
			CellPosition anchorCell = selModel.getAnchorCell();
			if (anchorCell == null)
				return;
			int anchorRow = anchorCell.getRow();
			int anchorColumn = anchorCell.getColumn();
			//�����Ԫ��ǰ״̬�Ǳ༭,���ұ༭û�н���,����Ӧ��Щ�¼�.
			if (table.isEditing()
					&& !table.getTablePane().getCellEditor().stopCellEditing()) {
				return;
			}
			/**
			 * �ƶ��Ƿ񵽴����ޱ��ģ�����λ��? �����,��Ҫ�ж��Ƿ���Ե���ģ�͵Ĵ�С;
			 * �ƶ���Ҫ�ж��Ƿ��ƶ�������ͼ�ı�Ե?�����,��Ҫ�ƶ���ͼ��λ��;
			 */
			int newRow, newCol;
			newRow = anchorRow + dy;
			newCol = anchorColumn + dx;
			//�����жϳ�ʼ����ĵ�Ԫ�Ƿ���һ����ϵ�Ԫ,�����,��Ҫ���ݵ�ǰ�����ķ���������Anchor��λ��.
			CombinedCell cc = dataModel.getCombinedAreaModel().belongToCombinedCell(anchorCell);//.belongToCombinedCell(anchorRow, anchorColumn);
			if (cc != null) {
				CellPosition endCell = cc.getArea().getEnd();
				if (dy > 0 && newRow <= endCell.getRow()) {
					newRow = endCell.getRow() + 1;
				} else if (dx > 0 && newCol <= endCell.getColumn()) {
					newCol = endCell.getColumn() + 1;
				}
			}
			if(newRow >= 0 && newCol >= 0){
			    selModel.clear();
			    selModel.setAnchorCell(newRow, newCol);
			}
			table.moveViewToDisplayRect(selModel.getAnchorCell(), true);
//			table.repaint(selModel.getAnchorCell(), true);
//			table.repaint(oldSelArea, true);			
		}
	}

	/** ����Pageup��Pagedown����Ӧ�� */
	private static class PagingAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private boolean forwards;
		private PagingAction(boolean forwards) {
			this.forwards = forwards;
		}
		/**
         * ��������ʱ�Ĵ�����
         */
		public void actionPerformed(ActionEvent e) {
			CellsPane table = (CellsPane) e.getSource();
			//���ȼ��������ͼ��λ�ã�Ȼ����㽹���ƶ������Ǹ�λ�á�
			CellsModel dataModel = table.getDataModel();
			SelectModel selModel = dataModel.getSelectModel();
			CellPosition anchorCell = selModel.getAnchorCell();
			HeaderModel rowModel = dataModel.getRowHeaderModel();

			int anchorY = rowModel.getPosition(anchorCell.getRow());
			JViewport view = (JViewport) table.getParent();
			Point cellPoint = view.getViewPosition();
			Dimension extDim = view.getExtentSize();
			Dimension tableDim = view.getViewSize();
			int anchorRow;
			//ֻ�ǿ��Ǵ�ֱ����ķ�ҳ��
			if (forwards) { //���·�ҳ
				cellPoint.y = Math.min(cellPoint.y + extDim.height,
						tableDim.height - extDim.height);
				anchorRow = rowModel
						.getIndexByPosition(anchorY + extDim.height);
				if (anchorRow < 0) {
					anchorRow = rowModel.getTempCount() - 1;
				}
			} else {
				cellPoint.y = Math.max(cellPoint.y - extDim.height, 0);
				anchorRow = rowModel
						.getIndexByPosition(anchorY - extDim.height);
				if (anchorRow < 0) {
					anchorRow = 0;
				}
			}
			
			view.setViewPosition(cellPoint);
			if (anchorRow != anchorCell.getRow()) {
				selModel.setAnchorCell(anchorRow, anchorCell.getColumn());
			}
			// @edit by wangyga at 2009-3-4,����07:09:36
			table.paginalData();
		}
	}

	/** ����Ctrl��A */
	private static class SelectAllAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			CellsPane table = (CellsPane) e.getSource();
			table.selectAll();
		}
	}
	
	private static class ObjectViewAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			 
			CellsPane table = (CellsPane) e.getSource();
			String clzName = "com.angel.datastream.ObjectView";
			Class clz = null;
			try {
				
//				ObjectView v = new ObjectView();
//				v.add(new java.util.Vector());
//				v.show();
//				
				clz = table.getClass().getClassLoader().loadClass(clzName);
				Object obj = clz.newInstance();
				Method mthd = clz.getMethod("add", Object.class);
//				mthd.invoke(obj, table.getReport());
				mthd.invoke(obj, table.getDataModel());
				mthd = clz.getMethod("show", null);
				mthd.invoke(obj, null);
				
			} catch (Throwable e1) {
				System.out.println("!!!!!!!!ObjectView error." + e1);
				AppDebug.debug(e1);
				return;
			}
		}
	}

	/**
	 * �༭ȡ������.��ӦEsc�������,ɾ����ǰ�ı༭��.
	 */
	private static class CancelEditingAction extends AbstractAction {
	 
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			CellsPane table = (CellsPane) e.getSource();
			table.removeEditor();
			BorderPlayRender.stopPlay(table);//ֹͣ������add by wangyga
		}
	}

	/**
	 * ��ʼ�༭�Ķ���.��ӦEnter��,�����㸳��༭����
	 */
	private static class StartEditingAction extends AbstractAction {
	 
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			CellsPane table = (CellsPane) e.getSource();
			if (!table.hasFocus()) { //��鵱ǰ�Ľ����Ƿ��ڱ༭���֮��
				CellEditor cellEditor = table.getTablePane().getCellEditor();
				if (cellEditor != null && !cellEditor.stopCellEditing()) {
					return;
				}
				table.requestFocus();
				return;
			}
			SelectModel sm = table.getSelectionModel();
			int row = sm.getAnchorCell().getRow();
			int col = sm.getAnchorCell().getColumn();
			//�õ�ѡ��Ľ���
			Component editorComp = table.getTablePane().getEditorComp();
			if (editorComp == null) {
				table.editCellAt(row, col);
				editorComp = table.getTablePane().getEditorComp();
			}
			if (editorComp != null) {
				editorComp.requestFocus();
			}
		}
	}

	/**
	 * �������¼��Ĵ��롣
	 */
	private class FocusHandler implements FocusListener {
		/**
		 * ��ý����ʱ����ƽ��㵥Ԫ
		 * 
		 * @param e
		 */
		public void focusGained(FocusEvent e) {
			repaintAnchorCell();
		}

		/**
		 * ʧȥ�����ʱ�����ʧȥ����ĵ�Ԫ
		 * 
		 * @param e
		 */
		public void focusLost(FocusEvent e) {
			repaintAnchorCell();
			//midify by wangyga ֹͣ����
			BorderPlayRender.stopPlay(m_cellsPane);
		
//			doneFullScreenDraw = false;
		}
        /**
         * ���»���ê�����ڵ�Ԫ
         */
		private void repaintAnchorCell() {
			int rc = m_cellsPane.getRowCount();
			int cc = m_cellsPane.getColumnCount();
			CellPosition cell = m_cellsPane.getSelectionModel().getAnchorCell();
			if (cell != null) {
				if (cell.getRow() < 0 || cell.getRow() >= rc
						|| cell.getColumn() < 0 || cell.getColumn() >= cc) {
					return;
				}
			} else {
				cell = CellPosition.getInstance(0, 0);
			}
			Rectangle dirtyRect = m_cellsPane.getCellRect(cell, false);
			m_cellsPane.repaint(dirtyRect);
		}
	}


	/**
	 * ���¼���Ҫ��ӦHome��End�Լ����ǵ����.
	 */
	private static class HomeEndAction extends NavigationalAction {
		private boolean ctrlPressed, home;

		/**
		 * 
		 * @param bCtrlPress
		 *            ctrl�Ƿ���.
		 * @param bHome
		 *            ��home����end.trueΪHome.
		 */
		protected HomeEndAction(boolean bCtrlPress, boolean bHome) {
			super(0, 0);
			ctrlPressed = bCtrlPress;
			home = bHome;
		}
		/**
         * ��������ʱ�Ĵ�����
         */
		public void actionPerformed(ActionEvent e) {
			//���ȱ仯����,Ȼ��任��ͼ.
			CellsPane cells = (CellsPane) e.getSource();
			SelectModel selModel = cells.getDataModel().getSelectModel();
			int anchorRow = selModel.getAnchorCell().getRow();
			int anchorCol = selModel.getAnchorCell().getColumn();
			int xOffset = 0, yOffset = 0;
			if (home) {
				xOffset = 0 - anchorCol;
				if (ctrlPressed) {
					yOffset = 0 - anchorRow;
				}
			} else {
				HeaderModel rowModel = cells.getDataModel().getRowHeaderModel();
				HeaderModel colModel = cells.getDataModel()
						.getColumnHeaderModel();
				xOffset = colModel.getTempCount() - 1 - anchorCol;
				if (ctrlPressed) {
					yOffset = rowModel.getTempCount() - 1 - anchorRow;
				}
			}
			setOffset(xOffset, yOffset);
			super.actionPerformed(e);
		}
	}

	/** �˴���������ճ���¼� */
//	static class TransferAction extends AbstractAction {
//
//		TransferAction(String name) {
//			super(name);
//		}
//
//		public void actionPerformed(ActionEvent e) {
//			Object src = e.getSource();
//			if (src instanceof CellsPane) {
//				CellsPane pane = (CellsPane) src;
//				UFOTable table = (UFOTable) pane.getParent().getParent();
//				String name = (String) getValue(Action.NAME);
//				if ("cut".equals(name)) {
//					table.cut(UFOTable.CELL_CONTENT);
//				} else if ("copy".equals(name)) {
//					table.copy(UFOTable.CELL_CONTENT);
//				} else if ("paste".equals(name)) {
//					table.paste();
//				} else if ("delete".equals(name)) {
//					table.clear(UFOTable.CELL_CONTENT);
//				}
//			}
//		}
//	}
	/**
	 * ��ȡCellsModel
	 */
	private CellsModel getCellsModel(){
	    return m_cellsPane.getDataModel();
	}
	/**
	 * ��ȡRowModel
	 */
	private HeaderModel getRowModel(){
	    return getCellsModel().getRowHeaderModel();
	}
	/**
	 * ��ȡColModel
	 */
	private HeaderModel getColModel(){
	    return getCellsModel().getColumnHeaderModel();
	}
}