package com.ufida.report.chart;

import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufida.dataset.metadata.Field;
import com.ufsoft.iufo.fmtplugin.chart.XYSericesChartModel;
import com.ufsoft.table.chart.IChartModel;
import com.ufsoft.iufo.resource.StringResource;

public class XYSericesSettingPanel  extends ChartSettingPanel {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * @i18n miufo00202=X数据轴
	 * @i18n miufo00203=Y数据轴
	 * @i18n miufo00385=系列轴
	 */
	public XYSericesSettingPanel(IChartModel chartModel) {
		super(chartModel);
		
		setLayout(null);
		XYSericesChartModel model = (XYSericesChartModel) chartModel;
		
		Field[] flds = getFieldsInfo();
		
		AxisPropPanel ap = new AxisPropPanel(StringResource.getStringResource("miufo00202"), model.getDataAxis(), flds);
		ap.setBounds(0, 0, 505, 64);
		this.add(ap);

		ap = new AxisPropPanel(StringResource.getStringResource("miufo00203"), model.getDataAxis2(), flds);
		ap.setBounds(0, 70, 505, 64);
		this.add(ap);
		
		ap = new AxisPropPanel(StringResource.getStringResource("miufo00385"), model.getCategoryAxis(), flds);
		ap.setBounds(0, 140, 505, 64);
		this.add(ap);
		
	}
	
	/**
	 * @i18n miufo00386=X、Y数据轴不能为空。
	 * @i18n miufo00206=X数据轴关联字段的数据类型必须为数值类型。
	 * @i18n miufo00207=Y数据轴关联字段的数据类型必须为数值类型。
	 */
	@Override
	void save() {
		
		XYSericesChartModel model = (XYSericesChartModel) getChartModel();
		
		if(model.getDataAxis().getFieldId() == null || model.getDataAxis2().getFieldId() == null){
			throw new RuntimeException(StringResource.getStringResource("miufo00386"));
		}
		
		if(model.getDataAxis().getFieldId() != null
				&& !DataTypeConstant.isNumberType(model.getDataAxis().getDataType())){
			throw new RuntimeException(StringResource.getStringResource("miufo00206"));
		}
		
		if(model.getDataAxis2().getFieldId() != null
				&& !DataTypeConstant.isNumberType(model.getDataAxis2().getDataType())){
			throw new RuntimeException(StringResource.getStringResource("miufo00207"));
		}	
		
	}
} 