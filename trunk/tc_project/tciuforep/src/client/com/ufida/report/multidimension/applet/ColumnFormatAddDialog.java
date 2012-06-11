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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nc.itf.iufo.freequery.IMember;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import com.ufida.report.multidimension.model.DimMemberCombinationVO;
import com.ufida.report.multidimension.model.MultiDimensionUtil;
import com.ufida.report.multidimension.model.SelDimensionVO;
import com.ufsoft.iufo.data.IMetaData;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author ll
 * 
 * 行列格式设计中选择行/列对话框
 */
public class ColumnFormatAddDialog extends nc.ui.pub.beans.UIDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jPanel = null;

	private JList jList = null;

	private JScrollPane jScrollPane = null;

	private JCheckBox m_checkBoxCombine = null;

	private JButton m_btnOK = null;

	private JButton m_btnCancel = null;

	private IMetaData[] m_members = null;

	private IMetaData[] m_memberCombines = null;

	private JPanel jPanelChoice = null;

	public ColumnFormatAddDialog(Container cont, SelDimensionVO[] dimVOs, int currIndex, ArrayList al_headers) {
		super(cont);
		initialize();
		initPanelDatas(dimVOs, currIndex, al_headers);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJPanel());
		this.setSize(388, 369);

	}

	private void initPanelDatas(SelDimensionVO[] dims, int index, ArrayList al_headers) {
		DimMemberCombinationVO[] combVOs = MultiDimensionUtil.getAllCombination(dims);
		ArrayList<DimMemberCombinationVO> al_fields = new ArrayList<DimMemberCombinationVO>();
		ArrayList<DimMemberCombinationVO> al_AllFields = new ArrayList<DimMemberCombinationVO>();
		if (index < 0 || index >= dims.length)// -1表示当前未选择层次
			index = dims.length - 1;

		for (int i = 0; i < dims.length; i++) {
			for (int j = 0; j < combVOs.length; j++) {
				IMember[] mems = combVOs[j].getMembers();
				DimMemberCombinationVO vo = new DimMemberCombinationVO();
				IMember[] newMems = new IMember[i + 1];
				System.arraycopy(mems, 0, newMems, 0, i + 1);
				vo.setMembers(newMems);

				if (!al_headers.contains(vo)) {//al_headers是已经设置了格式的行列头,不再参与选择
					if (!al_AllFields.contains(vo)) {
						al_AllFields.add(vo);
						if (i <= index)
							al_fields.add(vo);
					}
				}
			}
		}

		m_members = new IMetaData[al_fields.size()];
		al_fields.toArray(m_members);
		m_memberCombines = new IMetaData[al_AllFields.size()];
		al_AllFields.toArray(m_memberCombines);

		getJCheckBoxCombine().setSelected(false);
		getJList().setListData(m_members);

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

			jPanel.add(getJCheckBoxCombine(), null);
			jPanel.add(getJButtonOK(), null);
			jPanel.add(getJButtonCancel(), null);
			jPanel.add(getJPanelChoice(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJList() {
		if (jList == null) {
			jList = new UIList();
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
			m_checkBoxCombine.setBounds(30, 243, 85, 21);
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
			m_btnOK.setBounds(67, 291, 75, 22);
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
			m_btnCancel.setBounds(207, 291, 75, 22);
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
			jScrollPane.setSize(new java.awt.Dimension(311, 180));
			jScrollPane.setViewportView(getJList());
			jScrollPane.setLocation(new java.awt.Point(7, 21));
		}
		return jScrollPane;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getJButtonOK()) {
//			int[] selectedIndex = getJList().getSelectedIndices();
			// TODO
			closeOK();
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

	}

	/**
	 * This method initializes jPanelChoice
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelChoice() {
		if (jPanelChoice == null) {
			jPanelChoice = new UIPanel();
			jPanelChoice.setLayout(null);
			jPanelChoice.setBounds(new java.awt.Rectangle(24, 27, 324, 207));
			jPanelChoice.add(getJScrollPane(), null);
		}
		return jPanelChoice;
	}

	public Object[] getSelectedFields() {
		Object[] selDatas = getJList().getSelectedValues();
		return selDatas;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
