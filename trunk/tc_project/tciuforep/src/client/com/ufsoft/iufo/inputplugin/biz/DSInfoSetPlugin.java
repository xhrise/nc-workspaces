package com.ufsoft.iufo.inputplugin.biz;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class DSInfoSetPlugin extends AbstractPlugIn {

	@Override
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this) {
			@Override
			protected IExtension[] createExtensions() {
				return new ICommandExt[] { new DSInfoSetExt(getReport()) };

			}
		};
	}

}
