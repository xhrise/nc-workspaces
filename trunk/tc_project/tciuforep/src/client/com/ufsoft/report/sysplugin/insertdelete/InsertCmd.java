package com.ufsoft.report.sysplugin.insertdelete;

import java.util.Arrays;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.exception.MessageException;
import com.ufsoft.report.util.IUFOLogger;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;

public class InsertCmd extends UfoCommand{

	private UfoReport m_Report;
	/**
	 * @param rep
	 *            UfoReport - 报表
	 */
	public InsertCmd(UfoReport rep) {
		super();
		this.m_Report = rep;
	}
	
	@Override
	public void execute(Object[] params) {
		if ((params == null) || (params.length == 0))
			return;

		try {
			int insertMethod = ((Integer) params[0]).intValue();
			CellsModel cellsModel = getCellsModel();
			if (cellsModel == null)
				return;
			UFOTable table = getReport().getTable();
			CellPosition newAnchorPos;
			CellPosition startCellPos;
			AreaPosition aimArea;
			UserUIEvent event;
			switch (insertMethod) {
			case DeleteInsertDialog.CELL_MOVE_RIGHT://考虑动态区域,组合单元.
				aimArea= (AreaPosition) params[1];
				event = new UserUIEvent(this, UserUIEvent.INSERTCELL,
						new Integer(insertMethod), aimArea);
				if (table.checkEvent(event)) {//检查动态区域,组合单元
					table.fireEvent(event);//删除组合单元
					startCellPos = aimArea.getStart();
					newAnchorPos = (CellPosition) startCellPos.getMoveArea(0, aimArea.getWidth());
					AreaPosition toMoveArea = InsertCellCmd.getToMoveArea(aimArea,
							insertMethod, cellsModel);
					cellsModel.moveCells(toMoveArea, newAnchorPos);
				}
				break;
			case DeleteInsertDialog.CELL_MOVE_DOWN:
				aimArea = (AreaPosition) params[1];
				event= new UserUIEvent(this, UserUIEvent.INSERTCELL,
						new Integer(insertMethod), aimArea);
				if (table.checkEvent(event)) {//检查动态区域,组合单元
					table.fireEvent(event);//删除组合单元
					startCellPos = aimArea.getStart();
					newAnchorPos = (CellPosition) startCellPos.getMoveArea(aimArea.getHeigth(), 0);
					AreaPosition toMoveArea = InsertCellCmd.getToMoveArea(aimArea,
							insertMethod, cellsModel);
					cellsModel.moveCells(toMoveArea, newAnchorPos);
				}
				break;
			case DeleteInsertDialog.INSERT_ROW:
				int[] selectRow = (int[]) params[1];
				if ((selectRow != null) && (selectRow.length > 0)) {
					
					Arrays.sort(selectRow);
					cellsModel.getRowHeaderModel().addHeader(selectRow[0],selectRow.length);
				}

				break;
			case DeleteInsertDialog.INSERT_COLUMN:
				int[] selectCol = (int[]) params[1];
				if ((selectCol != null) && (selectCol.length > 0)) {
					Arrays.sort(selectCol);
					cellsModel.getColumnHeaderModel().addHeader(selectCol[0],selectCol.length);
				}
				break;
			}
		} catch (Exception e) {
			AppDebug.debug(e);
			IUFOLogger.getLogger(this).fatal(
					MultiLang.getString("uiuforep0000858"));//插入失败
			throw new MessageException(MessageException.TYPE_ERROR,e.getMessage());
		}
	}
	
	protected UfoReport getReport(){
		return m_Report;
	}
	
	protected CellsModel getCellsModel(){
		return getReport().getTable().getCells().getDataModel();
	}
}
