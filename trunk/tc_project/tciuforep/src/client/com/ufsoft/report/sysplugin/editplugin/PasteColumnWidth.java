package com.ufsoft.report.sysplugin.editplugin;

import com.ufida.zior.view.Mainboard;
import com.ufsoft.report.sysplugin.edit.EditPasteExt;
import com.ufsoft.table.Cell;

public class PasteColumnWidth extends AbsPasteExecutor{

	public PasteColumnWidth(Mainboard mainBoard, boolean transfer) {
		super(mainBoard, transfer);
	}

	@Override
	protected boolean fireUserEvent(Object content, String pasteType, Cell[][] c) {
		return true;
	}

	@Override
	protected String getPasteType() {
		if(isTransfer()){
        	return EditPasteExt.TRANSFER;
        }
		return EditPasteExt.WIDTH;
	}

	@Override
	protected void paste(Cell cell, int row, int column) {
		pasteColumnWidth(column, cell.getCol());//ап©М	
	}
}
