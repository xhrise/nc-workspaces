package com.ufsoft.report.sysplugin.editplugin;

import com.ufida.zior.view.Mainboard;
import com.ufsoft.report.sysplugin.edit.EditPasteExt;
import com.ufsoft.table.Cell;

public class PasteContent extends AbsPasteExecutor{

	public PasteContent(Mainboard mainBoard, boolean transfer) {
		super(mainBoard, transfer);
	}

	@Override
	protected boolean fireUserEvent(Object content, String pasteType, Cell[][] c) {
		return userEventAction(content,getPasteType(),c,getTable());
	}

	@Override
	protected String getPasteType() {
		if(isTransfer()){
        	return EditPasteExt.TRANSFER;
        }
		return EditPasteExt.CONTENT;
	}

	@Override
	protected void paste(Cell cell, int row, int column) {
		pasteValue(cell, row, column);//ֵ	
		
	}

}
