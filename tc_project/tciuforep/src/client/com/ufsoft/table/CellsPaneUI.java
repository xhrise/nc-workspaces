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
 * Title: 表格组件的UI代理
 * </p>
 * <p>
 * Description: 负责表格部分的绘制和事件响应
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: UFSOFT
 * </p>
 * 
 * @author wupeng
 * @version 1.0.0.1</br> 2004-04-01 修正线条类型设置为Line.L_NULL时，线条依然绘制的错误。</br>
 *          2004-12-6 添加对于透明的区域数据显示的功能。
 *  
 */

public class CellsPaneUI extends ComponentUI {
	/** 代理关联的组件 */
	private CellsPane m_cellsPane;
	/** 实际绘制的面板 */
	private CellRendererPane m_rendererPane;
	/** 监听器 */
	private FocusListener focusListener;
 
	/** 绘制各种边线的最大线条宽度。 */
	int MaxLineDeep = 5;
//
//	boolean doneFullScreenDraw = false;
	
	/**鼠标监听器*/
	CellsPaneUIMouseActionHandler mouseActionHandler = null;
	//×××××××××××××××××以下方法创建各个监听器.××××××××××××××××××××××
	/**
	 * 创建CellsPaneUI的焦点事件监听器
	 */
	private FocusListener createFocusListener() {
		return new FocusHandler();
	}
   /**
    * 创建CellsPaneUI的鼠标移动事件监听器
    * @return
    */
	public CellsPaneUIMouseActionHandler createMouseInputListener() {
	    if(mouseActionHandler == null) {
	        mouseActionHandler = new CellsPaneUIMouseActionHandler(this.m_cellsPane);
	    }
		return mouseActionHandler;
	}
	/**
	 * 设置CellsPaneUI新的鼠标监听器，并移除老的监听器
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
	//×××××××××××××××××××××××××× 安装、卸载UI××××××××××××××××××××××××
	/**
	 * 创建UI代理
	 */
	public static ComponentUI createUI(JComponent c) {
		return new CellsPaneUI();
	}
    /**
     * 覆盖类 ComponentUI 中的 installUI
     * 初始化CellsPane的实际绘制的面板CellRendererPane，按顺序调用 installDefaults()、installListeners() 和 installKeyboardActions() 来初始化CellsPaneUI对应的CellsPane
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
	 * 设置当前UI的一些缺省的属性。
	 */
	protected void installDefaults() {
		m_cellsPane.setBackground(Color.white);
		m_cellsPane.setForeground(Color.black);
//		table.setFont(Font.getFont("Dialog"));
		m_cellsPane.setGridColor(TableStyle.GRID_COLOR);
	}

	/**
	 * 添加监听器.
	 */
	protected void installListeners() {
		focusListener = createFocusListener();
		m_cellsPane.addFocusListener(focusListener);

        setCellsPaneUIMouseActionHandler(createMouseInputListener());
		
	}

	/**
	 * 注册键盘动作
	 */
	protected void installKeyboardActions() {
		SwingUtilities.replaceUIActionMap(m_cellsPane, createActionMap());
		//利用在JTAble中定义的键盘事件.
		InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		SwingUtilities.replaceUIInputMap(m_cellsPane,
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inputMap);
	}

	/**
	 * 构建一个ActionMap.为了简单起见,借用JTable已经定义的动作名称.注意:这些名称的定义
	 * 在其UI管理器中,如果希望修改,需要修改相应的getInputMap的方法. 目前支持的键盘动作包括
	 * <ol>
	 * <li>上下左右键:对应当前选择焦点的变换
	 * </ol>
	 * 
	 * @return ActionMap
	 */
	private ActionMap createActionMap() {
		ActionMap map = new ActionMap();
		//上下左右键的支持
		map.put("selectNextColumn", new NavigationalAction(1, 0));
		map.put("selectPreviousColumn", new NavigationalAction(-1, 0));
		map.put("selectNextRow", new NavigationalAction(0, 1));
		map.put("selectPreviousRow", new NavigationalAction(0, -1));

		//Pageup,Pagedown的处理
		map.put("Pageup", new PagingAction(false));
		map.put("Pagedown", new PagingAction(true));
		//回车键的移动
		map.put("nextCell", new NavigationalAction(0, 1));

		//Home,End,Ctrl+Home,Ctrl+End
		map.put("Home", new HomeEndAction(false, true));
		map.put("End", new HomeEndAction(false, false));
		map.put("CtrlHome", new HomeEndAction(true, true));
		map.put("CtrlEnd", new HomeEndAction(true, false));

		//在某个选中单元输入字母或者数字,开始编辑.

		//处理全选,取消单元编辑,单元编辑
		map.put("selectAll", new SelectAllAction());
		map.put("cancel", new CancelEditingAction());
		map.put("startEditing", new StartEditingAction());
		
		map.put("objectView", new ObjectViewAction());

//		//处理复制,减切,粘贴
//		map.put("copy", new TransferAction("copy"));
//		map.put("cut", new TransferAction("cut"));
//		map.put("paste", new TransferAction("paste"));
//		map.put("delete", new TransferAction("delete"));
		return map;
	}

