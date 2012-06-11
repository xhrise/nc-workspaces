package com.ufsoft.table;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import javax.print.PrintService;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import nc.ui.pub.beans.UIViewport;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.event.EventManager;
import com.ufsoft.report.component.StyleUtil;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.event.PropertyChangeEventAdapter;
import com.ufsoft.table.header.Header;
import com.ufsoft.table.header.HeaderModel;
import com.ufsoft.table.header.HeaderNode;
import com.ufsoft.table.header.HeaderTree;
import com.ufsoft.table.header.TableHeader;
import com.ufsoft.table.print.PrintPreView;
import com.ufsoft.table.print.PrintPreviewDialog;
import com.ufsoft.table.print.PrintSet;
import com.ufsoft.table.re.SheetCellEditor;

/**
 * <p>Title:表格容器面板 </p>
 * <p>Description: 这个面板是作为表格控件的容器，管理其中各个控件的显示，布局。如果当前的面板需要
 * 处理窗口的拆分,那么窗口中容纳的组件的数量将会增加.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 * @see TableLayout
 */

public class TablePane extends JComponent implements TableConstants, Pageable {

	private static final long serialVersionUID = -434057970337762938L;


	/**
	 *垂直滚动条的显示规则，缺省为需要时显示滚动条
	 */
	private int m_nVScrollBarPolicy;

	/**
	 *水平滚动条的显示规则，缺省为需要时显示滚动条
	 */
	private int m_nHScrollBarPolicy;

	/**
	 * 容器中的主视图，通常就是容纳表格的视图。
	 */
	private JViewport m_MainView;

	/**
	 * 列头部视图容器。
	 */
	private JViewport m_ColumnHeaderView;

	/**
	 * 行头部视图容器。
	 */
	private JViewport m_RowHeaderView;
	/**
	 * 表格的行列树是否可见
	 */
	private boolean m_bShowTree;
	/**
	 * 表格的行列树
	 */
	private JViewport m_RowTreeView, m_ColTreeView;

	/**
	 * 垂直滚动条
	 */
	private TableScrollBar m_VScrollBar;

	/**
	 * 水平滚动条
	 */
	private TableScrollBar m_HScrollBar;

	/**
	 * 容纳页签的视图容器
	 */
	private JViewport m_PageView;

	/**
	 * 页签和滚动条之间的滑块
	 */
	private SlideBox m_SlideBox;

	/**
	 * 四个角落的组件
	 */
	private Component m_upperLeft, m_upperRight, m_lowerLeft, m_lowerRight;

	/**
	 * 是否显示页签
	 */
//	private boolean m_bShowPageMark;

	/**
	 * 记录是否是无限表的属性。这一点在点击滚动条的按钮时需要判断是否扩展控件范围。
	 */
	private boolean m_bInfinite;

	/**
	 * 关联的表格组件
	 */
	private CellsPane m_CellsPane;

	//*********当表格处于窗口拆分的状态时,需要添加视口组件.
	/**
	 * 拆分出来的行标题视图
	 */
	private JViewport m_RowHeaderView2;

	/**
	 * 拆分出来的列标题视图
	 */
	private JViewport m_ColumnHeaderView2;

	/**
	 * 水平方向拆分时候,右侧出现的视图.
	 */
	private JViewport m_RightView;

	/**
	 * 垂直方向拆分时候,下方出现的视图
	 */
	private JViewport m_DownView;

	/**
	 * 沿着两个方向拆分出现的右下方的视图
	 */
	private JViewport m_RightDownView;

	/**
	 * 水平方向拆分,出现在右侧的水平滚动条
	 */
	private TableScrollBar m_HScrollBar2;

	/**
	 * 垂直方向拆分,出现在下方的垂直滚动条
	 */
	private TableScrollBar m_VScrollBar2;
	private JViewport m_RowTreeView2, m_ColTreeView2;

	/**分割视图的分割条*/
	private SeperatorBar m_horBar, m_verBar;
	private SeperatorBox m_crossBox;

	/** 在编辑过程中，正在编辑的组件*/
	private Component editorComp;
	/** 在编辑过程中，使用的编辑器*/
	private SheetCellEditor cellEditor;
	/** 正在编辑的位置. */
	private int editingColumn = -1;
	/** 正在编辑的位置. */
	private int editingRow = -1;
	/**行列标签的可视*/
	private boolean m_rowHeaderShow = true, m_colHeaderShow = true;

	/**
	 * 打印预览对户框的实例
	 */
	private PrintPreviewDialog m_printPreView;

	//****************************************************************************
	/**
	 * 初始化一个容器面板。
	 * @param c Component 在面板中央显示的组件
	 * @param vsbPolicy int 垂直滚动策略，取值为TableConstants中关于滚动规则的常量
	 * @param hsbPolicy int 水平滚动策略，取值为TableConstants中关于滚动规则的常量
	 * @param showPageMark boolean 是否显示页签
	 * @param bInfinite 是否是无限表
	 * @see TableConstants
	 */
	public TablePane(CellsPane c, int vsbPolicy, int hsbPolicy,
			boolean bInfinite) {
		this.m_bInfinite = bInfinite;
		setVScrollBarPolicy(vsbPolicy);
		setHScrollBarPolicy(hsbPolicy);
		setMainviewView(c);
//		this.m_bShowPageMark = showPageMark;
		c.setTablePane(this);
		installAllComponent();
		addMouseWheelListener();
		setBackground(StyleUtil.reportUncoveredColor);
	}

