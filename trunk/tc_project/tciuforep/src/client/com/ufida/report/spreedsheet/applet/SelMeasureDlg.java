package com.ufida.report.spreedsheet.applet;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import nc.itf.iufo.freequery.IMember;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;

import com.ufida.report.adhoc.model.PageDimField;
import com.ufida.report.spreedsheet.model.SpreadCellPropertyVO;
import com.ufida.report.spreedsheet.model.SpreadSheetModel;
import com.ufsoft.iufo.resource.StringResource;

public class SelMeasureDlg extends UIDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton jButtonOK = null;

	private JButton jButtonCancel = null;

	private JPanel jPanelMain = null;

	private JPanel m_propertyPanel;

	private ArrayList al_fields = new ArrayList();

	private SpreadCellPropertyVO m_oldCell = null;

	public SelMeasureDlg(Container cont) {
		super(cont);
		initialize();
	}

	private void initialize() {
		this.setSize(new java.awt.Dimension(380, 330));
		this.setContentPane(getJPanelMain());
		
		setTitle(StringResource.getStringResource("ubimultidim0007"));

	}

	/**
	 * This method initializes jPanelMain
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelMain() {
		if (jPanelMain == null) {
			jPanelMain = new UIPanel();
			jPanelMain.setLayout(null);
			jPanelMain.add(getCellPropertyPanel(), null);
			jPanelMain.add(getJButtonOK(), null);
			jPanelMain.add(getJButtonCancel(), null);
		}
		return jPanelMain;
	}

	private JPanel getCellPropertyPanel() {
		if (m_propertyPanel == null) {
			m_propertyPanel = new UIPanel();
			m_propertyPanel.setBounds(new java.awt.Rectangle(15, 10, 335, 240));
		}
		return m_propertyPanel;
	}

	/**
	 * This method initializes jButtonOK
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new nc.ui.pub.beans.UIButton();
			jButtonOK.setBounds(new java.awt.Rectangle(120, 260, 75, 22));
			jButtonOK.setText(StringResource.getStringResource("miufo1000064"));
			jButtonOK.addActionListener(this);
		}
		return jButtonOK;
	}

	/**
	 * This method initializes jButtonCancel
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new nc.ui.pub.beans.UIButton();
			jButtonCancel.setBounds(new java.awt.Rectangle(260, 260, 75, 22));
			jButtonCancel.setText(StringResource.getStringResource("miufo1000274"));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == getJButtonOK()) {
			closeOK();
		} else if (e.getSource() == getJButtonCancel()) {
			closeCancel();
		}

	}

	public SpreadCellPropertyVO getCellProperty() {
		if (al_fields.size() == 0)
			return m_oldCell;

		if (m_oldCell.getMembers().length != al_fields.size())
			return m_oldCell;// 不应该出现此情况
		IMember[] mems = m_oldCell.getMembers();
		for (int i = 0; i < mems.length; i++) {
			PageDimField field = (PageDimField) al_fields.get(i);
			IMember selMem = field.getSelectedValue();
			mems[i] = selMem;
		}
		m_oldCell.setMembers(mems);
		return m_oldCell;

	}

	/**
	 * @param cellVO
	 * @param spreadModel
	 */
	public void setCellDimProperty(SpreadCellPropertyVO cellVO, SpreadSheetModel spreadModel) {
		m_oldCell = cellVO;

		getCellPropertyPanel().removeAll();
		al_fields = SpreadCellPropertyDlg.createDimRef(getCellPropertyPanel(), cellVO, spreadModel.getQueryCache(),
				spreadModel.getPageDimFields(), false);
	}

}
