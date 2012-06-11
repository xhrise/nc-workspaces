/*
 * Created on 2005-7-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.applet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SelectDataDlg extends UfoDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane1 = null;
	private JScrollPane jScrollPane2 = null;
	private JScrollPane jScrollPane3 = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JButton jButton2 = null;
	private JButton jButton3 = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JButton jButton4 = null;
	private JButton jButton5 = null;
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(17, 32, 254, 183);
		}
		return jScrollPane;
	}
	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new UIScrollPane();
			jScrollPane1.setBounds(357, 31, 254, 183);
		}
		return jScrollPane1;
	}
	/**
	 * This method initializes jScrollPane2	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new UIScrollPane();
			jScrollPane2.setBounds(17, 258, 254, 183);
		}
		return jScrollPane2;
	}
	/**
	 * This method initializes jScrollPane3	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane3() {
		if (jScrollPane3 == null) {
			jScrollPane3 = new UIScrollPane();
			jScrollPane3.setBounds(374, 253, 254, 183);
		}
		return jScrollPane3;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new nc.ui.pub.beans.UIButton();
			jButton.setBounds(283, 57, 75, 22);
			jButton.setText(StringResource.getStringResource("ubichart00005"));
		}
		return jButton;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new nc.ui.pub.beans.UIButton();
			jButton1.setBounds(285, 115, 75, 22);
			jButton1.setText(StringResource.getStringResource("ubichart00006"));
		}
		return jButton1;
	}
	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new nc.ui.pub.beans.UIButton();
			jButton2.setBounds(285, 270, 75, 22);
			jButton2.setText(StringResource.getStringResource("ubichart00005"));
		}
		return jButton2;
	}
	/**
	 * This method initializes jButton3	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton3() {
		if (jButton3 == null) {
			jButton3 = new nc.ui.pub.beans.UIButton();
			jButton3.setBounds(285, 322, 75, 22);
			jButton3.setText(StringResource.getStringResource("ubichart00006"));
		}
		return jButton3;
	}
	/**
	 * This method initializes jButton4	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton4() {
		if (jButton4 == null) {
			jButton4 = new nc.ui.pub.beans.UIButton();
			jButton4.setBounds(395, 455, 75, 22);
		}
		return jButton4;
	}
	/**
	 * This method initializes jButton5	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton5() {
		if (jButton5 == null) {
			jButton5 = new nc.ui.pub.beans.UIButton();
			jButton5.setBounds(164, 455, 75, 22);
		}
		return jButton5;
	}
              public static void main(String[] args) {
    }
	/**
	 * This is the default constructor
	 */
	public SelectDataDlg() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(StringResource.getStringResource("ubichart00007"));
		this.setSize(635, 517);
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
			jLabel.setBounds(17, 7, 133, 25);
			jLabel.setText(StringResource.getStringResource("ubichart00008"));
			jLabel1.setBounds(359, 7, 133, 25);
			jLabel1.setText(StringResource.getStringResource("ubichart00009"));
			jLabel2.setBounds(17, 232, 133, 25);
			jLabel2.setText(StringResource.getStringResource("ubichart00010"));
			jLabel3.setBounds(385, 228, 133, 25);
			jLabel3.setText(StringResource.getStringResource("ubichart00011"));
			jContentPane.add(getJScrollPane(), null);
			jContentPane.add(getJScrollPane1(), null);
			jContentPane.add(getJScrollPane2(), null);
			jContentPane.add(getJScrollPane3(), null);
			jContentPane.add(getJButton(), null);
			jContentPane.add(getJButton1(), null);
			jContentPane.add(getJButton2(), null);
			jContentPane.add(getJButton3(), null);
			jContentPane.add(jLabel, null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(jLabel2, null);
			jContentPane.add(jLabel3, null);
			jContentPane.add(getJButton4(), null);
			jContentPane.add(getJButton5(), null);
		}
		return jContentPane;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
