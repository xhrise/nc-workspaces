package com.ufsoft.report.sysplugin.viewmanager;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

import com.ufida.zior.comp.KCheckBoxMenuItem;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.ICompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufsoft.report.util.MultiLang;

public class StatusViewMngAction extends AbstractViewMngAction{
	
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
        PluginActionDescriptor desc = (PluginActionDescriptor)super.getPluginActionDescriptor();
        desc.setGroupPaths(new String[]{MultiLang.getString("view"),getName()});
		return desc;
	}

	@Override
	protected ICompentFactory createCompFactory() {
		return new DefaultCompentFactory(){

			@Override
			protected JComponent createMenuItem(String strGroup,
					AbstractAction action) {
				KCheckBoxMenuItem item = new KCheckBoxMenuItem(getName());
				item.setGroup(strGroup);
				item.setSelected(true);
				item.addItemListener(createItemListener(getMainboard().getStatusBar()));
				return item;
			}
			
		};
	}

	@Override
	protected String getName() {		
		return "×´Ì¬À¸";
	}

}
