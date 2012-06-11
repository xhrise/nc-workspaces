package com.ufsoft.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;

import nc.ui.pub.beans.UIViewport;

import com.ufsoft.table.header.HeaderModel;
import com.ufsoft.table.header.HeaderTree;
import com.ufsoft.table.header.TableHeader;
import com.ufsoft.table.header.TreeModel;

/**
 * <p>
 * Title:表格容器面板的UI
 * </p>
 * <p>
 * Description: 当前面板自己不需要绘制，主要是为组件添加监听器。包括主面板视图；滚动条
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: UFSOFT
 * </p>
 * 
 * @author wupeng
 * @version 1.0.0.1 2004－04－01 修正表格滚动中无法显示最末端的单元
 */

public class TablePaneUI extends ComponentUI implements TableConstants {
	/**
	 * 关联的组件
	 */
	private TablePane m_table;

	/** 滚动条状态更改的监听器 */
	private SBChangeListener m_lVScrollbar1, m_lVScrollbar2, m_lHScrollbar1,
			m_lHScrollbar2;

	/**
	 * 主视图窗口的监听器
	 */
	private ChangeListener m_lLTViewportChange, m_lLDViewportChange,
			m_lRTViewportChange, m_lRDViewportChange;

	// *********************************************
	/**
	 * 创建组件UI
	 */
	public static ComponentUI createUI(JComponent x) {
		return new TablePaneUI();
	}

	/**
	 * 获取组件默认大小 返回空，表示需要利用布局管理器计算
	 * 
	 * @param c
	 * @return ArrayList
	 */
	public Dimension getPreferredSize(JComponent c) {
		return null;
	}

	/**
	 * 获取组件最小大小
	 */
	public Dimension getMinimumSize(JComponent c) {
		return getPreferredSize(c);
	}

	/**
	 * 获取组件最大大小
	 */
	public Dimension getMaximumSize(JComponent c) {
		return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
	}

	// ******************************
	/**
	 * 安装监听器。 表格组件的设计中，监听器的添加主要在两个地方控制：如果是某个组件的数据模型
	 * 中注册这个组件，监听器的注册和注销在该组件的设置数据模型方法中控制，例如CellsModel和CellsPane
	 * 的注册在CellsPane.setDataModel()中实现；如果是不同组件之间的监听器注册，都控制在这这里，
	 * 例如，HeaderModel对于CellsPane的通知，或者是Viewport的属性变化对于TableScrollBar的 通知。
	 * 当组件重新构建，新的子组件添加，表格分栏，表页切换的时候需要重新设置组件之间监听器的关系。
	 * 添加之前，需要调用uninstallListeners，保证以前的监听器被删除。
	 */
	public void installListeners() {
		JViewport viewMain = m_table.getMainView();
		JViewport viewDown = m_table.getDownView();
		JViewport viewRight = m_table.getRightView();
		JViewport viewRightdown = m_table.getRightDownView();
		JViewport viewRowHeader = m_table.getRowHeader();
		JViewport viewRowHeader2 = m_table.getRowHeader2();
		JViewport viewColHeader = m_table.getColumnHeader();
		JViewport viewColHeader2 = m_table.getColumnHeader2();
		TableScrollBar vsb = m_table.getVScrollBar();
		TableScrollBar vsb2 = m_table.getVScrollBar2();
		TableScrollBar hsb = m_table.getHScrollBar();
		TableScrollBar hsb2 = m_table.getHScrollBar2();
		JViewport viewRowTree = m_table.getRowTreeView();
		JViewport viewRowTree2 = m_table.getRowTreeView2();
		JViewport viewColTree = m_table.getColTreeView();
		JViewport viewColTree2 = m_table.getColTreeView2();
		// HeaderModel和CellsModel之间的关联。
		CellsPane cPane = (CellsPane) viewMain.getView();
		CellsModel dataModel = cPane.getDataModel();
		// @edit by wangyga at 2009-2-25,上午09:39:59
		if(dataModel != null){
			HeaderModel rowModel = dataModel.getRowHeaderModel();
			HeaderModel colModel = dataModel.getColumnHeaderModel();
			rowModel.addHeaderModelListener(dataModel);
			colModel.addHeaderModelListener(dataModel);
			// TreeModle和HeaderModel之间的关联。
			if (viewRowTree != null) {
				HeaderTree rowTree = (HeaderTree) viewRowTree.getView();
				if (rowTree != null) {
					TreeModel treeModle = rowTree.getTreeModel();
					treeModle.addTreeModelListener(rowModel);
					rowModel.addHeaderModelListener(treeModle);
				}
			}
			if (viewColTree != null) {
				HeaderTree colTree = (HeaderTree) viewColTree.getView();
				if (colTree != null) {
					TreeModel treeModle = colTree.getTreeModel();
					treeModle.addTreeModelListener(colModel);
					colModel.addHeaderModelListener(treeModle);
				}
			}
		}

		// 构建滚动条的监听器。
		if (isAvtive(vsb)) {
			m_lVScrollbar1 = new SBChangeListener(viewMain, viewRight, false);
			vsb.getModel().addChangeListener(m_lVScrollbar1);
		}
		if (isAvtive(vsb2)) {
			m_lVScrollbar2 = new SBChangeListener(viewDown, viewRightdown,
					false);
			vsb2.getModel().addChangeListener(m_lVScrollbar2);
		}
		if (isAvtive(hsb)) {
			m_lHScrollbar1 = new SBChangeListener(viewMain, viewDown, true);
			hsb.getModel().addChangeListener(m_lHScrollbar1);
		}
		if (isAvtive(hsb2)) {
			m_lHScrollbar2 = new SBChangeListener(viewRight, viewRightdown,
					true);
			hsb2.getModel().addChangeListener(m_lHScrollbar2);
		}
		// 构建ViewPort的监听器。

		if (viewMain != null) {
			m_lLTViewportChange = createViewportChangeListener(hsb, vsb,
					viewColHeader, viewRowHeader, viewColTree, viewRowTree);
			viewMain.addChangeListener(m_lLTViewportChange);
		}
		if (viewRight != null) {
			m_lRTViewportChange = createViewportChangeListener(hsb2, vsb,
					viewColHeader2, viewRowHeader, viewColTree2, viewRowTree);
			viewRight.addChangeListener(m_lRTViewportChange);
		}
		if (viewDown != null) {
			m_lLDViewportChange = createViewportChangeListener(hsb, vsb2,
					viewColHeader, viewRowHeader2, viewColTree, viewRowTree2);
			viewDown.addChangeListener(m_lLDViewportChange);
		}
		if (viewRightdown != null) {
			m_lRDViewportChange = createViewportChangeListener(hsb2, vsb2,
					viewColHeader2, viewRowHeader2, viewColTree2, viewRowTree2);
			viewRightdown.addChangeListener(m_lRDViewportChange);
		}

	}

