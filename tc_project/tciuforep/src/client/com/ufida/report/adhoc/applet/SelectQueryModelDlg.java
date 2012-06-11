/*
 * Created on 2005-6-22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.adhoc.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import nc.ui.pub.beans.UIPanel;
import nc.vo.bi.query.manager.QueryModelVO;

import com.ufida.report.rep.applet.SelectQueryPanel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SelectQueryModelDlg extends UfoDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JButton OKBtn = null;
	private JButton cancelBtn = null;
	private SelectQueryPanel jPanel = null;
	private String userID = null;
	/**
	 * This is the default constructor
	 */
	public SelectQueryModelDlg(Container owner, String userID) {
		super(owner);
		this.userID = userID;
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(StringResource.getStringResource("mbiadhoc00002"));
		this.setSize(746, 485);
		this.setContentPane(getJContentPane());
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new UIPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), BorderLayout.CENTER);
			JPanel panel = new UIPanel();
			panel.add(getOKBtn());
			panel.add(getCancelBtn());
			jContentPane.add(panel, BorderLayout.SOUTH);			
		}
		return jContentPane;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getOKBtn() {
		if (OKBtn == null) {
			OKBtn = new nc.ui.pub.beans.UIButton();		
			OKBtn.setText(StringResource.getStringResource("mbiadhoc00021"));
			OKBtn.addActionListener(new ActionListener() {
                /**
				 * @i18n uibiadhoc00008=您还没有选择查询对象
				 * @i18n uibiadhoc00009=选择查询对象错误
				 */
                public void actionPerformed(ActionEvent e) {
                	if(getQueryModel() != null){
                		 setResult(UfoDialog.ID_OK);
                         close();
                	}else{
                		JOptionPane.showMessageDialog(SelectQueryModelDlg.this, StringResource.getStringResource("uibiadhoc00008"), StringResource.getStringResource("uibiadhoc00009"), JOptionPane.ERROR_MESSAGE);
                	}
                  
                }
			    });
		}
		return OKBtn;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCancelBtn() {
		if (cancelBtn == null) {
			cancelBtn = new nc.ui.pub.beans.UIButton();	
			cancelBtn.setText(StringResource.getStringResource("mbiadhoc00022"));
			cancelBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   setResult(UfoDialog.ID_CANCEL);
                   close();
                }
			    });
		}
		return cancelBtn;
	}
	
	public QueryModelVO getQueryModel() {
		SelectQueryPanel panel = (SelectQueryPanel) getJPanel();
	    return  panel.getSelectedQueryModelVO();
	}
	public QueryModelVO[] getSelQueryModels() {
		SelectQueryPanel panel = (SelectQueryPanel) getJPanel();
	    return  panel.getSelQueryModels();
	}
	public String getSelQueryID(){
		SelectQueryPanel panel = (SelectQueryPanel) getJPanel();
		return panel.getSelectedQueryID();
	}
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private SelectQueryPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new SelectQueryPanel(userID);			
		}
		return jPanel;
	}
 }  //  @jve:decl-index=0:visual-constraint="10,10"
