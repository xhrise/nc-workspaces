package com.ufida.report.chart;

import javax.swing.JPanel;

import com.ufida.dataset.metadata.Field;
import com.ufsoft.iufo.fmtplugin.chart.CandlestickChartModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.chart.IChartModel;

public class CandlestickSettingPanel  extends ChartSettingPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * @i18n miufo00368=时间轴
	 * @i18n miufo00269=数据轴
	 * @i18n miufo00369=最高值
	 * @i18n miufo00370=最低值
	 * @i18n miufo00371=开启值
	 * @i18n miufo00372=闭合值
	 * @i18n iufobi00014=成交量轴
	 */
	public CandlestickSettingPanel(IChartModel chartModel) {
		super(chartModel);

		setLayout(null);
		CandlestickChartModel model = (CandlestickChartModel) chartModel;

		Field[] flds = getFieldsInfo();
		int step = 70;
		int index = 0;
		int width = 505;
		int height = 58;
		

		JPanel panel = TimeSericesSettingPanel.generateTimeTypeSetting(model);
		panel.setBounds(0, 0, 505, 60);
		this.add(panel);
		
		AxisPropPanel ap = new AxisPropPanel(StringResource.getStringResource("miufo00368"), model.getTimeAxis(),
				flds);
		ap.setBounds(0, 62, 505, height);
		this.add(ap);
		
		ap = new AxisPropPanel(StringResource.getStringResource("iufobi00014"), model.getDataAxis(), flds);
		index++;
		ap.setBounds(0, 120, 505, height);
		this.add(ap);

		ap = new AxisPropPanel(StringResource.getStringResource("miufo00369"), model.getHighAxis(), flds);
		index++;
		ap.setBounds(0, 180, 250, height);
		this.add(ap);

		ap = new AxisPropPanel(StringResource.getStringResource("miufo00370"), model.getLowAxis(), flds);
		index++;
		ap.setBounds(255, 180, 250, height);
		this.add(ap);
		
		ap = new AxisPropPanel(StringResource.getStringResource("miufo00371"), model.getOpenAxis(), flds);
		index++;
		ap.setBounds(0, 240, 250, height);
		this.add(ap);
		
		ap = new AxisPropPanel(StringResource.getStringResource("miufo00372"), model.getCloseAxis(), flds);
		index++;
		ap.setBounds(255, 240, 250, height);
		this.add(ap);

	}
	

	/**
	 * @i18n miufo00368=时间轴
	 * @i18n miufo00269=数据轴
	 * @i18n miufo00369=最高值
	 * @i18n miufo00370=最低值
	 * @i18n miufo00371=开启值
	 * @i18n miufo00372=闭合值
	 * @i18n iufobi00014=成交量轴
	 */
	@Override
	void save() {

		CandlestickChartModel model = (CandlestickChartModel) getChartModel();
		
		validateInput(model.getTimeAxis(), StringResource.getStringResource("miufo00368"));
		
		validateDataType(model.getDataAxis(), StringResource.getStringResource("iufobi00014"));
		
		validateTypeAndInput(model.getHighAxis(), StringResource.getStringResource("miufo00369"));
		
		validateTypeAndInput(model.getLowAxis(), StringResource.getStringResource("miufo00370"));
		
		validateDataType(model.getOpenAxis(), StringResource.getStringResource("miufo00371"));
		
		validateTypeAndInput(model.getCloseAxis(), StringResource.getStringResource("miufo00372"));
		 
	}

}
  