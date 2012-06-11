package com.ufida.report.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.demo.SampleXYDataset;
import org.jfree.chart.plot.PlotOrientation;

import com.sun.codemodel.JLabel;
import com.ufsoft.iufo.resource.StringResource;

public class ChartZoomDialog extends UIDialog {
 
	private static final long serialVersionUID = 911747080429025410L;

	private ChartPanel chartPanel = null;
	private JCheckBox xzoom;
	private JCheckBox yzoom;
	
	/**
	 * @i18n miufo00141=图表缩放浏览
	 * @i18n miufo00142=水平缩放
	 * @i18n miufo00147=垂直缩放
	 * @i18n miufo00154=   (缩放只对数值类型的坐标轴有效)
	 */
	ChartZoomDialog(Container p, JFreeChart chart){
		super(p);
		setResizable(true);
		setPreferredSize(new java.awt.Dimension(800, 600));
		setTitle(StringResource.getStringResource("miufo00141"));
		chartPanel = new ChartPanel(chart);
		chartPanel.setHorizontalZoom(true);
		chartPanel.setVerticalZoom(true);
		chartPanel.setHorizontalAxisTrace(true);
		chartPanel.setVerticalAxisTrace(true);
		chartPanel.setFillZoomRectangle(true);
		chartPanel.setPreferredSize(new java.awt.Dimension(650, 570));

		JPanel main = new JPanel(new BorderLayout());
		JPanel checkpanel = new JPanel();
		xzoom = new JCheckBox(StringResource.getStringResource("miufo00142"));
		xzoom.setSelected(true);
		yzoom = new JCheckBox(StringResource.getStringResource("miufo00147"));
		yzoom.setSelected(true);
		CheckListener clisten = new CheckListener();
		xzoom.addItemListener(clisten);
		yzoom.addItemListener(clisten);
		checkpanel.add(xzoom);
		checkpanel.add(yzoom);
		UILabel hint = new UILabel(StringResource.getStringResource("miufo00154"));
		hint.setForeground(Color.gray);
		checkpanel.add(hint);
		main.add(checkpanel, BorderLayout.SOUTH);
		main.add(chartPanel);
		setContentPane(main);
		
	}
	
	class CheckListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			Object source = e.getItemSelectable();
			if (source == xzoom) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					chartPanel.setHorizontalZoom(false);
					chartPanel.setHorizontalAxisTrace(false);
					chartPanel.repaint();
				}
				else {
					chartPanel.setHorizontalZoom(true);
					chartPanel.setHorizontalAxisTrace(true);
				}
			}
			else if (source == yzoom) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					chartPanel.setVerticalZoom(false);
					chartPanel.setVerticalAxisTrace(false);
					chartPanel.repaint();
				}
				else {
					chartPanel.setVerticalZoom(true);
					chartPanel.setVerticalAxisTrace(true);
				}
			}
	   }
	}
}
 