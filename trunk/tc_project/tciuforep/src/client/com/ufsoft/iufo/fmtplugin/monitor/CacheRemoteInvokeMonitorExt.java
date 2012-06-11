package com.ufsoft.iufo.fmtplugin.monitor;

import com.ufsoft.report.UfoReport;
import com.ufsoft.iufo.resource.StringResource;

public class CacheRemoteInvokeMonitorExt  extends MonitorExt {
 
	@Override
	public Object[] getParams(UfoReport container) {
		new CacheRemoteInvokeMonitorDlg(container).show();
		return null;
	}

	/**
	 * @i18n miufo00535=»º´æÔ¶³Ìµ÷ÓÃ¼à¿Ø
	 */
	@Override
	protected String getDesName() {
		return StringResource.getStringResource("miufo00535");
	}
	
}
