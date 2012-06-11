package com.ufsoft.iufo.inputplugin.querynavigation;

import com.ufsoft.table.CellPosition;

import nc.ui.iufo.input.edit.RepDataEditor;

public class FormulaTraceNavView {

	private RepDataEditor editor;
	
	private CellPosition cellPos;
	
	public FormulaTraceNavView(RepDataEditor editor,CellPosition cellPos){
		this.editor = editor;
		this.cellPos = cellPos;
	}
	@Override
	public boolean equals(Object obj){
		FormulaTraceNavView param = (FormulaTraceNavView) obj;
		if(param.getEditor().getId().equals(this.editor.getId()) && param.getCellPos().toString().equals(cellPos.toString()))
			return true;
		else
			return false;
	}
	
	
	public RepDataEditor getEditor() {
		return editor;
	}
	public void setEditor(RepDataEditor editor) {
		this.editor = editor;
	}
	public CellPosition getCellPos() {
		return cellPos;
	}
	public void setCellPos(CellPosition cellPos) {
		this.cellPos = cellPos;
	}
}
