package com.ufsoft.report.sysplugin.location;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.plugin.IStatusBarExt;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.SelectModel;
import com.ufsoft.table.event.SelectEvent;

/**
 * 
 * @author zzl 2005-5-25
 */
public class LocationPlugin extends AbstractPlugIn {

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
     */
    public IPluginDescriptor createDescriptor() {
        return new AbstractPlugDes(this){

            protected IExtension[] createExtensions() {
        		ICommandExt extGoto = new GotoExt(getReport());//¶¨Î»
        		return new IExtension[]{extGoto};
            }

        };
    }
}
