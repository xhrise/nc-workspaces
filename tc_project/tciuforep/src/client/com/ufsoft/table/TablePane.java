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
 * <p>Title:���������� </p>
 * <p>Description: ����������Ϊ���ؼ����������������и����ؼ�����ʾ�����֡������ǰ�������Ҫ
 * �����ڵĲ��,��ô���������ɵ������������������.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 * @see TableLayout
 */

public class TablePane extends JComponent implements TableConstants, Pageable {

	private static final long serialVersionUID = -434057970337762938L;


	/**
	 *��ֱ����������ʾ����ȱʡΪ��Ҫʱ��ʾ������
	 */
	private int m_nVScrollBarPolicy;

	/**
	 *ˮƽ����������ʾ����ȱʡΪ��Ҫʱ��ʾ������
	 */
	private int m_nHScrollBarPolicy;

	/**
	 * �����е�����ͼ��ͨ���������ɱ�����ͼ��
	 */
	private JViewport m_MainView;

	/**
	 * ��ͷ����ͼ������
	 */
	private JViewport m_ColumnHeaderView;

	/**
	 * ��ͷ����ͼ������
	 */
	private JViewport m_RowHeaderView;
	/**
	 * �����������Ƿ�ɼ�
	 */
	private boolean m_bShowTree;
	/**
	 * ����������
	 */
	private JViewport m_RowTreeView, m_ColTreeView;

	/**
	 * ��ֱ������
	 */
	private TableScrollBar m_VScrollBar;

	/**
	 * ˮƽ������
	 */
	private TableScrollBar m_HScrollBar;

	/**
	 * ����ҳǩ����ͼ����
	 */
	private JViewport m_PageView;

	/**
	 * ҳǩ�͹�����֮��Ļ���
	 */
	private SlideBox m_SlideBox;

	/**
	 * �ĸ���������
	 */
	private Component m_upperLeft, m_upperRight, m_lowerLeft, m_lowerRight;

	/**
	 * �Ƿ���ʾҳǩ
	 */
//	private boolean m_bShowPageMark;

	/**
	 * ��¼�Ƿ������ޱ�����ԡ���һ���ڵ���������İ�ťʱ��Ҫ�ж��Ƿ���չ�ؼ���Χ��
	 */
	private boolean m_bInfinite;

	/**
	 * �����ı�����
	 */
	private CellsPane m_CellsPane;

	//*********������ڴ��ڲ�ֵ�״̬ʱ,��Ҫ����ӿ����.
	/**
	 * ��ֳ������б�����ͼ
	 */
	private JViewport m_RowHeaderView2;

	/**
	 * ��ֳ������б�����ͼ
	 */
	private JViewport m_ColumnHeaderView2;

	/**
	 * ˮƽ������ʱ��,�Ҳ���ֵ���ͼ.
	 */
	private JViewport m_RightView;

	/**
	 * ��ֱ������ʱ��,�·����ֵ���ͼ
	 */
	private JViewport m_DownView;

	/**
	 * �������������ֳ��ֵ����·�����ͼ
	 */
	private JViewport m_RightDownView;

	/**
	 * ˮƽ������,�������Ҳ��ˮƽ������
	 */
	private TableScrollBar m_HScrollBar2;

	/**
	 * ��ֱ������,�������·��Ĵ�ֱ������
	 */
	private TableScrollBar m_VScrollBar2;
	private JViewport m_RowTreeView2, m_ColTreeView2;

	/**�ָ���ͼ�ķָ���*/
	private SeperatorBar m_horBar, m_verBar;
	private SeperatorBox m_crossBox;

	/** �ڱ༭�����У����ڱ༭�����*/
	private Component editorComp;
	/** �ڱ༭�����У�ʹ�õı༭��*/
	private SheetCellEditor cellEditor;
	/** ���ڱ༭��λ��. */
	private int editingColumn = -1;
	/** ���ڱ༭��λ��. */
	private int editingRow = -1;
	/**���б�ǩ�Ŀ���*/
	private boolean m_rowHeaderShow = true, m_colHeaderShow = true;

	/**
	 * ��ӡԤ���Ի����ʵ��
	 */
	private PrintPreviewDialog m_printPreView;

