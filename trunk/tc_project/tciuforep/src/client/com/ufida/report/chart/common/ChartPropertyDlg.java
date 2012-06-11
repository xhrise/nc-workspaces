/*
 * Created on 2005-5-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.common;
import com.ufida.iufo.pub.tools.AppDebug;

import java.awt.Component;

import org.jfree.chart.JFreeChart;

import com.ufida.report.chart.panel.ChartPropertyPane;
import com.ufida.report.chart.property.ChartProperty;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.BaseDialog;
/**
 * 图属性对话框
 * @author caijie
 */
public class ChartPropertyDlg extends BaseDialog{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2290462705512215164L;

	/**
     * 图表
     */
    private JFreeChart chart = null;
    
    ChartProperty m_chartProperty = null;
    
    ChartPropertyPane m_chartPropertyPane = null;
    
    public ChartPropertyDlg(Component parentComponent, ChartProperty chartProperty , JFreeChart chart) {
        super(parentComponent, StringResource.getStringResource("ubichart00024"), true);
        this.chart = chart;
        
        if(chartProperty == null)
            throw new IllegalArgumentException(StringResource.getStringResource("ubichart00025"));
        m_chartProperty = chartProperty;
        init();       
        this.setSize(650,450);
        this.setLocationRelativeTo(this.getParent());
    }
    
    private void init() {       
        try {
            m_chartPropertyPane = ChartPropertyPaneFactory.createChartPropertyPane(m_chartProperty, (JFreeChart) chart.clone());
        } catch (CloneNotSupportedException e1) {
            return;
        }
        if(m_chartPropertyPane != null) {
            try {
                m_chartPropertyPane.setSampleChart(((JFreeChart) chart.clone()));
            } catch (CloneNotSupportedException e) {
                AppDebug.debug("(JFreeChart) chart.clone()) is error");//@devTools System.out.print("(JFreeChart) chart.clone()) is error");
                JFreeChart sample = ChartPublic.createSampleChart(m_chartProperty.getChartType());
                m_chartPropertyPane.setSampleChart(sample);
            }
            this.getDialogArea().add(m_chartPropertyPane);
        }
        
    }
    
    public static void main(String[] args) {
//        ChartPropertyDlg dlg = new ChartPropertyDlg(new BarChartProperty(), null);
//        dlg.setSize(400, 300);         
//        dlg.show();
       
    }    
    /**
     * @return Returns the m_chartProperty.
     */
    public ChartProperty getChartProperty() {
        return m_chartProperty;
    }
}
