/*
 * InsertCellCmd.java
 * Created on 2004-10-19 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.report.sysplugin.insertdelete;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectModel;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;

/**
 * 报表工具中插入单元命令
 * @author CaiJie
 * @since 3.1
 */
public class InsertCellCmd extends UfoCommand {
	private UfoReport m_report;

	/**
	 * @param rep UfoReport - 报表
	 */
	public InsertCellCmd(UfoReport rep) {
		super();
		this.m_report = rep;
	}
	/* Overrding method
	 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
	 */
	public void execute(Object[] params) {
	    if(params == null){
	        return;
	    }
	    int insertType = ((Integer)(params[0])).intValue();
	    AreaPosition[] areapos = getSelectArea();//modify by wangyga 2008-9-3
	    if(areapos == null || areapos.length == 0)
	    	return;
	    insertCells(areapos[0],insertType);
	}
	/**
	 * 返回选定区域扩展方向上的区域
	 * @param aimArea
	 * @param insertType
	 * @param cellsModel
	 * @return
	 */
	public static AreaPosition getToMoveArea(AreaPosition aimArea, int insertType, CellsModel cellsModel){
		AreaPosition toMoveArea;
		CellPosition startCellPos = aimArea.getStart();
		CellPosition endCellPos;			
		if(insertType == DeleteInsertDialog.CELL_MOVE_DOWN){
		    endCellPos = CellPosition.getInstance(cellsModel.getRowNum()-1,aimArea.getEnd().getColumn());
		}else if(insertType == DeleteInsertDialog.CELL_MOVE_RIGHT){
		    endCellPos = CellPosition.getInstance(aimArea.getEnd().getRow(),cellsModel.getColNum()-1);
		}else{
		    throw new IllegalArgumentException();
		}
		return AreaPosition.getInstance(startCellPos,endCellPos);
	}
	
	/**
	 * 插入单元格.
	 * 
	 * @param aimArea
	 * @param insertType
	 *            见InsertCellDlg:MOVE_RIGHT_WHEN_INSERT_CELL,MOVE_DOWN_WHEN_INSERT_CELL
	 */
	public void insertCells(final AreaPosition aimArea, final int insertType) {
		if (aimArea == null)
			return;
		UFOTable table = m_report.getTable();
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.INSERTCELL,
				new Integer(insertType), aimArea);
		if (table.checkEvent(event)) {//检查动态区域,组合单元
			table.fireEvent(event);//删除组合单元
			CellsModel cellsModel = getCellsModel();
			SelectModel selectModel = cellsModel.getSelectModel();
			CellPosition newAnchorPos;
			CellPosition startCellPos = aimArea.getStart();
			if (insertType == DeleteInsertDialog.CELL_MOVE_DOWN) {
				newAnchorPos = (CellPosition) startCellPos.getMoveArea(aimArea.getHeigth(), 0);
			} else if (insertType == DeleteInsertDialog.CELL_MOVE_RIGHT) {
				newAnchorPos = (CellPosition) startCellPos.getMoveArea(0, aimArea.getWidth());
			} else {
				throw new IllegalArgumentException();
			}
			AreaPosition toMoveArea = getToMoveArea(aimArea,
					insertType, cellsModel);
			cellsModel.moveCells(toMoveArea, newAnchorPos);
		}
	}
	
	protected CellsModel getCellsModel(){
		return m_report.getCellsModel();
	}
	
	protected AreaPosition[] getSelectArea(){
		return getCellsModel().getSelectModel().getSelectedAreas();	
	}
}

