package com.ufsoft.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.event.EventManager;
import com.ufida.zior.event.EventService;
import com.ufida.zior.view.IDockingContainer;
import com.ufida.zior.view.Viewer;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.report.undo.CellValueUndo;
import com.ufsoft.table.IVerify.VerifyType;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.header.Header;
import com.ufsoft.table.header.HeaderModel;
import com.ufsoft.table.re.BorderPlayRender;
import com.ufsoft.table.re.CellRenderAndEditor;
import com.ufsoft.table.re.PriorityMouseHandle;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * <p>
 * Title: 显示表格单元的组件。
 * </p>
 * <p>
 * Description: 该组件是一个表格控件中显示表格单元的部分。
 */

public class CellsPane extends JComponent implements Scrollable,
		CellsModelListener, CellEditorListener, SelectListener {

	private static final long serialVersionUID = 7444496238277292689L;

	// *********************属性*********************//
	// 数据模型部分：
	/** 表格的数据模型。该模型在构建表格的时候被初始化，不能为空，其中维护对于行列模型的引用 */
	private CellsModel dataModel;

	// 表格的属性部分：
	/** 网格线的颜色 */
	private Color gridColor;

	/** 接口Scrollable使用，提供可视区域的大小 */
	private Dimension preferredViewportSize;
	/** CellsPane对应的TablePane */
	private TablePane m_tablePane;// 建议为了区别，CellsPane对象简称cp，TablePane对象简称tp，
									// UFOTable简称table。

	/**
	 * 渲染器和编辑器的缓存管理
	 */
	private CellRenderAndEditor renderAndEditor;

	/** 是否显示行列标签 */
	private boolean showRowHeader, showColHeader;
	
	private IDockingContainer m_container = null;
	
	private UfoReport m_report = null;

	
	// 本CellsPane局部的扩展绘制器和编辑器
	//v55临时处理。考虑使用局部CellRenderAndEditor，v6重构。 liuyy+
	private List<SheetCellRenderer> local_extSheetCellRenderer = new Vector<SheetCellRenderer>();
	private List<SheetCellEditor> local_extSheetCellEditor = new Vector<SheetCellEditor>();
		
	// @edit by ll at 2009-5-14,上午10:51:43 增加私有状态，优先级最高
	private int m_operationState = -1;

	/**
	 * 右键菜单
	 */
	// private CellsPanePopupMenu m_popupMenu = null;

	// *********************构造函数*******************//
	/**
	 * 构建几个表格组件。
	 * 
	 * @param dataModel
	 *            数据模型
	 * @param showRowHeader
	 *            显示行标题
	 * @param showColHeader
	 *            显示列标题
	 */
	public CellsPane(CellsModel dataModel, boolean showRowHeader,
			boolean showColHeader) {
		// 每次实例CellsPane时，恢复显示比例缺省值，保证显示比例设置不影响其他再打开的报表。liuyy. 2007-04-26
		TableStyle.setViewScale(DefaultSetting.DEFAULT_VIEWSCALE);

		this.showRowHeader = showRowHeader;
		this.showColHeader = showColHeader;
		setDataModel(dataModel);
		init();

		// m_popupMenu = new CellsPanePopupMenu();
	}

	// public CellsPanePopupMenu getPopupMenu(){
	// return m_popupMenu;
	// }

	/**
	 * 初始化表格组件方法：初始化编辑器和渲染器，设置当前组件的UI
	 */
	private void init() {
		setOpaque(true);
		renderAndEditor = CellRenderAndEditor.getInstance();//new CellRenderAndEditor();
		setUI(CellsPaneUI.createUI(this));
	}

	// *********************UI设置***********************************
	/**
	 * 得到当前组件的UI
	 * 
	 * @return 当前组件的UI
	 */
	public CellsPaneUI getUI() {
		return (CellsPaneUI) ui;
	}

	/**
	 * 设置当前组件的UI
	 * 
	 * @param ui
	 *            当前组件的UI
	 */
	public void setUI(CellsPaneUI ui) {
		if (this.ui != ui) {
			super.setUI(ui);
			repaint();
		}
	}

	/**
	 * 设置当前CellsPane对应的TablePane。
	 * 
	 * @param tp
	 *            TablePane
	 */
	public void setTablePane(TablePane tp) {
		if (m_tablePane != tp) {
			m_tablePane = tp;
		}
	}

	/**
	 * 得到本CellsPane对应的TablePane。
	 * 
	 * @return TablePane
	 */
	public TablePane getTablePane() {
		return m_tablePane;
	}

	// *************************************************
	// 表格属性的操作
	// *****************************************************
	/**
	 * 设置表格的数据模型。
	 * 如果当前表格组件有旧的数据模型则删除旧数据模型上的CellsModelListener和SelectModelListener
	 * 
	 * @param newModel
	 *            CellsModel 新的表格的数据模型。
	 */
	public void setDataModel(CellsModel newModel) {
		if (newModel == null) {
//			throw new IllegalArgumentException();
			return;
		}
		if (newModel != this.dataModel) {
			
			//清除undo操作。
			newModel.resetUndoManager();
			
			CellsModel oldModel = this.dataModel;
			this.dataModel = newModel;
			// 卸载旧模型上的所有的监听器，添加到新的模型中
			if (oldModel != null) {
				oldModel.removeCellsModelListener(this);
				oldModel.getSelectModel().removeSelectModelListener(this);
			}
			newModel.addCellsModelListener(this);
			if (getSelectionModel() != null) {
				getSelectionModel().addSelectModelListener(this);
			}

		}
		 
		// 保证产生临时行列。解决绘制时因为临时行列不够而界面上出现空白区域。
		if (this.dataModel.isInfinite()) {
			Dimension screenDim = TableStyle.getScreenDim();
			int minRowSize = screenDim.height / TableStyle.ROW_HEIGHT + 1;
			int minColSize = screenDim.width / TableStyle.COLUMN_WIDTH + 1;
			
			boolean oldDirty= this.dataModel.isDirty();
			HeaderModel rowModel = this.dataModel.getRowHeaderModel();
			HeaderModel colModel = this.dataModel.getColumnHeaderModel();
			if (minRowSize > rowModel.getTempCount()) {
				rowModel.setTempCount(minRowSize);
				this.dataModel.setDirty(oldDirty);
			}
			if (minColSize > colModel.getTempCount()) {
				colModel.setTempCount(minColSize);
				this.dataModel.setDirty(oldDirty);
			}
		}
		
		
		// 为了简化代码，就不作属性改变的通知了。
		resizeAndRepaint();
	}

	/**
	 * 得到表格的数据模型。
	 * 
	 * @return CellsModel 表格的数据模型。
	 */
	public CellsModel getDataModel() {
		return dataModel;
	}

	// /**
	// * 设置右键菜单
	// *
	// * @param pMenu
	// * @deprecated 直接使用父类的add
	// */
	// public void addPopupMenu(JPopupMenu pMenu) {
	// if (m_popup != null) {
	// remove(m_popup);
	// }
	// this.m_popup = pMenu;
	// if (m_popup != null) {
	// add(m_popup);
	// }
	// }
	/**
	 * 是否显示行标题
	 * 
	 * @return boolean
	 */
	public boolean isShowRowHeader() {
		return showRowHeader;
	}

	/**
	 * 是否显示列标题
	 * 
	 * @return boolean
	 */
	public boolean isShowColHeader() {
		return showColHeader;
	}

	/**
	 * 设置表格的网格线颜色
	 * 
	 * @param gridColor
	 *            表格的网格线颜色
	 * @exception IllegalArgumentException
	 *                参数不许为空
	 */
	public void setGridColor(Color gridColor) {
		checkParam(gridColor);
		if (!gridColor.equals(this.gridColor)) {
			this.gridColor = gridColor;
			repaint();
		}
	}

	/**
	 * 得到网格线颜色
	 * 
	 * @return Color 网格线颜色
	 */
	public Color getGridColor() {
		return gridColor;
	}

	/**
	 * 为当前的一个表格组件设置读写权限的模型。
	 * 
	 * @param newAuth
	 *            单元的读写权限管理
	 */
	public void setCellsAuthorization(CellsAuthorization newAuth) {
		CellsAuthorization auth = getDataModel().getCellsAuth();
		if (newAuth == auth) {
			return;
		}
		getDataModel().setCellsAuth(newAuth);
	}

	// /**
	// * 更改单元权限。调用前应该注意需要首先初始化auth字段。
	// *
	// * @param row
	// * @param col
	// * @param type
	// */
	// public void authorize(int row, int col, int type) {
	// CellsAuthorization auth = getDataModel().getCellsAuth();
	// if (auth == null) {
	// return;
	// }
	// auth.authorize(row, col, type);
	// //此处添加代码修改相应的单元。
	// getDataModel().freshCell(row, col);
	// }

	/**
	 * 得到控件上编辑器和渲染器。
	 * 
	 * @return CellRenderAndEditor
	 */
	public CellRenderAndEditor getReanderAndEditor() {
		return renderAndEditor;
	}

	/**
	 * 得到选择模型
	 * 
	 * @return SelectModel
	 */
	public SelectModel getSelectionModel() {
		return dataModel.getSelectModel();
	}

	/**
	 * 得到表格组件的缺省尺寸
	 * 
	 * @return Dimension
	 */
	public Dimension getPreferredSize() {
		// 如果是无限表，首先要保证缺省的表空间可以满足，如果发生了拖动，那么需要根据临时的表空间来计算。
		// 如果是有限表，它的尺寸就是行高列宽的总和
		CellsModel cellsModel = getDataModel();
		if(cellsModel == null){
			return new Dimension(0,0);
		}
		HeaderModel rowM = cellsModel.getRowHeaderModel();
		HeaderModel colM = cellsModel.getColumnHeaderModel();

		return new Dimension(colM.getTotalSize(), rowM.getTotalSize());
	}

	// ***********************************************
	// 以下为该类的私有方法
	// *********************************************
	/**
	 * 不允许参数为空
	 * 
	 * @param o
	 */
	private void checkParam(Object o) {
		if (o == null) {
			throw new IllegalArgumentException();
		}
	}

	// ***********************************************
	// 以下方法是为了使用的便利,对于引用的数据模型进行操作
	// *********************************************
	/**
	 * 得到单元内容
	 * 
	 * @param row
	 *            行号
	 * @param col
	 *            列号
	 * @return Cell
	 */
	public Cell getCell(int row, int col) {
		return dataModel.getCell(row, col);
	}

	/**
	 * 判断某个单元是否被选中
	 * 
	 * @param row
	 * @param col
	 * @return boolean
	 */
	public boolean isCellSelected(int row, int col) {
		return getSelectionModel().isSelected(row, col);
	}

	/**
	 * 判断某个单元是否是选择模型中的锚点。
	 * 
	 * @param row
	 * @param col
	 * @return boolean
	 */
	public boolean isCellAnchor(int row, int col) {
		return getSelectionModel().isAnchorCell(row, col);
	}

	/**
	 * 得到某一列的宽度。
	 * 
	 * @param col
	 *            列
	 * @return int
	 */
	public int getColumnWidth(int col) {
		HeaderModel m = getDataModel().getColumnHeaderModel();
		return m.getSize(col);
		// Header h = m.getHeader(col);
		// return h == null ? m.getPreferredSize() : h.getSize();
	}

	/**
	 * 得到行间距
	 * 
	 * @return int
	 */
	public int getRowMargin() {
		return getDataModel().getRowHeaderModel().getMargin();
	}

	/**
	 * 得到列间距
	 * 
	 * @return int
	 */
	public int getColumnMargin() {
		return getDataModel().getColumnHeaderModel().getMargin();
	}

	/**
	 * 得到模型中包含的行的数量
	 * 
	 * @return int
	 */
	public int getRowCount() {
		return dataModel.getRowNum();
	}

	/**
	 * 得到模型中包含的列的数量
	 * 
	 * @return int
	 */
	public int getColumnCount() {
		return dataModel.getColNum();
	}

	/**
	 * 得到行高
	 * 
	 * @param index
	 * @return int
	 */
	public int getRowHeight(int index) {
		HeaderModel m = dataModel.getRowHeaderModel();
		return m.getSize(index);
		// Header h = m == null ? null : m.getHeader(index);
		// return h == null ? m.getPreferredSize() : h.getSize();
	}

	/**
	 * 得到某一列的名称
	 * 
	 * @param column
	 *            列号
	 * @return Object 名称
	 */
	public Object getColumnValue(int column) {
		Header h = getDataModel().getColumnHeaderModel().getHeader(column);
		return h == null ? null : h.getValue();
	}

	/**
	 * 得到某一行的名称
	 * 
	 * @param row
	 *            行号
	 * @return Object 名称
	 */
	public Object getRowValue(int row) {
		Header h = getDataModel().getRowHeaderModel().getHeader(row);
		return h == null ? null : h.getValue();
	}

	/**
	 * 根据坐标判断当前位置在哪一列。越界返回－1。
	 * 
	 * @param point
	 *            物理坐标
	 * @return int 根据坐标判断当前位置在哪一列。越界返回－1。
	 */
	public int columnAtPoint(Point point) {
		return getDataModel().getColumnHeaderModel()
				.getIndexByPosition(point.x);
	}

	/**
	 * 根据坐标判断当前位置在哪一行,越界返回－1。注意在无限表的情况中，临时行列不存在于行列模型中， 所以需要判断临时行列的位置。
	 * 
	 * @param point
	 *            物理坐标
	 * @return 根据坐标判断当前位置在哪一行。越界返回－1。
	 */
	public int rowAtPoint(Point point) {
		return dataModel.getRowHeaderModel().getIndexByPosition(point.y);
	} 
	
	

	/**
	 * 得到某个单元区域所处矩形
	 * 
	 * @param area
	 *            单元区域（如A3:A7）
	 * @param includeSpacing
	 *            是否有边线
	 * @return Rectangle 该单元的矩形区域（象素）
	 */
	public Rectangle getCellRect(IArea area, boolean includeSpacing) {
		return getDataModel().getCellRect(area, includeSpacing);
	}

	// ***********************************************************//
	// 关于选择模型
	// ******************************************************
	/**
	 * 选择所有单元
	 */
	public void selectAll() {
		// 如果有单元在编辑，需要停止
		if (isEditing()) {
			removeEditor();
		}
		getSelectionModel().setSelectAll(true);
	}

	// ***********************表格数据模型接口的实现**********************

	// *************************关于编辑的处理*****************************
	/**
	 * 单元是否可以编辑.需要判断当前单元是否在组合单元区域中，如果是，需要判断这个组合单元左上角。
	 * 
	 * @param row
	 *            行号
	 * @param col
	 *            列号
	 * @return boolean
	 */
	public boolean isCellEditable(int row, int col) {
		CellsModel model = getDataModel();
		CombinedCell cc = model.getCombinedAreaModel().belongToCombinedCell(row, col);
		if (cc != null) {
			row = cc.getArea().getStart().getRow();
			col = cc.getArea().getStart().getColumn();
		}

		if (isCellReadable(row, col)) {
			CellsAuthorization auth = getDataModel().getCellsAuth();
			return auth == null ? true : auth.isWritable(row, col);
		} else {
			return false;
		}
	}

	private boolean isCellReadable(int row, int col) {
		CellsAuthorization auth = getDataModel().getCellsAuth();
		return auth == null ? true : auth.isReadable(row, col);
	}

	/**
	 * 编辑某个单元
	 * 
	 * @param row
	 *            行
	 * @param column
	 *            列
	 * @exception IllegalArgumentException
	 * @return boolean 如果无法编辑，返回false。
	 */
	public boolean editCellAt(int row, int column) {
		return editCellAt(row, column, null);
	}

	/**
	 * 编辑某个单元，是编辑过程中主要调用的方法 处理过程：获取编辑单元(如果是合并单元则获得合并单元的其始单元)的编辑器，并注册监听
	 * 调用prepareEditor方法获取该单元的编辑组件，并设置其编辑边界 设置对应的tablePane的编辑器，编辑组件，编辑行和列等
	 * 
	 * @param row
	 *            行
	 * @param column
	 *            列
	 * @param e
	 *            编辑事件(如单元值改变，单元格式改变等)
	 * @exception IllegalArgumentException
	 * @return boolean 如果无法编辑，返回false。
	 */
	public boolean editCellAt(int row, int column, EventObject e) {
		CellPosition pos = CellPosition.getInstance(row,
				column);
		
		// 组合单元其他非首单元格转换为首单元格。
		CombinedCell cc = getDataModel().getCombinedAreaModel().belongToCombinedCell(row, column);
		if (cc != null) {
			row = cc.getArea().getStart().getRow();
			column = cc.getArea().getStart().getColumn();
		}
		// 编辑器非空，并且中断当前编辑成功。
		SheetCellEditor oldEditor = m_tablePane.getCellEditor();
		if (oldEditor != null && !oldEditor.stopCellEditing()) {
			return false;
		}
		if (!isCellEditable(row, column)) {
			return false;
		}
		SheetCellEditor editor = getCellEditor(pos);
		if (editor != null && editor.isCellEditable(e)) {
			Component editorComp = prepareEditor(editor, row, column);
			if (editorComp == null) {
				removeEditor();
				return false;
			}
			Rectangle cellRect = getCellRect(pos, false); // 坐标相对于cellsPane
			if (cc != null && !this.getParent().getBounds().contains(cellRect)) {// 主要是适应组合单元跨分栏的问题
																					// 。
				cellRect = SwingUtilities.convertRectangle(this, cellRect,
						m_tablePane);
				cellRect = cellRect.intersection(this.getParent().getBounds());
				cellRect = SwingUtilities.convertRectangle(m_tablePane,
						cellRect, this);
			}
			// add by 王宇光 2008-4-2 获得区域边框动画效果的绘制器，控制动画停止
			BorderPlayRender.stopPlay(this);
			// end
			editorComp.setBounds(cellRect);
			add(editorComp);
			editorComp.validate();
			m_tablePane.setEditorComp(editorComp);
			m_tablePane.setCellEditor(editor);
			m_tablePane.setEditingRow(row);
			m_tablePane.setEditingColumn(column);
			editor.addCellEditorListener(this);
			return true;
		}
		return false;
	}

	/**
	 * 是否有单元正在编辑
	 * 
	 * @return boolean
	 */
	public boolean isEditing() {
		return (m_tablePane.getCellEditor() == null) ? false : true;
	}

	/**
	 * 判断当前鼠标点击的单元是否按照客户的需求处理鼠标事件.
	 * 
	 * @param row
	 *            行号
	 * @param col
	 *            列号
	 * @param oldValue
	 *            Object 原来设置在当前单元上Value
	 * @param mEvent
	 *            鼠标事件
	 * @return Object 事件处理结束后
	 */
	public Object priorityMouseEvent(int row, int col, Object oldValue,
			MouseEvent mEvent) {
		PriorityMouseHandle p = getPriorityMouseEvent();
		return p == null ? null : p.priorityMouseEvent(row, col, oldValue,
				mEvent);
	}

	/**
	 * 判断当前鼠标事件是否优先处理
	 * 
	 * @param row
	 *            点击位置
	 * @param col
	 *            点击位置
	 * @param e
	 *            鼠标事件
	 * @return boolean
	 */
	public boolean hasPriority(int row, int col, MouseEvent e) {
		PriorityMouseHandle p = getPriorityMouseEvent();
		return p == null ? false : p.hasPriority(row, col, e);
	}

	/**
	 * 设置用户定义的鼠标优先处理事件
	 * 
	 * @param newPriority
	 */
	public void setPriorityMouseEvent(PriorityMouseHandle newPriority) {
		getDataModel().setPriorityMouseEvent(newPriority);
	}

	/**
	 * 得到用户定义的鼠标优先处理事件
	 * 
	 * @return PriorityMouseHandle
	 */
	public PriorityMouseHandle getPriorityMouseEvent() {
		return getDataModel().getPriorityMouseEvent();
	}

	//
	// **************************CellEditorListener接口实现*************
	//

	/**
	 * 编辑过程结束后的处理方法，是CellEditorListener的事件处理方法 处理过程：
	 * 调用setValueAt()，设置单元格的值为编辑器的值 removeEditor()，移除tablePane的编辑器
	 * 如果需要自动换行处理则调用getFitHeaderSize()
	 * 
	 * @param e
	 *            ChangeEvent
	 */
	public void editingStopped(ChangeEvent e) {
		SheetCellEditor editor = m_tablePane.getCellEditor();
		if (editor == null) {
			return;
		}
		
		Object value = editor.getCellEditorValue();
		final CellPosition pos = CellPosition.getInstance(m_tablePane
				.getEditingRow(), m_tablePane.getEditingColumn());
		
		setValueAt(value, pos.getRow(), pos.getColumn());
		
		removeEditor();
		
		IufoFormat format = (IufoFormat) getDataModel().getCellFormat(pos);
		if (format != null && format.isFold()) {// 处理自动折行时的高度。
			int preferRowHeight = getFitHeaderSize(true, pos.getRow());
			getDataModel().getRowHeaderModel().getHeader(pos.getRow())
					.autoSetSize(preferRowHeight);
		}
		
	}

	/**
	 * 编辑取消的时候调用
	 * 
	 * @param e
	 *            ChangeEvent
	 */
	public void editingCanceled(ChangeEvent e) {
		removeEditor();
	}

	/**
	 * 设置单元值
	 * 
	 * @param value
	 * @param row
	 * @param col
	 */
	public void setValueAt(Object value, int row, int col) {
		CellPosition pos = CellPosition.getInstance(row, col);

		Object oldValue = dataModel.getCellValue(pos);

//		Cell oldCell = dataModel.getCell(pos);
		 
		dataModel.setCellValue(row, col, value);
		
//		Cell newCell = dataModel.getCell(pos);

		CellValueUndo ue = new CellValueUndo(pos, oldValue, value);
		
		dataModel.fireUndoHappened(ue);

	}

	// *********************接口Scrollable的实现*******************//
	/**
	 * 设置组件显示的视口大小
	 * 
	 * @param size
	 *            Dimension 组件显示的视口大小.
	 */
	public void setPreferredScrollableViewportSize(Dimension size) {
		preferredViewportSize = size;
	}

	/**
	 * 得到组件显示的视口大小
	 * 
	 * @return 组件显示的视口大小
	 */
	public Dimension getPreferredScrollableViewportSize() {
		return preferredViewportSize;
	}

	/**
	 * 得到每次滚动一个单位时移动的象素大小。该方法在用户每次请求一个单位移动时调用
	 * 
	 * @param visibleRect
	 *            Rectangle 视口的可见区域大小
	 * @param orientation
	 *            int 移动方向：SwingConstants.VERTICAL，SwingConstants.HORIZONTAL
	 * @param direction
	 *            int 上、左移动为负数；下、右移动为正数
	 * @return int 移动的象素
	 * @see Scrollable#getScrollableUnitIncrement
	 */
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		JViewport view = (JViewport) getParent();
		if (view == null) {
			return 20;
		} else {
			Point vP = view.getViewPosition();
			// 得到当前视图左上角单元的坐标，然后根据该单元的大小决定移动的大小
			boolean bHor = orientation == SwingConstants.HORIZONTAL;
			HeaderModel headerModel = bHor ? getDataModel()
					.getColumnHeaderModel() : getDataModel()
					.getRowHeaderModel();
			int pos = bHor ? vP.x : vP.y;
			int index = 0;
			int newPos = 0;
			if (direction > 0) {
				index = headerModel.getIndexByPosition(pos) + 1;
				newPos = headerModel.getPosition(index);
				return newPos - pos;
				
			} else {
				index = headerModel.getIndexByPosition(pos) - 1; 
				if(index < 0){
					return 0;
				}
				newPos = headerModel.getPosition(index);
				return pos - newPos;
			}
		}
	}

	/**
	 * 得到移动一个滑块时，移动的距离。通常是移动一页，所以返回可见区域的高度或者宽度
	 * 
	 * @param visibleRect
	 * @param orientation
	 * @param direction
	 * @return int 移动一页的距离
	 */
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		// 这里代码需要修改，例如移动的时候应该是保证每次左上角都是一个完整的单元
		int n = 0;
		switch (orientation) {
		case SwingConstants.VERTICAL:
			n = visibleRect.height;
			break;
		case SwingConstants.HORIZONTAL:
			n = visibleRect.width;
			break;

		default:
			break;
		}
		return n;
	}

	/**
	 * 视口的宽度是否由表格的宽度决定。当表格是无限表的时候，返回false;当视口是有限表的时候， 需要根据表格在修改尺寸时的规则决定。
	 * 
	 * @return boolean 视口的宽度是否由表格的宽度决定
	 */
	public boolean getScrollableTracksViewportWidth() {
		// 需要修改此处代码。
		return false;
	}

	/**
	 * 视口的高度是否由表格的高度决定。当表格是无限表的是否，返回false;当视口是有限表的时候， 需要根据表格在修改尺寸时的规则决定。
	 * 
	 * @return boolean 视口的高度是否由表格的高度决定
	 */
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	/**
	 * 重新布局绘制
	 */
	private void resizeAndRepaint() {
		revalidate();
		repaint();
	}
	
	public void requestFocus(){
//		AppDebug.debug("CellsPane.requestFocus()");
		super.requestFocus();
	}

	public void repaint() {
		// AppDebug.debug("CellsPane关闭repaint（）调用。");
		super.repaint();
	}

	/**
	 * 得到在某个单元进行绘制的组件 处理过程： 首先判断是否有读的权限； 然后判断该单元是否被选中,是否是选择事件的起始点；
	 * 得到当前位置单元使用的所有渲染器(背景渲染器，扩展表格渲染器，扩展格式渲染器，单元值类型对应的渲染器)；
	 * 得到组件的所有渲染器对应的绘制组件Component
	 * 
	 * @param row
	 *            the row of the cell to render, where 0 is the first row
	 * @param column
	 *            the column of the cell to render, where 0 is the first column
	 * @param bPrint
	 * @return the <code>Component</code> under the event location
	 */
	public Component[] prepareRenderer(int row, int column, boolean bPrint) {
		Cell cell = getCell(row, column);

		// if(c == null || c.getFormat() == null){
		// return null;
		// }

		Object value = cell == null ? null : cell.getValue();
		// 是否可视
		boolean bRead = isCellReadable(row, column);
		boolean isSelected = isCellSelected(row, column) && !bPrint;
		boolean isAnchor = isCellAnchor(row, column) && !bPrint;
		ArrayList<Component> list = new ArrayList<Component>();

		SheetCellRenderer renderer = null;
		Component com = null;

		// liuyy, 最底层绘制背景色。为解决扩展绘制器可能有背景的问题, 将单元格背景与值分开绘制.
		com = CellRenderAndEditor.getBackgroundRenderer().getCellRendererComponent(
				this, value, isSelected, isAnchor, row, column, cell);
		list.add(com);

		boolean isHasKeyOrMeasure = false;

		//扩展绘制器
		SheetCellRenderer[] extRenderers = getExtSheetRender();
		for (SheetCellRenderer scr : extRenderers) {
			Component eachCom = scr.getCellRendererComponent(this, value,
					isSelected, isAnchor, row, column, cell);
			if (eachCom != null) {
				list.add(eachCom);
				isHasKeyOrMeasure = true;
			}
		}
		
		// 解决在格式设计状态下在报表边框外设置关键字和指标不能打印显示的问题 modify by guangang 2007-11-6
		if (cell != null) {
			if (isHasKeyOrMeasure) {
				cell.setPrint(true);
			} else {
				cell.setPrint(false);
			}
		}
		// modify end

		// 扩展格式
		if (bRead && cell != null && cell.getExtFmtSize() > 0) {
			String[] extNames = cell.getExtNames();
			for (int i = 0; i < extNames.length; i++) {
				Object extFmt = cell.getExtFmt(extNames[i]);
				if (extFmt == null) {
					continue;
				}
				if (bPrint
						&& extFmt instanceof ICellVerify
						&& ((ICellVerify) extFmt).isVerify(cell,
								VerifyType.UNSHOW_WHEN_PRINT)) {
					continue;
				}
				renderer = renderAndEditor.getRender(extNames[i]);

				Component aCom = renderer.getCellRendererComponent(this, cell
						.getExtFmt(extNames[i]), isSelected, isAnchor, row,
						column, cell);
				if (aCom != null) {
					list.add(aCom);
				}
			}
		}

		// liuyy, 将单元格的值最后绘制。
		if (bRead) {
			// add by guogang 2007-7-3 单元格如果设置条件格式则用Double Render
			Format format = getDataModel().getFormat(
					CellPosition.getInstance(row, column));
			if (format != null && format.isCondition()) {
				renderer = renderAndEditor.getRender(Double.class);
			} else {
				renderer = renderAndEditor.getRender(value == null ? null
						: value.getClass());
			}
		} else {
			renderer = CellRenderAndEditor.getUnreadableRenderer();
		}
		com = renderer.getCellRendererComponent(this, value, isSelected,
				isAnchor, row, column, cell);
		list.add(com);

		return (Component[]) list.toArray(new Component[0]);
	}

	/**
	 * 得到某个合并组合单元的绘制控件. 除了边框外的东东。
	 * 
	 * @param cc
	 *            CombinedCell
	 * @param bPrint
	 * @return Component[]
	 */
	public Component[] prepareRenderer(AreaCell cc, boolean bPrint) {
		// TODO 需要详细修改！
		if (cc instanceof CombinedCell) {
			int row = cc.getArea().getStart().getRow();
			int column = cc.getArea().getStart().getColumn();
			return prepareRenderer(row, column, bPrint);
		} else {
			return null;
		}

	}

	// public Component prepareRenderer(CombinedCell areaData) {
	// CellPosition cellStart = areaData.getArea().getStart();
	// boolean bRead = auth.isReadable(cellStart.getRow(),
	// cellStart.getColumn());
	// SheetCellRenderer renderer = bRead ?
	// renderAndEditor.getRender(areaData.getValue()) :
	// renderAndEditor.getUnreadableRenderer();
	// boolean isSelected = isCellSelected(cellStart.getRow(),
	// cellStart.getColumn());
	// boolean IsAnchor = isCellAnchor(cellStart.getRow(),
	// cellStart.getColumn());
	// return renderer.getCellRendererComponent(this, areaData.getValue(),
	// isSelected, IsAnchor,
	// cellStart.getRow(),
	// cellStart.getColumn());
	// }

	
	/**
	 * 得到某个位置的编辑器
	 */
	public SheetCellEditor getCellEditor(CellPosition pos) {
		
		SheetCellEditor editor = CellRenderAndEditor.getInstance().getEditor(
				getDataModel(), pos);
		
		if(getOperationState() == ReportContextKey.OPERATION_INPUT){
			return editor;
		}
		SheetCellEditor[] arrExtSheetEditors = getExtSheetEditor();
		for (int i = 0; i < arrExtSheetEditors.length; i++) {
			SheetCellEditor aEditor = arrExtSheetEditors[i];
			if (aEditor != null
					&& editor.getEditorPRI() < aEditor.getEditorPRI()
					&& aEditor.isEnabled(getDataModel(), pos)) {
				editor = aEditor;
			}
		}
		return editor;
	}

	/**
	 * 为某个单元准备编辑组件
	 * 
	 * @param editor
	 *            SheetCellEditor 编辑器
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 * @return Component 编辑组件
	 */
	public Component prepareEditor(SheetCellEditor editor, int row, int column) {
		Cell c = getCell(row, column);
		Object value = c == null ? null : c.getValue();
		boolean isSelected = isCellSelected(row, column);
		Component comp = editor.getTableCellEditorComponent(this, value,
				isSelected, row, column);
		if (comp instanceof JComponent) {
			JComponent jComp = (JComponent) comp;
			if (jComp.getNextFocusableComponent() == null) {
				jComp.setNextFocusableComponent(this);
			}
		}
		if (!isCellEditable(row, column)) {
			comp.setEnabled(false);
		}
		return comp;
	}

	/**
	 * 删除tablePane的编辑器及监听，编辑组件，编辑行和列.编辑过程结束后调用
	 */
	public void removeEditor() {
		SheetCellEditor editor = m_tablePane.getCellEditor();
		Component editorComp = m_tablePane.getEditorComp();
		if (editor != null) {
			editor.removeCellEditorListener(this);
			if (editorComp != null) {
				remove(editorComp);
			}
			int editingRow = m_tablePane.getEditingRow(), editingCol = m_tablePane
					.getEditingColumn();
			m_tablePane.setCellEditor(null);
			m_tablePane.setEditingColumn(-1);
			m_tablePane.setEditingRow(-1);
			m_tablePane.setEditorComp(null);
			repaint(getCellRect(CellPosition
					.getInstance(editingRow, editingCol), false));
			this.requestFocus();
		}
	}

	/**
	 * 
	 * @param row
	 * @param col
	 */
	private void repaintCell(int row, int col) {
		if (row < 0)
			row = 0;

		if (col < 0)
			col = 0;

		repaint(CellPosition.getInstance(row, col), true);
	}

	// *********************监听器注册*******************//
	// *********************接口SelectListener的实现*******************//
	/**
	 * 选择改变（鼠标锚点改变，选择单元改变）的处理方法，是SelectListener的事件处理方法 只负责绘制修改区域，不负责移动视图。
	 */
	public void selectedChanged(SelectEvent e) {
		if (e == null) {
			return;
		}
		
		//适配新框架 liuyy+
		EventManager eventManager = getEventManager();
		if(eventManager != null){//hr或者预算的应用eventManager可能为null
			eventManager.dispatch(e);
		}
		
		// System.out.println(DateUtil.getCurTimeWithMillisecond() + ": " +
		// e.getProperty());
		if (e.getProperty().equals(SelectEvent.ANCHOR_CHANGED)) {
			JViewport viewPort = (JViewport) getParent();
			if (viewPort == null) { // 保证视图中可以看到当前的焦点.
				return;
			}

			repaintCell(e.getOldAnchor().getRow(), e.getOldAnchor().getColumn());
			
			// repaintCell(e.getNewAnchorRow(),e.getNewAnchorCol());
			// viewPort.repaint();
			// end

		} else if (e.getProperty().equals(SelectEvent.SELECTED_CHANGE)) {
			//			
			// //得到最后选择区域的与焦点对角的的单元格位置。
			// CellPosition anchorCell = getSelectionModel().getAnchorCell();
			// AreaPosition dragSelectArea =
			// getSelectionModel().getDragSelectedArea();
			// if (dragSelectArea != null) {
			// CellPosition start = dragSelectArea.getStart();
			// CellPosition end = dragSelectArea.getEnd();
			// int row = start.getRow() == anchorCell.getRow() ? end
			// .getRow() : start.getRow();
			// int col = start.getColumn() == anchorCell.getColumn() ? end
			// .getColumn() : start.getColumn();
			// moveViewToDisplayRect(CellPosition.getInstance(row, col),true);
			// }

			
			
			AreaPosition[] changedArea = e.getChangedArea();
			if (changedArea == null) {
				return;
			}
			
			for (AreaPosition area : changedArea) {


				repaint(area, true);
			}
		}

	}

	/**
	 * 改变视图位置，使其显示区域（参数）的位置
	 * 
	 * @param area
	 *            区域
	 * @param includeSpacing
	 *            是否包括边框
	 */
	public void moveViewToDisplayRect(IArea area, boolean includeSpacing) {
		Rectangle rect = getCellRect(area, includeSpacing);
		moveViewToDisplayRect(rect);
		 
		
	}

	public void repaint(IArea[] areas, boolean includeSpacing) {
		for (IArea a : areas) {
			repaint(a, includeSpacing);
		}
	}

	/**
	 * 重新绘制区域的方法,禁止调用CellsPane.repaint()方法,还要做越界处理
	 * 
	 * @param area
	 *            区域
	 * @param includeSpacing
	 *            是否包括边框
	 */
	public void repaint(IArea area, boolean includeSpacing) {

		// 不执行绘制。liuyy+
		if (!getDataModel().isEnableEvent()) {
			AppDebug.debug("CellsModel禁止绘制：" + area);
			return;
		}
		
		// 保证产生临时行列。
		if (getDataModel().isInfinite()) {
			int row = area.getEnd().getRow(), col = area.getEnd().getColumn();
			HeaderModel rowModel = getDataModel().getRowHeaderModel();
			HeaderModel colModel = getDataModel().getColumnHeaderModel();
			if (row >= rowModel.getTempCount()) {
				rowModel.setTempCount(row + 1);
			}
			if (col >= colModel.getTempCount()) {
				colModel.setTempCount(col + 1);
			}
		}
		

		// 开始绘制。
		Graphics g1 = this.getGraphics();
		if (g1 == null) {
			return;
		}
		Rectangle paintRect = null;
		Rectangle viewRect = getViewRect();
		Rectangle rect = getCellRect(area, includeSpacing);

		// add by guogang 2008-4-21 为保证区域绘制的时候能够清除上、左边线，要扩大绘制区域到包括左、上的单元
		rect.x -= 1;
		rect.y -= 1;
		rect.width += 1;
		rect.height += 1;
 
		if (viewRect != null) {
			paintRect = rect.intersection(viewRect);
		} else {
			paintRect = rect;
		}
		
//		AppDebug.debug("绘制区域：" + area + ";数值区域：" + paintRect);
		
		// 确保在有锁定的情况下只绘制正确区域
		if (paintRect.width > 0 && paintRect.height > 0) {
			g1.setClip(paintRect);
			getUI().paint(g1, this);
		}

	}
	/**
	 * 获取可视区域
	 * @return
	 */
	private Rectangle getViewRect(){
		JViewport view = null;
		Rectangle viewRect = null;
		if (this.getParent() instanceof JViewport) {
			view = (JViewport) this.getParent();
		}
		if (view != null) {
			viewRect = view.getViewRect();
		}
		return viewRect;
		
	}

	/**
	 * 改变视图位置，使其显示矩形（参数）的位置。 矩形（参数）如果比视图大，则按左上角对齐显示。 modify by guogang
	 * 2007-12-13 当CellsPane绘制前调用SelectModel.setAnchorCell()触发selectChange事件
	 * 会引起不必要的重新定位问题，如公式追踪的指标定位问题
	 * 
	 * @param rect
	 *            Rectangle
	 */
	private void moveViewToDisplayRect(Rectangle rect) {
		JViewport view = (JViewport) this.getParent();
		SeperateLockSet lockset = getDataModel().getSeperateLockSet();

		Rectangle viewRect = view.getViewRect();
		if (viewRect.contains(rect)) {
			return;
		}
		// add by guogang
		if (!lockset.isFreezing()
				&& (viewRect.width == 0 || viewRect.height == 0)) {
			view.setViewPosition(new Point(rect.x, rect.y));
		} else {
			TablePane pane = (TablePane) view.getParent();
			view=pane.getRightDownView();
			if (view==null)
				view=pane.getDownView();
			if (view==null)
				view=pane.getRightView();
			if (view==null)
				view=pane.getMainView();
			viewRect = view.getViewRect();
			Rectangle unionRect = rect.union(viewRect);
			int dx = (viewRect.x != unionRect.x) ? (unionRect.x - viewRect.x)
					: (unionRect.width - viewRect.width);
			int dy = (viewRect.y != unionRect.y) ? (unionRect.y - viewRect.y)
					: (unionRect.height - viewRect.height);
			Point p = view.getViewPosition();
			
			if (pane != null
					&& pane.isFreezing()
					&& (pane.getSeperateRow() != 0 || pane.getSeperateCol() != 0)) {
				// 移动视图位置。
				int newX = Math.max(p.x + dx, pane.getSeperateX());
				int newY = Math.max(p.y + dy, pane.getSeperateY());
				if (view == pane.getMainView()) {
					// 当前视图是主视图时移动情况要视分栏情况而定。
					if (pane.getRightView() == null
							&& pane.getDownView() == null) {// 未分栏
						view.setViewPosition(new Point(newX, newY));
					} else if (pane.getRightView() != null
							&& pane.getDownView() == null) {// 左右分栏
						view.setViewPosition(new Point(0, newY));
					} else if (pane.getRightView() == null
							&& pane.getDownView() != null) {// 上下分栏
						view.setViewPosition(new Point(newX, 0));
					} else {// 上下左右都分栏。
						view.setViewPosition(new Point(0, 0));
					}
				}
				if (view == pane.getRightView()) {
					if (pane.getDownView() != null) {
						view.setViewPosition(new Point(newX, 0));
					} else {
						view.setViewPosition(new Point(newX, newY));
					}
				}
				if (view == pane.getDownView()) {
					if (pane.getRightView() != null) {
						view.setViewPosition(new Point(0, newY));
					} else {
						view.setViewPosition(new Point(newX, newY));
					}
				}
				if (view == pane.getRightDownView()) {
					view.setViewPosition(new Point(newX, newY));
				}
				// 确定当焦点越过seperate点时转换焦点组件。
				// rect = SwingUtilities.convertRectangle(this,rect,pane);
				// rect =
				//SwingUtilities.convertRectangle(pane,rect,pane.getMainView());
				newX = rect.x;
				newY = rect.y;
				if (newX >= pane.getSeperateX() && pane.getRightView() != null) { // 焦点应该在右视图或者下右视图
					if (newY >= pane.getSeperateY()
							&& pane.getDownView() != null) {
						// 因为每一个视图都会收到selectedChange事件，所以只有焦点是自己时才相应。
						if (this == pane.getRightDownView().getView()
								&& !this.isEditing()) {
							this.requestFocus();
						}
					} else {
						if (this == pane.getRightView().getView()
								&& !this.isEditing()) {
							this.requestFocus();
						}
					}
				} else { // 焦点应该在主视图或者下视图
					if (newY >= pane.getSeperateY()
							&& pane.getDownView() != null) {
						if (this == pane.getDownView().getView()
								&& !this.isEditing()) {
							this.requestFocus();
						}
					} else {
						if (this == pane.getMainView().getView()
								&& !this.isEditing()) {
							this.requestFocus();
						}
					}
				}
			} else {
				view.setViewPosition(new Point(p.x + dx, p.y + dy));
			}

		}
		

		paginalData();
		
		// if(pane.getDownView() != null){
		// pane.getDownView().requestFocus();
		// }
		// if(pane.getRightView() != null){
		// pane.getRightView().requestFocus();
		// }
		// if(pane.getColumnHeader2() != null){
		// pane.getColumnHeader2().requestFocus();
		// }
		//				
		// view.requestFocus();
	}

	// *********************接口CellsModelListener的实现*******************//

	/*
	 * 单元格改变的事件处理方法，接口CellsModelListener的实现
	 * @
	 * @param event CellsEvent
	 * 
	 * @see
	 * com.ufsoft.table.CellsModelListener#cellsChanged(com.ufsoft.table.CellsEvent
	 * )
	 * modify by guogang 2009-3-30 适配5.6的事件处理
	 */
	public void cellsChanged(CellsEvent event) {
		
		if (dataModel.isEnableEvent() == false) {
			return;
		}
		//先向其他派发
		EventManager eventManager = getEventManager();
		if(eventManager != null){//hr或者预算的应用eventManager可能为null
			eventManager.dispatch(event);
		}
			
		SheetCellEditor oldEditor =null;
		if(m_tablePane!=null){
			oldEditor= m_tablePane.getCellEditor();
		}
		
		if (oldEditor != null && event != null
				&& event.getMessage() == CellsEvent.SIZE_CHANGED) {
			oldEditor.stopCellEditing();
		}
		if (oldEditor != null && event != null
				&& event.getMessage() == CellsEvent.MARGIN_CHANGED) {
			oldEditor.stopCellEditing();
		}
		int type = event.getMessage();
		setSize(getPreferredSize());
		if (type == CellsEvent.VALUE_CHANGED) {
			IArea area = event.getArea();
			if (area != null) {
				// modify by guogang 2008-4-30 解决在诸如插入单元格的时候边线绘制的问题
				Rectangle rect = getCellRect(area, true);
				rect.x--;
				rect.y--;
				rect.width++;
				rect.height++;
				repaint(rect);
			}
		} else {// if (type == CellsEvent.SIZE_CHANGED || type ==
				// CellsEvent.MARGIN_CHANGED) {
			resizeAndRepaint();
		}

	}

	// *********************接口CellEditorListener的实现*******************//
	// *********************私有方法*******************//

	/**
	 * 用户UI发起的动作，改变表格的选择模型。不包括模型发起的事件（比如区域公式要求全选导致的选择改变）。 负责移动视图，不负责绘制。
	 * 
	 * @param row
	 *            行
	 * @param col
	 *            列
	 * @param isControlDown
	 *            Control键是否按下
	 * @param isShiftDown
	 *            Shift键是否按下
	 * @param bDrag
	 *            是否有拖动
	 */
	public void changeSelectionByUser(int row, int col, boolean isControlDown,
			boolean isShiftDown, boolean bDrag) {

		getSelectionModel().changeSelectionByUser(row, col, isControlDown,
				isShiftDown, bDrag);

		moveViewToDisplayRect(CellPosition.getInstance(row, col), true);

	}
	
