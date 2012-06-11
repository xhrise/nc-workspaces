package com.ufsoft.table.re;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.ufsoft.table.CellsPane;
/**
 * 只读编辑器
 * 当判断一个单元格可编辑，但又想控制其不能编辑时，使用这个编辑器。
 * @author zzl 2005-8-12
 */
public class ReadOnlyEditor extends DefaultSheetCellEditor{
    
    /**
     * @param textField
     */
    public ReadOnlyEditor(JLabel label) {
        super(label);        
    }

    public Component getTableCellEditorComponent(CellsPane table, Object value,
            boolean isSelected, int row, int column) {
        delegate.setValue(value);
        return editorComponent;
    }
}
