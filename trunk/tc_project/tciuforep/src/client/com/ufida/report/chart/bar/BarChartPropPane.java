/*
 * Created on 2005-4-29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.bar;

import javax.swing.JPanel;

import org.jfree.chart.JFreeChart;

import com.ufida.report.chart.common.ChartPublic;
import com.ufida.report.chart.panel.CategoryPlotPropPane;
import com.ufida.report.chart.panel.ChartPropertyPane;
import com.ufida.report.chart.property.CategoryPlotProperty;

/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BarChartPropPane  extends ChartPropertyPane{ 
    /**
	 * 
	 */
	private static final long serialVersionUID = -4416801168744212298L;
	JPanel[] plotPanels = null;
    /**
     * @param pageName
     * @param chartProperty
     */
    public BarChartPropPane(BarChartProperty barChartProperty, JFreeChart sampleChart) {        
        super(barChartProperty, sampleChart);  
        this.setName(ChartPublic.getChartName(ChartPublic.BAR_CHART));
    }
    
    protected JPanel[]  getPlotPanels() {
        if(plotPanels == null) {
            CategoryPlotPropPane pane = new CategoryPlotPropPane((CategoryPlotProperty) this.getChartProperty().getPlotProperty(), this.getSampleChart().getCategoryPlot());      
            plotPanels =  pane.getPropertyPanels();              
        }
        return plotPanels;
        
    }    
}    
