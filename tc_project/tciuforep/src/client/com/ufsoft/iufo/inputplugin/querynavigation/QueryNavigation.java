package com.ufsoft.iufo.inputplugin.querynavigation;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.util.Vector;

import javax.swing.JFrame;

import netscape.javascript.JSObject;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.ReportQueryFrame;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceBizUtil;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceNavPanel;
import com.ufsoft.iuforeport.UfoQueryNavApplet;
import com.ufsoft.iuforeport.tableinput.applet.TableInputApplet;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.MultiLang;

/**
 * 公式追踪界面导航控制。
 * @author liuyy
 * @version 5.02
 */
public class QueryNavigation {
 
	private Vector<QueryNaviMenu> menus = new Vector<QueryNaviMenu>();
	
	private QueryNaviMenu curWindow = null;
	
	
	private static QueryNavigation singleton = null;
	
	public synchronized static QueryNavigation getSingleton(){
		if(singleton == null){
			singleton = new QueryNavigation();
		}
		
		return singleton;
	}


	/**
	 * 查询报表显示
	 * @param newReport
	 * @param title
	 * @param newWindow
	 */
	public static void showReport(UfoReport newReport, String title,
			boolean newWindow,boolean needSave) {
		
		if(title == null || title.length() < 1){
			throw new IllegalArgumentException(" title is null.");
		}
	
		//		UfoReport newReport = getNewReport(container);
		if (newWindow) {
			ReportQueryFrame frame = new ReportQueryFrame(newReport,needSave);
			 
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	
			QueryNaviMenu menu = new QueryNaviMenu(frame);
			frame.setMenu(menu);
			menu.add(newReport, title);
			getSingleton().addMenu(menu);
			frame.setVisible(true);
			
			menu.toFront();
			
		} else {
			QueryNaviMenu menu = getSingleton().getCurWindow();
			menu.add(newReport, title);
			
			changeReport(menu.getWindow(), newReport);
			 
		}
		
		refreshMenu(newReport);
		
		//		dialog.setModal(false);
		//		dialog.setTitle(getTitle());
		//		dialog.setRootPane(newReport);
		//		dialog.setLocation(0, 0);
		//		dialog.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		//		dialog.setVisible(true);
	
	}
	
	/**
	 * 报表显示
	 * @param newReport
	 * @param title
	 * @param type  报表类型
	 */
	public static void showReport(UfoReport newReport, String title,
			int type) {
		
		QueryNaviMenu[] menus = QueryNavigation.getSingleton().getMenus();
		
		int size = menus.length;
		QueryNaviMenu menu = null;
		for (int i = 0; i < size; i++) {
			menu = menus[i];
			if(menu != null && type == menu.getType()){
				
				menu.add(newReport, title);
				changeReport(menu.getWindow(), newReport);
				refreshMenu(newReport);
				menu.toFront();
				
				return;
			}
		}
		
		showReport(newReport, title, true,false);
		
	}
	
	
	/**
	 * 同一窗口切换查询报表
	 * @param container
	 * @param newReport
	 */
	public static void changeReport(Container container, UfoReport newReport){
		//切换的时候先取消原来的追踪信息
		FormulaTraceNavPanel panel=FormulaTraceBizUtil.getFormulaTraceNavPanel(newReport);
		if(panel!=null){
			panel.clear();
		}
		if(container instanceof TableInputApplet){
			((TableInputApplet) container).setUfoReport(newReport);
			
		} else {
			((ReportQueryFrame) container).changeReport(newReport);
			
		}
		
		newReport.updateUI();
		
	}
	
	/**
	 * 重新生成综合查询菜单
	 * @param report
	 */
	public static void refreshMenu(UfoReport report){
//		IPlugIn plug = report.getPluginManager().getPlugin(QueryNavigationPlugin.class.getName());
//		if(plug == null){
//			throw new IllegalArgumentException("QueryNavigationPlugin need loaded.");
//		}
//		((QueryNavigationPlugin) plug).refreshMenu(report);
		
		if(report == null){
			return;
		}
		
		report.refreshPlugIn(QueryNavigationPlugin.class.getName());
		report.refreshPlugIn(QueryNextPlugin.class.getName());
		
		report.getReportMenuBar().updateUI();
		report.getToolBarPane().updateUI();
		
	}
	
	/**
	 * @i18n uiuforep00005=导航信息，
	 * @i18n uiuforep00006=打开报表数
	 */
	public void focusMenu(QueryNaviMenu menu){
		AppDebug.debug(MultiLang.getString("uiuforep00005") + menu.getWindow().getClass().getName() + MultiLang.getString("uiuforep00006") + menu.getItemNum());
		setCurWindow(menu);
		QueryNaviItem curItem = menu.getCurItem();
		if(curItem != null){
			QueryNavigation.refreshMenu(curItem.getReport());
		}
	}
	
