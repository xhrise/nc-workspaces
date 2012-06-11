package com.ufida.report.chart;

import com.ufida.dataset.metadata.Field;
import com.ufsoft.iufo.fmtplugin.chart.GanttChartModel;
import com.ufsoft.table.chart.IChartModel;
import com.ufsoft.iufo.resource.StringResource;

public class GanttSettingPanel extends ChartSettingPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * @i18n ubiquery0138=��������
	 * @i18n miufo00406=�������
	 * @i18n miufo00407=������ʼʱ��
	 * @i18n miufo00408=�������ʱ��
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
	 * @i18n miufo00409=�������ơ�������ʼʱ������ʱ�䲻��Ϊ�ա�
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
		// throw new RuntimeException("X����������ֶε��������ͱ���Ϊ��ֵ���͡�");
		// }
		//		
		// if(model.getDataAxis2().getFieldId() != null
		// &&
		// !DataTypeConstant.isNumberType(model.getDataAxis2().getDataType())){
		// throw new RuntimeException("Y����������ֶε��������ͱ���Ϊ��ֵ���͡�");
		// }

	}

}
 