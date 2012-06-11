/**
 * 
 */
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
 * 隐藏列的功能点
 * @author guogang
 *
 */
public class HiddenColsExt extends AbsActionExt {
	private UfoReport m_report;//报表工具

	/**
	 * 构造函数
	 * @param rep - 报表
	 */
	public HiddenColsExt(UfoReport rep) {
		m_report = rep;
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
	 */
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

	/* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
	 */

	public Object[] getParams(UfoReport container) {
		Object[] result = new Object[3];
		result[0] = new Character('p');
		CellsModel cellModel = this.getReport().getCellsModel();
		int[] selectedCol = cellModel.getSelectModel().getSelectedCol();
		if (selectedCol != null && selectedCol.length > 0) {
			result[1] = selectedCol;
		}
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
			result[1] = selectedCol;
		}
		result[2] = TableStyle.MINHEADER;
		return result;
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setName(MultiLang.getString("Hidden"));
		uiDes1.setPaths(new String[] { MultiLang.getString("format"),
				MultiLang.getString("Column") });
		uiDes1.setGroup("headerSizeExt");
		return new ActionUIDes[] { uiDes1 };
	}

	private UfoReport getReport() {
		return this.m_report;
	}
}
