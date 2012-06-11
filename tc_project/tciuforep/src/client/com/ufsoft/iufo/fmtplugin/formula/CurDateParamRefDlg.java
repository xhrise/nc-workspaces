/*
 * 创建日期 2006-4-20

 */
package com.ufsoft.iufo.fmtplugin.formula;


import java.awt.Dialog;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
/**
 * 
 * @author ljhua 2005-11-22
 */
public class CurDateParamRefDlg extends UfoDialog implements ItemListener{

	private javax.swing.JPanel jContentPane = null;

	private JComboBox jComboBox = null;
	private JLabel jLabel = null;
	private String m_strDate=null;
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new UIComboBox();
			jComboBox.setBounds(104, 22, 168, 29);
			jComboBox.addItem("");
			jComboBox.addItem("'Y'");
			jComboBox.addItem("'M'");
			jComboBox.addItem("'S'");
			jComboBox.addItem("'P'");
			jComboBox.addItem("'W'");
			jComboBox.addItem("'D'");
			
			jComboBox.addItemListener(this);
			
		}
		return jComboBox;
	}
 	public static void main(String[] args) {
	}
	/**
	 * This is the default constructor
	 */
	public CurDateParamRefDlg() {
		super();
		initialize();
	}
	public CurDateParamRefDlg(Dialog parent,String strDate) {
		super(parent);
		m_strDate=strDate==null?null:strDate.toUpperCase();
		initialize();

	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 114);
		this.setContentPane(getJContentPane());
		setTitle(StringResource.getStringResource("uiufofunc146"));

		getJComboBox().setSelectedItem(m_strDate);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jLabel = new nc.ui.pub.beans.UILabel();
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			jLabel.setBounds(15, 22, 63, 28);
			jLabel.setText(StringResource.getStringResource("uiufofunc024"));
			jContentPane.add(getJComboBox(), java.awt.BorderLayout.CENTER);
			jContentPane.add(jLabel, null);
		}
		return jContentPane;
	}
	public String getSelectedTime(){
		return (String) getJComboBox().getSelectedItem();
	}
	/* 
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange()==ItemEvent.SELECTED){
			setResult(ID_OK);
			this.close();
		}
		
	}
	public void windowClosing(java.awt.event.WindowEvent e) {
		setResult(ID_CANCEL);
//		close();
//		return;
		super.windowClosing(e);
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
