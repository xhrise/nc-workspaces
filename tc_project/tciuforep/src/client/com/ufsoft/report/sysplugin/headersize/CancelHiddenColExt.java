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
 * 取消隐藏列的功能点
 * @author guogang
 *
 */
public class CancelHiddenColExt extends AbsActionExt {
	private UfoReport m_report;//报表工具

	public CancelHiddenColExt(UfoReport report) {
		this.m_report = report;
	}

	@Override
	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				if (params[1] != null) {
					int[] cols = (int[]) params[1];
					int height = ((Integer) params[2]).intValue();
					HeaderModel headerModel = m_report.getCellsModel()
							.getColumnHeaderModel();
					for (int i = 0; i < cols.length; i++) {
						headerModel.setSize(cols[i], height);
					}
				}
			}
		};
	}

	@Override
	public Object[] getParams(UfoReport container) {

		Object[] result = new Object[3];
		int[] hiddenedCol = null;
		CellsModel cellModel = this.getReport().getCellsModel();
		CellsPane pane = this.getReport().getTable().getCells();

		int[] selectedCol = cellModel.getSelectModel().getSelectedCol();
		HashSet set = new HashSet();

		AreaPosition[] selectedArea = cellModel.getSelectModel().getSelectedAreas();
		if ((selectedArea != null) && selectedArea.length > 0) {
			HashSet areaSet = AreaCommonOpr.intArrayToHashSet(AreaCommonOpr
					.getColumns(selectedArea));
			HashSet selectedColSet = AreaCommonOpr
					.intArrayToHashSet(selectedCol);
			if (selectedColSet == null)
				selectedColSet = new HashSet();
			selectedColSet.addAll(areaSet);
			selectedCol = AreaCommonOpr.hashSetToIntArray(selectedColSet);

		}
		if ((selectedCol != null) && selectedCol.length > 0) {
			for (int i = 0; i < selectedCol.length; i++) {
				if (pane.getColumnWidth(selectedCol[i]) == TableStyle.MINHEADER) {
					set.add(new Integer(selectedCol[i]));
				}
			}
			hiddenedCol = AreaCommonOpr.hashSetToIntArray(set);
		}
		result[0] = new Character('p');
		result[1] = hiddenedCol;
		result[2] = TableStyle.COLUMN_WIDTH;
		return result;
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setName(MultiLang.getString("CancelHidden"));
		uiDes1.setPaths(new String[] { MultiLang.getString("format"),
				MultiLang.getString("Column") });
		uiDes1.setGroup("headerSizeExt");
		return new ActionUIDes[] { uiDes1 };
	}

	private UfoReport getReport() {
		return this.m_report;
	}
}
