package com.ufida.report.chart;
 

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufida.dataset.metadata.Field;
import com.ufsoft.iufo.fmtplugin.chart.PieChartModel;
import com.ufsoft.table.chart.IChartModel;
import com.ufsoft.iufo.resource.StringResource;

public class PieSettingPanel extends ChartSettingPanel {
	private static final long serialVersionUID = 2098981543367246928L;
	
	JCheckBox cbItemLabel = new JCheckBox();
	
	/**
	 * @i18n uibichart00051=分类轴
	 * @i18n miufo00266=次分类轴
	 * @i18n miufo00267=高级选项
	 * @i18n miufo00268=显示值标签
	 */
	public PieSettingPanel(IChartModel chartModel) {
		super(chartModel);
		
		setLayout(null);
		PieChartModel model = (PieChartModel) chartModel;
		
		Field[] flds = getFieldsInfo();
		
		AxisPropPanel ap = new AxisPropPanel(StringResource.getStringResource("uibichart00051"), model.getCategoryAxis(), flds);
		ap.setBounds(0, 0, 505, 64);
		this.add(ap);

		ap = new AxisPropPanel(StringResource.getStringResource("miufo00266"), model.getCategoryAxis2(), flds);
		ap.setBounds(0, 70, 505, 64);
		this.add(ap);
		
//		ap = new AxisPropPanel("数据轴", model.getDataAxis(), flds);
//		ap.setBounds(0, 140, 505, 64);
//		this.add(ap);
//		

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(500, 65));
		panel.setBounds(0, 210, 505, 64);
		this.add(panel);
		panel.setBorder(BorderFactory.createTitledBorder(null,
				StringResource.getStringResource("miufo00267"), TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("Dialog", Font.BOLD, 12), Color.blue));
		
		cbItemLabel.setText(StringResource.getStringResource("miufo00268"));
		cbItemLabel.setBounds(39, 42, 118, 26);
		panel.add(cbItemLabel);
		
		cbItemLabel.setSelected(model.isItemLabelVisible());
		
	}
	
	/**
	 * @i18n miufo00273=分类轴不能为空。
	 */
	@Override
	void save() {
		
		PieChartModel model = (PieChartModel) getChartModel();
		if(model.getCategoryAxis2().getFieldId() == null){
			model.setLegend(false); 
		}
		if(model.getCategoryAxis().getFieldId() == null){
			throw new RuntimeException(StringResource.getStringResource("miufo00273"));
		}
		
//		if(model.getDataAxis().getFieldId() == null){
//			throw new RuntimeException("数据轴不能为空。");
//		}
//		
//		if(model.getDataAxis().getFieldId() != null
//				&& !DataTypeConstant.isNumberType(model.getDataAxis().getDataType())){
//			throw new RuntimeException("数据轴关联字段的数据类型必须为数值类型。");
//		}
		
		model.setItemLabelVisible(cbItemLabel.isSelected());
		
	}

}
 