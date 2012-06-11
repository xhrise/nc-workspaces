package com.ufida.report.anareport.applet;

import java.awt.Component;

import com.ufida.report.anareport.model.AnaCellPagination;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

/**
 * 按分组分页扩展
 * 
 * @author ll
 */
public class AnaInsertCellPaginationExt extends AbsActionExt {
	private AnaReportPlugin m_plugin = null;

	public AnaInsertCellPaginationExt(AnaReportPlugin plugin) {
		m_plugin = plugin;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	private class AnaCellPaginationCmd extends UfoCommand {
		public void execute(Object[] params) {
			if (params == null)
				return;
			UfoReport report = (UfoReport) params[0];
			CellPosition fmtPos = m_plugin.getModel().getFormatCellPos(report.getCellsModel(),
					report.getCellsModel().getSelectModel().getAnchorCell());
			CellsModel fmtModel = m_plugin.getModel().getFormatModel();
			Cell cell = fmtModel.getCell(fmtPos);
			if (cell == null)
				return;

			AnaRepField field = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
			if (field != null) {
				field.getDimInfo().setCellPagination(AnaCellPagination.getInstance());
				if (!m_plugin.getModel().isFormatState())
					m_plugin.refreshDataModel(true);
			}
		}
	}

	@Override
	public UfoCommand getCommand() {
		return new AnaCellPaginationCmd();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		return new Object[] { container };
	}

	/*
	 * @see com.ufsoft.report.plugin.IMainMenuExt#getPath()
	 */
	public String[] getPath() {
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
	 */
	public boolean isEnabled(Component focusComp) {
		CellsModel cm = m_plugin.getModel().getCellsModel();
		CellPosition fmtPos = m_plugin.getModel().getFormatCellPos(cm, cm.getSelectModel().getAnchorCell());
		CellsModel fmtModel = m_plugin.getModel().getFormatModel();
		Cell cell = fmtModel.getCell(fmtPos);
		if (cell == null)
			return false;

		AnaRepField field = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
		if (field == null || field.getFieldType() != AnaRepField.TYPE_GROUP_FIELD)
			return false;

		return true;
	}

	/*
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(StringResource.getStringResource("uiiufo0001"));
		uiDes.setPaths(new String[] { MultiLang.getString("format") });
		uiDes.setGroup("paginationExt");
		return new ActionUIDes[] { uiDes };
	}
}
