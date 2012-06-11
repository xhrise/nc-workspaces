package com.ufsoft.report.sysplugin.cellpostil;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.EventObject;

import com.ufida.zior.exception.ForbidedOprException;
import com.ufida.zior.plugin.IPlugin;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.sysplugin.postil.PostilInternalFrame;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.event.MouseListenerAdapter;
import com.ufsoft.table.event.MouseEventAdapter;
/**
 * 插入批注
 */
public class PostilInsertAction extends AbstractPostilAction{

	public PostilInsertAction(IPlugin p) {
		super(p);
		p.getEventManager().addListener(createListener());
	}

	@Override
	protected void doAction() {
		CellPosition cellPos = getCellsModel().getSelectModel().getAnchorCell();
				
		PostilInternalFrame iFrame = getPostilManager().insertPostil(getCellsPane(),
				cellPos);
		iFrame.setVisible(true);
		iFrame.repaint();
		iFrame.revalidate();

		iFrame.getTextArea().requestFocus();
	}
	
	@Override
	public boolean isEnabled() {
		return getPostilManager().isInsertEnable(getCellsModel());
	}
	/*
	 * miufo1004051 : 批注
	 */
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
        PluginActionDescriptor desc = (PluginActionDescriptor)super.getPluginActionDescriptor();
        desc.setExtensionPoints(XPOINT.MENU,XPOINT.POPUPMENU);
        desc.setGroupPaths(new String[]{MultiLang.getString("format"),MultiLang.getString("miufo1004051"),CellPostilDefPlugin.GROUP});
        desc.setIcon("images/reportcore/add_postil.png");
		return desc;
	}

	@Override
	protected String getName() {
		return MultiLang.getString("uiuforep0001113");
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
