package com.ufida.report.anareport.edit;

import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.exarea.ExAreaModel;

public abstract class AbsAnaReportExt extends AbsActionExt {
	protected AnaReportPlugin m_plugin = null;

	protected void setPlugIn(AnaReportPlugin plugin) {
		m_plugin = plugin;
	}
	protected CellPosition getFirstSelPos(){
		AnaReportModel model = m_plugin.getModel();
		CellPosition pos = model.getCellsModel().getSelectModel().getAnchorCell();
		CellPosition fmtPos = null;
		boolean isFormat = model.isFormatState();
		if (isFormat)
			fmtPos = pos;
		else {
			CellPosition[] dataStateCells = model.getFormatPoses(model.getDataModel(),
					new AreaPosition[] { AreaPosition.getInstance(pos, pos) });
			if (dataStateCells != null && dataStateCells.length > 0)
				fmtPos = dataStateCells[0];
		}
		return fmtPos;
	}
	protected AreaDataModel getSelAreaModel(boolean mustHasDS) {
		AnaReportModel model = m_plugin.getModel();
		CellPosition fmtPos = getFirstSelPos();
		
		// 当前选择点必须是一个数据区域
		ExAreaModel areaModel = ExAreaModel.getInstance(model.getFormatModel());
		if (areaModel.getExArea(fmtPos) == null)
			return null;

		String areaPk = areaModel.getExArea(fmtPos).getExAreaPK();
		AreaDataModel aModel = m_plugin.getModel().getAreaData(areaPk);
		if (mustHasDS && (aModel.getDSTool() == null))
			return null;

		return aModel;

	}
}
