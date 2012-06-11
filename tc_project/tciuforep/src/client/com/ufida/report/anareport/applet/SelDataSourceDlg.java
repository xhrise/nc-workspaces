package com.ufida.report.anareport.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;

import nc.ui.iufo.query.datasetmanager.DataSetManager;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITree;
import nc.vo.iufo.datasetmanager.DataSetDefVO;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

public class SelDataSourceDlg extends UfoDialog {
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JButton OKBtn = null;
	private JButton cancelBtn = null;
	// private SelectQueryPanel jPanel = null;
	// private String userID = null;

	private JTree m_tree = null;
	private JTable m_table = null;

	private DataSetManager m_dsManager = null;
	/**
	 * This is the default constructor
	 */
	public SelDataSourceDlg(Container owner, String userID) {
		super(owner);
		// this.userID = userID;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(StringResource.getStringResource("mbiadhoc00002"));
		this.setSize(746, 485);
		this.setContentPane(getJContentPane());
		
		getDSManager().setTreeModel(getLeftTree());
		getDSManager().setTableModel(getRightTable());
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

	private JPanel getMainPanel() {
		JPanel panel = new UIPanel();

		JSplitPane jSplitPane = new UISplitPane();
		jSplitPane.setOneTouchExpandable(true);

		JScrollPane leftScrollPane = new UIScrollPane();
		leftScrollPane.setViewportView(getLeftTree());
		leftScrollPane.setSize(300, 300);

		jSplitPane.setLeftComponent(leftScrollPane);
		jSplitPane.setDividerLocation(180);

		JScrollPane rightScrollPane = new UIScrollPane();
		rightScrollPane.setViewportView(getRightTable());
		rightScrollPane.setSize(300, 300);

		jSplitPane.setRightComponent(rightScrollPane);

		panel.add(jSplitPane);
		return panel;
	}

	private JTree getLeftTree() {
		if (m_tree == null)
			m_tree = new UITree();
		return m_tree;
	}

	private JTable getRightTable() {
		if (m_table == null)
			m_table = new UITable();
		return m_table;
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
			OKBtn.addActionListener(new ActionListener() {
				/**
				 * @i18n uibiadhoc00008=您还没有选择查询对象
				 * @i18n uibiadhoc00009=选择查询对象错误
				 */
				public void actionPerformed(ActionEvent e) {
					if (getDataSetDef() != null) {
						setResult(UfoDialog.ID_OK);
						close();
					} else {
						JOptionPane.showMessageDialog(SelDataSourceDlg.this, StringResource
								.getStringResource("uibiadhoc00008"), StringResource
								.getStringResource("uibiadhoc00009"), JOptionPane.ERROR_MESSAGE);
					}

				}
			});
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
			cancelBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setResult(UfoDialog.ID_CANCEL);
					close();
				}
			});
		}
		return cancelBtn;
	}

	public DataSetDefVO getDataSetDef() {
//		return AnaRepDataSource.createDataSetDefVO(1)[0];
		return (getDSManager() == null) ? null : getDSManager().getCurrentDataSetDef()[0];
	}
	public DataSetDefVO[] getSelQueryModels() {
		return new DataSetDefVO[]{getDataSetDef()};
	}
//	public DataSetDefVO[] getSelQueryModels() {
//		return AnaRepDataSource.createDataSetDefVO(3);
//
//	}

	private DataSetManager getDSManager(){
		if(m_dsManager == null)
			m_dsManager = new DataSetManager();
		return m_dsManager;
	}
}
