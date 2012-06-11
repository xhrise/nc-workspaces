package com.ufsoft.iufo.inputplugin.hbdraft;

import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class HBDraftPlugin extends AbstractPlugIn {

	@Override
	protected IPluginDescriptor createDescriptor() {
		// TODO Auto-generated method stub
		return new HBDraftDescriptor(this);
	}
	
}
