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
 * Title:�����������UI
 * </p>
 * <p>
 * Description: ��ǰ����Լ�����Ҫ���ƣ���Ҫ��Ϊ�����Ӽ������������������ͼ��������
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: UFSOFT
 * </p>
 * 
 * @author wupeng
 * @version 1.0.0.1 2004��04��01 �������������޷���ʾ��ĩ�˵ĵ�Ԫ
 */

public class TablePaneUI extends ComponentUI implements TableConstants {
	/**
	 * ���������
	 */
	private TablePane m_table;

	/** ������״̬���ĵļ����� */
	private SBChangeListener m_lVScrollbar1, m_lVScrollbar2, m_lHScrollbar1,
			m_lHScrollbar2;

	/**
	 * ����ͼ���ڵļ�����
	 */
	private ChangeListener m_lLTViewportChange, m_lLDViewportChange,
			m_lRTViewportChange, m_lRDViewportChange;

	// *********************************************
	/**
	 * �������UI
	 */
	public static ComponentUI createUI(JComponent x) {
		return new TablePaneUI();
	}

	/**
	 * ��ȡ���Ĭ�ϴ�С ���ؿգ���ʾ��Ҫ���ò��ֹ���������
	 * 
	 * @param c
	 * @return ArrayList
	 */
	public Dimension getPreferredSize(JComponent c) {
		return null;
	}

	/**
	 * ��ȡ�����С��С
	 */
	public Dimension getMinimumSize(JComponent c) {
		return getPreferredSize(c);
	}

	/**
	 * ��ȡ�������С
	 */
	public Dimension getMaximumSize(JComponent c) {
		return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
	}

	// ******************************
	/**
	 * ��װ�������� ������������У��������������Ҫ�������ط����ƣ������ĳ�����������ģ��
	 * ��ע������������������ע���ע���ڸ��������������ģ�ͷ����п��ƣ�����CellsModel��CellsPane
	 * ��ע����CellsPane.setDataModel()��ʵ�֣�����ǲ�ͬ���֮��ļ�����ע�ᣬ�������������
	 * ���磬HeaderModel����CellsPane��֪ͨ��������Viewport�����Ա仯����TableScrollBar�� ֪ͨ��
	 * ��������¹������µ��������ӣ�����������ҳ�л���ʱ����Ҫ�����������֮��������Ĺ�ϵ��
	 * ���֮ǰ����Ҫ����uninstallListeners����֤��ǰ�ļ�������ɾ����
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
		// HeaderModel��CellsModel֮��Ĺ�����
		CellsPane cPane = (CellsPane) viewMain.getView();
		CellsModel dataModel = cPane.getDataModel();
		// @edit by wangyga at 2009-2-25,����09:39:59
		if(dataModel != null){
			HeaderModel rowModel = dataModel.getRowHeaderModel();
			HeaderModel colModel = dataModel.getColumnHeaderModel();
			rowModel.addHeaderModelListener(dataModel);
			colModel.addHeaderModelListener(dataModel);
			// TreeModle��HeaderModel֮��Ĺ�����
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

		// �����������ļ�������
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
		// ����ViewPort�ļ�������

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
	 * ��װ�����Ӧ��UI
	 * 
	 * @see installListeners()
	 */
	public void installUI(JComponent x) {
		m_table = (TablePane) x;
		installListeners();
	}

	/**
	 * ж�ؼ��������μ�installListeners��˵����
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
		// HeaderModel��CellsModel֮��Ĺ�����
		CellsPane cPane = (CellsPane) viewMain.getView();
		CellsModel dataModel = cPane.getDataModel();
		// @edit by wangyga at 2009-2-25,����09:32:48 cellsModel������null
		if(dataModel != null){
			HeaderModel rowModel = dataModel.getRowHeaderModel();
			HeaderModel colModel = dataModel.getColumnHeaderModel();
			rowModel.removeHeaderModelListener(dataModel);
			colModel.removeHeaderModelListener(dataModel);
			// TreeModle��HeaderModel֮��Ĺ�����
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
	
		// �����������ļ�������
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
		// ����ViewPort�ļ�������
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
	 * ж��UI����
	 */
	public void uninstallUI(JComponent c) {
		uninstallListeners();
		m_table = null;
	}

	/**
	 * ���ú�ĳ����ͼ�����Ĺ���������Ϣ
	 * 
	 * @param view
	 *            ��ͼ
	 * @param sb
	 *            ������
	 * @param bHor
	 *            �Ƿ�ˮƽ������
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
		} // ��Сֵ������ȡ�ϴε����á�
		if (!(value == sb.getValue() && extent == sb.getModel().getExtent() && max == sb
				.getMaximum())) {
			sb.setValues(value, extent, min, max);
		}
	}

	/*
	 * ��ͷ������ͼ�����������߼�����ϵ˵����
	 * 1.�������������ƶ�ʱ���㷨�ж��Ƿ��������ƶ������������ƶ��޸ĸĶ�ֵΪ�����ƶ���ͬʱ֪ͨ����ͼ�������ƶ���
	 * 2.������ͼ�����ƶ�ʱ����Ϊ���ü����ƶ����Ա�֤�Ѿ��������ƶ�������֪ͨ�������������ƶ���
	 * 3.��ͷ����Ҫ����������󶨵�����ͼ��λ���ϣ�ֻҪ����ͼλ�øı䣬��λ�ø���ı䣻
	 * 4.����ͼ�͹����������������𱻶����ƶ��������ߵ��ƶ�Ҳ�ἤ�������ߵļ�������
	 * ����������жϱ����ߵ�ֵ�Ѿ����ڵ�ǰֵ���Ͳ���֪ͨ(JViewport�������������ֵ����ԭֵʱ��֪ͨ�Ĺ���)��
	 */