	/**
	 * 安装组件对应的UI
	 * 
	 * @see installListeners()
	 */
	public void installUI(JComponent x) {
		m_table = (TablePane) x;
		installListeners();
	}

	/**
	 * 卸载监听器。参见installListeners的说明。
	 */
	public void uninstallListeners() {
		JViewport viewMain = m_table.getMainView();
		JViewport viewDown = m_table.getDownView();
		JViewport viewRight = m_table.getRightView();
		JViewport viewRightdown = m_table.getRightDownView();
		TableScrollBar vsb = m_table.getVScrollBar();
		TableScrollBar vsb2 = m_table.getVScrollBar2();
		TableScrollBar hsb = m_table.getHScrollBar();
		TableScrollBar hsb2 = m_table.getHScrollBar2();
		JViewport viewRowTree = m_table.getRowTreeView();
		JViewport viewColTree = m_table.getColTreeView();
		// HeaderModel和CellsModel之间的关联。
		CellsPane cPane = (CellsPane) viewMain.getView();
		CellsModel dataModel = cPane.getDataModel();
		// @edit by wangyga at 2009-2-25,上午09:32:48 cellsModel可能是null
		if(dataModel != null){
			HeaderModel rowModel = dataModel.getRowHeaderModel();
			HeaderModel colModel = dataModel.getColumnHeaderModel();
			rowModel.removeHeaderModelListener(dataModel);
			colModel.removeHeaderModelListener(dataModel);
			// TreeModle和HeaderModel之间的关联。
			if (viewRowTree != null) {
				HeaderTree rowTree = (HeaderTree) viewRowTree.getView();
				if (rowTree != null) {
					TreeModel treeModle = rowTree.getTreeModel();
					treeModle.removeTreeModelListener(rowModel);
					rowModel.removeHeaderModelListener(treeModle);
				}
			}
			if (viewColTree != null) {
				HeaderTree colTree = (HeaderTree) viewColTree.getView();
				if (colTree != null) {
					TreeModel treeModle = colTree.getTreeModel();
					treeModle.removeTreeModelListener(colModel);
					colModel.removeHeaderModelListener(treeModle);
				}
			}
		}
	
		// 构建滚动条的监听器。
		if (isAvtive(vsb)) {
			vsb.getModel().removeChangeListener(m_lVScrollbar1);
		}
		if (isAvtive(vsb2)) {
			vsb2.getModel().removeChangeListener(m_lVScrollbar2);
		}
		if (isAvtive(hsb)) {
			hsb.getModel().removeChangeListener(m_lHScrollbar1);
		}
		if (isAvtive(hsb2)) {
			hsb2.getModel().removeChangeListener(m_lHScrollbar2);
		}
		// 构建ViewPort的监听器。
		if (viewMain != null) {
			viewMain.removeChangeListener(m_lLTViewportChange);
		}
		if (viewRight != null) {
			viewRight.removeChangeListener(m_lRTViewportChange);
		}
		if (viewDown != null) {
			viewDown.removeChangeListener(m_lLDViewportChange);
		}
		if (viewRightdown != null) {
			viewRightdown.removeChangeListener(m_lRDViewportChange);
		}
		m_lVScrollbar1 = null;
		m_lVScrollbar2 = null;
		m_lHScrollbar1 = null;
		m_lHScrollbar2 = null;
		m_lLTViewportChange = null;
		m_lLDViewportChange = null;
		m_lRTViewportChange = null;
		m_lRDViewportChange = null;
	}

