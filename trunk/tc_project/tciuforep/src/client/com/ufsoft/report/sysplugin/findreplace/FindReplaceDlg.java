package com.ufsoft.report.sysplugin.findreplace;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;

public class FindReplaceDlg extends UfoDialog {

	private JPanel jContentPane = null;
	private nc.ui.pub.beans.UITabbedPane jTabbedPane = null;
	private JPanel findPanel = null;
	private JPanel sharePanel = null;
	private JPanel btnPanel = null;
	private JButton replaceButton = null;
	private JButton replaceAllButton = null;
	private JButton findNextButton = null;
	private JButton closeButton = null;
	private JLabel findContentLabel = null;
	private JTextField findContextTextField = null;
	private JLabel replaceContextLabel = null;
	private JTextField replaceContextTextField = null;
	private JCheckBox diffBigSmallCheckBox = null;
	private JPanel replacePanel = null;
	
	private FindReplaceCmd findReplaceCmd;
	private boolean isFind;
	private UfoReport report;
	/**
	 * This is the default constructor
	 */
	public FindReplaceDlg(Container container, FindReplaceCmd cmd,boolean isFind) {
		super(container);
		if(container instanceof UfoReport)
		  this.report=(UfoReport)container;
		this.isFind=isFind;
		initialize();
		this.findReplaceCmd = cmd;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(399, 200);
		this.setTitle(MultiLang.getString("findAndReplace"));
		this.setContentPane(getJContentPane());
		// xulm 2009-09-02 将焦点聚集在查找内容文本框中
		this.addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent arg0) {
				// TODO Auto-generated method stub
				findContextTextField.requestFocusInWindow();
			}
			public void windowLostFocus(WindowEvent arg0) {
			}
		});
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
			jContentPane.add(getJTabbedPane(), java.awt.BorderLayout.CENTER);
			jContentPane.add(getBtnPanel(), java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return nc.ui.pub.beans.UITabbedPane	
	 */    
	private nc.ui.pub.beans.UITabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new nc.ui.pub.beans.UITabbedPane();
			jTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() { 
				public void stateChanged(javax.swing.event.ChangeEvent e) {   
					if(jTabbedPane.getSelectedComponent() == getFindPanel()){
						getFindPanel().add(getSharePanel(),BorderLayout.CENTER);
						getSharePanel().getComponent(2).setVisible(false);
						getSharePanel().getComponent(3).setVisible(false);
						getReplaceButton().setVisible(false);
						getReplaceAllButton().setVisible(false);
					}else{
						getReplacePanel().add(getSharePanel(),BorderLayout.CENTER);
						getSharePanel().getComponent(2).setVisible(true);
						getSharePanel().getComponent(3).setVisible(true);
						getReplaceButton().setVisible(true);
						getReplaceAllButton().setVisible(true);
					}
					//xulm  2009-09-02 将焦点聚集在查找内容文本框中
					focusInFindContextTextField();
				}
			});
			jTabbedPane.addTab(MultiLang.getString("find"), null, getFindPanel(), null);
