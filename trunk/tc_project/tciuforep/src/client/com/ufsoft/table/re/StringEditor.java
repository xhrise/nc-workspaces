package com.ufsoft.table.re;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.text.JTextComponent;

import com.ufsoft.report.IufoFormat;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.format.TableConstant;

/**
 * 字符串类型编辑器该编辑器在格式设计时使用，在原有DefaultSheetCellRenderer的功能
 * 基础上增加日期类型指标绘制的分支。
 * @author chxiaowei 2007-3-26
 */
public class StringEditor extends DefaultSheetCellEditor {
	private SheetCellEditor strEditor = new GenericEditor(new JTextField());
	private DateEditor dateEditor = new DateEditor();
	private SheetCellEditor proxy = strEditor;
	
	/**
	 *  缺省构造函数
	 */
	public StringEditor() {
		super(new JTextField());
	}
	
	/**
	 * 参见父类 包装放入的对象都是String.
	 * 
	 * @param table
	 * @param value
	 * @param isSelected
	 * @param row
	 * @param column
	 * @return Component
	 *  
	 */
	public Component getTableCellEditorComponent(CellsPane table, Object value,
			boolean isSelected, int row, int column) {
		IufoFormat format = (IufoFormat)table.getTablePane().getCellsModel().getFormatIfNullNew(CellPosition.getInstance(row, column));
		if(TableConstant.CELLTYPE_DATE == format.getCellType()){
			proxy = dateEditor;
		} else{
			proxy = strEditor;
		}
		
		final Component comp = proxy.getTableCellEditorComponent(table, value, isSelected, row, column);
        if(comp instanceof JTextComponent){
            SwingUtilities.invokeLater(new Runnable(){
                public void run() {
                    ((JTextComponent)comp).selectAll();                    
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
		return 1;
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
