/**
 * 
 */
package com.ufida.report.chart;

import java.awt.Component;

import org.jfree.chart.JFreeChart;

import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.chart.IChartModel;

/**
 * @author wangyga
 * @created at 2009-8-18,ÏÂÎç03:39:47
 *
 */
public abstract class AbstractChartExt extends AbsActionExt{

	private ChartPlugin m_plugin = null;
	
	public AbstractChartExt(ChartPlugin p){
		m_plugin = p;
	}
	
	abstract protected String getName();
	
	abstract protected String getImage();
	
	@Override
	public UfoCommand getCommand() {
		return null;
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(getName());
		uiDes.setImageFile(getImage());
		uiDes.setPaths(new String[] { MultiLang.getString("format") });
		uiDes.setGroup("chart");
		ActionUIDes uiDes2 = (ActionUIDes) uiDes.clone();
		uiDes2.setPopup(true);
		uiDes2.setPaths(new String[] {});
		return new ActionUIDes[] { uiDes, uiDes2 };
	}

	@Override
	public boolean isEnabled(Component focusComp) {
		IChartModel chartModel = getSelectedChartModel();

		if (chartModel != null) {
			return true;
		}

		return false;
	}
	
	protected JFreeChart getSelectedChart(){
		IChartModel chartModel = getSelectedChartModel();
		if(chartModel == null){
			return null;
		}
		JFreeChart chart = chartModel.getChart();
		return chart;
	}
	
	protected IChartModel getSelectedChartModel(){
		CellPosition anchor = m_plugin.getAnchorCell();
		if (anchor == null) {
			return null;
		}

		Cell cell = m_plugin.getCellsModel().getCell(anchor.getRow(),
				anchor.getColumn());
		if (cell == null) {
			return null;
		}

		Object value = cell.getValue();
		if (value == null ||  !(value instanceof IChartModel)) {
			return null;
		}
		return (IChartModel)value;
	}
	
	protected IPlugIn getPlugin(){
		return m_plugin;
	}
	
}
