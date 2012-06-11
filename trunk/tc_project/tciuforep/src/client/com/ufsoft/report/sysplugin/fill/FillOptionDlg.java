package com.ufsoft.report.sysplugin.fill;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIPanel;

import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
/**
 * 填充选项对话框
 * @author zzl
 */
public class FillOptionDlg extends UfoDialog {
	private static final long serialVersionUID = 4152559620339548954L;
	private JPanel jContentPane = null;
	private JPanel btnPanel = null;
	private JButton okButton = null;
	private JButton cancleButton = null;
	private JPanel optionPanel = null;
	private JCheckBox sequenceCheckBox = null;
	private JCheckBox formatCheckBox = null;

	/**
	 * This is the default constructor
	 * @param container 
	 */
	public FillOptionDlg(Container container) {
		super(container);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 144);
		this.setTitle(MultiLang.getString("fill_option"));
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
			jContentPane.add(getBtnPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getOptionPanel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes cmdPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getBtnPanel() {
		if (btnPanel == null) {
			btnPanel = new UIPanel();
			btnPanel.add(getOkButton(), null);
			btnPanel.add(getCancleButton(), null);
		}
		return btnPanel;
	}

	/**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new nc.ui.pub.beans.UIButton();
			okButton.setText(MultiLang.getString("ok"));
			okButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					setResult(ID_OK);
					close();
				}
			});
		}
		return okButton;
	}

	/**
	 * This method initializes cancleButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCancleButton() {
		if (cancleButton == null) {
			cancleButton = new nc.ui.pub.beans.UIButton();
			cancleButton.setText(MultiLang.getString("cancel"));
			cancleButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					setResult(ID_CANCEL);
					close();
				}
			});
		}
		return cancleButton;
	}

	/**
	 * This method initializes optionPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getOptionPanel() {
		if (optionPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(2);
			gridLayout.setHgap(0);
			gridLayout.setColumns(1);
			optionPanel = new UIPanel();
			optionPanel.setLayout(gridLayout);
			optionPanel.add(getSequenceCheckBox(), null);
			optionPanel.add(getFormatCheckBox(), null);
			optionPanel.setBorder(new EmptyBorder(5,5,5,5));
		}
		return optionPanel;
	}

	/**
	 * This method initializes sequenceCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getSequenceCheckBox() {
		if (sequenceCheckBox == null) {
			sequenceCheckBox = new UICheckBox();
			sequenceCheckBox.setText(MultiLang.getString("is_serial_fill"));
			sequenceCheckBox.setSelected(true);
		}
		return sequenceCheckBox;
	}
	public boolean isSequenceFill(){
		return getSequenceCheckBox().isSelected();
	}
	/**
	 * This method initializes formatCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 * @i18n report00113=是否填充格式。
	 */    
	private JCheckBox getFormatCheckBox() {
		if (formatCheckBox == null) {
			formatCheckBox = new UICheckBox();
			formatCheckBox.setText(MultiLang.getString("report00113"));
			formatCheckBox.setSelected(false);
		}
		return formatCheckBox;
	}
	public boolean isFillFormat(){
		return getFormatCheckBox().isSelected();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
  