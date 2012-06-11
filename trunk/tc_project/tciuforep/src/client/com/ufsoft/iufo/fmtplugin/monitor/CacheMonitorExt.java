package com.ufsoft.iufo.fmtplugin.monitor;

import com.ufsoft.report.UfoReport;
import com.ufsoft.iufo.resource.StringResource;

class CacheMonitorExt extends MonitorExt {
 
	@Override
	public Object[] getParams(UfoReport container) {
		new CacheMonitorDlg(container).show();
		return null;
	}

	/**
	 * @i18n miufo00834=»º´æÄÚÈÝ¼à¿Ø
	 */
	@Override
	protected String getDesName() {
		return StringResource.getStringResource("miufo00834");
	}
	
	
 
}
 