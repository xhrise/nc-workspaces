package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.bi.query.manager.QueryModelBO_Client;
import nc.ui.iuforeport.businessquery.RowNoCellRenderer;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.UITextField;
import nc.vo.bi.query.manager.BIQueryUtil;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.QEEnvParamBean;
import nc.vo.iuforeport.businessquery.QueryUtil;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.core.ObjectNode;
import nc.vo.pub.ddc.datadict.DatadictNode;
import nc.vo.pub.ddc.datadict.FieldDef;
import nc.vo.pub.ddc.datadict.TableDef;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 动态变更表结构测试面板 创建日期：(2005-6-17 9:32:02)
 * 
 * @author：朱俊彬
 */
public class AlterTablePanel extends UIPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//定义数据源
	private String m_dsNameForDef = null;

	//数据类型常量
	public final static String[] DATA_TYPES = new String[] { "varchar", "char",
			"int", "smallint", "decimal" };

	//对话框实例
	private AlterTableDlg m_alterDlg = null;

	private UIButton ivjBnAddLine = null;

	private UIButton ivjBnAlter = null;

	private UIButton ivjBnDelLine = null;

	private UIPanel ivjPnNorth = null;

	private UIPanel ivjPnSouth = null;

	private UITablePane ivjTablePn = null;

	private ButtonGroup ivjButtonGroup = null;

	private UIComboBox ivjCbbDsn = null;

	private UIComboBox ivjCbbTable = null;

	private UILabel ivjLabelDsn = null;

	private UIRadioButton ivjRadioBnAlter = null;

	private UIRadioButton ivjRadioBnCreate = null;

	private UITextField ivjTFTable = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener, ItemListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == AlterTablePanel.this.getBnAlter())
				connEtoC1(e);
			if (e.getSource() == AlterTablePanel.this.getBnDelLine())
				connEtoC2(e);
			if (e.getSource() == AlterTablePanel.this.getBnAddLine())
				connEtoC3(e);
			if (e.getSource() == AlterTablePanel.this.getRadioBnAlter())
				connEtoC4(e);
			if (e.getSource() == AlterTablePanel.this.getRadioBnCreate())
				connEtoC5(e);
			if (e.getSource() == AlterTablePanel.this.getBnClose())
				connEtoC8(e);
			if (e.getSource() == AlterTablePanel.this.getRadioBnDel())
				connEtoC9(e);
		};

		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == AlterTablePanel.this.getCbbDsn())
				connEtoC6(e);
			if (e.getSource() == AlterTablePanel.this.getCbbTable())
				connEtoC7(e);
		};
	};

	private UIButton ivjBnClose = null;

	/**
	 * AlterTablePanel 构造子注解。
	 */
	public AlterTablePanel() {
		super();
		initialize();
	}

	/**
	 * 增行 创建日期：(2003-8-8 15:14:24)
	 */
	public void addLine() {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//界面加行
		int iIndex = getTM().getRowCount();
		Object[] objrows = new Object[] { "", "", "varchar", "20", "0",
				new Boolean(true) };
		getTM().addRow(objrows);
		//选中新增行
		getTable().getSelectionModel().setSelectionInterval(iIndex, iIndex);
	}

	/**
	 * 增行
	 */
	public void bnAddLine_ActionPerformed(ActionEvent actionEvent) {
		addLine();
	}

	/**
	 * 变更
	 * @i18n miufo00436=你真想删啊？
	 * @i18n miufo00437=是你的表吗你就删？
	 */
	public void bnAlter_ActionPerformed(ActionEvent actionEvent) {
		//		---------------------------
		//		信使服务
		//		---------------------------
		//		在 2005-6-17 11:18:03 从 BIHUI 到 NC_ZJB 的消息
		//
		//		不施粉黛、不布光影、不挑背景、不饰华服、不择手段
		//		---------------------------
		//		确定
		//		---------------------------
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//合法性校验
		String strErr = check();
		if (strErr != null) {
			MessageDialog.showWarningDlg(this, "", strErr);
			return;
		}
		//提示
		if (getAct() == 1) {
			String hint = StringResource.getStringResource("miufo00436");
			if (MessageDialog.showYesNoDlg(this, "", hint) != UIDialog.ID_YES) {
				return;
			}
			hint = StringResource.getStringResource("miufo00437");
			if (MessageDialog.showYesNoDlg(this, "", hint) != UIDialog.ID_YES) {
				return;
			}
		}
		//执行变更
		doAlter();
	}

	/**
	 * 删行
	 */
	public void bnDelLine_ActionPerformed(ActionEvent actionEvent) {
		delLine();
	}

	/**
	 * 数据源下拉框改变选项事件响应
	 */
	public void cbbDsn_ItemStateChanged(ItemEvent itemEvent) {
		if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
			fillCbbTable();
		}
	}

	/**
	 * 数据表下拉框改变选项事件响应
	 */
	public void cbbTable_ItemStateChanged(ItemEvent itemEvent) {
		if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
			fillMetaData();
		}
	}

	/**
	 * 删行 创建日期：(2003-8-8 15:14:24)
	 */
	private void delLine() {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1) {
			//界面刷新
			getTM().removeRow(iSelIndex);
			if (iSelIndex != 0)
				getTable().getSelectionModel().setSelectionInterval(
						iSelIndex - 1, iSelIndex - 1);
			else if (getTable().getRowCount() != 0)
				getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
	}

	/**
	 * 填充数据表下拉框
	 */
	private void fillCbbTable() {
		try {
			int iSelIndex = getCbbTable().getSelectedIndex();
			//去除监听
			getCbbTable().removeItemListener(ivjEventHandler);
			//获得数据表
			String dsName = getCbbDsn().getSelectedItem().toString();
			FromTableVO[] fts = QueryModelBO_Client.getTables(dsName);
			//加入下拉框
			getCbbTable().removeAllItems();
			int iLen = (fts == null) ? 0 : fts.length;
			for (int i = 0; i < iLen; i++) {
				getCbbTable().addItem(fts[i].getTablecode());
			}
			//选中
			if (iSelIndex != -1 && iSelIndex < iLen) {
				getCbbTable().setSelectedIndex(iSelIndex);
			}
			//添加监听
			getCbbTable().addItemListener(ivjEventHandler);
			//选中第一张表
			fillMetaData();
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	/**
	 * 填充数据表下拉框
	 */
	private void fillMetaData() {
		try {
			//获得数据表
			String dsName = getCbbDsn().getSelectedItem().toString();
			String tableId = getCbbTable().getSelectedItem().toString();
			SelectFldVO[] sfs = QueryModelBO_Client.getFields(tableId, dsName);
			//加入下拉框
			getTM().setNumRows(0);
			int iLen = (sfs == null) ? 0 : sfs.length;
			for (int i = 0; i < iLen; i++) {
				String fldAlias = sfs[i].getFldalias();
				String colType = QueryUtil.convDataType2DB(sfs[i].getColtype()
						.intValue());
				colType = colType.toLowerCase();
				Integer iScale = new Integer(sfs[i].getScale());
				Integer iPrecision = new Integer(sfs[i].getPrecision());
				Boolean bNull = new Boolean(sfs[i].getNote() == null);
				//加行
				Object[] objRows = new Object[] { "", fldAlias, colType,
						iScale, iPrecision, bNull };
				getTM().addRow(objRows);
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	/**
	 * 返回 ButtonGroup 特性值。
	 * 
	 * @return ButtonGroup
	 */
	/* 警告：此方法将重新生成。 */
	private ButtonGroup getButtonGroup() {
		if (ivjButtonGroup == null) {
			try {
				ivjButtonGroup = new ButtonGroup();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjButtonGroup;
	}

	/**
	 * 获得表格 创建日期：(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	public UITable getTable() {
		return (UITable) getTablePn().getTable();
	}

	/**
	 * 获得表格 创建日期：(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	public DefaultTableModel getTM() {
		return (DefaultTableModel) getTable().getModel();
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
	 * 初始化连接
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	/* 警告：此方法将重新生成。 */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		// user code end
		getBnAlter().addActionListener(ivjEventHandler);
		getBnDelLine().addActionListener(ivjEventHandler);
		getBnAddLine().addActionListener(ivjEventHandler);
		getRadioBnAlter().addActionListener(ivjEventHandler);
		getRadioBnCreate().addActionListener(ivjEventHandler);
		getCbbDsn().addItemListener(ivjEventHandler);
		getCbbTable().addItemListener(ivjEventHandler);
		getBnClose().addActionListener(ivjEventHandler);
		getRadioBnDel().addActionListener(ivjEventHandler);
	}

	/**
	 * 初始化数据源下拉框 创建日期：(2003-9-17 14:50:41)
	 */
	public void initDsn() {
		String dsNameForDef = getDsNameForDef();
		try {
			//去除监听
			getCbbDsn().removeItemListener(ivjEventHandler);
			//获得所有数据源
			String[] dsns = QEEnvParamBean.reloadInstance().getQueryDsn();//DataSourceBO_Client.getMwDataSources();
			int iLen = (dsns == null) ? 0 : dsns.length;
			for (int i = 0; i < iLen; i++) {
				getCbbDsn().addItem(dsns[i]);
			}
			//添加监听
			getCbbDsn().addItemListener(ivjEventHandler);
			//选中UFBI
			if (getCbbDsn().getSelectedItem().equals(dsNameForDef)) {
				fillCbbTable();
			} else {
				getCbbDsn().setSelectedItem(dsNameForDef);
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	/**
	 * 设置列编辑器 创建日期：(2003-9-17 14:50:41)
	 * @i18n uiufofurl0377=数据类型
	 */
	public void initEditorValue() {
		//数据类型列编辑器
		JComboBox cbbOperator = new UIComboBox(DATA_TYPES);
		TableColumn tc = getTable().getColumn(StringResource.getStringResource("uiufofurl0377"));
		tc.setCellEditor(new UIRefCellEditor(cbbOperator));
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("AlterTablePanel");
			setLayout(new BorderLayout());
			setSize(600, 480);
			add(getPnNorth(), "North");
			add(getPnSouth(), "South");
			add(getTablePn(), "Center");
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		getTFTable().setEnabled(false);
		//初始化按钮组
		getButtonGroup().add(getRadioBnAlter());
		getButtonGroup().add(getRadioBnCreate());
		getButtonGroup().add(getRadioBnDel());
		getRadioBnAlter().setSelected(true);
		//初始化表格
		initTable();
		setTableCell();
		initEditorValue();
		//初始化数据源
		initDsn();
		// user code end
	}

	/**
	 * 初始化表格 创建日期：(2003-4-2 13:57:39)
	 * @i18n miufo00438=字段物理名
	 * @i18n uiufofurl0377=数据类型
	 * @i18n miufo1001011=长度
	 * @i18n miufo00439=精度
	 * @i18n miufo00440=可空
	 */
	private void initTable() {
		//表模型
		DefaultTableModel tm = new DefaultTableModel(new Object[] { "",
				StringResource.getStringResource("miufo00438"), StringResource.getStringResource("uiufofurl0377"), StringResource.getStringResource("miufo1001011"), StringResource.getStringResource("miufo00439"), StringResource.getStringResource("miufo00440") }, 0) {
			/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

			public int getColumnCount() {
				return 6;
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int col) {
				if (col == 5) {
					return Boolean.class;
				} else {
					return Object.class;
				}
			}

			public boolean isCellEditable(int row, int col) {
				return (col != 0);
			}
		};
		getTable().setModel(tm);
		//设置表属性
		getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//getTable().getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		//getTable().getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
		//getTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		//getTable().sizeColumnsToFit(-1);
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable().setColumnWidth(new int[] { 20, 188, 120, 100, 100, 100 });
	}

	/**
	 * 设置居中对齐 创建日期：(01-5-14 13:17:27)
	 */
	public void setTableCell() {
		DefaultTableCellRenderer renderer = null;
		for (int i = 0; i < getTable().getColumnCount() - 1; i++) {
			TableColumn tc = getTable().getColumnModel().getColumn(i);
			if (i == 0)
				renderer = new RowNoCellRenderer();
			else
				renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			tc.setCellRenderer(renderer);
		}
	}

	/**
	 * 设置对话框实例 创建日期：(2005-5-16 19:07:52)
	 *  
	 */
	public void setDlg(AlterTableDlg newAlterDlg) {
		m_alterDlg = newAlterDlg;
	}

	/**
	 * 合法性校验 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 * @i18n miufo00441=字段数为0
	 * @i18n miufo1000695=第
	 * @i18n miufo00442=行字段名为空
	 * @i18n miufo00443=字段名不能重复：
	 * @i18n miufo00444=字段别名请使用字母开头且由字母、数字、下划线构成的字符，不得使用SQL保留字
	 * @i18n miufologin021=存在非法字符
	 * @i18n miufo00445=行长度为空
	 * @i18n miufo00446=行精度为空
	 */
	private String check() {
		int iLen = getTM().getRowCount();
		if (iLen == 0) {
			return StringResource.getStringResource("miufo00441");
		}
		Hashtable<String, String> hashFldAlias = new Hashtable<String, String>();
		for (int i = 0; i < iLen; i++) {
			//校验字段名
			String fldAlias = (getTM().getValueAt(i, 1) == null) ? "" : getTM()
					.getValueAt(i, 1).toString().trim();
			if (fldAlias.equals(""))
				return StringResource.getStringResource("miufo1000695") + (i + 1) + StringResource.getStringResource("miufo00442");
			if (hashFldAlias.containsKey(fldAlias))
				return StringResource.getStringResource("miufo00443");
			else
				hashFldAlias.put(fldAlias, fldAlias);
			//字段命名
			String strTemp = fldAlias.toLowerCase();
			for (int j = 0; j < strTemp.length(); j++) {
				char c = strTemp.charAt(j);
				if (j == 0 && (c < 'a' || c > 'z'))
					return StringResource.getStringResource("miufo00444");
				if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z')
						|| c == '_')
					continue;
				return StringResource.getStringResource("miufologin021") + c + StringResource.getStringResource("miufo00444");
			}
			//校验长度
			String strScale = (getTM().getValueAt(i, 3) == null) ? "" : getTM()
					.getValueAt(i, 3).toString().trim();
			if (strScale.equals(""))
				return StringResource.getStringResource("miufo1000695") + (i + 1) + StringResource.getStringResource("miufo00445");
			//校验精度
			String strPrecision = (getTM().getValueAt(i, 4) == null) ? ""
					: getTM().getValueAt(i, 4).toString().trim();
			if (strPrecision.equals(""))
				return StringResource.getStringResource("miufo1000695") + (i + 1) + StringResource.getStringResource("miufo00446");
		}
		return null;
	}

	/**
	 * CLOSE
	 */
	public void bnClose_ActionPerformed(ActionEvent actionEvent) {
		if (getDlg() != null) {
			getDlg().closeCancel();
		}
	}

	/**
	 * 获得对话框
	 */
	private AlterTableDlg getDlg() {
		return m_alterDlg;
	}

	/**
	 * 构造表定义
	 */
	private TableDef makeTableDef(String tableId) {
		ObjectNode node = new DatadictNode();
		node.setID(tableId);
		node.setDisplayName(tableId);
		//
		TableDef td = new TableDef();
		td.setNode(node);
		//
		int iLen = getTM().getRowCount();
		for (int i = 0; i < iLen; i++) {
			FieldDef fd = new FieldDef();
			//获得字段名
			String fldAlias = getTM().getValueAt(i, 1).toString().trim();
			fd.setID(fldAlias);
			fd.setDisplayName(fldAlias);
			//获得数据类型
			String colType = getTM().getValueAt(i, 2).toString().trim();
			int iColType = QueryUtil.convDB2DataType(colType.toUpperCase());
			fd.setDataType(iColType); //SQL TYPES
			//获得长度
			String strScale = getTM().getValueAt(i, 3).toString().trim();
			fd.setLength(Integer.parseInt(strScale));
			//获得精度
			String strPecision = getTM().getValueAt(i, 4).toString().trim();
			fd.setPrecision(Integer.parseInt(strPecision));
			//获得可空性
			Boolean bNull = (Boolean) getTM().getValueAt(i, 5);
			fd.setNull(bNull.booleanValue());
			//记录
			td.getFieldDefs().addFieldDef(fd);
		}
		return td;
	}

	/**
	 * 获得表名
	 */
	private String getTableId() {
		String tableId = null;
		if (getRadioBnCreate().isSelected()) {
			tableId = getTFTable().getText().trim();
		} else {
			tableId = getCbbTable().getSelectedItem().toString();
		}
		return tableId;
	}

	/**
	 * 执行变更
	 * @i18n miufo00447=变更完成
	 * @i18n miufo00448=变更失败
	 */
	private void doAlter() {
		//执行变更
		try {
			String tableId = getTableId();
			TableDef td = makeTableDef(tableId);
			int iAct = getAct();
			String dsName = getCbbDsn().getSelectedItem().toString();
			BIQueryUtil.alterTable(tableId, td, iAct, dsName);
			//
			MessageDialog.showHintDlg(this, "", StringResource.getStringResource("miufo00447"));
		} catch (Exception e) {
			AppDebug.debug(e);
			MessageDialog.showWarningDlg(this, "", StringResource.getStringResource("miufo00448"));
		}
	}

	/**
	 * 获得变更类型
	 */
	private int getAct() {
		int iAct = 2;
		if (getRadioBnCreate().isSelected()) {
			iAct = 0;
		} else if (getRadioBnDel().isSelected()) {
			iAct = 1;
		}
		AppDebug.debug("Act = " + iAct);//@devTools System.out.println("Act = " + iAct);
		return iAct;
	}

	private UIRadioButton ivjRadioBnDel = null;

	/**
	 * connEtoC1: (BnAlter.action.actionPerformed(ActionEvent) -->
	 * AlterTablePanel.bnAlter_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC1(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnAlter_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC2: (BnDelLine.action.actionPerformed(ActionEvent) -->
	 * AlterTablePanel.bnDelLine_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC2(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnDelLine_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3: (BnAddLine.action.actionPerformed(ActionEvent) -->
	 * AlterTablePanel.bnAddLine_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC3(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnAddLine_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC4: (RadioBnAlter.action.actionPerformed(ActionEvent) -->
	 * AlterTablePanel.radioBnAlter_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC4(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.radioBnAlter_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC5: (RadioBnCreate.action.actionPerformed(ActionEvent) -->
	 * AlterTablePanel.radioBnCreate_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC5(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.radioBnCreate_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC6: (CbbDsn.item.itemStateChanged(ItemEvent) -->
	 * AlterTablePanel.cbbDsn_ItemStateChanged(LItemEvent;)V)
	 * 
	 * @param arg1
	 *            ItemEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC6(ItemEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.cbbDsn_ItemStateChanged(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC7: (CbbTable.item.itemStateChanged(ItemEvent) -->
	 * AlterTablePanel.cbbTable_ItemStateChanged(LItemEvent;)V)
	 * 
	 * @param arg1
	 *            ItemEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC7(ItemEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.cbbTable_ItemStateChanged(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC8: (BnClose.action.actionPerformed(ActionEvent) -->
	 * AlterTablePanel.bnClose_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC8(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnClose_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC9: (RadioBnDel.action.actionPerformed(ActionEvent) -->
	 * AlterTablePanel.radioBnDel_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC9(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.radioBnDel_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * 返回 BnAddLine 特性值。
	 * 
	 * @return UIButton
	 * @i18n uiufo20207=增行
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnAddLine() {
		if (ivjBnAddLine == null) {
			try {
				ivjBnAddLine = new UIButton();
				ivjBnAddLine.setName("BnAddLine");
				ivjBnAddLine.setText(StringResource.getStringResource("uiufo20207"));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnAddLine;
	}

	/**
	 * 返回 BnAlter 特性值。
	 * 
	 * @return UIButton
	 * @i18n miufo00449=变更
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnAlter() {
		if (ivjBnAlter == null) {
			try {
				ivjBnAlter = new UIButton();
				ivjBnAlter.setName("BnAlter");
				ivjBnAlter.setText(StringResource.getStringResource("miufo00449"));
				ivjBnAlter.setForeground(Color.red);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnAlter;
	}

	/**
	 * 返回 BnClose 特性值。
	 * 
	 * @return UIButton
	 * @i18n miufo1000764=关闭
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnClose() {
		if (ivjBnClose == null) {
			try {
				ivjBnClose = new UIButton();
				ivjBnClose.setName("BnClose");
				ivjBnClose.setText(StringResource.getStringResource("miufo1000764"));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnClose;
	}

	/**
	 * 返回 BnDelLine 特性值。
	 * 
	 * @return UIButton
	 * @i18n uiufo20208=删行
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnDelLine() {
		if (ivjBnDelLine == null) {
			try {
				ivjBnDelLine = new UIButton();
				ivjBnDelLine.setName("BnDelLine");
				ivjBnDelLine.setText(StringResource.getStringResource("uiufo20208"));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnDelLine;
	}

	/**
	 * 返回 CbbDsn 特性值。
	 * 
	 * @return UIComboBox
	 */
	/* 警告：此方法将重新生成。 */
	private UIComboBox getCbbDsn() {
		if (ivjCbbDsn == null) {
			try {
				ivjCbbDsn = new UIComboBox();
				ivjCbbDsn.setName("CbbDsn");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCbbDsn;
	}

	/**
	 * 返回 CbbTable 特性值。
	 * 
	 * @return UIComboBox
	 */
	/* 警告：此方法将重新生成。 */
	private UIComboBox getCbbTable() {
		if (ivjCbbTable == null) {
			try {
				ivjCbbTable = new UIComboBox();
				ivjCbbTable.setName("CbbTable");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCbbTable;
	}

	/**
	 * 返回 LabelDsn 特性值。
	 * 
	 * @return UILabel
	 * @i18n uiufofn0008=数据源
	 */
	/* 警告：此方法将重新生成。 */
	private UILabel getLabelDsn() {
		if (ivjLabelDsn == null) {
			try {
				ivjLabelDsn = new UILabel();
				ivjLabelDsn.setName("LabelDsn");
				ivjLabelDsn.setText(StringResource.getStringResource("uiufofn0008"));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelDsn;
	}

	/**
	 * 返回 PnNorth 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnNorth() {
		if (ivjPnNorth == null) {
			try {
				ivjPnNorth = new UIPanel();
				ivjPnNorth.setName("PnNorth");
				ivjPnNorth.setPreferredSize(new Dimension(10, 200));
				ivjPnNorth.setLayout(new GridBagLayout());

				GridBagConstraints constraintsLabelDsn = new GridBagConstraints();
				constraintsLabelDsn.gridx = 2;
				constraintsLabelDsn.gridy = 1;
				constraintsLabelDsn.ipadx = 8;
				constraintsLabelDsn.insets = new Insets(33, 10, 16, 10);
				getPnNorth().add(getLabelDsn(), constraintsLabelDsn);

				GridBagConstraints constraintsCbbDsn = new GridBagConstraints();
				constraintsCbbDsn.gridx = 3;
				constraintsCbbDsn.gridy = 1;
				constraintsCbbDsn.fill = GridBagConstraints.HORIZONTAL;
				constraintsCbbDsn.weightx = 1.0;
				constraintsCbbDsn.ipadx = 100;
				constraintsCbbDsn.insets = new Insets(33, 10, 16, 160);
				getPnNorth().add(getCbbDsn(), constraintsCbbDsn);

				GridBagConstraints constraintsRadioBnAlter = new GridBagConstraints();
				constraintsRadioBnAlter.gridx = 2;
				constraintsRadioBnAlter.gridy = 2;
				constraintsRadioBnAlter.ipadx = -40;
				constraintsRadioBnAlter.insets = new Insets(17, 10, 16, 10);
				getPnNorth().add(getRadioBnAlter(), constraintsRadioBnAlter);

				GridBagConstraints constraintsRadioBnCreate = new GridBagConstraints();
				constraintsRadioBnCreate.gridx = 2;
				constraintsRadioBnCreate.gridy = 3;
				constraintsRadioBnCreate.ipadx = -40;
				constraintsRadioBnCreate.insets = new Insets(17, 10, 35, 10);
				getPnNorth().add(getRadioBnCreate(), constraintsRadioBnCreate);

				GridBagConstraints constraintsCbbTable = new GridBagConstraints();
				constraintsCbbTable.gridx = 3;
				constraintsCbbTable.gridy = 2;
				constraintsCbbTable.fill = GridBagConstraints.HORIZONTAL;
				constraintsCbbTable.weightx = 1.0;
				constraintsCbbTable.ipadx = 100;
				constraintsCbbTable.insets = new Insets(17, 10, 16, 160);
				getPnNorth().add(getCbbTable(), constraintsCbbTable);

				GridBagConstraints constraintsTFTable = new GridBagConstraints();
				constraintsTFTable.gridx = 3;
				constraintsTFTable.gridy = 3;
				constraintsTFTable.fill = GridBagConstraints.HORIZONTAL;
				constraintsTFTable.weightx = 1.0;
				constraintsTFTable.ipadx = 196;
				constraintsTFTable.ipady = 2;
				constraintsTFTable.insets = new Insets(17, 10, 35, 160);
				getPnNorth().add(getTFTable(), constraintsTFTable);

				GridBagConstraints constraintsRadioBnDel = new GridBagConstraints();
				constraintsRadioBnDel.gridx = 1;
				constraintsRadioBnDel.gridy = 2;
				constraintsRadioBnDel.ipadx = -40;
				constraintsRadioBnDel.insets = new Insets(17, 80, 16, 10);
				getPnNorth().add(getRadioBnDel(), constraintsRadioBnDel);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnNorth;
	}

	/**
	 * 返回 PnSouth 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnSouth() {
		if (ivjPnSouth == null) {
			try {
				ivjPnSouth = new UIPanel();
				ivjPnSouth.setName("PnSouth");
				ivjPnSouth.setLayout(getPnSouthFlowLayout());
				getPnSouth().add(getBnAddLine(), getBnAddLine().getName());
				getPnSouth().add(getBnDelLine(), getBnDelLine().getName());
				getPnSouth().add(getBnAlter(), getBnAlter().getName());
				getPnSouth().add(getBnClose(), getBnClose().getName());
				// user code begin {1}
				//getPnSouth().setPreferredSize(new Dimension(10, 40));
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
	 * 返回 PnSouthFlowLayout 特性值。
	 * 
	 * @return FlowLayout
	 */
	/* 警告：此方法将重新生成。 */
	private FlowLayout getPnSouthFlowLayout() {
		FlowLayout ivjPnSouthFlowLayout = null;
		try {
			/* 创建部件 */
			ivjPnSouthFlowLayout = new FlowLayout();
			ivjPnSouthFlowLayout.setVgap(8);
			ivjPnSouthFlowLayout.setHgap(20);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnSouthFlowLayout;
	}

	/**
	 * 返回 RadioBnAlter 特性值。
	 * 
	 * @return UIRadioButton
	 * @i18n miufo1001396=修改
	 */
	/* 警告：此方法将重新生成。 */
	private UIRadioButton getRadioBnAlter() {
		if (ivjRadioBnAlter == null) {
			try {
				ivjRadioBnAlter = new UIRadioButton();
				ivjRadioBnAlter.setName("RadioBnAlter");
				ivjRadioBnAlter.setText(StringResource.getStringResource("miufo1001396"));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRadioBnAlter;
	}

	/**
	 * 返回 RadioBnCreate 特性值。
	 * 
	 * @return UIRadioButton
	 * @i18n miufo1001591=创建
	 */
	/* 警告：此方法将重新生成。 */
	private UIRadioButton getRadioBnCreate() {
		if (ivjRadioBnCreate == null) {
			try {
				ivjRadioBnCreate = new UIRadioButton();
				ivjRadioBnCreate.setName("RadioBnCreate");
				ivjRadioBnCreate.setText(StringResource.getStringResource("miufo1001591"));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRadioBnCreate;
	}

	/**
	 * 返回 RadioBnDel 特性值。
	 * 
	 * @return UIRadioButton
	 * @i18n ubichart00006=删除
	 */
	/* 警告：此方法将重新生成。 */
	private UIRadioButton getRadioBnDel() {
		if (ivjRadioBnDel == null) {
			try {
				ivjRadioBnDel = new UIRadioButton();
				ivjRadioBnDel.setName("RadioBnDel");
				ivjRadioBnDel.setText(StringResource.getStringResource("ubichart00006"));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRadioBnDel;
	}

	/**
	 * 返回 TablePn 特性值。
	 * 
	 * @return UITablePane
	 */
	/* 警告：此方法将重新生成。 */
	private UITablePane getTablePn() {
		if (ivjTablePn == null) {
			try {
				ivjTablePn = new UITablePane();
				ivjTablePn.setName("TablePn");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTablePn;
	}

	/**
	 * 返回 TFTable 特性值。
	 * 
	 * @return UITextField
	 */
	/* 警告：此方法将重新生成。 */
	private UITextField getTFTable() {
		if (ivjTFTable == null) {
			try {
				ivjTFTable = new UITextField();
				ivjTFTable.setName("TFTable");
				ivjTFTable.setMaxLength(50);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTFTable;
	}

	/**
	 * radioBnAlter
	 */
	public void radioBnAlter_ActionPerformed(ActionEvent actionEvent) {
		//状态改变
		changeStatus(true);
	}

	/**
	 * radioBnCreate
	 */
	public void radioBnCreate_ActionPerformed(ActionEvent actionEvent) {
		//状态改变
		changeStatus(false);
	}

	/**
	 * radioBnDel
	 */
	public void radioBnDel_ActionPerformed(ActionEvent actionEvent) {
		//状态改变
		changeStatus(true);
	}

	/**
	 * 状态改变
	 */
	private void changeStatus(boolean bAlter) {
		if (bAlter) {
			getCbbTable().setEnabled(true);
			getTFTable().setEnabled(false);
			fillCbbTable();
			//fillMetaData();
		} else {
			getCbbTable().setEnabled(false);
			getTFTable().setEnabled(true);
			getTFTable().grabFocus();
			getTM().setNumRows(0);
		}
	}

	/**
	 * 关闭按钮不可见
	 */
	public void setBnCloseVisible(boolean bVisible) {
		getBnClose().setVisible(bVisible);
	}

	/**
	 * 设置定义数据源
	 */
	public void setDsNameForDef(String dsNameForDef) {
		m_dsNameForDef = dsNameForDef;
	}

	/**
	 * 获得定义数据源
	 */
	public String getDsNameForDef() {
		return m_dsNameForDef;
	}
}  