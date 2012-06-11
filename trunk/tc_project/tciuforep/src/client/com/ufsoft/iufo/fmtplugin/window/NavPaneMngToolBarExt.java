package com.ufsoft.iufo.fmtplugin.window;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import nc.ui.pub.beans.UICheckBoxMenuItem;

import com.ufsoft.report.UICloseableTabbedPane;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.menu.UFMenuButton;
import com.ufsoft.report.menu.UFPopupMenu;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.INavigationExt;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.toolbar.dropdown.JPopupPanelButton;
import com.ufsoft.report.util.MultiLang;

public class NavPaneMngToolBarExt extends AbsActionExt {

	private UfoReport m_report;
	public NavPaneMngToolBarExt(UfoReport report) {
	        m_report = report;
	    }
	@Override
	public UfoCommand getCommand() {
		return null;
	}
	
    private void buildPopupMenu(JPopupMenu popuMenu){

		IPlugIn[] plugins = m_report.getPluginManager().getAllPlugin();

		IPlugIn plugin;
		IPluginDescriptor des;
		IExtension[] exts;
		IExtension ext;

		for (int i = 0; i < plugins.length; i++) {
			plugin = plugins[i];
			des = plugin.getDescriptor();
			exts = des.getExtensions();
			if (exts != null) {
				for (int j = 0; j < exts.length; j++) {
					ext = exts[j];
					if (ext != null && ext instanceof INavigationExt) {
						INavigationExt iExt = (INavigationExt) ext;

						if (iExt != null) {
							createNavPanelMenuItem(m_report, iExt,popuMenu);
						}

					}
				}
			}
		}
	
    }
    
	private void createNavPanelMenuItem(final UfoReport report,
			final INavigationExt iExt,JPopupMenu popuMenu) {

		String tabName = iExt.getName();
		final UICheckBoxMenuItem item = new UICheckBoxMenuItem(tabName);
		item.setName(tabName);
		popuMenu.add(item);
		final UICloseableTabbedPane tabs = report.getReportNavPanel().getPanelById(iExt.getNavPanelPos());
		
		tabs.addCloseListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (item.getName().equals(e.getActionCommand())&&item.isSelected())
					item.setSelected(false);
			}

		});
		tabs.addShowListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if (item.getName().equals(e.getActionCommand())&&!item.isSelected()){
					item.setSelected(true);
				}
				
			}
			
		});
       
		item.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {
					
					if (tabs.indexOfTab(iExt.getName()) < 0) {
						tabs.add(iExt.getName(), iExt.getPanel());

					}
					if(!tabs.isVisible()){
						tabs.setVisible(true);
					}
					if(!iExt.getPanel().isVisible()){
						iExt.getPanel().setVisible(true);
					}
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {

					if (tabs.indexOfTab(iExt.getName()) >= 0) {
						tabs.removeTabAt(iExt.getName());

					}
				}
			}

		});
		
		item.setSelected(iExt.isShow()&&tabs.isVisible());
	}

	@Override
	public Object[] getParams(UfoReport container) {
		return new Object[] { container };
	}

	public void initListenerByComp(Component stateChangeComp) {
		if (stateChangeComp instanceof UFMenuButton) {
			final UFMenuButton menu = (UFMenuButton) stateChangeComp;
			menu.getPopupMenu().addPopupMenuListener(new PopupMenuListener() {

				public void popupMenuCanceled(PopupMenuEvent e) {
					// TODO Auto-generated method stub

				}

				public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
					// TODO Auto-generated method stub

				}

				public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
					if (menu.getPopupMenu().getComponentCount() < 1) {
						buildPopupMenu(menu.getPopupMenu());
					}

				}

			});
			
		}

	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes3 = new ActionUIDes();
		uiDes3.setToolBar(true);
		uiDes3.setGroup("view");
		uiDes3.setDirectory(true);
		uiDes3.setPaths(new String[] {});
		uiDes3.setName(MultiLang.getString("panelManager"));
		uiDes3.setTooltip(MultiLang.getString("panelManager"));
		uiDes3.setImageFile("reportcore/navpanelMng.gif");
		return new ActionUIDes[] { uiDes3 };
	}

}
