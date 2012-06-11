/**
 * 
 */
package com.ufida.report.anareport.applet;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

import com.ufida.report.anareport.model.AnaCellPagination;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;

/**
 * @author ll
 * 
 */
public class AnaCellPaginationExt extends AbsActionExt {

	public static final String RESID_SETFIELD = "miufo00178";
	public static final String RESID_PAGINATION = "miufo1001541";// 强制分页
	public static final String RESID_GROUP_PAGINATION = "uiiufo0001";//按分组分页
	public static final String RESID_CANCEL_PAGINATION = "miufo1001540";// 取消分页
	private AnaReportPlugin m_plugin = null;

	public AnaCellPaginationExt(AnaReportPlugin plugin) {
		this.m_plugin = plugin;
	}

	private class AnaCellPaginationCmd extends UfoCommand {
		public void execute(Object[] params) {
			if (params == null)
				return;

			UfoReport report = (UfoReport) params[0];
			CellPosition[] fmtcells = m_plugin.getModel().getFormatPoses(report.getCellsModel(),
					report.getCellsModel().getSelectModel().getSelectedAreas());
			changePagination(m_plugin.getModel().getFormatModel(), params, fmtcells);
			if (!m_plugin.getModel().isFormatState())
				m_plugin.refreshDataModel(true);
		}
	}

	@Override
	public UfoCommand getCommand() {
		return new AnaCellPaginationCmd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
	 */
	@Override
	public Object[] getParams(UfoReport container) {
		CellsModel cells = m_plugin.getModel().getFormatModel();
		AreaPosition[] areaPos = m_plugin.getFormatSelected();
		if (areaPos == null || areaPos.length <= 0)
			return null;
		return new Object[] { container };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	/**
	 * @i18n miufo1000877=格式
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setToolBar(false);
		uiDes.setGroup(StringResource.getStringResource(RESID_SETFIELD));
		uiDes.setPaths(new String[] {StringResource.getStringResource("miufo1000877")});
		uiDes.setName(StringResource.getStringResource(RESID_PAGINATION));
		return new ActionUIDes[] { uiDes };
	}

	private void changePagination(CellsModel fmtModel, Object[] param, CellPosition[] formatPoses) {
		if (formatPoses == null || formatPoses.length == 0)
			return;
		ArrayList<CellPosition> al_flds = new ArrayList<CellPosition>();// 发生了字段变化的单元位置
		for (CellPosition pos : formatPoses) {

			Cell cell = fmtModel.getCell(pos);
			if (cell == null)
				cell = fmtModel.getCellIfNullNew(pos.getRow(), pos.getColumn());

			AnaRepField field = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
			if (field == null) {
				return;
			}
			Object obj = field.getDimInfo().getCellPagination();
			if (obj == null)
				 field.getDimInfo().setCellPagination(AnaCellPagination.getInstance());
			else
				 field.getDimInfo().setCellPagination(null);
		}
	}
}
 