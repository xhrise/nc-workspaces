package com.ufida.report.chart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufida.dataset.metadata.Field;
import com.ufsoft.iufo.fmtplugin.chart.ITimeChartModel;
import com.ufsoft.iufo.fmtplugin.chart.TimeSericesChartModel;
import com.ufsoft.table.chart.IChartModel;
import com.ufsoft.iufo.resource.StringResource;


public class TimeSericesSettingPanel  extends ChartSettingPanel {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * @i18n miufo00368=时间轴
	 * @i18n miufo00269=数据轴
	 * @i18n miufo00455=序列
	 */
	public TimeSericesSettingPanel(IChartModel chartModel) {
		super(chartModel);
		
		setLayout(null);
		final TimeSericesChartModel model = (TimeSericesChartModel) chartModel;
		
		Field[] flds = getFieldsInfo();

		JPanel panel = generateTimeTypeSetting(model);
		panel.setBounds(0, 0, 505, 64);
		this.add(panel);
		
		AxisPropPanel ap = new AxisPropPanel(StringResource.getStringResource("miufo00368"), model.getTimeAxis(), flds);
		ap.setBounds(0, 70, 505, 64);
		this.add(ap);

		ap = new AxisPropPanel(StringResource.getStringResource("miufo00269"), model.getDataAxis(), flds);
		ap.setBounds(0, 140, 505, 64);
		this.add(ap);
		
		ap = new AxisPropPanel(StringResource.getStringResource("miufo00455"), model.getSericesAxis(), flds);
		ap.setBounds(0, 210, 505, 64);
		this.add(ap);
		
		 
		
	}
	
	/**
	 * @i18n miufo00456=时间类型
	 */
	public static JPanel generateTimeTypeSetting(final ITimeChartModel model){
		
		JPanel panel = new JPanel();

		panel.setPreferredSize(new Dimension(500, 65));
		panel.setBorder(BorderFactory.createTitledBorder(null,
				StringResource.getStringResource("miufo00456"), TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("Dialog", Font.BOLD, 12), Color.blue));
		
		ButtonGroup bg = new ButtonGroup();
		for(int i = 0; i < 7; i++){
			final JRadioButton rd = new JRadioButton();
			bg.add(rd);
			panel.add(rd);
			rd.setText(ITimeChartModel.TYPES[i][0]);
			if(ITimeChartModel.TYPES[i][1].equals(model.getTimeType() + "")){
				rd.setSelected(true);
			}
			rd.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(rd.isSelected()){
						String text = rd.getText();
						for(String[] val: ITimeChartModel.TYPES){
							if(val[0].equals(text)){
								int tt = new Integer(val[1]).intValue();
								model.setTimeType(tt);
							}
						}
					}
				}});
			 
		}
		
		return panel;
	}
	
	/**
	 * @i18n miufo00457=时间轴或数据轴不能为空。
	 * @i18n miufo00458=数据轴关联字段的数据类型必须为数值类型。
	 */
	@Override
	void save() {
		
		TimeSericesChartModel model = (TimeSericesChartModel) getChartModel();
		
		if(model.getDataAxis().getFieldId() == null || model.getTimeAxis().getFieldId() == null){
			throw new RuntimeException(StringResource.getStringResource("miufo00457"));
		}
		
		if(model.getDataAxis().getFieldId() != null
				&& !DataTypeConstant.isNumberType(model.getDataAxis().getDataType())){
			throw new RuntimeException(StringResource.getStringResource("miufo00458"));
		}
		
	}
} 