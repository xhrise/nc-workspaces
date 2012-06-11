package com.ufsoft.report.sysplugin.headersize;

import java.awt.Component;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.TableStyle;
import com.ufsoft.table.header.HeaderModel;

/**
 * 设置行头最适合行高
 * @author chxw
 */
public class SetRowPreferredSizeExt extends AbsActionExt {
	private UfoReport m_report;//报表工具

	public SetRowPreferredSizeExt(UfoReport report) {
		this.m_report = report;
	}

	@Override
	public UfoCommand getCommand() {
		return null;
	}

	@Override
	public Object[] getParams(UfoReport container) {
		CellsModel cellModel = this.getReport().getCellsModel();
		int[] selectedRow = cellModel.getSelectModel().getSelectedRow();
		HeaderModel headerModel = m_report.getCellsModel().getRowHeaderModel();
		for (int i = 0; i < selectedRow.length; i++) {
			headerModel.setFitSize(selectedRow[i]);
		}
		return null;

	}

	/**
	 * @i18n miufo00117=最适合的行高
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setName(MultiLang.getString("miufo00117"));
		uiDes1.setPaths(new String[] { MultiLang.getString("format"),
				MultiLang.getString("Row") });
		uiDes1.setGroup("headerSizeExt");
		return new ActionUIDes[] { uiDes1 };
	}

	
	@Override
	public boolean isEnabled(Component focusComp) {
		CellsModel cellModel = this.getReport().getCellsModel();
		if(cellModel == null){
			return false;
		}
		return cellModel.getSelectModel().getSelectedRow() != null;
	}

	private UfoReport getReport() {
		return this.m_report;
	}

}
 