	//****************************************************************************
	/**
	 * ��ʼ��һ��������塣
	 * @param c Component �����������ʾ�����
	 * @param vsbPolicy int ��ֱ�������ԣ�ȡֵΪTableConstants�й��ڹ�������ĳ���
	 * @param hsbPolicy int ˮƽ�������ԣ�ȡֵΪTableConstants�й��ڹ�������ĳ���
	 * @param showPageMark boolean �Ƿ���ʾҳǩ
	 * @param bInfinite �Ƿ������ޱ�
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
	 *  ���ָ��������������������մ����������������¼�
	 */
	private void addMouseWheelListener() {
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(getCellsModel() == null){// @edit by wangyga at 2009-2-24,����08:12:23
					return;
				}
				Component comp = getComponentAt(e.getPoint());
				if (comp instanceof JViewport) {
					JViewport view = (JViewport) comp;
					//������״̬��������ͼ����ʱ���Ϻ�����ͼ�����ܹ������Ұѹ����¼�ת������ͼ
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
						
						//��ҳ���� liuyy+
						cellsPane.paginalData();
					}
				}
			}

		});

	}

	/**
	 * ����������������һ����������ʱ�򣬿ͻ�ֻ��Ҫ����CellsPane��Header�ȣ���������ViewPort
	 * �Ĺ����������Զ�������
	 */
	private void installAllComponent() {
		TableLayout layout = new TableLayout();
		setLayout(layout);
		//��ӹ�����
		m_VScrollBar = new TableScrollBar(TableScrollBar.VERTICAL, getCells(),
				m_bInfinite);
		add(m_VScrollBar, VSCROLLBAR);
		m_HScrollBar = new TableScrollBar(TableScrollBar.HORIZONTAL,
				getCells(), m_bInfinite);
		add(m_HScrollBar, HSCROLLBAR);
		//�����б��⣬�б��⣬����ͼ��ҳǩ
		m_RowHeaderView = new UIViewport();
		m_ColumnHeaderView = new UIViewport();
		add(m_RowHeaderView, ROWHEADERVIEW);
		add(m_ColumnHeaderView, COLUMNHEADERVIEW);
		//����һ������������Ҫ������ά����������Ϣ������塣
		// @edit by wangyga at 2009-2-24,����04:02:50 ֧�ֿ��Կ�model
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
		// @edit by wangyga at 2009-3-5,����11:27:59 ��ʱ�����µ�UI���
		if(!(this instanceof ReportTable)){
			m_PageView = new UIViewport();
			add(m_PageView, PAGEVIEW);
		}
		m_SlideBox = SlideBox.createDefaultSlideBox(SlideBox.HORIZONTAL);
		add(m_SlideBox, SLIDEBOX);

		//������Ͻǵ�ȫѡ����
		SelectAllCmp selAllCmp = new SelectAllCmp(this);
		setCorner(UPPER_LEFT, selAllCmp);

		setOpaque(true);
		setUI(TablePaneUI.createUI(this));
		layout.syncWithScrollPane(this);

	}

	/**
	 * �µ�UI����ڹ���ʱ��cellsModelΪ�գ������ʼ��TableHeader,����������cellsModel֮���ٵ��ô˷���
	 * @create by wangyga at 2009-3-5,����11:24:58
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
	 * ���ñ����������Ƿ�ɼ�
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
	 * ������ģ��
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
		//����������еļ�������
		getUI().uninstallListeners();
		getUI().installListeners();
		tree.getTreeModel().refresh(null);
		revalidate();

	}

	/**
	 * ���ô�ֱ�ָ��λ��.
	 * @param ySep int ��ֱ�ָ������λ��
	 */
	public void setSeperateY(int ySep) {
		if (m_DownView != null) { //��������Ѿ������ˡ�
			if (ySep == getSeperateLockSet().getSeperateY()) {
				return;
			}
		} else {
			//��ʼ������������ڷָ���������
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
		//Ϊ����ӵ����ע�������
		TablePaneUI ui = getUI();
		ui.uninstallListeners();
		ui.installListeners();
		revalidate();
	}

	/**
	 * ���ô�ֱ�ָ��λ��.
	 * @param sepX int ˮƽ�ָ������λ��
	 */
	public void setSeperateX(int sepX) {
		if (m_RightView != null) {
			if (sepX == getSeperateLockSet().getSeperateX()) {
				return;
			}
		} else {
			//��ʼ������������
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
		//Ϊ����ӵ����ע�������
		TablePaneUI ui = getUI();
		ui.uninstallListeners();
		ui.installListeners();
		revalidate();
	}

	/**
	 * ������²�Ĵ���
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
	 * ����Ϊ����ʱ,�в���.
	 * ����Ϊ����ʱ,�в���.
	 * ������0ʱ,����Ϊ2,2.
	 * @param row
	 * @param col void
	 */
	public void setSeperatePos(int row, int col) {
		if (row <= 0 && col <= 0) {
			//@edit by liuyy at 2008-12-26 ����10:21:40
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
	 * ȡ���������.�����ڷ������������ɾ����
	 */
	public void cancelSeperate() {
		getSeperateLockSet().setSeperateRow(0);
		getSeperateLockSet().setSeperateCol(0);
		//ɾ��������
		TablePaneUI ui = getUI();
		ui.uninstallListeners();
		//ɾ�����
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
		//����ֵ
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
     * ��������ͼ,ͬʱ���CellsModelListener��SelectModelListener
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
    * �����ͷ����ͼ
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
    * �����ͷ��ͼ
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
	 * �����Ƿ񶳽�
	 * @return boolean �����Ƿ񶳽�
	 */
	public boolean getFreezing() {
		return getSeperateLockSet().isFreezing();
	}

	/**
	 * ��ֵ�Ƿ񶳽ᴰ��.������嶳��,�����Ĵ����Ϊ������,��Ҫ����������λ�û���,����2����������Ϊ��������;
	 * ����������ʱ��,��Ҫ������ȥ.�ڼ䣬��Ҫ�޸ķ������ֵĹ������Ĺ�����Χ����Сֵ������ԭ����Ӧ�Ĺ����������á�
	 * @param bFreezing
	 */
	public void setFreezing(boolean bFreezing) {
		boolean bOld = getSeperateLockSet().isFreezing();
		getSeperateLockSet().setFreezing(bFreezing);
		
		if (bOld != getSeperateLockSet().isFreezing()) {
			
			getCellsModel().setDirty(true);
			
			if (bFreezing) { //����
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
			} else { //�����л���Ϊ�����λ��
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
	 * @edit by wangyga at 2009-5-21,����04:51:43 �ɷ�tablePane���Ըı��¼�
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
	 * �õ���ֱ�������Ĳ���
	 * @return int ��ֱ�������Ĳ���
	 */
	public int getVScrollBarPolicy() {
		return m_nVScrollBarPolicy;
	}

	/**
	 * �õ�ˮƽ�������Ĳ���
	 * @return int ˮƽ�������Ĳ���
	 */
	public int getHScrollBarPolicy() {
		return m_nHScrollBarPolicy;
	}

	/**
	 * ���ô�ֱ���������ԣ�ȡֵΪTableConstants�й��ڹ�������ĳ���
	 * @param policy ��ֱ����������
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
	 * ����ˮƽ�������Ĺ���ȡֵΪTableConstants�й��ڹ�������ĳ���
	 * @param policy int ˮƽ�������Ĺ���
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
	 * �õ���ֱ�����������ṩ���ù������ķ���������ʼ��ʱ������������ֻ�ǿ���������ʾ���ԡ�
	 * @return TableScrollBar ��ֱ������
	 */
	public TableScrollBar getVScrollBar() {
		return m_VScrollBar;
	}

	/**
	 * �õ�ˮƽ�����������ṩ���ù������ķ���������ʼ��ʱ������������ֻ�ǿ���������ʾ���ԡ�
	 * @return TableScrollBar ˮƽ������
	 */
	public TableScrollBar getHScrollBar() {
		return m_HScrollBar;
	}

	/**
	 * ��������ͼ���ɵ�������������Ҫʵ��Scrollable�ӿ�
	 * @param c Component ��ӵ�����ͼ�е����
	 */
	private void setMainviewView(CellsPane c) {
		if (m_MainView == null) {
			m_MainView = new UIViewport();
		}
		m_MainView.setView(c);
		this.m_CellsPane = c;
	}

	/**
	 * ���õ�ǰ�����ӿڡ�
	 * @param viewport JViewport ����ͼ
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
	 * �õ���ǰ�����ӿڡ�
	 * @return JViewPort ����ͼ
	 */
	public JViewport getMainView() {
		return m_MainView;
	}

	/**
	 * �õ�����еı�����
	 * @return CellsPane
	 */

	public CellsPane getCells() {
		return m_CellsPane;
	}
	/**
	 * ���ݵ�Ԫ���λ�û�ȡ�����ڵ�CellsPane,��Ҫ���������з�������������
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
    * �õ�����еı�����������ģ��
    * @return
    */
	public CellsModel getCellsModel() {
		return getCells().getDataModel();
	}

	/**
	 * �õ�����ҳǩ������ͼ
	 * @return JViewport ����ҳǩ������ͼ
	 */
	public JViewport getPageView() {
		return m_PageView;
	}

	/**
	 * ����б�����ͼ
	 * @return JViewport �б�����ͼ
	 */
	public JViewport getRowHeader() {
		return m_RowHeaderView;
	}

	/**
	 * �����б�����ͼ
	 * @param rowHeader JViewport �б�����ͼ
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
	 *  �����б�����ͼ�����ɵ����
	 * @param c Component �б�����ͼ�����ɵ����
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
	 * �õ��б�������
	 * @return JViewport �б�������
	 */
	public JViewport getColumnHeader() {
		return m_ColumnHeaderView;
	}

	/**
	 *  �����б�����ͼ�����ɵ����
	 * @param c Component �б�����ͼ�����ɵ����
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
	 * �����б�����ͼ
	 * @param columnHeader JViewport �б�����ͼ
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
	 * ���ĳ���������
	 * @param key String �����ʶ������ȡֵ��Χ���£�
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
	 * ����ĳ���������
	 * @param key String �����ʶ������ȡֵ��Χ���£�
	 * <ul>
	 * <li>TableConstants.UPPER_LEFT
	 * <li>TableConstants.UPPER_RIGHT
	 * <li>TableConstants.LOWER_lEFT
	 * <li>TableConstants.LOWER_RIGHT
	 * </ul>
	 * @param corner Component ��Ҫ���õ����
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
	 * �������Ķ������������е��������Ҫ���²��֡��μ�����˵��
	 * @return boolean
	 */
	public boolean isValidateRoot() {
		return true;
	}

	/**
	 * ���ò��ֹ�����.У�鲼�ֹ��������μ�����˵��
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
	 * �μ�����˵��
	 * @param ui
	 */

	public void setUI(TablePaneUI ui) {
		super.setUI(ui);
	}

	/**
	 * �μ�����˵��
	 * @return TablePaneUI
	 */
	public TablePaneUI getUI() {
		return (TablePaneUI) ui;
	}

	/**
	 * �õ��������
	 * @return SlideBox
	 */
	public SlideBox getSlideBox() {
		return m_SlideBox;
	}

	/**
	 * �õ�ˮƽ�ָ���
	 * @return SeperatorBar
	 */
	public SeperatorBar getHorBar() {
		return m_horBar;
	}

	/**
	 * �õ���ֱ�ķָ���
	 * @return SeperatorBar
	 */
	public SeperatorBar getVerBar() {
		return m_verBar;
	}

	/**
	 * �õ��ָ������沿λ����������������ˮƽ�ʹ�ֱ����ά�ȵ��϶���
	 * @return SeperatorBox
	 */
	public SeperatorBox getCrossBox() {
		return m_crossBox;
	}

//	/**
//	 * �Ƿ���ʾҳǩ
//	 * @return boolean
//	 */
//	public boolean isShowPageMark() {
//		return m_bShowPageMark;
//	}

	/**
	 * �õ�����������·����б���
	 * @return JViewport
	 */
	public JViewport getRowHeader2() {
		return m_RowHeaderView2;
	}

	/**
	 * �õ�����������Ҳ���б���
	 * @return JViewport
	 */
	public JViewport getColumnHeader2() {
		return m_ColumnHeaderView2;
	}

	/**
	 * �õ�ˮƽ������ʱ��,�Ҳ���ֵ���ͼ.
	 * @return JViewport
	 */
	public JViewport getRightView() {
		return m_RightView;
	}

	/**
	 * �õ���ֱ������ʱ��,�·����ֵ���ͼ
	 * @return JViewport
	 */
	public JViewport getDownView() {
		return m_DownView;
	}

	/**
	 * �õ��������������ֳ��ֵ����·�����ͼ
	 * @return JViewport
	 */
	public JViewport getRightDownView() {
		return m_RightDownView;
	}

	/**
	 * �б���������ͼ���
	 * @return JViewport
	 */
	public JViewport getRowTreeView() {
		return m_RowTreeView;
	}

	/**
	 * ������ɵ��б���������ͼ���
	 * @return JViewport
	 */
	public JViewport getRowTreeView2() {
		return m_RowTreeView2;
	}

	/**
	 * �б���������ͼ���
	 * @return JViewport
	 */
	public JViewport getColTreeView() {
		return m_ColTreeView;
	}

	/**
	 * ������ɵ��б���������ͼ���
	 * @return JViewport
	 */
	public JViewport getColTreeView2() {
		return m_ColTreeView2;
	}

	/**
	 * ˮƽ������,�������Ҳ��ˮƽ������
	 * @return TableScrollBar
	 */
	public TableScrollBar getHScrollBar2() {
		return m_HScrollBar2;
	}

	/**
	 * ��ֱ������,�������·��Ĵ�ֱ������
	 * @return TableScrollBar
	 */
	public TableScrollBar getVScrollBar2() {
		return m_VScrollBar2;
	}

	/**
	 * ���ڷָ��ˮƽ���ص�λ��
	 * @return int
	 */
	public int getSeperateX() {
		//��ֹ�϶��и��п�ʱ��m_nSeperateX�����Զ����¡��������и��п�仯ʱ���£����ټ�������
		getSeperateLockSet().setSeperateX(
				getCells().getDataModel().getColumnHeaderModel().getPosition(
						getSeperateCol()));
		return getSeperateLockSet().getSeperateX();
	}

	/**
	 * ���ڷָ��ˮƽ���ص�λ��
	 * @return int
	 */
	public int getSeperateY() {
		//��ֹ�϶��и��п�ʱ��m_nSeperateX�����Զ����¡�
		getSeperateLockSet().setSeperateY(
				getCells().getDataModel().getRowHeaderModel().getPosition(
						getSeperateRow()));
		return getSeperateLockSet().getSeperateY();
	}

	/**
	 * ������������
	 * @return int
	 */
	public int getSeperateRow() {
		return getSeperateLockSet().getSeperateRow();
	}

	/**
	 * ������������
	 * @return int
	 */
	public int getSeperateCol() {
		return getSeperateLockSet().getSeperateCol();
	}

	/**
	 * �����Ƿ񶳽�
	 * @return boolean
	 */
	public boolean isFreezing() {
		if(getCellsModel() == null){
			return false;
		}
		return getSeperateLockSet().isFreezing();
	}

	//************************************
	//ʵ�ַ�ҳ��ӡ�Ľӿ�
	//***********************************
	/**��ǰϵͳ�Ĵ�ӡ����*/
	private PrinterJob printerJob;

	/**��ӡ���õ���Ϣ*/
	//	private PrintSet m_printSet;Ǩ�Ƶ�CellsModel��
	public PrinterJob getPrinterJob() {
		if (printerJob == null) {
			printerJob = PrinterJob.getPrinterJob();
		}
		printerJob.setPageable(this);
		return printerJob;
	}

	/**
	 * ���ô�ӡ��Ϣ
	 * @param ps
	 */
	public void setPrintSet(PrintSet ps) {
		getCellsModel().setPrintSet(ps);
	}

	/**
	 * �õ���ӡ������Ϣ
	 * @return PrintSet
	 */
	public PrintSet getPrintSet() {
		return getCellsModel().getPrintSet();
	}

	/**
	 * ��ӡ��ҳ������
	 *
	 * �����JDK1.4.2�µ���ҳ�����öԻ����ʱ�򱳾�����ˢ�� update by CaiJie 2005-10-28
	 */
	public void pageFromat() {
		//		PrintSet ps = getPrintSet();
		//		PageFormat oldPf = ps.getPageformat();
		//        PageFormat newPf = getPrinterJob().pageDialog(oldPf);
		//		//������Ҫ��鷵�ص�ҳ�������Ƿ�Ϸ���������Ϸ�����Ҫ������
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
				//������Ҫ��鷵�ص�ҳ�������Ƿ�Ϸ���������Ϸ�����Ҫ������
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
			// TODO �Զ����� catch ��
			AppDebug.debug(e);
		}

	}

	/**
	 * ��ӡ
	 * @return boolean
	 */
	public boolean print() {
		return print(true);
	}

	/**
	 * ��ӡ
	 * @param showPrintDlg ��ӡʱ�Ƿ񵯳���ӡ���öԻ���
	 * @return boolean
	 * @i18n report00015=��ӡ�����ܴ����ӡ������ȷ�ϴ�ӡ���������������ԡ�
	 */
	public boolean print(boolean showPrintDlg) {
		if (getPrintPageRect().length == 0) {
			return false;
		}
		boolean result = false;
		
		//�˴�����û�йرյ�ǰ�ı�����ͼ������Ҫ���ò�������Ҫ���»���
		
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
	 * ����������ӡ�����������ӡ���������Ҳ�������ӡ���öԻ���
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
	 * ָ����ӡ����
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
	 * ��ӡ�Ĵ�ӡԤ����
	 * modify by guogang 2007-12-11 
	 * ������ӡԤ����ʱ��ͨ���޸Ĵ�ӡ������Ԥ����ʱ�����ԭ�ȵĽ��泬����ʾ����
	 * ��PrintPreviewDialog.freshPrintPanel()�У�printPane.removeAll()��
	 * ֻ�ǵ���printPane.repaint()����������������PrintPreview
	 * ��û���κ���ʾ�����
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
			//Ԥ����ʱ��Ҫ���»���
			if(!m_printPreView.isVisible()){
			  m_printPreView.setVisible(true);
			}else{
			  m_printPreView.show();
			}
			
			  

		} finally {
		

		}
	}
 

	/**
	 * ��ҳ���㷽����
	 * ��ǰ������Ҫ�Ǽ��㰴�մ�ӡ���ã���Ҫ������ÿ����ӡҳ��Ĵ�ӡ����
	 * �������¹�����㣺
	 * ���ȴӴ�ӡ�����еõ���ӡý���ϵĴ�ӡ����
	 * �ø������ֵ���Ա������õ���ý���ϴ�ӡ�����ݶ�Ӧ���ռ�õ�����
	 * ���������ÿ��ý��ռ�õ�����������,����ǿ�Ʒ����У��õ���Ҫ��ӡ��ҳ���������Ȼ��ÿ��ý����ҳ����������������ء�
	 * ����û������˹̶�����ͷβ������ҳ���С��Ҫ��ȥ����ͷβ��С�������Ҫ��ӡ��������Ҫ�޳��̶�������β��λ�á�
	 * @return Rectangle[] �����¼ÿҳ�����ڱ��ؼ��϶�Ӧ��λ��
	 * modify by guogang 2007-6-27 �����˴�ӡ����ʾ��һ�µ���������ӡʱ�����һҳ
	 * ������������ԭ���Ǵ�ӡʱ����ȡ��Ԫ�񳤶�ʱû�г���PRINT_SCALE����Ԥ��ʱ�ǳ���PRINT_SCALE��
	 * modify by guogang 2007-7-20 �����˴�ӡ����ҳʱ�ķ�ҳ����
	 * add by guogang 2007-12-5 �ڼ����ӡ�����ʱ��û�м���ҳü��ҳ�ŵĸ߶ȣ��������ڴ�ӡ��ʱ�����ӡ��ֽ�ű߾���������Ҫ��ֽ�Ŵ�ӡ�����ӡ�Ļ�����Ҫ������
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
		
		//�жϴ�ӡ
		int[] nPrintArea = new int[] { -1, -1, 0, 0 };
		int[] nRowHeadRang = new int[] { -1, 0 };
		int[] nColHeadRang = new int[] { -1, 0 };
		if (ps.getPrintArea() != null) {
			System.arraycopy(ps.getPrintArea(), 0, nPrintArea, 0, ps
					.getPrintArea().length);//�������鱣֤���ı�ԭ��ps�е���ֵ��
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
		//���ȼ������������Ҫ��ӡ��������������˹̶�����ͷβ����Ҫɾ���̶����������ӡ������û�У���ô����������������Ϣ������
		if (nPrintArea[0] == -1) {//��ʼ��δ����
			nPrintArea[0] = 0;
		}
		if (nPrintArea[1] == -1) {//��ʼ��δ����
			nPrintArea[1] = 0;
		}
		if (nPrintArea[2] == 0 || nPrintArea[2] > nMaxRow) {//������δ����
			nPrintArea[2] = nMaxRow;
		}
		if (nPrintArea[3] == 0 || nPrintArea[3] > nMaxCol) {//������δ����
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
		
		//���������������ӡ������Ĵ�С�����Ǵ�ӡý�ʵĴ�С�Ȱ��մ�ӡ����ӳ���Ϊ��Ӧ����Ĵ�С��Ȼ���ȥ�̶����еĴ�С��
		double scale = ps.getPrintScale();
		//�ڴ�ӡ�����£����ý��ÿҳ��Ӧ����ĳߴ硣
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
		//����ÿ��ҳ���������Ĵ�С���ָ����У���֤���д�ӡ��������֤ǿ�Ʒ�ҳ����֤�����߳�ʼ�����д�ӡ�����ı��ߣ�
		//�ָ��С�
		pageStartRow = nPrintArea[0];
		pageStartCol = nPrintArea[1];
		nMaxRow = nPrintArea[2];
		nMaxCol = nPrintArea[3];
//		//ֻ��ӡ��Ч�������򣨼��������ݻ��ʽ�����򣩡�
//		int[] validHeaders = getCells().getDataModel().getValidHeaders();
//		//����ӡmaxRow��maxCol.
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
		//�������е����ȹ��������д�ӡ�����˳��.
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
	 * ��ҳ��ʱ��������е����ȹ��������д�ӡ�����˳��
	 * @param model1 ���ȼ��ߵı�ͷ����ģ��
	 * @param model2 ���ȼ��͵ı�ͷ����ģ��
	 * @param aryBreak1  ���ȼ��ߵķ���ķ�ҳ��ʼ������
	 * @param aryBreak2  ���ȼ��͵ķ���ķ�ҳ��ʼ������
	 * @param num1 ���ȼ��ߵķ���ķ�ҳ��ҳ��
	 * @param num2 ���ȼ��͵ķ���ķ�ҳ��ҳ��
	 * @param nMax1 ���ȼ��ߵķ�������Ԫ��
	 * @param nMax2 ���ȼ��͵ķ�������Ԫ��
	 * @param pagePositons ��ҳ��������
	 * @param colPriority �Ƿ���������
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
						+ model1.getSize(nMax1 - 1);// + 2;//�������б�֤��ӡ���ֱ��ߡ�
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
	 * ���ط�ҳ��ʼλ���б�
	 * 
	 * @param hModel ��ͷ����
	 * @param nMaxH ���Ԫ��
	 * @param mediaSize ��ҳÿҳ�Ĵ�С
	 * @param pageStartPos ��ʼλ��
	 * @return ArrayList ��ҳ��ʼλ���б�
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
		// ��¼ÿ�ηָ�ľ������ʼλ�ã�������Ϣ��0Ϊ��ʼλ�á�
		ArrayList<Integer> aryBreak = new ArrayList<Integer>();
		while (pageStartPos < nMaxH) {

			aryBreak.add(new Integer(pageStartPos));
			// modify by guogang 2007-6-27
			// ����̶��������㲻��(0,0)�Ļ�hModel.getPosition(pageStartPos) +
			// mediaSize�ͳ����˵�һҳ�ĳ���
			// ����ҳ�Ĵ��������ҳ�����˳��ȣ�˵���̶�ͷ���Ǵ�(0,0)��ʼ��
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
			// ���ǿ�Ʒ�ҳ�ж�
			newStartPos = resetStartPages(newStartPos, pageStartPos, pages);
			if (newStartPos == -1 || newStartPos >= nMaxH) {
				break;
			}
			if (newStartPos <= pageStartPos) {// ����������ӡ�����޷�����һ�����ݡ���ʾ����
				JOptionPane.showMessageDialog(this, MultiLang
						.getString("print_media_too_small"));
				return new ArrayList();
			}
			pageStartPos = newStartPos;
			
			
		}
		return aryBreak;
	}

	/**
	 * ���÷�ҳ����ʼλ��
	 * @param newStartRow �����Ƿ�ҳ������ʼλ��
	 * @param lastStartPos ��ҳ��ʼλ��
	 * @param pages ǿ�Ʒ�ҳλ��
	 * @return int ���Ƿ�ҳ����ʵλ��
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
	 * ���㵱ǰ�ĵ���Ҫ��ӡ��ҳ����
	 * @return the number of pages in this <code>Pageable</code>.
	 */
	public int getNumberOfPages() {
		return getPrintPageRect().length;
	}

	/**
	 * ��ȡ��ӡ����ʵ��������ʵ�ʵĴ�ӡ����
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
				// //��ͼ����ƶ�ҳ�߾��ƫ��.
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
	 * ��ȡ��ӡҳ������
	 * @param pageIndex int
	 * @return PageFormat
	 */
	public PageFormat getPageFormat(int pageIndex) {
		//		return getPrintSet().getPageformat();//���ֱ�ӷ������ֵ��Imageable�����޷���ʾҳüҳ�ţ�
		PageFormat pf = (PageFormat) getPrintSet().getPageformat().clone();
		Paper paper = pf.getPaper();
		paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
		pf.setPaper(paper);
		return pf;
	}

	/**
	 *�������ڱ༭����
	 * @param aColumn
	 */
	public void setEditingColumn(int aColumn) {
		editingColumn = aColumn;
	}

	/**
	 *�������ڱ༭����
	 * @param aRow
	 */
	public void setEditingRow(int aRow) {
		editingRow = aRow;
	}

	/**
	 * �õ���ǰ�༭������С����û�б༭��Ԫ�����ǣ�1��
	 * @return  int ��ǰ�༭������С����û�б༭��Ԫ�����ǣ�1��
	 */
	public int getEditingColumn() {
		return editingColumn;
	}

	/**
	 * �õ���ǰ�༭������С����û�б༭��Ԫ�����ǣ�1��
	 * @return  int ��ǰ�༭������С����û�б༭��Ԫ�����ǣ�1��
	 */
	public int getEditingRow() {
		return editingRow;
	}

	/**
	 * �õ���ǰ��ҳ�ı༭��
	 * @return SheetCellEditor
	 */
	public SheetCellEditor getCellEditor() {
		return cellEditor;
	}

	/**
	 * ���õ�ǰ�༭��
	 * @param anEditor
	 */
	public void setCellEditor(SheetCellEditor anEditor) {
		if (anEditor != cellEditor) {
			cellEditor = anEditor;
		}
	}

	/**
	 * �õ��༭�����
	 * @return Component
	 */
	public Component getEditorComp() {
		return editorComp;
	}

	/**
	 * ���ñ༭�����
	 * @param editorComp Component
	 */
	public void setEditorComp(Component editorComp) {
		this.editorComp = editorComp;
	}

	/**
	 * �޸��б�ǩ����
	 * @param show
	 */
	public void setRowHeaderVisible(boolean show) {
		if (show != m_rowHeaderShow) {
			m_rowHeaderShow = show;
			//�޸��ӿڵĿ���
			getRowHeader().setVisible(show);
			if (getRowHeader2() != null) {
				getRowHeader2().setVisible(show);
			}
		}
	}

	/**
	 * �޸��б�ǩ����
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
	 * ���������ͼ����ʾ������
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
	 * ����ӿڵ���ʾ����
	 * @return double
	 */
	public static double getViewScale() {
		return TableStyle.getViewScale();
	}

	
	/**
	 * @param showPageMark Ҫ���õ� bShowPageMark��
	 */
//	public void setShowPageMark(boolean showPageMark) {
//		m_bShowPageMark = showPageMark;
//		((TableLayout) getLayout()).syncWithScrollPane(this);
//	}

	/**
	 * @return ���� m_frozenNoSplit��
	 */
	public boolean isFrozenNoSplit() {
		return getSeperateLockSet().isFrozenNoSplit();
	}

	/**
	 * @param noSplit Ҫ���õ� m_frozenNoSplit��
	 */
	public void setFrozenNoSplit(boolean frozenNoSplit) {
		getSeperateLockSet().setFrozenNoSplit(frozenNoSplit);
		firePropertyChange("seperate2lock", null, null);

	}

	//    /**********�����޸�֪ͨ**********/
	//    private transient PropertyChangeSupport support = new PropertyChangeSupport(this);
	//    public synchronized void addPropertyChangeListener(
	//            String propertyName,
	//            PropertyChangeListener listener) {
	//        support.addPropertyChangeListener(propertyName,listener);
	//    }
	/**
	 * ģ��������룬���������ж��Ƿ���Ա༭���༭�������ת����
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