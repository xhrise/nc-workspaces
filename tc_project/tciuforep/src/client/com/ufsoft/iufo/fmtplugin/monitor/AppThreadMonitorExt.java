package com.ufsoft.iufo.fmtplugin.monitor;

import com.ufsoft.report.UfoReport;
import com.ufsoft.iufo.resource.StringResource;

class AppThreadMonitorExt extends MonitorExt {
 
	@Override
	public Object[] getParams(UfoReport container) {
		new AppThreadMonitorDlg(container).show();
		return null;
	}

	/**
	 * @i18n miufo00070=Ïß³Ì¼à¿Ø
	 */
	@Override
	protected String getDesName() {
		return StringResource.getStringResource("miufo00070");
	}
	
	

}
 