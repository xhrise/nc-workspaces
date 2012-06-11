package com.ufida.report.anareport.applet;

import java.awt.Component;

import javax.swing.KeyStroke;

import com.ufida.report.anareport.model.AnaRepField;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.header.Header;

/**
 * 取消强制分页扩展
 * 
 * @author zzl
 */
public class AnaDeletePaginationExt extends AbsActionExt {// implements
	// IMainMenuExt {
	private AnaReportPlugin m_plugin = null;

	public AnaDeletePaginationExt(AnaReportPlugin plugin) {
		m_plugin = plugin;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.ufsoft.report.plugin.ICommandExt#getName()
	 */
	public String getName() {
		return MultiLang.getString("miufo1001540");
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.ufsoft.report.plugin.ICommandExt#getHint()
	 */
	public String getHint() {
		return null;
	}

	// /* （非 Javadoc）
	// * @see com.ufsoft.report.plugin.ICommandExt#getMenuSlot()
	// */
	// public int getMenuSlot() {
	// return ReportMenuBar.EDIT_END;
	// }

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.ufsoft.report.plugin.ICommandExt#getImageFile()
	 */
	public String getImageFile() {
		return null;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.ufsoft.report.plugin.ICommandExt#getAccelerator()
	 */
	public KeyStroke getAccelerator() {
		return null;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				UfoReport report = (UfoReport) params[0];

				// 1.取消界面上的强制分页
				CellsModel cm = report.getCellsModel();
				CellPosition anchorPos = cm.getSelectModel().getAnchorCell();
				if (anchorPos.getRow() != 0) {
					cm.delPage(Header.ROW, anchorPos.getRow());
				}
				if (anchorPos.getColumn() != 0) {
					cm.delPage(Header.COLUMN, anchorPos.getColumn());
				}
				// 2。取消格式设计中的按分组分页
				CellPosition fmtPos = m_plugin.getModel().getFormatCellPos(report.getCellsModel(),
						report.getCellsModel().getSelectModel().getAnchorCell());
				CellsModel fmtModel = m_plugin.getModel().getFormatModel();
				Cell cell = fmtModel.getCell(fmtPos);
				if (cell != null) {
					AnaRepField field = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
					if (field != null) {
						field.getDimInfo().setCellPagination(null);
						if (!m_plugin.getModel().isFormatState())
							m_plugin.refreshDataModel(true);
					}
				}

			}
		};
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
		return true;
	}

	/*
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("miufo1001540"));
		uiDes.setPaths(new String[] { MultiLang.getString("format") });
		uiDes.setGroup("paginationExt");
		return new ActionUIDes[] { uiDes };
	}
}
