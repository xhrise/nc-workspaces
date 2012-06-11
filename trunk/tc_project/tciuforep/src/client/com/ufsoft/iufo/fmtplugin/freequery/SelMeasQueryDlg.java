package com.ufsoft.iufo.fmtplugin.freequery;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.pub.iufo.exception.CommonException;
import nc.ui.iufo.query.measurequery.MeasureQueryBO_Client;
import nc.ui.iufo.query.measurequery.MeasureQueryConBO_Client;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.iufo.query.measurequery.MeasureQueryConVO;
import nc.vo.iufo.query.measurequery.MeasureQueryVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.freequery.SelMeasQueryDef;
import com.ufsoft.report.dialog.UfoDialog;

public class SelMeasQueryDlg extends UfoDialog implements ListSelectionListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel MainPanel = null; // @jve:decl-index=0:visual-constraint="20,6"

	private JSplitPane jSplitPane = null;

	private JList jListLeft = null;

	private JList jListRight = null; // @jve:decl-index=0:visual-constraint="535,56"

	private DefaultListModel rightListModel = null;

	private JButton btnOK = null;

	private JButton btnCancel = null;

	private MeasureQueryVO[] mQueryVOs = null;

	private MeasureQueryConVO[] mQueryConVOs = null;

	private MeasureQueryVO selectedQueryVO = null;// 选择的指标查询

	private MeasureQueryConVO selectedQueryCondVO = null;// 选择的指标查询条件

	/**
	 * @param owner
	 * @throws java.awt.HeadlessException
	 */
	public SelMeasQueryDlg(Container parent, String title, String unitID, String taskID) throws HeadlessException {
		super(parent, title);
		initialize();
		initValues(unitID, taskID);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(550, 450);
		this.setContentPane(getMainPanel());
	}

	/**
	 * This method initializes MainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (MainPanel == null) {
			MainPanel = new UIPanel();
			MainPanel.setLayout(new BorderLayout());
			MainPanel.add(getJSplitPane(), BorderLayout.CENTER);

			Dimension size = new Dimension(75, 20);
			getBtnOk().setSize(size);
			getBtnOk().setPreferredSize(size);
			getBtnCancel().setSize(size);
			getBtnCancel().setPreferredSize(size);
			JLabel spaceLabel = new JLabel();
			spaceLabel.setSize(size);
			JPanel btnPane = new UIPanel();
			btnPane.add(getBtnOk());
			btnPane.add(spaceLabel);
			btnPane.add(getBtnCancel());
			MainPanel.add(btnPane, BorderLayout.SOUTH);
		}
		return MainPanel;
	}

	/**
	 * This method initializes jSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setSize(437, 290);
			jSplitPane.setLocation(21, 7);
			UIScrollPane sp1 = new UIScrollPane();
			sp1.setViewportView(getJListLeft());
			jSplitPane.setLeftComponent(sp1);
			UIScrollPane sp2 = new UIScrollPane();
			sp2.setViewportView(getJListRight());
			jSplitPane.setRightComponent(sp2);
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jListLeft
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJListLeft() {
		if (jListLeft == null) {
			jListLeft = new JList();
			jListLeft.setBounds(10, 45, 100, 247);
			jListLeft.addListSelectionListener(this);
		}
		return jListLeft;
	}

	/**
	 * This method initializes jListRight
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJListRight() {
		if (jListRight == null) {
			jListRight = new JList();
			jListRight.setBounds(160, 45, 188, 247);
			jListRight.setModel(getRightListModel());
		}
		return jListRight;
	}

	/**
	 * This method initializes okButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnOk() {
		if (btnOK == null) {
			btnOK = new JButton();
			btnOK.setText(StringResource.getStringResource("miufo1000758"));// miufo1000758=确定

			btnOK.setSize(120, 20);
			btnOK.setLocation(200, 10);
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
			btnCancel = new JButton();
			btnCancel.setText(StringResource.getStringResource("miufo1000757"));// miufo1000757=取消
			btnCancel.setSize(120, 20);
			btnCancel.setLocation(400, 10);
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}

	private void initValues(String strUnitId, String strTaskId) {
		try {
			mQueryVOs = MeasureQueryBO_Client.loadQueryByUnit(strUnitId);
			DefaultListModel leftListModel = new DefaultListModel();
			if (mQueryVOs != null)
				for (int i = 0; i < mQueryVOs.length; i++) {
					leftListModel.addElement(mQueryVOs[i].getQueryName());
				}
			getJListLeft().setModel(leftListModel);
			if (leftListModel.size() > 0)
				getJListLeft().setSelectedIndex(0);

		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			throw new CommonException("miufo10000");// 未定义的错误！
		}

	}

	public void valueChanged(ListSelectionEvent e) {

		int index = getJListLeft().getSelectedIndex();
		if (index < 0 || mQueryVOs == null || index >= mQueryVOs.length)
			return;

		getRightListModel().clear();
		MeasureQueryVO vo = mQueryVOs[index];
		// 加载指标查询上定义的条件
		try {
			String select = vo.getQueryID();
			if (select == null || select.length() == 0)
				throw new CommonException("miufo1003104"); // "请先选择一指标查询！"

			mQueryConVOs = MeasureQueryConBO_Client.loadQueryConVOsByQueryId(select);
			// 装载该指标查询的查询条件信息。
			if (mQueryConVOs != null && mQueryConVOs.length > 0) {
				int iSize = mQueryConVOs.length;
				for (int i = 0; i < iSize; i++) {
					String strCondition = mQueryConVOs[i].getM_strConName();
					getRightListModel().addElement(strCondition);
				}
			}
			if (getRightListModel().getSize() > 0)
				getJListRight().setSelectedIndex(0);
		} catch (CommonException ex) {
			throw ex;
		} catch (Exception ex) {
			AppDebug.debug(ex);
			throw new CommonException("miufo10000");// 未定义的错误！
		}
	}

	private DefaultListModel getRightListModel() {
		if (rightListModel == null) {
			rightListModel = new DefaultListModel();
		}
		return rightListModel;

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnCancel()) {
			setResult(UfoDialog.ID_CANCEL);
			close();
		} else if (e.getSource() == getBtnOk()) {
			resetResult();
			// JOptionPane.showMessageDialog(this, "querycondID is
			// "+getSelectedQueryCondVO().getM_strQueryConId());
			setResult(UfoDialog.ID_OK);
			close();
		}
	}

	private void resetResult() {
		selectedQueryVO = null;
		selectedQueryCondVO = null;
		int leftIndex = getJListLeft().getSelectedIndex();
		if (mQueryVOs != null && leftIndex >= 0 && leftIndex < mQueryVOs.length)
			selectedQueryVO = mQueryVOs[leftIndex];

		int rightIndex = getJListRight().getSelectedIndex();
		if (mQueryConVOs != null && rightIndex >= 0 && rightIndex < mQueryConVOs.length)
			selectedQueryCondVO = mQueryConVOs[rightIndex];

	}

	public MeasureQueryVO getSelectedQueryVO() {
		return selectedQueryVO;
	}

	public MeasureQueryConVO getSelectedQueryCondVO() {
		return selectedQueryCondVO;
	}

	public void setSelectedVO(SelMeasQueryDef selDef) {
		if (selDef == null)
			return;
		int index = findIndex(mQueryVOs, selDef.getMeasQueryVO());
		getJListLeft().setSelectedIndex(index);

	}

	private int findIndex(MeasureQueryVO[] vos, MeasureQueryVO vo) {
		return -1;
	}
}
