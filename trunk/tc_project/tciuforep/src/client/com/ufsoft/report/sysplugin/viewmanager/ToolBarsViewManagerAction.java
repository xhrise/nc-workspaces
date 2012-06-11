package com.ufsoft.report.sysplugin.viewmanager;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import com.ufida.zior.comp.KCheckBoxMenuItem;
import com.ufida.zior.comp.KMenu;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.ICompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.util.UIUtilities;
import com.ufsoft.report.util.MultiLang;
/**
 * 
 * @author wangyga
 *
 */
public class ToolBarsViewManagerAction extends AbstractViewMngAction {

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor desc = (PluginActionDescriptor)super.getPluginActionDescriptor();;		
		desc.setGroupPaths(new String[] { MultiLang.getString("view"),
				MultiLang.getString("toolBar"), "view_manager" });
		return desc;
	}

	@Override
	protected ICompentFactory createCompFactory() {
		return new DefaultCompentFactory() {

			@Override
			protected JComponent createMenuItem(String[] paths,
					JComponent root, AbstractAction action) {
				JComponent parent = root;

				for (int i = 0; i < paths.length - 1; i++) {
					String name = paths[i];
					JComponent comp = (JComponent) UIUtilities
							.getComponentByName(parent, name);
					if (comp == null) {
						comp = new KMenu(name);

						if (!comp.isVisible()) {
							comp.setVisible(true);
						}
						parent.add(comp);
					} else{
						comp.add(new JPopupMenu.Separator());
					}

					parent = comp;
				}

				JToolBar[] toolBars = getMainboard().getToolBarPanel().getToolBars();
				
				if(toolBars == null || toolBars.length ==0){
					return null;
				}
				
				for(final JToolBar toolBar : toolBars){
					JMenuItem item = new KCheckBoxMenuItem(toolBar.getName());
					item.setSelected(true);
					item.addItemListener(createItemListener(toolBar));
					parent.add(item);
				}
				
				return parent;
			}

		};
	}

	@Override
	protected String getName() {
		return MultiLang.getString("toolBar");
	}
}
