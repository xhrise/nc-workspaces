package com.ufsoft.report.fmtplugin.formula;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JTextField;

import com.ufsoft.script.base.AreaFormulaModel;
import com.ufsoft.script.base.FormulaVO;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.DefaultSheetCellEditor;

public class FormulaDefEditor extends DefaultSheetCellEditor {
	/**
	 * 公式插件实例
	 */
	private final AreaFormulaPlugin plugin;

	public FormulaDefEditor(AreaFormulaPlugin plugin, JTextField field) {
		super(field);
		this.plugin = plugin;
	}

	public Component getTableCellEditorComponent(CellsPane table, Object value,
			boolean isSelected, int row, int column) {
		CellPosition cellPos = CellPosition.getInstance(row,column);
		AreaFormulaModel formulaModel = AreaFormulaModel.getInstance(table.getDataModel());	
		FormulaVO fmlVO = formulaModel.getDirectFml(cellPos); 
		if(fmlVO != null){		
			new AreaFormulaActionHandler(plugin.getReport().getTable().getCells()).execute(null);
		}
		return null;
	}

	public int getEditorPRI() {
		return 2;
	}
	
	public boolean isEnabled(CellsModel cellsModel, CellPosition cellPos) {
		AreaFormulaModel formulaModel = AreaFormulaModel.getInstance(cellsModel);			
		return formulaModel.getRelatedFmlVO(cellPos)[0] != null;
	}
	
	public boolean isCellEditable(EventObject anEvent){
		return super.isCellEditable(anEvent) && anEvent instanceof MouseEvent;
	}
	
}