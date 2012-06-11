package com.ufsoft.report.sysplugin.repname;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class RepNamePlugin extends AbstractPlugIn {

	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(RepNamePlugin.this){

			protected IExtension[] createExtensions() {
				return new IExtension[]{
						new RepNameExt(getReport())
						};
			}			
		};
	}

}
