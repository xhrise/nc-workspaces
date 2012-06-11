package com.ufsoft.iufo.inputplugin.querynavigation;

import java.awt.Component;

import javax.swing.AbstractButton;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.undo.AbsUndoExt;

public class QueryNextExt extends AbsActionExt  {

	
	private boolean next = true;
	
	private UfoReport report = null;

	public QueryNextExt(boolean doNext,UfoReport report) {
		super(); 
		this.next = doNext;
		this.report = report;
		 
	}
	
	
	public boolean isEnabled(Component focusComp) {

		QueryNaviMenu menu = QueryNavigation.getSingleton().getCurWindow();
		 
		if(menu == null){
			return false;
		}
		
		if(isNext() && menu.isNavLast()){
				return false;
				
		} else if(!isNext() && menu.isNavFirst()){
				return false;
		}

		return true;
	}
	
	
	@Override
	public UfoCommand getCommand() {
		return new QueryNextCmd(report);
	}

	@Override
	public Object[] getParams(UfoReport container) { 
		 
		return new Object[]{new Boolean(next)};
		
	}

	/**
	 * @i18n uiuforep00134=下一个查询报表
	 * @i18n uiuforep00135=上一个查询报表
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();

//		QueryNaviMenu menu = QueryNavigation.getSingleton().getCurWindow();
		 
		String label = MultiLang.getString("uiuforep00134");//menu.getMenuLabel();
		if(!isNext()){
			label = MultiLang.getString("uiuforep00135");
//			if(menu == null || menu.isNavFirst()){
//				uiDes.setImageFile("reportcore/previous_gray.gif");
//			} else {
				uiDes.setImageFile("reportcore/previous.gif");
				
//			}
		} else {
//			if(menu == null || menu.isNavFirst()){
//				uiDes.setImageFile("reportcore/next_gray.gif");
//			} else {
				uiDes.setImageFile("reportcore/next.gif");
//			}
		}
		uiDes.setName(label);
		uiDes.setTooltip(label);
		uiDes.setGroup(MultiLang.getString("file"));
		uiDes.setToolBar(true);
		
		return new ActionUIDes[] { uiDes };
	}
//	
//	public ActionUIDes[] getUIDesArr() {
//        ActionUIDes uiDes = new ActionUIDes();
//        uiDes.setName(MultiLang.getString("窗口"));
//        uiDes.setPaths(new String[]{MultiLang.getString("edit"),});
//         
//        ActionUIDes uiDes2 = (ActionUIDes) uiDes.clone();
//        uiDes2.setPaths(new String[]{});
//        uiDes2.setPopup(true);
//        return new ActionUIDes[]{uiDes,uiDes2}; 
//	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

}
 