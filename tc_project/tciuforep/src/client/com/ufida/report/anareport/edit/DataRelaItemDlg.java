package com.ufida.report.anareport.edit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nc.ui.pub.beans.UIPanel;

import com.ufida.dataset.metadata.Field;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.DataRelaItem;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.table.CellPosition;
import com.ufsoft.report.exception.MessageException;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;


/**
 * 多区域数据关系的设置界面
 * 
 * @author ll
 * 
 */
public class DataRelaItemDlg extends UfoDialog {
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JComboBox jFieldList = null;
	private JComboBox jAreaList = null;
	// private JCheckBox jRelaCell = null;
	private JTextField jPosText = null;
	private JComboBox jMainFieldList = null;

	private JButton OKBtn = null;
	private JButton cancelBtn = null;

	private AnaReportModel m_report = null;
	private AreaDataModel m_data = null;
	private DataRelaItem m_def = null;

	private ExAreaCell[] all_areas = null;
	private Field[] all_flds = null;
	private Field[] all_mainFlds = null;

	/**
	 * This is the default constructor
	 */
	public DataRelaItemDlg(Container owner, AnaReportModel reportModel, ExAreaCell[] areas, AreaDataModel areaData) {
		super(owner);
		m_report = reportModel;
		m_data = areaData;
		all_areas = areas;
		all_flds = areaData.getDSTool().getFields(true);
		AreaDataModel model = (AreaDataModel) areas[0].getModel();
		all_mainFlds = model.getDSTool().getFields(true);
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
			JPanel panel = new UIPanel();
			panel.add(getOKBtn());
			panel.add(getCancelBtn());
			jContentPane.add(panel, BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * @i18n mbiadhoc00025=选择字段
	 * @i18n miufo00323=依赖区域
	 * @i18n miufo00324=依赖字段
	 */
	private JPanel getMainPanel() {
		JPanel panel = new UIPanel();
		panel.setSize(400, 100);
		panel.setLayout(null);
		// panel.setLayout(new GridLayout(5, 2));

		JLabel lbl1 = new JLabel(StringResource.getStringResource("mbiadhoc00025"));
		lbl1.setBounds(20, 10, 80, 22);
		panel.add(lbl1);
		panel.add(getFieldList());
		
		JLabel lbl2 = new JLabel(StringResource.getStringResource("miufo00323"));
		lbl2.setBounds(20, 60, 80, 22);
		panel.add(lbl2);
		panel.add(getAreaList());
		JLabel lbl3 = new JLabel(StringResource.getStringResource("miufo00324"));
		lbl3.setBounds(20, 100, 80, 22);
		panel.add(lbl3);
		panel.add(getMainFieldList());
		// panel.add(getIsRelaCell());
//		panel.add(getPosText());
		return panel;
	}

	private JComboBox getFieldList() {
		if (jFieldList == null) {
			jFieldList = new JComboBox(all_flds);
			jFieldList.setBounds(100, 10, 200, 22);
		}
		return jFieldList;
	}

	private JComboBox getAreaList() {
		if (jAreaList == null) {
			jAreaList = new JComboBox(all_areas);
			jAreaList.setBounds(100, 60, 200, 22);
			jAreaList.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int index = jAreaList.getSelectedIndex();
					AreaDataModel mainArea = (AreaDataModel) all_areas[index].getModel();
					if (mainArea != null) {
						all_mainFlds = mainArea.getDSTool().getFields(true);
						setMainFld(all_mainFlds);
					}
				}
			});
		}
		return jAreaList;
	}

	/*
	 * private JCheckBox getIsRelaCell() { if (jRelaCell == null) { jRelaCell =
	 * new JCheckBox("指定单元位置"); jRelaCell.setBounds(100, 70, 200, 22);
	 * jRelaCell.setSelected(true); jRelaCell.addChangeListener(new
	 * ChangeListener() { public void stateChanged(ChangeEvent e) { boolean
	 * isPos = jRelaCell.isSelected(); getPosText().setVisible(isPos);
	 * getMainFieldList().setVisible(!isPos);
	 *  }
	 * 
	 * }); } return jRelaCell; }
	 */
	private JComboBox getMainFieldList() {
		if (jMainFieldList == null) {
			jMainFieldList = new JComboBox(all_mainFlds);
			jMainFieldList.setBounds(100, 100, 200, 22);
			jMainFieldList.setVisible(true);
		}
		return jMainFieldList;
	}

	public void setMainFld(Field[] mainFlds) {
		all_mainFlds = mainFlds;

		DefaultComboBoxModel model = (DefaultComboBoxModel) getMainFieldList().getModel();
		model.removeAllElements();
		for (Field fld : mainFlds)
			model.addElement(fld);

	}

	private JTextField getPosText() {
		if (jPosText == null) {
			jPosText = new JTextField();
			jPosText.setBounds(100, 70, 200, 22);
			jPosText.setVisible(false);
		}
		return jPosText;
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

	public void setDataRelation(DataRelaItem def) {
		m_def = def;
		if (def != null && def.getFld() != null) {
			String fld = def.getFld();
			String mainArea = def.getMainArea();
			boolean isPos = def.isRelsCell();
			CellPosition pos = def.getCellPosition();
			String mainFld = def.getMainFld();

			int fldIndex = 0;
			for (int i = 0; i < all_flds.length; i++) {
				if (fld.equals(all_flds[i].getFldname())) {
					fldIndex = i;
					break;
				}
			}
			getFieldList().setSelectedIndex(fldIndex);

			// getIsRelaCell().setSelected(isPos);
			if (isPos) {
				getPosText().setText(pos.toString());
			} else {
				int mainIndex = 0;
				for (int i = 0; i < all_areas.length; i++) {
					if (all_areas[i].getExAreaPK().equals(mainArea)) {
						mainIndex = i;
						break;
					}
				}
				getAreaList().setSelectedIndex(mainIndex);

				fldIndex = 0;
				for (int i = 0; i < all_mainFlds.length; i++) {
					if (mainFld.equals(all_mainFlds[i].getFldname())) {
						fldIndex = i;
						break;
					}
				}
				getMainFieldList().setSelectedIndex(fldIndex);
			}
		}
	}

	public DataRelaItem getDataRelation() {
		String fld = all_flds[getFieldList().getSelectedIndex()].getFldname();
		// boolean isPos = getIsRelaCell().isSelected();
		boolean isPos = false;
		if (isPos) {
			CellPosition pos = CellPosition.getInstance(getPosText().getText());
			m_def.setRelation(fld, true, pos, null, null);
		} else {
			String mainArea = all_areas[getAreaList().getSelectedIndex()].getExAreaPK();
			String mainFld = all_mainFlds[getMainFieldList().getSelectedIndex()].getFldname();
			m_def.setRelation(fld, false, null, mainArea, mainFld);
		}
		return m_def;
	}

    	/**
	 * 校验依赖关系: 1:两个都有图表的扩展区，不能依赖
	 * 
	 * @create by wangyga at 2009-2-21,下午02:36:52
    	 * @i18n iufobi00032=对不起,两个图表不允许设置依赖关系
	 * 
	 */
	private void validateRelation() {
		ExAreaModel exAreaModel = ExAreaModel.getInstance(m_report
				.getFormatModel());

		ExAreaCell currentAreaCell = exAreaModel.getExAreaByPK(m_data
				.getAreaPK());
		ExAreaCell mainAreaCell = all_areas[getAreaList().getSelectedIndex()];
		if (currentAreaCell.getExAreaType() == ExAreaCell.EX_TYPE_CHART
				&& mainAreaCell.getExAreaType() == ExAreaCell.EX_TYPE_CHART) {
			throw new MessageException(MessageException.TYPE_WARNING,StringResource.getStringResource("iufobi00032"));
		}
	}

	private class BtnListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == OKBtn) {
				try {
					validateRelation();
				} catch (MessageException ex) {
					UfoPublic.sendMessage(ex, null);
					return;
				}

				setResult(UfoDialog.ID_OK);
				close();

			} else if (e.getSource() == cancelBtn) {
				setResult(UfoDialog.ID_CANCEL);
				close();
			}
		}
	}
}
  