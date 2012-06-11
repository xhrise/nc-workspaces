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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;

import com.ufida.report.multidimension.model.DimMemberCombinationVO;
import com.ufida.report.multidimension.model.MultiReportFormat;
import com.ufida.report.multidimension.model.MultiReportRowFormat;
import com.ufida.report.multidimension.model.SelDimensionVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.table.format.Format;

/**
 * @author ll
 * 
 */
public class ColumnFormatDialog extends UIDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jPanel = null;

	private JScrollPane jScrollPane = null;

	private JList jListColumns = null;

	private JButton jBtnAdd = null;

	private JButton jBtnDelete = null;

	private JButton jBtnDeleteAll = null;

	private JTextField jTfFontSample = null;

	private JButton jBtnFont = null;

	private JButton jBtnFormat = null;

	private JButton jBtnOK = null;

	private JButton jBtnCancel = null;

	private JPanel jPanelColumns = null;

	private JPanel jPanelFormat = null;

	private JPanel jPanelTitleFormat = null;

	private SelDimensionVO[] m_selDims = null;

	private int m_selIndex = -1;

	private MultiReportFormat m_formatModel = null;

	private ArrayList al_header = null;

	private boolean isRow = false;

	/**
	 * This is the default constructor
	 */
	public ColumnFormatDialog(Container cont) {
		super(cont);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJPanel());
		this.setSize(464, 358);
		this.setTitle(StringResource.getStringResource("ubimultidim0038"));
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
			jPanel.add(getJBtnOK(), null);
			jPanel.add(getJBtnCancel(), null);
			jPanel.add(getJPanelColumns(), null);
			jPanel.add(getJPanelFormat(), null);
			jPanel.add(getJPanelTitleFormat(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(new java.awt.Rectangle(14, 18, 225, 110));
			jScrollPane.setViewportView(getJListColumns());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJListColumns() {
		if (jListColumns == null) {
			jListColumns = new UIList();
		}
		return jListColumns;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnAdd() {
		if (jBtnAdd == null) {
			jBtnAdd =new nc.ui.pub.beans.UIButton();
			jBtnAdd.setText(StringResource.getStringResource("miufo1000080"));
			jBtnAdd.setBounds(new java.awt.Rectangle(284, 23, 75, 22));
			jBtnAdd.addActionListener(this);
		}
		return jBtnAdd;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnDelete() {
		if (jBtnDelete == null) {
			jBtnDelete = new nc.ui.pub.beans.UIButton();
			jBtnDelete.setText(StringResource.getStringResource("miufo1000930"));
			jBtnDelete.setBounds(new java.awt.Rectangle(284, 64, 75, 22));
			jBtnDelete.addActionListener(this);
		}
		return jBtnDelete;
	}

	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnDeleteAll() {
		if (jBtnDeleteAll == null) {
			jBtnDeleteAll = new nc.ui.pub.beans.UIButton();
			jBtnDeleteAll.setText(StringResource.getStringResource("ubimultidim0018"));
			jBtnDeleteAll.setBounds(new java.awt.Rectangle(284, 105, 75, 22));
			jBtnDeleteAll.addActionListener(this);
		}
		return jBtnDeleteAll;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTfFontSample() {
		if (jTfFontSample == null) {
			jTfFontSample = new UITextField();
			jTfFontSample.setBounds(new java.awt.Rectangle(17, 35, 78, 26));
			jTfFontSample.setText(StringResource.getStringResource("ubimultidim0063"));
			jTfFontSample.setEditable(false);
		}
		return jTfFontSample;
	}

	/**
	 * This method initializes jButton3
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnFont() {
		if (jBtnFont == null) {
			jBtnFont = new nc.ui.pub.beans.UIButton();
			jBtnFont.setText(StringResource.getStringResource("miufo1000846"));
			jBtnFont.setBounds(new java.awt.Rectangle(117, 36, 75, 22));
			jBtnFont.addActionListener(this);
		}
		return jBtnFont;
	}

	/**
	 * This method initializes jButton4
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnFormat() {
		if (jBtnFormat == null) {
			jBtnFormat = new nc.ui.pub.beans.UIButton();
			jBtnFormat.setText(StringResource.getStringResource("miufo1001244"));
			jBtnFormat.setBounds(new java.awt.Rectangle(29, 39, 75, 22));
			jBtnFormat.addActionListener(this);
		}
		return jBtnFormat;
	}

	/**
	 * This method initializes jButton5
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnOK() {
		if (jBtnOK == null) {
			jBtnOK = new nc.ui.pub.beans.UIButton();
			jBtnOK.setBounds(101, 275, 75, 22);
			jBtnOK.setText(StringResource.getStringResource("miufo1000094"));
			jBtnOK.addActionListener(this);
		}
		return jBtnOK;
	}

	/**
	 * This method initializes jButton6
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnCancel() {
		if (jBtnCancel == null) {
			jBtnCancel = new nc.ui.pub.beans.UIButton();
			jBtnCancel.setBounds(272, 275, 75, 22);
			jBtnCancel.setText(StringResource.getStringResource("miufo1000274"));
			jBtnCancel.addActionListener(this);
		}
		return jBtnCancel;
	}

	/**
	 * This method initializes jPanelColumns
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelColumns() {
		if (jPanelColumns == null) {
			jPanelColumns = new UIPanel();
			jPanelColumns.setLayout(null);
			jPanelColumns.setBounds(new java.awt.Rectangle(25, 20, 398, 138));
			jPanelColumns.setBorder(BorderFactory.createTitledBorder(StringResource.getStringResource("ubimultidim0062")));
			jPanelColumns.add(getJScrollPane(), null);
			jPanelColumns.add(getJBtnAdd(), null);
			jPanelColumns.add(getJBtnDelete(), null);
			jPanelColumns.add(getJBtnDeleteAll(), null);
		}
		return jPanelColumns;
	}

	/**
	 * This method initializes jPanelFormat
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelFormat() {
		if (jPanelFormat == null) {
			jPanelFormat = new UIPanel();
			jPanelFormat.setLayout(null);
			jPanelFormat.setBounds(new java.awt.Rectangle(281, 174, 142, 80));
			jPanelFormat.setBorder(BorderFactory.createTitledBorder(StringResource.getStringResource("miufo1001692")));
			jPanelFormat.add(getJBtnFormat(), null);
		}
		return jPanelFormat;
	}

	/**
	 * This method initializes jPanelTitleFormat
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelTitleFormat() {
		if (jPanelTitleFormat == null) {
			jPanelTitleFormat = new UIPanel();
			jPanelTitleFormat.setLayout(null);
			jPanelTitleFormat.setBounds(new java.awt.Rectangle(25, 174, 228, 80));
			jPanelTitleFormat.setBorder(BorderFactory.createTitledBorder(StringResource.getStringResource("ubimultidim0054")));
			jPanelTitleFormat.add(getJTfFontSample(), null);
			jPanelTitleFormat.add(getJBtnFont(), null);
		}
		return jPanelTitleFormat;
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getJBtnAdd()) {//增加行/列
			ArrayList<DimMemberCombinationVO> al_headVOs = new ArrayList<DimMemberCombinationVO>();
			for (int i = 0; i < al_header.size(); i++) {
				al_headVOs.add(((MultiReportRowFormat) al_header.get(i)).getKey_RowHeader());
			}
			ColumnFormatAddDialog dlg = new ColumnFormatAddDialog(this, m_selDims, m_selIndex, al_headVOs);
			if (dlg.showModal() == UIDialog.ID_OK) {
				Object[] selFields = dlg.getSelectedFields();
				if (selFields != null && selFields.length > 0) {
					for (int i = 0; i < selFields.length; i++) {
						DimMemberCombinationVO vo = (DimMemberCombinationVO) selFields[i];

						MultiReportRowFormat newRowFormat = new MultiReportRowFormat();
						newRowFormat.setKey_RowHeader(vo);
						newRowFormat.setRow(isRow);
						newRowFormat.setRowFormat(new IufoFormat());
						newRowFormat.setDataFormat(new IufoFormat());

						al_header.add(newRowFormat);
					}
					setListDatas();
					getJListColumns().setSelectedIndex(al_header.size() - 1);

				}
			}
		} else if (e.getSource() == getJBtnDelete()) {//删除
			int index = getJListColumns().getSelectedIndex();
//			getJListColumns().remove(index);
			al_header.remove(index);
			setListDatas();
			if(al_header.size()> index)
				getJListColumns().setSelectedIndex(index);
		} else if (e.getSource() == getJBtnDeleteAll()) {//全部删除
//			getJListColumns().removeAll();
			al_header.clear();
			setListDatas();
		} else if (e.getSource() == getJBtnFont()) {//字体
			MultiReportRowFormat selectFormat = (MultiReportRowFormat) al_header.get(getJListColumns()
					.getSelectedIndex());
			Format headerFormat = ReportFormatDialog.getUserSetFormat(this, selectFormat.getRowFormat(), true);
			if (headerFormat != null)
				selectFormat.setRowFormat(headerFormat);

		} else if (e.getSource() == getJBtnFormat()) {//格式
			MultiReportRowFormat selectFormat = (MultiReportRowFormat) al_header.get(getJListColumns()
					.getSelectedIndex());

			Format dataFormat = ReportFormatDialog.getUserSetFormat(this, selectFormat.getDataFormat(), false);
			if (dataFormat != null)
				selectFormat.setDataFormat(dataFormat);

		} else if (e.getSource() == getJBtnOK()) {//确定
			if (isRow)
				m_formatModel.setAl_rowFormat(al_header);
			else
				m_formatModel.setAl_colFormat(al_header);

			this.closeOK();

		} else if (e.getSource() == getJBtnCancel()) {//取消
			this.closeCancel();
		}

	}

	@SuppressWarnings("unchecked")
	private void setListDatas() {
		Object[] datas = new Object[al_header.size()];
		datas = al_header.toArray(datas);
		getJListColumns().setListData(datas);

	}

	public void setFormatModel(MultiReportFormat model, SelDimensionVO[] dims, boolean isEditRow, int selIndex) {
		m_formatModel = model;
		m_selDims = dims;
		m_selIndex = selIndex;
		isRow = isEditRow;
		ArrayList al_tmp = (isEditRow) ? model.getAl_rowFormat() : model.getAl_colFormat();
		al_header = (ArrayList) al_tmp.clone();

		setListDatas();

	}

	public ArrayList getAl_header() {
		return al_header;
	}
} // @jve:decl-index=0:visual-constraint="43,6"
