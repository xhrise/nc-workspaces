package com.ufsoft.report.sysplugin.editplugin;

import com.ufida.zior.view.Mainboard;
import com.ufsoft.report.sysplugin.edit.EditPasteExt;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;

public class PastePostil extends AbsPasteExecutor{

	public PastePostil(Mainboard mainBoard, boolean transfer) {
		super(mainBoard, transfer);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean fireUserEvent(Object content, String pasteType, Cell[][] c) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected String getPasteType() {
		if(isTransfer()){
        	return EditPasteExt.TRANSFER;
        }
		return EditPasteExt.POSTIL;
	}

	@Override
	protected void paste(Cell cell, int row, int column) {
		if(cell == null){
			return;
		}
		CellPosition cellPosition = CellPosition.getInstance(cell.getRow(), cell.getCol());
		pastePostil(row, column,
				cellPosition);//Åú×¢
	}

}
