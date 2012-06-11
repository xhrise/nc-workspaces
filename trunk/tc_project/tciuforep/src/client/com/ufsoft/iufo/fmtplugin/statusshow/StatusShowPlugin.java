package com.ufsoft.iufo.fmtplugin.statusshow;

import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class StatusShowPlugin extends AbstractPlugIn {

	@Override
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this){
			@Override
			protected IExtension[] createExtensions() {
				return new IExtension[]{
						new DataSourceStatusExt((UfoContextVO)getReport().getContextVo()),
						new FormatRightStatusExt((UfoContextVO)getReport().getContextVo())};
			}			
		};
	}

}
