/*
 * 创建日期 2006-9-8
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.ufida.report.chart.common;

import org.jfree.chart.JFreeChart;

import com.ufida.report.chart.area.AreaChartPropPane;
import com.ufida.report.chart.area.AreaChartProperty;
import com.ufida.report.chart.bar.BarChartPropPane;
import com.ufida.report.chart.bar.BarChartProperty;
import com.ufida.report.chart.meter.MeterChartPropPane;
import com.ufida.report.chart.meter.MeterChartProperty;
import com.ufida.report.chart.panel.ChartPropertyPane;
import com.ufida.report.chart.pie.MultiPieChartPropertyPane;
import com.ufida.report.chart.pie.MultiplePieChartProperty;
import com.ufida.report.chart.pie.PieChartPropPane;
import com.ufida.report.chart.pie.PieChartProperty;
import com.ufida.report.chart.property.ChartProperty;

public class ChartPropertyPaneFactory {
    /**
     * 根据图属性对象生成图属性面板
     * @param chartProperty
     * @return
     */
    public static ChartPropertyPane createChartPropertyPane(ChartProperty chartProperty, JFreeChart sampleChart) {           
        if(chartProperty == null) return null;
        
        int chartType = chartProperty.getChartType();  
        int parent = ChartPublic.getParent(chartType);
        
        JFreeChart chart = sampleChart;        
        if(sampleChart == null) {
            chart = ChartPublic.createSampleChart(chartType);
        }
        
        
        if(chartType == ChartPublic.BAR_CHART || parent == ChartPublic.BAR_CHART
        		||chartType == ChartPublic.BANNER_CHART || parent == ChartPublic.BANNER_CHART)
        {            
            return new BarChartPropPane((BarChartProperty)chartProperty, chart);
        }    
        
        if(chartType == ChartPublic.STANDARD_PIE_CHART )
        {
            return new PieChartPropPane((PieChartProperty)chartProperty, chart);
        }  
        if(chartType == ChartPublic.MULTI_PIE_CHART )
        {
            return new MultiPieChartPropertyPane((MultiplePieChartProperty)chartProperty, chart);
        }  
        if(chartType == ChartPublic.AREA_CHART || parent == ChartPublic.AREA_CHART)        		
        {            
            return new AreaChartPropPane((AreaChartProperty)chartProperty, chart);
        }   
        
        if(chartType == ChartPublic.METER_CHART || ChartPublic.getParent(chartType) == ChartPublic.METER_CHART)
        {
            return new MeterChartPropPane((MeterChartProperty)chartProperty, chart);
        }         
//        System.out.println("你还没有在ChartPublic中注册相应的属性面板");
        
        return new ChartPropertyPane(chartProperty, chart);  
    }

}
