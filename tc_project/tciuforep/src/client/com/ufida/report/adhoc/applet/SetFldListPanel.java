package com.ufida.report.adhoc.applet;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import com.ufsoft.iufo.fmtplugin.freequery.UniqueList;
import com.ufsoft.iufo.resource.StringResource;

public class SetFldListPanel extends UIPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UniqueList fieldList = null;
	private JLabel jLabelField = null;
	private JScrollPane jScrollPane2 = null;
	private JPanel btnPanel = null;
	/**
	 * This is the default constructor
	 */
	public SetFldListPanel() {
		super();
		initialize();
	}
	private  void initialize() {
		jLabelField = new nc.ui.pub.beans.UILabel();
		jLabelField.setText(StringResource.getStringResource("mbiadhoc00038"));
		this.setLayout(new BorderLayout());
		this.add(jLabelField, BorderLayout.NORTH);
		this.add(getJScrollPane2(), BorderLayout.CENTER);
//		this.add(getBtnPanel(), BorderLayout.EAST);
	}
	/**
	 * This method initializes groupFieldList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	public UniqueList getReportFieldList() {
		if (fieldList == null) {
			fieldList = new UniqueList(){
				@Override
				public int getObjIndex(Object obj) {
					return ((DefaultListModel)getModel()).indexOf(obj);
				};
			};
			fieldList.setModel(new DefaultListModel());
		}
		return fieldList;
	}
	/**
	 * This method initializes jScrollPane2	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new UIScrollPane();
			jScrollPane2.setViewportView(getReportFieldList());
		}
		return jScrollPane2;
	}
	/**
	 * This method initializes btnPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getBtnPanel() {
		if (btnPanel == null) {
			btnPanel = new ListOperPanel(getReportFieldList());
			((ListOperPanel)btnPanel).getBtnMoveDown().setVisible(false);
			((ListOperPanel)btnPanel).getBtnMoveUp().setVisible(false);

		}
		return btnPanel;
	}	

}
