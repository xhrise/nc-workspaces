package com.ufida.report.chart;

import java.awt.Component;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import com.ufida.dataset.DataSet;
import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufida.dataset.metadata.Field;
import com.ufsoft.iufo.fmtplugin.chart.AxisPropEvent;
import com.ufsoft.iufo.fmtplugin.chart.AxisPropListener;
import com.ufsoft.iufo.fmtplugin.chart.ChartAxis;
import com.ufsoft.iufo.fmtplugin.chart.ChartModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.chart.IChartModel;

/**
 *  图表设置面版基类
 * @author liuyy
 *
 */
public abstract class ChartSettingPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private IChartModel chartModel = null;
	

	private List<AxisPropListener> listeners = new Vector<AxisPropListener>();
	
	public ChartSettingPanel(IChartModel chartModel) {
		this.chartModel = chartModel;
	}

	abstract void save();

	public void setChartModel(IChartModel chartModel) {
		this.chartModel = chartModel;
	}

	public IChartModel getChartModel() {
		return chartModel;
	}
	
	protected Field[] getFieldsInfo(){
		ChartModel model = (ChartModel) chartModel;
		DataSet ds = model.getDataSetInstance();
		if(ds == null){
			return null;
		}
		return ds.getMetaData().getFields(true);
	}
	
	public Component add(Component comp){
		if(comp instanceof AxisPropListener){
			addListener((AxisPropListener) comp);
		}
		return super.add(comp);
		
	}
	
	protected void addListener(AxisPropListener l){
		listeners.add(l);
	}
	
	public void fireEvent(AxisPropEvent e){
		for(AxisPropListener l: listeners){
			l.actionPerformed(e);
		}
	}
	
	public void changeDataSet(){
		AxisPropEvent e = new AxisPropEvent(this, null, getFieldsInfo());
		fireEvent(e);
	}

	

	/**
	 * @i18n miufo00296=不能为空。
	 */
	protected void validateInput(ChartAxis axis, String note){
		if(axis.getFieldId() == null){
			throw new RuntimeException(note + StringResource.getStringResource("miufo00296"));
		}
	}
	
	/**
	 * @i18n miufo00297=关联字段的数据类型必须为数值类型。
	 */
	protected void validateDataType(ChartAxis axis, String note) {
		if(axis == null || axis.getFieldId() == null){
			return ;
		}
		if (!DataTypeConstant.isNumberType(axis.getDataType())) {
			throw new RuntimeException(note + StringResource.getStringResource("miufo00297"));
		}
	}

	protected void validateTypeAndInput(ChartAxis axis, String note) {
		validateInput(axis, note);
		validateDataType(axis, note);
	}

}
 