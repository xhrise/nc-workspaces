/*
 * Created on 2005-7-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.chart.applet;

import nc.ui.pub.beans.UISplitPane;
/**
 * @author caijie
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DimChartReportPanel extends UISplitPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DimChartPanel dimChartPanel = null;
	SelectChartPartPanel selectChartPartPanel = null;
	/**
	 * This is the default constructor
	 */
	public DimChartReportPanel(DimChartPanel chartPanel) {
		super();
		this.dimChartPanel = chartPanel;
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		this.setLeftComponent(getSelectChartPartPanel());
		this.setRightComponent(dimChartPanel);
		this.setSize(300,200);
	}
	
	
	private SelectChartPartPanel getSelectChartPartPanel() {
	    if (selectChartPartPanel == null) {
	        selectChartPartPanel = new SelectChartPartPanel();
		}
		return selectChartPartPanel;
	}
 }
