package com.ufsoft.table.re;


import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

public class GeneralListRefComp extends UIPanel implements IRefComp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String m_strTitle = null;
	private JList listRefComp=null;

	public GeneralListRefComp(ListModel dataModel,String title) {
		listRefComp=new UIList(dataModel);
		listRefComp.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.m_strTitle=title;
		UIScrollPane listPanel = new UIScrollPane(listRefComp);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(300,500));
		this.add(listPanel,BorderLayout.CENTER);
	}

	public Object getSelectValue() {
		return listRefComp.getSelectedValue();
	}

	public String getTitleValue() {
		return m_strTitle;
	}

	public Object getValidateValue(String text) {
		return text;
	}

	public void setDefaultValue(Object obj) {
		listRefComp.setSelectedValue(obj, true);

	}
	
	 public int getSelectedIndex() {
		 return listRefComp.getSelectedIndex();
	 }

	public JList getListRefComp() {
		return listRefComp;
	}
     
}
