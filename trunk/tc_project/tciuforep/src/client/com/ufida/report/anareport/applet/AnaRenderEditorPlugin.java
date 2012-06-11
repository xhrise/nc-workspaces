/**
 * 
 */
package com.ufida.report.anareport.applet;

import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.JTextField;

import com.ufida.report.chart.ChartRender;
import com.ufida.report.crosstable.CrossTableCellElement;
import com.ufsoft.iufo.fmtplugin.chart.BubbleChartModel;
import com.ufsoft.iufo.fmtplugin.chart.CandlestickChartModel;
import com.ufsoft.iufo.fmtplugin.chart.CategoryChartModel;
import com.ufsoft.iufo.fmtplugin.chart.CombineChartModel;
import com.ufsoft.iufo.fmtplugin.chart.GanttChartModel;
import com.ufsoft.iufo.fmtplugin.chart.MeterChartModel;
import com.ufsoft.iufo.fmtplugin.chart.MultipleAxisChartModel;
import com.ufsoft.iufo.fmtplugin.chart.PieChartModel;
import com.ufsoft.iufo.fmtplugin.chart.TimeSericesChartModel;
import com.ufsoft.iufo.fmtplugin.chart.XYSericesChartModel;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.re.CellRenderAndEditor;
import com.ufsoft.table.re.DefaultSheetCellEditor;
import com.ufsoft.table.re.DefaultSheetCellRenderer;
import com.ufsoft.table.re.DoubleRender;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * 仅仅是个注册绘制器的插件
 * @author guogang
 * @created at Feb 11, 2009,3:29:43 PM
 *
 */
public class AnaRenderEditorPlugin extends AbstractPlugIn {

	/* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.AbstractPlugIn#createDescriptor()
	 */
	@Override
	protected IPluginDescriptor createDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startup() {
		CellRenderAndEditor cellsRender = getReport().getTable()
				.getReanderAndEditor();
		if (cellsRender != null)
			register(cellsRender);
	}

	public void register(CellRenderAndEditor cellsRender){
		cellsRender.registRender(CrossTableCellElement.class, new CrossTableCellRender());
		//@edit by guogang 2009-1-16 解决查询引擎将int型处理成BigDecimal的问题,本来应该处理成扩展属性的编辑和绘制的
		cellsRender.registEditor(BigDecimal.class, new DefaultSheetCellEditor(new JTextField()){
			public Object getCellEditorValue() {
				JTextField editorComponent = (JTextField) getComponent();
				String text=editorComponent.getText();
				if("".equals(text)){
					return null;
				}
//				BigDecimal value=new BigDecimal(text);
				return text;
			}});
		cellsRender.registRender(BigDecimal.class,new DoubleRender(){

			@Override
			protected String decorateValue(Component render, Object value,
					Format format) {
				if(value instanceof BigDecimal){
					return super.decorateValue(render, ((BigDecimal)value).doubleValue(), format);
				}
				return super.decorateValue(render, value, format);
			}
			
		});
		//图表的绘制器
		SheetCellRenderer dataRender = new ChartRender();
		cellsRender.registRender(CategoryChartModel.class, dataRender);
		cellsRender.registRender(MultipleAxisChartModel.class, dataRender);
		cellsRender.registRender(CombineChartModel.class, dataRender);
		cellsRender.registRender(MeterChartModel.class, dataRender);
		cellsRender.registRender(XYSericesChartModel.class, dataRender);
		cellsRender.registRender(BubbleChartModel.class, dataRender);
		cellsRender.registRender(CandlestickChartModel.class, dataRender);
		cellsRender.registRender(GanttChartModel.class, dataRender);
		cellsRender.registRender(TimeSericesChartModel.class, dataRender);
		cellsRender.registRender(PieChartModel.class, dataRender);
		//扩展区域交叉绘制器
		cellsRender.registRender(ExAreaCell.class, new CrossSetRender());
		
	}
}
