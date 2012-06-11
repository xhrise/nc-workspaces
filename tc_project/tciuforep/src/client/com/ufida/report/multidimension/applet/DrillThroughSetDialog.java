/*
 * Created on 2005-6-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.multidimension.applet;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.report.manager.ReportVO;

import com.ufida.report.multidimension.model.DrillThroughSet;
import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.multidimension.model.MultiReportSrvUtil;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufida.report.multidimension.model.SelDimensionVO;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author ll
 * 
 * 穿透设置对话框
 */
public class DrillThroughSetDialog extends nc.ui.pub.beans.UIDialog implements
		ActionListener, ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private class DrillThroughSetTableModel extends DefaultTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * @i18n ubidim0008=维度
		 */
		private static final String COLNAME_DIMDEF_NAME_RESID = "ubidim0008";

		/**
		 * @i18n mbimulti00021=目标报表
		 */
		private static final String COLNAME_REPORT_NAME_RESID = "mbimulti00021";

		private static final String COLNAME_DIMDEF_ID = "pk_dim";

		private static final String COLNAME_REPORT_ID = "pk_report";

		private String[] m_colNames = new String[] { StringResource.getStringResource(COLNAME_DIMDEF_NAME_RESID),
				StringResource.getStringResource(COLNAME_REPORT_NAME_RESID), COLNAME_DIMDEF_ID, COLNAME_REPORT_ID };

		public DrillThroughSetTableModel() {
			super();
			initColNames();

		}

		private void initColNames() {
			setColumnIdentifiers(m_colNames);
		}

		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@SuppressWarnings("unchecked")
		public Class getColumnClass(int columnIndex) {
			return String.class;
		}

		public void setValueAt(Object value, int row, int column) {
			super.setValueAt(value, row, column);
			if (column == 3) {//reportID
				String reportName = null;
				if (value != null && m_reportTable.containsKey(value)) {
					reportName = m_reportTable.get(value).toString();
				}
				setValueAt(reportName, row, 1);
			}
		}

	}

	private JPanel jPanel = null;

	private JTable jTable = null;

	private DrillThroughSetTableModel m_tableMode = null;

	private JScrollPane jScrollPane = null;

	private JButton jButtonOK = null;

	private JButton jButtonCancel = null;

	private JButton jButtonAdd = null;

	private JButton jButtonDelete = null;

	private ChooseReportDlg m_reportDlg = null;

	//对维度设置模型的引用，用于构建所有待设置维度
	private SelDimModel m_dimModel = null;

