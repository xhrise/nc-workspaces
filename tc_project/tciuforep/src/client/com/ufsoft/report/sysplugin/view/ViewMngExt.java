package com.ufsoft.report.sysplugin.view;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToolBar;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.ufsoft.report.ReportStatusBar;
import com.ufsoft.report.ReportToolBar;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.menu.MenuUtil;
import com.ufsoft.report.menu.UFMenu;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
/**
 * 视图功能点及实现
 * @author guogang
 *
 */
public class ViewMngExt extends AbsActionExt {
	private UfoReport m_report;
	public ViewMngExt(UfoReport report) {
	        m_report = report;
	    }
	
	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				UfoReport report = (UfoReport) params[0];
				JToolBar[] toolBars = report.getToolBar();
				ReportToolBar toolBar = null;
				for (int i = 0; i < toolBars.length; i++) {
					if (toolBars[i] instanceof ReportToolBar) {
						toolBar = (ReportToolBar) toolBars[i];
						if (toolBar.isView()) {
							MenuUtil.buildCompView(new String[] {
									MultiLang.getString("view"),
									MultiLang.getString("toolBar") }, 0,
									report, toolBar);
						}
					}
				}

			}

		};
	}

	public Object[] getParams(UfoReport container) {
		return new Object[] { container };
	}

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setPaths(new String[]{MultiLang.getString("view")});
		uiDes.setDirectory(true);
		uiDes.setCheckBoxMenuItem(true);
		uiDes.setName(MultiLang.getString("toolBar"));
		uiDes.setTooltip(MultiLang.getString("toolBar"));		
		return new ActionUIDes[] { uiDes };
	}
	@Override
	public void initListenerByComp(Component stateChangeComp) {
		ReportStatusBar statusBar=m_report.getStatusBar();
		if (statusBar != null && statusBar.isView()) {
			statusBar.setName(MultiLang.getString("statusBar"));
			MenuUtil.buildCompView(new String[] { 
					MultiLang.getString("view") }, 0, m_report, statusBar);
		}
		
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

}
