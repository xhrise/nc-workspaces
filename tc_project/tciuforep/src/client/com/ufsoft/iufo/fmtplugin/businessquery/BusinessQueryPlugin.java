package com.ufsoft.iufo.fmtplugin.businessquery;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class BusinessQueryPlugin extends AbstractPlugIn {

	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(null){
			protected IExtension[] createExtensions() {
				return new IExtension[]{new QueryCreateExt(),new QueryMngExt()};
			}
			
		};
	}

}