	/**
	 * 构建键盘时事件响应之间的关系.
	 * 
	 * @param condition
	 *            int
	 * @return InputMap
	 */
	private InputMap getInputMap(int condition) {
		if (condition == JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT) {
			InputMap keyMap = new InputMap();
			//方向键左右上下
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
					"selectPreviousColumn");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
					"selectNextColumn");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
					"selectPreviousRow");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
					"selectNextRow");
			//回车键移动到下一个单元。回车键一定要加参数true.因为编辑状态时,其press事件被编辑器消耗.
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),//, true),
							"nextCell");
			//Esc，Ctrl＋A，F2
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
	 * 计算表格的实际尺寸。方法内只计算表格的高度。
	 * 
	 * @param width
	 *            表格的宽度。
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
	 * 得到当前组件的最小高度。重载父类方法
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
	 * 得到组件的缺省尺寸。重载父类方法
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
	 * 得到最大尺寸。重载父类方法
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
	//****************** 以下为绘制表格的代码 ****************
	//************************************************************
	/**
	 * 实现打印调用的绘制接口.这个方法供打印预览和打印共同调用.
	 * 
	 * @param g
	 *            Graphics
	 * @param rect
	 *            Rectangle 组件上本次绘制区间
	 * @param xscale
	 *            double 打印或者预览视图需要x方向缩放的比例
	 * @param yscale
	 *            double 打印或者预览视图需要y方向缩放的比例
	 * @return int
	 * modify by guogang 2007-9-25 修正了打印越界的问题
	 */
	public int print(Graphics g, Rectangle rect, double xscale,double yscale) {
		//处理打印比例
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform transform = g2d.getTransform();
		
//		double scalex=transform.getScaleX();
//		double scaley=transform.getScaleY();
//		int linex=(int)(1*scalex);
//		int liney=(int)(1*scaley);
//		
		transform.setToScale(xscale, yscale);
		g2d.transform(transform);
		//设置打印区域。
		g2d.translate(-rect.x, -rect.y);
		
		//绘制区域
		
		Point upperLeft = rect.getLocation();
		//为了获取正确的结束行列,有-1
		Point lowerRight = new Point(rect.x + rect.width-1, rect.y
				+ rect.height - 1);
		
		int rMin = m_cellsPane.rowAtPoint(upperLeft);
		int rMax = m_cellsPane.rowAtPoint(lowerRight);
		int cMin = m_cellsPane.columnAtPoint(upperLeft);
		int cMax = m_cellsPane.columnAtPoint(lowerRight);
		// 如果越界，绘制首尾行列
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
		
        //确保要打印的单元格（包括边线）在剪切区域内,使的x-1,x-1后为获取整个区域width+1
		//modify by guogang 2008-4-9 如果首行边线的线宽>1的话仅仅向外扩1象素是不够的
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
	 * 绘制表格
	 * 
	 * @param g
	 *            Graphics
	 * @param cc
	 *            JComponent
	 */
	public void paint(Graphics g, JComponent cc) {
		// @edit by wangyga at 2009-2-24,下午07:46:31
		if(getCellsModel() == null){
			return;
		}
		//绘制区域
		Rectangle clip = g.getClipBounds();
		Point upperLeft = clip.getLocation();
		Point lowerRight = new Point(clip.x + clip.width, clip.y
				+ clip.height );
		int rMin = m_cellsPane.rowAtPoint(upperLeft);
		int rMax = m_cellsPane.rowAtPoint(lowerRight);
		int cMin = m_cellsPane.columnAtPoint(upperLeft);
		int cMax = m_cellsPane.columnAtPoint(lowerRight);
		// 如果越界，绘制首尾行列
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
	 * 打印和屏幕绘制共同调用的代码
	 * 
	 * @param g
	 *            Graphics 绘制图象
	 * @param isPrint
	 *            boolean 是否正在打印。如果是打印，那么不绘制网格线和区域选择， Anchor在单元上添加的图象。
	 *  modify by guogang 修正了打印越界的问题
	 */
	private void draw(Graphics g, boolean isPrint,int rMin, int rMax, int cMin, int cMax) {
		// 有限表
		if (!getCellsModel().isInfinite()
				&& (m_cellsPane.getRowCount() <= 0 || m_cellsPane
						.getColumnCount() <= 0)) {
			return;
		}
		
//		AreaPosition area = AreaPosition.getInstance(CellPosition.getInstance(
//				rMin, cMin), CellPosition.getInstance(rMax, cMax));
		 
		
		// 绘制网格线.
		if (!isPrint) {
			paintGrid(g, rMin, rMax, cMin, cMax);
		}
		
		// 绘制单元.
		paintCells(g, rMin, rMax, cMin, cMax, isPrint);
		 
//		//绘制焦点单元的效果.
//		if (!isPrint) {
//			paintAnchor(g);
//		}
		
		//重新绘制编辑单元
		if (m_cellsPane.isEditing()) {
			Component component = m_cellsPane.getTablePane().getEditorComp();
			component.repaint();
			component.validate();
		}
		
		//绘制后，删除所有渲染器
		m_rendererPane.removeAll();
		
		//绘制打印的分割线条
		drawPrintLine(g,rMin,rMax,cMin,cMax,isPrint);
		 
	}

	/**
	 * 绘制网格线
	 * @param g 图形上下文
	 * @param rMin 绘制区域起点的行号
	 * @param rMax 绘制区域终点的行号
	 * @param cMin 绘制区域起点的列号
	 * @param cMax 绘制区域终点的列号
	 */
	private void paintGrid(Graphics g, int rMin, int rMax, int cMin, int cMax) {
 
		if(!m_cellsPane.isShowRowHeader() && !m_cellsPane.isShowColHeader()){
			return;
		}
		
		g.setColor(m_cellsPane.getGridColor());
		//    Rectangle minCell = table.getCellRect(rMin, cMin,
		// true);//这里计算遇到组合单元时，会取出组合单元的矩形，因此出错。
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
			//绘制水平线
			int tableWidth = damagedArea.x + damagedArea.width;
			int y = damagedArea.y;
			for (int row = rMin; row <= rMax; row++) {
				y += m_cellsPane.getRowHeight(row);
				g.drawLine(damagedArea.x, y - 1, tableWidth - 1, y - 1);
			}
		}
		
		if(m_cellsPane.isShowColHeader()){
			//绘制垂直线
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
	 * 绘制所有的单元.首先构建每个单元对应的单元组件，然后检查单元是否有边线，有边线需要擦除以前绘制 的Grid。
	 * @param g    图形上下文
	 * @param rMin 绘制区域起点的行号
	 * @param rMax 绘制区域终点的行号
	 * @param cMin 绘制区域起点的列号
	 * @param cMax 绘制区域终点的列号 
	 * @param isPrint
	 *            是否是绘制打印效果
	 */
	private void paintCells(Graphics g, int rMin, int rMax, int cMin, int cMax,
			boolean isPrint) {
	
		Rectangle cellRect; //绘制范围
		
		//绘制个别单元
//		AppDebug.debug("开始绘制:("+rMin+","+cMin+")-->("+rMax+","+cMax+")");
		
//		if(rMin == 0 && cMin == 0 && rMax > 30 && cMax > 10){
			//AppDebug.debug("全屏绘制:("+rMin+","+cMin+")-->("+rMax+","+cMax+")");  //关闭
//			AppDebug.debug(Thread.currentThread().getStackTrace());
//		}
		
		for (int row = rMin; row <= rMax; row++) {
			for (int column = cMin; column <= cMax; column++) {
				CellPosition pos = CellPosition.getInstance(row, column);
				CombinedCell cc = getCellsModel().getCombinedAreaModel().belongToCombinedCell(pos);
				//合并区域会在区域绘制的时候处理，不用绘制合并区域的单元格。liuyy. 2008-08-25
				if(cc != null){// && !pos.equals(cc.getArea().getStart())){
					continue;
				}
				cellRect = m_cellsPane.getCellRect(pos, false);
				paintCell(g, cellRect, row, column, isPrint);
				  
			}
		}
		
		/**
		 * 得到当前需要绘制的区域。就是以最大行列位置为结束所可能包含的所有组合单元。
		 * 注意：可能组合单元定义在最小单元之外，但是延伸的单元会到达绘制区域。
		 */
		IArea validArea = AreaPosition.getInstance(CellPosition.getInstance(rMin, cMin), CellPosition.getInstance(rMax, cMax));
		ArrayList<AreaCell> areaCells = getCellsModel().getAreaCell(validArea);//rMax, cMax);
		ArrayList<AreaCell>[] filter = filterCells(areaCells);
		ArrayList<AreaCell> opaque = filter[0];
		ArrayList<AreaCell> disopaque = filter[1];
		
		//绘制区域
		drawArea(g, rMin, rMax, cMin, cMax, isPrint, opaque,true);
		
		//开始绘制透明的区域
		drawArea(g, rMin, rMax, cMin, cMax, isPrint, disopaque,false);
		
		
		//为解决单元格粗边线与临近单元格背景绘制覆盖问题，先绘制所有单元格，再绘制单元格边线，弱啊~~``  liuyy. 2007-04-25
		//modify by guogang 2008-4-9 为解决组合单元覆盖临近边线的问题,先绘制区域，再绘制单元格边线
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
	 * 绘制由于拖动行列而产生的线条。
	 * 
	 * @param pos
	 *            int 拖动到达的位置
	 * @param isRowHeader
	 *            boolean 是否是拖动行
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
	 * 绘制报表分页产生的区域分界线条。
	 * @param g    图形上下文
	 * @param rMin 绘制区域起点的行号
	 * @param rMax 绘制区域终点的行号
	 * @param cMin 绘制区域起点的列号
	 * @param cMax 绘制区域终点的列号 
	 * @param isPrint 是否是绘制打印效果
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
	 * 绘制区域效果
	 * @param g    图形上下文
	 * @param rMin 绘制区域起点的行号
	 * @param rMax 绘制区域终点的行号
	 * @param cMin 绘制区域起点的列号
	 * @param cMax 绘制区域终点的列号 
	 * @param isPrint 是否是绘制打印效果
	 * @param opaque 单元区域数组
	 * @param isOpaque 单元区域中的单元格网格线是否是不透明的，是则不绘制，否则则需要绘制
	 */
	private void drawArea(Graphics g, int rMin, int rMax, int cMin, int cMax,
			boolean isPrint, ArrayList<AreaCell> opaque,boolean isOpaque) {
		Rectangle cellRect;
		//开始绘制区域
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
	 * 过滤多有区域单元，按照该单元对应的绘制器是否透明分为两个数组。前者为非透明渲染器，后者为 透明渲染器。
	 * 
	 * @param list
	 *            要过滤的多有区域单元
	 * @return ArrayList[] 两个数组，前者为非透明渲染器，后者为 透明渲染器。
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
			// //注意，对于所有无法获得渲染器的单元都认为是非透明单元，他背后的非区域单元不会被绘制。
			// //所以，避免将空值的透明区域单元放入数据模型中。
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
	

	//选择区域绘制格式
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
	 * 绘制锚点。
	 * @deprecated
	 */
	private void paintAnchor(Graphics g) {
		CellPosition cellPos = getCellsModel().getSelectModel()
				.getAnchorCell();
		if (cellPos == null) {
			return;
		}
		
		//在这个单元的内部区域绘制一条蓝色的界限表示是一个Anchor单元.
//		 AreaPosition area = m_cellsPane.getDataModel().getCombinedCellArea(cellPos);
		
		 Rectangle rect = m_cellsPane.getCellRect(getCellsModel().getSelectModel().getSelectedArea(), false);
		 
		//如果有边线,设置颜色为边线的颜色亮度改变,其他设置为黑色的边框.
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
	 * 绘制单个单元
	 * @param g 图形上下文
	 * @param cellRect 单元的边框
	 * @param row 单元行
	 * @param column 单元列
	 * @param bPrint 是否打印
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
						cellRect.x, cellRect.y, w, h, false);  //liyyy， 参数改为false，绘制时不作组件校验。

			} catch (Throwable e) {
				// 这里捕捉例外，解决jdk的bug：http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4138921
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
	 * 绘制区域单元。
	 * 
	 * @param g
	 *            图形上下文
	 * @param cellRect
	 *            区域边框
	 * @param cc
	 *            区域单元
	 * @param bPrint
	 *            是否打印
	 * @param opaque
	 *            是否不透明
	 */
	private void paintArea(Graphics g, Rectangle cellRect, AreaCell cc,
			boolean bPrint,boolean opaque) {
//		AreaPosition aPos = cc.getArea();
//		int row = aPos.getStart().getRow();
//		int col = aPos.getStart().getColumn();
		
		//绘制区域的时候，需要将区域后的网格线擦除，否则绘制白色区域的时候，JLable不擦除网格线。
		if(opaque){
			Color oldColor = g.getColor();
			g.setColor(m_cellsPane.getBackground());
			g.fillRect(cellRect.x, cellRect.y, cellRect.width, cellRect.height);
			g.setColor(oldColor);
		}
		
		//绘制单元区。
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
									cellRect.height, false); //liyyy， 参数改为false，绘制时不作组件校验。
						} catch(Throwable e) {
							AppDebug.debug(e);
						}
					}
				}
			} else {
			CellRenderAndEditor re = m_cellsPane.getReanderAndEditor();
			Object value = cc == null ? null : cc.getValue();
			// TODO 需要详细考虑
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
								cellRect.height, false); // liyyy，
						// 参数改为false，绘制时不作组件校验。
					} catch (Throwable e) {
						AppDebug.debug(e);
					}

				}
			}
		}

