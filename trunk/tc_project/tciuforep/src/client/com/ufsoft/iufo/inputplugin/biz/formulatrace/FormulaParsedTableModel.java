package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import javax.swing.table.AbstractTableModel;

import com.ufsoft.iuforeport.tableinput.applet.FormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedCalInfo;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedTraceInfo;
import com.ufsoft.report.util.MultiLang;

public class FormulaParsedTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private FormulaParsedDataItem[] m_oFormulaParsedDataItems = null;
    //列头名称
    /**
	 * @i18n uiuforep00067=公式子项内容
	 * @i18n uiuforep00068=子项值
	 * @i18n uiuforep00069=联查
	 */
	public static String[] S_HEADS = new String[] { MultiLang.getString("uiuforep00067"), MultiLang.getString("uiuforep00068"), MultiLang.getString("uiuforep00069") };//TODO
	public static int COL_CONTENT = 0;
	public static int COL_CALCULATE = 1;
	public static int COL_TRACE = 2;
	/**
	 * FormulaParsedTableModel 构造子注解。
	 */
	public FormulaParsedTableModel(FormulaParsedDataItem[] formulaParsedDataItems) {
		super();
		this.m_oFormulaParsedDataItems = formulaParsedDataItems;
	}
	public String getColumnName(int column) {
		return S_HEADS[column];
	}
	/**
	 * 
	 * @param formulaParsedDataItems
	 */
	public void resetModel(FormulaParsedDataItem[] formulaParsedDataItems) {
		m_oFormulaParsedDataItems = formulaParsedDataItems;
		fireTableDataChanged();
	}
	
	public int getColumnCount() {
		return 3;
	}

	public int getRowCount() {
		return m_oFormulaParsedDataItems!=null?m_oFormulaParsedDataItems.length:0;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex <0 || rowIndex >= m_oFormulaParsedDataItems.length){
			return null;
		}
		if(columnIndex == 0){
			return m_oFormulaParsedDataItems[rowIndex].getDisContent();
		}else if(columnIndex == 1){
			IFormulaParsedCalInfo formulaParsedCalInfo = FormulaParsedTMColValueData.copyInstance(m_oFormulaParsedDataItems[rowIndex]);
			return formulaParsedCalInfo;
		}else if(columnIndex == 2){
			IFormulaParsedTraceInfo formulaParsedTraceInfo = FormulaParsedTMColTraceData.copyInstance(m_oFormulaParsedDataItems[rowIndex]);
			return formulaParsedTraceInfo;
		}
		return null;
	}

}
 