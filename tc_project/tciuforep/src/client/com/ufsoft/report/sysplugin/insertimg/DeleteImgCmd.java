/*
 * InsertCellCmd.java
 * Created on 2004-10-19 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.report.sysplugin.insertimg;

import javax.swing.ImageIcon;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectModel;

/**
 * µ¥Ôª¸ñÉ¾³ýÍ¼Æ¬
 * @author liuyy 
 */
public class DeleteImgCmd extends UfoCommand {
	private UfoReport m_report;

 
	public DeleteImgCmd(UfoReport rep) {
		super();
		this.m_report = rep;
	}
	/* Overrding method
	 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
	 */
	public void execute(Object[] params) {
	    
	    CellsModel cellsModel = m_report.getCellsModel();
		SelectModel selectModel = cellsModel.getSelectModel();
	    CellPosition[] arrPos = selectModel.getSelectedCells();
	    if(arrPos == null || arrPos.length < 1){
	    	return;
	    }
	    for (int i = 0; i < arrPos.length; i++) {
	    	CellPosition pos = arrPos[i];
			Object value = cellsModel.getCellValue(pos);
	    	if(value != null && value instanceof ImageIcon){
		    	cellsModel.setCellValue(pos, null);
	    	}			
		}
	}
	 
 
}