	/**
	 *  添加指定的鼠标轮侦听器，接收此组件发出的鼠标轮事件
	 */
	private void addMouseWheelListener() {
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(getCellsModel() == null){// @edit by wangyga at 2009-2-24,下午08:12:23
					return;
				}
				Component comp = getComponentAt(e.getPoint());
				if (comp instanceof JViewport) {
					JViewport view = (JViewport) comp;
					//当锁定状态，且下视图存在时，上和主视图不接受滚动，且把滚动事件转给下视图
					if (getSeperateLockSet().isFreezing() && m_DownView != null) {
						if (m_DownView.getView() instanceof CellsPane) {
							CellsPane cellsPane = (CellsPane) m_DownView
									.getView();
							int distance = cellsPane
									.getScrollableUnitIncrement(m_DownView
											.getViewRect(),
											SwingConstants.VERTICAL, e
													.getWheelRotation());
							Point p = m_DownView.getViewPosition();
							if (p.y + distance * e.getWheelRotation() < getSeperateLockSet()
									.getSeperateY()) {
								return;
							}
							view = m_DownView;
						}
					}
					if (view.getView() instanceof CellsPane) {
						CellsPane cellsPane = (CellsPane) view.getView();
						int distance = cellsPane.getScrollableUnitIncrement(
								view.getViewRect(), SwingConstants.VERTICAL, e
										.getWheelRotation());
						Point p = view.getViewPosition();
						int oldY=p.y;
						p.translate(0, distance * e.getWheelRotation());
						
						if (p.y + view.getViewRect().height >= view
								.getViewSize().height||oldY==p.y) {
							return;
						}
						view.setViewPosition(p);
						
						//分页处理。 liuyy+
						cellsPane.paginalData();
					}
				}
			}

		});

	}

	/**
	 * 添加所有子组件。当一个报表构建的时候，客户只需要构建CellsPane，Header等，滚动条，ViewPort
	 * 的构建在这里自动构建。
	 */
	private void installAllComponent() {
		TableLayout layout = new TableLayout();
		setLayout(layout);
		//添加滚动条
		m_VScrollBar = new TableScrollBar(TableScrollBar.VERTICAL, getCells(),
				m_bInfinite);
		add(m_VScrollBar, VSCROLLBAR);
		m_HScrollBar = new TableScrollBar(TableScrollBar.HORIZONTAL,
				getCells(), m_bInfinite);
		add(m_HScrollBar, HSCROLLBAR);
		//设置行标题，列标题，主视图，页签
		m_RowHeaderView = new UIViewport();
		m_ColumnHeaderView = new UIViewport();
		add(m_RowHeaderView, ROWHEADERVIEW);
		add(m_ColumnHeaderView, COLUMNHEADERVIEW);
		//对于一个表格组件，需要将其中维护的行列信息放入面板。
		// @edit by wangyga at 2009-2-24,下午04:02:50 支持可以空model
		CellsModel cellsModel = m_CellsPane.getDataModel();	
		if(cellsModel != null){
			if (m_CellsPane.isShowColHeader()) {			
				TableHeader colHeaderView = new TableHeader(cellsModel.getColumnHeaderModel(), Header.COLUMN,
						m_CellsPane);
				m_ColumnHeaderView.setView(colHeaderView);
			}
			if (m_CellsPane.isShowRowHeader()) {
				TableHeader rowHeaderView = new TableHeader(cellsModel.getRowHeaderModel(), Header.ROW,
						m_CellsPane);
				m_RowHeaderView.setView(rowHeaderView);
			}
		}
		
		setMainView(new UIViewport());
		if (m_CellsPane != null) {
			setMainviewView(m_CellsPane);
		}
		// @edit by wangyga at 2009-3-5,上午11:27:59 暂时适配新的UI框架
		if(!(this instanceof ReportTable)){
			m_PageView = new UIViewport();
			add(m_PageView, PAGEVIEW);
		}
		m_SlideBox = SlideBox.createDefaultSlideBox(SlideBox.HORIZONTAL);
		add(m_SlideBox, SLIDEBOX);

		//添加左上角的全选区域
		SelectAllCmp selAllCmp = new SelectAllCmp(this);
		setCorner(UPPER_LEFT, selAllCmp);

		setOpaque(true);
		setUI(TablePaneUI.createUI(this));
		layout.syncWithScrollPane(this);

	}

	/**
	 * 新的UI框架在构造时，cellsModel为空，不会初始化TableHeader,所以在设置cellsModel之后，再调用此方法
	 * @create by wangyga at 2009-3-5,上午11:24:58
	 *
	 */
	public void initTableHeader(){
		CellsModel cellsModel = m_CellsPane.getDataModel();	
		if(cellsModel != null){
			if (m_CellsPane.isShowColHeader()) {			
				TableHeader colHeaderView = new TableHeader(cellsModel.getColumnHeaderModel(), Header.COLUMN,
						m_CellsPane);
				m_ColumnHeaderView.setView(colHeaderView);
			}
			if (m_CellsPane.isShowRowHeader()) {
				TableHeader rowHeaderView = new TableHeader(cellsModel.getRowHeaderModel(), Header.ROW,
						m_CellsPane);
				m_RowHeaderView.setView(rowHeaderView);
			}
		}
	}
	
	/**
	 * 设置表格的行列树是否可见
	 * @param bVisible
	 */
	public void setHeaderTreeVisible(boolean bVisible) {
		boolean old = m_bShowTree;
		m_bShowTree = bVisible;
		if (m_RowTreeView != null) {
			m_RowTreeView.setVisible(m_bShowTree);
		}
		if (m_ColTreeView != null) {
			m_ColTreeView.setVisible(m_bShowTree);
		}
		if (old != m_bShowTree) {
			revalidate();
		}
	}

	/**
	 * 设置树模型
	 * @param node
	 * @param type
	 */
	public void setHeaderTreeModel(HeaderNode node, int type) {
		if (node == null) {
			throw new IllegalArgumentException();
		}
		TableHeader header = type == Header.ROW ? (TableHeader) m_RowHeaderView
				.getView() : (TableHeader) m_ColumnHeaderView.getView();
		HeaderTree tree = new HeaderTree(type, node, header.getModel());
		if (type == Header.ROW) {
			if (m_RowTreeView == null) {
				m_RowTreeView = new UIViewport();
				add(ROW_TREE, m_RowTreeView);
			}
			m_RowTreeView.setView(tree);
			if (m_RowTreeView2 != null) {
				HeaderTree tree2 = new HeaderTree(tree.getTreeModel());
				m_RowTreeView2.setView(tree2);
			}
		} else if (type == Header.COLUMN) {
			if (m_ColTreeView == null) {
				m_ColTreeView = new UIViewport();
				add(COL_TREE, m_ColTreeView);
			}
			m_ColTreeView.setView(tree);
			if (m_ColTreeView2 != null) {
				HeaderTree tree2 = new HeaderTree(tree.getTreeModel());
				m_ColTreeView.setView(tree2);
			}
		} else {
			throw new IllegalArgumentException();
		}
		//重新添加所有的监听器。
		getUI().uninstallListeners();
		getUI().installListeners();
		tree.getTreeModel().refresh(null);
		revalidate();

	}

	/**
	 * 设置垂直分割的位置.
	 * @param ySep int 垂直分割的象素位置
	 */
	public void setSeperateY(int ySep) {
		if (m_DownView != null) { //分栏组件已经构造了。
			if (ySep == getSeperateLockSet().getSeperateY()) {
				return;
			}
		} else {
			//初始化并且添加由于分割产生的组件
			m_DownView = new UIViewport();
			CellsPane view = (CellsPane) m_MainView.getView();
			CellsPane down = (CellsPane) view.clone();
			m_DownView.setView(down);
			m_RowHeaderView2 = new UIViewport();
			TableHeader rowH = (TableHeader) m_RowHeaderView.getView();
			TableHeader rowH2 = (TableHeader) rowH.clone();
			m_RowHeaderView2.setView(rowH2);
			m_VScrollBar2 = new TableScrollBar(TableScrollBar.VERTICAL, down,
					m_bInfinite);
			m_verBar = SeperatorBar.createSeperatorBar(true, this);
			if (m_RowTreeView != null) {
				if (m_RowTreeView2 == null) {
					m_RowTreeView2 = new UIViewport();
				}
				HeaderTree tree = (HeaderTree) m_RowTreeView.getView();
				m_RowTreeView2.setView(tree.cloneSelf());
				add(ROW_TREE2, m_RowTreeView2);
			}
			add(HOR_SLIDEBOX, m_verBar);
			add(DOWN_VIEW, m_DownView);
			add(ROWHEADERVIEW2, m_RowHeaderView2);
			add(VSCROLLBAR2, m_VScrollBar2);
			addCrossView();
		}
		getSeperateLockSet().setSeperateY(ySep);
		//为新添加的组件注册监听器
		TablePaneUI ui = getUI();
		ui.uninstallListeners();
		ui.installListeners();
		revalidate();
	}

	/**
	 * 设置垂直分割的位置.
	 * @param sepX int 水平分割的像素位置
	 */
	public void setSeperateX(int sepX) {
		if (m_RightView != null) {
			if (sepX == getSeperateLockSet().getSeperateX()) {
				return;
			}
		} else {
			//初始化并且添加组件
			m_RightView = new UIViewport();
			CellsPane view = (CellsPane) m_MainView.getView();
			CellsPane rightPane = (CellsPane) view.clone();
			m_RightView.setView(rightPane);
			m_ColumnHeaderView2 = new UIViewport();
			TableHeader header = (TableHeader) m_ColumnHeaderView.getView();
			TableHeader header2 = (TableHeader) header.clone();
			m_ColumnHeaderView2.setView(header2);
			m_HScrollBar2 = new TableScrollBar(TableScrollBar.HORIZONTAL,
					rightPane, m_bInfinite);
			m_horBar = SeperatorBar.createSeperatorBar(false, this);
			if (m_ColTreeView != null) {
				if (m_ColTreeView2 == null) {
					m_ColTreeView2 = new UIViewport();
				}
				HeaderTree tree = (HeaderTree) m_ColTreeView.getView();
				m_ColTreeView2.setView(tree.cloneSelf());
				add(COL_TREE2, m_ColTreeView2);
			}
			//      HeaderTree
			add(RIGHT_VIEW, m_RightView);
			add(COLUMNHEADERVIEW2, m_ColumnHeaderView2);
			add(HSCROLLBAR2, m_HScrollBar2);
			add(VER_SLIDEBOX, m_horBar);
			addCrossView();
		}
		getSeperateLockSet().setSeperateX(sepX);
		//为新添加的组件注册监听器
		TablePaneUI ui = getUI();
		ui.uninstallListeners();
		ui.installListeners();
		revalidate();
	}

	/**
	 * 添加右下侧的窗体
	 */
	private void addCrossView() {
		if (m_RightView != null && m_DownView != null
				&& m_RightDownView == null) {
			m_RightDownView = new UIViewport();
			CellsPane view = (CellsPane) m_MainView.getView();
			CellsPane rdView = (CellsPane) view.clone();
			m_crossBox = SeperatorBox.createSeperatorBox();
			m_RightDownView.setView(rdView);
			add(RIGHTDOWN_VIEW, m_RightDownView);
			add(CROSS_SLIDEBOX, m_crossBox);
		}
	}

	/**
	 * 当行为负数时,行不动.
	 * 当列为负数时,列不动.
	 * 当都是0时,设置为2,2.
	 * @param row
	 * @param col void
	 */
	public void setSeperatePos(int row, int col) {
		if (row <= 0 && col <= 0) {
			//@edit by liuyy at 2008-12-26 上午10:21:40
//			row = 2;
//			col = 2;
			return;
		}
		
		if (row >= 0) {
			getSeperateLockSet().setSeperateRow(row);
			setSeperateY(getCells().getDataModel().getRowHeaderModel()
					.getPosition(row));
		}
		if (col >= 0) {
			getSeperateLockSet().setSeperateCol(col);
			setSeperateX(getCells().getDataModel().getColumnHeaderModel()
					.getPosition(col));
		}
		firePropertyChange("seperate2lock", null, null);		
	}

	/**
	 * 取消分栏表格.将由于分栏参数的组件删除。
	 */
	public void cancelSeperate() {
		getSeperateLockSet().setSeperateRow(0);
		getSeperateLockSet().setSeperateCol(0);
		//删除监听器
		TablePaneUI ui = getUI();
		ui.uninstallListeners();
		//删除组件
		clearHeaderView(m_RowHeaderView2);
		clearHeaderView(m_ColumnHeaderView2);
		clearCellsView(m_RightDownView);
		clearCellsView(m_DownView);
		clearCellsView(m_RightView);
		if (m_HScrollBar2 != null) {
			remove(m_HScrollBar2);
		}
		if (m_VScrollBar2 != null) {
			remove(m_VScrollBar2);
		}
		if (m_horBar != null) {
			remove(m_horBar);
		}
		if (m_verBar != null) {
			remove(m_verBar);
		}
		if (m_crossBox != null) {
			remove(m_crossBox);
		}
		clearTreeView(m_RowTreeView2);
		clearTreeView(m_ColTreeView2);
		//附空值
		m_RowHeaderView2 = null;
		m_ColumnHeaderView2 = null;
		m_RightDownView = null;
		m_DownView = null;
		m_RightView = null;
		m_HScrollBar2 = null;
		m_VScrollBar2 = null;
		m_RowTreeView2 = null;
		m_ColTreeView2 = null;
		m_horBar = null;
		m_verBar = null;
		m_crossBox = null;
		ui.installListeners();
		firePropertyChange("seperate2lock", null, null);

		revalidate();
	}
    /**
     * 清除表格视图,同时清除CellsModelListener和SelectModelListener
     * modify by guogang 2008-4-9
     * @param view
     */
	private void clearCellsView(JViewport view) {
		if (view != null) {
			CellsPane pane = (CellsPane) view.getView();
			pane.getDataModel().removeCellsModelListener(pane);
			pane.getDataModel().getSelectModel().removeSelectModelListener(pane);
			remove(view);
		}
	}
   /**
    * 清除表头树视图
    * @param view
    */
	private void clearTreeView(JViewport view) {
		if (view != null) {
			HeaderTree tree = (HeaderTree) view.getView();
			tree.getTreeModel().removeTreeModelListener(tree);
			remove(view);
		}
	}
   /**
    * 清除表头视图
    * @param view
    */
	private void clearHeaderView(JViewport view) {
		if (view != null) {
			TableHeader header = (TableHeader) view.getView();
			header.getModel().removeHeaderModelListener(header);
			remove(view);
		}
	}

	/**
	 * 窗体是否冻结
	 * @return boolean 窗体是否冻结
	 */
	public boolean getFreezing() {
		return getSeperateLockSet().isFreezing();
	}

	/**
	 * 数值是否冻结窗体.如果窗体冻结,副属的窗体成为主窗体,需要将滚动条的位置互换,将第2个滚动条作为主滚动条;
	 * 当冻结解除的时候,需要调换回去.期间，需要修改分栏出现的滚动条的滚动范围的最小值，并且原来对应的滚动条不可用。
	 * @param bFreezing
	 */
	public void setFreezing(boolean bFreezing) {
		boolean bOld = getSeperateLockSet().isFreezing();
		getSeperateLockSet().setFreezing(bFreezing);
		
		if (bOld != getSeperateLockSet().isFreezing()) {
			
			getCellsModel().setDirty(true);
			
			if (bFreezing) { //冻结
				if (m_RowHeaderView2 != null) {
					TableHeader rowHeader = (TableHeader) m_RowHeaderView2
							.getView();
					getSeperateLockSet().setSeperateRow(
							rowHeader.getModel().getIndexByPosition(
									getSeperateY()));
				}
				if (m_ColumnHeaderView2 != null) {
					TableHeader colHeader = (TableHeader) m_ColumnHeaderView2
							.getView();
					getSeperateLockSet().setSeperateCol(
							colHeader.getModel().getIndexByPosition(
									getSeperateX()));
				}
			} else { //将行列换算为具体的位置
				if (m_RowHeaderView2 != null) {
					TableHeader rowHeader = (TableHeader) m_RowHeaderView2
							.getView();
					getSeperateLockSet().setSeperateY(
							rowHeader.getModel().getPosition(getSeperateRow()));
					m_VScrollBar.setEnabled(true);
				}
				if (m_ColumnHeaderView2 != null) {
					TableHeader colHeader = (TableHeader) m_ColumnHeaderView2
							.getView();
					getSeperateLockSet().setSeperateX(
							colHeader.getModel().getPosition(getSeperateCol()));
					m_HScrollBar.setEnabled(true);
				}
			}
			firePropertyChange("seperate2lock", bOld, getSeperateLockSet()
					.isFreezing());
			revalidate();
		}
	}

	/**
	 * @edit by wangyga at 2009-5-21,下午04:51:43 派发tablePane属性改变事件
	 */
	@Override
	public void firePropertyChange(String propertyName, boolean oldValue,
			boolean newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
		
		if(getCells() == null){
			return;
		}
		EventManager eventManager = getCells().getEventManager();
		if(eventManager != null){
			PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName,
					   null, null);
			eventManager.dispatch(new PropertyChangeEventAdapter(event));
		}
	}

	@Override
	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
		
		if(getCells() == null){
			return;
		}
		EventManager eventManager = getCells().getEventManager();
		if(eventManager != null){
			PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName,
					   null, null);
			eventManager.dispatch(new PropertyChangeEventAdapter(event));
		}
	}

	/**
	 * 得到垂直滚动条的策略
	 * @return int 垂直滚动条的策略
	 */
	public int getVScrollBarPolicy() {
		return m_nVScrollBarPolicy;
	}

	/**
	 * 得到水平滚动条的策略
	 * @return int 水平滚动条的策略
	 */
	public int getHScrollBarPolicy() {
		return m_nHScrollBarPolicy;
	}

	/**
	 * 设置垂直滚动条策略，取值为TableConstants中关于滚动规则的常量
	 * @param policy 垂直滚动条策略
	 */
	public void setVScrollBarPolicy(int policy) {
		switch (policy) {
		case VERTICAL_SCROLLBAR_AS_NEEDED:
		case VERTICAL_SCROLLBAR_NEVER:
		case VERTICAL_SCROLLBAR_ALWAYS:
			break;
		default:
			throw new IllegalArgumentException(
					"invalid verticalScrollBarPolicy");
		}
		int old = m_nVScrollBarPolicy;
		m_nVScrollBarPolicy = policy;
		firePropertyChange("verticalScrollBarPolicy", old, policy);
		revalidate();
		repaint();
	}

	/**
	 * 设置水平滚动条的规则，取值为TableConstants中关于滚动规则的常量
	 * @param policy int 水平滚动条的规则
	 */
	public void setHScrollBarPolicy(int policy) {
		switch (policy) {
		case HORIZONTAL_SCROLLBAR_AS_NEEDED:
		case HORIZONTAL_SCROLLBAR_NEVER:
		case HORIZONTAL_SCROLLBAR_ALWAYS:
			break;
		default:
			throw new IllegalArgumentException(
					"invalid horizontalScrollBarPolicy");
		}
		int old = m_nHScrollBarPolicy;
		m_nHScrollBarPolicy = policy;
		firePropertyChange("horizontalScrollBarPolicy", old, policy);
		revalidate();
		repaint();
	}

	/**
	 * 得到垂直滚动条。不提供设置滚动条的方法，面板初始化时构建滚动条，只是控制它的显示策略。
	 * @return TableScrollBar 垂直滚动条
	 */
	public TableScrollBar getVScrollBar() {
		return m_VScrollBar;
	}

	/**
	 * 得到水平滚动条。不提供设置滚动条的方法，面板初始化时构建滚动条，只是控制它的显示策略。
	 * @return TableScrollBar 水平滚动条
	 */
	public TableScrollBar getHScrollBar() {
		return m_HScrollBar;
	}

	/**
	 * 设置主视图容纳的组件。该组件需要实现Scrollable接口
	 * @param c Component 添加到主视图中的组件
	 */
	private void setMainviewView(CellsPane c) {
		if (m_MainView == null) {
			m_MainView = new UIViewport();
		}
		m_MainView.setView(c);
		this.m_CellsPane = c;
	}

	/**
	 * 设置当前的主视口。
	 * @param viewport JViewport 主视图
	 */
	public void setMainView(JViewport viewport) {
		JViewport old = getMainView();
		this.m_MainView = viewport;
		if (viewport != null) {
			add(viewport, MAINVIEW);
		} else if (old != null) {
			remove(old);
		}
		firePropertyChange("viewport", old, viewport);
		revalidate();
		repaint();
	}

	/**
	 * 得到当前的主视口。
	 * @return JViewPort 主视图
	 */
	public JViewport getMainView() {
		return m_MainView;
	}

	/**
	 * 得到面板中的表格组件
	 * @return CellsPane
	 */

	public CellsPane getCells() {
		return m_CellsPane;
	}
	/**
	 * 根据单元格的位置获取其所在的CellsPane,主要用来处理有分栏、冻结的情况
	 * @param cellPos
	 * @return
	 */
	public CellsPane getCells(Rectangle rect){
		CellsPane curCellsPane=null;
		SeperateLockSet lockset=getCells().getDataModel().getSeperateLockSet();
		if(lockset.isFreezing()){
			
			JViewport view=null;
			Rectangle rectView=null;
			Rectangle interView=null;
			if(getMainView() != null){
				view=getMainView();
				rectView = view.getViewRect();
				interView = rect.intersection(rectView);
				if(interView.width>0&&interView.height>0){
					curCellsPane=(CellsPane)view.getView();
				}
			}
			if(getRightView() != null){
				view=getRightView();
				rectView = view.getViewRect();
				interView = rect.intersection(rectView);
				if(interView.width>0&&interView.height>0){
					curCellsPane=(CellsPane)view.getView();
				}
			}
			if(getDownView() != null){
				view=getDownView();
				rectView = view.getViewRect();
				interView = rect.intersection(rectView);
				if(interView.width>0&&interView.height>0){
					curCellsPane=(CellsPane)view.getView();
				}
			}
			if(getRightDownView()!= null){
				view=getRightDownView();
				rectView = view.getViewRect();
				interView = rect.intersection(rectView);
				if(interView.width>0&&interView.height>0){
					curCellsPane=(CellsPane)view.getView();
				}
			}
			
		}else{
			curCellsPane=getCells();	
		}
		return curCellsPane;
	}
   /**
    * 得到面板中的表格组件的数据模型
    * @return
    */
	public CellsModel getCellsModel() {
		return getCells().getDataModel();
	}

	/**
	 * 得到容纳页签的主视图
	 * @return JViewport 容纳页签的主视图
	 */
	public JViewport getPageView() {
		return m_PageView;
	}

	/**
	 * 获得行标题视图
	 * @return JViewport 行标题视图
	 */
	public JViewport getRowHeader() {
		return m_RowHeaderView;
	}

	/**
	 * 设置行标题视图
	 * @param rowHeader JViewport 行标题视图
	 */
	public void setRowHeader(JViewport rowHeader) {
		JViewport old = getRowHeader();
		this.m_RowHeaderView = rowHeader;
		if (rowHeader != null) {
			add(rowHeader, ROWHEADERVIEW);
		} else if (old != null) {
			remove(old);
		}
		firePropertyChange("rowHeader", old, rowHeader);
		revalidate();
		repaint();
	}

	/**
	 *  设置行标题视图中容纳的组件
	 * @param c Component 行标题视图中容纳的组件
	 */
	public void setRowHeaderView(Component c) {
		if (m_RowHeaderView == null) {
			m_RowHeaderView = new UIViewport();
		}
		m_RowHeaderView.setView(c);
		revalidate();
		repaint();
	}

	/**
	 * 得到列标题容器
	 * @return JViewport 列标题容器
	 */
	public JViewport getColumnHeader() {
		return m_ColumnHeaderView;
	}

	/**
	 *  设置列标题视图中容纳的组件
	 * @param c Component 列标题视图中容纳的组件
	 */
	public void setColumnHeaderView(Component c) {
		if (m_ColumnHeaderView == null) {
			m_ColumnHeaderView = new UIViewport();
		}
		m_ColumnHeaderView.setView(c);
		revalidate();
		repaint();
	}

	/**
	 * 设置列标题视图
	 * @param columnHeader JViewport 列标题视图
	 */
	public void setColumnHeader(JViewport columnHeader) {
		JViewport old = getColumnHeader();
		this.m_ColumnHeaderView = columnHeader;
		if (columnHeader != null) {
			add(columnHeader, COLUMNHEADERVIEW);
		} else if (old != null) {
			remove(old);
		}
		firePropertyChange("columnHeader", old, columnHeader);
		revalidate();
		repaint();
	}

	/**
	 * 获得某个角落组件
	 * @param key String 组件标识，它的取值范围如下：
	 * <ul>
	 * <li>TableConstants.UPPER_LEFT
	 * <li>TableConstants.UPPER_RIGHT
	 * <li>TableConstants.LOWER_lEFT
	 * <li>TableConstants.LOWER_RIGHT
	 * </ul>
	 * @return Component
	 * @see TableConstants
	 */
	public Component getCorner(String key) {
		if (key.equals(UPPER_LEFT)) {
			return m_upperLeft;
		} else if (key.equals(UPPER_RIGHT)) {
			return m_upperRight;
		} else if (key.equals(LOWER_lEFT)) {
			return m_lowerLeft;
		} else if (key.equals(LOWER_RIGHT)) {
			return m_lowerRight;
		} else {
			return null;
		}
	}

	/**
	 * 设置某个角落组件
	 * @param key String 组件标识，它的取值范围如下：
	 * <ul>
	 * <li>TableConstants.UPPER_LEFT
	 * <li>TableConstants.UPPER_RIGHT
	 * <li>TableConstants.LOWER_lEFT
	 * <li>TableConstants.LOWER_RIGHT
	 * </ul>
	 * @param corner Component 需要设置的组件
	 * @see TableConstants
	 */
	public void setCorner(String key, Component corner) {
		Component old;
		if (key.equals(UPPER_LEFT)) {
			old = m_upperLeft;
			m_upperLeft = corner;
		} else if (key.equals(UPPER_RIGHT)) {
			old = m_upperRight;
			m_upperRight = corner;
		} else if (key.equals(LOWER_RIGHT)) {
			old = m_lowerRight;
			m_lowerRight = corner;
		} else if (key.equals(LOWER_lEFT)) {
			old = m_lowerLeft;
			m_lowerLeft = corner;
		} else {
			throw new IllegalArgumentException("invalid corner key");
		}
		add(corner, key);
		firePropertyChange(key, old, corner);
		revalidate();
		repaint();
	}

	/**
	 * 组件如果改动，容器中所有的组件都需要重新布局。参见父类说明
	 * @return boolean
	 */
	public boolean isValidateRoot() {
		return true;
	}

	/**
	 * 设置布局管理器.校验布局管理器。参见父类说明
	 * @param layout
	 */
	public void setLayout(LayoutManager layout) {
		if (layout instanceof TableLayout) {
			super.setLayout(layout);
			((TableLayout) layout).syncWithScrollPane(this);
		} else {
			throw new ClassCastException("Layout must be TableLayout");
		}
	}

	/**
	 * 参见父类说明
	 * @param ui
	 */

	public void setUI(TablePaneUI ui) {
		super.setUI(ui);
	}

	/**
	 * 参见父类说明
	 * @return TablePaneUI
	 */
	public TablePaneUI getUI() {
		return (TablePaneUI) ui;
	}

	/**
	 * 得到滑块组件
	 * @return SlideBox
	 */
	public SlideBox getSlideBox() {
		return m_SlideBox;
	}

	/**
	 * 得到水平分割栏
	 * @return SeperatorBar
	 */
	public SeperatorBar getHorBar() {
		return m_horBar;
	}

	/**
	 * 得到垂直的分割栏
	 * @return SeperatorBar
	 */
	public SeperatorBar getVerBar() {
		return m_verBar;
	}

	/**
	 * 得到分割栏交叉部位的组件，该组件处理水平和垂直两个维度的拖动。
	 * @return SeperatorBox
	 */
	public SeperatorBox getCrossBox() {
		return m_crossBox;
	}

