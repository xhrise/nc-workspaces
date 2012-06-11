package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import javax.swing.table.AbstractTableModel;

import com.ufsoft.iuforeport.tableinput.applet.IFormulaTraceValueItem;
import com.ufsoft.report.util.MultiLang;

public class MultiValueTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private IFormulaTraceValueItem[] m_oFormulaTraceValueItems = null;
	int nColumnCount = 0;
	public MultiValueTableModel(IFormulaTraceValueItem[] formulaTraceValueItems){
		this.m_oFormulaTraceValueItems = formulaTraceValueItems;
		nColumnCount = doCalColCount(formulaTraceValueItems);
	}
	protected static int doCalColCount(IFormulaTraceValueItem[] formulaTraceValueItems){
		if(formulaTraceValueItems!=null && formulaTraceValueItems.length > 0){
			int nKeyCount = formulaTraceValueItems[0].getKeyNames()!=null?formulaTraceValueItems[0].getKeyNames().length:0;
			if(nKeyCount >=0){
				return nKeyCount+1;
			}
		}
		return 0;
	}
    /**
	 * @i18n report00104=ֵ
	 */
    public String getColumnName(int column) {
		int nColumnCount = getColumnCount();
		if(nColumnCount <=0 ){
			return null;
		}
    	if(column <0 || column >= nColumnCount){
			return null;
		}
		IFormulaTraceValueItem curFormulaTraceValueItem =  m_oFormulaTraceValueItems[0];
		if(column >= 0 && column < nColumnCount-1){
			return curFormulaTraceValueItem.getKeyNames()[column];
		}else if(column == nColumnCount-1){
			return MultiLang.getString("report00104");
			////TODO
		}
		return null;
    }
	public int getColumnCount() {		
		return nColumnCount;
	}

	public int getRowCount() {
		if(m_oFormulaTraceValueItems!=null){
			return m_oFormulaTraceValueItems.length;
		}
		return 0;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex <0 || rowIndex >= m_oFormulaTraceValueItems.length){
			return null;
		}
		int nColumnCount = getColumnCount();
		IFormulaTraceValueItem curFormulaTraceValueItem =  m_oFormulaTraceValueItems[rowIndex];
		if(columnIndex >= 0 && columnIndex < nColumnCount-1){
			return curFormulaTraceValueItem.getKeyValues()[columnIndex];
		}else if(columnIndex == nColumnCount-1){
			return curFormulaTraceValueItem.getValue();
		}
		return null;
	}

}
 