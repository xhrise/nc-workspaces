package com.ufida.report.anareport.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.dsmanager.ParameterSetPanel;

import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.ReportParameter;
import com.ufida.report.anareport.model.AreaParameter;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

/**
 * 一个简单设置报表中全部数据集参数的界面
 * 
 * @author ll
 * 
 */
public class ReportParamDlg extends UfoDialog {
	private static final long serialVersionUID = 1L;
	/**
	 * @i18n miufo00403=设置数据集参数值
	 */
	private static final String RESID_SETPARAM = StringResource.getStringResource("miufo00403");
	private JPanel jContentPane = null;
	private JPanel jMainPanel = null;
	private JButton OKBtn = null;
	private JButton cancelBtn = null;

	private ReportParameter m_def = null;
	private AnaReportModel rptModel=null;
	private transient Hashtable<String,ParameterSetPanel> panels=new Hashtable<String,ParameterSetPanel>();

	/**
	 * This is the default constructor
	 */
	public ReportParamDlg(Container owner, ReportParameter params,AnaReportModel model) {
		super(owner);
		this.rptModel=model;
		this.m_def=params;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(StringResource.getStringResource(RESID_SETPARAM));
		this.setSize(500, 450);
		this.setContentPane(getJContentPane());

	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new UIPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getMainPanel(), BorderLayout.CENTER);
			JPanel panel = new UIPanel();
			panel.add(getOKBtn());
			panel.add(getCancelBtn());
			jContentPane.add(panel, BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	private JPanel getMainPanel() {
		if (jMainPanel == null) {
			jMainPanel = new UIPanel();
			jMainPanel.setSize(450, 400);
			jMainPanel.setLayout(new BorderLayout());
			if (m_def != null) {
				AreaParameter[] areaParams = m_def.getAllParams();
				if (m_def.getSize() == 1) {
					jMainPanel.add(getParamPanel(areaParams[0]), BorderLayout.CENTER);
				} else {
					JTabbedPane tabPane = new JTabbedPane();
					for (int i = 0; i < areaParams.length; i++) {
						AreaParameter aPara = areaParams[i];
						JPanel panel = getParamPanel(aPara);
						tabPane.addTab(aPara.getAreaName(), panel);
					}
					jMainPanel.add(tabPane, BorderLayout.CENTER);
				}
			}

		}
		return jMainPanel;
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
				public void actionPerformed(ActionEvent e) {
					String msg = checkReportParams();
					if (msg != null) {
						MessageDialog.showErrorDlg(ReportParamDlg.this, null, msg);
					} else {
						setResult(UfoDialog.ID_OK);
						close();
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

	public ReportParameter getReportParams() {
		return m_def;
	}

	private JPanel getParamPanel(AreaParameter areaPara) {
		ParameterSetPanel panel =this.panels.get(areaPara.getAreaPK());
		if(panel==null){
			panel= new ParameterSetPanel(this,rptModel.getDataSetTool(areaPara.getDSPK()).getDSDef(),areaPara.getParams());
			panels.put(areaPara.getAreaPK(), panel);
		}
		
		return panel;
	}

	private String checkReportParams() {
		String msg = "";
		if (panels != null) {
			Enumeration<String> areaPKs=panels.keys();
			
			while(areaPKs.hasMoreElements()){
				String subMsg=panels.get(areaPKs.nextElement()).validateParams();
				if(subMsg!=null&&subMsg.length()>0){
					msg += subMsg;
				}
				
			}
		 }
		
		if (msg.length() > 0)
			return msg;
		return null;
	}
}
 