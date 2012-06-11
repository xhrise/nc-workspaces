package com.ufida.report.chart;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.ufsoft.iufo.fmtplugin.chart.MeterChartModel;
import com.ufsoft.report.util.FloatDocument;
import com.ufsoft.table.chart.IChartModel;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 仪表设置面板
 * @author liuyy
 *
 */
public class ChartMeterPanel extends ChartSettingPanel {
 
	private JTextField fldMax;
	private JTextField fldNormal;
	private JTextField fldCritical;
	private JTextField fldWarring;
	private JTextField fldMin;
	private JTextField fldUnit;
	private static final long serialVersionUID = 8509990194985586800L;
	
	JTextField[] m_flds = null;
	
	/**
	 * @i18n miufo00343=仪表图选项
	 * @i18n miufo00344=数值单位：
	 * @i18n miufo00345=阀值设定：
	 * @i18n uibichart00011=最小值
	 * @i18n miufo00346=正常阀值
	 * @i18n miufo00347=警告阀值
	 * @i18n miufo00348=危急阀值
	 * @i18n uibichart00012=最大值
	 */
	ChartMeterPanel(IChartModel chartModel) {
		super(chartModel);
		
		setLayout(null);
		
		setBorder(BorderFactory.createTitledBorder(null,
				StringResource.getStringResource("miufo00343"), TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("Dialog", Font.BOLD, 12), Color.blue));

		final JLabel label = new JLabel();
		label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		label.setBounds(10, 26, 87, 18);
		label.setText(StringResource.getStringResource("miufo00344"));
		add(label);

		fldUnit = new JTextField();
		fldUnit.setBounds(110, 24, 207, 22);
		add(fldUnit);

		final JLabel label_1 = new JLabel();
		label_1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		label_1.setText(StringResource.getStringResource("miufo00345"));
		label_1.setBounds(10, 57, 87, 18);
		add(label_1);

		final JLabel label_2 = new JLabel();
		label_2.setText(StringResource.getStringResource("uibichart00011"));
		label_2.setBounds(110, 57, 45, 18);
		add(label_2);

		fldMin = new JTextField();
		fldMin.setBounds(155, 55, 51, 22);
		add(fldMin);

		final JLabel label_2_1 = new JLabel();
		label_2_1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		label_2_1.setText(StringResource.getStringResource("miufo00346"));
		label_2_1.setBounds(90, 88, 65, 18);
		add(label_2_1);

		fldWarring = new JTextField();
		fldWarring.setBounds(265, 84, 51, 22);
		add(fldWarring);

		final JLabel label_2_2 = new JLabel();
		label_2_2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		label_2_2.setText(StringResource.getStringResource("miufo00347"));
		label_2_2.setBounds(207, 88, 58, 18);
		add(label_2_2);

		fldCritical = new JTextField();
		fldCritical.setBounds(378, 82, 51, 22);
		add(fldCritical);

		final JLabel label_2_2_1 = new JLabel();
		label_2_2_1.setText(StringResource.getStringResource("miufo00348"));
		label_2_2_1.setBounds(322, 87, 65, 18);
		add(label_2_2_1);

		fldNormal = new JTextField();
		fldNormal.setBounds(156, 85, 51, 22);
		add(fldNormal);

		final JLabel label_2_2_2 = new JLabel();
		label_2_2_2.setText(StringResource.getStringResource("uibichart00012"));
		label_2_2_2.setBounds(221, 56, 45, 18);
		add(label_2_2_2);

		fldMax = new JTextField();
		fldMax.setBounds(266, 54, 51, 22);
		add(fldMax);
		
		MeterChartModel mcm = (MeterChartModel) getChartModel();
		fldUnit.setText(mcm.getUnitLabel());
		
		m_flds = new JTextField[]{fldMin, fldNormal, fldWarring, fldCritical, fldMax};
	    double[] values = mcm.getSettingValues();
		for(int i = 0; i < 5; i++){
			m_flds[i].setDocument(new FloatDocument());
			m_flds[i].setHorizontalAlignment(JTextField.CENTER);
			double v = values[i];
			m_flds[i].setText(v + "");
		}
		 
	}
 
	/**
	 * @i18n miufo00349=仪表图阀值为空。
	 * @i18n miufo00350=仪表图阀值设定错误。
	 */
	@Override
	void save() {
		
		MeterChartModel mcm = (MeterChartModel) getChartModel();
		mcm.setUnitLabel(fldUnit.getText());
		
		double[] values = mcm.getSettingValues();
		for(int i = 0; i < 5; i++){
			String text = m_flds[i].getText();
			if(i > 0 && (text == null || text.trim().length() < 1)){
				throw new RuntimeException(StringResource.getStringResource("miufo00349"));
			}
			if(i >= 1){
				double v1 = new Double(values[i - 1] + "").doubleValue();
				double v2 = new Double(text).doubleValue();
				if(v1 >= v2){
					throw new RuntimeException(StringResource.getStringResource("miufo00350"));
				}
			}
			values[i] = new Double(text);
		}
	}

}
 