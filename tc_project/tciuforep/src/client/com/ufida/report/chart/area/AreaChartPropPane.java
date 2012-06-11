package com.ufida.report.chart.area;

import javax.swing.JPanel;

import org.jfree.chart.JFreeChart;

import com.ufida.report.chart.common.ChartPublic;
import com.ufida.report.chart.panel.CategoryPlotPropPane;
import com.ufida.report.chart.panel.ChartPropertyPane;
import com.ufida.report.chart.property.CategoryPlotProperty;

public class AreaChartPropPane extends ChartPropertyPane{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1324336840698954998L;
	JPanel[] plotPanels = null;
    /**
     * @param pageName
     * @param chartProperty
     */
    public AreaChartPropPane(AreaChartProperty areaChartProperty, JFreeChart sampleChart) {        
        super(areaChartProperty, sampleChart);  
        this.setName(ChartPublic.getChartName(ChartPublic.AREA_CHART));
    }
    
    protected JPanel[]  getPlotPanels() {
        if(plotPanels == null) {
            CategoryPlotPropPane pane = new CategoryPlotPropPane((CategoryPlotProperty) this.getChartProperty().getPlotProperty(), this.getSampleChart().getCategoryPlot());      
            plotPanels =  pane.getPropertyPanels();              
        }
        return plotPanels;
        
    }    

}
