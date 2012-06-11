package com.ufsoft.report.sysplugin.cellpostil;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

import com.ufida.zior.comp.KCheckBoxMenuItem;
import com.ufida.zior.exception.ForbidedOprException;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.ICompentFactory;
import com.ufida.zior.plugin.IPlugin;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.event.MouseListenerAdapter;
import com.ufsoft.table.event.MouseEventAdapter;
/**
 * 对所有批注进行显示控制的插件，该插件放置在视图菜单组
 */
public class PostilControlAction extends AbstractPostilAction{

	public PostilControlAction(IPlugin p) {
		super(p);
		p.getEventManager().addListener(createListener());
	}

	@Override
	protected void doAction() {		
	}
	
	@Override
	protected ICompentFactory getComponentFactory() {		
		return new DefaultCompentFactory(){

			@Override
			protected JComponent createMenuItem(String strGroup,
					AbstractAction action) {
				KCheckBoxMenuItem comp = new KCheckBoxMenuItem();
				comp.setGroup(strGroup);
				comp.setAction(action);
				comp.setText(getName());
				comp.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {

						if (e.getStateChange() == ItemEvent.SELECTED) {
							getPostilManager().showAllPostils(true);
							getPostilManager().setStatusShow(true);

						} else if (e.getStateChange() == ItemEvent.DESELECTED) {
							getPostilManager().hideAllPostils();
							getPostilManager().setStatusShow(false);
						}

					}

				});
				return comp;
			}			
		};
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor desc = (PluginActionDescriptor)super.getPluginActionDescriptor();
		desc.setExtensionPoints(XPOINT.MENU);
		desc.setGroupPaths(new String[]{MultiLang.getString("view"),CellPostilDefPlugin.GROUP});
		return desc;
	}

	@Override
	protected String getName() {
		return MultiLang.getString("uiuforep0001114");
	}
	
	private ListenerHandler createListener(){
		return new ListenerHandler();
	}
	
	private class ListenerHandler extends MouseListenerAdapter implements UserActionListner{

		private CellPosition lastShowCellPos;
		
		@Override
		public void mouseClicked(MouseEventAdapter e) {
			Point p = e.getPoint();
			CellsPane cellsPane = getCellsPane();
			CellPostilManager helper = getPostilManager();
			int row = cellsPane.rowAtPoint(p);
			int col = cellsPane.columnAtPoint(p);
			CellPosition cellPos = CellPosition.getInstance(row, col);
			if (helper.hasPostilOnCell(getCellsModel(),cellPos)
					&&isInCtrlArea(row, col, p)) {
				if (!helper.isCellPostilShow(getCellsPane(),cellPos)) {
					helper.showPostil(getCellsPane(),cellPos, true);
					lastShowCellPos = cellPos;
				}
			} else if (lastShowCellPos != null
					&& !helper.shouldBeShown(getCellsModel(),lastShowCellPos)) {
				helper.hidePostil(getCellsModel(),lastShowCellPos);
				lastShowCellPos = null;
			}
		}
		
		private boolean isInCtrlArea(int row, int col, Point p) {
			Rectangle rect = getCellsPane().getCellRect(CellPosition.getInstance(
					row, col), true);
			int x = p.x - rect.x;
			int y = p.y - rect.y;
			if (x < rect.width * 11 / 12 || y > rect.height / 5) {
				return false;
			}
			return true;
		}
		
		public void userActionPerformed(UserUIEvent e) {
			if (e.getEventType() == UserUIEvent.FILL
					|| e.getEventType() == UserUIEvent.INSERTCELL
					|| e.getEventType() == UserUIEvent.DELETECELL) {// 发生了单元格移动的事件时，需要重汇标注

				getPostilManager().reShowAllPostils();
			}
			
		}

		public String isSupport(int source, EventObject e)
				throws ForbidedOprException {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
