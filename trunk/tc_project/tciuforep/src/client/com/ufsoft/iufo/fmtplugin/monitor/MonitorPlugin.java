package com.ufsoft.iufo.fmtplugin.monitor;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class MonitorPlugin extends AbstractPlugIn {

	@Override
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this) {

			protected IExtension[] createExtensions() {
				return new IExtension[] { 
						new AppThreadMonitorExt(),
						new CacheMonitorExt(),
						new CacheRemoteInvokeMonitorExt() };
			}

		};
	}

}
