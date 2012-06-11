package com.ufsoft.report.sysplugin.findreplace;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class FindReplacePlugin extends AbstractPlugIn {

	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this){
			protected IExtension[] createExtensions() {
				return new IExtension[]{new FindExt(),new ReplaceExt(getReport())};
			}
			
		};
	}

}
