package com.ufsoft.report.sysplugin.dnd;

import java.awt.Point;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 * 
 * @author zzl 2005-4-25
 */
public class TableDndAdapter implements IDndAdapter {

    private JTable m_table;

    /**
     * 
     */
    public TableDndAdapter(JTable table) {
        m_table = table;
        m_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /*
     * @see dnd.IDndAdapter#getSourceObject()
     */
    public Object getSourceObject() {
        int row = m_table.getSelectedRow();
        int col = m_table.getSelectedColumn();
        return m_table.getValueAt(row,col);   
    }

    /*
     * @see dnd.IDndAdapter#removeSourceNode()
     */
    public void removeSourceNode() {
        int row = m_table.getSelectedRow();
        int col = m_table.getSelectedColumn();
        m_table.setValueAt(null,row,col);        
    }

    /*
     * @see dnd.IDndAdapter#insertObject(java.awt.Point, java.lang.Object)
     */
    public boolean insertObject(Point ap, Object obj) {
        int row = m_table.rowAtPoint(ap);
        int col = m_table.columnAtPoint(ap);
        m_table.setValueAt(obj,row,col);
        return true;
    }
   

}
