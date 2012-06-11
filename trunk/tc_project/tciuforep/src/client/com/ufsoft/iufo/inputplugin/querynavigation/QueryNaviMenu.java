package com.ufsoft.iufo.inputplugin.querynavigation;

import java.applet.Applet;
import java.awt.Container;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.JFrame;

import netscape.javascript.JSObject;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iuforeport.tableinput.applet.ReportViewType;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.MultiLang;


/**
 * 查询导航窗口菜单描述
 * 每个窗口一个QNMenu实例。
 * @author liuyy
 *
 */
public class QueryNaviMenu implements Serializable { 
 
	private static final long serialVersionUID = 3756517522569419063L;

	private Vector<QueryNaviItem> items = new Vector<QueryNaviItem>();

	private int index = 0;
	
	private Container window = null;
	
	//窗口类型（根据业务类型定义）
	private int type = -1;
	
	public QueryNaviMenu(Container window){
		this.window = window;
	}
	
	public QueryNaviMenu(Container window, int type){
		this.window = window;
		this.type = type;
	}
	
	public Container getWindow(){
		return window;
	}
	
	public boolean isNavFirst(){
		return index <= 0;
	}
	
	public boolean isNavLast(){
		return items.size() == 0 || index >= items.size() - 1;
	}
	
	public boolean hasItem(){
		return items.size() > 0;
	}
	
	public QueryNaviItem[] getItems(){
		return items.toArray(new QueryNaviItem[0]);
		
	}
	
	
	public int getItemNum(){
		return items.size();
	}
	
	/**
	 * 下一个
	 * @return
	 */
	public QueryNaviItem next(){
		if(index >= items.size() - 1){
			return null;
		}
		
		index = index + 1;
		
		return items.get(index);
				
	}
	
	/**
	 * 上一个
	 * @return
	 */
	public QueryNaviItem previous(){
		if(index <= 0 || index > items.size()){
			return null;
		}
		
		index = index - 1;
		
		return items.get(index);
				
	} 
	
//	/**
//	 * 清除所有报表项
//	 */
//	public void clear(){
//		items.clear();
//		index = 0;
//	}
	
	/**
	 * 添加追踪报表项
	 * @param report
	 * @param title
	 * @return
	 * @i18n uiuforep00070=导航调试，添加后的ItemNum：
	 */
	public QueryNaviItem add(UfoReport report, String title){
		
		if(title == null || title.length() < 1){
			title = ReportViewType.getViewTypeNote(getType());
		}
		QueryNaviItem item = new QueryNaviItem(report, title);

//		if(item.getRepCode() == null){  //无报表，，不允许添加
//			return null;
//		}
		
		while(items.size() - 1 > index){
			items.remove(index + 1);
		}
		
		 items.add(item);
		 index = items.size() - 1;
		 
		 AppDebug.debug(MultiLang.getString("uiuforep00070") + items.size());
		 
		return item;
	}
	 
	 
	protected void setCurIndex(int nIndex){
		if(nIndex < 0 || nIndex >= items.size()){
			throw new IllegalArgumentException();
		}
		index = nIndex;
	}
	
	public QueryNaviItem getCurItem(){
       
		if(items.size() < 1){
			return null;
		}
		
		if(index < 0 || index >= items.size()){
			index = 0;
		}
		
		return items.get(index);
	}
 
	
	public void toFront(){
      
		Container container = getWindow();
		if(container instanceof Applet){
			 try{
				JSObject jsWin = JSObject.getWindow((Applet) container);
				if(jsWin == null){
					QueryNavigation.getSingleton().removeMenu(this);
				}
				jsWin.eval("window.focus()");
			 } catch(Throwable e){
//				    AppDebug.debug(e);
//					UfoPublic.showErrorDialog(container, "切换窗口出错。" + e.getMessage(), MultiLang.getString("error"));//MultiLang.getString("miufo00001")
					return;
			 }
			 
		} else if(container instanceof JFrame){
			
			JFrame frame = (JFrame) container;
			frame.setTitle(getWindowTitle());
			frame.toFront();
			
		} else {
			throw new IllegalArgumentException();
		
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	private String getWindowTitle(){
		QueryNaviItem vo = getCurItem();
		if(vo == null){
			return "";		
		}
		
		return vo.getTitle();  
	}
 
	public String toString(){
		QueryNaviItem vo = getCurItem();
		if(vo == null){
			return "";		
		}
		
		return vo.getSimpleTitle();  
	}
	
 
}
 