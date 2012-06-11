/*
 * Created on 2005-5-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.panel;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import com.ufsoft.iufo.resource.StringResource;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PreviewPanel extends nc.ui.pub.beans.UIPanel implements ChartChangeListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 图预览所用面版，主要是取消菜单等部分功能
     */
    private class SampleChartPanel extends ChartPanel {   
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
         * Constructs a panel that displays the specified chart.
         *
         * @param chart  the chart.
         */
        public SampleChartPanel(JFreeChart chart) {
            
            super(
                chart,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                DEFAULT_MINIMUM_DRAW_WIDTH,
                DEFAULT_MINIMUM_DRAW_HEIGHT,
                DEFAULT_MAXIMUM_DRAW_WIDTH,
                DEFAULT_MAXIMUM_DRAW_HEIGHT,
                DEFAULT_BUFFER_USED,
                false,  // properties
                false,  // save
                false,  // print
                false,  // zoom
                false   // tooltips
            );
            chart.addChangeListener(this);
        }

    }

    JPanel m_samplePanel = null;
	/**
	 * This is the default constructor
	 */
	public PreviewPanel(JPanel content, JFreeChart  chart) {
		super();

		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		Dimension size = new Dimension(600,300);
		this.setPreferredSize(size);
		this.setSize(size);
		this.setMaximumSize(size);
		this.setMinimumSize(size);
		
		Dimension contentSize = new Dimension(300,300);
		content.setPreferredSize(contentSize);
		content.setSize(contentSize);
		content.setMaximumSize(contentSize);
		content.setMinimumSize(contentSize);
		
		Dimension priviewSize = new Dimension(300,300);
		getSampleChartPanel(chart).setPreferredSize(priviewSize);
		getSampleChartPanel(chart).setSize(priviewSize);
		getSampleChartPanel(chart).setMaximumSize(priviewSize);
		getSampleChartPanel(chart).setMinimumSize(priviewSize);

		this.add(content);
		this.add(getSampleChartPanel(chart));		
	}
	
	/**
	 * @i18n ubiquery0127=预览
	 */
	public JPanel getSampleChartPanel(JFreeChart  chart) {
        if (m_samplePanel == null) {
            m_samplePanel = new SampleChartPanel(chart);			
            m_samplePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, StringResource.getStringResource("ubiquery0127"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));           
    			
		}
		return m_samplePanel;
    }

    /* (non-Javadoc)
     * @see org.jfree.chart.event.ChartChangeListener#chartChanged(org.jfree.chart.event.ChartChangeEvent)
     */
    public void chartChanged(ChartChangeEvent event) {
        this.validate();
        this.repaint();
        
    }

//    /* (non-Javadoc)
//     * @see org.jfree.chart.event.PlotChangeListener#plotChanged(org.jfree.chart.event.PlotChangeEvent)
//     */
//    public void plotChanged(PlotChangeEvent event) {
//        this.invalidate();
//        this.validate();
//        this.repaint();
//    }
}
