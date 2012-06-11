package com.ufsoft.report.sysplugin.viewmanager;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComponent;

import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.ICompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;

/**
 * 
 * @author wangyga
 *
 */
public abstract class AbstractViewMngAction extends AbstractPluginAction{

	abstract protected String getName();
	
	abstract protected ICompentFactory createCompFactory();
	
	protected ItemListener createItemListener(final JComponent src){
		return new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					src.setVisible(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					src.setVisible(false);
				}						
			}
			
		};
	}
	
	@Override
	public void execute(ActionEvent e) {
		
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor desc = new PluginActionDescriptor(getName());
		desc.setExtensionPoints(XPOINT.MENU);
		desc.setCompentFactory(createCompFactory());
		return desc;
	}

}
