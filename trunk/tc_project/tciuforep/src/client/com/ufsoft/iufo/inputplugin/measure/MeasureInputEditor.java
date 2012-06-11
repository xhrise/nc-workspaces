package com.ufsoft.iufo.inputplugin.measure;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.text.JTextComponent;


import com.ufsoft.iufo.inputplugin.editor.RepDataCodeRefEditor;
import com.ufsoft.iufo.inputplugin.editor.RepDataDateEditor;
import com.ufsoft.iufo.inputplugin.editor.RepDataDoubleEditor;
import com.ufsoft.iufo.inputplugin.editor.RepDataStringEditor;
import com.ufsoft.iufo.inputplugin.inputcore.StringEditor;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellEditor;

/**
 * 
 * @author zzl 2005-6-16
 */
public class MeasureInputEditor implements SheetCellEditor {
    private SheetCellEditor proxy = new StringEditor();

    /**
     * @param table
     * @param value
     * @param isSelected
     * @param row
     * @param column
     * @return
     * @see com.ufsoft.table.re.SheetCellEditor#getTableCellEditorComponent(com.ufsoft.table.CellsPane, java.lang.Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(CellsPane table, Object value, boolean isSelected, int row,int column) {
        MeasureFmt fmt = (MeasureFmt) table.getDataModel().getBsFormat(CellPosition.getInstance(row,column),MeasureFmt.EXT_FMT_MEASUREINPUT);
        if(fmt != null){
            if(fmt.getType() == MeasureFmt.TYPE_NUMBER){
                proxy = new RepDataDoubleEditor();
            }else if(fmt.getType() == MeasureFmt.TYPE_CHAR){
            	StringEditor strEditor = new RepDataStringEditor();
                strEditor.setCharLength(fmt.getCharLength());
                proxy = strEditor;
            }else if(fmt.getType() == MeasureFmt.TYPE_REF_CODE){
                proxy = new RepDataCodeRefEditor();
            }else if(fmt.getType() == MeasureFmt.TYPE_REF_DATE){
            	proxy = new RepDataDateEditor();
            }else{
                throw new IllegalArgumentException();
            }
        }
        final Component comp = proxy.getTableCellEditorComponent(table, value, isSelected, row, column);
        if(comp instanceof JTextComponent){
            SwingUtilities.invokeLater(new Runnable(){
                public void run() {
                    ((JTextComponent)comp).selectAll();   
                    ((JTextComponent)comp).requestFocus();
                }                
            });
        }
        return comp;
    }
    /**
     * @return
     * @see javax.swing.CellEditor#stopCellEditing()
     */
    public boolean stopCellEditing() {
        return proxy.stopCellEditing();
    }       
    /**
     * @return
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue() {
        return proxy.getCellEditorValue();
    }
	/**
	 *  @see com.ufsoft.table.re.SheetCellEditor#getEditorPRI()
	 */
	public int getEditorPRI() {
		return 2;
	}
    /**
     * @param anEvent
     * @return
     * @see javax.swing.CellEditor#isCellEditable(java.util.EventObject)
     */
    public boolean isCellEditable(EventObject anEvent) {
        return proxy.isCellEditable(anEvent);
    }
    /**
     * @param anEvent
     * @return
     * @see javax.swing.CellEditor#shouldSelectCell(java.util.EventObject)
     */
    public boolean shouldSelectCell(EventObject anEvent) {
        return proxy.shouldSelectCell(anEvent);
    }
    /**
     * 
     * @see javax.swing.CellEditor#cancelCellEditing()
     */
    public void cancelCellEditing() {
        proxy.cancelCellEditing();
    }
    /**
     * @param l
     * @see javax.swing.CellEditor#addCellEditorListener(javax.swing.event.CellEditorListener)
     */
    public void addCellEditorListener(CellEditorListener l) {
        proxy.addCellEditorListener(l);
        
    }
    /**
     * @param l
     * @see javax.swing.CellEditor#removeCellEditorListener(javax.swing.event.CellEditorListener)
     */
    public void removeCellEditorListener(CellEditorListener l) {
        proxy.removeCellEditorListener(l);
        
    }
	public boolean isEnabled(CellsModel cellsModel, CellPosition cellPos) {
		return true;
	}

}
