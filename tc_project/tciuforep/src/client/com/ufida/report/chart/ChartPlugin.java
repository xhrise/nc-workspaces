package com.ufida.report.chart;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.ui.RefineryUtilities;

import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufsoft.iufo.fmtplugin.chart.CategoryChartModel;
import com.ufsoft.iufo.fmtplugin.chart.ChartModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.chart.IChartModel;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;


public class ChartPlugin extends AbstractPlugIn implements UserActionListner {

	public void startup() {		
		
	}

	/**
	 * 图表设置窗口。
	 */
	void showSettingDlg() {
		CellsModel cellsModel = getCellsModel();
		CellPosition anchor = getAnchorCell();
		if (anchor == null) {
			return;
		}

		Cell cell = cellsModel.getCellIfNullNew(anchor.getRow(), anchor
				.getColumn());
		ChartModel chartModel = null;
		Object value = cell.getValue();
		if (value != null && value instanceof ChartModel) {
			chartModel = (ChartModel) value;
		}

		boolean isNew = false;
		if (chartModel == null) {
			isNew = true;
			chartModel = new CategoryChartModel();

		} else {
			chartModel = (ChartModel) chartModel.clone();

		}

		ChartSettingDlg dlg = new ChartSettingDlg(chartModel, getReport());
		dlg.showModal();

		if (dlg.getResult() != ChartSettingDlg.ID_OK) {
			return;
		}

		chartModel = dlg.getChartModel();
		cell.setValue(chartModel);
		cellsModel.setCell(anchor.getRow(), anchor.getColumn(), cell);

		// 处理合并单元格
		AreaPosition selArea = cellsModel.getSelectModel().getSelectedArea();
		CombinedCell[] ccs = cellsModel.getCombinedAreaModel()
				.getContainCombinedCells(selArea);
		if (!selArea.isCell()
				&& (ccs == null || ccs.length < 1 || !ccs[0].getArea().equals(
						selArea))) {
			cellsModel.getCombinedAreaModel().removeCombinedCell(ccs);
			cellsModel.getCombinedAreaModel().addCombinedCell(
					new CombinedCell(selArea, null, chartModel));
		}

		// 数据源中添加选中数据集
		AnaReportPlugin arPlugin = (AnaReportPlugin) getReport()
				.getPluginManager().getPlugin(AnaReportPlugin.class.getName());
		arPlugin.getModel().getDataSource().addDataSourcePK(
				new String[] { chartModel.getDataSetDefPK() });
		arPlugin.refreshDataSource(false);

		//删除选中区域类包含的可扩展区
		ExAreaModel exAreaModel = ExAreaModel.getInstance(getCellsModel());
		ExAreaCell[] existCells = exAreaModel.getContainExCells(selArea);
		for(ExAreaCell exArea: existCells){
			if(!exArea.getArea().equals(selArea)){
				exAreaModel.removeExArea(exArea);
			}
		}
		
		// 新规则：图表必须放在可扩展区域中。
		ExAreaCell exCell = exAreaModel.getExArea(selArea);
		if (exCell == null) {
			exCell = exAreaModel.addExArea(selArea);			
		}
		exCell.setExAreaType(ExAreaCell.EX_TYPE_CHART);
		AreaDataModel dm = (AreaDataModel) exCell.getModel();
		if (dm == null) {
			dm = new AreaDataModel(exCell.getExAreaPK(), arPlugin.getModel());
			exCell.setModel(dm);
		}

		if (dm.getDSPK() == null || exAreaModel.isCombinedExArea(exCell)) {
			dm.setDSPK(chartModel.getDataSetDefPK());
		}
		
		refreshDataModel();
	}

	/**
	 * add by wangyga 需要刷新数据时，子类重写此方法
	 */
	protected void refreshDataModel(){		
	}
	
	protected CellPosition getAnchorCell(){
		return getCellsModel().getSelectModel().getAnchorCell();
	}
	
	/**
	 * @i18n miufo00314=图表无数据。
	 */
	void showZoomDlg() {
		CellPosition anchor = getAnchorCell();
		if (anchor == null) {
			return;
		}

		Cell cell = getCellsModel().getCell(anchor.getRow(),
				anchor.getColumn());
		if (cell == null) {
			return;
		}

		Object value = cell.getValue();
		if (value == null ||  !(value instanceof IChartModel)) {
			return;
		}
		JFreeChart chart = ((IChartModel) value).getChart();
		if(chart == null){
			UfoPublic.showErrorDialog(getReport(), StringResource.getStringResource("miufo00314"), "");
			return;
		}
		ChartZoomDialog dlg = new ChartZoomDialog(getReport(), chart);
		dlg.pack();
		RefineryUtilities.centerFrameOnScreen(dlg);
		dlg.setVisible(true);
 
	}

	@Override
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this) {
			protected IExtension[] createExtensions() {
				return new IExtension[] { new ChartSettingExt(
						(ChartPlugin) getPlugin()),new ChartPropertiesExt((ChartPlugin) getPlugin()), 
						new ChartZoomExt(
								(ChartPlugin) getPlugin())};
			}
		};
	}

	public void userActionPerformed(UserUIEvent e) {
		switch (e.getEventType()) {
		case UserUIEvent.PASTE:
			preccessPaste(e);

			break;

		}

	}

	private EditParameter getEditParameter(UserUIEvent ee) {

		EditParameter parameter = (EditParameter) ee.getNewValue();

		return parameter;

	}

	private void preccessPaste(UserUIEvent e) {
		EditParameter p = getEditParameter(e);
		AreaPosition[] newAreas = p.getPasteAreas();
		AreaPosition oldArea = p.getCopyArea();

		CellPosition[] poss = oldArea.split();
		Map<CellPosition, IChartModel> hash = new HashMap<CellPosition, IChartModel>();
		for (CellPosition pos : poss) {
			Cell cell = getCellsModel().getCell(pos);
			if (cell == null || !(cell.getValue() instanceof IChartModel)) {
				continue;
			}
			hash.put(pos, (IChartModel) cell.getValue());
		}

		if (hash.size() < 1) {
			return;
		}
		poss = hash.keySet().toArray(new CellPosition[0]);
		for (AreaPosition newArea : newAreas) {
			for (CellPosition pos : poss) {
				CellPosition newPos = (CellPosition) pos.getMoveArea(oldArea
						.getStart(), newArea.getStart());
				Cell newCell = getCellsModel().getCell(newPos);
				if (newCell == null
						|| !(newCell.getValue() instanceof IChartModel)) {
					continue;
				}
				IChartModel newCM = (IChartModel) hash.get(pos).clone();
				newCell.setValue(newCM);
			}
		}

	}

	public String isSupport(int source, EventObject e)
			throws ForbidedOprException {
		return null;
	}

}
 