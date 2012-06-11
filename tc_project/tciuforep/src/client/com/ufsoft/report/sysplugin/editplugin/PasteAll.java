package com.ufsoft.report.sysplugin.editplugin;

import com.ufida.zior.view.Mainboard;
import com.ufsoft.report.sysplugin.edit.EditPasteExt;
import com.ufsoft.table.Cell;

public class PasteAll extends AbsPasteExecutor{

	public PasteAll(Mainboard mainBoard, boolean transfer) {
		super(mainBoard, transfer);
	}

	@Override
	protected boolean fireUserEvent(Object content, String pasteType, Cell[][] c) {
		return userEventAction(content,getPasteType(),c,getTable());
	}

	@Override
	protected String getPasteType() {
		if (isTransfer()) {
			return EditPasteExt.TRANSFER;
		}
		return EditPasteExt.ALL;
	}

	@Override
	protected void paste(Cell cell, int row, int column) {
		if(cell == null){
			return;
		}
	
		pasteAll(cell,row,column);
	}

}
