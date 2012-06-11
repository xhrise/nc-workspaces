/*
 * 创建日期 2006-6-28
 */
package com.ufida.report.adhoc.applet;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIPanel;

import com.ufida.report.adhoc.calc.AdhocCalcService;
import com.ufida.report.adhoc.model.AdhocModel;
import com.ufida.report.adhoc.model.AdhocQueryModel;
import com.ufida.report.adhoc.model.IFunctionFieldElement;
import com.ufida.report.multidimension.calc.DefFormulaPane;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.script.exception.ParseException;

/**
 * 组头、表头公式定义界面
 * 
 * @author ljhua
 */
public class AdhocFormulaDlg extends UfoDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String NAME_LABEL_FIELD = "uibiadhoccal001";// 字段

	private String m_strDbFormula = null;

	private javax.swing.JPanel jContentPane = null;

	private DefFormulaPane defFormulaPane = null;

	private JButton btnOK = null;

	private JButton btnCancel = null;

	private AdhocCalcService m_adhocCalcSrv = null;

	/**
	 * This method initializes defFormulaPane
	 * 
	 * @return com.ufida.report.multidimension.calc.DefFormulaPane
	 */
	private DefFormulaPane getDefFormulaPane() {
		if (defFormulaPane == null) {
			defFormulaPane = new DefFormulaPane();
			defFormulaPane.setBounds(0, 0, 430, 274);
		}
		return defFormulaPane;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new nc.ui.pub.beans.UIButton();
			btnOK.setBounds(171, 303, 75, 22);
			btnOK.setText(StringResource.getStringResource("miufopublic246"));
			btnOK.addActionListener(this);
		}
		return btnOK;
	}

	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new nc.ui.pub.beans.UIButton();
			btnCancel.setBounds(299, 303, 75, 22);
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
	public AdhocFormulaDlg(Container parent, AdhocModel adModel, String strDbFormula) {
		super(parent);
		initialize();
		initFormPane(adModel, strDbFormula);

	}
    public String getTitle() {
    	return StringResource.getStringResource("mbiadhoc00011");
    }
	private void initFormPane(AdhocModel adModel, String strDbFormula) {
		m_adhocCalcSrv = new AdhocCalcService(adModel);

		String strUserFormula = m_adhocCalcSrv.getUserFormula(strDbFormula);

//		String strQueryId = adModel.getQueryIDs()[0];
//
//		IFunctionFieldElement[] fields = AdhocPublic.getFunFldFromQueryModel(strQueryId);
		AdhocQueryModel query = adModel.getDataCenter().getCurrQuery();
		IFunctionFieldElement[] fields = null;
		if(query != null)
			fields = query.getFuncFld(true);
		IFunctionFieldElement[] calcFields = adModel.getCalcColumns();
		int iLen = 0;
		int iFieldsLen = 0;
		if (fields != null) {
			iLen += fields.length;
			iFieldsLen = iLen;
		}
		if (calcFields != null)
			iLen += calcFields.length;
		IFunctionFieldElement[] allFields = new IFunctionFieldElement[iLen];
		if (fields != null && fields.length > 0)
			System.arraycopy(fields, 0, allFields, 0, fields.length);
		if (calcFields != null && calcFields.length > 0)
			System.arraycopy(calcFields, 0, allFields, iFieldsLen, calcFields.length);

		getDefFormulaPane().initData(new AdhocFormulaElement(allFields), strUserFormula,
				StringResource.getStringResource(NAME_LABEL_FIELD));
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(435, 384);
		this.setContentPane(getJContentPane());

	}

	public String getDbFormula() {
		return m_strDbFormula;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getDefFormulaPane(), null);
			jContentPane.add(getBtnOK(), null);
			jContentPane.add(getBtnCancel(), null);
		}
		return jContentPane;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnOK()) {
			String strForm = getDefFormulaPane().getFormula();
			if (strForm == null || strForm.trim().equals("")) {
				// 公式为空等同删除，不做提示
				// JOptionPane.showMessageDialog(this,StringResource.getStringResource(CalcLanguageRes.MSG_FROMULA_NULL));
			} else {
				try {
					m_strDbFormula = m_adhocCalcSrv.checkUserFormula(strForm);
				} catch (ParseException e1) {
					Logger.error("checkUserFormula", e1);
					JOptionPane.showMessageDialog(this, e1.getMessage());
					return;
				}
			}
			setResult(UfoDialog.ID_OK);
			close();
		}
	}
} // @jve:decl-index=0:visual-constraint="10,10"