	private boolean isAvtive(Component c) {
		return c != null && c.isEnabled();
	}

	/**
	 * 卸载UI代理
	 */
	public void uninstallUI(JComponent c) {
		uninstallListeners();
		m_table = null;
	}

	/**
	 * 设置和某个视图关联的滚动条的信息
	 * 
	 * @param view
	 *            视图
	 * @param sb
	 *            滚动条
	 * @param bHor
	 *            是否水平滚动条
	 */
	private void syncScrollbar(JViewport view, TableScrollBar sb, boolean bHor) {
		if (view == null || sb == null || !view.isEnabled() || !sb.isEnabled()) {
			return;
		}
		Dimension extentSize = view.getExtentSize();
		Dimension viewSize = view.getViewSize();
		Point viewPosition = view.getViewPosition();
		int extent = bHor ? extentSize.width : extentSize.height;
		int max = bHor ? viewSize.width : viewSize.height;
		int min = sb.getMinimum();
		int value = Math.max(0, Math.min(
				bHor ? viewPosition.x : viewPosition.y, max - extent));
		if (value < min) {
			value = min;
		} // 最小值的设置取上次的设置。
		if (!(value == sb.getValue() && extent == sb.getModel().getExtent() && max == sb
				.getMaximum())) {
			sb.setValues(value, extent, min, max);
		}
	}

	/*
	 * 表头、主视图、滚动条三者监听关系说明：
	 * 1.当滚动条主动移动时，算法判断是否是整格移动，不是整格移动修改改动值为整格移动，同时通知主视图无条件移动；
	 * 2.当主视图主动移动时，因为其用键盘移动可以保证已经是整格移动，所以通知滚动条无条件移动；
	 * 3.表头不需要监听，将其绑定到主视图的位置上，只要主视图位置改变，其位置跟随改变；
	 * 4.主视图和滚动条，主动者引起被动者移动，被动者的移动也会激发主动者的监听器，
	 * 因此主动者判断被动者的值已经等于当前值，就不再通知(JViewport组件本身有设置值等于原值时不通知的功能)。
	 */

	/**
	 * 处理视图发生变化的监听器。当行列的尺寸，数量或者组件的大小发生改变的时候都会改变视图的大小，
	 * 此时视图对应的滚动条的范围应该作出调整。需要注意：当拆分窗口并且锁定了窗口的情况下，
	 * 如果行列改变的位置再拆分位置之前，那么需要重新计算锁定的位置。
	 */
	public class ViewportChangeHandler implements ChangeListener {
		private JViewport horHeaderView, verHeaderView;
		private JViewport horTreeView, verTreeView;
		private TableScrollBar hBar, vBar;

		/**
		 * 视图发生变化的监听器
		 * 
		 * @param hBar
		 *            行滚动条
		 * @param vBar
		 *            列滚动条
		 * @param horHeaderView
		 *            行头视图
		 * @param verHeaderView
		 *            列头视图
		 * @param horTreeView
		 *            行头树视图
		 * @param verTreeView
		 *            列头数视图
		 */
		public ViewportChangeHandler(TableScrollBar hBar, TableScrollBar vBar,
				JViewport horHeaderView, JViewport verHeaderView,
				JViewport horTreeView, JViewport verTreeView) {
			this.horTreeView = horTreeView;
			this.verTreeView = verTreeView;
			this.horHeaderView = horHeaderView;
			this.verHeaderView = verHeaderView;
			this.hBar = hBar;
			this.vBar = vBar;
		}

