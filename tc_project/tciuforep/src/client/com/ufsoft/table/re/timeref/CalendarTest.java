package com.ufsoft.table.re.timeref;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;
import com.ufsoft.report.util.MultiLang;

public class CalendarTest extends JFrame {

    private JPanel jContentPane = null;
    private JPanel jPanel = null;
    private JTextField jTextField = null;
    private JButton jButton = null;

    /**
     * This is the default constructor
     */
    public CalendarTest() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(300, 200);
        this.setContentPane(getJContentPane());
        this.setTitle("JFrame");
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new UIPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJPanel(), java.awt.BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */    
    private JPanel getJPanel() {
    	if (jPanel == null) {
    		jPanel = new UIPanel();
    		jPanel.add(getJTextField(), null);
    		jPanel.add(getJButton(), null);
    	}
    	return jPanel;
    }

    /**
     * This method initializes jTextField	
     * 	
     * @return javax.swing.JTextField	
     */    
    private JTextField getJTextField() {
    	if (jTextField == null) {
    		jTextField = new UITextField();
    		jTextField.setText("0000 00 00 0000");
    	}
    	return jTextField;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     * @i18n uiuforep0001103= ±º‰≤Œ’’
     */    
    private JButton getJButton() {
    	if (jButton == null) {
    		jButton = new nc.ui.pub.beans.UIButton();
    		jButton.setText(MultiLang.getString("uiuforep0001103"));
    		jButton.addActionListener(new java.awt.event.ActionListener() { 
    			public void actionPerformed(java.awt.event.ActionEvent e) {    
    				new DatePickerDialog(getJTextField()).setVisible(true);
    			}
    		});
    	}
    	return jButton;
    }

}
 