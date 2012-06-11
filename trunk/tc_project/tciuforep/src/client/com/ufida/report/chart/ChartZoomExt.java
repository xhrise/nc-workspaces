package com.ufida.report.chart;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

public class ChartZoomExt extends AbstractChartExt {

	ChartZoomExt(ChartPlugin p) {
		super(p);
	}

	@Override
	public UfoCommand getCommand() {
		return null;
	}

	@Override
	public Object[] getParams(UfoReport container) {
		((ChartPlugin)getPlugin()).showZoomDlg();
		return null;
	}

	@Override
	protected String getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getName() {
		return StringResource.getStringResource("miufo00141");
	}


}
 