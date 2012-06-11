package com.ufsoft.iufo.inputplugin.biz;

import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class TotalSourcePlugin extends AbstractPlugIn {

	protected IPluginDescriptor createDescriptor() {
		return new TotalSourcePlugDes(this); 
	}

}
