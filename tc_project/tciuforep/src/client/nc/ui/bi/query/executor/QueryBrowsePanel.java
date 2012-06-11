package nc.ui.bi.query.executor;
import java.awt.Dimension;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.iuforeport.businessquery.DBReportTableModel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.pub.querymodel.QueryConst;

import com.borland.dx.dataset.StorageDataSet;
import com.ufida.iufo.pub.tools.AppDebug;

/**
 * ��ѯ������ �������ڣ�(2002-11-18 13:14:12)
 * 
 * @author���쿡��
 */
public class QueryBrowsePanel extends UIPanel implements ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//���ݼ�
	private StorageDataSet m_dataset = null;

	//��ת����ǰ���ݼ�
	private StorageDataSet m_dsBeforeRotate = null;

	private UITablePane ivjTablePnCenter = null;

	private UIScrollPane ivjPnSouth = null;

	/**
	 * QueryBrowsePanel ������ע�⡣
	 */
	public QueryBrowsePanel() {
		super();
		initialize();
	}

	/**
	 * QueryBrowsePanel ������ע�⡣
	 * 
	 * @param p0
	 *            boolean
	 */
	public QueryBrowsePanel(boolean p0) {
		super(p0);
	}

	/**
	 * ������ݼ� �������ڣ�(2002-11-18 13:25:55)
	 * 
	 * @param dataset
	 *            nc.ui.com.dbbase.ClientDataSet
	 */
	public StorageDataSet getDataset() {
		return m_dataset;
	}

	/**
	 * ���ѡ���е���ֵ���ֶ�ȡֵ��ϣ�� �������ڣ�(2003-10-8 12:31:48)
	 * 
	 * @return java.util.Hashtable
	 */
	public void getDecimalCol(Vector<String> vecColname, Vector<String> vecCaption) {
		int iColCount = getTable().getColumnCount();
		int colsize = 0;
		for (int i = 0; i < iColCount; i++) {
			//����
			int iColType = BIModelUtil.variantTypeToSqlType(getDataset()
					.getColumn(i).getDataType());
			if (BIModelUtil.isNumberType(iColType)) {
				//����
				String colName = getDataset().getColumn(i).getColumnName();
				String caption = getDataset().getColumn(i).getCaption();
				vecColname.addElement(colName);
				vecCaption.addElement(caption);
				colsize++;
			}
			//if (colsize >= 8) {
			//break;
			//}
		}
	}

	/**
	 * �����תǰ���ݼ� �������ڣ�(2002-11-18 13:25:55)
	 * 
	 * @param dataset
	 *            nc.ui.com.dbbase.ClientDataSet
	 */
	public StorageDataSet getDsBeforeRotate() {
		return m_dsBeforeRotate;
	}

	/**
	 * ���ѡ���й�ϣ�� �������ڣ�(2003-10-8 12:31:48)
	 * 
	 * @return java.util.Hashtable
	 */
	public Hashtable getPeneRow() {

		Hashtable<String, String> hashPeneRow = null;
		int iSelRow = getSelRow();
		if (iSelRow != -1) {
			hashPeneRow = new Hashtable<String, String>();
			int iColCount = getTable().getColumnCount();
			for (int i = 0; i < iColCount; i++) {
				//ȡֵ
				Object obj = getTable().getValueAt(iSelRow, i);
				String value = (obj == null) ? " " : obj.toString();
				//����
				String colName = getDataset().getColumn(i).getColumnName();
				//����
				hashPeneRow.put(colName, value);
			}
		}
		return hashPeneRow;
	}

	/**
	 * ���� PnSouth ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIScrollPane getPnSouth() {
		if (ivjPnSouth == null) {
			try {
				ivjPnSouth = new nc.ui.pub.beans.UIScrollPane();
				ivjPnSouth.setName("PnSouth");
				ivjPnSouth.setPreferredSize(new java.awt.Dimension(10, 2));
				ivjPnSouth.setBackground(java.awt.Color.blue);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnSouth;
	}

	/**
	 * ���ѡ���� �������ڣ�(2003-10-8 16:58:36)
	 * 
	 * @param iSelRow
	 *            int
	 */
	public int getSelCol() {
		if (getTable().getColumnCount() == 0)
			return -1;
		else
			return getTable().getSelectedColumn();
	}

	/**
	 * ���ѡ���� �������ڣ�(2003-10-8 16:58:36)
	 * 
	 * @param iSelRow
	 *            int
	 */
	public int getSelRow() {
		if (getTable().getRowCount() == 0)
			return -1;
		else
			return getTable().getSelectedRow();
	}

	/**
	 * ��ñ�� �������ڣ�(2002-11-18 13:24:41)
	 * 
	 * @return nc.ui.pub.beans.UITable
	 */
	public UITable getTable() {
		return getTablePnCenter().getTable();
	}

	/**
	 * ���� TablePnCenter ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITablePane
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UITablePane getTablePnCenter() {
		if (ivjTablePnCenter == null) {
			try {
				ivjTablePnCenter = new nc.ui.pub.beans.UITablePane();
				ivjTablePnCenter.setName("TablePnCenter");
				// user code begin {1}
				/*
				 * UITable table = new UITable() { public TableCellRenderer
				 * getDefaultRenderer(Class columnClass) { if (columnClass ==
				 * Double.class) { return
				 * getDefaultRenderer(columnClass.getSuperclass()); } else {
				 * return super.getDefaultRenderer(columnClass); } } };
				 * ivjTablePnCenter.setTable(table);
				 */
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTablePnCenter;
	}

	/**
	 * ��ñ����� �������ڣ�(2002-11-18 13:24:41)
	 * 
	 * @return nc.ui.pub.beans.UITable
	 */
	public UITablePane getTablePnPub() {
		return getTablePnCenter();
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		AppDebug.debug("--------- δ��׽�����쳣 ---------");//@devTools System.out.println("--------- δ��׽�����쳣 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("QueryBrowsePanel");
			setLayout(new java.awt.BorderLayout());
			setSize(640, 480);
			add(getTablePnCenter(), "Center");
			add(getPnSouth(), "South");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		//���ñ����ʾ����
		getTable().setAutoResizeMode(UITable.AUTO_RESIZE_OFF);
		getTable().getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		//���ñ�ͷ��ɫ
		getTable().getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable().getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
		// user code end
	}

	/**
	 * ��ʼ�������ѡȡ���� �������ڣ�(2003-10-22 9:02:12)
	 */
	public void initListener(boolean bAdd) {
		//ȥ�������ѡȡ����
		getTable().getSelectionModel().removeListSelectionListener(this);
		//��ӱ����ѡȡ����
		if (bAdd) {
			getTable().getSelectionModel().addListSelectionListener(this);
			getPnSouth().setPreferredSize(new Dimension(10, 168));
		} else {
			getPnSouth().setPreferredSize(new Dimension(10, 2));
		}
		validate();
	}

	/**
	 * �������ݼ� �������ڣ�(2002-11-18 13:25:55)
	 * 
	 * @param dataset
	 *            nc.ui.com.dbbase.ClientDataSet
	 */
	public void setDataset(StorageDataSet dataset, int iOutWidth) {
		m_dataset = dataset;

		//���ñ�ģ��
		DBReportTableModel tm = new DBReportTableModel();
		tm.setDataSet(m_dataset);
		getTable().setModel(tm);

		getTable().getSelectionModel().setSelectionInterval(0, 0);
		int iColCount = tm.getColumnCount();
		if (iColCount > 0 && iColCount < 10) {
			int[] iColWidths = new int[iColCount];
			int iWidth = iOutWidth / iColCount;
			if (iWidth < 100)
				iWidth = 100;
			for (int i = 0; i < iColCount; i++)
				iColWidths[i] = iWidth;
			getTable().setColumnWidth(iColWidths);
			getTable().sizeColumnsToFit(-1);
		}
	}

	/**
	 * ������תǰ���ݼ� �������ڣ�(2002-11-18 13:25:55)
	 * 
	 * @param dataset
	 *            nc.ui.com.dbbase.ClientDataSet
	 */
	public void setDsBeforeRotate(StorageDataSet dsBeforeRotate) {
		m_dsBeforeRotate = dsBeforeRotate;
	}

	/**
	 * ����ѡ���� �������ڣ�(2003-10-8 16:58:36)
	 * 
	 * @param iSelRow
	 *            int
	 */
	public void setSelRow(int iSelRow) {
		getTable().getSelectionModel().setSelectionInterval(iSelRow, iSelRow);
		getTable().scrollRectToVisible(
				getTable().getCellRect(iSelRow, 0, false));
	}

	/**
	 * ����иı��¼���Ӧ
	 * 
	 * @param e
	 *            the event that characterizes the change.
	 */
	public void valueChanged(ListSelectionEvent e) {
	}
} 