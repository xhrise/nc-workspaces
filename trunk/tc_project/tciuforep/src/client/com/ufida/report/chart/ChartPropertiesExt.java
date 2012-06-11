/**
 * 
 */
package com.ufida.report.chart;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ChartPropertyEditPanel;

import com.ufsoft.iufo.fmtplugin.chart.ChartModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;

/**
 * @author wangyga
 * 图表属性设置
 * @created at 2009-8-18,下午03:05:08
 *
 */
public class ChartPropertiesExt extends AbstractChartExt{

	public ChartPropertiesExt(ChartPlugin p){
		super(p);
	}
	
	@Override
	public Object[] getParams(UfoReport container) {
		
		JFreeChart chart = getSelectedChart();
		if(chart == null){
			UfoPublic.showErrorDialog(getPlugin().getReport(), StringResource.getStringResource("miufo00314"), "");
			return null;
		}
		
		ResourceBundle localizationResources = ResourceBundle.getBundle("org.jfree.chart.LocalizationBundle");
			
		ChartPropertyEditPanel panel = new ChartPropertyEditPanel(chart);
		if (JOptionPane.showConfirmDialog(getPlugin().getReport(), panel, 
				  localizationResources.getString("Chart_Properties"),
				  JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
			panel.updateChartProperties(chart);
			
			ChartModel model = (ChartModel)getSelectedChartModel();
			ChartPropertiesValues properties = new ChartPropertiesValues();
			properties.getProperties(model);
			model.setPropertiesXml(properties.toXmlString());
			
			ChartPlugin plugin = (ChartPlugin)getPlugin();
			CellPosition anchor = plugin.getAnchorCell();
			if (anchor == null) {
				return null;
			}

			Cell cell = plugin.getCellsModel().getCell(anchor.getRow(),
					anchor.getColumn());
			cell.setValue(model);
			//脏标记并没有改变，需要再设置一下下
			plugin.getCellsModel().setCell(anchor.getRow(), anchor.getColumn(), cell);
		}
		return null;
	}

	@Override
	protected String getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @i18n iufobi00060=图表属性设置
	 */
	@Override
	protected String getName() {
		return StringResource.getStringResource("iufobi00060");
	}

}
 