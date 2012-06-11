package com.ufida.report.anareport.fmtplugin;

import java.awt.Component;

import javax.swing.JTextField;

import com.ufsoft.report.fmtplugin.formula.FormulaDefEditor;
import com.ufsoft.script.base.FormulaVO;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
/**
 * 分析报表公式编辑器
 * @author wangyga
 *
 */
public class AnaFormulaDefEditor extends FormulaDefEditor{

	private final AnaAreaFormulaPlugin plugin;
	
	public AnaFormulaDefEditor(AnaAreaFormulaPlugin plugin, JTextField field) {
		super(plugin, field);
		this.plugin = plugin;
	}

	@Override
	public Component getTableCellEditorComponent(CellsPane table, Object value,
			boolean isSelected, int row, int column) {
		CellPosition cellPos = CellPosition.getInstance(row,column);
		FormulaVO fmlVO = plugin.getFmlExecutor().getFormulaModel().getDirectFml(cellPos); 
		if(fmlVO != null){		
			plugin.new AnaAreaFormulaDefCmd().execute(new Object[]{plugin.getReport()});
		}
		return null;
	}
    
	
}
