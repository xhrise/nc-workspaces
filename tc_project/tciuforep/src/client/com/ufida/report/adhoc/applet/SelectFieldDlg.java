/*
 * Created on 2005-6-2
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.adhoc.applet;


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SelectFieldDlg extends UfoDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JLabel jLabel = null;
	private JTextField queryModelField = null;
	private JList fieldList = null;
	private JList pageFieldList = null;
	private JList columnFieldList = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JButton okBtn = null;
	private JButton cancelBtn = null;
	/**
	 * This is the default constructor
	 */
	public SelectFieldDlg() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(StringResource.getStringResource("mbiadhoc00034"));
		this.setSize(600, 750);
		this.setContentPane(getJContentPane());
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jLabel3 = new nc.ui.pub.beans.UILabel();
			jLabel2 = new nc.ui.pub.beans.UILabel();
			jLabel1 = new nc.ui.pub.beans.UILabel();
			jLabel = new nc.ui.pub.beans.UILabel();
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			jLabel.setBounds(41, 15, 79, 25);
			jLabel.setText(StringResource.getStringResource("mbiadhoc00035"));
			jLabel1.setBounds(41, 49, 117, 25);
			jLabel1.setText(StringResource.getStringResource("mbiadhoc00036"));
			jLabel2.setBounds(251, 49, 58, 25);
			jLabel2.setText(StringResource.getStringResource("mbiadhoc00037"));
			jLabel3.setBounds(251, 177, 80, 25);
			jLabel3.setText(StringResource.getStringResource("mbiadhoc00038"));
			jContentPane.add(jLabel, null);
			jContentPane.add(getQueryModelField(), null);
			jContentPane.add(getFieldList(), null);
			jContentPane.add(getPageFieldList(), null);
			jContentPane.add(getColumnFieldList(), null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(jLabel2, null);
			jContentPane.add(jLabel3, null);
			jContentPane.add(getOkBtn(), null);
			jContentPane.add(getCancelBtn(), null);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getQueryModelField() {
		if (queryModelField == null) {
			queryModelField = new UITextField();
			queryModelField.setBounds(120, 15, 313, 25);
			queryModelField.setEditable(false);
		}
		return queryModelField;
	}
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getFieldList() {
		if (fieldList == null) {
			fieldList = new UIList();
			fieldList.setBounds(41, 74, 182, 291);
		}
		return fieldList;
	}
	/**
	 * This method initializes jList1	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getPageFieldList() {
		if (pageFieldList == null) {
			pageFieldList = new UIList();
			pageFieldList.setBounds(251, 74, 182, 95);
		}
		return pageFieldList;
	}
	/**
	 * This method initializes jList2	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getColumnFieldList() {
		if (columnFieldList == null) {
			columnFieldList = new UIList();
			columnFieldList.setBounds(251, 204, 182, 161);
		}
		return columnFieldList;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getOkBtn() {
		if (okBtn == null) {
			okBtn = new nc.ui.pub.beans.UIButton();
			okBtn.setBounds(98, 384, 75, 22);
			okBtn.setText(StringResource.getStringResource("mbiadhoc00021"));
		}
		return okBtn;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCancelBtn() {
		if (cancelBtn == null) {
			cancelBtn = new nc.ui.pub.beans.UIButton();
			cancelBtn.setBounds(288, 384, 75, 22);
			cancelBtn.setText(StringResource.getStringResource("mbiadhoc00022"));			
		}
		return cancelBtn;
	}
	
	public static void main(String[] args) {
	    SelectFieldDlg dlg = new SelectFieldDlg();
//        dlg.setSize(400, 300);         
        dlg.show();
       
    }   
      }  //  @jve:decl-index=0:visual-constraint="10,10"
