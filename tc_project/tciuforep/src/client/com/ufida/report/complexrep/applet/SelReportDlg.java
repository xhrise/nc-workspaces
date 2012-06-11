package com.ufida.report.complexrep.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import nc.ui.pub.beans.UIPanel;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

public class SelReportDlg extends UfoDialog {

    private JPanel jContentPane = null;
    private JButton jButton = null;

    /**
     * This is the default constructor
     * @param dlg 
     */
    public SelReportDlg(Container container) {
        super(container);
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(111, 61);
        this.setModal(false);
        this.setUndecorated(true);
        this.addFocusListener(new FocusListener(){
            public void focusGained(FocusEvent e) {                                                
            }
            public void focusLost(FocusEvent e) {
                if(SelReportDlg.this.isValid()){
                    setVisible(true);
                }
            }            
        });
        this.setContentPane(getJContentPane());
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
            jContentPane.add(getJButton(), java.awt.BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */    
    private JButton getJButton() {
    	if (jButton == null) {
    		jButton = new nc.ui.pub.beans.UIButton();
    		jButton.setText(StringResource.getStringResource("uibicomplex0021"));//—°‘Ò»∑∂®
//    		jButton.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
    	}
    	return jButton;
    }
    public void addActionListenerToButton(ActionListener l){
        getJButton().addActionListener(l);
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