/**
 * 分页数据填充
 * liuyy
 */
	public void paginalData(){
		 
		int state = getOperationState();
		if(state == ReportContextKey.OPERATION_FORMAT){
			return;
		}
		
		CellsModel dataModel = getDataModel();
		if(dataModel == null){// @edit by wangyga at 2009-2-24,下午08:30:42
			return;
		}
		Rectangle viewRect =  getVisibleRect(); 
		
		int rMin = dataModel.getRowHeaderModel().getIndexByPosition(viewRect.y);
		int rMax = dataModel.getRowHeaderModel().getIndexByPosition(viewRect.y + viewRect.height);
  		
		if(rMin < 0){
			rMin = 0;
		}
		if(rMax < rMin){
			rMax = rMin;
		}
		AreaPosition area = AreaPosition.getInstance(CellPosition.getInstance(
				rMin, 0), CellPosition.getInstance(rMax, 255));
		 
		ExAreaModel eaModel = ExAreaModel.getInstance(dataModel);

		eaModel.paginalData(area);
		
		
	}

	/**
	 * 这里返回一个和当前组件数据模型,选择模型,行列模型完全相同的组件
	 * 
	 * @return Object
	 */
	public Object clone() {
		CellsPane cp = new CellsPane(dataModel, showRowHeader, showColHeader);
		cp.setRenderAndEditor(renderAndEditor);
		cp.setTablePane(getTablePane());
		cp.setActionMap(getActionMap());
		cp.setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT));
		cp.setPrivateOperationState(this.m_operationState);
		return cp;
	}

	/**
	 * 重载父类的键盘处理方法。首先当前组件检查输入的鼠标事件是否已经绑定，如果是按照父类的方法处理。
	 * 如此可以处理在InputMap中定义的键盘输入。其他，例如一个字符输入，此时需要激活当前单元的编辑器，
	 * 将这个键盘事件交割被激活的编辑组件处理。目的是在某个单元敲击字符时，激活编辑器。
	 * 该方法在Jdk1.3上才有效，在这个版本之下super.processKeyBinding不可见。
	 * 
	 * @param ks
	 * @param e
	 * @param condition
	 * @param pressed
	 * @return boolean
	 */
	protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
			int condition, boolean pressed) {
		// 首先调用缺省的方法,只是检查Keymap中是否有响应的键盘事件的绑定.
		// 以下内容可以根据当前的Jdk版本切换.
		boolean retValue = super.processKeyBinding(ks, e, condition, pressed);// 父类只处理了KeyPress和WHEN_ANCESTOR_OF_FOCUSED_COMPONENT的组合
																				// 。
		// 当键盘事件没有被父类处理,并且不是当前组件获得焦点的鼠标事件,在下面处理.
		// 主要是得到当前的Anchor,并且在该位置添加编辑组件.

		// add at 2006-9-21,处理在单元内容编辑状态，按等号弹出公式定义框问题
		if (!retValue && condition == WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
				&& m_tablePane != null && m_tablePane.getEditorComp() != null
				&& m_tablePane.getEditorComp().hasFocus()
				&& e.getKeyChar() == '=')
			return true;

		if (!retValue
				&& // 当父类没有处理
				condition == WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
				&& // 当满足焦点的条件
				hasFocus()
				&& // 当修饰键是SHIFT键或没有修饰键
				(e.getModifiers() == InputEvent.SHIFT_MASK || e.getModifiers() == 0)
				&& // 当不是ActionKey
				!e.isActionKey() && e.getKeyChar() != KeyEvent.VK_DELETE
				&& e.getKeyChar() != KeyEvent.VK_ENTER// 当不是DELETE键.
				&& e.getKeyChar() != KeyEvent.VK_ESCAPE//当不是ESC键
		) {
			Component editorComp = m_tablePane.getEditorComp();
			if (e == null || e.getID() != KeyEvent.KEY_TYPED) {
				return false;
			}
			int code = e.getKeyCode();
			if (code == KeyEvent.VK_SHIFT || code == KeyEvent.VK_CONTROL
					|| code == KeyEvent.VK_ALT) {
				return false;
			}
			
			if (editorComp == null) {
				CellPosition anchorCell = getSelectionModel().getAnchorCell();
				int anchorRow = anchorCell.getRow();
				int anchorColumn = anchorCell.getColumn();
				if (anchorRow != -1 && anchorColumn != -1 && !isEditing()) {
					if (!editCellAt(anchorRow, anchorColumn)) {
						return false;
					}
				}
				editorComp = m_tablePane.getEditorComp();
				if (editorComp == null) {
					return false;
				}
			}
			// 如果编辑组件是JComponent, 将键盘事件交给组件处理.
			if (editorComp instanceof JComponent) {
//				if (editorComp instanceof JTextComponent) {
//					((JTextComponent) editorComp).setText("");
//				}
				editorComp.requestFocus();
				getToolkit().getSystemEventQueue()
						.postEvent(
								new KeyEvent(editorComp, e.getID(),
										e.getWhen(), e.getModifiers(), e
												.getKeyCode(), e.getKeyChar()));
				return true;
			}
		}
		return retValue;
	}

	/**
	 * 此处插入方法描述。 创建日期：(2004-5-19 9:04:21)
	 * 
	 * @param re
	 *            com.ufsoft.table.re.CellRenderAndEditor
	 */
	public void setRenderAndEditor(CellRenderAndEditor re) {
		renderAndEditor = re;
	}

	/**
	 * 仅用于测试输出当前面板的标识。
	 * 
	 * @return String
	 */
	public String getIdentifier() {
		TablePane tablePane = (TablePane) this.getParent().getParent();
		if (this.getParent() == tablePane.getMainView()) {
			return "MainView";
		} else if (this.getParent() == tablePane.getRightView()) {
			return "RightView";
		} else if (this.getParent() == tablePane.getDownView()) {
			return "DownView";
		} else if (this.getParent() == tablePane.getRightDownView()) {
			return "RightDownView";
		} else {
			return "UnknowView";
		}
	}

	/**
	 * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
	 * modify by guogang 2009-3-30 适配5.6的事件处理
	 */
	public String isSupport(int source, EventObject e)
			throws ForbidedOprException {
		return TableUtilities.newCheckEvent(source, e);
	}

	// /**
	// * 处理鼠标滚动。
	// * @param e void
	// */
	// public void processMouseWheelMoved(MouseWheelEvent e) {
	// Container parent = getParent();
	// if(parent != null && parent instanceof JViewport){
	// JViewport view = (JViewport) parent;
	// int distance = getScrollableUnitIncrement(view.getViewRect(),
	// SwingConstants.VERTICAL,e.getWheelRotation());
	// Point p = view.getViewPosition();
	// p.translate(0,distance*e.getWheelRotation());
	// if(p.y < view.getHeight()){
	// view.setViewPosition(p);
	// }
	// }
	// }
	/**
	 * 从未使用
	 */
	public boolean isPermitViewPos(Point viewPos, boolean isRow) {
		if (!(getDataModel().isInfinite())) {
			if (getParent() != null && getParent() instanceof JViewport) {
				JViewport viewPort = (JViewport) getParent();
				Point p = (Point) viewPos.clone();
				p.translate(viewPort.getWidth() - 1, viewPort.getHeight() - 1);
				if ((isRow && rowAtPoint(p) >= getRowCount())
						|| (!isRow && columnAtPoint(p) >= getColumnCount())) {
					return false;
				}
			}

		}
		return true;
	}

	public void registExtSheetRenderer(SheetCellRenderer renderer) {
		if (renderer == null) {
			return;
		}
		if(!local_extSheetCellRenderer.contains(renderer)){
			local_extSheetCellRenderer.add(renderer);
		} 
	}


	/**
	 * 获取全部扩展绘制器
	 * 包括全局和局部
	 * @return
	 */
	private SheetCellRenderer[] getExtSheetRender() {
		List<SheetCellRenderer> renders = new ArrayList<SheetCellRenderer>();
		renders.addAll(Arrays.asList(CellRenderAndEditor.getInstance().getExtSheetRender()));
		renders.addAll(local_extSheetCellRenderer);
		return renders.toArray(new SheetCellRenderer[0]);
	}

	public void registExtSheetEditor(SheetCellEditor editor) {
		if (editor == null) {
			return;
		}
		if(!local_extSheetCellEditor.contains(editor)){
			local_extSheetCellEditor.add(editor);
		}
	}

	/**
	 * 获取全部扩展编辑器
	 * 包括全局和局部
	 * @return
	 */
	private SheetCellEditor[] getExtSheetEditor() {
		List<SheetCellEditor> editors = new ArrayList<SheetCellEditor>();
		editors.addAll(Arrays.asList(CellRenderAndEditor.getInstance().getExtSheetEditor()));
		editors.addAll(local_extSheetCellEditor);
		return editors.toArray(new SheetCellEditor[0]);
	}

	/**
	 * 计算合适的行或列高
	 * 
	 * @param isRow
	 *            是否是行
	 * @param index
	 *            行号
	 * @return int 新的行高,如果isRow为true则返回当前行的所有单元的最大行高，否则返回所有行的最大行高
	 */
	public int getFitHeaderSize(boolean isRow, int index) {
		int fitHeaderSize = -1;
		if (isRow) {
			int colNum = getDataModel().getColNum();
			for (int col = 0; col < colNum; col++) {
				CellPosition pos = CellPosition.getInstance(index, col);
				Cell cell = getDataModel().getCell(pos);
				Object value = cell == null ? null : cell.getValue();
				if (value != null) {
					JComponent comp = getValueComp(value, index, col, cell);
					int preferSize = comp.getPreferredSize().height;
					fitHeaderSize = Math.max(fitHeaderSize, preferSize);
					remove(comp);
				}
			}
			fitHeaderSize = (int) (fitHeaderSize * 1.13);// 修正计算出来的行高偏小的问题。
		} else {
			int rowNum = getDataModel().getRowNum();
			for (int row = 0; row < rowNum; row++) {
				CellPosition pos = CellPosition.getInstance(row, index);
				Cell cell = getDataModel().getCell(pos);
				Object value = cell == null ? null : cell.getValue();
				if (value != null) {
					JComponent comp = getValueComp(value, row, index, cell);
					int preferSize = comp.getPreferredSize().width;
					fitHeaderSize = Math.max(fitHeaderSize, preferSize);
					remove(comp);
				}
			}
		}
		
		if(fitHeaderSize > 800){
			fitHeaderSize = 800;
		}
		return fitHeaderSize;
	}

	/**
	 * 获得对应单元上值（参数）类型的编辑组件
	 * 
	 * @param value
	 *            值
	 * @param row
	 *            对应单元的行
	 * @param col
	 *            对应单元的列
	 * @return JComponent 编辑组件
	 */
	private JComponent getValueComp(Object value, int row, int col, Cell cell) {
		SheetCellRenderer render = getReanderAndEditor().getRender(
				value.getClass());
		JComponent valueComp = (JComponent) render.getCellRendererComponent(
				this, value, false, false, row, col, cell);
		Rectangle rect = getCellRect(CellPosition.getInstance(row, col), true);
		valueComp.setBounds(rect);
		add(valueComp);
		return valueComp;
	}

	@Override
	/*
	 * 为TablePane的绘制增加 modify by guogang 2008-1-24
	 */
	public Graphics getComponentGraphics(Graphics g) {
		return super.getComponentGraphics(g);
	}
	
	
	
	public int getOperationState(){
		// @edit by ll at 2009-5-14,上午10:53:21
		if(m_operationState != -1)
			return m_operationState;
		
		IDockingContainer container =getContainer();
		if(container == null){
			return ReportContextKey.OPERATION_FORMAT;
		}
		if(container instanceof UfoReport){
			return ((UfoReport) container).getOperationState();
		}

		IContext context = getContext();
		if(context == null){
			throw new IllegalArgumentException();
		}
		
		if(context.getAttribute(ReportContextKey.OPERATION_STATE) == null){
			throw new IllegalArgumentException("OPERATION_STATE is NUll");
		}
		
		int state = new Integer("" + context.getAttribute(ReportContextKey.OPERATION_STATE));
		
		return state;
	}

	public IContext getContext(){
		//兼容旧模型
		Viewer vr = (Viewer)SwingUtilities.getAncestorOfClass(Viewer.class, this);
		if(vr != null){
			return vr.getContext();
		}
		if(getContainer() == null){
			throw new IllegalArgumentException("Container is null.");
		}
		return getContainer().getContext();
	}
	
	public EventManager getEventManager(){
		EventManager em = EventService.getEventManager();
		if(em != null){
			return em;
		}
		IDockingContainer container = getContainer();
		if(container == null){
			return null;
		}
		return container.getEventManager();
	}
	
	private IDockingContainer getContainer(){
		if(m_container != null){
			return m_container;
		}
		m_container = (IDockingContainer)SwingUtilities.getAncestorOfClass(IDockingContainer.class, this);
		
		return m_container;
	}
	
	//此方法预算那边调用
	public UfoReport getReport() {
		if (m_report == null) {
			m_report = (UfoReport)SwingUtilities.getAncestorOfClass(UfoReport.class, this);		
		}
		return m_report;
	}
	public void setPrivateOperationState(int state){
		m_operationState = state;
	}
}