		/**
		 * 处理视图的变化事件，通知对应的滚动条。这里只处理视图大小发生变化而触发的事件，修改对应滚动条 的最大、最小值。而对于视图位置的变化不处理。
		 * 这个监听器只添加到主视图上。 modify by guogang 2007-11-15
		 * 解决当滚动由下向上的时候重新设置表头模型的大小引起脏数据标志的问题
		 * 
		 * @param e
		 *            ChangeEvent
		 */
		public void stateChanged(ChangeEvent e) {
			// 如果视锁定视图的情况,主视图属性的更改不需要通知滚动条
			JViewport viewport = (JViewport) e.getSource();
			Point p = viewport.getViewPosition();
			// 主视图的大小发生了改变,表头视图和树型视图跟随无条件改变。
			if (horHeaderView != null
					&& p.x != horHeaderView.getViewPosition().x) {
				boolean isIncrease = p.x - horHeaderView.getViewPosition().x > 0;
				horHeaderView.setViewPosition(new Point(p.x, horHeaderView
						.getViewPosition().y));
				// if(isMaxPos(viewport,true,horHeaderView) && !isIncrease){
				// ((TableHeader) horHeaderView.getView()).reDefineCount();
				// }
			}
			if (verHeaderView != null
					&& p.y != verHeaderView.getViewPosition().y) {
				boolean isIncrease = p.y - verHeaderView.getViewPosition().y > 0;
				verHeaderView.setViewPosition(new Point(verHeaderView
						.getViewPosition().x, p.y));
				// if(isMaxPos(viewport,false,verHeaderView) && !isIncrease){
				// ((TableHeader) verHeaderView.getView()).reDefineCount();
				// }
			}
			if (horTreeView != null) {
				horTreeView.setViewPosition(new Point(p.x, horTreeView
						.getViewPosition().y));
			}
			if (verTreeView != null) {
				verTreeView.setViewPosition(new Point(verTreeView
						.getViewPosition().x, p.y));
			}

			if (hBar.getValue() != viewport.getViewPosition().x
					|| vBar.getValue() != viewport.getViewPosition().y) {
				// 通知滚动条改变。
				syncScrollbar(viewport, hBar, true);
				syncScrollbar(viewport, vBar, false);
			}
			/*
			 * if (m_table.isFreezing()) { if (view == m_table.getMainView()) {
			 * syncScrollbar(view, hBar, true); syncScrollbar(view, vBar,
			 * false); } else if (view == m_table.getRightView()) {
			 * syncScrollbar(view, hBar, true); } else if (view ==
			 * m_table.getDownView()) { syncScrollbar(view, vBar, false); } }
			 * else { syncScrollbar(view, hBar, true); syncScrollbar(view, vBar,
			 * false); }
			 */
		}

		/**
		 * 参数视图的最大位置是否是四个视图中最大的。
		 * 
		 * @param viewport
		 * @param b
		 * @return boolean
		 */
		private boolean isMaxPos(JViewport viewport, boolean bHor,
				JViewport headerView) {
			HeaderModel headerModel = ((TableHeader) headerView.getView())
					.getModel();
			JViewport[] ports = new JViewport[] { m_table.getMainView(),
					m_table.getRightView(), m_table.getDownView(),
					m_table.getRightDownView() };
			for (int i = 0; i < ports.length; i++) {
				if (ports[i] != null && viewport != ports[i]) {
					int maxPos = bHor ? ports[i].getViewRect().x
							+ ports[i].getViewRect().width : ports[i]
							.getViewRect().y
							+ ports[i].getViewRect().height;
					int theMaxPos = bHor ? viewport.getViewRect().x
							+ viewport.getViewRect().width : viewport
							.getViewRect().y
							+ viewport.getViewRect().height;
					if (maxPos > theMaxPos + headerModel.getPreferredSize()) {
						return false;
					}
				}
			}
			return true;
		}
	}

	/**
	 * 创建视图变化的处理器
	 * 
	 * @param hBar
	 * @param vBar
	 * @param horHeaderView
	 * @param verHeaderView
	 * @param horTreeView
	 * @param verTreeView
	 * @return
	 * @see ViewportChangeHandler的构造方法
	 */
	private ChangeListener createViewportChangeListener(TableScrollBar hBar,
			TableScrollBar vBar, JViewport horHeaderView,
			JViewport verHeaderView, JViewport horTreeView,
			JViewport verTreeView) {
		return new ViewportChangeHandler(hBar, vBar, horHeaderView,
				verHeaderView, horTreeView, verTreeView);
	}

