package com.ufsoft.report;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.plaf.ComponentUI;

import com.ufida.zior.comp.KMenu;
import com.ufida.zior.comp.KMenuItem;
import com.ufida.zior.comp.KPopupMenu;
import com.ufida.zior.view.Viewer;
import com.ufsoft.report.exception.MessageException;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.SelectAllCmp;
import com.ufsoft.table.header.TableHeader;
/**
 * 
 * @author wangyga
 * @created at 2009-3-16,上午10:40:59
 *
 */
public class ReportDesignerUI extends ComponentUI{
//
//	private RightMouseHandler mouseHandler = null;
//	
//	private KPopupMenu popupMenu = null;
//		
//    private Viewer curentViewer = null;
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		if(c instanceof Viewer){			
//			curentViewer = ((Viewer)c);
//			popupMenu = curentViewer.getMainboard().getPopupMenu();
		} else {
			throw new MessageException("右键菜单没有初始化");
		}
//		addGlobalPopMenuSupport(c,createRightMouseListener());
	}
//	
//	private MouseListener createRightMouseListener(){
//		if(mouseHandler == null){
//			mouseHandler = new RightMouseHandler();
//		}
//		return mouseHandler;
//	}
////	
//	private void addGlobalPopMenuSupport(Container focusComp, MouseListener lis) {
//		if (focusComp instanceof CellsPane || focusComp instanceof TableHeader
//				|| focusComp instanceof SelectAllCmp) {
//
//			MouseListener[] listeners = focusComp.getMouseListeners();
//			for (MouseListener ls : listeners) {//子组件的监听优先父组件的执行,此处调整一下顺序
//				focusComp.removeMouseListener(ls);
//			}
//			focusComp.addMouseListener(lis);
//			for (MouseListener ls : listeners) {
//				focusComp.addMouseListener(ls);
//			}
//		}
//
//		Component[] coms = focusComp.getComponents();
//		for (Component comp : coms) {
//			if (comp instanceof Container) {
//				addGlobalPopMenuSupport((Container) comp, lis);
//			}
//		}
//	}
//	
	private void showPopup(MouseEvent e) {
//		if (e.isPopupTrigger()) {
//			resetPopupMenu(popupMenu, e.getComponent());
//			popupMenu.show(e.getComponent(), e.getX(), e.getY());
//		}
	}
	
	private class RightMouseHandler extends MouseAdapter{
//
//		@Override
//		public void mousePressed(MouseEvent e) {
//			showPopup(e);
//		}
//
//		@Override
//		public void mouseReleased(MouseEvent e) {
//			if(!(e.getComponent() instanceof JMenuItem)){				
//				curentViewer.getMainboard().getToolBarPanel().validateSubCompsEnabled();
//			}			
//			showPopup(e);
//		}
		
		
	}
	
	/**
	 * 根据当前焦点组件确定加载哪些菜单。 
	 * void
	 */
	private static void resetPopupMenu(Container c, Component aimComp) {
//		if (c instanceof UFMenuItem) {
//			c.setEnabled(((UFMenuItem) c).isEnabled(aimComp));
//		}
//		if (c instanceof UFPopMenuItem) {
//			if (StateUtil.isToolBar(aimComp)) {
//				c
//						.setVisible(((UFPopMenuItem) c).isEnabled(aimComp)
//								&& ((UFPopMenuItem) c).getAimComp() != TableConstant.UNDEFINED
//								&& (((UFPopMenuItem) c).getAimComp() & TableConstant.TOOLBAR) == TableConstant.TOOLBAR);
//			} else if (StateUtil.isStatusBar(aimComp)) {
//				c
//						.setVisible(((UFPopMenuItem) c).isEnabled(aimComp)
//								&& ((UFPopMenuItem) c).getAimComp() != TableConstant.UNDEFINED
//								&& (((UFPopMenuItem) c).getAimComp() & TableConstant.STATUSBAR) == TableConstant.STATUSBAR);
//			} else if (StateUtil.isCPane1THeader(null, aimComp)) {
//				c
//						.setVisible(((UFPopMenuItem) c).isEnabled(aimComp)
//								&& (((UFPopMenuItem) c).getAimComp() == TableConstant.UNDEFINED || (((UFPopMenuItem) c)
//										.getAimComp() & TableConstant.CELLSPANE) == TableConstant.CELLSPANE));
//			}
//		}
		
		if(c instanceof KMenuItem){
			boolean isEnabled = ((KMenuItem)c).getAction().isEnabled();
			if(isEnabled && !c.isEnabled()){
				c.setEnabled(isEnabled);
			}
			c.setVisible(((KMenuItem)c).getAction().isEnabled());
		}
		
		Component[] childs = c.getComponents();
		//处理当一组菜单项都不显示的时候，不得显示右键分栏。liuyy+
		boolean bGroupVisible = false;
		for (int i = 0; i < childs.length; i++) {
			if (!(childs[i] instanceof Container)) {
				continue;
			}
			
			Container subcomp = (Container) childs[i];
			
			resetPopupMenu(subcomp, aimComp);
			
			if (subcomp instanceof KMenuItem && subcomp.isVisible()) {
				bGroupVisible = true;
			}
			
			if(subcomp instanceof JSeparator){
				if(i == childs.length - 1){
					bGroupVisible = false;
				}
				subcomp.setVisible(bGroupVisible);
				bGroupVisible = false;
			}
			
		}
		
		if (c instanceof KMenu) {
			Component[] subMenus = ((KMenu)c).getMenuComponents();
			boolean isVisible = false;
			for (Component subMenu : subMenus) {
				if (subMenu.isVisible()) {
					isVisible = true;
					break;
				}
			}
			c.setVisible(isVisible);
		}
	}
	
}
