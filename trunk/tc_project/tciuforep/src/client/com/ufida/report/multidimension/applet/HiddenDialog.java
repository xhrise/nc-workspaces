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
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIScrollPane;

import com.ufida.report.multidimension.model.HiddenDescriptor;
import com.ufida.report.multidimension.model.IAnalyzerSet;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufsoft.iufo.data.IMetaData;
import com.ufsoft.iufo.resource.StringResource;
import java.awt.Rectangle;

/**
 * @author ll
 * 
 * 隐藏行列设置对话框
 */
public class HiddenDialog extends nc.ui.pub.beans.UIDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jPanel = null;

	private JRadioButton m_jRadioBtnMember = null;

	private JRadioButton m_jRadioBtnFormula = null;

	private JList jList = null;

	private JCheckBox m_checkBoxCombine = null;

	private JButton m_btnOK = null;

	private JButton m_btnCancel = null;

	private JScrollPane jScrollPane = null;

	private HiddenDescriptor m_hiddenDesc = null;

	private IMetaData[] m_members = null;

	private IMetaData[] m_memberCombines = null;

	public HiddenDialog(Container cont, SelDimModel dimModel) {
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
		this.setSize(502, 367);

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
			jPanel.add(getJRadioButtonMember(), null);
			jPanel.add(getJRadioButtonFormula(), null);
			jPanel.add(getJCheckBoxCombine(), null);
			jPanel.add(getJButtonOK(), null);
			jPanel.add(getJButtonCancel(), null);
			jPanel.add(getJScrollPane(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRadioButtonMember() {
		if (m_jRadioBtnMember == null) {
			m_jRadioBtnMember = new UIRadioButton();
			m_jRadioBtnMember.setBounds(60, 28, 131, 26);
			m_jRadioBtnMember.setText(StringResource.getStringResource("ubimultidim0026"));
			m_jRadioBtnMember.addActionListener(this);
		}
		return m_jRadioBtnMember;
	}

	/**
	 * This method initializes jRadioButton1
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRadioButtonFormula() {
		if (m_jRadioBtnFormula == null) {
			m_jRadioBtnFormula = new UIRadioButton();
			m_jRadioBtnFormula.setBounds(237, 30, 170, 21);
			m_jRadioBtnFormula.setText(StringResource.getStringResource("ubimultidim0027"));
			m_jRadioBtnFormula.addActionListener(this);
		}
		return m_jRadioBtnFormula;
	}

	/**
	 * This method initializes jList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJList() {
		if (jList == null) {
			jList = new UIList();
			jList.setBounds(new Rectangle(52, 57, 397, 204));
		}
		return jList;
	}

	/**
	 * This method initializes jCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBoxCombine() {
		if (m_checkBoxCombine == null) {
			m_checkBoxCombine = new UICheckBox();
			m_checkBoxCombine.setBounds(53, 272, 85, 21);
			m_checkBoxCombine.setText(StringResource.getStringResource("ubimultidim0030"));
			m_checkBoxCombine.addActionListener(this);
		}
		return m_checkBoxCombine;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonOK() {
		if (m_btnOK == null) {
			m_btnOK = new nc.ui.pub.beans.UIButton();
			m_btnOK.setBounds(173, 299, 75, 22);
			m_btnOK.setText(StringResource.getStringResource("miufo1000094"));
			m_btnOK.addActionListener(this);
		}
		return m_btnOK;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCancel() {
		if (m_btnCancel == null) {
			m_btnCancel = new nc.ui.pub.beans.UIButton();
			m_btnCancel.setBounds(344, 294, 75, 22);
			m_btnCancel.setText(StringResource.getStringResource("miufo1000274"));
			m_btnCancel.addActionListener(this);
		}
		return m_btnCancel;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(50, 59, 398, 203);
			jScrollPane.setViewportView(getJList());
		}
		return jScrollPane;
	}

	public HiddenDescriptor getHiddenDesc() {
		return m_hiddenDesc;
	}

	/**
	 * 
	 * @param AnalyzerHiddenVO
	 * 
	 */
	public void setHiddenDesc(HiddenDescriptor desc) {
		this.m_hiddenDesc = desc;

		// 设置界面显示
		if (desc != null) {
			if (desc.getHiddenType() == IAnalyzerSet.INT_HIDDEN_TYPE_MEMBER) {
				getJRadioButtonMember().setSelected(true);
				getJRadioButtonFormula().setSelected(false);
				getJList().setEnabled(true);
				getJCheckBoxCombine().setEnabled(true);

			} else if (desc.getHiddenType() == IAnalyzerSet.INT_HIDDEN_TYPE_FORMULA) {
				getJRadioButtonFormula().setSelected(true);
				getJRadioButtonMember().setSelected(false);
				getJList().setEnabled(false);
				getJCheckBoxCombine().setEnabled(false);
			}

			getJCheckBoxCombine().setSelected(desc.isCombine());
			IMetaData[] selFields = desc.getSelHiddenMember();
			IMetaData[] allFields = desc.isCombine() ? m_memberCombines : m_members;
			if (allFields != null) {
				getJList().setListData(allFields);
				if (selFields != null) {
					int[] indexes = new int[selFields.length];
					for (int i = 0; i < allFields.length; i++) {
						for (int j = 0; j < selFields.length; j++) {
							if (allFields[i].getID().equals(selFields[j].getID()))
								indexes[j] = i;
						}
					}
					Arrays.sort(indexes);
					getJList().setSelectedIndices(indexes);
				} else {
					getJList().setSelectedIndex(0);
				}
			}
		}
	}

	public void setFileds(IMetaData[] members, IMetaData[] memberCombines) {
		m_members = members;
		m_memberCombines = memberCombines;

		getJCheckBoxCombine().setSelected(false);
		getJList().setListData(m_members);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getJButtonOK()) {
			// set HiddenDesc from UI
			String msg = changeHiddenDescFromUI();
			if (msg != null) {
				MessageDialog.showErrorDlg(getParent(), null, msg);
			} else {
				closeOK();
			}
		}
		if (e.getSource() == getJButtonCancel()) {
			closeCancel();
		}
		if (e.getSource() == getJCheckBoxCombine()) {
			// change list
			if (getJCheckBoxCombine().isSelected()) {
				getJList().setListData(m_memberCombines);
			} else {
				getJList().setListData(m_members);
			}
			return;
		}
		if (e.getSource() == getJRadioButtonMember()) {
			if (!getJRadioButtonMember().isSelected()) {
				getJRadioButtonMember().setSelected(true);
			}
			getJRadioButtonFormula().setSelected(false);
			getJList().setEnabled(true);
			getJCheckBoxCombine().setEnabled(true);

		} else if (e.getSource() == getJRadioButtonFormula()) {
			if (!getJRadioButtonFormula().isSelected()) {
				getJRadioButtonFormula().setSelected(true);
			}
			getJRadioButtonMember().setSelected(false);
			getJList().setEnabled(false);
			getJCheckBoxCombine().setEnabled(false);
		}

	}

	/**
	 * @i18n mbimulti00018=请选择要隐藏的成员
	 */
	private String changeHiddenDescFromUI() {
		if (m_hiddenDesc == null) {
			m_hiddenDesc = new HiddenDescriptor();
		}

		//
		if (getJRadioButtonMember().isSelected())
			m_hiddenDesc.setHiddenType(IAnalyzerSet.INT_HIDDEN_TYPE_MEMBER);
		else if(getJRadioButtonFormula().isSelected())
			m_hiddenDesc.setHiddenType(IAnalyzerSet.INT_HIDDEN_TYPE_FORMULA);
		boolean isCombine = getJCheckBoxCombine().isSelected();
		m_hiddenDesc.setIsCombine(isCombine);

		int[] indexes = getJList().getSelectedIndices();
		if (indexes == null || indexes.length == 0) {
			// error msg
			return StringResource.getStringResource("mbimulti00018");
		}

		IMetaData[] selFields = new IMetaData[indexes.length];
		IMetaData[] sourceFields = null;
		if (isCombine)
			sourceFields = m_memberCombines;
		else
			sourceFields = m_members;

		for (int i = 0; i < indexes.length; i++) {
			selFields[i] = sourceFields[indexes[i]];
		}

		m_hiddenDesc.setSelHiddenMember(selFields);

		return null;

	}

} // @jve:decl-index=0:visual-constraint="10,10"
