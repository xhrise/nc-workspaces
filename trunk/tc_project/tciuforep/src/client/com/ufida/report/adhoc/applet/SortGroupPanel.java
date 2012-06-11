/*
 * Created by caijie  on  2005-12-31
 *   
 */
package com.ufida.report.adhoc.applet;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIScrollPane;

import com.ufida.report.rep.model.SortDescriptor;
import com.ufida.report.rep.model.SortVO;
import com.ufsoft.iufo.data.IMetaData;
import com.ufsoft.iufo.fmtplugin.freequery.UniqueList;
import com.ufsoft.iufo.resource.StringResource;

/**
 * Adhoc设计向导中排序面板
 * 
 * @author caijie
 */
public class SortGroupPanel extends UIPanel {
	private static final long serialVersionUID = 1L;

	private UniqueList sortFieldList = null;

	private JLabel jLabelSort = null;

	private JRadioButton ascendingRB = null;

	private JRadioButton dscendingRB = null;

	private JScrollPane jScrollPane1 = null;

	private ButtonGroup buttonGroup = null;

	private UniqueList groupFieldList = null;

	private JLabel jLabelGroup = null;

	private JScrollPane jScrollPane2 = null;

	/**
	 * This is the default constructor
	 * 
	 */
	public SortGroupPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		setLayout(new GridLayout(2, 1));
		JPanel sortPanel = new JPanel();
		sortPanel.setLayout(new BorderLayout());
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		jLabelSort = new nc.ui.pub.beans.UILabel();
		jLabelSort.setText(StringResource.getStringResource("mbiadhoc00041"));
		JPanel rbPanel = new JPanel();
		rbPanel.add(getAscendingRB());
		rbPanel.add(getDscendingRB());
		topPanel.add(jLabelSort, BorderLayout.WEST);
		topPanel.add(new JPanel(), BorderLayout.CENTER);
		topPanel.add(rbPanel, BorderLayout.EAST);

		sortPanel.add(topPanel, BorderLayout.NORTH);
		sortPanel.add(getJScrollPane1(), BorderLayout.CENTER);

		JPanel groupPanel = new JPanel();
		groupPanel.setLayout(new BorderLayout());

		jLabelGroup = new nc.ui.pub.beans.UILabel();
		jLabelGroup.setText(StringResource.getStringResource("mbiadhoc00040"));
		groupPanel.add(jLabelGroup, BorderLayout.NORTH);
		groupPanel.add(getJScrollPaneGroup(), BorderLayout.CENTER);

		add(sortPanel);
		add(groupPanel);
	}

	/**
	 * This method initializes jList1
	 * 
	 * @return javax.swing.JList
	 */
	protected UniqueList getSortList() {
		if (sortFieldList == null) {
			DefaultListModel model = new DefaultListModel();
			model.addListDataListener(new ListDataListener() {
				public void contentsChanged(ListDataEvent e) {

				}

				// 将对象自动转化为排序描述子
				public void intervalAdded(ListDataEvent e) {
					DefaultListModel listModel = (DefaultListModel) sortFieldList.getModel();
					Object obj = listModel.getElementAt(e.getIndex0());
					if (obj instanceof SortDescriptor) {

					} else {
						SortDescriptor sd = new SortDescriptor((IMetaData) obj, SortVO.SORT_ASCENDING);
						listModel.setElementAt(sd, e.getIndex0());
					}
				}

				public void intervalRemoved(ListDataEvent e) {
				}
			});
			sortFieldList = new UniqueList() {
				private static final long serialVersionUID = 1L;

				@Override
				public int getObjIndex(Object obj) {
					DefaultListModel model = (DefaultListModel) getModel();
					for (int i = 0; i < model.size(); i++) {
						SortDescriptor sortFld = (SortDescriptor) model.get(i);
						if (sortFld.getField().equals(obj))
							return i;

					}
					return -1;
				}
			};
			sortFieldList.setModel(model);
			sortFieldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			sortFieldList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					try {
						SortDescriptor value = (SortDescriptor) sortFieldList.getModel().getElementAt(
								sortFieldList.getSelectedIndex());
						int type = value.getType();
						if (type == SortVO.SORT_ASCENDING) {
							getAscendingRB().setSelected(true);
							getDscendingRB().setSelected(false);
						} else if (type == SortVO.SORT_DESCENDING) {
							getAscendingRB().setSelected(false);
							getDscendingRB().setSelected(true);
						} else {
							getAscendingRB().setSelected(false);
							getDscendingRB().setSelected(false);
						}
					} catch (Exception e1) {
					}
				}
			});
		}
		return sortFieldList;
	}

	/**
	 * This method initializes jRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getAscendingRB() {
		if (ascendingRB == null) {
			ascendingRB = new UIRadioButton();
			getButtonGroup().add(ascendingRB);
			ascendingRB.setText(StringResource.getStringResource("mbiadhoc00042"));
			ascendingRB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					setSortFldProperty(ascendingRB.isSelected());
				}
			});
		}
		return ascendingRB;
	}
	private void setSortFldProperty(boolean isAscending){
		if(getSortList().getModel().getSize() == 0)
			return; 
		if (getSortList().getSelectedIndex() == -1)
			getSortList().setSelectedIndex(0);
		SortDescriptor vo = (SortDescriptor) getSortList().getModel().getElementAt(
				getSortList().getSelectedIndex());

		if (ascendingRB.isSelected()) {
			vo.setType(SortVO.SORT_ASCENDING);
		} else {
			vo.setType(SortVO.SORT_DESCENDING);
		}
	}
	public SortDescriptor[] getSortDescriptors() {
		return null;
	}

	/**
	 * This method initializes jRadioButton1
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getDscendingRB() {
		if (dscendingRB == null) {
			dscendingRB = new UIRadioButton();
			getButtonGroup().add(dscendingRB);
			dscendingRB.setText(StringResource.getStringResource("mbiadhoc00043"));
			dscendingRB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					setSortFldProperty(ascendingRB.isSelected());
				}
			});
		}
		return dscendingRB;
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new UIScrollPane();
			jScrollPane1.setViewportView(getSortList());
		}
		return jScrollPane1;
	}

	private ButtonGroup getButtonGroup() {
		if (buttonGroup == null) {
			buttonGroup = new ButtonGroup();
		}
		return buttonGroup;
	}

	private JScrollPane getJScrollPaneGroup() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new UIScrollPane();
			jScrollPane2.setViewportView(getGroupFieldList());
		}
		return jScrollPane2;
	}

	public UniqueList getGroupFieldList() {
		if (groupFieldList == null) {
			groupFieldList = new UniqueList() {
				private static final long serialVersionUID = 1L;

				@Override
				public int getObjIndex(Object obj) {
					return ((DefaultListModel) getModel()).indexOf(obj);
				}

			};
			groupFieldList.setModel(new DefaultListModel());
		}
		return groupFieldList;
	}

} // @jve:decl-index=0:visual-constraint="16,8"
