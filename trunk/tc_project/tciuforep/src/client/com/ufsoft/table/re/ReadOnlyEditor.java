package com.ufsoft.table.re;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.ufsoft.table.CellsPane;
/**
 * ֻ���༭��
 * ���ж�һ����Ԫ��ɱ༭������������䲻�ܱ༭ʱ��ʹ������༭����
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
