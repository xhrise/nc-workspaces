package com.ufsoft.iufo.fmtplugin.freequery;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UITable;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufsoft.iufo.fmtplugin.freequery.MultiSelMeasureTableModel;
import com.ufsoft.iufo.fmtplugin.measure.MeasureCheckCellRenderer;
import com.ufsoft.iufo.fmtplugin.measure.MeasureRefDlg;
import com.ufsoft.iufo.fmtplugin.measure.MeasureRefRightPanelList;
import com.ufsoft.iufo.fmtplugin.measure.MeasureTableModel;
/**
 * �̳�ָ����յ�����壬ʵ��ָ���ѡ����
 * @author ll
 *
 */
public class MultiSelMeasureRefRightPanelList extends MeasureRefRightPanelList {

	private static final long serialVersionUID = 1L;
	private DefaultCellEditor editor;
	public MultiSelMeasureRefRightPanelList(MeasureRefDlg parentDlg, boolean isContainsCurrentReport,
			KeyGroupVO currentKeyGroupVO, ArrayList excludeMeasuresList, boolean bIncludeRefMeas) {
		super(parentDlg, isContainsCurrentReport, currentKeyGroupVO, excludeMeasuresList, bIncludeRefMeas);
	}
    
	@Override
	protected JTable initTable() {
  
		return new UITable(){

			@Override
			public Component prepareRenderer(TableCellRenderer renderer,
					int row, int column) {
                if(renderer instanceof MeasureCheckCellRenderer){
                	((MeasureCheckCellRenderer)renderer).setEnabled(((MultiSelMeasureTableModel)m_tablemodel).isSameKeyGroup(((MultiSelMeasureTableModel)m_tablemodel).getVO(row)));
                }
				return super.prepareRenderer(renderer, row, column);
			}
			
		};
	}

	protected void initColumnsToTable() {
		TableColumn column;
		TableCellRenderer renderer = new MeasureCheckCellRenderer();
		column = new TableColumn(0, 40, renderer, getCheckBoxEditor());
		measureListTable.addColumn(column);
		//ָ�����������
		for (int k = 0; k < MeasureTableModel.columnNames.length; k++) {
			renderer = new DefaultTableCellRenderer();
			if (k == 0) {// ������
				((DefaultTableCellRenderer)renderer).setHorizontalAlignment(JLabel.LEFT);
			} else {
				((DefaultTableCellRenderer)renderer).setHorizontalAlignment(JLabel.CENTER);
			}
			column = new TableColumn(k+1, MeasureTableModel.columnWidths[k], renderer, getCheckBoxEditor());
			measureListTable.addColumn(column);
		}
		
		getCheckBoxEditor().addCellEditorListener(new CellEditorListener(){

			public void editingCanceled(ChangeEvent e) {
				
			}

			public void editingStopped(ChangeEvent e) {
				initSelectedAction(e);
				
			}
			
		});
	}
	public DefaultCellEditor getCheckBoxEditor() {
		if (editor == null) {
			// ��һ������Ϊѡ����
			JCheckBox check = new UICheckBox();
			editor = new DefaultCellEditor(check);
		}
		return editor;

	}
	
	protected void initSelectedAction(ChangeEvent e){
		
	}
	/**
	 * ѡ��ָ����Ƿ��Զ��ر�
	 */
	protected boolean isAutoClose() {
		return false;
	}
	/**
	 * ��ʼ��tablemodel �������ڣ�(2003-7-1 14:19:23)
	 */
	protected void initTableModel() {
		m_tablemodel = new MultiSelMeasureTableModel();
	}
	@Override
	public MeasureVO[] getSelMeasureVOs() {
		return ((MultiSelMeasureTableModel)m_tablemodel).getAllSelectedMeasureVOs();
	}
	/**
	 * ���ظ��෽�������õ�ǰѡ��ı���PK
	 */
	protected MeasureVO[] getMatchingMeasureVOs(ReportVO repvo, boolean bIncludeRefMeas) {
		MeasureVO[] vos = super.getMatchingMeasureVOs(repvo, bIncludeRefMeas);
		if(vos != null ){
			MeasureVO[] multivos = new MeasureVO[vos.length];
			for (int i = 0; i < multivos.length; i++) {
				multivos[i] = vos[i].copy();
				multivos[i].setSelReportPK(repvo.getReportPK());
			}
			return multivos;
		}
		return null;
	}
	@Override
	public void setSelMeasureVOs(MeasureVO[] selMeasures) {
		((MultiSelMeasureTableModel)m_tablemodel).setAllSelectedMeasureVOs(selMeasures);
	}
	
	public void addSelectedMeasure(MeasureVO selVO){
		((MultiSelMeasureTableModel)m_tablemodel).addSelectedMeasure(selVO);
	}
	public void removeSelectedMeasure(MeasureVO selVO){
		((MultiSelMeasureTableModel)m_tablemodel).removeSelectedMeasure(selVO);
	}

	
}

