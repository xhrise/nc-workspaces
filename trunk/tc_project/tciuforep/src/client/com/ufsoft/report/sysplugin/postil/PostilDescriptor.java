package com.ufsoft.report.sysplugin.postil;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;

/** 
 * @author zzl Create on 2005-3-7
 */
public class PostilDescriptor extends AbstractPlugDes {

    public PostilDescriptor(IPlugIn plugin) {
        super(plugin);
    }

    public String getName() {
        return null;
    }

    public String getNote() {
        return null;
    }

    public String[] getPluginPrerequisites() {
        return null;
    }

    public IExtension[] createExtensions() {
        return new ICommandExt[]{
        		new PostilInsertExt(getPlugin())
        		,new PostilEditExt(getPlugin())
        		,new PostilDelExt(getPlugin())
        		,new PostilControlExt(getPlugin())
        		,new PostilOneShowExt(getPlugin())
        		,new PostilOneHideExt(getPlugin())
        };
    }

    public String getHelpNode() {
        return null;
    }

}
