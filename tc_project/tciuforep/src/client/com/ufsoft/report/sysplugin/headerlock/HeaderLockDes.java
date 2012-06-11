package com.ufsoft.report.sysplugin.headerlock;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;

/**
 * 
 * @author zzl 2005-5-23
 */
public class HeaderLockDes extends AbstractPlugDes {    

    /**
     * @param plugin
     */
    public HeaderLockDes(IPlugIn plugin) {
        super(plugin);
    }

    /*
     * @see com.ufsoft.report.plugin.AbstractPlugDes#creatExtensions()
     */
    protected IExtension[] createExtensions() {
        return new IExtension[]{new AreaSeparateExt(getReport()),
        		new AreaLockExt(getReport())};
    }

}
