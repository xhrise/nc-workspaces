package com.ufsoft.table.re.timeref;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.plaf.UIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

public class CalendarTable extends JTable {

	/** ��ǰѡ�е����� */
	private Calendar selectCalendar = null;
	/** ������ɫ */
	private final Color GRID_COLOR = new Color(204, 204, 255);
	/** ��ͷ��ɫ 230 */
	private final Color HEADER_COLOR = new Color(222, 235, 239);
	/** �и� */
	private final int ROW_HEIGHT = 28;
	/** �п� */
	private final int COLUMN_WIDTN = 28;
	/** ����ڱ�����ƶ���ʱ���� */
	private int iRow = -1;
	/** ����ڱ�����ƶ���ʱ���� */
	private int iColumn = -1;

	
	public CalendarTable() {
		setModel(new CalendarDataModel());
		tableStyle();
	}

	private void tableStyle() {
		setRowSelectionAllowed(false);//�Ƿ����ѡ���ģ���е���
		setCellSelectionEnabled(false);//�˱��Ƿ�����ͬʱ������ѡ�����ѡ��
		setColumnSelectionAllowed(false);//�Ƿ����ѡ���ģ���е���
		setIntercellSpacing(new Dimension(1, 1));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setGridColor(GRID_COLOR);
		setBorder(new LineBorder(new Color(204, 204, 255)));
		setRowHeight(ROW_HEIGHT);
		getTableHeader().setBackground(HEADER_COLOR);
		getTableHeader().setDefaultRenderer(createDefaultRenderer());
		CalendarCellRenderer cellRender = new CalendarCellRenderer();
		setDefaultRenderer(CalendarCellRenderer.class, cellRender);
		getTableHeader().setReorderingAllowed(false);
		addMouseMotionListener(createCellMouseListener());
		registerKeyboardAction(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
//				getParent().requestFocusInWindow();
				nextFocus();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		registerKeyboardAction(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
//				getParent().requestFocusInWindow();
				nextFocus();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_TAB,InputEvent.SHIFT_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	/**
	 * ����table������ģ��
	 * 
	 * @param Date
	 *            date
	 * @return
	 */
	protected void updateDataModel(Calendar calendar) {	
		if(calendar==null)
			return ;
		
		this.selectCalendar = calendar;
		GregorianCalendar cPrevMonthLastDay = new GregorianCalendar(this.selectCalendar.get(Calendar.YEAR),this.selectCalendar.get(Calendar.MONTH),1);
		cPrevMonthLastDay.add(Calendar.DAY_OF_MONTH, -1);
		int iDayOfWeek_prevMonth = cPrevMonthLastDay.get(Calendar.DAY_OF_WEEK);
		int iEndDay_prevMonth = CalendarUtils.getMonthDays(cPrevMonthLastDay.get(Calendar.YEAR),cPrevMonthLastDay.get(Calendar.MONTH) + 1);		
		if (iDayOfWeek_prevMonth == 7)
			iDayOfWeek_prevMonth = 0;

		int iDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int iDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int iEndDay = CalendarUtils.getMonthDays(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1);
		


		// �������ǰ��������table����
		int iCurentRow = (iDayOfWeek_prevMonth + iDayOfMonth) / 7
				+ (((iDayOfWeek_prevMonth + iDayOfMonth) % 7) > 0 ? 1 : 0);
		// ���������table������
		int rowCount = (iDayOfWeek_prevMonth + iEndDay) / 7
				+ (((iDayOfWeek_prevMonth + iEndDay) % 7) > 0 ? 1 : 0);
		CalendarDataModel model = (CalendarDataModel) this.getModel();
		if(model == null){
			return;
		}
		// ����ģ�͵�����
		model.setData(new String[rowCount][7]);
		int iRowCount = model.getRowCount();
		int iColumnCount = model.getColumnCount();
		// ���õ�ǰ����
		model.setValueAt(iDayOfMonth + "", iCurentRow - 1, iDayOfWeek - 1);
		this.addRowSelectionInterval(iCurentRow - 1, iCurentRow - 1);
		this.addColumnSelectionInterval(iDayOfWeek - 1, iDayOfWeek - 1);
		this.iRow=iCurentRow - 1;
		this.iColumn=iDayOfWeek - 1;
		// ��ǰ����֮ǰ
		int iBack = iDayOfMonth;
		boolean bStop = false;
		boolean bPre = false;
		// ���õ�ǰ����֮ǰ��ֵ
		for (int i = iCurentRow - 1; i >= 0; i--) {
//			if (bStop)
//				break;
		    
			for (int j = iColumnCount -1; j >= 0; j--) {
				if (i == (iCurentRow - 1) && j >= (iDayOfWeek - 1))
					continue;
				else {
//					if (iBack == 1) {
//						bStop = true;
//						break;
//					}
					if(iBack == 1){						
						iBack = iEndDay_prevMonth + 1;
						bPre = true;
					}
					model.setValueAt((--iBack) + "", i, j);
					model.setFlag(bPre, i, j);
				}
			}
		}
		// ��ǰ����֮��
		int iForward = iDayOfMonth;
		boolean breakOut = false;
		boolean bNext = false;
		// ���õ�ǰ����֮���ֵ
		for (int i = iCurentRow - 1; i < iRowCount; i++) {
//			if (breakOut)
//				break;
			for (int j = 0; j <iColumnCount; j++) {
				if (i == (iCurentRow - 1) && j <= (iDayOfWeek - 1))
					continue;
				else {
					if (iForward == iEndDay) {
						iForward = 0;
						bNext = true;
//						breakOut = true;
//						break;
					}
					model.setValueAt((++iForward) + "", i, j);
					model.setFlag(bNext, i, j);
				}
			}
		}
		this.repaint();
	}

	/**
	 * �������ͷ���Ļ�����
	 * 
	 * @param Date
	 *            date
	 * @return
	 */
	private TableCellRenderer createDefaultRenderer() {
		DefaultTableCellRenderer label = new TableHeaderCellRenderer();
		label.setHorizontalAlignment(JLabel.CENTER);
		return label;
	}

	private class TableHeaderCellRenderer extends DefaultTableCellRenderer
			implements UIResource {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (table != null) {
				JTableHeader header = table.getTableHeader();
				if (header != null) {
					setForeground(header.getForeground());
					setBackground(header.getBackground());
					setFont(header.getFont());
				}
			}
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}

	private class CalendarCellRenderer extends DefaultTableCellRenderer {

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (table == null) {
				return null;
			}
			Component cell = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
			if (isSelected) {
				cell.setBackground(Color.ORANGE);
			}
			Object cellValue = table.getValueAt(row, column);
			String strSelectValue = null;
			if (cellValue != null) {
				strSelectValue = cellValue.toString();
			}
			if (strSelectValue == null || strSelectValue.length() == 0) {
				cell.setBackground(HEADER_COLOR);
			} else {
				int curentDate = 0;
				try {
					curentDate = Integer.parseInt(strSelectValue);
				} catch (NumberFormatException e) {
					AppDebug.debug(e);
				}
				Calendar calendar = Calendar.getInstance();
				if (curentDate == calendar.get(Calendar.DAY_OF_MONTH)
						&& selectCalendar.get(Calendar.MONTH) == calendar
								.get(Calendar.MONTH)
						&& selectCalendar.get(Calendar.YEAR) == calendar
								.get(Calendar.YEAR)) {
					cell.setBackground(Color.ORANGE);// ���õ�ǰ���ڵı���ɫ
				} else {
					cell.setBackground(Color.WHITE);
				}
			}
			CalendarDataModel model = (CalendarDataModel) table.getModel();
			if(model == null){
				return null;
			}
			Object flag = model.getFlag(row, column);
			if(flag != null && Boolean.parseBoolean(flag.toString())){
				cell.setBackground(new Color(230,230,230));
			}
			
			table.getColumnModel().getColumn(column).setResizable(false);
			setHorizontalAlignment(SwingConstants.CENTER);// ���þ���
			table.getColumnModel().getColumn(column).setPreferredWidth(
					COLUMN_WIDTN);
			return cell;
		}

	}

	/**
	 * ����ϵ�����ƶ�����
	 * 
	 * @param
	 * 
	 * @return MouseMotionListener
	 */
	private MouseMotionListener createCellMouseListener() {
		return new MouseMotionAdapter() {

			public void mouseMoved(MouseEvent e) {
				Point p = e.getPoint();
				iRow = rowAtPoint(p);
				iColumn = columnAtPoint(p);
				repaint();
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			}

		};
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row,
			int column) {
		Object value = getValueAt(row, column);
		Component cell = super.prepareRenderer(renderer, row, column);
		if (cell != null && iRow == row && iColumn == column && value != null) {
			((JComponent) cell).setBorder(BorderFactory
					.createLineBorder(Color.ORANGE));
		}
		return cell;
	}

	public class CalendarDataModel extends AbstractTableModel {

		 private final String[] aryColumnNames = {
		 StringResource.getStringResource("miufo00040"),//��
		 StringResource.getStringResource("miufo00041"),//һ
		 StringResource.getStringResource("miufo00042"),//��
		 StringResource.getStringResource("miufo00043"),//��
		 StringResource.getStringResource("miufo00044"),//��
		 StringResource.getStringResource("miufo00045"),//��
		 StringResource.getStringResource("miufo00046") };//��

		private String[][] data = null;
		private String[][] flag = null;

		CalendarDataModel() {

		}

		public void setData(String[][] data) {
			if (data == null || data.length == 0 || data[0] == null
					|| data[0].length == 0) {
				throw new IllegalArgumentException(StringResource.getStringResource("miufo1000496"));//�������������Ϊ��
			}
			this.data = data;
			flag = new String[data.length][data[0].length];
		}

		public String getColumnName(int column) {
			if (column < 0 || column > getColumnCount()) {
				throw new IllegalArgumentException(StringResource.getStringResource("mhbmeet00002"));//�����벻С��0��ֵ
			}

			return aryColumnNames[column];
		}

		public int getColumnCount() {
			return aryColumnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public Object getValueAt(int row, int column) {
			if (row < 0 || column < 0 || row > getRowCount()
					|| column > getColumnCount()) {
				throw new IllegalArgumentException(StringResource.getStringResource("mhbmeet00002"));//�����벻С��0��ֵ
			}

			return data[row][column];
		}

		public void setValueAt(Object value, int row, int column) {
			if (row < 0 || column < 0 || row > getRowCount()
					|| column > getColumnCount()) {
				throw new IllegalArgumentException(StringResource.getStringResource("mhbmeet00002"));//�����벻С��0��ֵ
			}
			data[row][column] = (String) value;
		}

		public void setFlag(Object value, int row, int column){
			if (row < 0 || column < 0 || row > getRowCount()
					|| column > getColumnCount()) {
				throw new IllegalArgumentException(StringResource.getStringResource("mhbmeet00002"));//�����벻С��0��ֵ
			}
			flag[row][column] = value.toString();
		}
		
		public Object getFlag(int row,int column){
			if (row < 0 || column < 0 || row > getRowCount()
					|| column > getColumnCount()) {
				throw new IllegalArgumentException(StringResource.getStringResource("mhbmeet00002"));//�����벻С��0��ֵ
			}
			return flag[row][column];
		}
		
		/**
		 * ��ͷ���Ļ�����
		 * 
		 * @param int
		 *            columnIndex
		 * @return Class<?>
		 */
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex < 0) {
				throw new IllegalArgumentException(StringResource.getStringResource("mhbmeet00002"));//�����벻С��0��ֵ
			}
			return CalendarCellRenderer.class;
		}
	}

	public Calendar getSelectCalendar() {
		return selectCalendar;
	}
	
}
