package com.ufida.report.chart.pie;

import javax.swing.JPanel;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;

import com.ufida.report.chart.common.ChartPublic;
import com.ufida.report.chart.panel.ChartPropertyPane;

/**
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 * @author caijie
 */
public class PieChartPropPane extends ChartPropertyPane {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel[] plotPanels = null;
    /**
     * @param chartProperty
     */
    public PieChartPropPane(PieChartProperty pieChartProperty, JFreeChart sampleChart) {
        super(pieChartProperty, sampleChart);
        if(!(sampleChart.getPlot() instanceof PiePlot))
            throw new IllegalArgumentException();
        this.setName(ChartPublic.getChartName(ChartPublic.PER_STATCKED_BAR_CHART));
    }
  
    /* (non-Javadoc)
     * @see com.ufsoft.report.bsplugin.chart.panel.ChartPropertyPane#getPlotPanels()
     */
    protected JPanel[] getPlotPanels() {
        if(plotPanels == null) {
            PiePlotPropPane pane = new PiePlotPropPane((PiePlotProperty) this.getChartProperty().getPlotProperty(), (PiePlot) this.getSampleChart().getPlot());      
            plotPanels =  new JPanel[]{pane};
        } 
        return plotPanels;
    }
}