//			jTabbedPane.addTab("共享", null, getSharePanel(), null);
			
			//数据录入状态的替换会导致出现关键字等处的非法数据，所以在找到校验办法之前，不能提供替换功能。
			if(report!=null&&report.getOperationState()==UfoReport.OPERATION_FORMAT){
				jTabbedPane.addTab(MultiLang.getString("replace"), null, getReplacePanel(), null);	
				if(!isFind){
					jTabbedPane.setSelectedComponent(getReplacePanel());
				}
			}
			
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes findPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getFindPanel() {
		if (findPanel == null) {
			findPanel = new UIPanel();
			findPanel.setLayout(new BorderLayout());
		}
		return findPanel;
	}

	/**
	 * This method initializes replacePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getSharePanel() {
		if (sharePanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridwidth = 2;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.insets = new java.awt.Insets(5,50,5,0);
			gridBagConstraints3.gridx = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.insets = new java.awt.Insets(5,50,5,0);
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.weightx = 8.0D;
			gridBagConstraints11.insets = new java.awt.Insets(5,0,5,20);
			gridBagConstraints11.gridy = 1;
			replaceContextLabel = new nc.ui.pub.beans.UILabel();
			replaceContextLabel.setText(MultiLang.getString("toReplaceContentIs"));
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 8.0D;
			gridBagConstraints1.insets = new java.awt.Insets(5,0,5,20);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.insets = new java.awt.Insets(5,50,5,0);
			gridBagConstraints.gridy = 0;
			findContentLabel = new nc.ui.pub.beans.UILabel();
			findContentLabel.setText(MultiLang.getString("toFindContent"));
			sharePanel = new UIPanel();
			sharePanel.setLayout(new GridBagLayout());
			sharePanel.add(findContentLabel, gridBagConstraints);
			sharePanel.add(getFindContextTextField(), gridBagConstraints1);
			sharePanel.add(replaceContextLabel, gridBagConstraints2);
			sharePanel.add(getReplaceContextTextField(), gridBagConstraints11);
			sharePanel.add(getDiffBigSmallCheckBox(), gridBagConstraints3);
		}
		return sharePanel;
	}

	/**
	 * This method initializes btnPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getBtnPanel() {
		if (btnPanel == null) {
			btnPanel = new UIPanel();
			btnPanel.add(getReplaceButton(), null);
			btnPanel.add(getReplaceAllButton(), null);
			btnPanel.add(getFindNextButton(), null);
			btnPanel.add(getCloseButton(), null);
		}
		return btnPanel;
	}

	/**
	 * This method initializes replaceButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getReplaceButton() {
		if (replaceButton == null) {
			replaceButton = new nc.ui.pub.beans.UIButton();
			replaceButton.setText(MultiLang.getString("replace"));
			replaceButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					findReplaceCmd.replaceOne(
							getFindContextTextField().getText(),
							getReplaceContextTextField().getText(),
							getDiffBigSmallCheckBox().isSelected()			
					);
				}
			});
		}
		return replaceButton;
	}

	/**
	 * This method initializes replaceAllButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getReplaceAllButton() {
		if (replaceAllButton == null) {
			replaceAllButton = new nc.ui.pub.beans.UIButton();
			replaceAllButton.setText(MultiLang.getString("replaceAll"));
			replaceAllButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					findReplaceCmd.replaceAll(
							getFindContextTextField().getText(),
							getReplaceContextTextField().getText(),
							getDiffBigSmallCheckBox().isSelected()		
					);
				}
			});
		}
		return replaceAllButton;
	}

	/**
	 * This method initializes findNextButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getFindNextButton() {
		if (findNextButton == null) {
			findNextButton = new nc.ui.pub.beans.UIButton();
			findNextButton.setText(MultiLang.getString("findNext"));
			findNextButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					findReplaceCmd.findNext(
							getFindContextTextField().getText(),
							getDiffBigSmallCheckBox().isSelected()
					);
				}
			});
		}
		return findNextButton;
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

	/**
	 * This method initializes findContextTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getFindContextTextField() {
		if (findContextTextField == null) {
			findContextTextField = new UITextField();
		}
		return findContextTextField;
	}

	/**
	 * This method initializes replaceContextTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getReplaceContextTextField() {
		if (replaceContextTextField == null) {
			replaceContextTextField = new UITextField();
		}
		return replaceContextTextField;
	}

	/**
	 * This method initializes diffBigSmallCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getDiffBigSmallCheckBox() {
		if (diffBigSmallCheckBox == null) {
			diffBigSmallCheckBox = new UICheckBox();
			diffBigSmallCheckBox.setText(MultiLang.getString("diffBigSmallLetter"));
		}
		return diffBigSmallCheckBox;
	}

	/**
	 * This method initializes replacePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getReplacePanel() {
		if (replacePanel == null) {
			replacePanel = new UIPanel();
			replacePanel.setLayout(new BorderLayout());
		}
		return replacePanel;
	}
	
	/**
	 * xulm  2009-09-02 将焦点聚集在查找内容文本框中
	 */
	private void focusInFindContextTextField()
	{
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				// TODO Auto-generated method stub
				findContextTextField.requestFocusInWindow();
			}
		});
	}
	
	public static void main(String[] args) {
		new FindReplaceDlg(null,null,true).setVisible(true);
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
