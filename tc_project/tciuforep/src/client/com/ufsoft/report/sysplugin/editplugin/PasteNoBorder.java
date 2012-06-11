package com.ufsoft.report.sysplugin.editplugin;

import com.ufida.zior.view.Mainboard;
import com.ufsoft.report.sysplugin.edit.EditPasteExt;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;

public class PasteNoBorder extends AbsPasteExecutor{

	public PasteNoBorder(Mainboard mainBoard, boolean transfer) {
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
		return EditPasteExt.NO_BORDER;
	}

	@Override
	protected void paste(Cell cell, int row, int column) {
		if(cell == null){
			return;
		}
		CellPosition cellPosition = CellPosition.getInstance(cell.getRow(), cell.getCol());
		pasteValue(cell, row, column);//ֵ
		pasteNoBorder(row, column,cell);//�߿����			
		pastePostil(row, column,
				cellPosition);//��ע
		pasteColumnWidth(column, cell.getCol());//�п�
		
	}

}
