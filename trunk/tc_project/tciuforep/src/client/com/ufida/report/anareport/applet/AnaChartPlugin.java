package com.ufida.report.anareport.applet;

import com.ufida.report.anareport.model.AnaReportModel;
import com.ufida.report.chart.ChartPlugin;
import com.ufsoft.iufo.fmtplugin.chart.ChartModel;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;

/**
 * 分析报表图表插件
 * 
 * @author wangyga
 * 
 */
public class AnaChartPlugin extends ChartPlugin {
	
	@Override
	public void userActionPerformed(UserUIEvent e) {
		super.userActionPerformed(e);
		
		switch (e.getEventType()) {
		case UserUIEvent.CLEAR:
			processClearEvent(e);
			break;

		}
	}
	
	@Override
	protected void refreshDataModel() {
		if(!getAanRepModel().isFormatState()){
			getAanPlugin().refreshDataModel(true);
		}
	}

	@Override
	protected CellPosition getAnchorCell() {
		AnaReportModel model = getAanRepModel();
		return model.getFormatCellPos(model.getCellsModel(), model
				.getCellsModel().getSelectModel().getAnchorCell());
	}

	@Override
	public CellsModel getCellsModel() {
		return getAanRepModel().getFormatModel();
	}

	private AnaReportModel getAanRepModel() {
		return getAanPlugin().getModel();
	}

	private AnaReportPlugin getAanPlugin() {
		return (AnaReportPlugin) getReport().getPluginManager().getPlugin(
				AnaReportPlugin.class.getName());
	}
	
	private void processClearEvent(UserUIEvent e){
		if(e == null)
			return;
		Object value = e.getNewValue();
		if(value == null || !(value instanceof AreaPosition[]))
			return;
		AreaPosition[] clearAreas = (AreaPosition[])value;
		CellsModel formatModel = getCellsModel();
		ExAreaModel areaModel = ExAreaModel.getInstance(formatModel);
		ExAreaCell exAreaCell = areaModel.getExArea(clearAreas[0]);
		if(exAreaCell == null || exAreaCell.getExAreaType() != ExAreaCell.EX_TYPE_CHART)
			return;
		for(CellPosition pos : exAreaCell.getArea().split()){
			Cell cell = formatModel.getCell(pos);
			if(cell != null && cell.getValue() instanceof ChartModel){
				return;
			}
		}
		exAreaCell.setExAreaType(ExAreaCell.EX_TYPE_NONE);
	}
}