//	private String m_userID = null;

	//动态构建的所有可设置维度，包括数据单元
	private DimensionVO[] m_defVOs = null;

	//报表缓存，用于主键和名称的对照
	private Hashtable<String, String> m_reportTable = new Hashtable<String, String>();

	private ReportVO[] m_allReports = null;

	private DrillThroughSet m_drillThroughSetModel = null;

	public DrillThroughSetDialog(Container cont, SelDimModel selDimModel, String userID) {
		super(cont);
		m_dimModel = selDimModel;
//		m_userID = userID;
		initialize();
		initPanel();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJPanel());
		this.setSize(528, 335);

	}

	private void initPanel() {
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new UIPanel();
			jPanel.setLayout(null);
			jPanel.add(getJScrollPane(), null);
			jPanel.add(getJButtonOK(), null);
			jPanel.add(getJButtonCancel(), null);
			jPanel.add(getJButtonAdd(), null);
			jPanel.add(getJButtonDelete(), null);

		}
		return jPanel;
	}

	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new UITable();

			jTable.setModel(getTableModel());
			int[] preWidths = new int[] { 100, 150 ,0,0};
			for (int i = 0; i < preWidths.length; i++) {
				jTable.getColumn(jTable.getColumnName(i)).setPreferredWidth(
						preWidths[i]);
			}

			jTable.removeColumn(jTable.getColumn(jTable.getColumnName(3)));
			jTable.removeColumn(jTable.getColumn(jTable.getColumnName(2)));
			
			jTable.getSelectionModel().addListSelectionListener(this);
		}
		return jTable;
	}

	private DrillThroughSetTableModel getTableModel() {
		if (m_tableMode == null) {
			m_tableMode = new DrillThroughSetTableModel();
		}
		return m_tableMode;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(4, 6, 400, 230);
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new nc.ui.pub.beans.UIButton();
			jButtonOK.setBounds(121, 267, 75, 22);
			jButtonOK.setText(StringResource.getStringResource("miufo1000094"));
			jButtonOK.addActionListener(this);
		}
		return jButtonOK;
	}

	private JButton getJButtonAdd() {
		if (jButtonAdd == null) {
			jButtonAdd = new nc.ui.pub.beans.UIButton();
			jButtonAdd.setBounds(420, 100, 75, 22);
			jButtonAdd.setText(StringResource.getStringResource("miufo1000080"));
			jButtonAdd.addActionListener(this);
		}
		return jButtonAdd;
	}

	private JButton getJButtonDelete() {
		if (jButtonDelete == null) {
			jButtonDelete = new nc.ui.pub.beans.UIButton();
			jButtonDelete.setBounds(420, 160, 75, 22);
			jButtonDelete.setText(StringResource.getStringResource("miufo1000930"));
			jButtonDelete.addActionListener(this);
		}
		return jButtonDelete;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new nc.ui.pub.beans.UIButton();
			jButtonCancel.setBounds(292, 266, 75, 25);
			jButtonCancel.setText(StringResource.getStringResource("miufo1000274"));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	/**
	 * @return Returns the drillThroughSetModel.
	 */
	public DrillThroughSet getDrillThroughSet() {
		for (int i = 0; i < getTableModel().getRowCount(); i++) {
			String pk_dim = getTableModel().getValueAt(i, 2).toString();
			Object value = getTableModel().getValueAt(i, 3);
			if (value != null) {
				m_drillThroughSetModel.setDrillReport(pk_dim, value.toString());
			} else {
				m_drillThroughSetModel.removeDrillReport(pk_dim);
			}
		}
		return m_drillThroughSetModel;
	}

	/**
	 * @param drillThroughSet
	 *            The drillThroughSetModel to set.
	 */
	public boolean setDrillThroughSet(
			DrillThroughSet drillThroughSet) {
		if (drillThroughSet == null) {
			m_drillThroughSetModel = new DrillThroughSet();
		} else {
			m_drillThroughSetModel = (DrillThroughSet) drillThroughSet
					.clone();
		}

		if (refreshDimCache()) {

			refreshTableModel();
			return true;
		}
		return false;
	}

	/**
	 * @i18n mbimulti00022=数据单元
	 */
	private boolean refreshDimCache() {
		Vector<DimensionVO> vec = new Vector<DimensionVO>();
		SelDimensionVO[] dims = m_dimModel.getSelDimVOs(IMultiDimConst.POS_ROW);
		if (dims != null) {
			for (int i = 0; i < dims.length; i++) {
				vec.addElement(dims[i].getDimDef());
			}
		}
		dims = m_dimModel.getSelDimVOs(IMultiDimConst.POS_COLUMN);
		if (dims != null) {
			for (int i = 0; i < dims.length; i++) {
				vec.addElement(dims[i].getDimDef());
			}
		}
		DimensionVO dataDim = new DimensionVO();
		dataDim.setDimID(IMultiDimConst.PK_DATA_DRILLTHROUGH);
		dataDim.setDimname(StringResource.getStringResource("mbimulti00022"));
		vec.addElement(dataDim);

		m_defVOs = new DimensionVO[vec.size()];
		vec.copyInto(m_defVOs);

		m_reportTable.clear();

		try {
			m_allReports = MultiReportSrvUtil.getAllReports(false);
			if (m_allReports != null) {
				for (int i = 0; i < m_allReports.length; i++) {
					ReportVO report = m_allReports[i];
					m_reportTable.put(report.getPrimaryKey(), report
							.getReportname() == null ? report.getPrimaryKey()
							: report.getReportname());
				}
			} else
				m_allReports = null;
		} catch (Exception ex) {
			//报表读取错误，可以忽略
		}
		return !(m_allReports == null);
	}

	private void refreshTableModel() {
		for (int i = getTableModel().getRowCount() - 1; i >= 0; i--) {
			getTableModel().removeRow(i);
		}
		for (int i = 0; i < m_defVOs.length; i++) {
			String pk_report = m_drillThroughSetModel
					.getDrillThroughReport(m_defVOs[i].getPrimaryKey());
			getTableModel().addRow(
					new Object[] { m_defVOs[i].getDimname(),
							getReportName(pk_report),
							m_defVOs[i].getPrimaryKey(), pk_report });

		}

	}

	private String getReportName(String reportID) {
		if (reportID == null)
			return "";
		String reportName = null;
		if (m_reportTable.containsKey(reportID)) {
			reportName = m_reportTable.get(reportID).toString();
		}
		return reportName == null ? reportID : reportName;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getJButtonCancel()) {
			super.closeCancel();
		}
		if (e.getSource() == getJButtonOK()) {
			super.closeOK();
		}
		if (e.getSource() == getJButtonAdd()) {
			int index = getJTable().getSelectedRow();

			getReportDlg().setAllReports(m_allReports);
			if (getReportDlg().showModal() == UIDialog.ID_OK) {
				ReportVO selReport = getReportDlg().getSelectedReport();
				m_reportTable.put(selReport.getPrimaryKey(), (selReport
						.getReportname() == null) ? selReport.getPrimaryKey()
						: selReport.getReportname());

				getTableModel().setValueAt(selReport.getPrimaryKey(), index, 3);//reportID
			}
		}
		if (e.getSource() == getJButtonDelete()) {
			int index = getJTable().getSelectedRow();
			getTableModel().setValueAt(null, index, 3);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		int index = getJTable().getSelectedRow();
		if (index < 0 || index >= getTableModel().getRowCount()) {
			getJButtonAdd().setEnabled(false);
			getJButtonDelete().setEnabled(false);
		} else {
			Object reportID = getTableModel().getValueAt(index, 3);
			boolean isNull = true;
			if (reportID != null && reportID.toString().length() > 0)
				isNull = false;

			getJButtonAdd().setEnabled(isNull);
			getJButtonDelete().setEnabled(!isNull);
		}

	}

	private ChooseReportDlg getReportDlg() {
		if (m_reportDlg == null) {
			m_reportDlg = new ChooseReportDlg(this);
		}
		return m_reportDlg;
	}
} //  @jve:decl-index=0:visual-constraint="10,10"
