package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nc.ui.pub.beans.UIPanel;

public class FormulaTraceSubPanel extends UIPanel{
	private static final long serialVersionUID = 1L;
	private JButton jButton = null;
	private JScrollPane jScrollPane1 = null;
	private String m_strBtnText = null;
	/**
	 * This method initializes 
	 * 
	 */
	public FormulaTraceSubPanel(String strBtnText) {
		super();
		m_strBtnText = strBtnText;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
        this.add(getJScrollPane1(),BorderLayout.CENTER);			
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setPreferredSize(new Dimension(140, 26));
			jScrollPane1.setViewportView(getJContentPanel());
		}
		return jScrollPane1;
	}
    private JPanel m_jContentPanel = null;
	private JPanel getJContentPanel(){
		if (m_jContentPanel == null) {
        	m_jContentPanel = new UIPanel();
        	m_jContentPanel.setLayout(new BorderLayout());
//        	m_jContentPanel.setBounds(6, 6, 20, 10);
        	m_jContentPanel.add(new JLabel(" "),BorderLayout.EAST);
        	m_jContentPanel.add(new JLabel(" "),BorderLayout.WEST);
        	m_jContentPanel.add(new JLabel(" "),BorderLayout.NORTH);
        	m_jContentPanel.add(new JLabel(" "),BorderLayout.SOUTH);
        	m_jContentPanel.add(getJButton(), BorderLayout.CENTER);   		
		}
		return m_jContentPanel;
		
	}
	

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton("toCalculate");
			jButton.setText(m_strBtnText);
//        	jButton.setBounds(6, 6, 20, 10);
		}
		return jButton;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
