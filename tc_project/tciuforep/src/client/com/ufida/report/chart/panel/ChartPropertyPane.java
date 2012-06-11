/*
 * Created on 2005-4-29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.panel;

import javax.swing.JPanel;

import org.jfree.chart.JFreeChart;

import com.ufida.report.chart.common.ChartPublic;
import com.ufida.report.chart.property.ChartProperty;

/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ChartPropertyPane extends nc.ui.pub.beans.UITabbedPane{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 4226127697754234524L;
	private ChartProperty m_chartProperty = null;  
    private JPanel m_legendPanel = null;
    private JPanel m_appearncePanel = null;
    /**示例图*/
    private JFreeChart m_sampleChart = null;
  
    /**
     * @param pageName
     */
    public ChartPropertyPane(ChartProperty chartProperty, JFreeChart sampleChart) {        
        if(chartProperty == null || sampleChart == null)
            throw new IllegalArgumentException();
        
        m_chartProperty = chartProperty; 
        if(sampleChart == null) {
            m_sampleChart = ChartPublic.createSampleChart(chartProperty.getChartType());
        }else {
            m_sampleChart = sampleChart;
        }        
        initPane();
    }
    
    private void initPane() { 
        this.removeAll();
        if(getLegendPanel() != null) {
            PreviewPanel panel = new PreviewPanel(getLegendPanel(), m_sampleChart);
            this.add(getLegendPanel().getName(),panel);
        }
        
        if(getPlotPanels() != null) {
            JPanel[] panels = getPlotPanels();
            for(int i = 0; i < panels.length; i++ ) {
                PreviewPanel panel = new PreviewPanel(panels[i], m_sampleChart);
                this.add(panels[i].getName(), panel);
            }
            
        }
        if(getAppearncePanel() != null) {
            PreviewPanel panel = new PreviewPanel(getAppearncePanel(), m_sampleChart);            
            this.add(getAppearncePanel().getName(), panel);
        }
        
        
    }
    
    protected JPanel getLegendPanel() {   
        if(m_legendPanel == null) {
            m_legendPanel = new LegendPropPanel(m_chartProperty.getTitleProperty(), m_chartProperty.getLegendProperty(), this.getSampleChart());            
        }
        return m_legendPanel;
    }
    
    protected void setLegendPanel(JPanel panel) {   
        m_legendPanel = panel;
    }
    
    /***
     * 返回Plot对应的属性面板,
     * 子类应该继承此方法,添加对应Plot对应属性面板
     * @return
     */
    protected  JPanel[]  getPlotPanels(){
    	return null;
    }
    
    protected JPanel getAppearncePanel() {
        if(m_appearncePanel == null) {
            m_appearncePanel = new ChartAppearncePanel(this.getChartProperty(), this.getSampleChart());
        }
        return m_appearncePanel;
    }
    /**
     * @return Returns the m_sampleChart.
     */
    public JFreeChart getSampleChart() {
        return m_sampleChart;
    }
    /**
     * @param chart The m_sampleChart to set.
     */
    public void setSampleChart(JFreeChart chart) {
        m_sampleChart = chart;           
    }
    /**
     * @return Returns the m_chartProperty.
     */
    public ChartProperty getChartProperty() {
        return m_chartProperty;
    }      
}
