/*
 * 创建日期 2005-5-24
 */
package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.cquery.IconPath;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.iuforeport.businessquery.OrderbyFldVO;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.querymodel.QueryBaseVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zjb
 * 
 * 排序字段设置界面
 */
public class SetOrderbyPanel extends AbstractQueryDesignSetPanel implements
		ActionListener, MouseListener, MouseMotionListener,
		ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0056";//"排序字段";

	private class sortListCellRenderer extends nc.ui.pub.beans.UILabel implements
			ListCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private ImageIcon iiAscending = new ImageIcon(getClass().getResource(
				IconPath.PATH_ASCENDING));

		private ImageIcon iiDescending = new ImageIcon(getClass().getResource(
				IconPath.PATH_DESCENDING));

		public sortListCellRenderer() {
			super();
			setOpaque(true);
			//setForeground(Color.black);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Font font = list.getFont();
			if (font != null)
				setFont(font);
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			setHorizontalTextPosition(SwingConstants.RIGHT);
			if (((Boolean) m_vecSortOrders.get(index)).booleanValue())
				setIcon(iiAscending);
			else
				setIcon(iiDescending);
			setText(value + "");
			return this;
		}
	}

	private Color background = new Color(204, 204, 204); //Style.getColor("业务界面.背景");

	private int m_nDragDirection = 0;

	private int[] m_nSelectedItems = null;

	private Object[] m_objTableColumns = null; //参与排序项VO数组

	private ArrayList<Integer> m_vecSelectableFields = null; //可选排序字段向量

	private ArrayList<Integer> m_vecSortFields = null; //排序字段向量

	private ArrayList<Boolean> m_vecSortOrders = null; //排序方式向量

	private ArrayList<Rectangle> m_vecMoveItems = new ArrayList<Rectangle>();

	private Point ptListSelectCorner = new Point(0, 0);

	private Point ptListSortCorner = new Point(0, 0);

	private Point ptOrigin = new Point(0, 0);

	private Point ptListSelectable = new Point(-100, -100);

	private Point ptListSort = new Point(-100, -100);

	//移位图标
	public ImageIcon m_iiTop = new ImageIcon(getClass().getResource(
			IconPath.PATH_TOP));

	public ImageIcon m_iiBottom = new ImageIcon(getClass().getResource(
			IconPath.PATH_BOTTOM));

	public ImageIcon m_iiUp = new ImageIcon(getClass().getResource(
			IconPath.PATH_UP));

	public ImageIcon m_iiDown = new ImageIcon(getClass().getResource(
			IconPath.PATH_DOWN));

	//常数
	public static final int MOVE_RIGHT = 0;

	public static final int MOVE_LEFT = 1;

	public static final int MOVE_UP = 2;

	public static final int MOVE_DOWN = 3;

	private UIButton ivjbtnAdd = null;

	private UIButton ivjbtnAddAll = null;

	private UIButton ivjbtnBottom = null;

	private UIButton ivjbtnDown = null;

	private UIButton ivjbtnRemove = null;

	private UIButton ivjbtnTop = null;

	private UIButton ivjbtnUp = null;

	private UIList ivjlistSelectableFields = null;

	private UIList ivjlistSortFields = null;

	private UIButton ivjbtnRemoveAll = null;

	private UIPanel ivjPnChoose = null;

	private UIPanel ivjPnMove = null;

	private UIScrollPane ivjSclPnSelectable = null;

	private UIScrollPane ivjSclPnSort = null;

	private UIPanel ivjPn1 = null;

	private UIPanel ivjPn2 = null;

	/**
	 * TableSorter 构造子注解。
	 */
	public SetOrderbyPanel() {
		super();
		initialize();
	}

	/**
	 * actionPerformed 方法注解。
	 */
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o instanceof UIButton) {
			String strButtonName = ((UIButton) o).getName();
			if (strButtonName.equalsIgnoreCase("btnAddAll")) {
				addAll();
			} else if (strButtonName.equalsIgnoreCase("btnAdd")) {
				int[] indices = getlistSelectableFields().getSelectedIndices();
				moveItems(MOVE_RIGHT, indices);
			} else if (strButtonName.equalsIgnoreCase("btnRemoveAll")) {
				removeAll();
			} else if (strButtonName.equalsIgnoreCase("btnRemove")) {
				int[] indices = getlistSortFields().getSelectedIndices();
				moveItems(MOVE_LEFT, indices);
			} else if (strButtonName.equalsIgnoreCase("btnTop")) {
				levelItems(MOVE_UP, getlistSortFields().getSelectedIndices(), 0);
			} else if (strButtonName.equalsIgnoreCase("btnUp")) {
				levelItems(MOVE_UP, getlistSortFields().getSelectedIndices(),
						getlistSortFields().getMinSelectionIndex() - 1);
			} else if (strButtonName.equalsIgnoreCase("btnDown")) {
				levelItems(MOVE_DOWN, getlistSortFields().getSelectedIndices(),
						getlistSortFields().getMaxSelectionIndex() + 2);
			} else if (strButtonName.equalsIgnoreCase("btnBottom")) {
				levelItems(MOVE_DOWN, getlistSortFields().getSelectedIndices(),
						getlistSortFields().getModel().getSize());
			}
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-2-2 14:45:41)
	 */
	public void addAll() {
		if (m_vecSelectableFields == null) {
			return;
		}
		int[] indices = new int[m_vecSelectableFields.size()];
		for (int i = 0; i < indices.length; i++)
			indices[i] = i;
		moveItems(MOVE_RIGHT, indices);
	}

	/**
	 * 返回 btnAdd 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getbtnAdd() {
		if (ivjbtnAdd == null) {
			try {
				ivjbtnAdd = new UIButton();
				ivjbtnAdd.setName("btnAdd");
				ivjbtnAdd.setText(">");
				ivjbtnAdd.setMargin(new Insets(2, 2, 2, 2));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnAdd;
	}

	/**
	 * 返回 btnAddAll 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getbtnAddAll() {
		if (ivjbtnAddAll == null) {
			try {
				ivjbtnAddAll = new UIButton();
				ivjbtnAddAll.setName("btnAddAll");
				ivjbtnAddAll.setPreferredSize(new Dimension(50, 22));
				ivjbtnAddAll.setText(">>");
				ivjbtnAddAll.setMargin(new Insets(2, 2, 2, 2));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnAddAll;
	}

	/**
	 * 返回 btnBottom 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getbtnBottom() {
		if (ivjbtnBottom == null) {
			try {
				ivjbtnBottom = new UIButton();
				ivjbtnBottom.setName("btnBottom");
				ivjbtnBottom.setIcon(null);
				ivjbtnBottom.setText("");
				ivjbtnBottom.setMargin(new Insets(2, 2, 2, 2));
				// user code begin {1}
				ivjbtnBottom.setIcon(m_iiBottom);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnBottom;
	}

	/**
	 * 返回 btnDown 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getbtnDown() {
		if (ivjbtnDown == null) {
			try {
				ivjbtnDown = new UIButton();
				ivjbtnDown.setName("btnDown");
				ivjbtnDown.setIcon(null);
				ivjbtnDown.setText("");
				ivjbtnDown.setMargin(new Insets(2, 2, 2, 2));
				// user code begin {1}
				ivjbtnDown.setIcon(m_iiDown);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnDown;
	}

	/**
	 * 返回 btnRemove 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getbtnRemove() {
		if (ivjbtnRemove == null) {
			try {
				ivjbtnRemove = new UIButton();
				ivjbtnRemove.setName("btnRemove");
				ivjbtnRemove.setText("<");
				ivjbtnRemove.setMargin(new Insets(2, 2, 2, 2));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnRemove;
	}

	/**
	 * 返回 btRemoveAll 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getbtnRemoveAll() {
		if (ivjbtnRemoveAll == null) {
			try {
				ivjbtnRemoveAll = new UIButton();
				ivjbtnRemoveAll.setName("btnRemoveAll");
				ivjbtnRemoveAll.setText("<<");
				ivjbtnRemoveAll.setMargin(new Insets(2, 2, 2, 2));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnRemoveAll;
	}

	/**
	 * 返回 btnTop 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getbtnTop() {
		if (ivjbtnTop == null) {
			try {
				ivjbtnTop = new UIButton();
				ivjbtnTop.setName("btnTop");
				ivjbtnTop.setIcon(null);
				ivjbtnTop.setText("");
				ivjbtnTop.setMargin(new Insets(2, 2, 2, 2));
				// user code begin {1}
				ivjbtnTop.setIcon(m_iiTop);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnTop;
	}

	/**
	 * 返回 btnUp 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getbtnUp() {
		if (ivjbtnUp == null) {
			try {
				ivjbtnUp = new UIButton();
				ivjbtnUp.setName("btnUp");
				ivjbtnUp.setIcon(null);
				ivjbtnUp.setText("");
				ivjbtnUp.setMargin(new Insets(2, 2, 2, 2));
				// user code begin {1}
				ivjbtnUp.setIcon(m_iiUp);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbtnUp;
	}

	/**
	 * 返回 listSelectableFields 特性值。
	 * 
	 * @return UIList
	 */
	/* 警告：此方法将重新生成。 */
	private UIList getlistSelectableFields() {
		if (ivjlistSelectableFields == null) {
			try {
				ivjlistSelectableFields = new UIList();
				ivjlistSelectableFields.setName("listSelectableFields");
				//ivjlistSelectableFields.setBackground(Color.white);
				ivjlistSelectableFields.setBounds(0, 0, 160, 120);
				// user code begin {1}
				ivjlistSelectableFields
						.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				/*
				 * ivjlistSelectableFields = new UIList() { public void
				 * paint(Graphics g) { super.paint(g); if (m_nDragDirection != 0 &&
				 * !ptListSelectable.equals(new Point(-100, -100)) &&
				 * !m_vecMoveItems.isEmpty()) { Color oldColor = g.getColor();
				 * g.setColor(Color.lightGray); for (int i = 0; i <
				 * m_vecMoveItems.size(); i++) { Rectangle rec = (Rectangle)
				 * m_vecMoveItems.elementAt(i); g.drawRect( rec.x +
				 * ptListSelectable.x - ptOrigin.x, rec.y + ptListSelectable.y -
				 * ptOrigin.y, rec.width, rec.height); } g.setColor(oldColor); } } };
				 */
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlistSelectableFields;
	}

	/**
	 * 返回 listSortFields 特性值。
	 * 
	 * @return UIList
	 */
	/* 警告：此方法将重新生成。 */
	private UIList getlistSortFields() {
		if (ivjlistSortFields == null) {
			try {
				ivjlistSortFields = new UIList();
				ivjlistSortFields.setName("listSortFields");
				//ivjlistSortFields.setBackground(Color.white);
				ivjlistSortFields.setBounds(0, 0, 160, 120);
				// user code begin {1}
				ivjlistSortFields
						.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				/*
				 * ivjlistSortFields = new UIList() { public void paint(Graphics
				 * g) { super.paint(g); if (m_nDragDirection != 0 &&
				 * !ptListSort.equals(new Point(-100, -100)) &&
				 * !m_vecMoveItems.isEmpty()) { Color oldColor = g.getColor();
				 * g.setColor(Color.lightGray); for (int i = 0; i <
				 * m_vecMoveItems.size(); i++) { Rectangle rec = (Rectangle)
				 * m_vecMoveItems.elementAt(i); g.drawRect( rec.x + ptListSort.x -
				 * ptOrigin.x, rec.y + ptListSort.y - ptOrigin.y, rec.width,
				 * rec.height); } g.setColor(oldColor); } } };
				 */
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlistSortFields;
	}

	/**
	 * 返回列名 创建日期：(2000-8-29 17:25:40)
	 * 
	 * @return java.lang.String[]
	 */
	public Object[] getObjTableColumns() {
		return m_objTableColumns;
	}

	/**
	 * 返回 Pn1 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPn1() {
		if (ivjPn1 == null) {
			try {
				ivjPn1 = new UIPanel();
				ivjPn1.setName("Pn1");
				ivjPn1.setLayout(new BorderLayout());
				getPn1().add(getSclPnSelectable(), "Center");
				getPn1().add(getPnChoose(), "East");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPn1;
	}

	/**
	 * 返回 Pn2 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPn2() {
		if (ivjPn2 == null) {
			try {
				ivjPn2 = new UIPanel();
				ivjPn2.setName("Pn2");
				ivjPn2.setLayout(new BorderLayout());
				getPn2().add(getSclPnSort(), "Center");
				getPn2().add(getPnMove(), "East");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPn2;
	}

	/**
	 * 返回 UIPanel1 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnChoose() {
		if (ivjPnChoose == null) {
			try {
				ivjPnChoose = new UIPanel();
				ivjPnChoose.setName("PnChoose");
				ivjPnChoose.setPreferredSize(new Dimension(55, 113));
				ivjPnChoose.setLayout(getPnChooseVerticalFlowLayout());
				ivjPnChoose.setBackground(background);
				getPnChoose().add(getbtnAdd(), getbtnAdd().getName());
				getPnChoose().add(getbtnAddAll(), getbtnAddAll().getName());
				getPnChoose().add(getbtnRemove(), getbtnRemove().getName());
				getPnChoose().add(getbtnRemoveAll(),
						getbtnRemoveAll().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnChoose;
	}

	/**
	 * 返回 PnChooseVerticalFlowLayout 特性值。
	 * 
	 * @return com.borland.jbcl.layout.VerticalFlowLayout
	 */
	/* 警告：此方法将重新生成。 */
	private com.borland.jbcl.layout.VerticalFlowLayout getPnChooseVerticalFlowLayout() {
		com.borland.jbcl.layout.VerticalFlowLayout ivjPnChooseVerticalFlowLayout = null;
		try {
			/* 创建部件 */
			ivjPnChooseVerticalFlowLayout = new com.borland.jbcl.layout.VerticalFlowLayout();
			ivjPnChooseVerticalFlowLayout
					.setAlignment(com.borland.jbcl.layout.VerticalFlowLayout.MIDDLE);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnChooseVerticalFlowLayout;
	}

	/**
	 * 返回 UIPanel2 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnMove() {
		if (ivjPnMove == null) {
			try {
				ivjPnMove = new UIPanel();
				ivjPnMove.setName("PnMove");
				ivjPnMove.setPreferredSize(new Dimension(50, 113));
				ivjPnMove.setLayout(getPnMoveVerticalFlowLayout());
				ivjPnMove.setBackground(background);
				getPnMove().add(getbtnTop(), getbtnTop().getName());
				getPnMove().add(getbtnUp(), getbtnUp().getName());
				getPnMove().add(getbtnDown(), getbtnDown().getName());
				getPnMove().add(getbtnBottom(), getbtnBottom().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnMove;
	}

	/**
	 * 返回 PnMoveVerticalFlowLayout 特性值。
	 * 
	 * @return com.borland.jbcl.layout.VerticalFlowLayout
	 */
	/* 警告：此方法将重新生成。 */
	private com.borland.jbcl.layout.VerticalFlowLayout getPnMoveVerticalFlowLayout() {
		com.borland.jbcl.layout.VerticalFlowLayout ivjPnMoveVerticalFlowLayout = null;
		try {
			/* 创建部件 */
			ivjPnMoveVerticalFlowLayout = new com.borland.jbcl.layout.VerticalFlowLayout();
			ivjPnMoveVerticalFlowLayout
					.setAlignment(com.borland.jbcl.layout.VerticalFlowLayout.MIDDLE);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnMoveVerticalFlowLayout;
	}

	/**
	 * 获得排序字段设置结果 创建日期：(2003-10-15 15:52:09)
	 * 
	 * @return nc.vo.iuforeport.businessquery.OrderbyFldVO
	 */
	public OrderbyFldVO[] getResultFromOrderby() {
		OrderbyFldVO[] ofs = null;
		int iSize = getlistSortFields().getModel().getSize();
		if (iSize != 0) {
			ofs = new OrderbyFldVO[iSize];
			for (int i = 0; i < iSize; i++) {
				SelectFldVO sf = (SelectFldVO) getlistSortFields().getModel()
						.getElementAt(i);
				sf = (SelectFldVO) sf.clone();
				ofs[i] = new OrderbyFldVO();
				ofs[i].setExpression(sf.getExpression());
				ofs[i].setFldname(sf.getFldname());
				ofs[i].setFldalias(sf.getFldalias());
				ofs[i].setAsc(UFBoolean.valueOf(m_vecSortOrders.get(i).toString()));
			}
		}
		return ofs;
	}

	/**
	 * 返回 UIScrollPane1 特性值。
	 * 
	 * @return UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private UIScrollPane getSclPnSelectable() {
		if (ivjSclPnSelectable == null) {
			try {
				ivjSclPnSelectable = new UIScrollPane();
				ivjSclPnSelectable.setName("SclPnSelectable");
				getSclPnSelectable().setViewportView(getlistSelectableFields());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSclPnSelectable;
	}

	/**
	 * 返回 UIScrollPane2 特性值。
	 * 
	 * @return UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private UIScrollPane getSclPnSort() {
		if (ivjSclPnSort == null) {
			try {
				ivjSclPnSort = new UIScrollPane();
				ivjSclPnSort.setName("SclPnSort");
				getSclPnSort().setViewportView(getlistSortFields());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSclPnSort;
	}

	/**
	 * 获得排序选中字段向量 创建日期：(01-3-9 10:09:16)
	 * 
	 * @return java.util.ArrayList
	 */
	public ArrayList getSortData() {
		ArrayList<ArrayList<Object>> vecSortData = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> vecTemp = null;
		for (int i = 0; i < getlistSortFields().getModel().getSize(); i++) {
			vecTemp = new ArrayList<Object>();
			vecTemp.add(getlistSortFields().getModel().getElementAt(i));
			vecTemp.add(m_vecSortOrders.get(i));
			vecSortData.add(vecTemp);
		}
		return vecSortData;
	}

	/**
	 * 返回 SorterPanelGridLayout 特性值。
	 * 
	 * @return GridLayout
	 */
	/* 警告：此方法将重新生成。 */
	private GridLayout getSorterPanelGridLayout() {
		GridLayout ivjSorterPanelGridLayout = null;
		try {
			/* 创建部件 */
			ivjSorterPanelGridLayout = new GridLayout();
			ivjSorterPanelGridLayout.setColumns(2);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjSorterPanelGridLayout;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		AppDebug.debug("--------- 未捕捉到的异常 ---------");//@devTools System.out.println("--------- 未捕捉到的异常 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * 初始化监听 创建日期：(01-4-23 9:48:04)
	 */
	private void initConnect() {
		getbtnAddAll().addActionListener(this);
		getbtnAdd().addActionListener(this);
		getbtnRemoveAll().addActionListener(this);
		getbtnRemove().addActionListener(this);
		getbtnTop().addActionListener(this);
		getbtnUp().addActionListener(this);
		getbtnDown().addActionListener(this);
		getbtnBottom().addActionListener(this);
		getlistSelectableFields().addListSelectionListener(this);
		getlistSelectableFields().addMouseListener(this);
		//getlistSelectableFields().addMouseMotionListener(this);
		// //禁止拖动，因为界面大小可调
		getlistSortFields().addListSelectionListener(this);
		getlistSortFields().addMouseListener(this);
		//getlistSortFields().addMouseMotionListener(this); //禁止拖动，因为界面大小可调
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			//可视化存储后注意修改以下方法：
			//getlistSelectableFields()，getlistSortFields()
			//注释本方法的以下语句：
			//setBackground(background);
			//ivjPnChoose.setBackground(background);
			//ivjPnMove.setBackground(background);
			// user code end
			setName("SorterPanel");
			setLayout(getSorterPanelGridLayout());
			setBackground(background);
			setSize(560, 220);
			add(getPn1(), getPn1().getName());
			add(getPn2(), getPn2().getName());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		ptListSelectCorner.setLocation(getSclPnSelectable().getLocation());
		ptListSortCorner.setLocation(getSclPnSort().getLocation());
		//初始化监听
		initConnect();
		// user code end
	}

	/**
	 * 是否可释放 创建日期：(2000-9-7 16:06:23)
	 * 
	 * @return boolean
	 * @param pt
	 *            Point
	 */
	private boolean isReleasable(Point pt) {
		Point ptCal = new Point(pt);
		Rectangle rectContainer = new Rectangle(0, 0, 0, 0);
		if (m_nDragDirection == 1) {
			ptCal.translate(ptListSelectCorner.x, ptListSelectCorner.y);
			rectContainer.setLocation(ptListSortCorner);
			rectContainer.setSize(getlistSortFields().getWidth(),
					getlistSortFields().getHeight());
			return rectContainer.contains(ptCal);
		} else if (m_nDragDirection == -1) {
			ptCal.translate(ptListSortCorner.x, ptListSortCorner.y);
			rectContainer.setLocation(ptListSelectCorner);
			rectContainer.setSize(getlistSelectableFields().getWidth(),
					getlistSelectableFields().getHeight());
			return rectContainer.contains(ptCal);
		}
		return false;
	}

	/**
	 * 调整排序项顺序 创建日期：(2000-8-30 10:22:29)
	 * 
	 * @param nDirection
	 *            int
	 * @param indices
	 *            int[]
	 * @param nPos
	 *            int
	 */
	private void levelItems(int nDirection, int[] nIndices, int nPos) {
		if (nDirection != MOVE_UP && nDirection != MOVE_DOWN)
			return;
		if (nIndices == null || nIndices.length <= 0)
			return;
		if (nDirection == MOVE_UP) {
			if (nPos < 0)
				return;
			for (int i = 0; i < nIndices.length; i++) {
				int idx = nIndices[nIndices.length - 1 - i] + i;
				Integer FieldNo = (Integer) m_vecSortFields.get(idx);
				Boolean FieldMode = (Boolean) m_vecSortOrders.get(idx);

				m_vecSortFields.remove(idx);
				m_vecSortFields.add(nPos, FieldNo);

				m_vecSortOrders.remove(idx);
				m_vecSortOrders.add(nPos, FieldMode);
			}
			for (int i = 0; i < nIndices.length; i++) {
				nIndices[i] = nPos + i;
			}
		} else if (nDirection == MOVE_DOWN) {
			if (nPos > getlistSortFields().getModel().getSize())
				return;
			for (int i = 0; i < nIndices.length; i++) {
				int idx = nIndices[nIndices.length - 1 - i];
				Integer FieldNo = new Integer(((Integer) m_vecSortFields
						.get(idx)).intValue());
				Boolean FieldMode = new Boolean(((Boolean) m_vecSortOrders
						.get(idx)).booleanValue());

				m_vecSortFields.add(nPos, FieldNo);
				m_vecSortOrders.add(nPos, FieldMode);
			}
			for (int i = 0; i < nIndices.length; i++) {
				int idx = nIndices[nIndices.length - 1 - i];
				m_vecSortFields.remove(idx);
				m_vecSortOrders.remove(idx);
			}
			for (int i = 0; i < nIndices.length; i++) {
				nIndices[nIndices.length - 1 - i] = nPos - i - 1;
			}

		}
		refreshSortList();
		getlistSortFields().setSelectedIndices(nIndices);
	}

	/**
	 * mouseClicked 方法注解。
	 */
	public void mouseClicked(MouseEvent e) {
		Object o = e.getSource();
		if (e.getClickCount() == 2) {
			if (o == getlistSelectableFields()) {
				int index = getlistSelectableFields().locationToIndex(
						e.getPoint());
				if (index >= 0
						&& index < getlistSelectableFields().getModel()
								.getSize()) {
					actionPerformed(new ActionEvent(getbtnAdd(), 0, getbtnAdd()
							.getName()));
				}
			} else if (o == getlistSortFields()) {
				int index = getlistSortFields().locationToIndex(e.getPoint());
				if (index >= 0
						&& index < getlistSortFields().getModel().getSize()) {
					actionPerformed(new ActionEvent(getbtnRemove(), 0,
							getbtnRemove().getName()));
				}
			}
		} else if (o == getlistSortFields()) {
			int index = getlistSortFields().locationToIndex(e.getPoint());
			if (index >= 0 && index < getlistSortFields().getModel().getSize()
					&& e.getPoint().x < 16) {
				boolean bOldValue = ((Boolean) m_vecSortOrders.get(index))
						.booleanValue();
				m_vecSortOrders.set(index, new Boolean(!bOldValue));
				getlistSortFields().invalidate();
				getlistSortFields().repaint();
			}
		}
	}

	/**
	 * mouseDragged 方法注解。
	 */
	public void mouseDragged(MouseEvent e) {
		Component comp = (Component) e.getSource();
		String strName = comp.getName();
		ptListSort.setLocation(-100, -100);
		ptListSelectable.setLocation(-100, -100);
		if (comp instanceof UIList
				&& strName.equalsIgnoreCase("listSelectableFields")) {
			if (m_nDragDirection == 0
					&& !getlistSelectableFields().isSelectionEmpty()) {
				m_nDragDirection = 1;
				getlistSelectableFields().setCursor(
						Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				getlistSortFields().setCursor(
						Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				m_vecMoveItems.clear();
				int[] indexes = getlistSelectableFields().getSelectedIndices();
				m_nSelectedItems = new int[indexes.length];
				System.arraycopy(indexes, 0, m_nSelectedItems, 0,
						indexes.length);
				ptOrigin.setLocation(getlistSelectableFields().getCellBounds(
						indexes[0], indexes[0]).getLocation());
				for (int i = 0; i < indexes.length; i++) {
					Rectangle rec = getlistSelectableFields().getCellBounds(
							indexes[i], indexes[i]);
					m_vecMoveItems.add(new Rectangle(rec.x, rec.y + 1,
							rec.width, rec.height - 2));
				}
			}
			if (m_nDragDirection != 0) {
				if (m_nSelectedItems != null)
					getlistSelectableFields().setSelectedIndices(
							m_nSelectedItems);
				if (isReleasable(e.getPoint()))
					ptListSort.setLocation(transPoint(e.getPoint()));
				else
					ptListSelectable.setLocation(e.getPoint());
				getlistSelectableFields().repaint();
				getlistSortFields().repaint();
			}
		} else if (comp instanceof UIList
				&& strName.equalsIgnoreCase("listSortFields")) {
			if (m_nDragDirection == 0
					&& !getlistSortFields().isSelectionEmpty()) {
				m_nDragDirection = -1;
				getlistSelectableFields().setCursor(
						Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				getlistSortFields().setCursor(
						Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				m_vecMoveItems.clear();
				int[] indexes = getlistSortFields().getSelectedIndices();
				m_nSelectedItems = new int[indexes.length];
				System.arraycopy(indexes, 0, m_nSelectedItems, 0,
						indexes.length);
				ptOrigin.setLocation(getlistSortFields().getCellBounds(
						indexes[0], indexes[0]).getLocation());
				for (int i = 0; i < indexes.length; i++) {
					Rectangle rec = getlistSortFields().getCellBounds(
							indexes[i], indexes[i]);
					m_vecMoveItems.add(new Rectangle(rec.x, rec.y + 1, 12,
							rec.height - 2));
					m_vecMoveItems.add(new Rectangle(rec.x + 14, rec.y + 1,
							rec.width - 20, rec.height - 2));
				}
			}
			if (m_nDragDirection != 0) {
				if (m_nSelectedItems != null)
					getlistSortFields().setSelectedIndices(m_nSelectedItems);
				if (isReleasable(e.getPoint()))
					ptListSelectable.setLocation(transPoint(e.getPoint()));
				else
					ptListSort.setLocation(e.getPoint());
				getlistSelectableFields().repaint();
				getlistSortFields().repaint();
			}
		}
	}

	/**
	 * mouseEntered 方法注解。
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * mouseExited 方法注解。
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * mouseMoved 方法注解。
	 */
	public void mouseMoved(MouseEvent e) {
	}

	/**
	 * mousePressed 方法注解。
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * mouseReleased 方法注解。
	 */
	public void mouseReleased(MouseEvent e) {
		if (m_nDragDirection == 0)
			return;
		Component comp = (Component) e.getSource();
		if (comp instanceof UIList && m_nDragDirection == 1) {
			if (m_nSelectedItems != null)
				getlistSelectableFields().setSelectedIndices(m_nSelectedItems);
			if (isReleasable(e.getPoint()) && getbtnAdd().isEnabled())
				getbtnAdd().doClick();
		} else if (comp instanceof UIList && m_nDragDirection == -1) {
			if (m_nSelectedItems != null)
				getlistSortFields().setSelectedIndices(m_nSelectedItems);
			if (isReleasable(e.getPoint()) && getbtnRemove().isEnabled())
				getbtnRemove().doClick();
		}
		m_nDragDirection = 0;
		m_nSelectedItems = null;
		ptListSelectable.setLocation(-100, -100);
		ptListSort.setLocation(-100, -100);
		ptOrigin.setLocation(0, 0);
		m_vecMoveItems.clear();
		getlistSelectableFields().setCursor(
				Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		getlistSortFields().setCursor(
				Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		getlistSelectableFields().repaint();
		getlistSortFields().repaint();
	}

	/**
	 * 将选中项从左列表移至右列表 创建日期：(2000-8-30 9:05:56)
	 * 
	 * @param nDirection
	 *            int
	 * @param nItems
	 *            int[]
	 */
	private void moveItems(int nDirection, int[] nIndices) {
		if (nDirection != MOVE_RIGHT && nDirection != MOVE_LEFT)
			return;
		if (nIndices == null || nIndices.length <= 0)
			return;
		if (nDirection == MOVE_RIGHT) {
			Stack<Integer> fldStack = new Stack<Integer>();
			for (int i = 0; i < nIndices.length; i++) {
				int idx = nIndices[nIndices.length - 1 - i];
				Integer nFieldNo = (Integer) m_vecSelectableFields.get(idx);
				m_vecSelectableFields.remove(idx);
				fldStack.push(nFieldNo);
				//m_vecSortFields.addElement(nFieldNo);
				//m_vecSortOrders.addElement(new Boolean(true));
			}
			while (!fldStack.empty()) {
				Integer nFieldNo = (Integer) fldStack.pop();
				m_vecSortFields.add(nFieldNo);
				m_vecSortOrders.add(new Boolean(true));
			}
		} else if (nDirection == MOVE_LEFT) {
			Stack<Integer> fldStack = new Stack<Integer>();
			for (int i = 0; i < nIndices.length; i++) {
				int idx = nIndices[nIndices.length - 1 - i];
				Integer nFieldNo = (Integer) m_vecSortFields.get(idx);
				m_vecSortFields.remove(idx);
				m_vecSortOrders.remove(idx);
				fldStack.push(nFieldNo);
				//m_vecSelectableFields.addElement(nFieldNo);
			}
			while (!fldStack.empty()) {
				Integer nFieldNo = (Integer) fldStack.pop();
				m_vecSelectableFields.add(nFieldNo);
			}
		}
		refreshLists();
	}

	/**
	 * 刷新按钮可用状态 创建日期：(2000-8-29 16:58:32)
	 */
	private void refreshButtonMode() {
		getbtnAddAll().setEnabled(
				!(getlistSelectableFields().getModel().getSize() <= 0));
		getbtnAdd().setEnabled(!getlistSelectableFields().isSelectionEmpty());
		getbtnRemoveAll().setEnabled(
				!(getlistSortFields().getModel().getSize() <= 0));
		getbtnRemove().setEnabled(!getlistSortFields().isSelectionEmpty());

		boolean isTop = true;
		boolean isBottom = true;
		boolean isContinuous = true;
		if (!getlistSortFields().isSelectionEmpty()) {
			isTop = (getlistSortFields().getMinSelectionIndex() == 0);
			isBottom = (getlistSortFields().getMaxSelectionIndex() == (getlistSortFields()
					.getModel().getSize() - 1));
			int[] nSelectedIndices = getlistSortFields().getSelectedIndices();
			for (int i = 0; i < nSelectedIndices.length - 1; i++) {
				if (nSelectedIndices[i] != nSelectedIndices[i + 1] - 1) {
					isContinuous = false;
					break;
				}
			}
		}
		getbtnTop().setEnabled(!isTop && isContinuous);
		getbtnUp().setEnabled(!isTop && isContinuous);
		getbtnDown().setEnabled(!isBottom && isContinuous);
		getbtnBottom().setEnabled(!isBottom && isContinuous);
	}

	/**
	 * 刷新列表显示 创建日期：(2000-8-30 9:55:53)
	 */
	private void refreshLists() {
		ArrayList<Object> listSelect = new ArrayList<Object>();
		for (int i = 0; i < m_vecSelectableFields.size(); i++) {
			int idx = ((Integer) m_vecSelectableFields.get(i)).intValue();
			listSelect.add(m_objTableColumns[idx]);
		}
		getlistSelectableFields().setListData(listSelect.toArray());
		getlistSelectableFields().invalidate();
		getlistSelectableFields().repaint();

		ArrayList<Object> listSort = new ArrayList<Object>();
		for (int i = 0; i < m_vecSortFields.size(); i++) {
			int idx = ((Integer) m_vecSortFields.get(i)).intValue();
			listSort.add(m_objTableColumns[idx]);
		}
		getlistSortFields().setListData(listSort.toArray());
		getlistSortFields().invalidate();
		getlistSortFields().repaint();
		refreshButtonMode();
	}

	/**
	 * 刷新排序列表 创建日期：(2000-8-30 10:02:29)
	 */
	private void refreshSortList() {
		ArrayList<Object> listSort = new ArrayList<Object>();
		for (int i = 0; i < m_vecSortFields.size(); i++) {
			int idx = ((Integer) m_vecSortFields.get(i)).intValue();
			listSort.add(m_objTableColumns[idx]);
		}
		getlistSortFields().setListData(listSort.toArray());
		getlistSortFields().invalidate();
		getlistSortFields().repaint();
		refreshButtonMode();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-2-2 14:46:51)
	 */
	public void removeAll() {
		if (m_vecSortFields == null || m_vecSortFields.size() == 0) {
			return;
		}
		int[] indices = new int[m_vecSortFields.size()];
		for (int i = 0; i < indices.length; i++)
			indices[i] = i;
		moveItems(MOVE_LEFT, indices);
	}

	/**
	 * 设置排序项VO数组 创建日期：(2000-8-29 17:25:40)
	 * 
	 * @param newStrTableColumns
	 *            java.lang.String[]
	 */
	public void setObjTableColumns(Object[] newObjTableColumns) {
		m_objTableColumns = newObjTableColumns;
	}

	/**
	 * 设置排序字段设置结果 创建日期：(2003-10-15 15:52:09)
	 * 
	 * @return nc.vo.iuforeport.businessquery.OrderbyFldVO
	 * @i18n miufo00281=旧查询对象排序字段处理
	 * @i18n miufo00282=不可能的排序表达式：
	 */
	public void setResultToOrderby(SelectFldVO[] sfs, OrderbyFldVO[] ofs) {
		//以下用于导入旧查询对象的特殊处理
		int iLen1 = (sfs == null) ? 0 : sfs.length;
		Hashtable<String, String> hashExpAlias = new Hashtable<String, String>();
		for (int i = 0; i < iLen1; i++)
			if (sfs[i].getFldalias() != null)
				hashExpAlias.put(sfs[i].getExpression(), sfs[i].getFldalias());
		//构造排序字段的别名哈希表
		int iLen = (ofs == null) ? 0 : ofs.length;
		Hashtable<String, OrderbyFldVO> hashAliasOf = new Hashtable<String, OrderbyFldVO>();
		for (int i = 0; i < iLen; i++) {
			String ofAlias = ofs[i].getFldalias();
			if (ofAlias != null) {
				//以下用于导入旧查询对象的特殊处理
				if (!hashExpAlias.containsValue(ofAlias)) {
					AppDebug.debug("旧查询对象排序字段处理");//@devTools System.out.println("旧查询对象排序字段处理");
					if (hashExpAlias.containsKey(ofAlias))
						ofAlias = hashExpAlias.get(ofAlias).toString();
					else
						AppDebug.debug("不可能的排序表达式：" + ofAlias);//@devTools System.out.println("不可能的排序表达式：" + ofAlias);
				}
				hashAliasOf.put(ofAlias, ofs[i]);
			}
		}
		//组织参数
		iLen = (sfs == null) ? 0 : sfs.length;
		ArrayList<SelectFldVO> vecOrderField = new ArrayList<SelectFldVO>();
		ArrayList<Integer> vecSF = new ArrayList<Integer>();
		ArrayList<Boolean> vecSO = new ArrayList<Boolean>();
		for (int i = 0; i < iLen; i++) {
			vecOrderField.add(sfs[i]);
			if (hashAliasOf.containsKey(sfs[i].getFldalias())) {
				vecSF.add(new Integer(i));
				//获得排序字段VO
				OrderbyFldVO of = (OrderbyFldVO) hashAliasOf.get(sfs[i]
						.getFldalias());
				boolean bAsc = ((UFBoolean) of.getAsc()).booleanValue();
				vecSO.add(new Boolean(bAsc));
			}
		}
		//
		setSortData(vecOrderField, vecSF, vecSO);
	}

	/**
	 * 设置排序数据 创建日期：(2000-8-29 16:45:22)
	 */
	public void setSortData(ArrayList vecOrderField, ArrayList vecSF,
			ArrayList vecSO) {
		Object[] objFields = new Object[vecOrderField.size()];
		for (int i = 0; i < vecOrderField.size(); i++)
			objFields[i] = vecOrderField.get(i);
		setObjTableColumns(objFields);
		setVecSortFields(vecSF);
		setVecSortOrders(vecSO);
		if (m_vecSelectableFields == null)
			m_vecSelectableFields = new ArrayList<Integer>();
		else
			m_vecSelectableFields.clear();

		for (int i = 0; i < m_objTableColumns.length; i++)
			m_vecSelectableFields.add(new Integer(i));
		ArrayList<Object> listSort = new ArrayList<Object>();
		for (int i = 0; i < m_vecSortFields.size(); i++) {
			int idx = ((Integer) m_vecSortFields.get(i)).intValue();
			listSort.add(objFields[idx]);
			m_vecSelectableFields.remove(new Integer(idx));
		}
		getlistSortFields().setListData(listSort.toArray());

		ArrayList<Object> listSelect = new ArrayList<Object>();
		for (int i = 0; i < m_vecSelectableFields.size(); i++)
			listSelect.add(objFields[((Integer) m_vecSelectableFields.get(i))
					.intValue()]);
		getlistSelectableFields().setListData(listSelect.toArray());
		getlistSortFields().setCellRenderer(new sortListCellRenderer());
		refreshButtonMode();
	}

	/**
	 * 设置获得排序字段序号向量 创建日期：(2000-8-29 16:42:54)
	 * 
	 * @param newVecSortFields
	 *            ArrayList
	 */
	private void setVecSortFields(ArrayList newVecSortFields) {
		if (m_vecSortFields == null)
			m_vecSortFields = new ArrayList<Integer>();
		else
			m_vecSortFields.clear();
		for (int i = 0; i < newVecSortFields.size(); i++) {
			Integer FieldNo = new Integer(newVecSortFields.get(i) + "");
			m_vecSortFields.add(FieldNo);
		}
	}

	/**
	 * 设置获得排序方式向量 创建日期：(2000-8-29 16:43:40)
	 * 
	 * @param newVecSortOrders
	 *            java.util.ArrayList
	 */
	private void setVecSortOrders(ArrayList newVecSortOrders) {
		if (m_vecSortOrders == null)
			m_vecSortOrders = new ArrayList<Boolean>();
		else
			m_vecSortOrders.clear();
		for (int i = 0; i < newVecSortOrders.size(); i++) {
			Boolean fieldMode = (Boolean) newVecSortOrders.get(i);
			m_vecSortOrders.add(fieldMode);
		}
	}

	/**
	 * 计算拖动点 创建日期：(2000-9-7 16:11:47)
	 * 
	 * @return Point
	 * @param pt
	 *            Point
	 */
	private Point transPoint(Point pt) {
		Point ptCal = new Point(pt);
		if (m_nDragDirection == 1) {
			ptCal.translate(ptListSelectCorner.x - ptListSortCorner.x,
					ptListSelectCorner.y - ptListSortCorner.y);
		} else if (m_nDragDirection == -1) {
			ptCal.translate(ptListSortCorner.x - ptListSelectCorner.x,
					ptListSortCorner.y - ptListSelectCorner.y);
		} else {
			ptCal.setLocation(-100, -100);
		}
		return ptCal;
	}

	public void valueChanged(ListSelectionEvent e) {
		refreshButtonMode();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getPanelTitle()
	 */
	public String getPanelTitle() {
		return TAB_TITLE;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getResultFromPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {
		//回设
		qmd.getQueryBaseVO().setOrderbyFlds(getResultFromOrderby());
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		QueryBaseVO qb = qmd.getQueryBaseVO();
		setResultToOrderby(qb.getSelectFlds(), qb.getOrderbyFlds());
	}

	/**
	 * 合法性检查 创建日期：(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	public String check() {
		return null;
	}

	/**
	 * 刷新查询基本定义
	 */
	public void refreshQbd() {
		//获得排序字段定义
		OrderbyFldVO[] ofs = getResultFromOrderby();
		//刷新
		getTabPn().getQueryBaseDef().setOrderbyFlds(ofs);
	}
}  