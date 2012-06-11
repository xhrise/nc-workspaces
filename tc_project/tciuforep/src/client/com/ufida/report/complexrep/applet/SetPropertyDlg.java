/*
 * 创建日期 2006-7-13
 */
package com.ufida.report.complexrep.applet;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;
import nc.vo.bi.report.manager.ReportResource;

import com.ufida.report.rep.model.BiModelProperty;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

/**
 * @author ljhua
 */
public class SetPropertyDlg extends UfoDialog implements ActionListener {

	private javax.swing.JPanel jContentPane = null;

	private JLabel jLabel = null;

	private JTextField jTextField = null;

	private JCheckBox ckbRowCol = null;

	private JCheckBox ckbPageDim = null;

	private JCheckBox ckbDataLink = null;

	private JButton btnOK = null;

	private JButton btnCancel = null;

	private BiModelProperty m_prop = null;

	private int m_repType = -1;

	/**
	 * @param parent
	 */
	public SetPropertyDlg(Container parent, BiModelProperty prop, int iType) {
		super(parent);
		m_repType = iType;

		if (prop != null)
			m_prop = (BiModelProperty) prop.clone();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */

	private void initialize() {
		this.setSize(288, 220);
		this.setContentPane(getJContentPane());
		if (m_prop != null) {
			getCkbDataLink().setSelected(m_prop.isDataLink());
			getCkbRowCol().setSelected(m_prop.isDisplayRowCol());
			getCkbPageDim().setSelected(m_prop.isDispalyPageDim());
			getJTextField().setText(m_prop.getDispalyName());
		} else {
			getCkbDataLink().setSelected(true);
			getCkbRowCol().setSelected(false);
			getCkbPageDim().setSelected(true);
			getJTextField().setText("");
		}
		if (m_repType == ReportResource.INT_REPORT_CHART) {
			getCkbRowCol().setEnabled(false);
		} else
			getCkbRowCol().setEnabled(true);

	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel = new nc.ui.pub.beans.UILabel();
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			jLabel.setBounds(14, 11, 92, 24);
			jLabel.setText(StringResource.getStringResource("uibicomplex0022"));//报表显示名称
			jContentPane.add(jLabel, null);
			jContentPane.add(getJTextField(), null);
			jContentPane.add(getCkbRowCol(), null);
			jContentPane.add(getCkbPageDim(), null);
			jContentPane.add(getCkbDataLink(), null);
			jContentPane.add(getBtnOK(), null);
			jContentPane.add(getBtnCancel(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new UITextField();
			jTextField.setBounds(114, 11, 160, 24);
		}
		return jTextField;
	}

	/**
	 * This method initializes ckbRowCol
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getCkbRowCol() {
		if (ckbRowCol == null) {
			ckbRowCol = new UICheckBox();
			ckbRowCol.setBounds(14, 54, 257, 22);
			ckbRowCol.setText(StringResource.getStringResource("uibicomplex0023"));//显示行列标识
		}
		return ckbRowCol;
	}

	/**
	 * This method initializes ckbPageDim
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getCkbPageDim() {
		if (ckbPageDim == null) {
			ckbPageDim = new UICheckBox();
			ckbPageDim.setBounds(14, 81, 257, 27);
			ckbPageDim.setText(StringResource.getStringResource("uibicomplex0024"));//显示页维度
		}
		return ckbPageDim;
	}

	/**
	 * This method initializes ckbDataLink
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getCkbDataLink() {
		if (ckbDataLink == null) {
			ckbDataLink = new UICheckBox();
			ckbDataLink.setBounds(14, 114, 257, 24);
			ckbDataLink.setText(StringResource.getStringResource("uibicomplex0025"));//参与联动
		}
		return ckbDataLink;
	}

	/**
	 * This method initializes btnOK
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new nc.ui.pub.beans.UIButton();
			btnOK.setText(StringResource.getStringResource("miufopublic246"));
			btnOK.setBounds(43, 155, 75, 22);
			btnOK.addActionListener(this);

		}
		return btnOK;
	}

	/**
	 * This method initializes btnCancel
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new nc.ui.pub.beans.UIButton();
			btnCancel.setText(StringResource
					.getStringResource("miufopublic247"));
			btnCancel.setBounds(144, 155, 75, 22);
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setResult(UfoDialog.ID_CANCEL);
					close();
				}
			});
		}
		return btnCancel;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnOK()) {
			if (m_prop == null)
				m_prop = new BiModelProperty();
			m_prop.setDispalyName(getJTextField().getText());
			m_prop.setDataLink(getCkbDataLink().isSelected());
			m_prop.setDispalyPageDim(getCkbPageDim().isSelected());
			m_prop.setDisplayRowCol(getCkbRowCol().isSelected());

			setResult(UfoDialog.ID_OK);
			close();
		}

	}

	public BiModelProperty getProperty() {
		return m_prop;
	}

} //  @jve:decl-index=0:visual-constraint="10,10"
