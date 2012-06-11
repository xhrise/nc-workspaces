package com.ufsoft.table.re.timeref;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIScrollPane;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UserListComponentPolicy;
import com.ufsoft.table.re.RefTextField;
import com.ufsoft.table.re.TimeRefComp;
import com.ufsoft.table.re.timeref.CalendarTable.CalendarDataModel;

public class CalendarDialog extends JDialog {
	private static final long serialVersionUID = -8142886208008816675L;
	
	/** 日历对象 */
	/** 上月按钮 */
	private JButton previousMonthButton = null;
	/** 下月按钮 */
	private JButton nextMonthButton = null;
	/** 关闭按钮 */
	private JButton closeButton = null;
	/** 选择年的下拉列表 */
	private JComboBox yearCombox = null;
	/** 选择月的下拉列表 */
	private JComboBox monthCombox = null;
	/** 显示当前日期的label */
	private JLabel showDateLabel = null;
	
	private TimeRefComp timeRefComp = null;
	/** 日历的网格 */
	private CalendarTable dataTable = null;
	/** 上部的图片路径 */
	private final String TOP_IMAGE = "reportcore/calpop_title_grad.gif";
	/** 下部的图片路径 */
	private final String BOTTOM_IMAGE = "reportcore/calpop_footer_grad.gif";
	/** 上月图片路径 */
	private final String PREVIOUS_IMAGE = "reportcore/backward.gif";
	/** 下月图片路径 */
	private final String NEXT_IMAGE = "reportcore/forward.gif";
	/** 关闭按钮图片路径 */
	private final String CLOSE_IMAGE = "reportcore/close.gif";
	/** 日历宽 */
	private final int WIDTH = 215;
	/** 默认的日期格式 */
	private String strFormat = "yyyy-MM-dd";
	/** 返回的文本 */
	private JTextField ref = null;
	/** 上部的panel */
	private CalendarTopPanel calendarTopPanel = null;
	/** 中部的panel */
	private CalendarCenterPanel calendarCenterPanel = null;
	/** 下部的panel */
	private CalendarBottomPanel calendarBottomPanel = null;
	/** 日历构造时指定的时间 */
	private Date defaultDate = null;	
	private boolean isInitOK=true;

	public CalendarDialog(JTextField ref, Date date){
		this(ref,date,null);
	}
	public CalendarDialog(JTextField ref, Date date, String strFormat) {
		if (ref == null) {
			throw new IllegalArgumentException(StringResource
					.getStringResource("miufo1000496"));// 输入参数不允许为空
		}
		if (strFormat != null && strFormat.trim().length() > 0) {
			this.strFormat = strFormat;
		}
		this.ref = ref;
		this.defaultDate = date;
		init();
	}

