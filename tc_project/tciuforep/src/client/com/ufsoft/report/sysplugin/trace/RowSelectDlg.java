package com.ufsoft.report.sysplugin.trace;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JButton;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.vo.pub.lang.UFDouble;

import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufida.dataset.metadata.Field;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.header.Header;
import com.ufsoft.table.header.HeaderModel;

/**
 * 多行数据选择的对话框
 */
public class RowSelectDlg extends UfoDialog implements ActionListener {
	private UfoReport m_report = null;

	private static final long serialVersionUID = 1L;

	private UIButton m_btnOK = null;
	private UIButton m_btnCancel = null;
	private int m_row = -1;
	private ContextVO m_context = null;

	public RowSelectDlg(Container parent, String title, ContextVO context) {
		super(parent);
		setTitle(title);
		m_context = context;
		initUI();
	}

	private void initUI() {
		this.setSize(850, 700);
		UIPanel mainPanel = new UIPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(getTablePane(), BorderLayout.CENTER);
		UIPanel btnPanel = new UIPanel();
		btnPanel.add(getOkButton());
		UILabel lbl = new UILabel();
		lbl.setPreferredSize(new Dimension(100, 20));
		btnPanel.add(lbl);
		btnPanel.add(getCancelButton());
		mainPanel.add(btnPanel, BorderLayout.SOUTH);
		this.setContentPane(mainPanel);
	}

	private JButton getOkButton() {
		if (m_btnOK == null) {

			m_btnOK = new nc.ui.pub.beans.UIButton();
			m_btnOK.setName("OkButton");
			m_btnOK.setText(MultiLang.getString("uiuforep0000782"));// 确定
			m_btnOK.addActionListener(this);
		}
		return m_btnOK;
	}

	private JButton getCancelButton() {
		if (m_btnCancel == null) {

			m_btnCancel = new nc.ui.pub.beans.UIButton();
			m_btnCancel.setName("cancelButton");
			m_btnCancel.setText(MultiLang.getString("uiuforep0000739"));// "取消");//
			m_btnCancel.addActionListener(this);
		}
		return m_btnCancel;
	}

	private UFOTable getTablePane() {
		if (m_report == null) {
			UFOTable table = UFOTable.createFiniteTable(1, 1);
			m_report = new UfoReport(UfoReport.OPERATION_INPUT, m_context, table.getCellsModel());
		}
		return m_report.getTable();
	}

	private CellsModel getCellsModel() {
		return getTablePane().getCellsModel();
	}

	@SuppressWarnings("unchecked")
	public void setDatas(Field[] flds, Object[][] rowDatas, String fldName) {
		// 更新表格内容
		if (flds == null || flds.length == 0 || rowDatas == null || rowDatas.length == 0)
			return;

		int row = rowDatas.length;
		int col = 0;
		int fldCol = 0;
		for (int i = 0; i < flds.length; i++) {
			if (!flds[i].isEdit())
				continue;
			if (flds[i].getFldname().equals(fldName))
				fldCol = col;
			col++;
		}
		Cell[][] cells = new Cell[row][col];
		// 设置列标题
		HeaderModel colHeader = getCellsModel().getColumnHeaderModel();
		int cc = colHeader.getCount();
		if (cc <= flds.length) {
			colHeader.addHeader(0, col - cc);
		}
		HeaderModel rowHeader = getCellsModel().getRowHeaderModel();
		cc = rowHeader.getCount();
		if (cc <= row) {
			rowHeader.addHeader(0, row - cc);
		}

		int cIndex = 0;
		for (int i = 0; i < flds.length; i++) {
			if (!flds[i].isEdit())
				continue;
			Header header = colHeader.getHeader(cIndex);
			header.setValue(flds[i].getCaption());
			cIndex++;
		}

		// 填充数据
		Object data = null;
		for (int i = 0; i < rowDatas.length; i++) {
			cIndex = 0;
			for (int j = 0; j < flds.length; j++) {
				CellPosition pos = CellPosition.getInstance(i, cIndex);
				data = rowDatas[i][j];
				if (!flds[j].isEdit())
					continue;
				Cell cell = new Cell();
				cell.setValue(convert2Double(data));
				if (isInteger(flds[j].getDataType())) {
					IufoFormat format = ((IufoFormat) getCellsModel().getCellFormat(pos));
					if (format == null) {
						format = new IufoFormat();
						format.setDateType(TableConstant.CELLTYPE_NUMBER);
						cell.setFormat(format);
					}
					((IufoFormat) cell.getFormat()).setDecimalDigits(0);
				}
				cells[i][cIndex] = cell;
				cIndex++;
			}
		}
		getCellsModel().setCells(CellPosition.getInstance(0, 0), cells);
		getCellsModel().getSelectModel().setAnchorCell(CellPosition.getInstance(0, fldCol));
	}

	private static boolean isInteger(int type) {
		if (type == DataTypeConstant.INT || type == DataTypeConstant.SHORT)
			return true;
		return false;
	}

	private static Object convert2Double(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Double)
			return obj;
		else if (obj instanceof UFDouble)
			return new Double(((UFDouble) obj).doubleValue());
		else if (obj instanceof Integer)
			return new Double(((Integer) obj).doubleValue());
		else if (obj instanceof BigDecimal)
			return new Double(((BigDecimal) obj).doubleValue());
		else if (obj instanceof Short)
			return new Double(((Short) obj).doubleValue());
		return obj;
	}

	public int getSelectRow() {
		return m_row;
	}

	private String checkRowSelect() {
		m_row = -1;
		AreaPosition area = getTablePane().getCellsModel().getSelectModel().getSelectedArea();
		if (area == null || area.getHeigth() > 1)
			return "请选择要追踪的数据";
		m_row = area.getStart().getRow();
		return null;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getOkButton()) {
			String msg = checkRowSelect();
			if (msg != null) {
				MessageDialog.showHintDlg(this, null, msg);
				return;
			}
			setResult(UfoDialog.ID_OK);
			this.close();
			return;
		} else if (e.getSource() == getCancelButton()) {
			setResult(UfoDialog.ID_CANCEL);
			this.close();
			return;
		}

	}

}
