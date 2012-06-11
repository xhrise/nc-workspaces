package com.ufsoft.iufo.fmtplugin.measure;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JTextField;

import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.key.AbsEditorAction;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.DefaultSheetCellEditor;

public class MeasureDefEditor extends DefaultSheetCellEditor{

	public MeasureDefEditor() {
        super(new JTextField());
    }
	
    public Component getTableCellEditorComponent(CellsPane table, Object value,
    		boolean isSelected, int row, int column) {

    	AbsEditorAction editorAction = new MeasureDefEditorAction(table);
    	editorAction.execute(editorAction.getParams());
    	
//		CellsModel cellsModel = table.getDataModel();
//    	new MeasureDefineCmd().execute(new Object[]{contain,
//    			new CellPosition[]{CellPosition.getInstance(row,column)},cellsModel});
    	return null;
    }
    /**
     * modify by wangyga 2008-7-1 指标编辑器的优先级由1修改为2：控制数值和日期型指标不可编辑
     * 由于数值和日期编辑器放回的优先级都是1
     */
    public int getEditorPRI() {
    	return 2;
    }
    public boolean isCellEditable(EventObject anEvent){
        if(super.isCellEditable(anEvent) && anEvent != null && anEvent instanceof MouseEvent){
            return true;
        }
        return false;
    }
	public boolean isEnabled(CellsModel cellsModel, CellPosition cellPos) {
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		return dynAreaModel.hasMeasureAfterDataProcess(cellPos.getRow(),cellPos.getColumn());
	}
}
