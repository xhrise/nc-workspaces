package com.ufsoft.report.sysplugin.headersize;

import java.util.HashSet;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.TableStyle;
import com.ufsoft.table.header.HeaderModel;
import com.ufsoft.report.util.AreaCommonOpr;
/**
 * 取消隐藏行的功能点
 * @author guogang
 *
 */
public class CancelHiddenRowExt extends AbsActionExt {
	private UfoReport m_report;//报表工具

	public CancelHiddenRowExt(UfoReport report) {
		this.m_report = report;
	}

	@Override
	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				if (params[1] != null) {
					int[] rows = (int[]) params[1];
					int height = ((Integer) params[2]).intValue();
					HeaderModel headerModel = m_report.getCellsModel()
							.getRowHeaderModel();
					for (int i = 0; i < rows.length; i++) {
						headerModel.setSize(rows[i], height);
					}
				}
			}
		};
	}

	@Override
	public Object[] getParams(UfoReport container) {
		Object[] result = new Object[3];
		int[] hiddenedRow = null;
		CellsModel cellModel = this.getReport().getCellsModel();
		CellsPane pane = this.getReport().getTable().getCells();

		int[] selectedRow = cellModel.getSelectModel().getSelectedRow();
		HashSet set = new HashSet();

		AreaPosition[] selectedArea = cellModel.getSelectModel().getSelectedAreas();
		if ((selectedArea != null) && selectedArea.length > 0) {
			HashSet areaSet = AreaCommonOpr.intArrayToHashSet(AreaCommonOpr
					.getRows(selectedArea));
			HashSet selectedRowSet = AreaCommonOpr
					.intArrayToHashSet(selectedRow);
			if (selectedRowSet == null)
				selectedRowSet = new HashSet();
			selectedRowSet.addAll(areaSet);
			selectedRow = AreaCommonOpr.hashSetToIntArray(selectedRowSet);

		}
		if ((selectedRow != null) && selectedRow.length > 0) {
			for (int i = 0; i < selectedRow.length; i++) {
				if (pane.getRowHeight(selectedRow[i]) == TableStyle.MINHEADER) {
					set.add(new Integer(selectedRow[i]));
				}
			}
			hiddenedRow = AreaCommonOpr.hashSetToIntArray(set);
		}
		result[0] = new Character('p');
		result[1] = selectedRow;
		result[2] = TableStyle.ROW_HEIGHT;
		return result;
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setName(MultiLang.getString("CancelHidden"));
		uiDes1.setPaths(new String[] { MultiLang.getString("format"),
				MultiLang.getString("Row") });
		uiDes1.setGroup("headerSizeExt");
		return new ActionUIDes[] { uiDes1 };
	}

	private UfoReport getReport() {
		return this.m_report;
	}
}
