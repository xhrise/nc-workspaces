package com.ufida.report.chart;

import com.ufida.dataset.metadata.Field;
import com.ufsoft.iufo.fmtplugin.chart.GanttChartModel;
import com.ufsoft.table.chart.IChartModel;
import com.ufsoft.iufo.resource.StringResource;

public class GanttSettingPanel extends ChartSettingPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * @i18n ubiquery0138=任务名称
	 * @i18n miufo00406=任务分类
	 * @i18n miufo00407=任务起始时间
	 * @i18n miufo00408=任务结束时间
	 */
	public GanttSettingPanel(IChartModel chartModel) {
		super(chartModel);

		setLayout(null);
		GanttChartModel model = (GanttChartModel) chartModel;

		Field[] flds = getFieldsInfo();
		int step = 70;
		int index = 0;
		int width = 505;
		int height = 64;
		AxisPropPanel ap = new AxisPropPanel(StringResource.getStringResource("ubiquery0138"), model.getTaskNameAxis(),
				flds);
		ap.setBounds(0, index * step, width, height);
		this.add(ap);

		ap = new AxisPropPanel(StringResource.getStringResource("miufo00406"), model.getTaskSericesAxis(), flds);
		index++;
		ap.setBounds(0, index * step, width, height);
		this.add(ap);

		ap = new AxisPropPanel(StringResource.getStringResource("miufo00407"), model.getTaskStartDateAxis(), flds);
		index++;
		ap.setBounds(0, index * step, width, height);
		this.add(ap);

		ap = new AxisPropPanel(StringResource.getStringResource("miufo00408"), model.getTaskEndDateAxis(), flds);
		index++;
		ap.setBounds(0, index * step, width, height);
		this.add(ap);

	}

	/**
	 * @i18n miufo00409=任务名称、任务起始时间或结束时间不能为空。
	 */
	@Override
	void save() {

		GanttChartModel model = (GanttChartModel) getChartModel();

		if (model.getTaskNameAxis().getFieldId() == null
				|| model.getTaskStartDateAxis().getFieldId() == null
				|| model.getTaskEndDateAxis().getFieldId() == null) {
			throw new RuntimeException(StringResource.getStringResource("miufo00409"));
		}

		// if(model.getDataAxis().getFieldId() != null
		// &&
		// !DataTypeConstant.isNumberType(model.getDataAxis().getDataType())){
		// throw new RuntimeException("X数据轴关联字段的数据类型必须为数值类型。");
		// }
		//		
		// if(model.getDataAxis2().getFieldId() != null
		// &&
		// !DataTypeConstant.isNumberType(model.getDataAxis2().getDataType())){
		// throw new RuntimeException("Y数据轴关联字段的数据类型必须为数值类型。");
		// }

	}

}
 