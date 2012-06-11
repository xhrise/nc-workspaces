/*
 * Created on 2005-7-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.applet;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;

import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SelectChartPartPanel extends UIPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane catagSP = null;  //  @jve:decl-index=0:visual-constraint="188,216"
	private JScrollPane serialSP = null;
	private JButton jButton = null;
	private JList jList = null;
	private JList jList1 = null;
	/**
	 * This is the default constructor
	 */
	public SelectChartPartPanel() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		
		this.setLayout(new GridBagLayout());
		this.setSize(300,200);
		GridBagConstraints[] cons = createGridBagConstraints();
		this.add(getSerialSP(), cons[0]);		
		this.add(getCatagSP(), cons[1]);
		this.add(getJButton(), cons[2]);
	}
	
	private GridBagConstraints[] createGridBagConstraints() {	    
	    GridBagConstraints[] cons = new GridBagConstraints[] {new GridBagConstraints(), new GridBagConstraints(), new GridBagConstraints()};
	    
	    cons[0].gridx = 0;
	    cons[0].gridy = 0;
	    cons[0].weightx = 1.0;
	    cons[0].weighty = 1.0;
	    cons[0].fill = java.awt.GridBagConstraints.BOTH;
		
		
	    cons[1].gridx = 0;
	    cons[1].gridy = 1;
	    cons[1].weightx = 1.0;
	    cons[1].weighty = 1.0;		
		cons[1].fill = java.awt.GridBagConstraints.BOTH;
		
		cons[2].gridx = 0;
	    cons[2].gridy = 2;
	    cons[2].weightx = 1.0;
	    cons[2].weighty = 1.0;		
		
	    return cons;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getCatagSP() {
		if (catagSP == null) {
			catagSP = new UIScrollPane();
			catagSP.setViewportView(getJList1());
		}
		return catagSP;
	}
	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getSerialSP() {
		if (serialSP == null) {
			serialSP = new UIScrollPane();
			serialSP.setViewportView(getJList());
		}
		return serialSP;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new nc.ui.pub.beans.UIButton();
		}
		return jButton;
	}
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getJList() {
		if (jList == null) {
			jList = new UIList();
		}
		return jList;
	}
	/**
	 * This method initializes jList1	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getJList1() {
		if (jList1 == null) {
			jList1 = new UIList();
		}
		return jList1;
	}
     }  //  @jve:decl-index=0:visual-constraint="96,124"
