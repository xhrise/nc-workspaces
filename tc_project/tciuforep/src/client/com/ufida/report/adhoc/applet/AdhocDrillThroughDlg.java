package com.ufida.report.adhoc.applet;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nc.vo.bi.report.manager.ReportSrv;
import nc.vo.bi.report.manager.ReportVO;

import com.ufida.report.rep.model.BIContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;

public class AdhocDrillThroughDlg extends UfoDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton btnOK = null;
	private JButton btnCancel = null;
	private JLabel jLabel = null;
	private JTextField jTextField = null;
	private JButton btnSelectReprot = null;
	
	private String m_repID = null;
	private UfoReport m_report;
	/**
	 * This is the default constructor
	 */
	public AdhocDrillThroughDlg(Container parent, String repID, UfoReport report) {
		super(parent);
		m_repID = repID;
		m_report = report;
		initialize();
	}

	public String getRepID(){
		return m_repID;
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @i18n uibiadhoc00010=Adhoc穿透
	 */
	private void initialize() {
		this.setTitle(StringResource.getStringResource("uibiadhoc00010"));		
		this.setSize(317, 177);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 * @i18n mbimulti00021=目标报表
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel = new JLabel();
			jLabel.setText(StringResource.getStringResource("mbimulti00021"));
			jLabel.setBounds(5, 43, 57, 18);
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(jLabel, null);
			jContentPane.add(getJTextField(), null);
			jContentPane.add(getBtnSelectReprot(), null);
			jContentPane.add(getBtnOK(), null);
			jContentPane.add(getBtnCancel(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes btnOK	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1003314=确定
	 */    
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new JButton();
			btnOK.setText(StringResource.getStringResource("miufo1003314"));
			btnOK.setBounds(84, 110, 60, 28);
			btnOK.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {		
						AdhocDrillThroughDlg.this.setResult(UfoDialog.ID_OK);
						AdhocDrillThroughDlg.this.setVisible(false);
						AdhocDrillThroughDlg.this.close();
				}});
		}
		return btnOK;
	}

	/**
	 * This method initializes btnCancel	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1003315=取消
	 */    
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setText(StringResource.getStringResource("miufo1003315"));
			btnCancel.setBounds(149, 110, 60, 28);
			btnCancel.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {		
						AdhocDrillThroughDlg.this.setResult(UfoDialog.ID_CANCEL);
						AdhocDrillThroughDlg.this.setVisible(false);
						AdhocDrillThroughDlg.this.close();
				}});
		}
		return btnCancel;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			jTextField.setBounds(62, 43, 198, 22);
			jTextField.setEditable(false);
			setReportNameField(m_repID);
		}
		return jTextField;
	}

	private void setReportNameField(String repID){
		getJTextField().setText(null);
		if(repID == null || repID.trim().length() == 0) {
			getJTextField().setText(null);
		}else{
			ReportSrv reportSrv = new ReportSrv();
			ReportVO[] reportVOs = (ReportVO[]) reportSrv.getByIDs(new String[]{repID});
			if(reportVOs == null || reportVOs.length == 0) return;
			getJTextField().setText(reportVOs[0].getReportname());
		}
	}
	/**
	 * This method initializes btnSelectReprot	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnSelectReprot() {
		if (btnSelectReprot == null) {
			btnSelectReprot = new JButton();
			btnSelectReprot.setText("...");
			btnSelectReprot.setBounds(260, 43, 41, 23);
			btnSelectReprot.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {	
						SelectDrillThroughReportDlg dlg = new SelectDrillThroughReportDlg(AdhocDrillThroughDlg.this, ((BIContextVO) m_report.getContextVo()).getCurUserID());
						dlg.setVisible(true);
						if(dlg.getResult() == UfoDialog.ID_OK){
							m_repID = dlg.getImportReportID();
							setReportNameField(m_repID);
						}
//						AdhocDrillThroughDlg.this.close();
				}});
		}
		return btnSelectReprot;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
