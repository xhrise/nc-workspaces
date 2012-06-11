package com.ufida.report.chart;

import com.ufsoft.iufo.fmtplugin.chart.MeterChartModel;
import com.ufsoft.table.chart.IChartModel;
import com.ufsoft.iufo.resource.StringResource;

public class MeterSettingPanel extends ChartSettingPanel {
	private static final long serialVersionUID = 2098981543367246928L;

	ChartMeterPanel cmp = null;
	
	/**
	 * @i18n miufo00269=Êý¾ÝÖá
	 */
	public MeterSettingPanel(IChartModel chartModel) {
		super(chartModel);
		
		setLayout(null);
		MeterChartModel mcm = (MeterChartModel) chartModel;
		
		AxisPropPanel ap = new AxisPropPanel(StringResource.getStringResource("miufo00269"), mcm.getDataAxis(), getFieldsInfo());
		ap.setBounds(0, 0, 505, 64);
		this.add(ap);
		
		cmp = new ChartMeterPanel(getChartModel());
		this.add(cmp);
		cmp.setBounds(0, 70, 505, 120);
		
	}


	@Override
	void save() {
		cmp.save();
	}

}
 