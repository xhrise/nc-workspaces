package com.ufsoft.iufo.inputplugin.biz;

import com.ufsoft.iufo.inputplugin.biz.data.TotalSourceLinkExt;
import com.ufsoft.iufo.inputplugin.ufobiz.UfoExcelExpPlugin;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;

public class TotalSourcePlugDes extends AbstractPlugDes {
    public TotalSourcePlugDes(IPlugIn plugin) {
        super(plugin);
    }
    
	protected IExtension[] createExtensions() {
        //联查个别报表数据
        IExtension totalSourceLinkExt = new TotalSourceLinkExt(getReport());
        
        return new IExtension[]{
                totalSourceLinkExt
        };
	}

}
