package com.ufida.report.chart;

import com.ufida.dataset.metadata.Field;
import com.ufsoft.iufo.fmtplugin.chart.BubbleChartModel;
import com.ufsoft.table.chart.IChartModel;

import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufsoft.iufo.resource.StringResource;

public class BubbleSettingPanel extends ChartSettingPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * @i18n miufo00202=X数据轴
	 * @i18n miufo00203=Y数据轴
	 * @i18n miufo00204=气泡半径
	 */
	public BubbleSettingPanel(IChartModel chartModel) {
		super(chartModel);

		setLayout(null);
		BubbleChartModel model = (BubbleChartModel) chartModel;

		Field[] flds = getFieldsInfo();
		int step = 70;
		int index = 0;
		int width = 505;
		int height = 64;
		AxisPropPanel ap = new AxisPropPanel(StringResource.getStringResource("miufo00202"), model.getDataAxis(), flds);
		ap.setBounds(0, index * step, width, height);
		this.add(ap);

		ap = new AxisPropPanel(StringResource.getStringResource("miufo00203"), model.getDataAxis2(), flds);
		index++;
		ap.setBounds(0, index * step, width, height);
		this.add(ap);

		ap = new AxisPropPanel(StringResource.getStringResource("miufo00204"), model.getDataAxis3(), flds);
		index++;
		ap.setBounds(0, index * step, width, height);
		this.add(ap);

	}

	/**
	 * @i18n miufo00205=数据轴不能为空。
	 * @i18n miufo00206=X数据轴关联字段的数据类型必须为数值类型。
	 * @i18n miufo00207=Y数据轴关联字段的数据类型必须为数值类型。
	 * @i18n miufo00208=气泡半径关联字段的数据类型必须为数值类型。
	 */
	@Override
	void save() {

		BubbleChartModel model = (BubbleChartModel) getChartModel();

		if (model.getDataAxis().getFieldId() == null
				|| model.getDataAxis2().getFieldId() == null
				|| model.getDataAxis3().getFieldId() == null) {
			throw new RuntimeException(StringResource.getStringResource("miufo00205"));
		}

		if (model.getDataAxis().getFieldId() != null
				&& !DataTypeConstant.isNumberType(model.getDataAxis()
						.getDataType())) {
			throw new RuntimeException(StringResource.getStringResource("miufo00206"));
		}

		if (model.getDataAxis2().getFieldId() != null
				&& !DataTypeConstant.isNumberType(model.getDataAxis2()
						.getDataType())) {
			throw new RuntimeException(StringResource.getStringResource("miufo00207"));
		}

		if (model.getDataAxis3().getFieldId() != null
				&& !DataTypeConstant.isNumberType(model.getDataAxis3()
						.getDataType())) {
			throw new RuntimeException(StringResource.getStringResource("miufo00208"));
		}
	}

}
 