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

/**
 * 报表工具中删除命令,包括删除行和删除列
 * @author CaiJie
 * @since 3.1
 */
public class DeleteCmd extends UfoCommand {

	private UfoReport m_Report;

	/**
	 * @param rep
	 *            UfoReport - 报表
	 */
	public DeleteCmd(UfoReport rep) {
		super();
		this.m_Report = rep;
	}

	/**
	 * 删除所有选中的行、列或单元	 
	 */
	public void execute(Object[] params) {
		if ((params == null) || (params.length == 0))
			return;

		try {
			int deleteMethod = ((Integer) params[0]).intValue();
			CellsModel cModel = m_Report.getTable().getCells().getDataModel();
			if (cModel == null)
				return;

			switch (deleteMethod) {
			case DeleteInsertDialog.CELL_MOVE_LEFT://考虑动态区域,组合单元.
				AreaPosition aimArea1 = (AreaPosition) params[1];
				m_Report.getTable().deleteCells(aimArea1,
						DeleteInsertDialog.CELL_MOVE_LEFT);
				break;
			case DeleteInsertDialog.CELL_MOVE_UP:
				AreaPosition aimArea2 = (AreaPosition) params[1];
				m_Report.getTable().deleteCells(aimArea2,
						DeleteInsertDialog.CELL_MOVE_UP);
				break;
			case DeleteInsertDialog.DELETE_ROW:
				int[] selectRow = (int[]) params[1];
				if ((selectRow != null) && (selectRow.length > 0)) {
					
					Arrays.sort(selectRow);
					
					//目前不支持多选区域。
					cModel.getRowHeaderModel().removeHeader(selectRow[0],
							selectRow.length);
					
					//	清除选中区域
					cModel.getSelectModel().clear();
					
				}

				break;
			case DeleteInsertDialog.DELTE_COLUMN:
				int[] selectCol = (int[]) params[1];
				if ((selectCol != null) && (selectCol.length > 0)) {
					Arrays.sort(selectCol);
					cModel.getColumnHeaderModel().removeHeader(selectCol[0],
							selectCol.length);
					//清除选中区域
					cModel.getSelectModel().clear();
				}
				break;
			}
		} catch (Exception e) {
			AppDebug.debug(e);
			IUFOLogger.getLogger(this).fatal(
					MultiLang.getString("uiuforep0000861"));//删除失败
//			UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000861"),
//					m_Report, e);//删除失败
			throw new MessageException(MessageException.TYPE_ERROR,e.getMessage());
		}
	}

	/**获取将要移动的区域(add by guogang 2007-8-20)
	 * @param aimArea 要删除的区域
	 * @param deleteType 移动类型(DeleteDialog.CELL_MOVE_LEFT,CELL_MOVE_UP)
	 * @param cellsModel 单元格模型
	 * @return AreaPosition 由于删除单元格引起移动的区域
	 */
	public static AreaPosition getToMoveArea(AreaPosition aimArea,
			int deleteType, CellsModel cellsModel) {
		CellPosition startCellPos;
		CellPosition endCellPos;
		if (deleteType == DeleteInsertDialog.CELL_MOVE_LEFT) {
			if (aimArea.getStart().getMoveArea(0, aimArea.getWidth()) == null) {
				startCellPos = CellPosition.getInstance(aimArea.getStart()
						.getRow(), 0);
			} else {
				startCellPos = (CellPosition) aimArea.getStart().getMoveArea(0,
						aimArea.getWidth());
			}
			endCellPos = CellPosition.getInstance(aimArea.getEnd().getRow(),
					cellsModel.getColNum() - 1);
		} else if (deleteType == DeleteInsertDialog.CELL_MOVE_UP) {
			if (aimArea.getStart().getMoveArea(aimArea.getHeigth(), 0) == null) {
				startCellPos = CellPosition.getInstance(0, aimArea.getStart()
						.getColumn());
			} else {
				startCellPos = (CellPosition) aimArea.getStart().getMoveArea(
						aimArea.getHeigth(), 0);
			}
			endCellPos = CellPosition.getInstance(cellsModel.getRowNum() - 1,
					aimArea.getEnd().getColumn());
		} else {
			throw new IllegalArgumentException();
		}
		return AreaPosition.getInstance(startCellPos, endCellPos);
	}
}