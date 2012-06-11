package com.ufsoft.table.header;

import java.beans.PropertyChangeEvent;

import com.ufsoft.report.undo.ReportUndo;

public class HeaderSizeUndo extends ReportUndo {

	//����ֱ��ά��Header����Ϊ�п����ں���������ɾ��Header��Ȼ��undo
	private HeaderModel hmodel = null;
	private int index = -1;
	private int oldSize = 0;
	private int newSize = 0;
	
	
	public HeaderSizeUndo(HeaderModel hmodel, int index, int oldSize, int newSize){
		this.hmodel = hmodel;
		this.index = index;
		this.oldSize = oldSize;
		this.newSize = newSize;
		
	}
	 
	private void undoredo(boolean isUndo){
		Header header = hmodel.getHeader(index);
		if(header == null){
			return;
		}
		int size = isUndo? oldSize: newSize;
		header.setSize(size);
		
		PropertyChangeEvent evt = new PropertyChangeEvent(
				new Header[] { header },
				Header.HEADER_SIZE_PROPERTY, null, index);
		hmodel.fireHeaderPropertyChanged(evt);
	}
	
	@Override
	public void redo() {
		undoredo(false);
	}

	@Override
	public void undo() {
		undoredo(true);
		
	}

	
}
