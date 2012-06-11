package com.ufsoft.report.sysplugin.cellpostil;

import java.awt.event.ActionEvent;

import com.ufida.zior.plugin.ICompentFactory;
import com.ufida.zior.plugin.IPlugin;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufsoft.report.AbstractRepPluginAction;

public abstract class AbstractPostilAction extends AbstractRepPluginAction{

	private CellPostilDefPlugin plugin = null;
	
    public AbstractPostilAction(IPlugin p){
    	plugin = (CellPostilDefPlugin)p;
    }
    
    abstract protected String getName();
    
    abstract protected void doAction();
    
	protected ICompentFactory getComponentFactory(){
    	return null;
    };
    
	@Override
	public void execute(ActionEvent e) {
		if(getCellsModel() == null){
			return;
		}
		doAction();
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor desc = new PluginActionDescriptor(getName());
		desc.setCompentFactory(getComponentFactory());
		
		return desc;
	}

	protected CellPostilManager getPostilManager(){
		return plugin.getPostilManager();
	}
	
}
