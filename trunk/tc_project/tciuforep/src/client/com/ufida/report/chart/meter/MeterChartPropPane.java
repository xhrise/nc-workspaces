/*
 * Created on 2005-5-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.meter;

import javax.swing.JPanel;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.MeterPlot;

import com.ufida.report.chart.panel.ChartPropertyPane;
import com.ufsoft.iufo.resource.StringResource;


/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MeterChartPropPane  extends ChartPropertyPane {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel[] plotPanels = null;
    /**
     * @param chartProperty
     */
    public MeterChartPropPane(MeterChartProperty meterChartProperty, JFreeChart sampleChart) {
        super(meterChartProperty, sampleChart);
        if(!(sampleChart.getPlot() instanceof MeterPlot))
            throw new IllegalArgumentException();
        this.setName(StringResource.getStringResource("ubichart00160"));
    }
  
    /* (non-Javadoc)
     * @see com.ufsoft.report.bsplugin.chart.panel.ChartPropertyPane#getPlotPanels()
     */
    protected JPanel[] getPlotPanels() {
        if(plotPanels == null) {
            MeterPlotPropPane pane = new MeterPlotPropPane((MeterPlotProperty) this.getChartProperty().getPlotProperty(), 
                    (MeterPlot) this.getSampleChart().getPlot());  
            plotPanels = new JPanel[]{pane};
        }
        
        
            
        return plotPanels;
    }
}
