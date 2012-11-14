package nc.ui.pub.bill;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import nc.bs.logging.Logger;
import nc.md.data.access.NCObject;
import nc.md.model.IAttribute;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable.SortItem;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.beans.constenum.IConstEnum;
import nc.ui.pub.beans.table.CellFont;
import nc.ui.pub.beans.table.ColoredCell;
import nc.ui.pub.beans.table.IMutilSortableTableModel;
import nc.ui.pub.beans.table.ISpanCell;
import nc.ui.pub.beans.util.MiscUtils;
import nc.ui.pub.bill.table.BillModelCellSpan;
import nc.ui.pub.bill.table.IBillModeCellSpan;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.vo.bill.pub.BillUtil;
import nc.vo.jcom.util.SortUtils;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.general.GeneralSuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

@SuppressWarnings("serial")
public class BillModel extends DefaultTableModel implements
		IMutilSortableTableModel, ColoredCell, CellFont, ISpanCell {
	public final static int UNSTATE = -1; // ��״̬

	public final static int NORMAL = 0; // ����

	public final static int ADD = 1; // ����

	public final static int MODIFICATION = 2; // �޸�

	public final static int DELETE = 3; // ɾ��

	public final static int SELECTED = 4; // ѡ��

	protected BillItem[] m_biBodyItems = null; // ����Ԫ������

	protected Hashtable<String, Integer> htBodyItems = null; // ��¼����Ԫ��λ��

	private Hashtable<String, BillItem> htBillItems = new Hashtable<String, BillItem>();

	private Vector vViewBodyCache = null; // ���ݱ���

	private Vector<Vector> vViewRowCopy = null; // �����ݱ���

	private Vector<Vector> vDeleteRow = null; // ɾ���м�¼

	protected DefaultTableModel m_tmlTotal = null; // �ϼ��б�ģʽ

	protected DefaultTableModel m_tmlRowNO = null; // �кű�ģʽ

	private FixRowNOModel fixRowHeaderModel = null;// fix row header

	private UserRowNOModel userRowHeaderModel = null;

	private UserFixRowTableModel m_userFixRowModel = null; // user fixrow model

	// �����Զ���RowAttribute
	protected Vector<RowAttribute> vRowAttrib = new Vector<RowAttribute>();

	// protected Hashtable htCellEditCopy = null; // ����

	protected int fixCol = -1; // ������

	protected BillTotalListener btl = null; // �ϼƼ���

	// ---------------------------------------------

	// �ж�setValueAtModel(..)�ĵ����Ƿ�����setBodyDataVO�����õ�ֵ,����ظ���ֵ
	private boolean isFirstSetBodyDataVO = false;

	// ---------------------------------------

	// ��ʽִ����
	protected FormulaParse m_formulaParse;

	// �б༭
	protected boolean m_bRowEditState = false;

	// �ɱ༭��
	// protected int m_intEditRow = -1;
	protected Vector m_editRow = null;

	// ���ɱ༭��
	protected ArrayList<RowAttribute> aryNotEditRow = null;

	/**
	 * ����ķ���
	 */
	// private boolean m_ascending = true;
	/**
	 * �����еĿɱ༭�Ե�Hash��
	 */
	// protected Hashtable m_htColEditable = null;
	/**
	 * �����пɱ༭�Ե�Hash��
	 */
	// protected Hashtable m_htRowEditable = null;
	/**
	 * �����������е�����
	 */
	private int[] m_indexes;

	/**
	 * �����������е����
	 */
	// private int m_sortColumn = -1;
	/**
	 * ������
	 */
	private List<SortItem> m_sortColumn = null;

	// ���������������
	protected EventListenerList sortRelaObjectListener = new EventListenerList();

	private IBillModelSortPrepareListener sortPrepareListener = null;

	public IBillModelRowStateChangeEventListener rowStateChangeListener = null;

	protected BillSortListener bsl = null; // �������

	protected EventListenerList afterSortListener = new EventListenerList();

	private final int DefaultSortType = -1;

	// private int sorttype = DefaultSortType; // value of billitem's types

	// ���Ӷ��Ƿ�ɱ༭��ȫ�ֿ���
	private boolean m_bEnabled = true;

	// �����Ƿ����ϼƵı�־
	private boolean m_bNeedCalculate = false;

	// private Hashtable<Cell,Color> hashCellBackColor = new
	// Hashtable<Cell,Color>();
	//
	// private Hashtable<Cell,Color> hashCellForeColor = new
	// Hashtable<Cell,Color>();

	private BillModelFormulaProcessor formulaProcessor = new BillModelFormulaProcessor(this);

	// for formula
	private BillScrollPane billScrollPane = null;

	// private BillItemContext context = null;

	private boolean changeTable = true;

	private BillModelCellEditableController cellEditableController = null;

	private String totalTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("common", "UC000-0001146");

	// private boolean showUserFixRowTable = false;

	// public static boolean setVectorSize = false;
	//	
	// public static boolean fireTable = false;

	private boolean ignoreScaleWhenSetValue = false;

	// ��ѡ��ģʽ
	private boolean rowSelectMode = false;

	//�ϲ���Ԫ������
	private IBillModeCellSpan cellSpan = null;

	/**
	 * �к�Model.
	 */
	public class RowNumberModel extends DefaultTableModel {
		public int getColumnCount() {
			if (isRowSelectMode())
				return 2;
			else
				return 1;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {

			Class c = null;

			switch (columnIndex) {
			case 0:
				c = String.class;
				break;
			case 1:
				c = Boolean.class;
				break;
			default:
				c = super.getColumnClass(columnIndex);
				break;
			}

			return c;
		}

		public String getColumnName(int col) {
			return "   ";// "�к�";
		}

		public boolean isCellEditable(int row, int col) {
			return false;
		}

		public void setNumRows(int rowCount) {
			int old = getRowCount();
			if (old == rowCount) {
				return;
			}
			dataVector.setSize(rowCount);
			if (rowCount <= old) {
			} else {
				justifyRows(old, rowCount);
			}
			fireTableChanged(new TableModelEvent(this));
		}

		@SuppressWarnings("unchecked")
		private void justifyRows(int from, int to) {
			dataVector.setSize(getRowCount());

			for (int i = from; i < to; i++) {
				if (dataVector.elementAt(i) == null) {
					dataVector.setElementAt(new Vector(), i);
				}
				((Vector) dataVector.elementAt(i)).setSize(getColumnCount());
			}
		}

		public Object getValueAt(int row, int column) {
			if (row >= 0 && column >= 0) {
				switch (column) {
				case 0:
					String value = row + 1 + "";
					return value;
				case 1:
					if (getRowState(row) == SELECTED) {
						return Boolean.TRUE;
					} else {
						return Boolean.FALSE;
					}
				default:
				}

			}
			return null;
		}
	}

	/**
	 * �к�Model.
	 */
	public class FixRowNOModel extends DefaultTableModel {
		public int getColumnCount() {
			if (isRowSelectMode())
				return 2;
			else
				return 1;
		}

		public int getRowCount() {
			int row = 0;
			if (BillModel.this == null)
				return row;
			if (m_tmlTotal != null)
				row += m_tmlTotal.getRowCount();
			return row;
		}

		public Object getValueAt(int row, int column) {
			if (column == 0)
				return totalTitle;
			else
				return null;
		}
	}

	/**
	 * �к�Model.
	 */
	public class UserRowNOModel extends DefaultTableModel {
		public int getColumnCount() {
			if (isRowSelectMode())
				return 2;
			else
				return 1;
		}

		public int getRowCount() {
			int row = 0;
			if (BillModel.this == null)
				return row;
			if (m_userFixRowModel != null)
				row += m_userFixRowModel.getRowCount();
			return row;
		}

		public Object getValueAt(int row, int column) {
			if (column == 0)
				return "" + row;
			else
				return null;
		}
	}

	/**
	 * �ϼ�Model.
	 */
	public class TotalTableModel extends DefaultTableModel {
		
		private BillModel model = null;
		
		public TotalTableModel(BillModel model) {
			super();
			this.model = model;
		}

		public int getColumnCount() {
			return BillModel.this.getColumnCount();
		}

		public String getColumnName(int col) {
			return BillModel.this.getColumnName(col);
		}

		public boolean isCellEditable(int row, int col) {
			return false;
		}

		public BillModel getMainModel() {
			return model;
		}
	}

	class MutiColumnComparator implements Comparator {

		Comparator[] itemComparator = null;

		List<SortItem> sortitems = null;

		public MutiColumnComparator(List<SortItem> sortitems) {
			super();
			this.sortitems = sortitems;
			initComparator();
		}

		private void initComparator() {

			if (sortitems != null) {
				itemComparator = new ColumnComparator[sortitems.size()];
				for (int i = 0; i < sortitems.size(); i++) {
					itemComparator[i] = getItemComparator(sortitems.get(i));
				}
			}
		}

		@SuppressWarnings("unchecked")
		public int compare(Object o1, Object o2) {
			int result = -1;

			for (int i = 0; i < itemComparator.length; i++) {
				Comparator comparator = itemComparator[i];
				result = comparator.compare(o1, o2);

				if (result != 0)
					break;
			}

			return result;
		}

	}

	class ColumnComparator implements Comparator {

		private int sorttype = DefaultSortType;

		private Comparator<Object> comp = null;

		private SortItem sortitem = null;

		// int comparetype = 0;
		public ColumnComparator(int sorttype, SortItem sortitem) {
			this.sorttype = sorttype;
			this.sortitem = sortitem;
		}

		public ColumnComparator(Comparator<Object> comp, SortItem sortitem) {
			this.comp = comp;
			this.sortitem = sortitem;
		}

		public int compare(Object o1, Object o2) {

			int result = -1;

			Object co1 = ((Vector) o1).get(sortitem.getColumn());
			Object co2 = ((Vector) o2).get(sortitem.getColumn());

			if (comp == null) {

				result = compareObject(co1, co2);

			} else {
				result = comp.compare(co1, co2);
			}

			return sortitem.isAscending() ? result : -result;
		}

		@SuppressWarnings("unchecked")
		private int compareObject(Object o1, Object o2) {
			int type = this.sorttype;
			// �����Ϊnull,�򷵻�0
			if (o1 == o2) {
				return 0;
			} else if (o1 == null) {
				// Ĭ��null������ֵС
				return -1;
			} else if (o2 == null) {
				return 1;
			}

			int ret = 0;

			// ָ�����ͱȽ�
			switch (type) {
			case DefaultSortType:
				if (o1 instanceof IConstEnum)
					o1 = o1.toString();
				if (o2 instanceof IConstEnum)
					o2 = o2.toString();

				// �����Ϊnull,�򷵻�0
				if (o1 == o2) {
					return 0;
				} else if (o1 == null) {
					// Ĭ��null������ֵС
					return -1;
				} else if (o2 == null) {
					return 1;
				}

				if (o1 instanceof Comparable && o2 instanceof Comparable) {
					ret = ((Comparable) o1).compareTo((Comparable) o2);
				}
				break;
			case BillItem.DECIMAL:
			case BillItem.INTEGER:
				UFDouble uo1 = new UFDouble(o1.toString());
				UFDouble uo2 = new UFDouble(o2.toString());

				ret = uo1.compareTo(uo2);
				break;
			default:

				if (o1 instanceof IConstEnum)
					o1 = o1.toString();
				if (o2 instanceof IConstEnum)
					o2 = o2.toString();

				// �����Ϊnull,�򷵻�0
				if (o1 == o2) {
					return 0;
				} else if (o1 == null) {
					// Ĭ��null������ֵС
					return -1;
				} else if (o2 == null) {
					return 1;
				}

				ret = MiscUtils.compareStringByBytes(o1, o2);
				break;
			}
			return ret;

		}

	}

	/**
	 * BillModel ������ע��.
	 */
	public BillModel() {
		super();
	}

	/**
	 * BillModel ������ע��.
	 * 
	 * @param data
	 *            java.lang.Object[][]
	 * @param columnNames
	 *            java.lang.Object[]
	 */
	public BillModel(java.lang.Object[][] data, java.lang.Object[] columnNames) {
		super(data, columnNames);
	}

	/**
	 * BillModel ������ע��.
	 * 
	 * @param columnNames
	 *            java.lang.Object[]
	 * @param numRows
	 *            int
	 */
	public BillModel(java.lang.Object[] columnNames, int numRows) {
		super(columnNames, numRows);
	}

	/**
	 * BillModel ������ע��.
	 * 
	 * @param numRows
	 *            int
	 * @param numColumns
	 *            int
	 */
	public BillModel(int numRows, int numColumns) {
		super(numRows, numColumns);
	}

	/**
	 * BillModel ������ע��.
	 * 
	 * @param columnNames
	 *            java.util.Vector
	 * @param numRows
	 *            int
	 */
	public BillModel(java.util.Vector columnNames, int numRows) {
		super(columnNames, numRows);
	}

	/**
	 * BillModel ������ע��.
	 * 
	 * @param data
	 *            java.util.Vector
	 * @param columnNames
	 *            java.util.Vector
	 */
	public BillModel(java.util.Vector data, java.util.Vector columnNames) {
		super(data, null);
	}

	/**
	 * ������.
	 */
	public void addLine() {
		addLine(1);
	}

	/**
	 * ������.
	 */
	void addLine(int count) {
		if (m_biBodyItems == null || count <= 0)
			return;
		for (int j = 0; j < count; j++) {
			// ����������
			int lastRow = getRowCount() - 1;
			Vector<Object> vNewRow = createRowVector(lastRow);
			vRowAttrib.addElement(createRowAttribute());

			addRow(vNewRow);
			// �����к�
			vNewRow = new Vector<Object>();
			vNewRow.add(null);
			getRowNOTableModel().addRow(vNewRow);

			if(getCellSpan() != null) {
				getCellSpan().addLine();
			}
		}
		if (isChangeTable())
			fireTableChanged(new TableModelEvent(this));
	}

	/**
	 * ����������. ��������:(2001-10-24 16:30:33)
	 * 
	 * @param row
	 *            ������λ��
	 */
	private Vector<Object> createRowVector(int row) {
		Vector<Object> vNewRow;
		vNewRow = new Vector<Object>(m_biBodyItems.length);
		if (isChangeTable()) {
			for (int i = 0; i < m_biBodyItems.length; i++) {
				Object value = null;
				if (row > -1 && m_biBodyItems[i].isLock()) {
					value = getValueAt(row, i);
				} else if (m_biBodyItems[i].getDefaultValueObject() != null) {
					value = m_biBodyItems[i].getDefaultValueObject();
					// if(value != null){
					// switch (m_biBodyItems[i].getDataType()) {
					// case BillItem.BOOLEAN:
					// if (value.toString().equalsIgnoreCase("true")
					// || value.toString().equalsIgnoreCase("Y") ||
					// value.toString().equals("1"))
					// value = UFBoolean.TRUE;
					// else
					// value = UFBoolean.FALSE;
					//                            
					// break;
					// case BillItem.INTEGER:
					// value = new Integer(value.toString());
					// break;
					// case BillItem.DECIMAL:
					// value = new UFDouble(value.toString());
					// value = ((UFDouble) value).setScale(0 -
					// m_biBodyItems[i].getDecimalDigits(),
					// UFDouble.ROUND_HALF_UP);
					//
					// break;
					// case IBillItem.COMBO:
					// if(value.toString().length() == 0 )
					// value = null;
					// else if(m_biBodyItems[i].isComboBoxReturnInteger() &&
					// !(value instanceof Integer))
					// value = new Integer(value.toString());
					//
					// UIComboBox combo = ((UIComboBox)
					// m_biBodyItems[i].getComponent());
					//        	                
					// int index = combo.getItemIndexByObject(value);
					// if (index < 0 && m_biBodyItems[i].isWithIndex()) {
					// if ("".equals(value))
					// index = 0;
					// else
					// index = Integer.parseInt(value.toString());
					// }
					//        	                
					// if (index >= 0)
					// value = combo.getItemAt(index);
					// break;
					// default:
					// break;
					// }
					// }
					// System.out.println(m_biBodyItems[i].getDefaultValueObject().getClass().getName()
					// + "=====" + m_biBodyItems[i].getDefaultValueObject());
				}
				vNewRow.add(value);
			}
		}

		return vNewRow;
	}

	private RowAttribute createRowAttribute() {
		RowAttribute ra = new RowAttribute();

		return ra;
	}

	/**
	 * �����������. ��������:(2001-10-24 16:30:33)
	 * 
	 * @param btl
	 *            nc.ui.pub.bill.BillTotalListener
	 */
	public void addSortListener(BillSortListener bsl) {
		this.bsl = bsl;
	}

	/**
	 * ���ӺϼƼ���. ��������:(2001-10-24 16:30:33)
	 * 
	 * @param btl
	 *            nc.ui.pub.bill.BillTotalListener
	 */
	public void addTotalListener(BillTotalListener btl) {
		this.btl = btl;
	}

	// ------------------------------------------------------------------------
	/**
	 * ���Ӿ��ȼ���
	 */
	public void addDecimalListener(IBillModelDecimalListener bdl) {

		if (bdl instanceof IBillModelDecimalListener2) {
			String[] targets = ((IBillModelDecimalListener2) bdl).getTarget();
			if (targets != null && targets.length > 0) {
				for (int i = 0; i < targets.length; i++) {
					getItemByKey(targets[i]).addDecimalListener(bdl);
				}
			}
		} else {// Ϊ�˱���ԭ���ӿ�(IBillModelDecimalListener)����
			BillItem[] items = getBodyItems();
			for (int i = 0; i < items.length; i++) {
				if (bdl.isTarget(items[i])) {
					items[i].addDecimalListener(bdl);
				}
			}
		}
	}

	/**
	 * ���ö�Ӧ�е�Item�ľ���
	 */
	private void setBillItemDecimalByVO(BillItem item, int row, CircularlyAccessibleValueObject bodyRowVO) {
		if (item.getDecimalListener() != null) {
			String source = item.getDecimalListener().getSource();
			Object pkValue = bodyRowVO.getAttributeValue(source);
			if (pkValue != null)
				item.setDecimalDigits(item.getDecimalListener().getDecimalFromSource(row, pkValue));
		}
	}

	private void setBillItemDecimalByRow(BillItem item, int row) {
		if (item.getDecimalListener() != null) {
			int colIndex = getBodyColByKey(item.getDecimalListener().getSource());
			Object id = getValueAt(row, colIndex);
			if (id != null && id instanceof IConstEnum)
				id = ((IConstEnum) id).getValue();
			if (id != null)
				item.setDecimalDigits(item.getDecimalListener().getDecimalFromSource(row, id));
		}
	}

	// ----------------------------------------------------------------------

	// /**
	// * ������Ԫ��༭����. ��������:(2001-3-25 16:52:47)
	// *
	// * @param operation java.lang.String
	// * @param row int
	// */
	// protected void adjustCellEdit(int operation, int row) {
	// adjustCellEditModel(operation, convertIntoModelRow(row));
	// }

	/**
	 * ������Ԫ��༭����. ��������:(2001-3-25 16:52:47)
	 * 
	 * @param operation
	 *            java.lang.String
	 * @param row
	 *            int
	 */
	// private void adjustCellEditModel(int operation, int row) {
	// if (htCellEdit == null)
	// return;
	// Hashtable temp = new Hashtable();
	// // ����
	// if (operation == 0) {
	// for (Enumeration e = htCellEdit.keys(); e.hasMoreElements();) {
	// String key = e.nextElement().toString();
	// int oldRow = Integer.parseInt(key
	// .substring(0, key.indexOf(",")));
	// if (oldRow >= row) {
	// String newKey = (oldRow + 1)
	// + key.substring(key.indexOf(","));
	// temp.put(newKey, htCellEdit.get(key));
	// } else
	// temp.put(key, htCellEdit.get(key));
	// }
	// }
	// // ɾ��
	// if (operation == 1) {
	// for (Enumeration e = htCellEdit.keys(); e.hasMoreElements();) {
	// String key = e.nextElement().toString();
	// int oldRow = Integer.parseInt(key
	// .substring(0, key.indexOf(",")));
	// if (oldRow != row) {
	// if (oldRow > row) {
	// String newKey = (oldRow - 1)
	// + key.substring(key.indexOf(","));
	// temp.put(newKey, htCellEdit.get(key));
	// htCellEdit.remove(key);
	// } else
	// temp.put(key, htCellEdit.get(key));
	// }
	// }
	// }
	// htCellEdit = temp;
	// }
	/**
	 * ��ʼ��indexes����,Ϊ��������׼��
	 * 
	 * @throws
	 * @see
	 * @since v1.0
	 * @deprecated
	 */
	protected void allocate() {
		m_indexes = new int[getRowCount()];
		for (int i = 0; i < m_indexes.length; i++)
			m_indexes[i] = i;
	}

	/**
	 * �����ʾ����.
	 */
	public void clearBodyData() {
		dataVector = new Vector();
		// ����к�
		getRowNOTableModel().setNumRows(0);
		// ���״̬
		// vRowState = new Vector();
		vRowAttrib = new Vector<RowAttribute>();
		// ����༭
		clearCellEdit();
		// �������
		m_indexes = null;
		// ����ϼ�
		clearTotalModel();
		// ������ɱ༭��
		setNotEditAllowedRows(null);
		// ���ɾ����
		if (vDeleteRow != null)
			vDeleteRow.removeAllElements();
		if (vViewRowCopy != null)
			vViewRowCopy.removeAllElements();

		// repaint
		fireTableChanged(new TableModelEvent(this));
	}

	/**
	 * ��������:(2003-6-20 11:11:56)
	 * 
	 * @param row
	 *            int
	 * @param col
	 *            int
	 * @param color
	 *            java.awt.Color
	 */
	void clearCellColor(String type) {
		for (int i = 0; i < getRowCount(); i++) {

			RowAttribute rowatt = getRowAttribute(i);

			if ("all".equals(type)) {
				rowatt.clearCellBackColor();
				rowatt.clearCellForeColor();
			} else if ("back".equals(type)) {
				rowatt.clearCellBackColor();
			} else
				rowatt.clearCellForeColor();
		}
	}

	/**
	 * ������ñ༭.
	 */
	public void clearCellEdit() {
		// htCellEdit = null;
		for (int i = 0; i < dataVector.size(); i++) {
			getRowAttribute(i).clearCellEdit();
		}
	}

	/**
	 * ���ָ����ָ��������. ��������:(2001-9-14 10:18:13)
	 * 
	 * @param row
	 *            int :: view row index
	 * @param keys
	 *            java.lang.String[]
	 */
	public void clearRowData(int row, String[] keys) {
		if (row < 0 || row >= getRowCount())
			return;
		if (keys == null) {
			for (int i = 0; i < getColumnCount(); i++) {
				setValueAt("", row, i);
			}
		} else {
			for (int i = 0; i < keys.length; i++) {
				setValueAt("", row, keys[i]);
			}
		}
	}

	/**
	 * ����ϼƱ�����. ��������:(2001-11-2 16:13:22)
	 */
	public void clearTotalModel() {
		if (m_tmlTotal != null) {
			for (int i = 0; i < m_tmlTotal.getColumnCount(); i++) {
				// System.out.println("a:" + i);
				m_tmlTotal.setValueAt(null, 0, i);
			}
		}
	}

	/**
	 * ��ñ��༭��. ��������:(2001-9-7 10:57:25)
	 * 
	 * @param newRowEdit
	 *            boolean
	 */
	// int[] convertIntoModelRow(int[] rows) {
	// if (rows != null) {
	// int[] mrows = new int[rows.length];
	// for (int i = 0; i < rows.length; i++)
	// mrows[i] = convertIntoModelRow(rows[i]);
	// rows = mrows;
	// }
	// return rows;
	// }
	/**
	 * �������ǰ���༭��. �㷨����,��ֱ��ʹ��row,����ת�� ��������:(2001-9-7 10:57:25)
	 * 
	 * @param newRowEdit
	 *            boolean
	 * @deprecated
	 */
	public int convertIntoModelRow(int row) {
		if (row < 0 || row >= getRowCount())
			return -1;
		// return (getSortIndex() != null && getSortIndex().length > row) ?
		// getSortIndex()[row]
		// : row;
		return row;
	}

	/**
	 * ��ñ��༭��. �㷨����,��ֱ��ʹ��row,����ת�� ��������:(2001-9-7 10:57:25)
	 * 
	 * @param newRowEdit
	 *            boolean
	 * @deprecated
	 */
	public int convertIntoViewRow(int row) {
		if (row < 0 || row >= getRowCount())
			return -1;
		// if (getSortIndex() != null && getSortIndex().length > row) {
		// for (int i = 0; i < getSortIndex().length; i++) {
		// if (getSortIndex()[i] == row)
		// return i;
		// }
		// }
		return row;
	}

	/**
	 * ���ƶ���.
	 */
	public void copyLine(int[] row) {
		if (vViewRowCopy == null)
			vViewRowCopy = new Vector<Vector>();
		vViewRowCopy.removeAllElements();
		for (int i = 0; i < row.length; i++) {
			if (row[i] > -1 && row[i] < dataVector.size()) {
				Vector vRow = (Vector) dataVector.elementAt(row[i]);
				vViewRowCopy.add(vRow);
			}
		}
	}

	/**
	 * ��ʽִ����. ��������:(01-2-21 10:08:48)
	 */
	protected FormulaParse createDefaultFormulaParse() {
		return new FormulaParse();
	}

	/**
	 * �кű�ģʽ. ��������:(01-2-21 10:08:48)
	 */
	protected DefaultTableModel createDefaultRowNumberModel() {
		return new RowNumberModel();
	}

	/**
	 * �ϼ��б�ģʽ. ��������:(01-2-21 10:08:48)
	 */
	protected DefaultTableModel createDefaultTotalTableModel() {
		return new TotalTableModel(this);
	}

	private UFDouble createUFDouble(Object o) {
		if (o instanceof UFDouble)
			return (UFDouble) o;
		String v;
		if (o == null || (v = o.toString().trim()).length() == 0)
			v = "0";
		return new UFDouble(v);
	}

	/**
	 * ɾ������.
	 */
	public void delLine(int[] array) {
		if (vDeleteRow == null)
			vDeleteRow = new Vector<Vector>();

		// �ȶ�Ҫɾ�����кŽ�������,��ȷ���Ӻ���ǰɾ����
		int[] row = (int[]) array.clone();

		java.util.Arrays.sort(row);
		
		for (int i = row.length - 1; i >= 0; i--) {
			if (getRowStateModel(row[i]) == NORMAL || getRowStateModel(row[i]) == MODIFICATION)
				vDeleteRow.add((Vector) dataVector.elementAt(row[i]));

			if(getCellSpan() != null) {
				getCellSpan().delLine(row[i]);
			}

			removeRow(row[i]);

			// �к�
			getRowNOTableModel().removeRow(row[i]);

			vRowAttrib.remove(row[i]);
		}


		// ����ϼ�
		reCalcurateAll();
	}

	/**
	 * ִ�б༭��ʽ ��������:(01-4-26 15:29:06) please call method
	 * execEditFormulaByKey(int, String) instead column : view column
	 * 
	 * @deprecated
	 */
	public void execEditFormula(int row, int column) {
		column = getBodyColByCol(column);
		execEditFormulaByModelColumn(row, column);
	}

	/**
	 * ִ�б༭��ʽ ��������:(01-4-26 15:29:06)
	 */
	public void execEditFormulaByKey(int row, String key) {
		execEditFormulaByModelColumn(row, getBodyColByKey(key));
	}

	/**
	 * ִ�б༭��ʽ ��������:(01-4-26 15:29:06)
	 */
	void execEditFormulaByModelColumn(int row, int column) {
		formulaProcessor.execEditFormulaByModelColumn(row, column);
	}

	/**
	 * ִ�б༭��ʽ ��������:(01-4-26 15:29:06) please call method
	 * execEditFormulasByKey(int, String)
	 * 
	 * instead column : view column
	 * 
	 * @deprecated
	 */
	public void execEditFormulas(int row, int column) {
		// �����
		column = getBodyColByCol(column);
		execEditFormulasByModelColumn(row, column);
	}

	/**
	 * ִ�б༭��ʽ ��������:(01-4-26 15:29:06)
	 */
	public void execEditFormulasByKey(int row, String key) {
		execEditFormulasByModelColumn(row, getBodyColByKey(key));
	}

	/**
	 * ִ�б༭��ʽ ��������:(01-4-26 15:29:06)
	 */
	void execEditFormulasByModelColumn(int row, int column) {
		formulaProcessor.execEditFormulasByModelColumn(row, column);
	}

	/**
	 * ���ñ༭������ֵ ��������:(01-4-26 15:29:06)
	 */
	protected void loadEditRelationItemValue(int row, int column) {
		loadRelationItemValue(row, column, true);
	}

	/**
	 * ���ü��Թ�����ֵ ��������:(01-4-26 15:29:06)
	 */
	public void loadLoadRelationItemValue(int row, String itemkey) {
		loadRelationItemValue(row, getBodyColByKey(itemkey), false);
	}

	/**
	 * ���ñ༭������ֵ ��������:(01-4-26 15:29:06)
	 */
	private void loadRelationItemValue(int row, int column, boolean isedit) {
		BillItem item = getBodyItems()[column];

		if (item.getDataType() == IBillItem.UFREF && item.getMetaDataProperty() != null) {

			ArrayList<IConstEnum> relationitem = getMetaDataRelationItems(item, isedit);

			if (relationitem != null) {
				if (getValueAt(row, column) != null) {
					String id = (String) ((IConstEnum) getValueAt(row, column)).getValue();
					// IConstEnum Value Object[]:,Key:itemkey
					IConstEnum[] o = item.getGetBillRelationItemValue().getRelationItemValue(relationitem, new String[] { id });
					if (o != null) {
						for (int i = 0; i < o.length; i++) {
							if (o[i].getValue() != null) {
								Object[] v = (Object[]) o[i].getValue();
								setValueAt(v[0], row, o[i].getName());

							}
						}
					}
				} else {
					for (int i = 0; i < relationitem.size(); i++) {
						setValueAt(null, row, relationitem.get(i).getName());
					}
				}
			}
		}

	}

	/**
	 * ���ñ༭������ֵ ��������:(01-4-26 15:29:06)
	 */
	public void loadEditRelationItemValue(int row, String itemkey) {
		loadEditRelationItemValue(row, getBodyColByKey(itemkey));

	}

	public void loadLoadRelationItemValue() {
		if (getRowCount() < 0)
			return;

		for (int col = 0; col < getBodyItems().length; col++) {
			BillItem item = getBodyItems()[col];

			if (item.getDataType() == IBillItem.UFREF && item.getMetaDataProperty() != null) {

				ArrayList<IConstEnum> relationitem = getMetaDataRelationItems(item, false);

				if (relationitem != null) {

					String[] ids = new String[getRowCount()];

					for (int row = 0; row < getRowCount(); row++) {
						Object o = getValueAt(row, col);
						if (o != null && o instanceof IConstEnum)
							ids[row] = (String) ((IConstEnum) o).getValue();
					}

					IConstEnum[] o = item.getGetBillRelationItemValue().getRelationItemValue(relationitem, ids);

					for (int row = 0; row < getRowCount(); row++) {
						if (o != null) {
							for (int i = 0; i < o.length; i++) {
								if (o[i].getValue() != null) {
									Object[] v = (Object[]) o[i].getValue();
									setValueAt(v[row], row, o[i].getName());
								}
							}
						}
					}
				}
			}
		}
	}

	private void loadLoadRelationItemValue(int row) {
		if (getRowCount() < 0)
			return;

		for (int col = 0; col < getBodyItems().length; col++) {
			BillItem item = getBodyItems()[col];

			if (item.getDataType() == IBillItem.UFREF && item.getMetaDataProperty() != null) {

				ArrayList<IConstEnum> relationitem = getMetaDataRelationItems(item, false);

				if (relationitem != null) {

					String id = null;
					Object obj = getValueAt(row, col);
					if (obj != null && obj instanceof IConstEnum)
						id = (String) ((IConstEnum) obj).getValue();

					IConstEnum[] o = item.getGetBillRelationItemValue().getRelationItemValue(relationitem, new String[] { id });

					if (o != null) {
						for (int i = 0; i < o.length; i++) {
							if (o[i].getValue() != null) {
								Object[] v = (Object[]) o[i].getValue();
								setValueAt(v[0], row, o[i].getName());
							}
						}
					}
				}
			}
		}
	}

	/*
	 * IConstEnum Value metadatapath:,Name:itemkey
	 */
	protected ArrayList<IConstEnum> getMetaDataRelationItems(BillItem item, boolean isediting) {

		if (item.getDataType() != IBillItem.UFREF)
			return null;

		if (item.getMetaDataProperty() == null)
			return null;

		ArrayList<IConstEnum> ics = new ArrayList<IConstEnum>();

		// ���ITEM��ʾ����
		// ���ع������ʾ���Ƽ���
		if (!isediting) {
			String showattname = getRefItemShowAttributeName(item);

			if (showattname != null) {
				IConstEnum ic = new DefaultConstEnum(showattname, item.getKey());
				ics.add(ic);
			}
		}

		// ���ITEM�༭��������Ĭ��ֵ����
		// itemkey,����·��:AAAA=B.A,BBBB=B.B
		// �༭������
		if (item.getMetaDataRelation() != null && isediting) {
			IConstEnum[] ies = BillUtil.getConstEnumByString(item.getMetaDataRelation());

			for (int i = 0; i < ies.length; i++) {

				BillItem ritem = getItemByKey(ies[i].getName());

				if (!addRefRelationItem(ics, ritem, (String) ies[i].getValue())) {
					ics.add(ies[i]);
				}
			}
		}

		// ���ITEM��������
		if (item.getRelationItem() != null) {
			for (int i = 0; i < item.getRelationItem().size(); i++) {
				BillItem ritem = item.getRelationItem().get(i);

				if (!addRefRelationItem(ics, ritem, ritem.getMetaDataAccessPath())) {
					IConstEnum ic = new DefaultConstEnum(ritem.getMetaDataAccessPath(), ritem.getKey());
					ics.add(ic);
				}

			}
		}

		if (ics.size() == 0)
			ics = null;

		return ics;

	}

	private boolean addRefRelationItem(ArrayList<IConstEnum> ics, BillItem ritem, String path) {

		if (ritem.getDataType() == IBillItem.UFREF) {
			IConstEnum ic = null;
			ic = new DefaultConstEnum(path, ritem.getKey() + IBillItem.ID_SUFFIX);
			ics.add(ic);
			// UFREF����
			String showattname = getRefItemShowAttributeName(ritem);
			if (showattname != null) {
				ic = new DefaultConstEnum(path + "." + showattname, ritem.getKey());
				ics.add(ic);
				return true;
			}
		}

		return false;
	}

	private String getRefItemShowAttributeName(BillItem item) {
		String showattname = null;

		if (item.getDataType() == IBillItem.UFREF && item.isShareShow()) {
			// if(item.getDataType() == IBillItem.UFREF){
			if (item.isRefReturnCode())
				showattname = item.getMetaDataProperty().getBDCodeAttributeName();
			else
				showattname = item.getMetaDataProperty().getBDNameAttributeName();
		}
		return showattname;
	}

	/**
	 * ִ�й�ʽ(�ӽ�����ȡֵ) ��������:(01-4-26 15:29:06)
	 */
	public void execFormula(int row, String[] formulas) {
		formulaProcessor.execFormula(row, formulas);
	}

	/**
	 * ����ִ�й�ʽ ��������:(01-4-26 15:29:06)
	 */
	public void execFormulas(String[] formulas) {
		execFormulas(formulas, -1, -1);
	}

	/**
	 * ����ִ�й�ʽ ��������:(01-4-26 15:29:06)
	 */
	public void execFormulas(String[] formulas, int beginRow, int endRow) {
		formulaProcessor.execFormulas(formulas, beginRow, endRow, false);
	}

	public void execEditFormulas(int row) {
		formulaProcessor.execEditFormulas(row, row, false);
	}

	/**
	 * ����ִ�й�ʽ ��������:(01-4-26 15:29:06)
	 */
	public void execFormulas(int row, String[] formulas) {
		formulaProcessor.execFormulas(row, formulas);
	}

	/**
	 * ִ�й�ʽ,�����ֱ�Ӹ���VO,����ʾ������! ��������:(01-4-26 15:29:06)
	 */
	public void execFormulasWithVO(CircularlyAccessibleValueObject[] VOs, String[] formulas) {
		formulaProcessor.execFormulasWithVO(VOs, formulas);
	}

	/**
	 * ִ�м��ع�ʽ ��������:(01-4-26 15:29:06)
	 */
	public void execLoadFormula() {
		// long t = System.currentTimeMillis();
		if (getRowCount() > 0)
			formulaProcessor.execLoadFormula();
		// System.out.println("\nִ�й�ʽʱ��:" + (System.currentTimeMillis() -t));
	}

	/**
	 * ִ�м��ع�ʽ ��������:(01-4-26 15:29:06)
	 */
	public void execLoadFormulaByRow(int row) {
		// long t = System.currentTimeMillis();
		if (getRowCount() > 0)
			formulaProcessor.execLoadFormulas(row, row, false);
		// System.out.println("\nִ�й�ʽʱ��:" + (System.currentTimeMillis() -t));
	}

	/**
	 * ִ�м��ع�ʽ ��������:(01-4-26 15:29:06) column : model column please call method
	 * execLoadFormulaByKey(String) instead
	 * 
	 * @deprecated
	 */
	public void execLoadFormula(int column) {
		column = getBodyColByCol(column);
		formulaProcessor.execLoadFormula(column);
	}

	/**
	 * ִ�м��ع�ʽ ��������:(01-4-26 15:29:06)
	 */
	public void execLoadFormulaByKey(String key) {
		formulaProcessor.execLoadFormula(getBodyColByKey(key));
	}

	/**
	 * ִ�м��ع�ʽ ��������:(01-4-26 15:29:06) column : model column please call method
	 * execLoadFormulasByKey(String) instead
	 * 
	 * @deprecated
	 */
	public void execLoadFormulas(int column) {
		column = getBodyColByCol(column);
		formulaProcessor.execLoadFormulas(column);
	}

	/**
	 * ִ�м��ع�ʽ ��������:(01-4-26 15:29:06)
	 */
	public void execLoadFormulasByKey(String key) {
		formulaProcessor.execLoadFormulas(getBodyColByKey(key));
	}

	/*
	 * if formulas is not null, itemkeys ignore;
	 */
	public boolean execValidateForumlas(String[] formulas, String[] itemkeys, int[] rows) {
		return getFormulaProcessor().execValidateForumlas(formulas, itemkeys, rows);
	}

	/**
	 * ��ô���״̬����. ��������:(2001-5-9 14:03:18)
	 */
	public Vector getBillModelData() {
		Vector<Vector> data = new Vector<Vector>();
		// ��״̬
		// data.addElement(transferRowState(vRowState));
		data.addElement(vRowAttrib);
		// ����
		data.addElement(dataVector);
		return data;
	}

	/**
	 * @return Returns the billScrollPane.
	 */
	BillScrollPane getBillScrollPane() {
		return billScrollPane;
	}

	/**
	 * ����ʾ�ж�Ӧʵ���к�. ��������:(01-2-21 14:47:10)
	 * 
	 * ʹ��JTable.convertColumnIndexToModel�����滻
	 * 
	 * @param strKey
	 *            java.lang.String please call this method from Billscrollpane
	 * @return java.lang.String
	 * @deprecated
	 */
	public int getBodyColByCol(int col) {
		int n = -1;
		// ���Ϲ̶���
		if (fixCol > -1) {
			col = col + fixCol;
		}
		for (int i = 0; i < getBodyItems().length; i++) {
			BillItem item = getBodyItems()[i];
			if (item.isShow())
				n++;
			if (n == col)
				return i;
		}
		Logger.info("û���ҵ�" + col + "�ж�Ӧʵ����.");
		return -1;
	}

	/**
	 * ��item�кŵõ���ʾ��. ��������:(01-2-21 14:47:10)
	 * 
	 * ʹ��JTable.convertColumnIndexToModel�滻
	 * 
	 * @param strKey
	 *            java.lang.String please call this method from BillScrollPane
	 * @return java.lang.String
	 * @deprecated
	 */
	public int getBodyColByIndex(int index) {
		int n = 0;
		for (int i = 0; i <= index; i++) {
			BillItem item = getBodyItems()[i];
			if (item.isShow())
				n++;
		}
		if (n > 0)
			return n + fixCol;
		return -1;
	}

	/**
	 * �ɹؼ��ֶ�Ӧ�к�. ��������:(01-2-21 14:47:10)
	 */
	public int getBodyColByKey(String strKey) {
		if (strKey == null)
			return -1;
		try {
			if (htBodyItems != null) {
					strKey = strKey.trim();
					if(htBodyItems.containsKey(strKey))
						return ((Integer) htBodyItems.get(strKey)).intValue();
					if(strKey.endsWith(IBillItem.ID_SUFFIX)) {
						strKey = strKey.substring(0,strKey.length()-IBillItem.ID_SUFFIX.length());
						if(htBodyItems.containsKey(strKey))
							return ((Integer) htBodyItems.get(strKey)).intValue();
					}
			}
			
		} catch (Exception e) {
			Logger.debug(e.getMessage());
		}
		Logger.info("û���ҵ�" + strKey + "��Ӧ�ؼ�����.");
		return -1;
	}

	/**
	 * ���Ԫ��. ��������:(01-2-21 10:08:48)
	 */
	public BillItem[] getBodyItems() {
		return m_biBodyItems;
	}

	/**
	 * ����ʾ�ж�Ӧ�ؼ���. ��������:(01-2-21 14:47:10) please call this method from
	 * 
	 * ʹ��BillScrollPane.getBodyKeyByCol�滻
	 * 
	 * @deprecated
	 */
	public String getBodyKeyByCol(int col) {
		String key = null;
		int n = -1;

		// ���Ϲ̶���
		if (fixCol > -1) {
			col = col + fixCol;
		}
		for (int i = 0; i < getBodyItems().length; i++) {
			BillItem item = getBodyItems()[i];
			if (item.isShow())
				n++;
			if (n == col)
				return item.getKey();
		}
		System.out.println("û���ҵ�" + col + "�ж�Ӧʵ����.");
		return key;
	}

	/*
	 * �õ���ѡ���VO����
	 */

	public CircularlyAccessibleValueObject[] getBodySelectedVOs(String bodyVOName) {
		return getBodySelectedVOs(bodyVOName, -1);
	}

	/*
	 * �õ���ѡ���VO����
	 */

	public CircularlyAccessibleValueObject[] getBodySelectedVOs(String bodyVOName, int count) {
		try {
			Class bodyVOClass = Class.forName(bodyVOName);
			Vector vBodyVOs = getSelectedVector(bodyVOName, count, false)[0];
			if (vBodyVOs != null) {
				CircularlyAccessibleValueObject[] bodyVOs = (CircularlyAccessibleValueObject[]) Array.newInstance(bodyVOClass, vBodyVOs.size());
				vBodyVOs.copyInto(bodyVOs);
				return bodyVOs;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace(System.out);
		}
		return null;
	}

	/*
	 * �õ���ѡ���VO����
	 */
	ArrayList getBodySelectedVOsAndRowNos(String bodyVOName, int count, boolean hasRowNumber) {
		Vector[] vecs = getSelectedVector(bodyVOName, count, hasRowNumber);
		if (vecs == null || vecs.length == 0)
			return null;
		Vector vecBody = vecs[0];
		Vector vecNo = vecs[1];
		if (vecBody == null)
			return null;
		int allCount = vecBody.size();
		ArrayList<Object[]> list = new ArrayList<Object[]>(allCount);
		for (int i = 0; i < allCount; i++) {
			list.add(new Object[] { vecBody.elementAt(i), vecNo.elementAt(i) });
		}
		return list;
	}

	/*
	 * �õ��仯��VO����
	 */

	public CircularlyAccessibleValueObject[] getBodyValueChangeVOs(String bodyVOName) {
		try {
			Class bodyVOClass = Class.forName(bodyVOName);
			Vector vBodyVOs = getValueChangeVector(bodyVOName);
			if (vBodyVOs != null) {
				CircularlyAccessibleValueObject[] bodyVOs = (CircularlyAccessibleValueObject[]) Array.newInstance(bodyVOClass, vBodyVOs.size());
				vBodyVOs.copyInto(bodyVOs);
				return bodyVOs;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace(System.out);
		}
		return null;
	}

	/*
	 * �õ��仯��VO����
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object>[] getBodyChangeValueByMetaData() {
		ArrayList<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		// ɾ����
		if (vDeleteRow != null) {
			for (int i = 0; i < vDeleteRow.size(); i++) {
				Vector rowVector = (Vector) vDeleteRow.elementAt(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				for (int j = 0; j < getBodyItems().length; j++) {
					BillItem item = getBodyItems()[j];
					if (item.getMetaDataProperty() != null && item.getIDColName() == null) {
						Object aValue = rowVector.elementAt(j);
						aValue = item.converType(aValue);
						map.put(item.getMetaDataAccessPath(), aValue);
					}
				}
				// ����״̬
				map.put(IAttribute.KEY_VOSTATUS, VOStatus.DELETED);
				maps.add(map);
			}
		}
		for (int i = 0; i < dataVector.size(); i++) {
			if (getRowState(i) != NORMAL) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				for (int j = 0; j < getBodyItems().length; j++) {
					BillItem item = getBodyItems()[j];
					if (item.getMetaDataProperty() != null && item.getIDColName() == null) {
						Object aValue = getValueAt(i, j);
						aValue = item.converType(aValue);
						map.put(item.getMetaDataAccessPath(), aValue);
					}
				}
				// ����״̬
				switch (getRowState(i)) {
				case ADD: {
					map.put(IAttribute.KEY_VOSTATUS, VOStatus.NEW);
					break;
				}
				case MODIFICATION: {
					map.put(IAttribute.KEY_VOSTATUS, VOStatus.UPDATED);
					break;
				}
				}
				maps.add(map);
			}
		}

		Map<String, Object>[] m = new Map[maps.size()];

		maps.toArray(m);

		return m;
	}

	/*
	 * �õ��仯��VO����
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object>[] getBodyValueByMetaData() {
		ArrayList<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < dataVector.size(); i++) {
			Map<String, Object> map = getBodyRowValueByMetaData(i);
			maps.add(map);
		}

		Map<String, Object>[] m = new Map[maps.size()];

		maps.toArray(m);

		return m;
	}

	/*
	 * �õ���VO
	 */

	public Map<String, Object> getBodyRowValueByMetaData(int row) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (int j = 0; j < getBodyItems().length; j++) {
			BillItem item = getBodyItems()[j];
			if (item.getMetaDataProperty() != null && item.getIDColName() == null) {
				Object aValue = getValueAt(row, j);
				aValue = item.converType(aValue);
				map.put(item.getMetaDataAccessPath(), aValue);
			}
		}
		// ����״̬
		switch (getRowState(row)) {
		case ADD: {
			map.put(IAttribute.KEY_VOSTATUS, VOStatus.NEW);
			break;
		}
		case MODIFICATION: {
			map.put(IAttribute.KEY_VOSTATUS, VOStatus.UPDATED);
			break;
		}
		}

		return map;
	}

	/*
	 * �õ��仯��VO����
	 */

	public GeneralSuperVO[] getBodyValueChangeVOs(GeneralSuperVO initVO) {
		try {
			Vector vBodyVOs = getValueChangeVector(initVO);
			if (vBodyVOs != null) {
				GeneralSuperVO[] bodyVOs = GeneralSuperVO.createVOArray(initVO, vBodyVOs.size(), true);
				vBodyVOs.copyInto(bodyVOs);
				return bodyVOs;
			}
		} catch (Throwable ex) {
			ex.printStackTrace(System.out);
		}
		return null;
	}

	/*
	 * �õ���VO
	 */

	public CircularlyAccessibleValueObject getBodyValueRowVO(int row, String bodyVOName) {
		try {
			CircularlyAccessibleValueObject bodyRowVO = (CircularlyAccessibleValueObject) Class.forName(bodyVOName).newInstance();
			for (int j = 0; j < getBodyItems().length; j++) {
				BillItem item = getBodyItems()[j];
				Object aValue = getValueAt(row, j);
				aValue = item.converType(aValue);
				bodyRowVO.setAttributeValue(item.getKey(), aValue);
			}
			// ����״̬
			switch (getRowState(row)) {
			case ADD:
				bodyRowVO.setStatus(nc.vo.pub.VOStatus.NEW);
				break;
			case MODIFICATION:
				bodyRowVO.setStatus(nc.vo.pub.VOStatus.UPDATED);
				break;
			}
			return bodyRowVO;
		} catch (ClassNotFoundException e) {
			e.printStackTrace(System.out);
		} catch (InstantiationException e) {
			e.printStackTrace(System.out);
		} catch (IllegalAccessException e) {
			e.printStackTrace(System.out);
		}
		return null;
	}

	public void getBodyValueVOs(CircularlyAccessibleValueObject[] bodyVOs) {
		for (int i = 0; i < bodyVOs.length; i++) {
			if (i > getRowCount())
				break;
			for (int j = 0; j < getBodyItems().length; j++) {
				BillItem item = getBodyItems()[j];
				Object aValue = getValueAt(i, j);
				aValue = item.converType(aValue);
				bodyVOs[i].setAttributeValue(item.getKey(), aValue);
			}
			// ����״̬
			switch (getRowState(i)) {
			case ADD:
				bodyVOs[i].setStatus(nc.vo.pub.VOStatus.NEW);
				break;
			case MODIFICATION:
				bodyVOs[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
				break;
			}
		}
	}

	/*
	 * �õ�VO����
	 */

	public CircularlyAccessibleValueObject[] getBodyValueVOs(String bodyVOName) {
		try {
			Class bodyVOClass = Class.forName(bodyVOName);
			int length = dataVector.size();
			CircularlyAccessibleValueObject[] bodyVOs = (CircularlyAccessibleValueObject[]) Array.newInstance(bodyVOClass, length);
			for (int i = 0; i < dataVector.size(); i++) {
				bodyVOs[i] = (CircularlyAccessibleValueObject) Class.forName(bodyVOName).newInstance();
				BillItem[] items = getBodyItems();
				BillItem item;
				Object aValue;
				for (int j = 0; j < items.length; j++) {
					item = items[j];
					aValue = getValueAt(i, j);
					aValue = item.converType(aValue);
					bodyVOs[i].setAttributeValue(item.getKey(), aValue);
				}
				// ����״̬
				switch (getRowState(i)) {
				case ADD:
					bodyVOs[i].setStatus(nc.vo.pub.VOStatus.NEW);
					break;
				case MODIFICATION:
					bodyVOs[i].setStatus(nc.vo.pub.VOStatus.UPDATED);
					break;
				}
			}
			return bodyVOs;
		} catch (ClassNotFoundException e) {
			e.printStackTrace(System.out);
		} catch (InstantiationException e) {
			e.printStackTrace(System.out);
		} catch (IllegalAccessException e) {
			e.printStackTrace(System.out);
		}
		return null;
	}

	// /**
	// * ��������:(2003-6-20 11:11:56)
	// *
	// * @param row int
	// * @param col int
	// * @param color java.awt.Color
	// */
	// @SuppressWarnings("unchecked")
	// HashMap<Cell, Color>[] getCellColor(int[] rows, int[] cols) {
	// if (rows == null || cols == null || rows.length == 0
	// || cols.length == 0)
	// return null;
	//
	// HashMap[] hashColors = { new HashMap<Cell, Color>(), new HashMap<Cell,
	// Color>() };
	// Color color;
	// int row;
	// // hashCellBackGround, hashCellForeGround };
	// for (int i = 0; i < rows.length; i++) {
	// row = rows[i];
	// RowAttribute rowatt = getRowAttribute(row);
	// for (int j = 0; j < cols.length; j++) {
	// if ((color = rowatt.getCellBackColor(cols[j])) != null)
	// hashColors[0].put(new Cell(i, j), color);
	// if ((color = rowatt.getCellForeColor(cols[j])) != null)
	// hashColors[1].put(new Cell(i, j), color);
	// }
	// }
	// if (hashColors[0].size() == 0 && hashColors[1].size() == 0)
	// return null;
	// return hashColors;
	// }

	/**
	 * Returns Object.class by default
	 */
	public Class<?> getColumnClass(int columnIndex) {
		if (getBodyItems() == null)
			return super.getColumnClass(columnIndex);
		switch (getBodyItems()[columnIndex].getDataType()) {
		case BillItem.BOOLEAN:
			return Boolean.class;
		default:
			return String.class;
		}
	}

	/**
	 * �������.
	 */
	public int getColumnCount() {
		if (getBodyItems() == null)
			return super.getColumnCount();
		return getBodyItems().length;
	}

	/**
	 * �õ��кŶ�Ӧ����. ��������:(01-2-21 10:08:48)
	 */
	public String getColumnName(int col) {
		if (getBodyItems() == null)
			return super.getColumnName(col);
		return getBodyItems()[col].getName();
	}

	/**
	 * ���ɾ��������. ��������:(01-2-21 10:08:48)
	 */
	public Vector getDeleteRow() {
		return vDeleteRow;
	}

	/**
	 * ��ñ��༭��. ��������:(2001-9-7 10:57:25)
	 * 
	 * @param newRowEdit
	 *            boolean
	 */
	public int getEditRow() {
		if (m_editRow == null || !dataVector.contains(m_editRow))
			return -1;
		return dataVector.indexOf(m_editRow);
	}

	/**
	 * ��ʽִ����. ��������:(01-2-21 10:08:48)
	 */
	public FormulaParse getFormulaParse() {
		if (m_formulaParse == null) {
			m_formulaParse = createDefaultFormulaParse();
		}
		return m_formulaParse;
	}

	/**
	 * @return Returns the formulaProcessor.
	 */
	BillModelFormulaProcessor getFormulaProcessor() {
		return formulaProcessor;
	}

	/**
	 * billitem.
	 */
	public BillItem getItemByKey(String key) {
		return htBillItems.get(key);
	}

	/**
	 * �������.
	 */
	public int getItemIndex(Object itemOrKey) {
		if (itemOrKey == null)
			throw new NullPointerException("itemOrkey is null.");
		BillItem[] items = getBodyItems();
		if (items == null)
			return -1;
		if (itemOrKey instanceof String) {
			for (int i = 0; i < items.length; i++) {
				if ((itemOrKey).equals(items[i].getKey()))
					return i;
			}
		} else if (itemOrKey instanceof BillItem) {
			BillItem item = (BillItem) itemOrKey;
			for (int i = 0; i < items.length; i++) {
				if (item.getKey().equals(items[i].getKey()))
					return i;
			}
		}
		throw new java.lang.IllegalArgumentException("itemOrKey not found!");
	}

	public int getPasteLineNumer() {
		if (vViewRowCopy == null)
			return 0;
		return vViewRowCopy.size();
	}

	/**
	 * ���༭״̬. ��������:(2001-9-7 10:57:25)
	 * 
	 * @param newRowEdit
	 *            boolean
	 */
	public boolean getRowEditState() {
		return m_bRowEditState;
	}

	/**
	 * �кű�ģʽ. ��������:(01-2-21 10:08:48)
	 */
	public DefaultTableModel getRowNOTableModel() {
		if (m_tmlRowNO == null) {
			m_tmlRowNO = createDefaultRowNumberModel();
		}
		return m_tmlRowNO;
	}

	/**
	 * �����״̬. ��������:(2001-4-16 9:45:23)
	 * 
	 * @param row
	 *            int
	 * @return int
	 */
	public int getRowState(int row) {
		return getRowStateModel(row);
	}

	/**
	 * �����״̬. ��������:(2001-4-16 9:45:23)
	 * 
	 * @param row
	 *            int
	 * @return int
	 */
	private int getRowStateModel(int row) {
		RowAttribute ra = getRowAttribute(row);

		if (ra == null)
			return UNSTATE;
		else
			return ra.getRowState();

	}

	public RowAttribute getRowAttribute(int row) {
		if (dataVector == null)
			return null;
		if (dataVector.size() <= row)
			return null;

		return vRowAttrib.get(row);
	}

	public void addRowAttributeObject(int row, String key, Object o) {
		RowAttribute ra = getRowAttribute(row);
		if (ra != null) {
			ra.addAttributeObject(key, o);
		}
	}

	public Object getRowAttributeObject(int row, String key) {
		RowAttribute ra = getRowAttribute(row);
		if (ra != null) {
			return ra.getAttributeObject(key);
		}
		return null;
	}

	public void removeRowAttributeObject(int row, String key) {
		RowAttribute ra = getRowAttribute(row);
		if (ra != null) {
			ra.removeAttributeObject(key);
		}
	}

	/**
	 * ������ѡ����VO����
	 */
	protected Vector getSelectedVector(String bodyVOName) {
		return getSelectedVector(bodyVOName, -1, false)[0];
	}

	/**
	 * ������ѡ����VO����
	 */
	private Vector[] getSelectedVector(String bodyVOName, int number, boolean hasRowNumber) {
		Vector<CircularlyAccessibleValueObject> bodyVOs = new Vector<CircularlyAccessibleValueObject>();
		Vector<Integer> rowNumber = hasRowNumber ? new Vector<Integer>() : null;
		try {
			for (int i = 0; i < dataVector.size(); i++) {
				if (getRowState(i) == SELECTED) {
					CircularlyAccessibleValueObject bodyVO = (CircularlyAccessibleValueObject) Class.forName(bodyVOName).newInstance();
					for (int j = 0; j < getBodyItems().length; j++) {
						BillItem item = getBodyItems()[j];
						Object aValue = getValueAtModel(i, j);
						aValue = item.converType(aValue);
						bodyVO.setAttributeValue(item.getKey(), aValue);
					}
					bodyVOs.add(bodyVO);
					if (hasRowNumber)
						rowNumber.addElement(new Integer(i));
					if (number != -1 && bodyVOs.size() == number)
						return new Vector[] { bodyVOs, rowNumber };
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace(System.out);
		} catch (InstantiationException e) {
			e.printStackTrace(System.out);
		} catch (IllegalAccessException e) {
			e.printStackTrace(System.out);
		}
		return new Vector[] { bodyVOs, rowNumber };
	}

	/**
	 * ���ص�ǰ�����������е��к�
	 * 
	 * @return int
	 * @throws
	 * @see
	 * @since v1.0
	 * @deprecated
	 */
	public int getSortColumn() {
		if (getSortColumns() != null && getSortColumns().size() > 0)
			return getSortColumns().get(0).getColumn();
		return -1;
	}

	/**
	 * ���ص�ǰ�����������е��к�
	 * 
	 * @return int
	 * @exception
	 * @see
	 * @since v1.0
	 */
	public List<SortItem> getSortColumns() {
		return m_sortColumn;
	}

	/**
	 * ���ص�ǰ�����������еĹؼ���
	 * 
	 * @deprecated
	 */
	public String getSortColumnKey() {
		String itemkey = null;
		if (getSortColumnKeys() != null)
			itemkey = getSortColumnKeys().get(0);
		return itemkey;
	}

	/**
	 * ���ص�ǰ�����������е��к�
	 * 
	 * @return int
	 * @exception
	 * @see
	 * @since v1.0
	 */
	public List<String> getSortColumnKeys() {
		List<String> itemkeys = null;

		if (getSortColumns() != null && getSortColumns().size() > 0) {
			itemkeys = new ArrayList<String>();

			for (int i = 0; i < getSortColumns().size(); i++) {

				String itemkey = getBodyItems()[getSortColumns().get(i).getColumn()].getKey();
				itemkeys.add(itemkey);

			}

		}

		return itemkeys;
	}

	/**
	 * ���ص�ǰ�����������е��к�.
	 * 
	 * �˷����Է���,��������Ϊ��ģ������,��ֱ��ʹ�ñ�����
	 * 
	 * @return int
	 * @throws
	 * @see
	 * @since v1.0
	 * @deprecated
	 */
	public int[] getSortIndex() {
		if (dataVector != null) {
			if (m_indexes == null || m_indexes.length != dataVector.size()) {
				allocate();
			}
		}
		return m_indexes;
	}

	/**
	 * @return Returns the sortPrepareListener.
	 */
	public IBillModelSortPrepareListener getSortPrepareListener() {
		return sortPrepareListener;
	}

	/**
	 * �ϼ��б�ģʽ. ��������:(01-2-21 10:08:48)
	 */
	public DefaultTableModel getTotalTableModel() {
		if (m_tmlTotal == null) {
			m_tmlTotal = createDefaultTotalTableModel();
		}
		if (m_biBodyItems != null)
			m_tmlTotal.setNumRows(1);
		return m_tmlTotal;
	}

	/**
	 * �õ���Ԫֵ.
	 */
	public Object getValueAt(int row, int column) {
		return getValueAtModel(row, column);
	}

	/**
	 * �õ���Ԫֵ.
	 */
	public Object getValueAt(int rowIndex, String strKey) {

		int colIndex = getBodyColByKey(strKey);
		Object o = getValueAt(rowIndex, colIndex);

		if (o instanceof IConstEnum) {
			if (strKey.endsWith(IBillItem.ID_SUFFIX))
				o = ((IConstEnum) o).getValue();
			else
				o = ((IConstEnum) o).getName();
		}

		return o;
	}

	/**
	 * �õ���Ԫֵ.
	 */
	Object getValueAtModel(int row, int column) {
		Vector rowVector = getRowVectorAtModel(row);
		if (column >= 0 && rowVector != null && rowVector.size() > column) {
			Object aValue = rowVector.elementAt(column);
			String sv;
			if (getBodyItems()[column].getDataType() == BillItem.BOOLEAN)
				if (aValue != null && aValue instanceof UFBoolean) {
					aValue = Boolean.valueOf(((UFBoolean) aValue).booleanValue());
				}
			if (aValue instanceof String) {
				sv = ((String) aValue).trim();
				if (sv.length() == 0) {
					aValue = null;
				}
			}
			return aValue;
		}
		return null;
	}

	/**
	 * �õ���Ԫֵ.
	 */
	Object getValueAtModel(int rowIndex, String strKey) {
		int colIndex = getBodyColByKey(strKey);
		return getValueAtModel(rowIndex, colIndex);
	}

	/**
	 * �����ѱ仯��VO����
	 */
	protected Vector getValueChangeVector(String bodyVOName) {
		Vector<CircularlyAccessibleValueObject> bodyVOs = new Vector<CircularlyAccessibleValueObject>();
		try {
			if (vDeleteRow != null) {
				for (int i = 0; i < vDeleteRow.size(); i++) {
					CircularlyAccessibleValueObject bodyVO = (CircularlyAccessibleValueObject) Class.forName(bodyVOName).newInstance();
					Vector rowVector = (Vector) vDeleteRow.elementAt(i);
					for (int j = 0; j < getBodyItems().length; j++) {
						BillItem item = getBodyItems()[j];
						// int col = getBodyColByKey(item.getKey());
						// Object aValue = rowVector.elementAt(col);
						Object aValue = rowVector.elementAt(j);
						aValue = item.converType(aValue);
						bodyVO.setAttributeValue(item.getKey(), aValue);
					}
					// ����״̬
					bodyVO.setStatus(nc.vo.pub.VOStatus.DELETED);
					bodyVOs.add(bodyVO);
				}
			}
			for (int i = 0; i < dataVector.size(); i++) {
				if (getRowState(i) != NORMAL) {
					CircularlyAccessibleValueObject bodyVO = (CircularlyAccessibleValueObject) Class.forName(bodyVOName).newInstance();
					for (int j = 0; j < getBodyItems().length; j++) {
						BillItem item = getBodyItems()[j];
						// Object aValue = getValueAt(i,item.getKey());
						Object aValue = getValueAt(i, j);
						aValue = item.converType(aValue);
						bodyVO.setAttributeValue(item.getKey(), aValue);
					}
					// ����״̬
					switch (getRowState(i)) {
					case ADD: {
						bodyVO.setStatus(nc.vo.pub.VOStatus.NEW);
						break;
					}
					case MODIFICATION: {
						bodyVO.setStatus(nc.vo.pub.VOStatus.UPDATED);
						break;
					}
					}
					bodyVOs.add(bodyVO);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace(System.out);
		} catch (InstantiationException e) {
			e.printStackTrace(System.out);
		} catch (IllegalAccessException e) {
			e.printStackTrace(System.out);
		}
		return bodyVOs;
	}

	/**
	 * �����ѱ仯��VO����
	 */
	protected Vector getValueChangeVector(GeneralSuperVO initVO) {
		Vector<GeneralSuperVO> bodyVOs = new Vector<GeneralSuperVO>();
		if (vDeleteRow != null) {
			for (int i = 0; i < vDeleteRow.size(); i++) {
				GeneralSuperVO bodyVO = GeneralSuperVO.createVOArray(initVO, 1, false)[0];
				Vector rowVector = (Vector) vDeleteRow.elementAt(i);
				for (int j = 0; j < getBodyItems().length; j++) {
					BillItem item = getBodyItems()[j];
					Object aValue = rowVector.elementAt(j);
					aValue = item.converType(aValue);
					bodyVO.setAttributeValue(item.getKey(), aValue);
				}
				// ����״̬
				bodyVO.setStatus(nc.vo.pub.VOStatus.DELETED);
				bodyVOs.add(bodyVO);
			}
		}
		for (int i = 0; i < dataVector.size(); i++) {
			if (getRowState(i) != NORMAL) {
				GeneralSuperVO bodyVO = GeneralSuperVO.createVOArray(initVO, 1, false)[0];
				for (int j = 0; j < getBodyItems().length; j++) {
					BillItem item = getBodyItems()[j];
					Object aValue = getValueAt(i, j);
					aValue = item.converType(aValue);
					bodyVO.setAttributeValue(item.getKey(), aValue);
				}
				// ����״̬
				switch (getRowState(i)) {
				case ADD: {
					bodyVO.setStatus(nc.vo.pub.VOStatus.NEW);
					break;
				}
				case MODIFICATION: {
					bodyVO.setStatus(nc.vo.pub.VOStatus.UPDATED);
					break;
				}
				}
				bodyVOs.add(bodyVO);
			}
		}
		return bodyVOs;
	}

	/**
	 * ������ ��������:(01-2-28 11:16:53)
	 */
	public void insertRow(int row) {
		// int mrow = convertIntoModelRow(row);
		// if (mrow < 0 && (row == getRowCount()))
		// mrow = row;
		// insertRowModel(mrow);
		insertRowModel(row);
	}

	/**
	 * ������ ��������:(01-2-28 11:16:53)
	 */
	private void insertRowModel(int row) {
		if (row < 0)
			return;
		Vector<Object> vNewRow = createRowVector(row);

		if(getCellSpan() != null) {
			getCellSpan().insertLine(row);
		}

		insertRow(row, vNewRow);
		// ������״̬
		vRowAttrib.insertElementAt(createRowAttribute(), row);

		vNewRow = new Vector<Object>();
		vNewRow.add(null);
		getRowNOTableModel().insertRow(row, vNewRow);

		fireTableChanged(new TableModelEvent(this));
	}

	/**
	 * ��Ԫ���Ƿ�ɱ༭
	 */
	public boolean isCellEditable(int row, int col) {
		boolean editable = isCellEditableModel(row, col);

		BillModelCellEditableController editableController = getCellEditableController();
		if (editableController != null) {
			editable = editableController.isCellEditable(editable, row, getBodyItems()[col].getKey());
		}
		return editable;
	}

	/**
	 * ��Ԫ���Ƿ�ɱ༭
	 */
	private boolean isCellEditableModel(int row, int col) {
		if (row < 0 || col < 0)
			return false;

		// whole switch
		if (!m_bEnabled)
			return false;

		// cell switch
		// ��Ԫ�༭
		BillItem item = getBodyItems()[col];
		// cell editable
		// if (htCellEdit != null) {
		// String strKey = row + "," + item.getKey().toString();
		// if (htCellEdit.containsKey(strKey)) {
		// return ((Boolean) htCellEdit.get(strKey)).booleanValue();
		// }
		// }
		Boolean cellEdit = getRowAttribute(row).isCellEdit(item.getKey());
		if (cellEdit != null)
			return cellEdit.booleanValue();
		// column switch
		if (!item.isEnabled())
			return false;
		// row switch
		boolean rowedit = true;
		// �б༭״̬
		if (getRowEditState()) {
			if (getEditRow() != -1) {
				if (getEditRow() != row)
					rowedit = false;
			} else {
				rowedit = !isNotEditRowModel(row);
			}
		}
		return rowedit;
	}

	/**
	 * �������Ƿ�ѡ��. ��������:(2001-11-29 15:35:27)
	 * 
	 * @return boolean
	 */
	public boolean isHasSelectRow() {
		for (int i = 0, j = getRowCount(); i < j; i++) {
			if (getRowStateModel(i) == SELECTED)
				return true;
		}
		return false;
	}

	/**
	 * ��������:(2003-01-24 9:30:49)
	 * 
	 * @return boolean
	 */
	public boolean isNeedCalculate() {
		return m_bNeedCalculate;
	}

	/**
	 * �жϲ��ɱ༭��. ��������:(2001-9-18 15:29:11)
	 * 
	 * @param row
	 *            int
	 * @return boolean
	 */
	protected boolean isNotEditRow(int row) {
		return isNotEditRowModel(row);
	}

	protected boolean isNotEditRowModel(int row) {
		if (row > -1 && row < getRowCount())
			return !getRowAttribute(row).isEdit();
		// if (htNotEditRow == null)
		// return false;
		// Object o = dataVector.get(row);
		// Object key = "" + System.identityHashCode(o);
		// if (htNotEditRow.containsKey(key))
		// return true;
		return false;
	}

	/**
	 * ճ������.
	 */
	public void pasteLine(int row) {
		pasteLineModel(row);
	}

	/**
	 * ճ������.
	 */
	private void pasteLineModel(int row) {
		if (vViewRowCopy == null)
			return;
		if (row < 0)
			return;

		int[] rows = new int[vViewRowCopy.size()];
		for (int i = 0; i < vViewRowCopy.size(); i++) {
			Vector<Object> vRow = transferBodyRowData((Vector) vViewRowCopy.elementAt(i));

			insertRow(row + i, vRow);
			rows[i] = row + i;
			// �к�
			vRow = new Vector<Object>();
			vRow.add(null);
			getRowNOTableModel().insertRow(row, vRow);
			// ��״̬
			vRowAttrib.insertElementAt(createRowAttribute(), row + i);
			
			if(getCellSpan() != null)
				getCellSpan().insertLine(row + i);
			
		}
		reCalcurateAll();

		// fireTableChanged(new TableModelEvent(this));
	}

	/**
	 * ճ������.
	 */
	void pasteLineToTail() {
		pasteLineModel(getRowCount());
	}

	/**
	 * �����кϼ� ��������:(01-2-28 11:16:53)
	 * 
	 * @param column
	 *            int : model column
	 */
	public void reCalcurate(int column) {
		if (isNeedCalculate()) {
			UFDouble total = new UFDouble(0.0);
			UFDouble aValue = null;
			BillItem item = getBodyItems()[column];
			if (btl == null) {
				for (int i = 0; i < dataVector.size(); i++) {
					if (isRowSelectMode() && !(getRowAttribute(i).getRowState() == SELECTED))
						continue;
					Object o = getValueAtModel(i, column);
					total = total.add(createUFDouble(o));
				}
			} else {
				total = btl.calcurateTotal(item.getKey());
			}
			if (total != null) {
				int digit = 0;
				if (item.getDataType() == BillItem.DECIMAL) {
					digit = 0 - item.getDecimalDigits();
				}
				aValue = total.setScale(digit, UFDouble.ROUND_HALF_UP);
			}
			if (m_tmlTotal != null)
				m_tmlTotal.setValueAt(aValue, 0, column);
		}
	}

	/**
	 * ���������кϼ� ��������:(01-2-28 11:16:53)
	 * 
	 * @param iCol
	 *            int
	 */
	public void reCalcurateAll() {
		if (!isNeedCalculate() || getBodyItems() == null)
			return;
		for (int column = 0; column < getBodyItems().length; column++) {
			if (getBodyItems()[column].isTotal() && (getBodyItems()[column].getDataType() == BillItem.INTEGER || getBodyItems()[column].getDataType() == BillItem.DECIMAL))
				reCalcurate(column);
		}
	}

	// /**
	// * ��������:(2003-6-20 11:11:56)
	// *
	// * @param row int
	// * @param col int
	// * @param color java.awt.Color
	// */
	// void removeCellColors(int row, int type) {
	//		
	// RowAttribute rowatt = getRowAttribute(row);
	//		
	// int colcount = getColumnCount();
	// for (int i = 0; i < colcount; i++) {
	//			
	// switch (type) {
	// case Cell.BACKANDFORECOLOR:
	// rowatt.clearCellBackColor(i);
	// rowatt.clearCellForeColor(i);
	// break;
	// case Cell.BACKCOLOR:
	// rowatt.clearCellBackColor(i);
	// break;
	// case Cell.FORECOLOR:
	// rowatt.clearCellForeColor(i);
	// break;
	// default:
	// break;
	// }
	// }
	// }

	/**
	 * �Ƴ��ϼƼ���. ��������:(2001-10-24 16:30:33)
	 * 
	 * @param btl
	 *            nc.ui.pub.bill.BillTotalListener
	 */
	public void removeTotalListener() {
		this.btl = null;
	}

	/**
	 * �Ƴ��ϼƼ���. ��������:(2001-10-24 16:30:33)
	 * 
	 * @param btl
	 *            nc.ui.pub.bill.BillTotalListener
	 */
	public void removeTotalListener(BillTotalListener btl) {
		this.btl = null;
	}

	// -----------------------------------------------------------------
	/**
	 * �Ƴ����ȼ���:
	 */
	public void removeDecimalListener() {
		BillItem[] items = getBodyItems();
		for (int i = 0; i < items.length; i++) {
			items[i].removeDecimalListener();
		}
	}

	// --------------------------------------------------------------

	/**
	 * �ָ���������.
	 */
	public void resumeValue() {
		dataVector = transferBodyData(vViewBodyCache);
		getRowNOTableModel().setNumRows(dataVector.size());
		int size = dataVector.size();
		// ��״̬�޸�Ϊ����
		vRowAttrib = new Vector<RowAttribute>(size);
		for (int i = 0; i < size; i++) {
			RowAttribute ra = createRowAttribute();
			ra.setRowState(NORMAL);
			vRowAttrib.addElement(ra);
		}
		vDeleteRow = null;
		reCalcurateAll();

		if (getSortColumnKeys() != null)
			sortByColumns(getSortColumns());
	}

	/**
	 * ���ô���״̬����. ��������:(2001-5-9 14:03:18)
	 */
	@SuppressWarnings("unchecked")
	public void setBillModelData(Vector vData) {
		if (vData == null)
			return;
		if (vData.size() < 2)
			return;
		// vRowState = (Vector) vData.elementAt(0);
		vRowAttrib = (Vector) vData.elementAt(0);
		dataVector = (Vector) vData.elementAt(1);

		//
		setSortIndex(null);
		getRowNOTableModel().setNumRows(dataVector.size());
		newRowsAdded(new TableModelEvent(this, 0, getRowCount() - 1, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	}

	/**
	 * @param billScrollPane
	 *            The billScrollPane to set.
	 */
	void setBillScrollPane(BillScrollPane billScrollPane) {
		this.billScrollPane = billScrollPane;
	}

	/**
	 * ���ñ�������. ��������:(01-2-23 14:22:07)
	 */
	public void setBodyDataVO(CircularlyAccessibleValueObject[] bodyVOs) {
		isFirstSetBodyDataVO = true;
		clearBodyData();
		if (bodyVOs == null || bodyVOs.length == 0)
			return;
		boolean needCalculate = isNeedCalculate();
		setNeedCalculate(false);
		setChangeTable(false);
		// if (bodyVOs[0] instanceof nc.vo.pub.SuperVO
		// && !(bodyVOs[0] instanceof nc.vo.pub.general.GeneralSuperVO)) {
		// SuperVO bodyRowVO;
		// BillItem[] items = getBodyItems();
		// String[] fieldnames = new String[items.length];
		// for (int i = 0; i < fieldnames.length; i++)
		// fieldnames[i] = items[i].getKey();
		// java.lang.reflect.Method[] mts = SuperVOGetterSetter.getGetMethods(
		// bodyVOs[0].getClass(), fieldnames);
		// BillItem item;
		// Object aValue;
		// try {
		// dataVector.ensureCapacity(bodyVOs.length);
		// for (int i = 0; i < bodyVOs.length; i++) {
		// addLine();
		// bodyRowVO = (SuperVO) bodyVOs[i];
		// for (int ii = 0; ii < items.length; ii++) {
		// item = items[ii];
		// if (mts[ii] != null)
		// aValue = bodyRowVO.invokeGetMethod(mts[ii],
		// fieldnames[ii]);
		// else
		// aValue = bodyRowVO
		// .getAttributeValue(fieldnames[ii]);
		// // -------------------------
		// if (item.getDataType() == BillItem.DECIMAL)
		// setItemDecimal(bodyRowVO, i, item);
		// else if(aValue != null && item.getDataType() == IBillItem.UFREF)
		// aValue = new DefaultConstEnum(aValue,(String)aValue);
		// // --------------------------
		// setValueAtModel(aValue, i, ii);
		// }
		// // setBodyRowVOModel(bodyVOs[i], i);
		// }
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// } else {
		for (int i = 0; i < bodyVOs.length; i++) {
			addLine();
			setBodyRowVOModel(bodyVOs[i], i);
		}
		// }
		setNeedCalculate(needCalculate);

		if (getSortColumns() != null)
			sortByColumns(getSortColumns());

		setChangeTable(true);
		fireTableChanged(new TableModelEvent(this));
		isFirstSetBodyDataVO = false;

	}

	/**
	 * ���ñ�������. ��������:(01-2-23 14:22:07)
	 */
	protected void setBodyObjectByMetaData(NCObject[] bodys) {
		isFirstSetBodyDataVO = true;
		clearBodyData();
		if (bodys == null || bodys.length == 0)
			return;
		boolean needCalculate = isNeedCalculate();
		setNeedCalculate(false);
		setChangeTable(false);

		try {
			dataVector.ensureCapacity(bodys.length);
			for (int i = 0; i < bodys.length; i++) {
				addLine();
				setBodyRowObjectModelByMetaData(bodys[i], i);
			}
			long t1 = System.currentTimeMillis();
			loadLoadRelationItemValue();
			System.out.println(System.currentTimeMillis() - t1);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		setNeedCalculate(needCalculate);

		if (getSortColumns() != null)
			sortByColumns(getSortColumns());

		setChangeTable(true);
		fireTableChanged(new TableModelEvent(this));
		isFirstSetBodyDataVO = false;

	}

	protected void setBodyRowObjectByMetaData(NCObject o, int row) {
		setBodyRowObjectModelByMetaData(o, row);
		loadLoadRelationItemValue(row);
	}

	private void setBodyRowObjectModelByMetaData(NCObject o, int row) {
		if (o == null || row < 0)
			return;

		BillItem[] items = getBodyItems();

		Object aValue;

		for (int col = 0; col < items.length; col++) {
			BillItem item = items[col];

			if (item.getMetaDataProperty() != null && item.getIDColName() == null) {
				aValue = o.getAttributeValue(item.getMetaDataProperty().getAttribute());
				//
				// switch (item.getDataType()) {
				// case IBillItem.UFREF:
				// if(aValue != null)
				// aValue = new DefaultConstEnum(aValue,(String)aValue);
				//					
				// break;
				//
				// case IBillItem.COMBO:
				// if(aValue == null || aValue.toString().length() == 0 )
				// aValue = null;
				// else if(item.getComponent() != null &&
				// item.isComboBoxReturnInteger() && !(aValue instanceof
				// Integer))
				// aValue = new Integer(aValue.toString());
				// break;
				// default:
				// break;
				// }
				setValueAtModel(aValue, row, col);
			}
		}

	}

	/**
	 * ���ñ�������. ��������:(01-2-23 14:22:07)
	 */
	protected void setImportBodyDataVO(CircularlyAccessibleValueObject[] bodyVOs) {
		setImportBodyDataVO(bodyVOs, true);
	}

	/**
	 * ���ñ�������. ��������:(01-2-23 14:22:07)
	 */
	protected void setImportBodyDataVO(CircularlyAccessibleValueObject[] bodyVOs, boolean isExecEditFormulas) {
		isFirstSetBodyDataVO = true;
		// clearBodyData();
		if (bodyVOs == null || bodyVOs.length == 0)
			return;

		clearBodyData();

		boolean needCalculate = isNeedCalculate();
		setNeedCalculate(false);
		setChangeTable(false);
		for (int i = 0; i < bodyVOs.length; i++) {
			addLine();
			Object aValue;
			BillItem item;
			String key;

			// for (int j = 0; j < items.length; j++) {
			for (int j = 0; j < bodyVOs[i].getAttributeNames().length; j++) {
				item = getItemByKey(bodyVOs[i].getAttributeNames()[j]);
				if (item != null) {
					key = item.getKey();
					aValue = bodyVOs[i].getAttributeValue(key);
					// aValue = convertVoValueToComboBoxName(aValue, item);
					// --------------
					if (item.getDataType() == IBillItem.DECIMAL)
						setBillItemDecimalByVO(item, getRowCount() - 1, bodyVOs[i]);
					// -------------
					if (aValue != null)
						setValueAtModel(aValue, getRowCount() - 1, getBodyColByKey(key));
				}

			}
			BillItem[] items = getBodyItems();
			for (int j = 0; j < items.length; j++) {
				if (items[j].getDataType() == IBillItem.UFREF) {
					aValue = bodyVOs[i].getAttributeValue(items[j].getKey());

					UIRefPane ref = (UIRefPane) items[j].getComponent();

					if (aValue != null) {
						ref.setBlurValue(aValue.toString());

						Object o = ref.getRefPK();

						if (items[j].getIDColName() != null)
							setValueAtModel(o, getRowCount() - 1, items[j].getIDColName());
						else
							setValueAt(o, getRowCount() - 1, items[j].getKey() + IBillItem.ID_SUFFIX);
					}
				}
			}
		}

		if (isExecEditFormulas && getRowCount() > 0)
			formulaProcessor.execEditFormulas(0, getRowCount() - 1, false);

		loadLoadRelationItemValue();

		setNeedCalculate(needCalculate);

		// if(getSortColumn() >= 0){
		// sortByColumn(getSortColumn(),m_ascending);
		// }
		if (getSortColumns() != null)
			sortByColumns(getSortColumns());

		setChangeTable(true);
		fireTableChanged(new TableModelEvent(this));
		isFirstSetBodyDataVO = false;

	}

	/**
	 * ���Ԫ��. ��������:(01-2-21 10:08:48)
	 */
	public void setBodyItems(BillItem[] newItems) {
		m_biBodyItems = newItems;
		htBodyItems = new Hashtable<String, Integer>();
		htBillItems.clear();
		setSortColumns(null);
		
		cellSpan = null;

		// HashMap<String, ArrayList<BillItem>> ritems = new
		// HashMap<String,ArrayList<BillItem>>();

		if (newItems != null) {
			for (int i = 0; i < newItems.length; i++) {
				htBodyItems.put(newItems[i].getKey(), new Integer(i));
				if (newItems[i].getDataType() == IBillItem.UFREF) {
					htBodyItems.put(newItems[i].getKey() + IBillItem.ID_SUFFIX, new Integer(i));
				}

				// if(newItems[i].getIDColName() != null &&
				// !newItems[i].getKey().equals(newItems[i].getIDColName())){
				// ArrayList<BillItem> ritemnames = null;
				// if(!ritems.containsKey(newItems[i].getIDColName())){
				// ritemnames = new ArrayList<BillItem>();
				// }else{
				// ritemnames = ritems.get(newItems[i].getIDColName());
				// }
				//					
				// ritemnames.add(newItems[i]);
				// ritems.put(newItems[i].getIDColName(), ritemnames);
				// }

				htBillItems.put(newItems[i].getKey(), newItems[i]);
			}

			for (int i = 0; i < newItems.length; i++) {
				BillItem item = newItems[i];
				if (item != null) {
					if (item.getIDColName() != null && !item.getKey().equals(item.getIDColName())) {
						BillItem iditem = htBillItems.get(item.getIDColName());
						if (iditem != null)
							iditem.addRelationItem(item);
					}
				}
			}
		}
		if (m_tmlTotal != null) {
			setTotalTableModel(null);
			getTotalTableModel();
		}
	}

	/**
	 * ���ñ���������. ��������:(01-2-23 14:22:07)
	 */
	public void setBodyRowVO(CircularlyAccessibleValueObject bodyRowVO, int row) {
		setBodyRowVOModel(bodyRowVO, row);
	}

	/**
	 * ���ñ���������. ��������:(01-2-23 14:22:07)
	 */
	private void setBodyRowVOModel(CircularlyAccessibleValueObject bodyRowVO, int row) {
		if (bodyRowVO == null)
			return;
		Object aValue;
		BillItem item;
		String key;
		BillItem[] items = getBodyItems();
		for (int i = 0; i < items.length; i++) {
			item = items[i];
			key = item.getKey();
			aValue = bodyRowVO.getAttributeValue(key);
			// aValue = convertVoValueToComboBoxName(aValue, item);
			// --------------
			switch (item.getDataType()) {
			case IBillItem.DECIMAL:
				setBillItemDecimalByVO(item, row, bodyRowVO);
				break;
			case IBillItem.UFREF:
				if (aValue == null || aValue.toString().length() == 0)
					aValue = null;
				Object o = getValueAt(row, i);
				if (o != null && o instanceof IConstEnum)
					if (aValue != null)
						aValue = new DefaultConstEnum(((IConstEnum) o).getValue(), aValue.toString());
					else
						aValue = new DefaultConstEnum(((IConstEnum) o).getValue(), null);
				else if (aValue != null)
					aValue = new DefaultConstEnum(aValue, aValue.toString());
				break;
			// case IBillItem.COMBO:
			// if(aValue == null || aValue.toString().length() == 0 )
			// aValue = null;
			// else if(item.getComponent() != null &&
			// item.isComboBoxReturnInteger() && !(aValue instanceof Integer))
			// aValue = new Integer(aValue.toString());
			// break;
			default:
				break;
			}

			// -------------
			setValueAtModel(aValue, row, i);
			setItemRelationValueAt(aValue, row, item);
		}
	}

	// ͬ�����¹�����ID��
	private void setItemRelationValueAt(Object aValue,int row,BillItem item){
		
		if(aValue == null || item.getRelationItem()== null)
			return;
		
    	for (int j = 0; j < item.getRelationItem().size(); j++) {
    		BillItem ritem = item.getRelationItem().get(j);
    		
    		if(ritem.getDataType() == IBillItem.UFREF){
        		int column = getBodyColByKey(item.getRelationItem().get(j).getKey());
        		
        		if(column >= 0){
	        		Object o = getValueAt(row, column);
	        		
	        		if(aValue != null){
	        			if(aValue instanceof IConstEnum)
	        				aValue = ((IConstEnum)aValue).getValue();

	        			if(aValue != null && aValue.toString().length() > 0) {
		        			if(o != null && o instanceof IConstEnum)
			        			aValue = new DefaultConstEnum(aValue,((IConstEnum)o).getName());
			        		else
			        			aValue = new DefaultConstEnum(aValue,(String)aValue);
	        			}else{
	        				aValue = null;
	        			}
	        		}else
	        			aValue = null;
	        	
	        		setValueAtModel(aValue, row, column);
        		}
    		}
		}
	}

	/**
	 * ���ñ�������. ��������:(01-2-23 14:22:07)
	 */
	public void setBodyRowVOs(CircularlyAccessibleValueObject[] bodyVOs, int[] rows) {
		if (bodyVOs == null || bodyVOs.length == 0 || rows == null || bodyVOs.length != rows.length)
			return;
		// rows = convertIntoModelRow(rows);
		int rowcount = getRowCount();
		boolean needCalculate = isNeedCalculate();
		setNeedCalculate(false);
		setChangeTable(false);
		// if (bodyVOs[0] instanceof nc.vo.pub.SuperVO
		// && !(bodyVOs[0] instanceof nc.vo.pub.general.GeneralSuperVO)) {
		// SuperVO bodyRowVO;
		// BillItem[] items = getBodyItems();
		// String[] fieldnames = new String[items.length];
		// for (int i = 0; i < fieldnames.length; i++)
		// fieldnames[i] = items[i].getKey();
		// java.lang.reflect.Method[] mts = SuperVOGetterSetter.getGetMethods(
		// bodyVOs[0].getClass(), fieldnames);
		// // BillItem item;
		// Object aValue;
		// try {
		// for (int i = 0; i < bodyVOs.length; i++) {
		// if (rows[i] < 0 || rows[i] >= rowcount)
		// continue;
		// bodyRowVO = (SuperVO) bodyVOs[i];
		// for (int ii = 0; ii < items.length; ii++) {
		// // item = items[ii];
		// if (mts[ii] != null)
		// aValue = bodyRowVO.invokeGetMethod(mts[ii],
		// fieldnames[ii]);
		// else
		// aValue = bodyRowVO
		// .getAttributeValue(fieldnames[ii]);
		// // aValue = convertVoValueToComboBoxName(aValue, item);
		// if(aValue != null && items[ii].getDataType() == IBillItem.UFREF)
		// aValue = new DefaultConstEnum(aValue,(String)aValue);
		//
		// setValueAtModel(aValue, rows[i], ii);
		// }
		// // setBodyRowVOModel(bodyVOs[i], rows[i]);
		// }
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// } else {
		for (int i = 0; i < bodyVOs.length; i++) {
			if (rows[i] < 0 || rows[i] >= rowcount)
				continue;
			setBodyRowVOModel(bodyVOs[i], rows[i]);
		}
		// }
		setNeedCalculate(needCalculate);
		setChangeTable(true);
		fireTableChanged(new TableModelEvent(this));
	}

	/**
	 * ��������:(2003-6-20 11:11:56)
	 * 
	 * @param row
	 *            int
	 * @param col
	 *            int
	 * @param color
	 *            java.awt.Color
	 */
	void setCellColor(int row, int col, java.awt.Color color, int type) {

		RowAttribute rowatt = getRowAttribute(row);

		switch (type) {
		case ColoredCell.BACKANDFORECOLOR:
			rowatt.setCellBackColor(col, color);
			rowatt.setCellForeColor(col, color);
			break;
		case ColoredCell.FORECOLOR:
			rowatt.setCellForeColor(col, color);
			break;
		case ColoredCell.BACKCOLOR:
			rowatt.setCellBackColor(col, color);
			break;

		default:
			break;
		}
	}

	/**
	 * ���õ�Ԫ���Ƿ�ɱ༭
	 */
	public void setCellEditable(int row, String key, boolean edit) {
		// row = convertIntoModelRow(row);
		// if (htCellEdit == null)
		// htCellEdit = new Hashtable();
		// String strKey = row + "," + key;
		// htCellEdit.put(strKey, new Boolean(edit));
		getRowAttribute(row).addCellEdit(key, edit);
	}

	/**
	 * ��ʼ�����Ԫ��. ��������:(01-2-21 10:08:48)
	 */
	public void setDataVector(Vector newData) {
		if (newData == null)
			throw new IllegalArgumentException("setDataVector() - Null parameter");

		// �������
		dataVector = new Vector(0);

		// �������
		dataVector = newData;

		// clear sortindex
		setSortIndex(null);

		int size = newData.size();
		// �к�
		getRowNOTableModel().setNumRows(size);

		vRowAttrib = new Vector<RowAttribute>(size);
		for (int i = 0; i < size; i++) {
			RowAttribute ra = createRowAttribute();
			ra.setRowState(NORMAL);
			vRowAttrib.addElement(ra);
		}

		newRowsAdded(new TableModelEvent(this, 0, getRowCount() - 1, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	}

	/**
	 * ���ñ��༭��. ��������:(2001-9-7 10:57:25)
	 * 
	 * @param newRowEdit
	 *            boolean
	 */
	public void setEditRow(int newRow) {
		if (newRow < 0 || newRow > getRowCount() - 1) {
			m_editRow = null;
		} else {
			m_editRow = (Vector) dataVector.elementAt(newRow);
		}
	}

	/**
	 * ��������:(2002-07-02 16:05:36)
	 * 
	 * @param isEnabled
	 *            boolean
	 */
	public void setEnabled(boolean isEnabled) {
		m_bEnabled = isEnabled;
	}

	/**
	 * ��������:(2002-07-02 16:05:36)
	 * 
	 * @param isEnabled
	 *            boolean
	 */
	public boolean isEnabled() {
		return m_bEnabled;
	}

	/**
	 * ��������:(2002-07-02 16:05:36)
	 * 
	 * @param isEnabled
	 *            boolean
	 */
	public void setEnabledAllItems(boolean isEnabled) {
		BillItem[] items = getBodyItems();
		if (items != null) {
			for (int i = 0; i < items.length; i++)
				items[i].setEnabled(isEnabled);
		}
	}

	void setEnabledByEditFlag(boolean isEnabled) {
		BillItem[] items = getBodyItems();
		if (items != null) {
			if (isEnabled) {
				for (int i = 0; i < items.length; i++)
					items[i].setEnabled(items[i].isEdit());
			} else
				for (int i = 0; i < items.length; i++)
					items[i].setEnabled(isEnabled);
		}
	}

	/**
	 * ��������:(2001-6-20 19:44:39)
	 * 
	 * @param col
	 *            int
	 */
	public void setFixCol(int col) {
		fixCol = col;
	}

	/**
	 * ��ʽִ����. ��������:(01-2-21 10:08:48)
	 */
	public void setFormulaParse(FormulaParse newFormulaParse) {
		m_formulaParse = newFormulaParse;
	}

	/**
	 * ��������:(2003-01-24 9:29:30)
	 * 
	 * @param doCalculate
	 *            boolean
	 */
	public void setNeedCalculate(boolean doCalculate) {
		if (m_bNeedCalculate != doCalculate) {
			m_bNeedCalculate = doCalculate;
			if (m_bNeedCalculate)
				reCalcurateAll();
		}
	}

	/**
	 * ��������:(2003-4-9 10:58:47)
	 * 
	 * @param rows
	 *            int[]
	 */
	public void setNotEditAllowedRows(int[] rows) {
		// if (htNotEditRow == null) {
		// htNotEditRow = new Hashtable();
		// } else
		// htNotEditRow.clear();
		if (rows == null || rows.length == 0)
			return;
		if (aryNotEditRow == null)
			aryNotEditRow = new ArrayList<RowAttribute>();
		else {
			for (int i = 0; i < aryNotEditRow.size(); i++) {
				aryNotEditRow.get(i).setEdit(true);
			}
			aryNotEditRow.clear();
		}
		for (int i = 0; i < rows.length; i++) {
			if (rows[i] < getRowCount()) {
				getRowAttribute(rows[i]).setEdit(false);
				aryNotEditRow.add(getRowAttribute(rows[i]));
				// Object o = dataVector.elementAt(rows[i]);
				// if (o != null) {
				// Object key = "" + System.identityHashCode(o);
				// htNotEditRow.put(key, "");
				// }
			}
		}
	}

	/**
	 * ���ñ��Ϊ�б༭״̬. ��������:(2001-9-7 10:57:25)
	 * 
	 * @param newRowEdit
	 *            boolean
	 */
	public void setRowEditState(boolean newRowEditState) {
		m_bRowEditState = newRowEditState;
	}

	/**
	 * ���ñ������. ��������:(2001-9-7 10:57:25)
	 * 
	 * @deprecated
	 * @param newRowEdit
	 *            boolean
	 */
	public void setRowSort(boolean newRowSort) {
	}

	/**
	 * ������״̬. ��������:(2001-4-16 9:45:23)
	 * 
	 * @param row
	 *            int : view row
	 * @return int
	 */
	public void setRowState(int row, int state) {
		if (row == -1)
			return;

		// remove by river for 2012-11-14
//		if (isRowSelectMode() && !(state == SELECTED || state == UNSTATE))
//			throw new RuntimeException("ѡ��ģʽֻ������SELECTED��UNSTATE");

		RowAttribute ra = getRowAttribute(row);

		int oldstats = ra.getRowState();

		if (oldstats != state) {
			ra.setRowState(state);
			if (rowStateChangeListener != null) {
				RowStateChangeEvent event = new RowStateChangeEvent(this, row, oldstats, state);
				rowStateChangeListener.valueChanged(event);
			}
			if (isRowSelectMode())
				reCalcurateAll();
		}

	}

	/**
	 * ��ס���������,�Ա�ɾ����ѡ����к󰴴�����������
	 * 
	 * @param newSortColumn
	 *            int
	 * @throws
	 * @see
	 * @since v1.0
	 * @deprecated
	 */
	protected void setSortColumn(int newSortColumn) {
		if (newSortColumn > -1) {
			SortItem si = new SortItem(newSortColumn);

			List<SortItem> lsi = new ArrayList<SortItem>();

			lsi.add(si);

			setSortColumns(lsi);
		} else {
			setSortColumns(null);
		}
	}

	/**
	 * ��ס���������,�Ա�ɾ����ѡ����к󰴴�����������
	 * 
	 * @param newSortColumn
	 *            int
	 * @throws
	 * @see
	 * @since v1.0
	 */
	protected void setSortColumns(List<SortItem> newSortColumns) {
		m_sortColumn = newSortColumns;
	}

	/**
	 * ��ס���������,�Ա�ɾ����ѡ����к󰴴�����������
	 * 
	 * @param newSortColumn
	 *            int
	 * @throws
	 * @see
	 * @since v1.0
	 */
	public void setSortColumn(String key) {
		setSortColumn(getBodyColByKey(key));
	}

	/**
	 * ���õ�ǰ�����������е��к���������
	 * 
	 * @return int
	 * @throws
	 * @see
	 * @since v1.0
	 */
	public void setSortIndex(int[] indexes) {
		m_indexes = indexes;
	}

	/**
	 * @param sortPrepareListener
	 *            The sortPrepareListener to set.
	 */
	public void setSortPrepareListener(IBillModelSortPrepareListener sortPrepareListener) {
		this.sortPrepareListener = sortPrepareListener;
	}

	/**
	 * �ϼ��б�ģʽ. ��������:(01-2-21 10:08:48)
	 */
	public void setTotalTableModel(DefaultTableModel newTotal) {
		m_tmlTotal = newTotal;
	}

	/**
	 * ���õ�Ԫֵ.
	 */
	public void setValueAt(Object aValue, int row, int column) {
		setValueAtModel(aValue, row, column);
	}

	public void setValueAt(Object aValue, int row, String strKey) {

		int col = getBodyColByKey(strKey);

		if (col >= 0) {

			BillItem item = getBodyItems()[col];

			if (aValue != null && item.getDataType() == IBillItem.UFREF && !(aValue instanceof IConstEnum)) {

				Object o = getValueAt(row, col);

				if (o != null) {
					if (strKey.endsWith(IBillItem.ID_SUFFIX)) {
						aValue = new DefaultConstEnum(aValue, ((IConstEnum) o).getName());
					} else {
						aValue = new DefaultConstEnum(((IConstEnum) o).getValue(), aValue.toString());
					}
				} else {
					if (strKey.endsWith(IBillItem.ID_SUFFIX)) {
						aValue = new DefaultConstEnum(aValue, (String) aValue);
					} else {
						aValue = new DefaultConstEnum(aValue, aValue.toString());
					}
				}
			}

			setValueAt(aValue, row, col);
			
			setItemRelationValueAt(aValue, row, item);
		}
	}

	/**
	 * ���õ�Ԫֵ.
	 */
	@SuppressWarnings("unchecked")
	void setValueAtModel(Object aValue, int row, int column) {
		if (row < 0 || column < 0)
			return;
		BillItem item = getBodyItems()[column];
		Vector rowVector = (Vector) dataVector.elementAt(row);
		int datatype = item.getDataType();
		// String strValue;

		// �Ƚ���Ϊ�յ��ж�
		if (aValue != null) {

			// �޸�С������
			if (!isFirstSetBodyDataVO && !isIgnoreScaleWhenSetValue() && item.getDataType() == IBillItem.DECIMAL) {
				// -----Ϊ���þ������ӵĴ��룬����С�����������¼���------------------
				setBillItemDecimalByRow(item, row);
				// aValue = BillUtil.getFormatUFDouble((UFDouble)aValue,
				// digits);
			}

			aValue = item.getConverter().convertToBillItem(datatype, aValue);

			// switch (datatype) {
			// case IBillItem.INTEGER:
			// // С��ʱ��ʽ��
			// if (!(aValue instanceof Integer)) {
			// strValue = aValue.toString().trim();
			// if (strValue.length() > 0) {
			// int pos = strValue.indexOf('.');
			// if (pos >= 0)
			// strValue = strValue.substring(0, pos);
			// if (strValue.length() == 0)
			// aValue = null;
			// else
			// aValue = new Integer(strValue);
			// } else
			// aValue = null;
			// }
			// break;
			// case IBillItem.DECIMAL:
			//                
			// if (aValue.getClass() != UFDouble.class) {
			// strValue = aValue.toString().trim();
			// aValue = (strValue.length() > 0) ? new UFDouble(strValue): null;
			// }
			// if (aValue != null) {
			// if (!isIgnoreScaleWhenSetValue()){
			// int digits = item.getDecimalDigits();
			// //-----Ϊ���þ������ӵĴ��룬����С�����������¼���------------------
			// if (!isFirstSetBodyDataVO && item.getDecimalListener() != null){
			// int colIndex =
			// getBodyColByKey(item.getDecimalListener().getSource());
			// Object id = getValueAt(row, colIndex);
			// if(id != null && id instanceof IConstEnum)
			// id = ((IConstEnum)id).getValue();
			// //item.setDecimalDigits(item.getDecimalListener().getDecimalFromSource(row,
			// id));
			// digits = item.getDecimalListener().getDecimalFromSource(row, id);
			// }
			// aValue = ((UFDouble) aValue).setScale(0 - digits,
			// UFDouble.ROUND_HALF_UP);
			// }
			// }
			// break;
			// case IBillItem.BOOLEAN:
			// if (aValue instanceof String) {
			// aValue = UFBoolean.valueOf(aValue.toString());
			// }
			// break;
			// case IBillItem.COMBO:
			// if(!(aValue instanceof IConstEnum)){
			// // �߼�ʱ��ʽ��
			// UIComboBox combo = ((UIComboBox) item.getComponent());
			//	                
			// int index = combo.getItemIndexByObject(aValue);
			// if (index < 0 && item.isWithIndex()) {
			// if ("".equals(aValue))
			// index = 0;
			// else
			// index = Integer.parseInt(aValue.toString());
			// }
			//	                
			// if (index >= 0)
			// aValue = combo.getItemAt(index);
			// }
			// break;
			// default:
			// if(item.getRelationItem() != null && item.getMetaDataProperty()
			// == null){
			// setItemRelationValueAt(aValue, row, item);
			// }
			// break;
			// }
		}

		rowVector.setElementAt(aValue, column);

		// ����ϼ�
		if (isNeedCalculate()) {
			if (item.isTotal() && (datatype == BillItem.INTEGER || datatype == BillItem.DECIMAL))
				reCalcurate(column);
		}
		if (isChangeTable())
			fireTableChanged(new TableModelEvent(this, row, row, column));
	}

    void setValueAtModel(Object aValue, int row, String strKey) {
		int col = getBodyColByKey(strKey);
		setValueAtModel(aValue, row, col);
	}

	/**
	 * �����ƶ���������
	 * 
	 * @createDate:(2000-12-26 20:16:55)
	 * @see:
	 * @since:v5.0
	 * @param column
	 *            int : ��ģ�͵�˳�򣬲��Ǳ���ʾ��˳��
	 * @param ascending
	 *            boolean �����ǽ���
	 * @param noldrow
	 *            int[] : ��ά����������
	 * @deprecated
	 */
	public void sortByColumn(int column, boolean ascending, int[] noldrow) {
		SortItem sortitem = new SortItem(column, ascending);

		List<SortItem> lsi = new ArrayList<SortItem>();

		lsi.add(sortitem);

		sortByColumns(lsi, noldrow);

	}

	/**
	 * �����ƶ���������
	 * 
	 * @createDate:(2000-12-26 20:16:55)
	 * @see:
	 * @since:v5.0
	 * @param items
	 *            SortItem : �����ж���
	 */
	public void sortByColumns(List<SortItem> items) {
		sortByColumns(items, new int[0]);
	}

	/**
	 * �����ƶ���������
	 * 
	 * @createDate:(2000-12-26 20:16:55)
	 * @see:
	 * @since:v5.0
	 * @param items
	 *            SortItem : �����ж���
	 * @param noldrow
	 *            int[] : ��ά����������
	 */
	public void sortByColumns(List<SortItem> items, int[] noldrow) {

		setSortColumns(items);

		// ��ȡ�Ƚ�����
		Comparator cp = new MutiColumnComparator(items);

		SortUtils.sort(dataVector, getSortObjectList(), getSortObjectArray(), noldrow, cp);

		getRowNOTableModel().fireTableDataChanged();

		fireTableDataChanged();

		// ����������¼�
		if (bsl != null) {
			String itemkey = getSortColumnKey();
			bsl.afterSort(itemkey);
		}

		EventListener[] ls = afterSortListener.getListeners(BillSortListener2.class);

		int currentrow = -1;
		if (getBillScrollPane() != null && noldrow != null && noldrow.length > 0)
			currentrow = noldrow[0];

		for (int i = 0; i < ls.length; i++) {
			((BillSortListener2) ls[i]).currentRowChange(currentrow);
		}
		// reset
		// this.sorttype = DefaultSortType;

	}

	private Comparator getItemComparator(SortItem sortitem) {
		Comparator cp = null;

		BillItem item = getBodyItems()[sortitem.getColumn()];

		if (item.getItemComparator() == null) {
			int sorttype = DefaultSortType;
			IBillModelSortPrepareListener sl = getSortPrepareListener();
			String itemkey = item.getKey();
			if (sl != null) {
				sorttype = sl.getSortTypeByBillItemKey(itemkey);
			} else {
				switch (getItemByKey(itemkey).getDataType()) {
				case BillItem.BOOLEAN:
					sorttype = BillItem.BOOLEAN;
					break;
				case BillItem.STRING:
					sorttype = BillItem.STRING;
					break;
				default:
					break;
				}
			}
			cp = new ColumnComparator(sorttype, sortitem);
		} else {
			cp = new ColumnComparator(item.getItemComparator(), sortitem);
		}

		return cp;

	}

	/**
	 * ������������
	 * 
	 * @createDate:(2000-12-26 20:16:55)
	 * @see:
	 * @since:v1.0
	 * @param column
	 *            int : ��ģ�͵�˳�򣬲��Ǳ���ʾ��˳��
	 * @param ascending
	 *            boolean �����ǽ���
	 */
	public void sortByColumn(int column, boolean ascending) {
		sortByColumn(column, ascending, new int[0]);
	}

	/**
	 * ������������
	 * 
	 * @createDate:(2000-12-26 20:16:55)
	 * @see:
	 * @since:v1.0
	 * @param column
	 *            int �е�����
	 * @param ascending
	 *            boolean �����ǽ���
	 */
	public void sortByColumn(String key, boolean ascending) {
		sortByColumn(getBodyColByKey(key), ascending);
	}

	/**
	 * ���ƶ�ά����
	 */
	private Vector transferBodyData(Vector vValue) {
		if (vValue == null) {
			return new Vector();
		}
		int size = vValue.size();
		Vector<Vector> vData = new Vector<Vector>(size);
		Vector vRow;
		Vector vNewRow;
		for (int i = 0; i < size; i++) {
			vRow = (Vector) vValue.elementAt(i);
			vNewRow = transferBodyRowData(vRow);
			vData.addElement(vNewRow);
		}
		return vData;
	}

	/**
	 * ����һά����
	 */
	@SuppressWarnings("unchecked")
	private Vector<Object> transferBodyRowData(Vector v) {
		return v == null ? new Vector<Object>() : new Vector<Object>(v);
	}

	/**
	 * ���±�������.
	 */
	public void updateValue() {
		vViewBodyCache = transferBodyData(dataVector);
		// htCellEditCopy = htCellEdit;
		int size = dataVector.size();
		// ��״̬�޸�Ϊ����
		// vRowState = new Vector(size);
		// String normalState = NORMAL + "";
		for (int i = 0; i < size; i++) {
			RowAttribute ra = getRowAttribute(i);
			ra.setRowState(NORMAL);
			ra.updataCellEdit();
		}
		vDeleteRow = null;
	}

	/**
	 * �õ���Ԫֵ.
	 */
	private Vector getRowVectorAtModel(int row) {
		if (row < 0)
			return null;
		if (dataVector == null || dataVector.size() <= row)
			return null;
		Vector rowVector = (Vector) dataVector.elementAt(row);
		return rowVector;
	}

	/**
	 * @return Returns the changeTable.
	 */
	private boolean isChangeTable() {
		return changeTable;
	}

	/**
	 * @param changeTable
	 *            The changeTable to set.
	 */
	private void setChangeTable(boolean changeTable) {
		this.changeTable = changeTable;
		// if(changeTable)
		// fireTableChanged(new TableModelEvent(this));
	}

	/**
	 * @return Returns the m_userFixRowModel.
	 */
	public UserFixRowTableModel getUserFixRowModel() {
		if (m_userFixRowModel == null) {
			m_userFixRowModel = new UserFixRowTableModel(this);
		}
		return m_userFixRowModel;
	}

	void setUserFixRowModel(UserFixRowTableModel m) {
		this.m_userFixRowModel = m;
		if (m != null)
			m.setParentModel(this);
	}

	FixRowNOModel getFixRowHeaderModel() {
		if (fixRowHeaderModel == null)
			fixRowHeaderModel = new FixRowNOModel();
		return fixRowHeaderModel;
	}

	UserRowNOModel getUserRowHeaderModel() {
		if (userRowHeaderModel == null)
			userRowHeaderModel = new UserRowNOModel();
		return userRowHeaderModel;
	}

	public BillModelCellEditableController getCellEditableController() {
		return cellEditableController;
	}

	public void setCellEditableController(BillModelCellEditableController cellEditableController) {
		this.cellEditableController = cellEditableController;
	}

	private boolean isIgnoreScaleWhenSetValue() {
		return ignoreScaleWhenSetValue;
	}

	public void addSortRelaObjectListener(IBillRelaSortListener l) {
		sortRelaObjectListener.add(IBillRelaSortListener.class, l);
	}

	public void addSortRelaObjectListener2(IBillRelaSortListener2 l) {
		sortRelaObjectListener.add(IBillRelaSortListener2.class, l);
	}

	public void addBillSortListener2(BillSortListener2 l) {
		afterSortListener.add(BillSortListener2.class, l);
	}

	public void removeSortRelaObjectListener(IBillRelaSortListener l) {
		sortRelaObjectListener.remove(IBillRelaSortListener.class, l);
	}

	public void removeSortRelaObjectListener2(IBillRelaSortListener2 l) {
		sortRelaObjectListener.remove(IBillRelaSortListener2.class, l);
	}

	public void removeBillSortListener2(BillSortListener2 l) {
		afterSortListener.remove(BillSortListener2.class, l);
	}

	private List[] getSortObjectList() {
		List[] ret = null;

		ArrayList<List> sortObject = new ArrayList<List>();
		sortObject.add(vRowAttrib);

		EventListener[] ls = sortRelaObjectListener.getListeners(IBillRelaSortListener.class);
		for (int i = 0; i < ls.length; i++) {
			List o = (List) ((IBillRelaSortListener) ls[i]).getRelaSortObject();
			if (o != null)
				sortObject.add(o);
		}

		ret = new List[sortObject.size()];
		sortObject.toArray(ret);

		return ret;
	}

	private Object[][] getSortObjectArray() {
		Object[][] ret = null;

		ArrayList<Object[]> sortObject = new ArrayList<Object[]>();

		EventListener[] ls = sortRelaObjectListener.getListeners(IBillRelaSortListener2.class);
		for (int i = 0; i < ls.length; i++) {
			Object[] o = (Object[]) ((IBillRelaSortListener2) ls[i]).getRelaSortObjectArray();
			if (o != null)
				sortObject.add(o);
		}

		ret = new Object[sortObject.size()][];

		for (int i = 0; i < sortObject.size(); i++) {
			ret[i] = sortObject.get(i);
		}

		return ret;
	}

	/**
	 * �÷���ֻ���ڽ������ģ���һ����������
	 * 
	 * @deprecated
	 */
	public void setIgnoreScaleWhenSetValue(boolean ignoreScaleWhenSetValue) {
		this.ignoreScaleWhenSetValue = ignoreScaleWhenSetValue;
	}

	/**
	 * ���ģ��������
	 * 
	 * @deprecated
	 */
	public boolean isSortAscending() {
		if (getSortColumns() != null && getSortColumns().size() > 0)
			return getSortColumns().get(0).isAscending();

		return false;
	}

	protected boolean isRowSelectMode() {
		return rowSelectMode;
	}

	protected void setRowSelectMode(boolean rowSelectMode) {
		this.rowSelectMode = rowSelectMode;
	}

	public void addRowStateChangeEventListener(IBillModelRowStateChangeEventListener l) {
		this.rowStateChangeListener = l;
	}

	public void removeRowStateChangeEventListener() {
		this.rowStateChangeListener = null;
	}

	public IBillModelRowStateChangeEventListener getRowStateChangeEventListener() {
		return this.rowStateChangeListener;
	}

	public Color getBackground(int row, int column) {
		return getRowAttribute(row).getCellBackColor(column);
	}

	public Color getForeground(int row, int column) {
		return getRowAttribute(row).getCellForeColor(column);
	}

	public void setBackground(Color color, int[] rows, int[] columns) {
		for (int i = 0; i < rows.length; i++) {
			getRowAttribute(rows[i]).clearCellBackColor();
			for (int j = 0; j < columns.length; j++) {
				getRowAttribute(rows[i]).setCellBackColor(columns[j], color);
			}
		}
	}

	public void setBackground(Color color, int row, int column) {
		getRowAttribute(row).setCellBackColor(column, color);
	}

	public void setForeground(Color color, int[] rows, int[] columns) {
		for (int i = 0; i < rows.length; i++) {
			getRowAttribute(rows[i]).clearCellForeColor();
			for (int j = 0; j < columns.length; j++) {
				getRowAttribute(rows[i]).setCellForeColor(columns[j], color);
			}
		}
	}

	public void setForeground(Color color, int row, int column) {
		getRowAttribute(row).setCellForeColor(column, color);
	}

	public Font getFont(int row, int column) {
		return getRowAttribute(row).getCellFont(column);
	}

	public void setFont(Font font, int[] rows, int[] columns) {
		for (int i = 0; i < rows.length; i++) {
			getRowAttribute(rows[i]).clearCellFont();
			for (int j = 0; j < columns.length; j++) {
				getRowAttribute(rows[i]).setCellFont(columns[j], font);
			}
		}
	}

	public void setFont(Font font, int row, int column) {
		getRowAttribute(row).setCellFont(column, font);

	}

	public IBillModeCellSpan getCellSpan() {
		return cellSpan;
	}
	
	public void addDefaultCellSpan() {
		cellSpan = new BillModelCellSpan(this);
	}
	
}