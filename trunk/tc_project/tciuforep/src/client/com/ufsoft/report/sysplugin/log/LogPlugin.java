package com.ufsoft.report.sysplugin.log;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

/**
 * 
 * @author zzl 2005-5-25
 */
public class LogPlugin extends AbstractPlugIn {

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
     */
    public IPluginDescriptor createDescriptor() {
        return new AbstractPlugDes(this){

            protected IExtension[] createExtensions() {
        		ICommandExt extLog = new LogExt(getReport());
                return new IExtension[]{extLog};
            }
            
        };
    }
    public LogWindow getLogWindow(){
    	return ((LogExt)getDescriptor().getExtensions()[0]).getLogWindow();
    }
}