//	/**
//	 * 是否显示页签
//	 * @return boolean
//	 */
//	public boolean isShowPageMark() {
//		return m_bShowPageMark;
//	}

	/**
	 * 得到分栏情况下下方的行标题
	 * @return JViewport
	 */
	public JViewport getRowHeader2() {
		return m_RowHeaderView2;
	}

	/**
	 * 得到分栏情况下右侧的列标题
	 * @return JViewport
	 */
	public JViewport getColumnHeader2() {
		return m_ColumnHeaderView2;
	}

	/**
	 * 得到水平方向拆分时候,右侧出现的视图.
	 * @return JViewport
	 */
	public JViewport getRightView() {
		return m_RightView;
	}

	/**
	 * 得到垂直方向拆分时候,下方出现的视图
	 * @return JViewport
	 */
	public JViewport getDownView() {
		return m_DownView;
	}

	/**
	 * 得到沿着两个方向拆分出现的右下方的视图
	 * @return JViewport
	 */
	public JViewport getRightDownView() {
		return m_RightDownView;
	}

	/**
	 * 行标题树的视图组件
	 * @return JViewport
	 */
	public JViewport getRowTreeView() {
		return m_RowTreeView;
	}

	/**
	 * 拆分生成的行标题树的视图组件
	 * @return JViewport
	 */
	public JViewport getRowTreeView2() {
		return m_RowTreeView2;
	}

	/**
	 * 列标题树的视图组件
	 * @return JViewport
	 */
	public JViewport getColTreeView() {
		return m_ColTreeView;
	}

	/**
	 * 拆分生成的列标题树的视图组件
	 * @return JViewport
	 */
	public JViewport getColTreeView2() {
		return m_ColTreeView2;
	}

	/**
	 * 水平方向拆分,出现在右侧的水平滚动条
	 * @return TableScrollBar
	 */
	public TableScrollBar getHScrollBar2() {
		return m_HScrollBar2;
	}

	/**
	 * 垂直方向拆分,出现在下方的垂直滚动条
	 * @return TableScrollBar
	 */
	public TableScrollBar getVScrollBar2() {
		return m_VScrollBar2;
	}

	/**
	 * 窗口分割的水平象素的位置
	 * @return int
	 */
	public int getSeperateX() {
		//防止拖动行高列宽时，m_nSeperateX不会自动更新。可以在行高列宽变化时重新，减少计算量！
		getSeperateLockSet().setSeperateX(
				getCells().getDataModel().getColumnHeaderModel().getPosition(
						getSeperateCol()));
		return getSeperateLockSet().getSeperateX();
	}

	/**
	 * 窗口分割的水平象素的位置
	 * @return int
	 */
	public int getSeperateY() {
		//防止拖动行高列宽时，m_nSeperateX不会自动更新。
		getSeperateLockSet().setSeperateY(
				getCells().getDataModel().getRowHeaderModel().getPosition(
						getSeperateRow()));
		return getSeperateLockSet().getSeperateY();
	}

	/**
	 * 分栏所处的行
	 * @return int
	 */
	public int getSeperateRow() {
		return getSeperateLockSet().getSeperateRow();
	}

	/**
	 * 分栏所处的列
	 * @return int
	 */
	public int getSeperateCol() {
		return getSeperateLockSet().getSeperateCol();
	}

	/**
	 * 窗口是否冻结
	 * @return boolean
	 */
	public boolean isFreezing() {
		if(getCellsModel() == null){
			return false;
		}
		return getSeperateLockSet().isFreezing();
	}

	//************************************
	//实现分页打印的接口
	//***********************************
	/**当前系统的打印服务*/
	private PrinterJob printerJob;

	/**打印设置的信息*/
	//	private PrintSet m_printSet;迁移到CellsModel中
	public PrinterJob getPrinterJob() {
		if (printerJob == null) {
			printerJob = PrinterJob.getPrinterJob();
		}
		printerJob.setPageable(this);
		return printerJob;
	}

	/**
	 * 设置打印信息
	 * @param ps
	 */
	public void setPrintSet(PrintSet ps) {
		getCellsModel().setPrintSet(ps);
	}

	/**
	 * 得到打印设置信息
	 * @return PrintSet
	 */
	public PrintSet getPrintSet() {
		return getCellsModel().getPrintSet();
	}

	/**
	 * 打印的页面设置
	 *
	 * 解决在JDK1.4.2下调用页面设置对话框的时候背景不能刷新 update by CaiJie 2005-10-28
	 */
	public void pageFromat() {
		//		PrintSet ps = getPrintSet();
		//		PageFormat oldPf = ps.getPageformat();
		//        PageFormat newPf = getPrinterJob().pageDialog(oldPf);
		//		//这里需要检查返回的页面设置是否合法，如果不合法，需要修正。
		////		PageFormat validPf = getPrinterJob().validatePage(pf);
		//        ps.setPageFormat(newPf);
		//        if(getPrintPageRect().length == 0){
		//            ps.setPageFormat(oldPf);
		//            return false;
		//        }
		//		repaint();
		//        return true;

		Thread t = new Thread(new Runnable() {
			public void run() {
				PrintSet ps = getPrintSet();
				PageFormat oldPf = ps.getPageformat();
				PageFormat newPf = getPrinterJob().pageDialog(oldPf);
				//这里需要检查返回的页面设置是否合法，如果不合法，需要修正。
				//	    		PageFormat validPf = getPrinterJob().validatePage(pf);
				ps.setPageFormat(newPf);
				if (getPrintPageRect().length == 0) {
					ps.setPageFormat(oldPf);
				}

				repaint();
			}
		});

		t.start();
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO 自动生成 catch 块
			AppDebug.debug(e);
		}

	}

	/**
	 * 打印
	 * @return boolean
	 */
	public boolean print() {
		return print(true);
	}

	/**
	 * 打印
	 * @param showPrintDlg 打印时是否弹出打印设置对话框
	 * @return boolean
	 * @i18n report00015=打印机不能处理打印任务，请确认打印机正常工作后重试。
	 */
	public boolean print(boolean showPrintDlg) {
		if (getPrintPageRect().length == 0) {
			return false;
		}
		boolean result = false;
		
		//此处由于没有关闭当前的报表视图，不仅要设置参数，还要重新绘制
		
		try {
			PrinterJob pj = getPrinterJob();
			if (showPrintDlg && !pj.printDialog()) {
				return false;
			}
			pj.print();
			result = true;
		} catch (PrinterException ex) {
			AppDebug.debug(ex);
			JOptionPane.showMessageDialog(this, MultiLang.getString("report00015"));
			//        	UfoPublic.showErrorDialog(, message, title);
		}
		return result;
	}

	/**
	 * 用于批量打印，可以输入打印份数，并且不弹出打印设置对话框
	 * @param copyCount
	 * @return boolean
	 */
	public boolean print(int copyCount) {
		for (int i = 0; i < copyCount; i++) {
			if (!print(false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 指定打印服务。
	 * @param printService
	 * @return boolean
	 */
	public boolean setPrintService(PrintService printService) {
		try {
			if (printService == null) {
				return false;
			}
			getPrinterJob().setPrintService(printService);
			return true;
		} catch (PrinterException e) {
			AppDebug.debug(e);
			return false;
		}
	}

	/**
	 * 打印的打印预览。
	 * modify by guogang 2007-12-11 
	 * 修正打印预览的时候通过修改打印设置再预览的时候如果原先的界面超出显示窗口
	 * 而PrintPreviewDialog.freshPrintPanel()中，printPane.removeAll()后
	 * 只是调用printPane.repaint()而不会绘制其子组件PrintPreview
	 * 而没有任何显示的情况
	 */
	public void printPreview(Container parent) {
		if (parent == null) {
			parent = this;
		}
		

		try {

			if(m_printPreView==null)
			  m_printPreView = new PrintPreviewDialog(parent, true, this);
			if (m_printPreView.getviewRects() == null
					|| m_printPreView.getviewRects().length < 1) {
				return;
			}
			m_printPreView.freshPrintPanel();
			//预览的时候都要重新绘制
			if(!m_printPreView.isVisible()){
			  m_printPreView.setVisible(true);
			}else{
			  m_printPreView.show();
			}
			
			  

		} finally {
		

		}
	}
 

	/**
	 * 分页计算方法。
	 * 当前方法主要是计算按照打印设置，需要产生的每个打印页面的打印区域
	 * 按照如下规则计算：
	 * 首先从打印设置中得到打印媒质上的打印区域；
	 * 用该区域的值除以比例，得到在媒质上打印的内容对应组件占用的区域；
	 * 将组件按照每个媒质占用的区域来分配,考虑强制分行列，得到需要打印的页面的数量，然后将每个媒质上页面的区域计算出来返回。
	 * 如果用户设置了固定行列头尾，可用页面大小需要减去行列头尾大小，组件需要打印的区间需要剔除固定行列首尾的位置。
	 * @return Rectangle[] 数组记录每页数据在表格控件上对应的位置
	 * modify by guogang 2007-6-27 修正了打印和显示不一致的情况：如打印时多出了一页
	 * 造成这种情况的原因是打印时，获取单元格长度时没有乘以PRINT_SCALE，而预览时是乘以PRINT_SCALE的
	 * modify by guogang 2007-7-20 增加了打印到单页时的分页处理
	 * add by guogang 2007-12-5 在计算打印区域的时候没有计算页眉、页脚的高度，是由于在打印的时候将其打印在纸张边距的区域，如果要在纸张打印区域打印的话就需要计算了
	 */
	public Rectangle[] getPrintPageRect() {
		
		PrintSet ps = getPrintSet();
		PageFormat pf = ps.getPageformat();
		CellsModel model = getCells().getDataModel();

		HeaderModel rowModel = model.getRowHeaderModel();
		HeaderModel colModel = model.getColumnHeaderModel();
		int nMaxRow = 0;//getPrintMaxRow();//model.getRowNum();
		int nMaxCol = 0;//getPrintMaxCol();//model.getColNum();
		int[] arr = model.getPrintMaxRowAndCol();
		nMaxRow = arr[0] + 1;
		nMaxCol = arr[1] + 1;
		
		//判断打印
		int[] nPrintArea = new int[] { -1, -1, 0, 0 };
		int[] nRowHeadRang = new int[] { -1, 0 };
		int[] nColHeadRang = new int[] { -1, 0 };
		if (ps.getPrintArea() != null) {
			System.arraycopy(ps.getPrintArea(), 0, nPrintArea, 0, ps
					.getPrintArea().length);//复制数组保证不改变原有ps中的数值。
		}
		if (ps.getRowHeadRang() != null) {
			System.arraycopy(ps.getRowHeadRang(), 0, nRowHeadRang, 0, ps
					.getRowHeadRang().length);
		}
		if (ps.getColHeadRang() != null) {
			System.arraycopy(ps.getColHeadRang(), 0, nColHeadRang, 0, ps
					.getColHeadRang().length);
		}
		if (nRowHeadRang[0] == -1) {
			nRowHeadRang[0] = 0;
		}
		if (nColHeadRang[0] == -1) {
			nColHeadRang[0] = 0;
		}

		// boolean bRowHeadTail = ps.isRowHeadTail();
		//首先计算出来报表需要打印的区域。如果设置了固定行列头尾，需要删除固定区域；如果打印设置中没有，那么就是所有设置了信息的区域。
		if (nPrintArea[0] == -1) {//起始行未设置
			nPrintArea[0] = 0;
		}
		if (nPrintArea[1] == -1) {//起始列未设置
			nPrintArea[1] = 0;
		}
		if (nPrintArea[2] == 0 || nPrintArea[2] > nMaxRow) {//结束行未设置
			nPrintArea[2] = nMaxRow;
		}
		if (nPrintArea[3] == 0 || nPrintArea[3] > nMaxCol) {//结束列未设置
			nPrintArea[3] = nMaxCol;
		}
        
		Rectangle[] pagePositons =new Rectangle[0];
		int nRowHeadSize = 0, nColHeadSize = 0;
		ArrayList<Integer> aryBreakY ;
		ArrayList<Integer> aryBreakX;
		if(ps.isPageToOne()){
			pagePositons =new Rectangle[1];
			aryBreakX=new ArrayList<Integer>();
			aryBreakX.add(new Integer(nPrintArea[1]));
			aryBreakY=new ArrayList<Integer>();
			aryBreakY.add(new Integer(nPrintArea[0]));
			calculatePagePos(colModel, rowModel, aryBreakX, aryBreakY,
					aryBreakX.size(), aryBreakY.size(), nPrintArea[3], nPrintArea[2], pagePositons,
					true);
		}else{
		
		//计算出可以用来打印的区域的大小。它是打印媒质的大小先按照打印比例映射成为对应组件的大小，然后减去固定行列的大小。
		double scale = ps.getPrintScale();
		//在打印比例下，输出媒质每页对应组件的尺寸。
		int mediaWidth = (int) (pf.getImageableWidth() / scale);
		int mediaHeight = (int) (pf.getImageableHeight() / scale);
		int pageStartRow = 0, pageStartCol = 0;
		if (nRowHeadRang != null && nRowHeadRang[0] >= 0
				&& nRowHeadRang[1] >= 0) {
			HeaderModel hmodel = rowModel;
			nRowHeadSize = hmodel.getPosition(nRowHeadRang[1])
					- hmodel.getPosition(nRowHeadRang[0]);
			pageStartRow = nRowHeadRang[1];
			mediaHeight -= nRowHeadSize;
		}
		if (nColHeadRang != null && nColHeadRang[0] >= 0
				&& nColHeadRang[1] >= 0) {
			HeaderModel hmodel = colModel;
			nColHeadSize = hmodel.getPosition(nColHeadRang[1])
					- hmodel.getPosition(nColHeadRang[0]);
			pageStartCol = nColHeadRang[1];
			mediaWidth -= nColHeadSize;
		}
		//按照每个页面可用区域的大小来分割行列：保证行列打印完整；保证强制分页，保证最后或者初始的行列打印完整的边线；
		//分割行。
		pageStartRow = nPrintArea[0];
		pageStartCol = nPrintArea[1];
		nMaxRow = nPrintArea[2];
		nMaxCol = nPrintArea[3];
//		//只打印有效数据区域（即存在内容或格式的区域）。
//		int[] validHeaders = getCells().getDataModel().getValidHeaders();
//		//不打印maxRow和maxCol.
//		validHeaders[0]++;
//		validHeaders[1]++;
//		nMaxRow = nMaxRow <= validHeaders[0] ? nMaxRow : validHeaders[0];
//		nMaxCol = nMaxCol <= validHeaders[1] ? nMaxCol : validHeaders[1];

		pageStartRow = Math.max(pageStartRow, (nRowHeadRang == null) ? 0
				: nRowHeadRang[1]);
		pageStartCol = Math.max(pageStartCol, (nColHeadRang == null) ? 0
				: nColHeadRang[1]);
		if (mediaHeight <= 0 || mediaWidth <= 0) {
			UfoPublic.showErrorDialog(this, MultiLang
					.getString("row_col_header_size_too_large"), MultiLang
					.getString("miufo1000185"));
			return null;//new Rectangle[0];
			//            throw new RuntimeException();
		}
		aryBreakY = sepPageHeader(rowModel, nMaxRow, mediaHeight,
				pageStartRow, Header.ROW);
		int heightNum = aryBreakY.size();
		if (heightNum == 0)
			return new Rectangle[0];
		aryBreakX = sepPageHeader(colModel, nMaxCol, mediaWidth,
				pageStartCol, Header.COLUMN);
		int widthNum = aryBreakX.size();
		if (widthNum == 0)
			return new Rectangle[0];
		pagePositons = new Rectangle[heightNum * widthNum];
		//根据行列的优先规则来排列打印区域的顺序.
		boolean colPriority = ps.isColPriorityPrinted();
		if (colPriority) {
			calculatePagePos(colModel, rowModel, aryBreakX, aryBreakY,
					widthNum, heightNum, nMaxCol, nMaxRow, pagePositons,
					colPriority);
		} else {
			calculatePagePos(rowModel, colModel, aryBreakY, aryBreakX,
					heightNum, widthNum, nMaxRow, nMaxCol, pagePositons,
					colPriority);
		}
		
		}
		
		return pagePositons;
	}

	/**
	 * 分页的时候根据行列的优先规则来排列打印区域的顺序
	 * @param model1 优先级高的表头数据模型
	 * @param model2 优先级低的表头数据模型
	 * @param aryBreak1  优先级高的方向的分页开始号数组
	 * @param aryBreak2  优先级低的方向的分页开始号数组
	 * @param num1 优先级高的方向的分页总页数
	 * @param num2 优先级低的方向的分页总页数
	 * @param nMax1 优先级高的方向的最大单元号
	 * @param nMax2 优先级低的方向的最大单元号
	 * @param pagePositons 分页区域数组
	 * @param colPriority 是否是列优先
	 */
	private void calculatePagePos(HeaderModel model1, HeaderModel model2,
			ArrayList<Integer> aryBreak1, ArrayList<Integer> aryBreak2, int num1, int num2,
			int nMax1, int nMax2, Rectangle[] pagePositons, boolean colPriority) {
		for (int i = 0; i < num1; i++) {
			int start1, end1;
			start1 = model1
					.getPosition(((Integer) aryBreak1.get(i)).intValue());
			if (i + 1 < num1) {
				end1 = model1.getPosition(((Integer) aryBreak1.get(i + 1))
						.intValue());
			} else {
				end1 = model1.getPosition(nMax1 - 1)
						+ model1.getSize(nMax1 - 1);// + 2;//最后的行列保证打印出粗边线。
			}
			
			for (int j = 0; j < num2; j++) {
				int start2, end2;
				start2 = model2.getPosition(((Integer) aryBreak2.get(j))
						.intValue());
				if (j + 1 < num2) {
					end2 = model2.getPosition(((Integer) aryBreak2.get(j + 1))
							.intValue());
				} else {
					end2 = model2.getPosition(nMax2 - 1)
							+ model2.getSize(nMax2 - 1);// + 2;
				}
				if (colPriority) {
					pagePositons[i * num2 + j] = new Rectangle(start1, start2,
							end1 - start1, end2 - start2);
				} else {
					pagePositons[i * num2 + j] = new Rectangle(start2, start1,
							end2 - start2, end1 - start1);
				}
			}
			
			
		}
	}

	/**
	 * 返回分页起始位置列表
	 * 
	 * @param hModel 表头数组
	 * @param nMaxH 最大单元号
	 * @param mediaSize 分页每页的大小
	 * @param pageStartPos 起始位置
	 * @return ArrayList 分页起始位置列表
	 */
	private ArrayList<Integer> sepPageHeader(HeaderModel hModel, int nMaxH,
			int mediaSize, int pageStartPos, int rowcolType) {
		//add by guogang 2007-6-27
		PrintSet ps = getPrintSet();
		PageFormat pf = ps.getPageformat();
		double scale = ps.getPrintScale();
		int mediaWidth = (int) (pf.getImageableWidth() / scale);
		int mediaHeight = (int) (pf.getImageableHeight() / scale);
		int beginPos=pageStartPos;
		//add end
		int[] pages = getCells().getDataModel().getPages(rowcolType);
		// 记录每次分割的具体的起始位置，其中信息以0为起始位置。
		ArrayList<Integer> aryBreak = new ArrayList<Integer>();
		while (pageStartPos < nMaxH) {

			aryBreak.add(new Integer(pageStartPos));
			// modify by guogang 2007-6-27
			// 如果固定区域的起点不是(0,0)的话hModel.getPosition(pageStartPos) +
			// mediaSize就超出了第一页的长度
			// 对首页的处理，如果首页超出了长度，说明固定头不是从(0,0)开始的
			int nextPageSize = hModel.getPosition(pageStartPos) + mediaSize;
			int newStartPos = hModel.getIndexByPosition(nextPageSize);
			if (pageStartPos == beginPos && rowcolType == Header.COLUMN
					&& nextPageSize > mediaWidth) {
				nextPageSize = mediaWidth;
				newStartPos = hModel.getIndexByPosition(nextPageSize);
			}
			if (pageStartPos == beginPos && rowcolType == Header.ROW
					&& nextPageSize > mediaHeight) {
				nextPageSize = mediaHeight;
				newStartPos = hModel.getIndexByPosition(nextPageSize);
			}

			// int newStartPos =
			// hModel.getIndexByPosition(hModel.getPosition(pageStartPos) +
			// mediaSize);
			//end modify
			// 添加强制分页判断
			newStartPos = resetStartPages(newStartPos, pageStartPos, pages);
			if (newStartPos == -1 || newStartPos >= nMaxH) {
				break;
			}
			if (newStartPos <= pageStartPos) {// 错误发生：打印区域无法容纳一行内容。提示错误。
				JOptionPane.showMessageDialog(this, MultiLang
						.getString("print_media_too_small"));
				return new ArrayList();
			}
			pageStartPos = newStartPos;
			
			
		}
		return aryBreak;
	}

	/**
	 * 重置分页的起始位置
	 * @param newStartRow 不考虑分页的新起始位置
	 * @param lastStartPos 上页起始位置
	 * @param pages 强制分页位置
	 * @return int 考虑分页的其实位置
	 */
	private int resetStartPages(int newStartRow, int lastStartPos, int[] pages) {
		for (int i = 0; i < pages.length; i++) {
			if (pages[i] > lastStartPos && pages[i] < newStartRow) {
				newStartRow = pages[i];
			}
		}
		return newStartRow;
	}

	/**
	 * 计算当前文档需要打印的页数。
	 * @return the number of pages in this <code>Pageable</code>.
	 */
	public int getNumberOfPages() {
		return getPrintPageRect().length;
	}

	/**
	 * 获取打印服务实例，负责实际的打印工作
	 * Returns the <code>Printable</code> instance responsible for
	 * rendering the page specified by <code>pageIndex</code>.
	 * @param pageIndex the zero based index of the page whose
	 *            <code>Printable</code> is being requested
	 * @return the <code>Printable</code> that renders the page.
	 * @throws IndexOutOfBoundsException if
	 *            the <code>Pageable</code> does not contain the requested
	 *		  page.
	 */
	public Printable getPrintable(final int pageIndex)
			throws IndexOutOfBoundsException {
		
		
		
		Printable print = new Printable() {

			public int print(Graphics g, PageFormat pf, int pi)
					throws PrinterException {
				Rectangle[] rects = getPrintPageRect();
				if (pi >= rects.length || pi < 0) {
					return Printable.NO_SUCH_PAGE;
				}
				
				PrintPreView view = new PrintPreView(rects[pi], TablePane.this,
						1, getPrintSet().getHeaderFooterModel(), pageIndex + 1,
						rects.length);// getPrintSet().getPrintScale());
				view.setDoubleBuffered(false);
				view.setBounds(0, 0, view.getPreferredSize().width, view
						.getPreferredSize().height);
				view.printAll(g);
				
				return Printable.PAGE_EXISTS;
				// CellsPane pane = getCells();
				// Rectangle[] rects = getPrintPageRect();
				// if (pi >= rects.length || pi < 0) {
				// return Printable.NO_SUCH_PAGE;
				// }
				// else {
				// //绘图句柄移动页边距的偏移.
				// g.translate( (int) pf.getImageableX(), (int)
				// pf.getImageableY());
				// return pane.getUI().print(g, rects[pi],
				// getPrintSet().getPrintScale());
				// }
			}
		};
		
		return print;
	}

	/**
	 * getPageFormat
	 * 获取打印页面设置
	 * @param pageIndex int
	 * @return PageFormat
	 */
	public PageFormat getPageFormat(int pageIndex) {
		//		return getPrintSet().getPageformat();//如果直接返回这个值，Imageable区域无法显示页眉页脚！
		PageFormat pf = (PageFormat) getPrintSet().getPageformat().clone();
		Paper paper = pf.getPaper();
		paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
		pf.setPaper(paper);
		return pf;
	}

	/**
	 *设置正在编辑的列
	 * @param aColumn
	 */
	public void setEditingColumn(int aColumn) {
		editingColumn = aColumn;
	}

	/**
	 *设置正在编辑的行
	 * @param aRow
	 */
	public void setEditingRow(int aRow) {
		editingRow = aRow;
	}

	/**
	 * 得到当前编辑组件的列。如果没有编辑单元，则是－1。
	 * @return  int 当前编辑组件的列。如果没有编辑单元，则是－1。
	 */
	public int getEditingColumn() {
		return editingColumn;
	}

	/**
	 * 得到当前编辑组件的列。如果没有编辑单元，则是－1。
	 * @return  int 当前编辑组件的列。如果没有编辑单元，则是－1。
	 */
	public int getEditingRow() {
		return editingRow;
	}

	/**
	 * 得到当前表页的编辑器
	 * @return SheetCellEditor
	 */
	public SheetCellEditor getCellEditor() {
		return cellEditor;
	}

	/**
	 * 设置当前编辑器
	 * @param anEditor
	 */
	public void setCellEditor(SheetCellEditor anEditor) {
		if (anEditor != cellEditor) {
			cellEditor = anEditor;
		}
	}

	/**
	 * 得到编辑组件。
	 * @return Component
	 */
	public Component getEditorComp() {
		return editorComp;
	}

	/**
	 * 设置编辑组件。
	 * @param editorComp Component
	 */
	public void setEditorComp(Component editorComp) {
		this.editorComp = editorComp;
	}

	/**
	 * 修改行标签可是
	 * @param show
	 */
	public void setRowHeaderVisible(boolean show) {
		if (show != m_rowHeaderShow) {
			m_rowHeaderShow = show;
			//修改视口的可视
			getRowHeader().setVisible(show);
			if (getRowHeader2() != null) {
				getRowHeader2().setVisible(show);
			}
		}
	}

	/**
	 * 修改列标签可视
	 * @param show
	 */
	public void setColHeaderVisible(boolean show) {
		if (show != m_colHeaderShow) {
			m_colHeaderShow = show;
			getColumnHeader().setVisible(show);
			if (getColumnHeader2() != null) {
				getColumnHeader2().setVisible(show);
			}
		}
	}

	/**
	 * 设置组件视图的显示比例。
	 * @param scale double
	 */
	public void setViewScale(double scale) {
		if (TableStyle.getViewScale() != scale) {
			double oldValue = TableStyle.getViewScale();
			TableStyle.setViewScale(scale);
			getCellsModel().getRowHeaderModel().resetSizeCache();
			getCellsModel().getColumnHeaderModel().resetSizeCache();
			revalidate();
			getCells().revalidate();
			repaint();
		}
	}

	/**
	 * 获得视口的显示比例
	 * @return double
	 */
	public static double getViewScale() {
		return TableStyle.getViewScale();
	}

	
	/**
	 * @param showPageMark 要设置的 bShowPageMark。
	 */
//	public void setShowPageMark(boolean showPageMark) {
//		m_bShowPageMark = showPageMark;
//		((TableLayout) getLayout()).syncWithScrollPane(this);
//	}

	/**
	 * @return 返回 m_frozenNoSplit。
	 */
	public boolean isFrozenNoSplit() {
		return getSeperateLockSet().isFrozenNoSplit();
	}

	/**
	 * @param noSplit 要设置的 m_frozenNoSplit。
	 */
	public void setFrozenNoSplit(boolean frozenNoSplit) {
		getSeperateLockSet().setFrozenNoSplit(frozenNoSplit);
		firePropertyChange("seperate2lock", null, null);

	}

	//    /**********属性修改通知**********/
	//    private transient PropertyChangeSupport support = new PropertyChangeSupport(this);
	//    public synchronized void addPropertyChangeListener(
	//            String propertyName,
	//            PropertyChangeListener listener) {
	//        support.addPropertyChangeListener(propertyName,listener);
	//    }
	/**
	 * 模拟键盘输入，这样可以判断是否可以编辑，编辑完成类型转换。
	 */
	public boolean simulateKeyBoardInput(int row, int col, String inputText) {
		if(inputText == null)
			return false;
		CellsModel model = getCellsModel();
		if(!model.isInfinite() && (row + 1 > model.getRowNum() || col + 1 > model.getColNum())){
			return false;
		}
		boolean result = getCells().editCellAt(row, col);
		if (!result) {
			return false;
		}
		try {
			if (getEditorComp() instanceof JTextComponent) {
				JTextComponent textComponent = (JTextComponent) getEditorComp();
				textComponent.setText(inputText);
//				for (int i = 0; i < inputText.length(); i++) {
//					char eachChar = inputText.charAt(i);
//					textComponent.replaceSelection("" + eachChar);
//				}
			}
			result = getCellEditor().stopCellEditing();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private SeperateLockSet getSeperateLockSet() {
		return getCellsModel().getSeperateLockSet();
	}
	
	public void setOperationState(int state){
		CellsPane view =null;
		if(this.getMainView()!=null){
			view= (CellsPane) getMainView().getView();
			if(view!=null){
				view.setPrivateOperationState(state);
			}
		}
		if(this.getRightView()!=null){
			view= (CellsPane) getRightView().getView();
			if(view!=null){
				view.setPrivateOperationState(state);
			}
		}
		if(this.getRightDownView()!=null){
			view= (CellsPane) getRightDownView().getView();
			if(view!=null){
				view.setPrivateOperationState(state);
			}
		}
		if(this.getDownView()!=null){
			view= (CellsPane) getDownView().getView();
			if(view!=null){
				view.setPrivateOperationState(state);
			}
		}
	}
} 