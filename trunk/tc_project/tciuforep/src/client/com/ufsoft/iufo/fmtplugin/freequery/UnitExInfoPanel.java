package com.ufsoft.iufo.fmtplugin.freequery;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.iufo.measure.UnitExInfoVO;
import com.ufsoft.iufo.fmtplugin.measure.MeasureCheckCellRenderer;

public class UnitExInfoPanel extends JPanel {
	protected UnitExInfoTableModel m_tablemodel = null;
	
	private TableCellEditor editor;
	private JScrollPane propListScrollPane = null;
	protected JTable propListTable = null;
	
	public UnitExInfoPanel() {
		setLayout(new BorderLayout());
		add(getMeasureListScrollPane(), java.awt.BorderLayout.CENTER);
//		add(getMeasureListScrollPane());
	}

	private JScrollPane getMeasureListScrollPane() {
		if (propListScrollPane == null) {
			propListScrollPane = new UIScrollPane();
			propListScrollPane.setViewportView(getMeasureListTable());
		}
		return propListScrollPane;
	}

	private JTable getMeasureListTable() {
		if (propListTable == null) {
			propListTable = new nc.ui.pub.beans.UITable();
			propListTable.setAutoCreateColumnsFromModel(false);
			propListTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			initTableModel();
			propListTable.setModel(m_tablemodel);
			propListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			m_tablemodel.addMouseListener(propListTable);
			initColumnsToTable();
		}
		return propListTable;
	}

	private void initTableModel() {
		m_tablemodel = new UnitExInfoTableModel();
	}

	private void initColumnsToTable() {
		TableColumn column;
		TableCellRenderer renderer = new MeasureCheckCellRenderer();
		column = new TableColumn(0, 40, renderer, getCheckBoxEditor());
		propListTable.addColumn(column);

		for (int k = 0; k < UnitExInfoTableModel.columnNames.length; k++) {
			renderer = new DefaultTableCellRenderer();
			if (k == 0) {// 名称列
				((DefaultTableCellRenderer) renderer)
						.setHorizontalAlignment(JLabel.LEFT);
			} else {
				((DefaultTableCellRenderer) renderer)
						.setHorizontalAlignment(JLabel.CENTER);
			}

			column = new TableColumn(k + 1, UnitExInfoTableModel.columnWidths[k], renderer, getCheckBoxEditor());
			propListTable.addColumn(column);
		}
        
		getCheckBoxEditor().addCellEditorListener(new CellEditorListener(){

			public void editingCanceled(ChangeEvent e) {
				
			}

			public void editingStopped(ChangeEvent e) {
				initSelectedAction(e);
				
			}
			
		});
		
	}
	
	public TableCellEditor getCheckBoxEditor() {
		if (editor == null) {
			// 第一列设置为选择列
			JCheckBox check = new UICheckBox();
			editor = new DefaultCellEditor(check);
		}
		return editor;

	}
	
    protected void initSelectedAction(ChangeEvent e){
		
	}
	public void setUnitInfo(UnitExInfoVO[] unitInfos){
		m_tablemodel.resetTable(unitInfos);
	}
	public void setSelUnitInfo(UnitExInfoVO[] selVOs){
		m_tablemodel.setSelPropVOs(selVOs);
	}
	public UnitExInfoVO[] getSelPropVOs(){
		return m_tablemodel.getSelPropVOs();
	}
	public UnitExInfoVO getSelectedVO(){
		return m_tablemodel.getVO(getMeasureListTable().getSelectedRow());
	}
	
	public  boolean removeSelectedPropVO(UnitExInfoVO vo){
		return m_tablemodel.removeSelectedPropVO(vo);
	}
	public void setRowSelectFromVO(UnitExInfoVO vo){
		if(vo!=null){
			if(m_tablemodel!=null){
			int anchorRow=m_tablemodel.getRowIndex(vo);
			getMeasureListTable().getSelectionModel().setSelectionInterval(anchorRow, anchorRow);
			Point newBegin=new Point(0,getMeasureListTable().getRowHeight()*anchorRow);
			if(!getMeasureListScrollPane().getViewport().getViewRect().contains(newBegin)){
				getMeasureListScrollPane().getViewport().setViewPosition(newBegin);
			}
			
			}	
		}
		
	}
}
