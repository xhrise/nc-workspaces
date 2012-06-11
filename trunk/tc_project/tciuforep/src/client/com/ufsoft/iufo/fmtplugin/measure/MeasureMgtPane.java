package com.ufsoft.iufo.fmtplugin.measure;	

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;

/**
@update
	修改显示信息
@end
 * 此处插入类型描述。
 * 创建日期：(2002-5-14 13:20:05)
 * @author：王海涛
 */
public class MeasureMgtPane extends UIPanel {
protected JTable m_table;
  	protected MeasureMgtTableModel m_data;
  	protected JPopupMenu m_popup;

  	class PopupTrigger extends MouseAdapter
	{
		public void mouseReleased(MouseEvent e)
		{

			int x = e.getX();
			int y = e.getY();


				int row = m_table.rowAtPoint(new Point(x,y));
				if(row>=0)
					m_table.setRowSelectionInterval(row,row);




			if (e.isPopupTrigger())
			{

					int X,Y;

					Dimension size = m_popup.getSize();
					Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
					Point p = e.getComponent().getLocationOnScreen();


					X = x + p.x + size.width;
					Y = y + p.y + size.height;

					if(X > ss.width)
						x = x-size.width;
					if(Y > ss.height)
						y = y-size.height;
					m_popup.show(e.getComponent(), x, y);


			}
		}
		public void mouseClicked(MouseEvent e)
		{
			if (e.getClickCount()==2)
			{
				int x = e.getX();
				int y = e.getY();
			

			}
		}
	}

/**
 * MeasurePane 构造子注解。
 */
public MeasureMgtPane(MeasureMgtTableModel data) {
	super();
	m_data = data;
	initPane();
}
/**
 * 得到数据。
 * 创建日期：(2000-12-7 14:02:46)
 * @author：王少松
 */
public MeasureMgtTableModel getData() {

	 return m_data;

  }
/**
 * 得到table。
 * 创建日期：(2000-12-7 14:02:46)
 * @author：王少松
 */

public JTable getTable() {

	 return m_table;

  }
/**
 * 初始化。
 * 创建日期：(2000-12-7 14:02:46)
 * @author：王少松
 */

public void initPane() {

	m_table = new nc.ui.pub.beans.UITable();
	m_table.setAutoCreateColumnsFromModel(false);

	m_table.setModel(m_data);
	m_table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	m_table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

	for (int k = 0; k < MeasureMgtTableModel.columnNames.length; k++) {
		TableCellRenderer renderer;
		DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
		textRenderer.setHorizontalAlignment( MeasureMgtTableModel.columnNames[k].alignment);
		renderer = textRenderer;

		TableCellEditor editor;

		editor = new DefaultCellEditor(new UITextField());
		TableColumn column = new TableColumn(k,
		        MeasureMgtTableModel.columnNames[k].width,renderer, editor);
	 	
		m_table.addColumn(column);
	}

	m_table.addMouseListener(new PopupTrigger());

	JTableHeader header = m_table.getTableHeader();
	header.setUpdateTableInRealTime(false);
	JScrollPane ps = new UIScrollPane(m_table);
	setLayout(new BorderLayout());
	add(ps,BorderLayout.CENTER);

  }
}


