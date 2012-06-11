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
 * ��̬�����ṹ������� �������ڣ�(2005-6-17 9:32:02)
 * 
 * @author���쿡��
 */
public class AlterTablePanel extends UIPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//��������Դ
	private String m_dsNameForDef = null;

	//�������ͳ���
	public final static String[] DATA_TYPES = new String[] { "varchar", "char",
			"int", "smallint", "decimal" };

	//�Ի���ʵ��
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
	 * AlterTablePanel ������ע�⡣
	 */
	public AlterTablePanel() {
		super();
		initialize();
	}

	/**
	 * ���� �������ڣ�(2003-8-8 15:14:24)
	 */
	public void addLine() {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//�������
		int iIndex = getTM().getRowCount();
		Object[] objrows = new Object[] { "", "", "varchar", "20", "0",
				new Boolean(true) };
		getTM().addRow(objrows);
		//ѡ��������
		getTable().getSelectionModel().setSelectionInterval(iIndex, iIndex);
	}

	/**
	 * ����
	 */
	public void bnAddLine_ActionPerformed(ActionEvent actionEvent) {
		addLine();
	}

	/**
	 * ���
	 * @i18n miufo00436=������ɾ����
	 * @i18n miufo00437=����ı������ɾ��
	 */
	public void bnAlter_ActionPerformed(ActionEvent actionEvent) {
		//		---------------------------
		//		��ʹ����
		//		---------------------------
		//		�� 2005-6-17 11:18:03 �� BIHUI �� NC_ZJB ����Ϣ
		//
		//		��ʩ���졢������Ӱ���������������λ����������ֶ�
		//		---------------------------
		//		ȷ��
		//		---------------------------
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//�Ϸ���У��
		String strErr = check();
		if (strErr != null) {
			MessageDialog.showWarningDlg(this, "", strErr);
			return;
		}
		//��ʾ
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
		//ִ�б��
		doAlter();
	}

	/**
	 * ɾ��
	 */
	public void bnDelLine_ActionPerformed(ActionEvent actionEvent) {
		delLine();
	}

	/**
	 * ����Դ������ı�ѡ���¼���Ӧ
	 */
	public void cbbDsn_ItemStateChanged(ItemEvent itemEvent) {
		if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
			fillCbbTable();
		}
	}

	/**
	 * ���ݱ�������ı�ѡ���¼���Ӧ
	 */
	public void cbbTable_ItemStateChanged(ItemEvent itemEvent) {
		if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
			fillMetaData();
		}
	}

	/**
	 * ɾ�� �������ڣ�(2003-8-8 15:14:24)
	 */
	private void delLine() {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1) {
			//����ˢ��
			getTM().removeRow(iSelIndex);
			if (iSelIndex != 0)
				getTable().getSelectionModel().setSelectionInterval(
						iSelIndex - 1, iSelIndex - 1);
			else if (getTable().getRowCount() != 0)
				getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
	}

	/**
	 * ������ݱ�������
	 */
	private void fillCbbTable() {
		try {
			int iSelIndex = getCbbTable().getSelectedIndex();
			//ȥ������
			getCbbTable().removeItemListener(ivjEventHandler);
			//������ݱ�
			String dsName = getCbbDsn().getSelectedItem().toString();
			FromTableVO[] fts = QueryModelBO_Client.getTables(dsName);
			//����������
			getCbbTable().removeAllItems();
			int iLen = (fts == null) ? 0 : fts.length;
			for (int i = 0; i < iLen; i++) {
				getCbbTable().addItem(fts[i].getTablecode());
			}
			//ѡ��
			if (iSelIndex != -1 && iSelIndex < iLen) {
				getCbbTable().setSelectedIndex(iSelIndex);
			}
			//��Ӽ���
			getCbbTable().addItemListener(ivjEventHandler);
			//ѡ�е�һ�ű�
			fillMetaData();
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	/**
	 * ������ݱ�������
	 */
	private void fillMetaData() {
		try {
			//������ݱ�
			String dsName = getCbbDsn().getSelectedItem().toString();
			String tableId = getCbbTable().getSelectedItem().toString();
			SelectFldVO[] sfs = QueryModelBO_Client.getFields(tableId, dsName);
			//����������
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
				//����
				Object[] objRows = new Object[] { "", fldAlias, colType,
						iScale, iPrecision, bNull };
				getTM().addRow(objRows);
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	/**
	 * ���� ButtonGroup ����ֵ��
	 * 
	 * @return ButtonGroup
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	public UITable getTable() {
		return (UITable) getTablePn().getTable();
	}

	/**
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	public DefaultTableModel getTM() {
		return (DefaultTableModel) getTable().getModel();
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
	 * ��ʼ������
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ��ʼ������Դ������ �������ڣ�(2003-9-17 14:50:41)
	 */
	public void initDsn() {
		String dsNameForDef = getDsNameForDef();
		try {
			//ȥ������
			getCbbDsn().removeItemListener(ivjEventHandler);
			//�����������Դ
			String[] dsns = QEEnvParamBean.reloadInstance().getQueryDsn();//DataSourceBO_Client.getMwDataSources();
			int iLen = (dsns == null) ? 0 : dsns.length;
			for (int i = 0; i < iLen; i++) {
				getCbbDsn().addItem(dsns[i]);
			}
			//��Ӽ���
			getCbbDsn().addItemListener(ivjEventHandler);
			//ѡ��UFBI
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
	 * �����б༭�� �������ڣ�(2003-9-17 14:50:41)
	 * @i18n uiufofurl0377=��������
	 */
	public void initEditorValue() {
		//���������б༭��
		JComboBox cbbOperator = new UIComboBox(DATA_TYPES);
		TableColumn tc = getTable().getColumn(StringResource.getStringResource("uiufofurl0377"));
		tc.setCellEditor(new UIRefCellEditor(cbbOperator));
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
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
		//��ʼ����ť��
		getButtonGroup().add(getRadioBnAlter());
		getButtonGroup().add(getRadioBnCreate());
		getButtonGroup().add(getRadioBnDel());
		getRadioBnAlter().setSelected(true);
		//��ʼ�����
		initTable();
		setTableCell();
		initEditorValue();
		//��ʼ������Դ
		initDsn();
		// user code end
	}

	/**
	 * ��ʼ����� �������ڣ�(2003-4-2 13:57:39)
	 * @i18n miufo00438=�ֶ�������
	 * @i18n uiufofurl0377=��������
	 * @i18n miufo1001011=����
	 * @i18n miufo00439=����
	 * @i18n miufo00440=�ɿ�
	 */
	private void initTable() {
		//��ģ��
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
		//���ñ�����
		getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//getTable().getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		//getTable().getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
		//getTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		//getTable().sizeColumnsToFit(-1);
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable().setColumnWidth(new int[] { 20, 188, 120, 100, 100, 100 });
	}

	/**
	 * ���þ��ж��� �������ڣ�(01-5-14 13:17:27)
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
	 * ���öԻ���ʵ�� �������ڣ�(2005-5-16 19:07:52)
	 *  
	 */
	public void setDlg(AlterTableDlg newAlterDlg) {
		m_alterDlg = newAlterDlg;
	}

	/**
	 * �Ϸ���У�� �������ڣ�(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 * @i18n miufo00441=�ֶ���Ϊ0
	 * @i18n miufo1000695=��
	 * @i18n miufo00442=���ֶ���Ϊ��
	 * @i18n miufo00443=�ֶ��������ظ���
	 * @i18n miufo00444=�ֶα�����ʹ����ĸ��ͷ������ĸ�����֡��»��߹��ɵ��ַ�������ʹ��SQL������
	 * @i18n miufologin021=���ڷǷ��ַ�
	 * @i18n miufo00445=�г���Ϊ��
	 * @i18n miufo00446=�о���Ϊ��
	 */
	private String check() {
		int iLen = getTM().getRowCount();
		if (iLen == 0) {
			return StringResource.getStringResource("miufo00441");
		}
		Hashtable<String, String> hashFldAlias = new Hashtable<String, String>();
		for (int i = 0; i < iLen; i++) {
			//У���ֶ���
			String fldAlias = (getTM().getValueAt(i, 1) == null) ? "" : getTM()
					.getValueAt(i, 1).toString().trim();
			if (fldAlias.equals(""))
				return StringResource.getStringResource("miufo1000695") + (i + 1) + StringResource.getStringResource("miufo00442");
			if (hashFldAlias.containsKey(fldAlias))
				return StringResource.getStringResource("miufo00443");
			else
				hashFldAlias.put(fldAlias, fldAlias);
			//�ֶ�����
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
			//У�鳤��
			String strScale = (getTM().getValueAt(i, 3) == null) ? "" : getTM()
					.getValueAt(i, 3).toString().trim();
			if (strScale.equals(""))
				return StringResource.getStringResource("miufo1000695") + (i + 1) + StringResource.getStringResource("miufo00445");
			//У�龫��
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
	 * ��öԻ���
	 */
	private AlterTableDlg getDlg() {
		return m_alterDlg;
	}

	/**
	 * �������
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
			//����ֶ���
			String fldAlias = getTM().getValueAt(i, 1).toString().trim();
			fd.setID(fldAlias);
			fd.setDisplayName(fldAlias);
			//�����������
			String colType = getTM().getValueAt(i, 2).toString().trim();
			int iColType = QueryUtil.convDB2DataType(colType.toUpperCase());
			fd.setDataType(iColType); //SQL TYPES
			//��ó���
			String strScale = getTM().getValueAt(i, 3).toString().trim();
			fd.setLength(Integer.parseInt(strScale));
			//��þ���
			String strPecision = getTM().getValueAt(i, 4).toString().trim();
			fd.setPrecision(Integer.parseInt(strPecision));
			//��ÿɿ���
			Boolean bNull = (Boolean) getTM().getValueAt(i, 5);
			fd.setNull(bNull.booleanValue());
			//��¼
			td.getFieldDefs().addFieldDef(fd);
		}
		return td;
	}

	/**
	 * ��ñ���
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
	 * ִ�б��
	 * @i18n miufo00447=������
	 * @i18n miufo00448=���ʧ��
	 */
	private void doAlter() {
		//ִ�б��
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
	 * ��ñ������
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	 * ���� BnAddLine ����ֵ��
	 * 
	 * @return UIButton
	 * @i18n uiufo20207=����
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� BnAlter ����ֵ��
	 * 
	 * @return UIButton
	 * @i18n miufo00449=���
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� BnClose ����ֵ��
	 * 
	 * @return UIButton
	 * @i18n miufo1000764=�ر�
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� BnDelLine ����ֵ��
	 * 
	 * @return UIButton
	 * @i18n uiufo20208=ɾ��
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� CbbDsn ����ֵ��
	 * 
	 * @return UIComboBox
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� CbbTable ����ֵ��
	 * 
	 * @return UIComboBox
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� LabelDsn ����ֵ��
	 * 
	 * @return UILabel
	 * @i18n uiufofn0008=����Դ
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnNorth ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnSouth ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnSouthFlowLayout ����ֵ��
	 * 
	 * @return FlowLayout
	 */
	/* ���棺�˷������������ɡ� */
	private FlowLayout getPnSouthFlowLayout() {
		FlowLayout ivjPnSouthFlowLayout = null;
		try {
			/* �������� */
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
	 * ���� RadioBnAlter ����ֵ��
	 * 
	 * @return UIRadioButton
	 * @i18n miufo1001396=�޸�
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� RadioBnCreate ����ֵ��
	 * 
	 * @return UIRadioButton
	 * @i18n miufo1001591=����
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� RadioBnDel ����ֵ��
	 * 
	 * @return UIRadioButton
	 * @i18n ubichart00006=ɾ��
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� TablePn ����ֵ��
	 * 
	 * @return UITablePane
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� TFTable ����ֵ��
	 * 
	 * @return UITextField
	 */
	/* ���棺�˷������������ɡ� */
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
		//״̬�ı�
		changeStatus(true);
	}

	/**
	 * radioBnCreate
	 */
	public void radioBnCreate_ActionPerformed(ActionEvent actionEvent) {
		//״̬�ı�
		changeStatus(false);
	}

	/**
	 * radioBnDel
	 */
	public void radioBnDel_ActionPerformed(ActionEvent actionEvent) {
		//״̬�ı�
		changeStatus(true);
	}

	/**
	 * ״̬�ı�
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
	 * �رհ�ť���ɼ�
	 */
	public void setBnCloseVisible(boolean bVisible) {
		getBnClose().setVisible(bVisible);
	}

	/**
	 * ���ö�������Դ
	 */
	public void setDsNameForDef(String dsNameForDef) {
		m_dsNameForDef = dsNameForDef;
	}

	/**
	 * ��ö�������Դ
	 */
	public String getDsNameForDef() {
		return m_dsNameForDef;
	}
}  