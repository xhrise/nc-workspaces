package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.inputplugin.biz.FormulaTracePlugIn;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.menu.MenuUtil;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.IArea;
import com.ufsoft.table.SeperateLockSet;
import com.ufsoft.table.TableStyle;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.header.HeaderModel;

/**
 * 客户端的公式追踪业务工具类
 * @author chxw
 */
public class FormulaTraceBizUtil {
	/**
	 * @i18n uiuforep00123=公式追踪
	 */
	private static String ID_FORMULATRACE = MultiLang.getString("uiuforep00123");
	public static String doGetStrFormulaTrace(){
		// TODO to add multi language resoure ids
        return MultiLangInput.getString(ID_FORMULATRACE);//"公式追踪";		
	}

	public static String doGetStrData(){
        return MultiLang.getString("data");//数据	
	}
	
	public static void setMenuSelected(String strMenuName,String[] strParantMenuNames,UfoReport ufoReport){
		JCheckBoxMenuItem menuItem = getMenuItem(strMenuName, strParantMenuNames, ufoReport);
		if(menuItem != null){
			menuItem.setSelected(true);					
		}
	}

	public static void setMenuSelectState(String strMenuName,String[] strParantMenuNames,UfoReport ufoReport,boolean bSeleted){
		JCheckBoxMenuItem menuItem = getMenuItem(strMenuName, strParantMenuNames, ufoReport);
		if(menuItem != null){
			menuItem.setSelected(bSeleted);					
		}
	}
	
	public static void highLightFormulaTracedPos(IArea[] curTracedPos,UfoReport ufoReport){
		FormulaTraceNavPanel panel = getFormulaTraceNavPanel(ufoReport); 
		if(panel!=null){
		   panel.setCurTracedPos(curTracedPos);
		}
	}
	
	public static FormulaTraceNavPanel getFormulaTraceNavPanel(UfoReport ufoReport){
		if(ufoReport == null){
			return null;
		}
		FormulaTraceNavPanel panel=null;
		FormulaTracePlugIn plugIn = (FormulaTracePlugIn) ufoReport.getPluginManager().getPlugin(
				FormulaTracePlugIn.class.getName());
		if (plugIn != null) {
			IExtension[] exts = plugIn.getDescriptor().getExtensions();
			panel = (FormulaTraceNavPanel) ((FormulaTraceNavExt) exts[1])
					.getFormulaTraceNavPanel();
		}
		return panel;		
	}
	
	public static boolean isMenuSelected(String strMenuName,String[] strParantMenuNames,UfoReport ufoReport){
		JCheckBoxMenuItem menuItem = getMenuItem(strMenuName, strParantMenuNames, ufoReport);
		if(menuItem != null){
			if(menuItem.isSelected()){
				return true;
			}
		}
		return false;		
	}
	
	public static JCheckBoxMenuItem getMenuItem(String strMenuName,String[] strParantMenuNames,UfoReport ufoReport){		
		JComponent com=MenuUtil.getCompByPath(strParantMenuNames, 0, ufoReport.getJMenuBar(), ufoReport);
		JCheckBoxMenuItem windowMenu=null;
		if(com instanceof JMenu){
			//先保证创建了‘面板管理’子菜单
			if(((JMenu)com).getItemCount()<1){
				ActionListener[] listeners=((JMenu)com).getActionListeners();
				ActionEvent event = null;
				for(int i=0;i<listeners.length;i++){
					event=new ActionEvent(com,ActionEvent.ACTION_PERFORMED,"");
					listeners[i].actionPerformed(event);
				}
				}
			JPopupMenu menu=((JMenu)com).getPopupMenu();		
		for(int i=0;i<menu.getComponentCount();i++){
			if(strMenuName.equals(menu.getComponent(i).getName())&&menu.getComponent(i) instanceof JCheckBoxMenuItem)
				windowMenu=(JCheckBoxMenuItem)menu.getComponent(i);
			}
		}
		return windowMenu;
	}
	
