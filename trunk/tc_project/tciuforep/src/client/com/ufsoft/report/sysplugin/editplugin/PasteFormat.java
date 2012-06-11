package com.ufsoft.report.sysplugin.editplugin;

import com.ufida.zior.view.Mainboard;
import com.ufsoft.report.sysplugin.edit.EditPasteExt;
import com.ufsoft.table.Cell;

public class PasteFormat extends AbsPasteExecutor{

	public PasteFormat(Mainboard mainBoard, boolean transfer) {
		super(mainBoard, transfer);
	}

	@Override
	protected String getPasteType() {
		if(isTransfer()){
        	return EditPasteExt.TRANSFER;
        }
		return EditPasteExt.FORMAT;
	}

	@Override
	protected void paste(Cell cell,int row,int column){
		pasteFormat(cell,row,column);
			
	}
	
	/**
	 * add by 王宇光 2008-3-23 用户事件，转交插件完成
	 * 
	 * @param Object content,String pasteType,Cell[][] c
	 * @return 
	 */
	@Override
	protected boolean fireUserEvent(Object content,String pasteType,Cell[][] c){	
		return true;
	}

}
