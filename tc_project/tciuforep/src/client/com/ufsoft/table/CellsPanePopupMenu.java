package com.ufsoft.table;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseEvent;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.menu.UFMenu;
import com.ufsoft.report.menu.UFMenuItem;
import com.ufsoft.report.menu.UFPopMenuItem;
import com.ufsoft.report.menu.UFPopupMenu;
import com.ufsoft.table.format.TableConstant;
 

/**
 * 表格面板右键
 * @author liuyy
 *@deprecated
 */
public class CellsPanePopupMenu extends UFPopupMenu {//implements Serializable { 
	 
	private static final long serialVersionUID = 72965970264797164L;
// extends MouseAdapter {
 
	
//	private JPopupMenu m_popup = new UFPopupMenu();

	CellsPanePopupMenu() {

	}
	
//
//	public void mousePressed(MouseEvent e) {
//		//		m_report.setFocusComp(e.getComponent());
//	//	show(e);
//	}
//
//	public void mouseReleased(MouseEvent e) {
//		 show(e);
//	}
    
	void show(MouseEvent e) {
//		if (e.isPopupTrigger()) {
			AppDebug.debug("popup is showing...");
			
			show(e.getComponent(), e.getX(), e.getY());
//		}
	}
	
	public void show(Component invoker, int x, int y) {
		resetPopupMenu(this, invoker);
		super.show(invoker, x, y);
	}
 
	/**
	 * 根据当前焦点组件确定加载哪些菜单。 
	 * void
	 */
	private static void resetPopupMenu(Container c, Component aimComp) {
		if (c instanceof UFMenuItem) {
			c.setEnabled(((UFMenuItem) c).isEnabled(aimComp));
		}
		if (c instanceof UFPopMenuItem) {
			UFPopMenuItem ppmi = (UFPopMenuItem) c;
			if (StateUtil.isToolBar(aimComp)) {
				c
						.setVisible((ppmi).isEnabled(aimComp)
								&& (ppmi).getAimComp() != TableConstant.UNDEFINED
								&& ((ppmi).getAimComp() & TableConstant.TOOLBAR) == TableConstant.TOOLBAR);
			} else if (StateUtil.isStatusBar(aimComp)) {
				c
						.setVisible((ppmi).isEnabled(aimComp)
								&& (ppmi).getAimComp() != TableConstant.UNDEFINED
								&& ((ppmi).getAimComp() & TableConstant.STATUSBAR) == TableConstant.STATUSBAR);
			} else if (StateUtil.isCPane1THeader(null, aimComp)) {
				c
						.setVisible((ppmi).isEnabled(aimComp)
								&& ((ppmi).getAimComp() == TableConstant.UNDEFINED || ((ppmi)
										.getAimComp() & TableConstant.CELLSPANE) == TableConstant.CELLSPANE));
			}
		}
		Component[] childs = c.getComponents();
		for (Component child : childs) {
			if (child instanceof Container) {
				resetPopupMenu((Container) child, aimComp);
			}
		}
		if (c instanceof UFMenu) {
			Component[] subMenus = c.getComponents();
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
//
//	public JPopupMenu getPopupMenu() {
//		return m_popup;
//	}
}
