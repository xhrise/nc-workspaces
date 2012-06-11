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
 * 查询浏览面板 创建日期：(2002-11-18 13:14:12)
 * 
 * @author：朱俊彬
 */
public class QueryBrowsePanel extends UIPanel implements ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//数据集
	private StorageDataSet m_dataset = null;

	//旋转交叉前数据集
	private StorageDataSet m_dsBeforeRotate = null;

	private UITablePane ivjTablePnCenter = null;

	private UIScrollPane ivjPnSouth = null;

	/**
	 * QueryBrowsePanel 构造子注解。
	 */
	public QueryBrowsePanel() {
		super();
		initialize();
	}

	/**
	 * QueryBrowsePanel 构造子注解。
	 * 
	 * @param p0
	 *            boolean
	 */
	public QueryBrowsePanel(boolean p0) {
		super(p0);
	}

	/**
	 * 获得数据集 创建日期：(2002-11-18 13:25:55)
	 * 
	 * @param dataset
	 *            nc.ui.com.dbbase.ClientDataSet
	 */
	public StorageDataSet getDataset() {
		return m_dataset;
	}

	/**
	 * 获得选中行的数值型字段取值哈希表 创建日期：(2003-10-8 12:31:48)
	 * 
	 * @return java.util.Hashtable
	 */
	public void getDecimalCol(Vector<String> vecColname, Vector<String> vecCaption) {
		int iColCount = getTable().getColumnCount();
		int colsize = 0;
		for (int i = 0; i < iColCount; i++) {
			//加入
			int iColType = BIModelUtil.variantTypeToSqlType(getDataset()
					.getColumn(i).getDataType());
			if (BIModelUtil.isNumberType(iColType)) {
				//列名
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
	 * 获得旋转前数据集 创建日期：(2002-11-18 13:25:55)
	 * 
	 * @param dataset
	 *            nc.ui.com.dbbase.ClientDataSet
	 */
	public StorageDataSet getDsBeforeRotate() {
		return m_dsBeforeRotate;
	}

	/**
	 * 获得选中行哈希表 创建日期：(2003-10-8 12:31:48)
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
				//取值
				Object obj = getTable().getValueAt(iSelRow, i);
				String value = (obj == null) ? " " : obj.toString();
				//列名
				String colName = getDataset().getColumn(i).getColumnName();
				//加入
				hashPeneRow.put(colName, value);
			}
		}
		return hashPeneRow;
	}

	/**
	 * 返回 PnSouth 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 获得选中列 创建日期：(2003-10-8 16:58:36)
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
	 * 获得选中行 创建日期：(2003-10-8 16:58:36)
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
	 * 获得表格 创建日期：(2002-11-18 13:24:41)
	 * 
	 * @return nc.ui.pub.beans.UITable
	 */
	public UITable getTable() {
		return getTablePnCenter().getTable();
	}

	/**
	 * 返回 TablePnCenter 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITablePane
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 获得表格面板 创建日期：(2002-11-18 13:24:41)
	 * 
	 * @return nc.ui.pub.beans.UITable
	 */
	public UITablePane getTablePnPub() {
		return getTablePnCenter();
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
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
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
		//设置表格显示属性
		getTable().setAutoResizeMode(UITable.AUTO_RESIZE_OFF);
		getTable().getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		//设置表头着色
		getTable().getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable().getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
		// user code end
	}

	/**
	 * 初始化表格行选取监听 创建日期：(2003-10-22 9:02:12)
	 */
	public void initListener(boolean bAdd) {
		//去除表格行选取监听
		getTable().getSelectionModel().removeListSelectionListener(this);
		//添加表格行选取监听
		if (bAdd) {
			getTable().getSelectionModel().addListSelectionListener(this);
			getPnSouth().setPreferredSize(new Dimension(10, 168));
		} else {
			getPnSouth().setPreferredSize(new Dimension(10, 2));
		}
		validate();
	}

	/**
	 * 设置数据集 创建日期：(2002-11-18 13:25:55)
	 * 
	 * @param dataset
	 *            nc.ui.com.dbbase.ClientDataSet
	 */
	public void setDataset(StorageDataSet dataset, int iOutWidth) {
		m_dataset = dataset;

		//设置表模型
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
	 * 设置旋转前数据集 创建日期：(2002-11-18 13:25:55)
	 * 
	 * @param dataset
	 *            nc.ui.com.dbbase.ClientDataSet
	 */
	public void setDsBeforeRotate(StorageDataSet dsBeforeRotate) {
		m_dsBeforeRotate = dsBeforeRotate;
	}

	/**
	 * 设置选中行 创建日期：(2003-10-8 16:58:36)
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
	 * 表格行改变事件响应
	 * 
	 * @param e
	 *            the event that characterizes the change.
	 */
	public void valueChanged(ListSelectionEvent e) {
	}
} 