	/**
	 * 将定位区域自动显示在可视位置
	 * @param ufoReport
	 * @param firstArea
	 */
	public static void setView2HighlightArea(UFOTable ufoTable, IArea firstArea,boolean isMoveView) {
		if(ufoTable == null || firstArea == null){
			return;
		}
		if(isMoveView){
			if (ufoTable.getCellEditor()==null || ufoTable.getCellEditor().stopCellEditing())
				ufoTable.getCells().moveViewToDisplayRect(firstArea, true);
		}
		repaintHighlightArea(ufoTable,firstArea);
		
	}	
	/**
	 * 公式追踪专用的绘制高亮区域的方法，区分冻结的绘制和普通绘制
	 * @param ufoReport
	 * @param firstArea
	 */
	public static void repaintHighlightArea(final UFOTable ufoTable, final IArea firstArea){
		//如果是本表的追踪信息，要强制重绘
		Runnable doWorkRunnable = new Runnable() { 
		    public void run() { 
		CellsPane cellsPane=ufoTable.getCells();
		    SeperateLockSet lockset=cellsPane.getDataModel().getSeperateLockSet();
		if(lockset.isFreezing()&&lockset.isFrozenNoSplit()){
			Rectangle rect=cellsPane.getCellRect(firstArea,true);
//			ufoReport.getTable().repaint(rect);
			JViewport view=null;
			Rectangle rectView=null;
			Rectangle paintRect=null;
			Point p=null;
			int crossHeady=0; 
			int crossHeadx=0; 
			HeaderModel colModel = cellsPane.getDataModel().getColumnHeaderModel();
			HeaderModel rowModel = cellsPane.getDataModel().getRowHeaderModel();
			Graphics ComponentG=ufoTable.getGraphics();
			if(ComponentG==null)
				return;
			Graphics g1=cellsPane.getComponentGraphics(ComponentG);
			g1.translate(TableStyle.ROW_HEADER_WIDTH, -TableStyle.COL_HEADER_HEIGTH);
		//	绘制左上		
		if (ufoTable.getMainView() != null) {
				view = ufoTable.getMainView();
				p = view.getViewPosition();
				crossHeady = 0;
				crossHeadx = 0;
				rectView = view.getViewRect();
				paintRect = rect.intersection(rectView);
				if(paintRect.width>0&&paintRect.height>0){
				g1.translate(crossHeadx - p.x, crossHeady - p.y + 36);
				g1.setClip(paintRect);
				cellsPane.getUI().update(g1,ufoTable.getCells());
				g1.translate(-(crossHeadx - p.x), -(crossHeady - p.y + 36));
				}
			}
		//绘制右上
			if (ufoTable.getRightView() != null) {
				view = ufoTable.getRightView();
				p = view.getViewPosition();
				crossHeady = 0;
				crossHeadx = colModel.getPosition(lockset.getSeperateCol());
				rectView = view.getViewRect();
				paintRect = rect.intersection(rectView);
				if(paintRect.width>0&&paintRect.height>0){
				g1.translate(crossHeadx - p.x, crossHeady - p.y + 36);
				g1.setClip(paintRect);
				cellsPane.getUI().update(g1,cellsPane);
				g1.translate(-(crossHeadx - p.x), -(crossHeady - p.y + 36));
				}
			}
			//绘制左下
			if (ufoTable.getDownView() != null) {
				view = ufoTable.getDownView();
				p = view.getViewPosition();
				crossHeady = rowModel.getPosition(lockset.getSeperateRow());
				crossHeadx = 0;
				rectView = view.getViewRect();
				paintRect = rect.intersection(rectView);
				if(paintRect.width>0&&paintRect.height>0){
				g1.translate(crossHeadx - p.x, crossHeady - p.y + 36);
				g1.setClip(paintRect);
				cellsPane.getUI().update(g1,cellsPane);
				g1.translate(-(crossHeadx - p.x), -(crossHeady - p.y + 36));
				}
			}
			//绘制右下
			if (ufoTable.getRightDownView() != null) {
				view = ufoTable.getRightDownView();
				p = view.getViewPosition();
				crossHeady = rowModel.getPosition(lockset.getSeperateRow());
				crossHeadx = colModel.getPosition(lockset.getSeperateCol());
				rectView = view.getViewRect();
				paintRect = rect.intersection(rectView);
                if(paintRect.width>0&&paintRect.height>0){
				g1.translate(crossHeadx - p.x, crossHeady - p.y + 36);
				g1.setClip(paintRect);
				cellsPane.getUI().update(g1,cellsPane);
				g1.translate(-(crossHeadx - p.x), -(crossHeady - p.y + 36));
                }
			}
				g1.translate(-TableStyle.ROW_HEADER_WIDTH, TableStyle.COL_HEADER_HEIGTH);
				}else{
				   cellsPane.repaint(firstArea, true);
				}
		    } 
		}; 
		
		try{
			SwingUtilities.invokeLater(doWorkRunnable);
		}catch(Exception e){
			AppDebug.debug(e);
		}
		
	}
	
}
 