	/**
	 * 滚动条的监听器。通知树视图，行列视图，主视图，主视图2（拆分窗口）改变视图的位置。需要特殊处理
	 * 的是如果当前的报表是无限表，已经拖动到视图的最大位置,那么继续点击拉伸按钮，表格的范围会扩大。
	 */
	public class SBChangeListener implements ChangeListener {
		/**
		 * 滚动条关联的视图
		 */
		private JViewport m_cellsview, m_cellsview2;

		/**
		 * 是否水平滚动，false标识垂直滚动
		 */
		private boolean hor;

		/**
		 * @param cellsView
		 * @param cellsView2
		 * @param bHor
		 */
		public SBChangeListener(JViewport cellsView, JViewport cellsView2,
				boolean bHor) {
			super();
			this.m_cellsview = cellsView;
			this.m_cellsview2 = cellsView2;
			this.hor = bHor;
		}

		/**
		 * 响应滚动条属性修改的事件。修改关联视图的位置。
		 * 
		 * @param view
		 *            JViewport 滚动条关联的视图的窗口
		 * @param value
		 *            int 滚动条的新值
		 * @param hor
		 *            boolean 是否水平滚动条
		 */
		private void changeViewPortPoint(JViewport view, int value, boolean hor) {
			if (isAvtive(view)) {
				Point p = view.getViewPosition();
				if (hor) {
					p.x = value;
				} else {
					p.y = value;
				}
				if (p.x != view.getViewPosition().x
						|| p.y != view.getViewPosition().y) {
					view.setViewPosition(p);
				}
			}
		}

		/**
		 * 实现接口的方法。 响应滚动条改变的事件。滚动条的改变可能是由于用户点击滚动条，也可能是由于
		 * 和滚动条关联的一个视窗的位置改变，而通知滚动条改变。由于滚动条的变动会通知视窗，而视窗的
		 * 改变也会通知滚动条（视窗中容纳的对象大小改变），所以要注意避免死循环，并且少这种递规的次数。
		 * 
		 * @param e
		 *            ChangeEvent
		 */
		public void stateChanged(ChangeEvent e) {
			synViewPort(e, m_cellsview);
			synViewPort(e, m_cellsview2);
		}

		private void synViewPort(ChangeEvent e, JViewport view) {
			BoundedRangeModel model = (BoundedRangeModel) (e.getSource());
			int value = model.getValue();
			if (isAvtive(view)) {
				/*
				 * 为了优化显示的效率，只有当滚动条拖动到一个新的行列时，才改变视图的位置。所以以下的代码首先计算
				 * 滚动条、视图对应的行列位置，然后比较它们的值。对于视图位置的获取，首先获取行列视图的位置，然后获取主视图
				 * 得位置。目的是如果主视图的位置被移动键盘改变，那么由于主视图通知滚动条改变，所以比较的位置
				 * 会发现相同，所以无法通知其他视图改变。如此的前提是，如果行列不显示，那么不允许拆分窗体和行列分组。
				 * 如果滚动条拖动到最后，需要显示最后单元的内容，该规则不适用。
				 */
				if (model.getExtent() + value <= model.getMaximum()) {
					Point p = view.getViewPosition();
					CellsPane cPane = (CellsPane) view.getView();
					CellsModel dataModel = cPane.getDataModel();
					if(dataModel == null){
						return;
					}
					HeaderModel headerMode = hor ? dataModel
							.getColumnHeaderModel() : dataModel
							.getRowHeaderModel();
					int index = headerMode.getIndexByPosition(hor ? p.x : p.y);
					int newIndex = headerMode.getIndexByPosition(value);
					if (index == newIndex) { // 滚动条只有当新的行列出现的时候才调整视图位置。
						// 解决:当视图左边能显示的最左边一列露出半列时,拖动滚动条无法拖出来.***************
						// 原因时只有当拖过边界时才能激发整格移动时间,这种情况已经显示了半格,不会再有拖过边界的情况发生.
						if (value == (hor ? m_table.getSeperateX() : m_table
								.getSeperateY())) {
							changeViewPortPoint(view, value, hor);
							// changeViewPortPoint(m_cellsview2, value, hor);
						} else if (value == model.getMaximum()
								- model.getExtent()) {
							changeViewPortPoint(view, value, hor);
						} else {
							return;
						}
					} else {
						value = headerMode.getPosition(newIndex);
					}
				}
				changeViewPortPoint(view, value, hor);
				// changeViewPortPoint(m_cellsview2, value, hor);
			}
		}
	}
}