	public boolean isCurWindow(QueryNaviMenu menu){
		return menu == this.curWindow;
	}
	
	/**
	 * 定位查询报表。
	 * 如果该查询报表已在某窗口打开，将该报表焦点显示。
	 * @param repCode 报表编码
	 * @return  UfoReport，如果该报表未打开过，返回null
	 */
	public UfoReport focusReport(String repCode, String strAlondID, String strTaskPK){
		if(repCode == null){
			return null;
		}
		int size = menus.size();
		QueryNaviMenu menu = null;
		QueryNaviItem item = null;
		for (int i = 0; i < size; i++) {
			menu = menus.elementAt(i);
			QueryNaviItem[] items = menu.getItems();
			for (int j = 0; j < items.length; j++) {
				item = items[j];
				if(item.equalsItem(repCode, strAlondID, strTaskPK)){
					if(menu.getCurItem() != item){
						menu.setCurIndex(j);
						QueryNavigation.changeReport(menu.getWindow(), item.getReport());
					}
					menu.toFront();
					return item.getReport();
				}
			}
		}
		return null;
		
	}
	

///**
// * 清空综合查询菜单项 
// */
//	public void clear(){
//		menus.clear();
//	}
	
	/**
	 * 记录查询弹出窗口
	 * @param report
	 */
	public void addMenu(QueryNaviMenu menu){ 
		menus.add(menu);
		curWindow = menu;
	}
	
	/**
	 * 删除菜单项
	 * @param menu
	 * @i18n uiuforep00007=删除导航菜单：
	 */
	public void removeMenu(QueryNaviMenu menu){
		AppDebug.debug(MultiLang.getString("uiuforep00007") + menu);
		menus.remove(menu);
	}
	
	/**
	 * 校验各导航窗口状态。
	 * 
	 * 处理开启多applet，applet页面关闭等事情造成的导航窗口信息不同步。
	 * 
	 */
	public void validateMenus(){
            		      
		for (int i = 0; i < menus.size(); i++) {
			QueryNaviMenu menu = menus.elementAt(i);
			try {
				Container container = menu.getWindow();
				if(container instanceof Applet){
					JSObject jsWin = JSObject.getWindow((Applet) container);
					if(jsWin == null){
						throw new IllegalArgumentException();
					}
				} 
				else if(container instanceof JFrame){
					JFrame frame = (JFrame) container;
//					 
//					AppDebug.debug("frame's active is " + frame.isActive());
//					AppDebug.debug("frame's Valid is " + frame.isValid());
//					AppDebug.debug("frame's Displayable is " + frame.isDisplayable());
//					AppDebug.debug("frame's Focusable is " + frame.isFocusable());
//					 
					if(!frame.isFocusable() || !frame.isDisplayable()){
						throw new IllegalArgumentException();
					}
					
					//UfoPublic.showErrorDialog(container, "切换窗口出错。" + ". frame's Focusable is " + frame.isFocusable() + ". frame's Displayable is " + frame.isDisplayable(), MultiLang.getString("error"));
 
				} 
				
			} catch(Throwable e){
				removeMenu(menu);
				i--;
			}
		}
		
	  
	}
	
//	/**
//	 * 记录menu窗口新增report
//	 * @param menu
//	 * @param report
//	 */
//	public void addMenuItem(QueryNaviMenu menu, UfoReport report){
//		if(menu == null){
//			menu = getCurWindow();
//		}
//		if(menu == null){
//			throw new IllegalArgumentException("menu is null.");
//		}
//		QueryNaviItem item = new QueryNaviItem(report);
//		menu.add(item);
////		hashMenu.put(report, menu);
//		
//		
//	}
	
	public void delMenu(QueryNaviMenu menu){
		menus.remove(menu);
	}
	
	protected QueryNaviMenu[] getMenus(){
		
//		ArrayList<UIMenuItem> uimenus = new ArrayList<UIMenuItem>();
//		for(int i = 0; i < menus.size(); i++){
//			QueryNaviMenu menu = menus.elementAt(i);			
//			UIMenuItem menuitem = new UIMenuItem(menu.getMenuLabel());
//			uimenus.add(menuitem);
//		}
//		return uimenus.toArray(new UIMenuItem[0]);
		
		return menus.toArray(new QueryNaviMenu[0]);
		
	}

	public QueryNaviMenu getCurWindow() {
		return curWindow;
	}
	
	
	
	public QueryNaviMenu getCurWindow(Component ui){
      Component root = ui.getParent();
      while(root != null){
      	root = root.getParent();
      	if(root instanceof ReportQueryFrame){
      		return ((ReportQueryFrame) root).getMenu();
      	} else if(root instanceof TableInputApplet){
      		return ((UfoQueryNavApplet) root).getMenu();
      	}
      }
      
      return null;
      
	}

	public void setCurWindow(QueryNaviMenu curWindow) {
		this.curWindow = curWindow;
	}

 
	
	
}
 