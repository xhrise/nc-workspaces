/*
 * Created on 2005-4-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.panel;

import javax.swing.JLabel;
import javax.swing.JTextField;

import nc.ui.pub.beans.UITextField;

import com.ufsoft.iufo.resource.StringResource;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TextTitlePage extends nc.ui.pub.beans.UIPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel jLabel = null;
	private JTextField TextTitle = null;
	/**
	 * This is the default constructor
	 */
	public TextTitlePage() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @i18n miufo1001805=БъЬт
	 */
	private  void initialize() {
		jLabel = new nc.ui.pub.beans.UILabel();
		this.setLayout(null);
		this.setSize(300,200);
		jLabel.setText(StringResource.getStringResource("miufo1001805"));
		jLabel.setBounds(41, 53, 39, 18);
		this.add(jLabel, null);
		this.add(getTextTitle(), null);
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTextTitle() {
		if (TextTitle == null) {
			TextTitle = new UITextField();
			TextTitle.setBounds(112, 54, 94, 24);
		}
		return TextTitle;
	}
 }
