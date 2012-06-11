/*
 * Created on 2005-7-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.multidimension.applet;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.bi.report.manager.ReportVO;

import com.ufsoft.iufo.resource.StringResource;

/**
 * @author ll
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ChooseReportDlg extends UIDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jPanel = null;

	private JScrollPane jScrollPane = null;

	private JButton jButtonOK = null;

	private JButton jButtonCancel = null;

	private JList jList = null;

	private ReportVO[] m_datas = null;

	/**
	 * This method initializes
	 *  
	 */
	public ChooseReportDlg(Container con) {
		super(con);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJPanel());
		this.setSize(323, 415);

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
			jPanel.add(getJScrollPane(), null);
			jPanel.add(getJButtonOK(), null);
			jPanel.add(getJButtonCancel(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(26, 22, 260, 275);
			jScrollPane.setViewportView(getJList());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new nc.ui.pub.beans.UIButton();
			jButtonOK.setBounds(71, 336, 75, 22);
			jButtonOK.addActionListener(this);
			jButtonOK.setText(StringResource.getStringResource("miufo1000094"));
		}
		return jButtonOK;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new nc.ui.pub.beans.UIButton();
			jButtonCancel.setBounds(206, 336, 75, 22);
			jButtonCancel.addActionListener(this);
			jButtonCancel.setText(StringResource.getStringResource("miufo1000274"));
		}
		return jButtonCancel;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getJButtonOK()) {
			super.closeOK();
		}
		if (e.getSource() == getJButtonCancel()) {
			super.closeCancel();
		}
	}

	public void setAllReports(ReportVO[] reports) {
		if (reports == null || reports.length == 0)
			m_datas = new ReportVO[0];
		else
			m_datas = reports;
		getJList().setListData(m_datas);
	}

	public ReportVO getSelectedReport() {
		int index = getJList().getSelectedIndex();
		if (index < 0 || index >= m_datas.length)
			return null;
		return m_datas[index];
	}
} //  @jve:decl-index=0:visual-constraint="10,10"
