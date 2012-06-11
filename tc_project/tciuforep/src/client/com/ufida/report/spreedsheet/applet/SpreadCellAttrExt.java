package com.ufida.report.spreedsheet.applet;

import java.awt.Component;

import javax.swing.JLabel;

import com.ufida.report.spreedsheet.model.SpreadCellPropertyVO;
import com.ufida.report.spreedsheet.model.SpreadSheetModel;
import com.ufida.report.spreedsheet.model.SpreadSheetUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.plugin.IStatusBarExt;
import com.ufsoft.report.sysplugin.cellattr.CellPropertyDialog;
import com.ufsoft.report.sysplugin.cellattr.SetCellAttrExt;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.SelectModel;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.format.Format;

public class SpreadCellAttrExt extends SetCellAttrExt implements IStatusBarExt {

	private UfoReport m_Report;

	private SpreadSheetModel m_model;

	public SpreadCellAttrExt(UfoReport rep) {
		super(rep);
		m_Report = rep;
	}

	public UfoCommand getCommand() {

		return new SpreadCellAttrCmd(this.getReport());
	}

	protected CellPropertyDialog getCellPropertyDialog(UfoReport report, Format format, boolean isTypeLocked, boolean bInnerBorder) {
		SpreadCellPropertyVO cellVO = SpreadSheetUtil.getCellVO(report.getCellsModel());
		// 初始化模型
		if (m_model == null) {
			IPlugIn[] plugins = report.getPluginManager().getAllPlugin();
			for (int i = 0; i < plugins.length; i++) {
				if (plugins[i] instanceof SpreadSheetPlugin) {
					m_model = ((SpreadSheetPlugin) plugins[i]).getModel();
					break;
				}
			}
		}

		SpreadCellPropertyDlg dlg = new SpreadCellPropertyDlg(report, format, isTypeLocked, cellVO, m_model
				.getQueryCache(), m_model);
		return dlg;
	}

	public void initListenerByComp(final Component stateChangeComp) {
		SelectModel selectModel = m_Report.getCellsModel().getSelectModel();
		if (stateChangeComp instanceof JLabel) {
			selectModel.addSelectModelListener(new SelectListener() {
				public void selectedChanged(SelectEvent e) {
					if (e.getProperty() == SelectEvent.ANCHOR_CHANGED) {
						AreaPosition cs = ((SelectModel) e.getSource()).getSelectedArea();
						SpreadCellPropertyVO cellVO = null;
						if (cs != null) {
							Cell cell = getReport().getCellsModel().getCell(cs.getStart());
							if (cell != null) {
								cellVO = (SpreadCellPropertyVO) cell
										.getExtFmt(SpreadCellPropertyVO.KEY_CELL_SPREAD_PROP);
							}
						}
						String strStatus = cellVO == null ? " " : cellVO.toString();
						((JLabel) stateChangeComp).setText(strStatus);
					}
				}
			});
		}
	}

}
