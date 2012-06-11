/*
 * 创建日期 2006-9-18
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import nc.util.iufo.pub.UFOString;

import com.ufsoft.iufo.inputplugin.biz.file.ChooseRepData;
import com.ufsoft.iufo.resource.StringResource;

public class ImportExcelTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -2851996646186425383L;
	private int changeCount = 0; //记录全选全消的flag标记是应为true还是false
    /**
     * 表行数据
     */
    private ImportExcelTableRow[] m_oImportExcelTableRows = null;
    /**
     * 列头名称
     */
    private String[] m_strHeads = null;
    protected final static int COL_SELECT = 0;
    protected final static int COL_IUFO_REPS = 1;
    protected final static int COL_EXCEL_SHEETS = 2;
    protected final static int COL_DYNAREA_ENDROW = 3;

    public ImportExcelTableModel(ImportExcelTableRow[] importExcelTableRows) {
        super();
        if(importExcelTableRows == null){
            importExcelTableRows = new ImportExcelTableRow[0];
        }
        //设置表头
        //选择，IUFO报表，Excel工作表，动态区结束行位置
        String[] strHeads = new String[4];
        strHeads[0] = StringResource.getStringResource("miufopublic285")+"/"+
            StringResource.getStringResource("miufo1000160");//"全选/全消"
        String[] strHeadCommon = ImportExcelDataBizUtil.getTableColumns();
        System.arraycopy(strHeadCommon,0,strHeads,1,3);
        
        m_strHeads = strHeads;
        
        //设置表数据
        m_oImportExcelTableRows = importExcelTableRows;
    }
    /**
     * 取消全部提取标志 创建日期：(2002-6-3 16:24:47)
     * 
     * @param table
     *            javax.swing.JTable
     */
    public void addMouseListener(final JTable ta) {
        ta.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                int col = ta.columnAtPoint(event.getPoint());
                if (col == COL_SELECT) {
                    for (int i = 0; i < m_oImportExcelTableRows.length; i++) {
                        if (changeCount % 2 == 0) {
                            m_oImportExcelTableRows[i].setIsSelected(true);
                        } else {
                            m_oImportExcelTableRows[i].setIsSelected(false);
                        }
                    }
                    changeCount += 1;

                }
                fireTableDataChanged();
            }
        });
    }
    protected ImportExcelTableRow[] getRows(){
        return m_oImportExcelTableRows;
    }
    protected String[] getHeads() {
        return m_strHeads;
    }

    public int getRowCount() {
        if(m_oImportExcelTableRows != null){
            return m_oImportExcelTableRows.length;
        }
        return 0;
    }
    public String getColumnName(int column) {
        if(m_strHeads!= null){
            return m_strHeads[column];
        }
        return super.getColumnName(column); 
    }
    public int getColumnCount() {
        if(m_strHeads!= null){
            return m_strHeads.length;
        }
        return 0;
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //只有sheentName列不允许编辑
        if(columnIndex != COL_EXCEL_SHEETS ){
            return true;
        }
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ImportExcelTableRow row = (ImportExcelTableRow) getRows()[rowIndex];
        switch (columnIndex) {
            case COL_SELECT :
                return new Boolean(row.isSelected());
            case COL_IUFO_REPS :
                return row.getChooseRepData();
            case COL_EXCEL_SHEETS :
                return row.getSheetName();
            case COL_DYNAREA_ENDROW :
                return row.getDynAreaEndRow()>0?row.getDynAreaEndRow():"";
            default :
                break;
        }
        return "";
    }
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ImportExcelTableRow row = (ImportExcelTableRow) getRows()[rowIndex];
        switch (columnIndex) {
            case COL_SELECT :
                row.setIsSelected(((Boolean)aValue).booleanValue());
                break;
            case COL_IUFO_REPS :
                row.setChooseRepData((ChooseRepData)aValue);
                break;
            case COL_DYNAREA_ENDROW :
                String strDynAreaEndRow = (String)aValue;
                if(UFOString.isInt(strDynAreaEndRow)){
                    row.setDynAreaEndRow(new Integer(strDynAreaEndRow).intValue());
                }
                break;
            default :
                break;
        }
        
    }

}
 