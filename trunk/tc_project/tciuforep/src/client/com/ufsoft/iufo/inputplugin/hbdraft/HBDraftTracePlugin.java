package com.ufsoft.iufo.inputplugin.hbdraft;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;


public class HBDraftTracePlugin extends AbstractPlugIn{

	@Override
	protected IPluginDescriptor createDescriptor() {
        return new AbstractPlugDes(this){

            protected IExtension[] createExtensions() {
        		ICommandExt hbDraftTraceExt = new HBDraftTraceExt(getReport());//¥Ú”°
                return new IExtension[]{hbDraftTraceExt};
            }
            
        };
    }

}