	/**
	 * ������ͼ�����仯�ļ������������еĳߴ磬������������Ĵ�С�����ı��ʱ�򶼻�ı���ͼ�Ĵ�С��
	 * ��ʱ��ͼ��Ӧ�Ĺ������ķ�ΧӦ��������������Ҫע�⣺����ִ��ڲ��������˴��ڵ�����£�
	 * ������иı��λ���ٲ��λ��֮ǰ����ô��Ҫ���¼���������λ�á�
	 */
	public class ViewportChangeHandler implements ChangeListener {
		private JViewport horHeaderView, verHeaderView;
		private JViewport horTreeView, verTreeView;
		private TableScrollBar hBar, vBar;

		/**
		 * ��ͼ�����仯�ļ�����
		 * 
		 * @param hBar
		 *            �й�����
		 * @param vBar
		 *            �й�����
		 * @param horHeaderView
		 *            ��ͷ��ͼ
		 * @param verHeaderView
		 *            ��ͷ��ͼ
		 * @param horTreeView
		 *            ��ͷ����ͼ
		 * @param verTreeView
		 *            ��ͷ����ͼ
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
		 * ������ͼ�ı仯�¼���֪ͨ��Ӧ�Ĺ�����������ֻ������ͼ��С�����仯���������¼����޸Ķ�Ӧ������ �������Сֵ����������ͼλ�õı仯������
		 * ���������ֻ��ӵ�����ͼ�ϡ� modify by guogang 2007-11-15
		 * ����������������ϵ�ʱ���������ñ�ͷģ�͵Ĵ�С���������ݱ�־������
		 * 
		 * @param e
		 *            ChangeEvent
		 */
		public void stateChanged(ChangeEvent e) {
			// �����������ͼ�����,����ͼ���Եĸ��Ĳ���Ҫ֪ͨ������
			JViewport viewport = (JViewport) e.getSource();
			Point p = viewport.getViewPosition();
			// ����ͼ�Ĵ�С�����˸ı�,��ͷ��ͼ��������ͼ�����������ı䡣
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
				// ֪ͨ�������ı䡣
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
		 * ������ͼ�����λ���Ƿ����ĸ���ͼ�����ġ�
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
	 * ������ͼ�仯�Ĵ�����
	 * 
	 * @param hBar
	 * @param vBar
	 * @param horHeaderView
	 * @param verHeaderView
	 * @param horTreeView
	 * @param verTreeView
	 * @return
	 * @see ViewportChangeHandler�Ĺ��췽��
	 */
	private ChangeListener createViewportChangeListener(TableScrollBar hBar,
			TableScrollBar vBar, JViewport horHeaderView,
			JViewport verHeaderView, JViewport horTreeView,
			JViewport verTreeView) {
		return new ViewportChangeHandler(hBar, vBar, horHeaderView,
				verHeaderView, horTreeView, verTreeView);
	}

	/**
	 * �������ļ�������֪ͨ����ͼ��������ͼ������ͼ������ͼ2����ִ��ڣ��ı���ͼ��λ�á���Ҫ���⴦��
	 * ���������ǰ�ı��������ޱ��Ѿ��϶�����ͼ�����λ��,��ô����������찴ť�����ķ�Χ������
	 */
	public class SBChangeListener implements ChangeListener {
		/**
		 * ��������������ͼ
		 */
		private JViewport m_cellsview, m_cellsview2;

		/**
		 * �Ƿ�ˮƽ������false��ʶ��ֱ����
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
		 * ��Ӧ�����������޸ĵ��¼����޸Ĺ�����ͼ��λ�á�
		 * 
		 * @param view
		 *            JViewport ��������������ͼ�Ĵ���
		 * @param value
		 *            int ����������ֵ
		 * @param hor
		 *            boolean �Ƿ�ˮƽ������
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
		 * ʵ�ֽӿڵķ����� ��Ӧ�������ı���¼����������ĸı�����������û������������Ҳ����������
		 * �͹�����������һ���Ӵ���λ�øı䣬��֪ͨ�������ı䡣���ڹ������ı䶯��֪ͨ�Ӵ������Ӵ���
		 * �ı�Ҳ��֪ͨ���������Ӵ������ɵĶ����С�ı䣩������Ҫע�������ѭ�������������ֵݹ�Ĵ�����
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
				 * Ϊ���Ż���ʾ��Ч�ʣ�ֻ�е��������϶���һ���µ�����ʱ���Ÿı���ͼ��λ�á��������µĴ������ȼ���
				 * ����������ͼ��Ӧ������λ�ã�Ȼ��Ƚ����ǵ�ֵ��������ͼλ�õĻ�ȡ�����Ȼ�ȡ������ͼ��λ�ã�Ȼ���ȡ����ͼ
				 * ��λ�á�Ŀ�����������ͼ��λ�ñ��ƶ����̸ı䣬��ô��������ͼ֪ͨ�������ı䣬���ԱȽϵ�λ��
				 * �ᷢ����ͬ�������޷�֪ͨ������ͼ�ı䡣��˵�ǰ���ǣ�������в���ʾ����ô�������ִ�������з��顣
				 * ����������϶��������Ҫ��ʾ���Ԫ�����ݣ��ù������á�
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
					if (index == newIndex) { // ������ֻ�е��µ����г��ֵ�ʱ��ŵ�����ͼλ�á�
						// ���:����ͼ�������ʾ�������һ��¶������ʱ,�϶��������޷��ϳ���.***************
						// ԭ��ʱֻ�е��Ϲ��߽�ʱ���ܼ��������ƶ�ʱ��,��������Ѿ���ʾ�˰��,���������Ϲ��߽���������.
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
