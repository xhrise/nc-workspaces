/**
 * 
 */
package com.ufida.report.chart;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import nc.ui.pub.beans.UIPanel;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.chart.ChartModel;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author wangyga
 * @created at 2009-5-27,œ¬ŒÁ12:45:09
 *
 */
public class CombineChartParamDlg extends UfoDialog{

	private static final long serialVersionUID = 1L;
	
	private JPanel buttonPanel = null;
	
	private ChartSettingPanel paramSettingPanel = null;
	
	private ChartModel chartModel = null;
	
	public CombineChartParamDlg(Container parent, ChartModel chartModel){
		super(parent);
		this.chartModel =  chartModel;
		initContentPane();
	}
	
	/**
	 * @i18n ubichart00014=Õº±Ì…Ë÷√
	 */
	private void initContentPane(){
		try {
			setTitle(StringResource.getStringResource("ubichart00014"));
			setSize(new Dimension(520,350));
			Container container = getContentPane();
			container.setLayout(new BorderLayout());
			container.add(getParamSettingPanel(),BorderLayout.CENTER);
			container.add(getButtonPanel(),BorderLayout.SOUTH);
			
		} catch (Throwable e) {
			AppDebug.debug(e);
		}
	}
	
	private JPanel getButtonPanel(){
		if(buttonPanel == null){
			buttonPanel = new UIPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,20,5));
			
			buttonPanel.add(createOkButton());
			buttonPanel.add(createCancleButton());
		}
		return buttonPanel;
	}
	
	private ChartSettingPanel getParamSettingPanel(){
		if(paramSettingPanel == null){
			paramSettingPanel = new CategorySettingPanel(chartModel);
		}
		return paramSettingPanel;
	}

	@Override
	protected JButton createOkButton() {
		JButton okButton = new nc.ui.pub.beans.UIButton();
		okButton.setText(MultiLang.getString("ok"));
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					getParamSettingPanel().save();
				} catch (Throwable e1) {
					UfoPublic.showErrorDialog(CombineChartParamDlg.this, e1
							.getMessage(), "");
					return;
				}
				
				setResult(ID_OK);
				close();
			}
		});
		return okButton;
	}	
}
 