package com.ufida.report.spreedsheet.applet;

import com.ufida.report.spreedsheet.model.SpreadCellPropertyVO;
import com.ufida.report.spreedsheet.model.SpreadReportEnv;
import com.ufida.report.spreedsheet.model.SpreadSheetUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsAuthorization;

public class SpreadSheetReportAuth implements CellsAuthorization {
	private SpreadSheetPlugin m_plugin;

	private SpreadReportEnv m_reportEnv;

	/**
	 * @param report
	 */
	public SpreadSheetReportAuth(SpreadSheetPlugin plugin, SpreadReportEnv reportEnv) {
		super();
		m_plugin = plugin;
		m_reportEnv = reportEnv;
	}

	public boolean isReadable(int row, int col) {
		return true;
	}

	public boolean isWritable(int row, int col) {
		UfoReport report = m_plugin.getReport();

		Cell cell = report.getTable().getCells().getCell(row, col);
		SpreadCellPropertyVO cellVO = null;
		if (cell != null)
			cellVO = (SpreadCellPropertyVO) cell.getExtFmt(SpreadCellPropertyVO.KEY_CELL_SPREAD_PROP);
		if (cell == null || cellVO == null) {// 对于非指标单元
			if (report.getOperationState() == UfoReport.OPERATION_FORMAT)
				return true;
			else
				return false;
		} else {
			if (report.getOperationState() == UfoReport.OPERATION_FORMAT || !m_plugin.getModel().isEdite())
				return false;
			else {
				boolean canWrite = SpreadSheetUtil.getCellEditableStatus(cellVO, m_reportEnv);
				if (canWrite) {// 对于可写的空单元格,将其设置为数值类型，便于套用合适的编辑器
					Object cellValue = cell.getValue();
					if (cellValue == null) {
						cell.setValue(new Double(0));
					}
				}
				return canWrite;
			}
		}

	}

	public void authorize(int row, int col, int type) {
		// TODO Auto-generated method stub

	}

}
