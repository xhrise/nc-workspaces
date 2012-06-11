package com.ufida.report.anareport.applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.AreaParameter;
import com.ufida.report.rep.applet.exarea.ExAreaParamSetPanel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.exarea.ExAreaCell;

public class AnaExAreaParamSetDlg extends UfoDialog {
	private JPanel jCmdPanel = null;
	private JButton btnOK;
	private JButton btnCancel;
	private ExAreaParamSetPanel setPanel=null;
	
	public AnaExAreaParamSetDlg(Container parent,ExAreaCell exArea,AreaDataModel areaData,AreaParameter paramsDef){
		super(parent,"",true);
		setSize(600,400);
		initParamSetPanel(areaData,paramsDef);
		setTitle(getParamSetPanel().getStepTitle());
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new ExAreaInfoPanel(exArea), BorderLayout.NORTH);
		getContentPane().add(getParamSetPanel().getContentPanel(), BorderLayout.CENTER);
		getContentPane().add(getCmdPanel((AreaDataModel) exArea.getModel()), BorderLayout.SOUTH);
	}
	
	private void initParamSetPanel(AreaDataModel areaData,AreaParameter paramsDef){
		setPanel=new ExAreaParamSetPanel(areaData,paramsDef);
	}
    
	public ExAreaParamSetPanel getParamSetPanel(){
		return setPanel;
	}
	
	private JPanel getCmdPanel(AreaDataModel areaData){
		   if(jCmdPanel==null){
			   jCmdPanel=new UIPanel(new FlowLayout(FlowLayout.TRAILING));
			   btnOK=createOkButton();
			   jCmdPanel.add(btnOK);
			   btnCancel=createCancleButton();
			   jCmdPanel.add(btnCancel);
			   
		   }
		   return jCmdPanel;
	   }

	private class ExAreaInfoPanel extends UIPanel{
		
		public ExAreaInfoPanel(ExAreaCell exArea){
			super();
			setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			setBorder(BorderFactory.createTitledBorder(null, MultiLang.getString("miufo00093"), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), Color.blue));
			JPanel exNamePanel=new UIPanel(new FlowLayout(FlowLayout.LEADING));
			JLabel jlAreaName=new UILabel(MultiLang.getString("miufo00103"));
			jlAreaName.setPreferredSize(new Dimension(100,20));
			exNamePanel.add(jlAreaName);
			JTextField jtAreaText = new UITextField();
			jtAreaText.setText(exArea.toString());
			jtAreaText.setEditable(false);
			exNamePanel.add(jtAreaText);
			add(exNamePanel);
			
			JPanel exAreaPanel=new UIPanel(new FlowLayout(FlowLayout.LEADING));
			JLabel jlArea=new UILabel(MultiLang.getString("miufo00104"));
			jlArea.setPreferredSize(new Dimension(100,20));
			exAreaPanel.add(jlArea);
			JTextField jtArea = new UITextField();
			jtArea.setText(exArea.getArea().toString());
			jtArea.setEditable(false);
			exAreaPanel.add(jtArea);
			add(exAreaPanel);
			
			JPanel exDataSetPanel=new UIPanel(new FlowLayout(FlowLayout.LEADING));
			JLabel jlDataSetName=new UILabel(StringResource.getStringResource("miufo00241"));
			jlDataSetName.setPreferredSize(new Dimension(100,20));
			exDataSetPanel.add(jlDataSetName);
			AreaDataModel areaData = (AreaDataModel) exArea.getModel();
			JTextField jtDataSetName = new UITextField();
			jtDataSetName.setEditable(false);
			jtDataSetName.setText(areaData.getDSInfo().toString());
			exDataSetPanel.add(jtDataSetName);
			add(exDataSetPanel);
		}
	}
}