	private void init() {
		Container container = (Container) getContentPane();
		container.setLayout(new BorderLayout());
		container.add(getTopPanel(), BorderLayout.NORTH);
		container.add(getCenterPanel(), BorderLayout.CENTER);
		container.add(getBottomPanel(), BorderLayout.SOUTH);
		getDataTable().addMouseListener(createTableMouseListener());
		setLocationRelativeTo(this);
		setResizable(false);
		setUndecorated(true);
		setModal(true);
		setSize(new Dimension(WIDTH, getDialogHeight()));
		
		ArrayList<Component> comList = new ArrayList<Component>();
		comList.add(this.getTopPanel().getYearCombox());
		comList.add(this.getTopPanel().getMonthCombox());
		comList.add(this.getDataTable());
		comList.add(this.getBottomPanel().getCloseButton());
		container.setFocusCycleRoot(true);
		container.setFocusTraversalPolicy(new UserListComponentPolicy(comList));
		container.setFocusTraversalPolicyProvider(true);
		getDataTable().getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "close");
		getDataTable().getActionMap().put("close", new CloseAction());
		this.getTopPanel().getYearCombox().getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "close");
		this.getTopPanel().getYearCombox().getActionMap().put("close", new CloseAction());
		this.getTopPanel().getMonthCombox().getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "close");
		this.getTopPanel().getMonthCombox().getActionMap().put("close", new CloseAction());
		this.getBottomPanel().getCloseButton().addActionListener(new CloseAction());
	}

	public void initDate(Date newDate) {
		isInitOK=false;
		if (newDate != null) {
			GregorianCalendar calendar =new GregorianCalendar();
			calendar.setTime(newDate);
			getTopPanel().getMonthCombox().setSelectedIndex(calendar
					.get(Calendar.MONTH));
			getTopPanel().getYearCombox().setSelectedItem(calendar
					.get(Calendar.YEAR));
			getDataTable().clearSelection();
			getDataTable().updateDataModel(calendar);
			setDialogSize();
			getDataTable().repaint();
		}
		isInitOK=true;
	}

	/**
	 * 获得table对象
	 * 
	 * @param
	 * @return
	 */
	private CalendarTable getDataTable() {
		if (dataTable == null) {
			dataTable = new CalendarTable();
		}
		return dataTable;
	}

	/**
	 * 绘制图片
	 * 
	 * @param Graphics
	 *            g, String strImagePath,int width, int height
	 * @return
	 */
	private void paintBackGroundImage(Graphics g, String strImagePath,
			int width, int height) {
		if (g == null || strImagePath == null || strImagePath.length() == 0) {
			return;
		}
		if (width < 0 || height < 0) {
			throw new IllegalArgumentException(StringResource
					.getStringResource("mhbmeet00002"));// 请输入不小于0的值
		}
		ImageIcon imageIcon = ResConst.getImageIcon(strImagePath);
		if (imageIcon == null) {
			return;
		}
		Image image = imageIcon.getImage().getScaledInstance(width, height,
				Image.SCALE_SMOOTH);
		imageIcon.setImage(image);
		g.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
	}

	/**
	 * 获得上部的panel
	 * 
	 * @param
	 * @return JPanel
	 */
	private CalendarTopPanel getTopPanel() {
		if (calendarTopPanel == null) {
			calendarTopPanel = new CalendarTopPanel();
		}
		return calendarTopPanel;
	}

	/**
	 * 获得中部的panel
	 * 
	 * @param
	 * @return JPanel
	 */
	private CalendarCenterPanel getCenterPanel() {
		if (calendarCenterPanel == null) {
			calendarCenterPanel = new CalendarCenterPanel();
		}
		return calendarCenterPanel;
	}

	/**
	 * 获得下部部的panel
	 * 
	 * @param
	 * @return JPanel
	 */
	private CalendarBottomPanel getBottomPanel() {
		if (calendarBottomPanel == null) {
			calendarBottomPanel = new CalendarBottomPanel();
		}
		return calendarBottomPanel;
	}

	/**
	 * 创建table的鼠标监听
	 * 
	 * @param
	 * @return MouseListener
	 */
	private MouseListener createTableMouseListener() {
		return new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {

				Point p = e.getPoint();
				JTable table = getDataTable();
				int iRow = table.rowAtPoint(p);
				int iColumn = table.columnAtPoint(p);
				closeDialog(iRow,iColumn);

			}
		};
	}

	/**
	 * 根据数据的行数，从新设置dialog的大小
	 * 
	 * @param
	 * @return
	 */
	private void setDialogSize() {
		CalendarDataModel dataModel = (CalendarDataModel) getDataTable()
				.getModel();
		int iDialogHeight = calendarTopPanel.getHeight()
				+ getDataTable().getRowHeight() * dataModel.getRowCount()
				+ calendarBottomPanel.getHeight() + 23;
		if ( !(ref instanceof RefTextField)) {
			this.setSize(WIDTH, iDialogHeight);
			return;
		}
		JDialog dialog = (JDialog)((RefTextField)ref).getDialog();
		if (dialog != null) {
			dialog.setSize(new Dimension(WIDTH, iDialogHeight));			
		}	
		if(timeRefComp != null){
			timeRefComp.setPreferredSize(new Dimension(WIDTH, iDialogHeight));
		}
		this.setSize(WIDTH, iDialogHeight);
		
	}

	private int getDialogHeight() {
		CalendarDataModel dataModel = (CalendarDataModel) getDataTable()
				.getModel();
		int iDialogHeight = calendarTopPanel.getHeight()
				+ getDataTable().getRowHeight() * dataModel.getRowCount()
				+ calendarBottomPanel.getHeight() + 15;
		return iDialogHeight;
	}

	private class CalendarTopPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public CalendarTopPanel() {
			init();
			
		}

		private void init() {

			setLayout(new FlowLayout());
			setSize(new Dimension(300, 60));
			setBackground(Color.BLUE);
			add(getYearCombox());
			add(getPreviousButton());
			add(getMonthCombox());
			add(getNextMonthButton());
			yearCombox.addActionListener(createYearComboxActionlistener());
			monthCombox.addActionListener(createMonthComboxActionlistener());
			previousMonthButton
					.addActionListener(createPreviousBtnActionlistener());
			nextMonthButton.addActionListener(createNextBtnActionListener());
		}

		@Override
		protected void paintComponent(Graphics g) {

			paintBackGroundImage(g, TOP_IMAGE, this.getWidth(), this
					.getHeight());
		}

		/**
		 * 获得年的选择列表
		 * 
		 * @param
		 * @return JComboBox
		 */
		private JComboBox getYearCombox() {
			if (yearCombox == null) {
				yearCombox = new JComboBox();
				yearCombox.setPreferredSize(new Dimension(80, 20));
				Calendar c = Calendar.getInstance();
				int curYear = c.get(Calendar.YEAR);
				for (int i = curYear - 10; i < curYear + 10; i++) {
					yearCombox.addItem(i);
				}
				if (defaultDate != null) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(defaultDate);
					yearCombox.setSelectedItem(calendar.get(Calendar.YEAR));
				} else {
					yearCombox.setSelectedItem(CalendarUtils.getCurentYear());
				}

			}
			return yearCombox;
		}

		/**
		 * 获得月的选择列表
		 * 
		 * @param
		 * @return JComboBox
		 */
		private JComboBox getMonthCombox() {
			if (monthCombox == null) {
				monthCombox = new JComboBox();
				monthCombox.setPreferredSize(new Dimension(80, 20));

				for (int i = 1; i < 13; i++) {
					monthCombox.addItem(i);
				}
				if (defaultDate != null) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(defaultDate);
					monthCombox.setSelectedIndex(calendar.get(Calendar.MONTH));
				} else {
					monthCombox
							.setSelectedIndex(CalendarUtils.getCurentMonth());
				}

			}
			return monthCombox;
		}

		/**
		 * 获得上月的点击按钮
		 * 
		 * @param
		 * @return JButton
		 */
		private JButton getPreviousButton() {
			if (previousMonthButton == null) {
				previousMonthButton = new UIButton();
				previousMonthButton = new UIButton();
				previousMonthButton.setPreferredSize(new Dimension(16, 16));
				previousMonthButton.setIcon(ResConst
						.getImageIcon(PREVIOUS_IMAGE));
				previousMonthButton.setBorderPainted(false);
				previousMonthButton.setOpaque(false);
			}
			return previousMonthButton;
		}

		/**
		 * 获得下月的点击按钮
		 * 
		 * @param
		 * @return JButton
		 */
		private JButton getNextMonthButton() {
			if (nextMonthButton == null) {
				nextMonthButton = new UIButton();
				nextMonthButton.setPreferredSize(new Dimension(16, 16));
				nextMonthButton.setIcon(ResConst.getImageIcon(NEXT_IMAGE));
				nextMonthButton.setBorderPainted(false);
				nextMonthButton.setOpaque(false);
			}
			return nextMonthButton;
		}

		/**
		 * 更新数据模型
		 * 
		 * @param
		 * @return
		 */
		private void updateDataModel() {
			if (isInitOK) {
				int year = (Integer) yearCombox.getSelectedItem();
				int month = (Integer) monthCombox.getSelectedItem();
				GregorianCalendar calendar = new GregorianCalendar(year,month - 1,1);
				getDataTable().updateDataModel(calendar);
				setDialogSize();
			}
			
		}

		/**
		 * 创建选择年的combox监听器
		 * 
		 * @param
		 * @return
		 */
		private ActionListener createYearComboxActionlistener() {
			return new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					updateDataModel();
				}

			};
		}

		/**
		 * 创建选择月的combox监听器
		 * 
		 * @param
		 * @return
		 */
		private ActionListener createMonthComboxActionlistener() {
			return new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					updateDataModel();
				}

			};
		}

		/**
		 * 创建上月的button监听器
		 * 
		 * @param
		 * @return
		 */
		private ActionListener createPreviousBtnActionlistener() {
			return new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					if (e.getSource() == previousMonthButton) {
						int selectYear = (Integer) yearCombox.getSelectedItem();
						int selectMonth = (Integer) monthCombox
								.getSelectedItem();
						int curMonth = selectMonth - 1;
						if (curMonth == 0) {
							curMonth = 12;
							yearCombox.setSelectedItem(selectYear - 1);
							selectYear = selectYear - 1;
						}
						isInitOK=false;
						monthCombox.setSelectedItem(curMonth);
						Calendar calendar=null;
						if (getDataTable().getSelectCalendar() == null) {
							calendar = new GregorianCalendar(selectYear, curMonth - 1, 1);
							getDataTable().updateDataModel(calendar);
						} else {
							calendar=getDataTable().getSelectCalendar();
							calendar.add(Calendar.MONTH, -1);
							getDataTable().updateDataModel(calendar);
						}	
						setDialogSize();
						isInitOK=true;
					}
				}

			};
		}

		/**
		 * 创建下月的button监听器
		 * 
		 * @param
		 * @return
		 */
		private ActionListener createNextBtnActionListener() {
			return new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == nextMonthButton) {
						int selectYear = (Integer) yearCombox.getSelectedItem();
						int selectMonth = (Integer) monthCombox
								.getSelectedItem();
						int curMonth = selectMonth + 1;
						if (curMonth > 12) {
							curMonth = 1;
							yearCombox.setSelectedItem(selectYear + 1);
							selectYear = selectYear + 1;
						}
						isInitOK=false;
						monthCombox.setSelectedItem(curMonth);
						Calendar calendar=null;
						if(getDataTable().getSelectCalendar()==null){
							calendar= new GregorianCalendar(selectYear,selectMonth,1);
							getDataTable().updateDataModel(calendar);
						}else{
							calendar=getDataTable().getSelectCalendar();
							calendar.add(Calendar.MONTH, 1);
							getDataTable().updateDataModel(calendar);
						}								
						setDialogSize();
						isInitOK=true;
					}
				}

			};
		}

	}

	private class CalendarCenterPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public CalendarCenterPanel() {
			init();
		}

		private void init() {
			setLayout(new BorderLayout());
			JScrollPane scrollPane = new UIScrollPane(getDataTable());
			add(scrollPane, BorderLayout.CENTER);
			int selectYear = (Integer) yearCombox.getSelectedItem();
			int selectMonth = (Integer) monthCombox.getSelectedItem();
			GregorianCalendar calendar = new GregorianCalendar(selectYear,selectMonth - 1,1);
			getDataTable().updateDataModel(calendar);
		}

	}

	private class CalendarBottomPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public CalendarBottomPanel() {
			init();
		}

		private void init() {
			setLayout(new FlowLayout());
			setPreferredSize(new Dimension(300, 22));
			add(getShowDateLable());
			add(getCloseButton());
			setBorder(new LineBorder(new Color(204, 204, 255)));
		}

		@Override
		protected void paintComponent(Graphics g) {

			paintBackGroundImage(g, BOTTOM_IMAGE, this.getWidth(), this
					.getHeight());
		}

		/**
		 * 获得显示当前日期的页签
		 * 
		 * @param
		 * @return JLabel
		 */
		private JLabel getShowDateLable() {
			if (showDateLabel == null) {
				showDateLabel = new UILabel();
				Calendar calendar = Calendar.getInstance();
				showDateLabel.setPreferredSize(new Dimension(180, 16));
				showDateLabel.setText(CalendarUtils.formatDate(calendar
						.getTime(), strFormat));
				showDateLabel.addMouseMotionListener(new MouseMotionAdapter() {
					public void mouseMoved(MouseEvent e) {
						showDateLabel.setCursor(Cursor
								.getPredefinedCursor(Cursor.HAND_CURSOR));
					}
				});
				showDateLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						ref.setText(showDateLabel.getText());
						ref.grabFocus();
						if ( !(ref instanceof RefTextField)) {
							CalendarDialog.this.dispose();
							return;
						}
						JDialog dialog = (JDialog)((RefTextField)ref).getDialog();
						dialog.dispose();
						CalendarDialog.this.dispose();
						dialog = null;
					}
				});
			}
			return showDateLabel;
		}

		/**
		 * 关闭按钮
		 * 
		 * @param
		 * @return JButton
		 */
		private JButton getCloseButton() {
			if (closeButton == null) {
				closeButton = new UIButton();
				closeButton.setPreferredSize(new Dimension(16, 16));
				closeButton.setIcon(ResConst.getImageIcon(CLOSE_IMAGE));
				closeButton.setBorderPainted(false);
				closeButton.setOpaque(false);

			}
			return closeButton;
		}

	}

	/**
	 * @i18n miufo00121=日历
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("test");
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setSize(200, 300);
		JButton btn = new JButton(MultiLang.getString("miufo00121"));
		final JTextField text = new JTextField();
		text.setPreferredSize(new Dimension(100, 20));
		frame.getContentPane().add(btn);
		frame.getContentPane().add(text);
		frame.setVisible(true);
		frame.setLocation(100, 100);
		btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// CalendarDialog dlg = CalendarDialog.getInstance(text);
				// dlg.setUndecorated(true);
				// dlg.setLocation(300, 300);
				// dlg.setVisible(true);

			}

		});
	}

	public TimeRefComp getTimeRefComp() {
		return timeRefComp;
	}

	public void setTimeRefComp(TimeRefComp timeRefComp) {
		this.timeRefComp = timeRefComp;
	}
	
	private void closeDialog(int iRow,int iColumn){

		JTable table = getDataTable();
		CalendarDataModel model = (CalendarDataModel) table.getModel();
		if (model == null) {
			return;
		}
		Object flag = model.getFlag(iRow, iColumn);
		boolean isCurentMonth = flag != null
				&& Boolean.parseBoolean(flag.toString()) ? false : true;
		Object selectObject = table.getValueAt(iRow, iColumn);
		if (selectObject == null) {
			return;
		}
		String strDay = selectObject.toString();
		if (strDay == null || strDay.length() == 0) {
			return;
		}
		int iSelectDay = 0, iSelectYear = 0, iSelectMonth = 0;
		try {
			iSelectDay = Integer.parseInt(strDay);
			iSelectYear = Integer.parseInt(yearCombox.getSelectedItem()
					.toString());
			iSelectMonth = Integer.parseInt(monthCombox.getSelectedItem()
					.toString());

		} catch (NumberFormatException ex) {
			AppDebug.debug(ex);
		}
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, iSelectYear);
		if (!isCurentMonth && iRow == 0) {
			c.set(Calendar.MONTH, iSelectMonth - 2);
		} else if (!isCurentMonth && iRow != 0) {
			c.set(Calendar.MONTH, iSelectMonth);
		} else {
			c.set(Calendar.MONTH, iSelectMonth - 1);
		}
		c.set(Calendar.DAY_OF_MONTH, iSelectDay);
		ref.setText(CalendarUtils.formatDate(c.getTime(), strFormat));
		if (ref instanceof RefTextField) {
			JDialog dialog = (JDialog) ((RefTextField) ref).getDialog();
			dialog.dispose();
			this.dispose();
			dialog = null;
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					ref.requestFocus();
				}

			});

		} else {
			this.dispose();
		}
	
	}
	private class CloseAction extends AbstractAction{

		public void actionPerformed(ActionEvent e) {
			int iRow = getDataTable().getSelectedRow();
			int iColumn = getDataTable().getSelectedColumn();
			closeDialog(iRow,iColumn);
		}
	}
}
 