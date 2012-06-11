package com.ufsoft.report.sysplugin.view;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
/**
 * 
 * @author wangyga
 * @created at 2009-2-24,обнГ08:44:14
 *
 */
public class ViewMngPlugin extends AbstractPlugIn{

	@Override
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this){

            protected IExtension[] createExtensions() {  
           	
            	return new IExtension[]{new ViewMngExt(getReport())};
            }
		};
	}

}
