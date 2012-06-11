package com.ufida.report.anareport.edit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;

import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.AreaDataRelation;
import com.ufida.report.anareport.model.DataRelaItem;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.table.exarea.ExAreaCell;

/**
 * 多区域数据关系的设置界面
 * 
 * @author ll
 * 
 */
public class DataRelationDlg extends UfoDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JCheckBox jEnabled = null;// 是否启用
	// private JComboBox jAreaList = null;
	private JPanel m_relaPanel = null;
	private JList jRelationList = null;

	private JButton addBtn = null;
	private JButton updateBtn = null;
	private JButton removeBtn = null;

	private JButton OKBtn = null;
	private JButton cancelBtn = null;

	private AreaDataModel m_data = null;
	private AreaDataRelation m_def = null;

	private ArrayList<ExAreaCell> m_al_areas = null;

	private DataRelaItemDlg m_itemDlg = null;

	private class DataRelaUIItem {
		DataRelaItem m_item = null;
		String m_mainAreaName = null;
		AreaDataModel m_mainArea = null;

		DataRelaUIItem(DataRelaItem item, ExAreaCell mainArea) {
			m_item = item;
			if (mainArea != null) {
				m_mainAreaName = mainArea.toString();
				if (mainArea.getModel() != null && mainArea.getModel() instanceof AreaDataModel)
					m_mainArea = (AreaDataModel) mainArea.getModel();
			}
		}

		DataRelaUIItem getInstance(DataRelaItem item, ExAreaCell mainArea) {
			if (mainArea.getModel() == null || !(mainArea.getModel() instanceof AreaDataModel))
				return null;
			return new DataRelaUIItem(item, mainArea);
		}

		void setMainArea(AreaDataModel mainArea) {
			m_mainArea = mainArea;
		}

		DataRelaItem getItem() {
			return m_item;
		}

		boolean isValid() {
			if (m_item.isRelsCell())
				return true;
			return (m_mainArea.getDSTool().getField(m_item.getMainFld()) != null);
		}

		/**
		 * @i18n miufo00783= 依赖于 
		 */
		public String toString() {
			String fldName = m_data.getDSTool().getField(getItem().getFld()).getCaption();

			fldName += StringResource.getStringResource("miufo00783");
			if (getItem().isRelsCell())
				fldName += getItem().getCellPosition();
			else {
				String mainFldName = m_mainArea.getDSTool().getField(getItem().getMainFld()).getCaption();
				fldName += m_mainAreaName + "." + mainFldName;
			}
			return fldName;
		}
	}

	/**
	 * This is the default constructor
	 */
	public DataRelationDlg(Container owner, ArrayList<ExAreaCell> al_areas, AnaReportModel reportModel,
			AreaDataModel areaData) {
		super(owner);
		m_data = areaData;
		m_al_areas = al_areas;
		m_itemDlg = new DataRelaItemDlg(owner, reportModel, (ExAreaCell[]) al_areas.toArray(new ExAreaCell[0]),
				areaData);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(StringResource.getStringResource(DataRelationExt.RESID_SET_DATA_RELATION));
		this.setSize(400, 300);
		this.setContentPane(getJContentPane());

	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new UIPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getMainPanel(), BorderLayout.CENTER);
			JPanel panel = new UIPanel(new FlowLayout(FlowLayout.TRAILING));
			panel.add(getOKBtn());
			panel.add(getCancelBtn());
			jContentPane.add(panel, BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	private JPanel getMainPanel() {
		JPanel panel = new UIPanel();
		panel.setSize(400, 100);
		panel.setLayout(new BorderLayout());
		// panel.setLayout(new GridLayout(5, 2));
		panel.add(getEnabledBox(), BorderLayout.NORTH);
		panel.add(getRelationPanel(), BorderLayout.CENTER);
		return panel;
	}

	/**
	 * @i18n miufo00366=数据关系
	 */
	private JPanel getRelationPanel() {
		if (m_relaPanel == null) {
			m_relaPanel = new JPanel();
			m_relaPanel.setBounds(20, 50, 380, 160);
			m_relaPanel.setLayout(new BorderLayout());

			JLabel lbl2 = new JLabel(StringResource.getStringResource("miufo00366"));
			lbl2.setBounds(20, 40, 80, 22);
			JScrollPane jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getRelationList());

			m_relaPanel.add(lbl2, BorderLayout.NORTH);
			m_relaPanel.add(jScrollPane1, BorderLayout.CENTER);

			JPanel btnPanel = new JPanel();
			btnPanel.setLayout(null);
			btnPanel.setPreferredSize(new Dimension(100, 120));
			btnPanel.add(getAddBtn());
			btnPanel.add(getUpdateBtn());
			btnPanel.add(getRemoveBtn());
			m_relaPanel.add(btnPanel, BorderLayout.EAST);

		}
		return m_relaPanel;
	}

	private JCheckBox getEnabledBox() {
		if (jEnabled == null) {
			jEnabled = new JCheckBox(StringResource.getStringResource(DataRelationExt.RESID_ENABLED_DATA_RELATION));
			jEnabled.setBounds(220, 10, 160, 20);
			jEnabled.addActionListener(this);
		}
		return jEnabled;
	}

	private JList getRelationList() {
		if (jRelationList == null) {
			jRelationList = new JList();
			jRelationList.setModel(new DefaultListModel());
			jRelationList.setPreferredSize(new Dimension(200, 100));
			jRelationList.setSize(new Dimension(200, 100));
		}
		return jRelationList;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOKBtn() {
		if (OKBtn == null) {
			OKBtn = new nc.ui.pub.beans.UIButton();
			OKBtn.setText(StringResource.getStringResource("mbiadhoc00021"));
			OKBtn.addActionListener(new BtnListener());
		}
		return OKBtn;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelBtn() {
		if (cancelBtn == null) {
			cancelBtn = new nc.ui.pub.beans.UIButton();
			cancelBtn.setText(StringResource.getStringResource("mbiadhoc00022"));
			cancelBtn.addActionListener(new BtnListener());
		}
		return cancelBtn;
	}

	/**
	 * @i18n ubichart00005=增加
	 */
	private JButton getAddBtn() {
		if (addBtn == null) {
			addBtn = new nc.ui.pub.beans.UIButton(StringResource.getStringResource("ubichart00005"));
			addBtn.addActionListener(new BtnListener());
			addBtn.setBounds(10, 20, 60, 22);
		}
		return addBtn;
	}

	/**
	 * @i18n miufo1001396=修改
	 */
	private JButton getUpdateBtn() {
		if (updateBtn == null) {
			updateBtn = new nc.ui.pub.beans.UIButton(StringResource.getStringResource("miufo1001396"));
			updateBtn.addActionListener(new BtnListener());
			updateBtn.setBounds(10, 50, 60, 22);
		}
		return updateBtn;
	}

	/**
	 * @i18n ubichart00006=删除
	 */
	private JButton getRemoveBtn() {
		if (removeBtn == null) {
			removeBtn = new nc.ui.pub.beans.UIButton(StringResource.getStringResource("ubichart00006"));
			removeBtn.addActionListener(new BtnListener());
			removeBtn.setBounds(10, 80, 60, 22);
		}
		return removeBtn;
	}

	public void setDataRelation(AreaDataRelation def) {
		m_def = def;
		ExAreaCell exArea=null;
		if (def != null&&def.getRelations()!=null&&def.getRelations().length>0) {
			setEnabledRelation(def.isEnabled());
			DataRelaItem[] items = def.getRelations();
			if (items != null)
				for (DataRelaItem d : items) {
					exArea=getArea(d.getMainArea());
					if(exArea!=null){
						((DefaultListModel) getRelationList().getModel()).addElement(new DataRelaUIItem(d,exArea));
					}
					
				}
		}else
			setEnabledRelation(false);
	}

	public AreaDataRelation getDataRelation() {
		if (m_def == null)
			m_def = new AreaDataRelation();

		int size = getRelationList().getModel().getSize();
		DataRelaItem[] items = new DataRelaItem[size];
		for (int i = 0; i < size; i++) {
			items[i] = ((DataRelaUIItem) getRelationList().getModel().getElementAt(i)).getItem();
		}
		m_def.setRelations(getEnabledBox().isSelected(), items);

		return m_def;
	}

	private class BtnListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == OKBtn) {
				setResult(UfoDialog.ID_OK);
				close();

			} else if (e.getSource() == cancelBtn) {
				setResult(UfoDialog.ID_CANCEL);
				close();
			} else if (e.getSource() == addBtn || e.getSource() == updateBtn) {
				DataRelaItem item = null;
				int selIndex = 0;
				if (e.getSource() == addBtn) {
					item = new DataRelaItem();
					selIndex = getRelationList().getModel().getSize();
				} else {
					item = ((DataRelaUIItem) getRelationList().getSelectedValue()).getItem();
					selIndex = getRelationList().getSelectedIndex();
				}

				m_itemDlg.setDataRelation(item);
				if (m_itemDlg.showModal() == UIDialog.ID_OK) {
					item = m_itemDlg.getDataRelation();
					DataRelaUIItem uiitem = new DataRelaUIItem(item, getArea(item.getMainArea()));
					if (uiitem != null) {
						if (selIndex >= getRelationList().getModel().getSize())
							((DefaultListModel) getRelationList().getModel()).addElement(uiitem);
						else
							((DefaultListModel) getRelationList().getModel()).set(selIndex, uiitem);
					}
				}

			} else if (e.getSource() == removeBtn) {
				((DefaultListModel) getRelationList().getModel()).remove(getRelationList().getSelectedIndex());
			}
		}
	}

	private ExAreaCell getArea(String areaPK) {
		for (int i = 0; i < m_al_areas.size(); i++) {
			if (m_al_areas.get(i).getExAreaPK().equals(areaPK))
				return m_al_areas.get(i);
		}
		return null;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getEnabledBox()) {// 根据是否启用设置属性面板的可用性
			if (getEnabledBox().isSelected())
				getRelationPanel().setVisible(true);
			else
				getRelationPanel().setVisible(false);
		}

	}

	private void setEnabledRelation(boolean isEnable) {
		getEnabledBox().setSelected(isEnable);
		getRelationPanel().setVisible(isEnable);
	}

}
 