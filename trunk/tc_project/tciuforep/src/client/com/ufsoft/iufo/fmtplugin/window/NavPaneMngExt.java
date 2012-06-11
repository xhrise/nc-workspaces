/**
 * 
 */
package com.ufsoft.iufo.fmtplugin.window;

import java.awt.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import nc.ui.pub.beans.UICheckBoxMenuItem;

import com.ufsoft.report.ReportNavPanel;
import com.ufsoft.report.UICloseableTabbedPane;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.menu.MenuUtil;
import com.ufsoft.report.menu.UFMenu;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.INavigationExt;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.util.MultiLang;

/**
 * 各导航面版管理功能:是否显示导航面版中的各tab
 * @author guogang
 *
 */
public class NavPaneMngExt extends AbsActionExt {
	private UfoReport m_report;
	public NavPaneMngExt(UfoReport report) {
	        m_report = report;
	    }
	/* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
	 */
	@Override
	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				UfoReport report = (UfoReport) params[0];

				JComponent parent = MenuUtil.getCompByPath(new String[] {
						MultiLang.getString("view"),
						MultiLang.getString("panelManager") }, 0, report
						.getJMenuBar(), report);
				IPlugIn[] plugins = report.getPluginManager().getAllPlugin();

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

								if (parent instanceof JMenu && iExt != null) {
									createNavPanelMenuItem(report,
											(JMenu) parent, iExt);
								}

							}
						}
					}
				}
			}
		};
		
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
	 */
	@Override
	public Object[] getParams(UfoReport container) {
		return new Object[] { container };
	}
    
	/**
	 * 增加额外的事件处理：当首次选择“面板管理”菜单的时候构建视图
	 */
	public void initListenerByComp(Component stateChangeComp) {
		if(stateChangeComp instanceof UFMenu){
			final UFMenu menu=(UFMenu)stateChangeComp;	
			menu.addMenuListener(new MenuListener(){

				public void menuCanceled(MenuEvent event) {
				}

				public void menuDeselected(MenuEvent event) {
				}

				public void menuSelected(MenuEvent e) {
					if(menu.getItemCount()<1){
					ActionListener[] listeners=menu.getActionListeners();
					ActionEvent event = null;
					for(int i=0;i<listeners.length;i++){
						event=new ActionEvent(e.getSource(),ActionEvent.ACTION_PERFORMED,"");
						listeners[i].actionPerformed(event);
					}
					}
				}
				
			});
		}
		
	}
	/**
	 * 创建窗口中的面版视图管理
	 * @param report 视图所在的报表模型 
	 * @param parentMenu  视图选择和取消选择的菜单项的父菜单
	 * @param iExt 与视图关联的panel插件
	 * add by guogang 2007-10-11
	 */
	private void createNavPanelMenuItem(final UfoReport report, JMenu parentMenu,
			final INavigationExt iExt) {
		
		String tabName = iExt.getName();
		final UICheckBoxMenuItem item = new UICheckBoxMenuItem(tabName);
		item.setName(tabName);
		
		parentMenu.add(item);
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
				}else if (e.getStateChange() == ItemEvent.DESELECTED) {

						if (tabs.indexOfTab(iExt.getName()) >= 0) {
							tabs.removeTabAt(iExt.getName());

						}
					}
				}
			
		});
		
		item.setSelected(iExt.isShow()&&tabs.isVisible());
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setPaths(new String[]{MultiLang.getString("view")});
		uiDes.setDirectory(true);
		uiDes.setCheckBoxMenuItem(true);
		uiDes.setTooltip(MultiLang.getString("panelManager"));	
		uiDes.setName(MultiLang.getString("panelManager"));
		
		return new ActionUIDes[] { uiDes};
	}

}
