/*
 * Created on 2005-7-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.pie;

import javax.swing.JPanel;

import org.jfree.chart.JFreeChart;

import com.ufida.report.chart.common.ChartPublic;
import com.ufida.report.chart.panel.ChartPropertyPane;
import com.ufida.report.chart.property.ChartProperty;

/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MultiPieChartPropertyPane extends ChartPropertyPane{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @param chartProperty
     * @param sampleChart
     */
    public MultiPieChartPropertyPane(ChartProperty chartProperty, JFreeChart sampleChart) {
        super(chartProperty, sampleChart);
        this.setName(ChartPublic.getChartName(ChartPublic.MULTI_PIE_CHART));
    }

   
    /* (non-Javadoc)
     * @see com.ufida.report.chart.panel.ChartPropertyPane#getPlotPanels()
     */
    protected JPanel[] getPlotPanels() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public static void main(String[] args) {
    }

}
