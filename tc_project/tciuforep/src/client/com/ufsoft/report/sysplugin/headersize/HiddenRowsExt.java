package com.ufsoft.report.sysplugin.headersize;

import java.util.HashSet;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.AreaCommonOpr;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.TableStyle;
import com.ufsoft.table.header.HeaderModel;
/**
 * 隐藏行的功能点
 * @author guogang
 *
 */
public class HiddenRowsExt extends AbsActionExt {
	private UfoReport m_report;//报表工具

	/**
	 * 构造函数
	 * @param rep - 报表
	 */
	public HiddenRowsExt(UfoReport rep) {
		m_report = rep;
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

	public Object[] getParams(UfoReport container) {
		Object[] result = new Object[3];
		result[0] = new Character('p');
		CellsModel cellModel = this.getReport().getCellsModel();
		int[] selectedRow = cellModel.getSelectModel().getSelectedRow();
		if (selectedRow != null && selectedRow.length > 0) {
			result[1] = selectedRow;
		}
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
			result[1] = selectedRow;
		}

		result[2] = TableStyle.MINHEADER;
		return result;
	}

	private UfoReport getReport() {
		return this.m_report;
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setName(MultiLang.getString("Hidden"));
		uiDes1.setPaths(new String[] { MultiLang.getString("format"),
				MultiLang.getString("Row") });
		uiDes1.setGroup("headerSizeExt");
		return new ActionUIDes[] { uiDes1 };
	}

}
