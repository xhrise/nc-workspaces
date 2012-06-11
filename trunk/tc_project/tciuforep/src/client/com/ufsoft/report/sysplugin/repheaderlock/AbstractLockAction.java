package com.ufsoft.report.sysplugin.repheaderlock;

import com.ufida.zior.plugin.ICompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.AbstractRepPluginAction;
/**
 * 
 * @author wangyga
 * @created at 2009-9-3,ÏÂÎç06:05:40
 *
 */
public abstract class AbstractLockAction extends AbstractRepPluginAction{

    protected abstract String getName();
    
    protected abstract String getImagePath();
    
    protected abstract ICompentFactory getComponentFactory();
    
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor desc = new PluginActionDescriptor(getName());
		desc.setExtensionPoints(XPOINT.MENU,XPOINT.POPUPMENU);
		desc.setIcon(getImagePath());
		desc.setCompentFactory(getComponentFactory());
		return desc;
	}

}
