package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.iufo.resource.StringResource;

public class FormulaTransDlg extends UfoDialog{
	private static final long serialVersionUID = 7173071842008892312L;

	private JTextField unitCodeText = null;
	private JLabel unitCodeLabel = null;
	private JPanel contentPane = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	
	public FormulaTransDlg(){
		super();
		init();
	}

	/**
	 * @i18n miufo00759=选择原单位
	 */
	private void init(){
		setTitle(StringResource.getStringResource("miufo00759"));
		setResizable(false);
		setSize(new Dimension(265,100));
		setLocationRelativeTo(this);
		getContentPane().add(getContentPanel());
	}
	
    private JPanel getContentPanel(){
    	if(contentPane == null){
    		try {
    			contentPane = new UIPanel();
    			contentPane.setLayout(null);
    			contentPane.add(getUnitCodeLabel());
    			contentPane.add(getUnitCodeText());
    			contentPane.add(getOkButton());
    			contentPane.add(getCancelButton());
			} catch (Exception e) {
				handleException(e);
			}	
    	}
    	return contentPane;
    }
    
    private JTextField getUnitCodeText(){
    	if(unitCodeText == null){
    		try {
    			unitCodeText = new UITextField();
    			unitCodeText.setBounds(5, 23, 250, 21);
			} catch (Exception e) {
				handleException(e);
			}
    	}
    	return unitCodeText;
    }
    
    /**
	 * @i18n miufo00761=原单位编码
	 */
    private JLabel getUnitCodeLabel(){
    	if(unitCodeLabel == null){
    		try {
    			unitCodeLabel = new UILabel();
    			unitCodeLabel.setBounds(5, 0, 80, 21);
    			unitCodeLabel.setText(StringResource.getStringResource("miufo00761"));
			} catch (Exception e) {
				handleException(e);
			}		
    	}
    	return unitCodeLabel;
    }
    
    private JButton getOkButton(){
    	if(okButton == null){
    		try {
    			okButton = createOkButton();
    			okButton.setBounds(85, 47, 30, 21);
			} catch (Exception e) {
				handleException(e);
			}
    		
    		
    	}
    	return okButton;
    }
    
    private JButton getCancelButton(){
    	if(cancelButton == null){
    		try {
    			cancelButton = createCancleButton();
    			cancelButton.setBounds(125, 47, 30, 21);
			} catch (Exception e) {
				handleException(e);
			}
    	}
    	return cancelButton;
    }
    
    public String getUnitCode(){
    	return getUnitCodeText().getText();
    }
    
    private void handleException(java.lang.Throwable exception) {
		AppDebug.debug(exception);
	}	
}
