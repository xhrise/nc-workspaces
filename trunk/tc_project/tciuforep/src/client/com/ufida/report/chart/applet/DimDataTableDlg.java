package com.ufida.report.chart.applet;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nc.ui.pub.beans.UIButton;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.chart.model.DimChartModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

public class DimDataTableDlg extends UfoDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane = null;
	private DimDataTable dimDataTable = null;
	private UIButton btnOK = null;  //  @jve:decl-index=0:visual-constraint="125,210"
	private UIButton btnCancle = null;  //  @jve:decl-index=0:visual-constraint="205,211"
	private UIButton btnApply = null;
	private DimChartModel chartModel = null;	
	/**
	 * This is the default constructor
	 */
	public DimDataTableDlg(Container parent, DimChartModel chartModel) {
		super(parent);
		this.chartModel = chartModel;
		initialize();
	}

	private DimChartModel getChartModel(){
		return chartModel;
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(600, 400);
		
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
			JPanel panel = new JPanel();
			panel.add(getBtnOK());
			panel.add(getBtnCancle());
			panel.add(getBtnApply());			
			jContentPane.add(panel, java.awt.BorderLayout.SOUTH);		
		}
		return jContentPane;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getDimDataTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes dimDataTable	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	private DimDataTable getDimDataTable() {
		if (dimDataTable == null) {
			dimDataTable = new DimDataTable(getChartModel());
		}
		return dimDataTable;
	}

	/**
	 * This method initializes btnOK	
	 * 	
	 * @return javax.swing.UIButton	
	 * @i18n miufo1003314=确定
	 */    
	private UIButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new UIButton();
			btnOK.setText(StringResource.getStringResource("miufo1003314"));
			btnOK.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(applySelectedRowCols()){
						DimDataTableDlg.this.setVisible(false);
						DimDataTableDlg.this.close();	
					}									
				}});
		}
		return btnOK;
	}

	/**
	 * This method initializes btnCancle	
	 * 	
	 * @return javax.swing.UIButton	
	 * @i18n miufo1003315=取消
	 */    
	private UIButton getBtnCancle() {
		if (btnCancle == null) {
			btnCancle = new UIButton();
			btnCancle.setText(StringResource.getStringResource("miufo1003315"));
			btnCancle.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					DimDataTableDlg.this.setVisible(false);
					DimDataTableDlg.this.close();					
				}});
		}
		return btnCancle;
	}
	
	/**
	 * @i18n uibichart00019=行(列)数据不能为空
	 */
	private boolean applySelectedRowCols(){
		String[] rows = getDimDataTable().getSelectedRowNames();
		String[] cols = getDimDataTable().getSelectedColumnNames();
		if(rows == null || rows.length < 1 || cols == null || cols.length < 1){
			JOptionPane.showMessageDialog(this,StringResource.getStringResource("uibichart00019"), "", JOptionPane.ERROR_MESSAGE);
			return false;
		}			
		getChartModel().setChartViewData(rows, cols);		
		//print(rows, cols);
		return true;
	}
	
	@SuppressWarnings("unused")
	private void print(String[] rows, String[] cols){
//		AppDebug.debug();//@devTools System.out.println();
		AppDebug.debug("selected row name : ");//@devTools System.out.print("selected row name : ");
		for(int i = 0; i < rows.length; i++){
			AppDebug.debug(" | " + rows[i]);//@devTools System.out.print(" | " + rows[i]);
		}
		
//		AppDebug.debug();//@devTools System.out.println();
		AppDebug.debug("selected column name : ");//@devTools System.out.print("selected column name : ");
		for(int i = 0; i < cols.length; i++){
			AppDebug.debug(" | " + cols[i]);//@devTools System.out.print(" | " + cols[i]);
		}
	}
	/**
	 * This method initializes btnApply	
	 * 	
	 * @return javax.swing.UIButton	
	 * @i18n miufo1001112=应用
	 */    
	private UIButton getBtnApply() {
		if (btnApply == null) {
			btnApply = new UIButton();
			btnApply.setText(StringResource.getStringResource("miufo1001112"));
			btnApply.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					applySelectedRowCols();			
				}});
		}
		return btnApply;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		DimDataTableDlg dlg = new DimDataTableDlg(null,null);	
		dlg.setLocationRelativeTo(null);         
		dlg.setVisible(true);		
	}
}
