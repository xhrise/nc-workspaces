package com.ufida.report.chart;

import java.awt.Component;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;

public class ChartSettingExt  extends AbstractChartExt {

	ChartSettingExt(ChartPlugin p){
		super(p);
	}
	
	public boolean isEnabled(Component focusComp) {
		
		if(getPlugin().getReport().getOperationState() != UfoReport.OPERATION_FORMAT){			
			if (getSelectedChartModel() != null) {
				return true;
			}			
			return false;
		}
		//modify by wangyga 2008-10-28
		ChartPlugin chartPlugin = (ChartPlugin)getPlugin();
		ExAreaModel exAreaModel = ExAreaModel.getInstance(chartPlugin.getCellsModel());
		CellPosition anchor = chartPlugin.getAnchorCell();
		if (anchor == null) {
			return false;
		}
		ExAreaCell exAreaCell = exAreaModel.getExArea(anchor);
		if(exAreaCell == null)
			return true;
		if(exAreaCell.getExAreaType() == ExAreaCell.EX_TYPE_SAMPLE)//扩展区域是表样时不能定义图表
			return false;
		return true;
	}
	
	@Override
	public Object[] getParams(UfoReport container) {
		((ChartPlugin)getPlugin()).showSettingDlg();
		return null;
	}

	@Override
	protected String getImage() {
		return "reportcore/chart.gif";
	}

	@Override
	protected String getName() {
		return StringResource.getStringResource("ubichart00014");
	}

}
 