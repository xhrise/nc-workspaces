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
 * ��������ɾ������,����ɾ���к�ɾ����
 * @author CaiJie
 * @since 3.1
 */
public class DeleteCmd extends UfoCommand {

	private UfoReport m_Report;

	/**
	 * @param rep
	 *            UfoReport - ����
	 */
	public DeleteCmd(UfoReport rep) {
		super();
		this.m_Report = rep;
	}

	/**
	 * ɾ������ѡ�е��С��л�Ԫ	 
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
			case DeleteInsertDialog.CELL_MOVE_LEFT://���Ƕ�̬����,��ϵ�Ԫ.
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
					
					//Ŀǰ��֧�ֶ�ѡ����
					cModel.getRowHeaderModel().removeHeader(selectRow[0],
							selectRow.length);
					
					//	���ѡ������
					cModel.getSelectModel().clear();
					
				}

				break;
			case DeleteInsertDialog.DELTE_COLUMN:
				int[] selectCol = (int[]) params[1];
				if ((selectCol != null) && (selectCol.length > 0)) {
					Arrays.sort(selectCol);
					cModel.getColumnHeaderModel().removeHeader(selectCol[0],
							selectCol.length);
					//���ѡ������
					cModel.getSelectModel().clear();
				}
				break;
			}
		} catch (Exception e) {
			AppDebug.debug(e);
			IUFOLogger.getLogger(this).fatal(
					MultiLang.getString("uiuforep0000861"));//ɾ��ʧ��
//			UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000861"),
//					m_Report, e);//ɾ��ʧ��
			throw new MessageException(MessageException.TYPE_ERROR,e.getMessage());
		}
	}

	/**��ȡ��Ҫ�ƶ�������(add by guogang 2007-8-20)
	 * @param aimArea Ҫɾ��������
	 * @param deleteType �ƶ�����(DeleteDialog.CELL_MOVE_LEFT,CELL_MOVE_UP)
	 * @param cellsModel ��Ԫ��ģ��
	 * @return AreaPosition ����ɾ����Ԫ�������ƶ�������
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