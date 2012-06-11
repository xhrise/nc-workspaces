package com.ufsoft.report;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import com.ufsoft.report.menu.UFMenu;
import com.ufsoft.report.menu.UFMenuItem;
import com.ufsoft.report.menu.UFPopMenuItem;
import com.ufsoft.report.menu.UFPopupMenu;
import com.ufsoft.table.format.TableConstant;

public class PopupMenuListener extends MouseAdapter {
	private JPopupMenu m_popup = new UFPopupMenu();
	private UfoReport m_report;

	public PopupMenuListener(UfoReport report) {
		m_report = report;
	}

	public void mousePressed(MouseEvent e) {		
		maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		//modify by wangyga 2008-8-5 ֪ͨ��������ť�ı�״̬
		m_report.setFocusComp(e.getComponent());//����ɿ�ʱ��ȥ�жϣ���֤�ܻ����ȷ��ѡ������
		maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			resetPopupMenu(m_popup, e.getComponent());
			m_popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	/**
	 * ���ݵ�ǰ�������ȷ��������Щ�˵��� 
	 * void
	 */
	private static void resetPopupMenu(Container c, Component aimComp) {
		if (c instanceof UFMenuItem) {
			c.setEnabled(((UFMenuItem) c).isEnabled(aimComp));
		}
		if (c instanceof UFPopMenuItem) {
			if (StateUtil.isToolBar(aimComp)) {
				c
						.setVisible(((UFPopMenuItem) c).isEnabled(aimComp)
								&& ((UFPopMenuItem) c).getAimComp() != TableConstant.UNDEFINED
								&& (((UFPopMenuItem) c).getAimComp() & TableConstant.TOOLBAR) == TableConstant.TOOLBAR);
			} else if (StateUtil.isStatusBar(aimComp)) {
				c
						.setVisible(((UFPopMenuItem) c).isEnabled(aimComp)
								&& ((UFPopMenuItem) c).getAimComp() != TableConstant.UNDEFINED
								&& (((UFPopMenuItem) c).getAimComp() & TableConstant.STATUSBAR) == TableConstant.STATUSBAR);
			} else if (StateUtil.isCPane1THeader(null, aimComp)) {
				c
						.setVisible(((UFPopMenuItem) c).isEnabled(aimComp)
								&& (((UFPopMenuItem) c).getAimComp() == TableConstant.UNDEFINED || (((UFPopMenuItem) c)
										.getAimComp() & TableConstant.CELLSPANE) == TableConstant.CELLSPANE));
			}
		}
		
		Component[] childs = c.getComponents();
		//����һ��˵������ʾ��ʱ�򣬲�����ʾ�Ҽ�������liuyy+
		boolean bGroupVisible = false;
		for (int i = 0; i < childs.length; i++) {
			if (!(childs[i] instanceof Container)) {
				continue;
			}
			
			Container subcomp = (Container) childs[i];
			
			resetPopupMenu(subcomp, aimComp);
			
			if (subcomp instanceof UFMenuItem && subcomp.isVisible()) {
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

	public JPopupMenu getPopupMenu() {
		return m_popup;
	}

}