//		}
			
		//开始绘制边线。   //.getCellFormat(aPos.getStart());//
//		Format format = getCellsModel().getRealFormat(aPos.getStart());//.getConbineFormat(aPos.getStart());
//		if (format != null && cc instanceof CombinedCell) {
//		    //区域的绘制很少有连着的,所以还是全部画 上和左 边线.
//			paintCellLine(g, format, cellRect, true, true);
//		}
	}

	/**
	 * 绘制一个单元的边线
	 * 
	 * @param g，绘图句柄
	 * @param format,单元的格式信息
	 * @param r,单元占据的区域，这个区域正好容纳在网格线之内。
	 * @param paintCeiling,是否绘制上端线条，为了提高绘制效率，通常只绘制右侧和下方的线条。绘制第一行为true
	 * @param paintLeft,否绘制左侧线条，为了提高绘制效率，通常只绘制右侧和下方的线条。绘制第一列为true
	 * @param colMargin,列间距
	 * @param rowMargin,行间距
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
		
		// 绘制斜线和反斜线
		draw2DLine(g2d, format.getLineType(Format.DIAGONAL_LINE), format
				.getLineColor(Format.DIAGONAL_LINE), startX, startY, endX, endY,
				Format.DIAGONAL_LINE);		
		
		draw2DLine(g2d, format.getLineType(Format.DIAGONAL2_LINE), format
				.getLineColor(Format.DIAGONAL2_LINE), endX, startY, startX, endY,
				Format.DIAGONAL2_LINE);				
		
	}
	/**
	 * 绘制边框线条。对于通常的实线，直接绘制或者填充，对于特殊效果的线条，使用Stroke绘制。 原因是Stroke绘制的效率比较低。
	 * 
	 * @param g 图形上下文
	 * @param line，线形
	 * @param lineColor，线条颜色
	 * @param x1，起点
	 * @param y1，起点
	 * @param x2，终点
	 * @param y2，终点
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
		 * 添加以下的分支判断，目的是提高绘制的效率。
		 */
		if (line == TableConstant.L_DASH   
				|| line == TableConstant.L_DOT
				|| line == TableConstant.L_DASHDOT) {
			//如果是虚线类型，那么先擦除网格线！并且设置Stroke
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
	 * 绘制一条Anchor线.它的规则是如果原来的线形是Line.L_SOLID2,Line.L_SOLID3,Line.L_SOLID4,
	 * 首先擦除以前绘制的边线,然后绘制一条特殊的线条;其他绘制粗线即可.
	 * 
	 * @param g 图形上下文
	 *            Graphics2D
	 * @param lineStyle 线型
	 *            int 
	 * @param x1，起点
	 * @param y1，起点
	 * @param x2，终点
	 * @param y2，终点
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

	//**************************鼠标，键盘动作对应的事件响应的处理********************//

	/**
	 * <p>
	 * Title:该类响应上下左右的移动事件
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

		/** 水平移动量 */
		protected int dx;

		/** 垂直移动量 */
		protected int dy;

		/**
		 * @param dx
		 *            水平移动单元的数量.
		 * @param dy
		 *            垂直移动的单元的数量.
		 */
		protected NavigationalAction(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}
		/**
		 * 设置移动量
		 * 
		 * @param x
		 * @param y
		 */
		protected void setOffset(int x, int y) {
			this.dx = x;
			this.dy = y;
		}
        /**
         * 发生操作时的处理方法
         */
		public void actionPerformed(ActionEvent e) {
			CellsPane table = (CellsPane) e.getSource();
			SelectModel selModel = table.getSelectionModel();
			CellsModel dataModel = table.getDataModel();
			
			//得到当前的锚点的位置
			CellPosition anchorCell = selModel.getAnchorCell();
			if (anchorCell == null)
				return;
			int anchorRow = anchorCell.getRow();
			int anchorColumn = anchorCell.getColumn();
			//如果单元当前状态是编辑,并且编辑没有结束,不响应这些事件.
			if (table.isEditing()
					&& !table.getTablePane().getCellEditor().stopCellEditing()) {
				return;
			}
			/**
			 * 移动是否到达无限表的模型最大位置? 如果是,需要判断是否可以调整模型的大小;
			 * 移动需要判断是否移动到达视图的边缘?如果是,需要移动视图的位置;
			 */
			int newRow, newCol;
			newRow = anchorRow + dy;
			newCol = anchorColumn + dx;
			//首先判断初始焦点的单元是否是一个组合单元,如果是,需要根据当前启动的方向来调整Anchor的位置.
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

	/** 对于Pageup，Pagedown的响应。 */
	private static class PagingAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private boolean forwards;
		private PagingAction(boolean forwards) {
			this.forwards = forwards;
		}
		/**
         * 发生操作时的处理方法
         */
		public void actionPerformed(ActionEvent e) {
			CellsPane table = (CellsPane) e.getSource();
			//首先计算出来视图的位置，然后计算焦点移动到达那个位置。
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
			//只是考虑垂直方向的翻页。
			if (forwards) { //向下翻页
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
			// @edit by wangyga at 2009-3-4,下午07:09:36
			table.paginalData();
		}
	}

	/** 对于Ctrl＋A */
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
	 * 编辑取消命令.对应Esc键的情况,删除当前的编辑器.
	 */
	private static class CancelEditingAction extends AbstractAction {
	 
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			CellsPane table = (CellsPane) e.getSource();
			table.removeEditor();
			BorderPlayRender.stopPlay(table);//停止动画，add by wangyga
		}
	}

	/**
	 * 开始编辑的动作.对应Enter键,将焦点赋予编辑对象
	 */
	private static class StartEditingAction extends AbstractAction {
	 
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			CellsPane table = (CellsPane) e.getSource();
			if (!table.hasFocus()) { //检查当前的焦点是否在编辑组件之上
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
			//得到选择的焦点
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
	 * 处理焦点事件的代码。
	 */
	private class FocusHandler implements FocusListener {
		/**
		 * 获得焦点的时候绘制焦点单元
		 * 
		 * @param e
		 */
		public void focusGained(FocusEvent e) {
			repaintAnchorCell();
		}

		/**
		 * 失去焦点的时候绘制失去焦点的单元
		 * 
		 * @param e
		 */
		public void focusLost(FocusEvent e) {
			repaintAnchorCell();
			//midify by wangyga 停止动画
			BorderPlayRender.stopPlay(m_cellsPane);
		
//			doneFullScreenDraw = false;
		}
        /**
         * 重新绘制锚点所在单元
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
	 * 该事件主要响应Home和End以及他们的组合.
	 */
	private static class HomeEndAction extends NavigationalAction {
		private boolean ctrlPressed, home;

		/**
		 * 
		 * @param bCtrlPress
		 *            ctrl是否按下.
		 * @param bHome
		 *            是home还是end.true为Home.
		 */
		protected HomeEndAction(boolean bCtrlPress, boolean bHome) {
			super(0, 0);
			ctrlPressed = bCtrlPress;
			home = bHome;
		}
		/**
         * 发生操作时的处理方法
         */
		public void actionPerformed(ActionEvent e) {
			//首先变化焦点,然后变换视图.
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

	/** 此处处理拷贝，粘贴事件 */
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
	 * 获取CellsModel
	 */
	private CellsModel getCellsModel(){
	    return m_cellsPane.getDataModel();
	}
	/**
	 * 获取RowModel
	 */
	private HeaderModel getRowModel(){
	    return getCellsModel().getRowHeaderModel();
	}
	/**
	 * 获取ColModel
	 */
	private HeaderModel getColModel(){
	    return getCellsModel().getColumnHeaderModel();
	}
}