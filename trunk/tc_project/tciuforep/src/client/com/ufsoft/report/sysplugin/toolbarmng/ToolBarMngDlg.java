package com.ufsoft.report.sysplugin.toolbarmng;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIPanel;

import com.ufsoft.report.ReportToolBar;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;

public class ToolBarMngDlg extends UfoDialog {

	private JPanel jContentPane = null;
	private JPanel btnPanel = null;
	private JPanel mainPanel = null;
	private JCheckBox[] allCheckBox = null;
	private JButton closeButton = null;
	
	private UfoReport report;

	/**
	 * This is the default constructor
	 */
	public ToolBarMngDlg(UfoReport report) {
		super(report);
		this.report = report;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setTitle(MultiLang.getString("toolBarMng"));
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
			jContentPane.add(getMainPanel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes btnPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getBtnPanel() {
		if (btnPanel == null) {
			btnPanel = new UIPanel();
			btnPanel.add(getCloseButton(), null);
		}
		return btnPanel;
	}

	/**
	 * This method initializes mainPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(getAllCheckBoxes().length);
			gridLayout.setVgap(5);
			gridLayout.setColumns(1);
			mainPanel = new UIPanel();
			mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10,20,10,20));
			mainPanel.setLayout(gridLayout);
			for(int i=0;i<getAllCheckBoxes().length;i++){
				mainPanel.add(getAllCheckBoxes()[i]);
			}
		}
		return mainPanel;
	}

	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox[] getAllCheckBoxes() {
		if (allCheckBox == null) {
			JPanel toolBarPanel = report.getToolBarPane();
			Component[] toolBars = toolBarPanel.getComponents();
			allCheckBox = new UICheckBox[toolBars.length];
			for (int i = 0; i < toolBars.length; i++) {
				final ReportToolBar bar = (ReportToolBar) toolBars[i];
				final JCheckBox each = new UICheckBox(bar.getName()); 				
				allCheckBox[i] = each;		
				each.setSelected(bar.isVisible());
				each.addItemListener(new ItemListener(){
					public void itemStateChanged(ItemEvent e) {
						boolean isSelected = each.isSelected();
						bar.setVisible(isSelected);
					}					
				});
			}			
		}
		return allCheckBox;
	}

	/**
	 * This method initializes closeButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new nc.ui.pub.beans.UIButton();
			closeButton.setText(MultiLang.getString("close"));
			closeButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					close();
				}
			});
		}
		return closeButton;
	}

}  //  @jve:decl-index=0:visual-constraint="162,17"
