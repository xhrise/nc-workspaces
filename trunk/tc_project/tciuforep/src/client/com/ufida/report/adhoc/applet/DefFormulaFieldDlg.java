/*
 * 创建日期 2006-6-28
 */
package com.ufida.report.adhoc.applet;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import com.ufida.report.adhoc.calc.AdhocFormFieldSrv;
import com.ufida.report.adhoc.calc.AdhocFormFileldElement;
import com.ufida.report.adhoc.model.AdhocQueryModel;
import com.ufida.report.adhoc.model.IFunctionFieldElement;
import com.ufida.report.multidimension.calc.CalcLanguageRes;
import com.ufida.report.multidimension.calc.DefFormulaPane;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

/**
 * 公式字段
 * 
 * @author ljhua
 */
public class DefFormulaFieldDlg extends UfoDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JLabel jLabel = null;

	private JTextField txtName = null;

	private DefFormulaPane defFormulaPane = null;

	private JButton btnOK = null;

	private JButton btnCancel = null;

	private AdhocFormFieldSrv m_formSrv = null;

	private String m_strDbFormula = null;

	private String m_strDbColumn = null;

	private int m_iType = 0;

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtName() {
		if (txtName == null) {
			txtName = new UITextField();
			txtName.setBounds(130, 16, 295, 20);
		}
		return txtName;
	}

	/**
	 * This method initializes defFormulaPane
	 * 
	 * @return com.ufida.report.multidimension.calc.DefFormulaPane
	 */
	private DefFormulaPane getDefFormulaPane() {
		if (defFormulaPane == null) {
			defFormulaPane = new DefFormulaPane();
			defFormulaPane.setBounds(14, 49, 417, 274);
		}
		return defFormulaPane;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new nc.ui.pub.beans.UIButton();
			btnOK.setBounds(170, 335, 75, 20);
			btnOK.setText(StringResource.getStringResource("miufopublic246"));
			btnOK.addActionListener(this);
		}
		return btnOK;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new nc.ui.pub.beans.UIButton();
			btnCancel.setBounds(256, 335, 75, 20);
			btnCancel.setText(StringResource.getStringResource("miufopublic247"));
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setResult(UfoDialog.ID_CANCEL);
					close();
				}
			});
		}
		return btnCancel;
	}

	public static void main(String[] args) {
	}

	/**
	 * This is the default constructor
	 */
	public DefFormulaFieldDlg(Container parent, AdhocQueryModel queryModel, String strName, String strUserFormula) {
		super(parent);
		initialize();
		getTxtName().setText(strName);
		initFormPane(queryModel, strUserFormula);
	}
	public String getTitle(){
		return StringResource.getStringResource("mbiadhoc00012");
	}

	private void initFormPane(AdhocQueryModel queryModel, String strUserFormula) {
		m_formSrv = new AdhocFormFieldSrv(queryModel);

		IFunctionFieldElement[] fields = queryModel.getFuncFld(true);

		getDefFormulaPane().initData(new AdhocFormFileldElement(fields), strUserFormula,
				StringResource.getStringResource(AdhocFormulaDlg.NAME_LABEL_FIELD));
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(454, 389);
		this.setContentPane(getJContentPane());

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
			jLabel.setBounds(41, 16, 87, 20);
			jLabel.setText(StringResource.getStringResource(CalcLanguageRes.LABEL_NAME));
			jContentPane.add(jLabel, null);
			jContentPane.add(getTxtName(), null);
			jContentPane.add(getDefFormulaPane(), null);
			jContentPane.add(getBtnOK(), null);
			jContentPane.add(getBtnCancel(), null);
		}
		return jContentPane;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnOK()) {
			String strName = getTxtName().getText();
			if (strName == null || strName.trim().equals("")) {
				JOptionPane.showMessageDialog(this, StringResource.getStringResource(CalcLanguageRes.STR_MSG_NAME));
				getTxtName().requestFocus();
				return;
			}

			String strFormula = getDefFormulaPane().getFormula();
			if (strFormula == null || strFormula.trim().equals("")) {
				// 公式为空等同删除，不做提示
				// JOptionPane.showMessageDialog(this, StringResource
				// .getStringResource(CalcLanguageRes.MSG_FROMULA_NULL));
			} else {
				try {
					Object[] objs = m_formSrv.checkUserFormla(strFormula.trim());
					if (objs != null && objs.length >= 3) {
						m_strDbFormula = (String) objs[0];
						m_strDbColumn = (String) objs[1];
						if (objs[2] != null)
							m_iType = ((Integer) objs[2]).intValue();

					}
				} catch (Exception e1) {
					Logger.error("checkUserFormula", e1);
					JOptionPane.showMessageDialog(this, e1.getMessage());
					return;
				}
			}
			setResult(UfoDialog.ID_OK);
			close();
		}
	}

	public String getDbFormula() {
		return m_strDbFormula;
	}

	public int getFormulaType() {
		return m_iType;
	}

	public String getDbColumn() {
		return m_strDbColumn;
	}

	public String getName() {
		return getTxtName().getText().trim();
	}
} // @jve:decl-index=0:visual-constraint="10,10"
