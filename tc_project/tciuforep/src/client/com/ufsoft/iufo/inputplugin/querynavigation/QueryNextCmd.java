package com.ufsoft.iufo.inputplugin.querynavigation;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

public class QueryNextCmd  extends UfoCommand {

	private UfoReport report = null;
	
	public QueryNextCmd(UfoReport report){
		this.report = report;
	}
	
	/**
	 * @i18n uiuforep00064=已是最后一项。
	 * @i18n uiuforep00065=已是第一项。
	 * @i18n uiuforep00066=无相关项。
	 */
	@Override
	public void execute(Object[] params) {

		boolean next = ((Boolean) params[0]).booleanValue();
		
		QueryNaviItem item = null;
//		report.getPluginManager().getPlugin(QueryNavigationPlugin.class.getName());
		
		QueryNaviMenu menu = QueryNavigation.getSingleton().getCurWindow();
		
		if(menu != null){
			
			if(next){
				if(menu.isNavLast()){
					UfoPublic.showErrorDialog(report, MultiLang.getString("uiuforep00064"), MultiLang.getString("error"));
					return;
				}
				item = menu.next();
				AppDebug.debug("Query next.");
			} else {
				if(menu.isNavFirst()){
					UfoPublic.showErrorDialog(report, MultiLang.getString("uiuforep00065"), MultiLang.getString("error"));
					return;
				}
				item = menu.previous();
				AppDebug.debug("Query previous.");
			}
		}
		
		
		if(menu == null || item == null){
			UfoPublic.showErrorDialog(report, MultiLang.getString("uiuforep00066"), MultiLang.getString("error"));//MultiLang.getString("miufo00001")
			return;
		}
		 
		QueryNavigation.changeReport(menu.getWindow(), item.getReport());
		
		this.report.refershPluginState(QueryNextPlugin.class.getName());
		
//		Container frame = menu.getWindow();
//		frame.remove(frame.getRootPane());
//		frame.add(item.getReport());
		
		
//		window.getWindow().remove(comp)
//		((ReportPopupDialog)  window.getWindow()). item.getReport();
		
	}

}
 