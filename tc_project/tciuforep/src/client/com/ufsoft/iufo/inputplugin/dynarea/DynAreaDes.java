package com.ufsoft.iufo.inputplugin.dynarea;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;

/**
 * 
 * @author zzl 2005-6-26
 */
public class DynAreaDes extends AbstractPlugDes {

    /**
     * @param plugin
     */
    public DynAreaDes(IPlugIn plugin) {
        super(plugin);
    }

    /*
     * @see com.ufsoft.report.plugin.AbstractPlugDes#createExtensions()
     */
    protected IExtension[] createExtensions() {
//        return new IExtension[]{new ExtendDynAreaExt((DynAreaPlugin)getPlugin())};
        return new IExtension[]{
            new InsertOneRowAbove((DynAreaInputPlugin)getPlugin()),
            new InsertOneRowBelow((DynAreaInputPlugin)getPlugin()),
            new InsertMultiRowAbove((DynAreaInputPlugin)getPlugin()),
            new InsertMultiRowBelow((DynAreaInputPlugin)getPlugin()),
            new DeleteOneRow((DynAreaInputPlugin)getPlugin()),
            new DeleteMultiRow((DynAreaInputPlugin)getPlugin())
        };
    }

}
