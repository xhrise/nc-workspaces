/**
 * 
 */
package com.ufida.report.chart;

import java.awt.Component;

import javax.swing.JLabel;

import nc.ui.reportquery.component.chart.ChartProperties;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import com.ufsoft.iufo.fmtplugin.chart.ChartModel;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * @author guogang
 * @created at Feb 16, 2009,11:11:29 AM
 *
 */
public class ChartRender implements SheetCellRenderer{

	private ChartPanel m_cp_componet = null;
	private JLabel m_cp_lbl = new JLabel();
	
	/* (non-Javadoc)
	 * @see com.ufsoft.table.re.SheetCellRenderer#getCellRendererComponent(com.ufsoft.table.CellsPane, java.lang.Object, boolean, boolean, int, int, com.ufsoft.table.Cell)
	 */
	public synchronized Component getCellRendererComponent(CellsPane cellsPane,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column, Cell cell) {

		if (value == null) {
			return null;
		}
		ChartModel cm = (ChartModel) value;
		if(cm.getPropertiesXml() !=  null){
			ChartPropertiesValues properties = ChartPropertiesValues.parseXmlString(cm.getPropertiesXml());
			properties.updateChartProperties(cm);
		}
		
		JFreeChart chart = cm.getChart();//ChartFactory.createPieChart3D
						
		if (m_cp_componet == null) {
			// edit by guogang 2009-3-20 解决没有显示图表就送打印的时候无法打印出图表的问题
			m_cp_componet = new ChartPanel(chart,false);
		} else {
			m_cp_componet.setChart(chart);
		}
		// m_cp_componet.setVerticalAxisTrace(true);
		// m_cp_componet.setHorizontalAxisTrace(true);
		// m_cp_componet.setVerticalZoom(true);
		// m_cp_componet.setHorizontalZoom(true);

		
		return m_cp_componet;// new ChartPanel(chart, true);

	}

}
