package com.ufida.report.anareport.applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
/**
 * 
 * @author wangyga
 *
 */
public class ExAreaPropertyDlg extends UfoDialog{
	private static final long serialVersionUID = -5184809746866526094L;

	private JTable groupInfoTable = null;
	
	private JTable detailInfoTable = null;
    
    private String[][] groupDataInfo = null;
    
    private JPanel topPanel = null;
    
    private JPanel centerPanel = null;
    
    private JPanel contentPanel = null;
    
	public ExAreaPropertyDlg(String[][] groupData,UfoReport rpt){
		super(rpt);
		this.groupDataInfo = groupData;
		initialize();
	}
	
	/**
	 * @i18n miufo00234=区域数据属性预览
	 */
	private void initialize(){
		this.setTitle(StringResource.getStringResource("miufo00234"));
		this.setSize(600,600);
		setLocationRelativeTo(this);
		setContentPane(getContentPanel());
	}
	
	
	private JTable getGroupInfoTable(){
		if(groupInfoTable == null){
			try {
				groupInfoTable = new UITable();
				groupInfoTable.setModel(new GroupFieldDataModel(groupDataInfo));
				groupInfoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return groupInfoTable;
	}
	
	private JTable getDetailInfoTable(){
		if(detailInfoTable == null){
			try {
				detailInfoTable = new UITable();
				detailInfoTable.setModel(new DetailFieldDataModel(null));
				detailInfoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return detailInfoTable;
	}
	
	private JPanel getContentPanel(){
		if(contentPanel == null){
			try {
				contentPanel = new UIPanel();
				contentPanel.setLayout(new BorderLayout());
				contentPanel.add(getTopPanel(),BorderLayout.NORTH);
				contentPanel.add(getCenterPanel(),BorderLayout.CENTER);
			} catch (Exception e) {
				handleException(e);
			}			
		}
		return contentPanel;
	}
	
	/**
	 * @i18n miufo00235=区域分组字段信息
	 */
	private JPanel getTopPanel(){
		if(topPanel == null){
			try {
				topPanel = new UIPanel();
				topPanel.setLayout(new BorderLayout());
				topPanel.setSize(new Dimension(600,150));
				topPanel.add(new UIScrollPane(getGroupInfoTable()),BorderLayout.CENTER);
				topPanel
				.setBorder(javax.swing.BorderFactory
						.createTitledBorder(
								null,
								StringResource.getStringResource("miufo00235"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
								javax.swing.border.TitledBorder.DEFAULT_POSITION,
								null, Color.BLUE));
			} catch (Exception e) {
				handleException(e);
			}			
		}
		return topPanel;
	}
	
	/**
	 * @i18n miufo00236=区域细节字段信息
	 */
	private JPanel getCenterPanel(){
		if(centerPanel == null){
			try {
				centerPanel = new UIPanel();
				centerPanel.setLayout(new BorderLayout());
				centerPanel.setSize(new Dimension(600,450));
				centerPanel.add(new UIScrollPane(getDetailInfoTable()),BorderLayout.CENTER);
				centerPanel
				.setBorder(javax.swing.BorderFactory
						.createTitledBorder(
								null,
								StringResource.getStringResource("miufo00236"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
								javax.swing.border.TitledBorder.DEFAULT_POSITION,
								null, Color.BLUE));
			} catch (Exception e) {
				handleException(e);
			}			
		}
		return centerPanel;
	}
	
	private class GroupFieldDataModel extends AbstractTableModel{
		private static final long serialVersionUID = 1L;
        
		private String[][] data = null;
		
		/**
		 * @i18n miufo00237=分组字段
		 * @i18n miufopublic307=合计
		 * @i18n miufo1000033=计算
		 * @i18n miufo1001258=平均
		 * @i18n miufo5308000005=最大
		 * @i18n miufo1001257=最小
		 */
		private final String[] groupFieldColumnNames = {StringResource.getStringResource("miufo00237"), StringResource.getStringResource("miufopublic307"), StringResource.getStringResource("miufo1000033"), StringResource.getStringResource("miufo1001258"), StringResource.getStringResource("miufo5308000005"), StringResource.getStringResource("miufo1001257")};
		
		public GroupFieldDataModel(String[][] data){
			this.data = data;
		}
		
		public int getColumnCount() {
			return groupFieldColumnNames.length;
		}

		public int getRowCount() {
			if(data == null)
				return 0;
			return data.length;
		}
		
		public String getColumnName(int column) {			
			return groupFieldColumnNames[column];
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex < 0 || columnIndex < 0 || rowIndex > getRowCount()
					|| columnIndex > getColumnCount()) {
				throw new IllegalArgumentException(StringResource.getStringResource("mhbmeet00002"));//请输入不小于0的值
			}
			return data[rowIndex][columnIndex];
		}		
	}
	
	private class DetailFieldDataModel extends AbstractTableModel{
		private static final long serialVersionUID = 1L;

		private String[][] data = null;
		
		/**
		 * @i18n miufo00238=细节字段
		 * @i18n miufo00239=固定成员
		 * @i18n miufo00240=升降序
		 * @i18n miufo00156=排名函数
		 */
		private String[] detailFieldColumnNames = {StringResource.getStringResource("miufo00238"), StringResource.getStringResource("miufo00239"), StringResource.getStringResource("miufo00240"), "topN",StringResource.getStringResource("miufo00156")};
		
		public DetailFieldDataModel(String[][] data){
			this.data = data;
		}
		
		public int getColumnCount() {
			return detailFieldColumnNames.length;
		}

		public int getRowCount() {
			if(data == null)
				return 0;
			return data.length;
		}

		public String getColumnName(int column) {			
			return detailFieldColumnNames[column];
		}
		
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex < 0 || columnIndex < 0 || rowIndex > getRowCount()
					|| columnIndex > getColumnCount()) {
				throw new IllegalArgumentException(StringResource.getStringResource("mhbmeet00002"));//请输入不小于0的值
			}
			return data[rowIndex][columnIndex];
		}		
	}
	
	/**
	 * 每当部件抛出异常时被调用
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		AppDebug.debug(exception);
	}
}
 