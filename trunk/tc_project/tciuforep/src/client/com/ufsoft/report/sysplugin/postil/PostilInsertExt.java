package com.ufsoft.report.sysplugin.postil;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.EventObject;

import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;

/**
 * 插入批注
 * 
 * @author zhaopq Create on 2008-12-30
 */
public class PostilInsertExt extends AbsActionExt {

	private PostilPlugin plugin;

	public PostilInsertExt(IPlugIn plugin) {
		super();
		this.plugin = (PostilPlugin) plugin;

		initPostilMouseAdapter();
	}

	@Override
	public void initListenerByComp(Component stateChangeComp) {
		plugin.getReport().getTable().addUserActionListener(
				new UserActionListner() {

					public void userActionPerformed(UserUIEvent e) {
						if (e.getEventType() == UserUIEvent.FILL
								|| e.getEventType() == UserUIEvent.INSERTCELL
								|| e.getEventType() == UserUIEvent.DELETECELL) {// 发生了单元格移动的事件时，需要重汇标注

							plugin.getHelper().reShowAllPostils();
						}
					}

					public String isSupport(int source, EventObject e)
							throws ForbidedOprException {
						return null;
					}

				});
	}

	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				CellPosition cellPos = (CellPosition) params[0];
				PostilInternalFrame iFrame = plugin.getHelper().insertPostil(
						cellPos);
				iFrame.setVisible(true);
				iFrame.repaint();
				iFrame.revalidate();

				iFrame.getTextArea().requestFocus();
			}
		};
	}

	public Object[] getParams(UfoReport container) {
		CellPosition cellPos = plugin.getReport().getCellsModel()
				.getSelectModel().getAnchorCell();
		return new Object[] { cellPos };
	}

	public boolean isEnabled(Component focusComp) {
		return StateUtil.isFormat_CPane(plugin.getReport(), focusComp)
				&& plugin.getHelper().isInsertEnable();
	}

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes editDes = new ActionUIDes();
		editDes.setName(MultiLang.getString("uiuforep0001113"));// 插入批注
		editDes.setPaths(new String[] { MultiLang.getString("format") });
		editDes.setImageFile("reportcore/add_postil.png");
		editDes.setGroup(PostilPlugin.EXT_MENU_GROUP);

		ActionUIDes editDes2 = new ActionUIDes();
		editDes2.setName(MultiLang.getString("uiuforep0001113"));// 插入批注
		editDes2.setPopup(true);
		editDes2.setImageFile("reportcore/add_postil.png");
		editDes2.setGroup(PostilPlugin.EXT_MENU_GROUP);

		return new ActionUIDes[] { editDes, editDes2 };
	}


	private void initPostilMouseAdapter() {
		if (plugin!=null && plugin.getReport()!=null && plugin.getReport().getTable()!=null 
				&& plugin.getReport().getTable().getMainView() != null) {
			CellsPane pane = (CellsPane) plugin.getReport().getTable()
					.getMainView().getView();
			if (!isPostilMouseAdapter(pane)) {
				pane.addMouseListener(new PostilMouseAdapter(pane));
			}
		}
	}

	private class PostilMouseAdapter extends MouseAdapter {
		private CellsPane pane;

		private CellPosition lastShowCellPos;

		PostilMouseAdapter(CellsPane cellspane) {
			this.pane = cellspane;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Point p = e.getPoint();
			int row = pane.rowAtPoint(p);
			int col = pane.columnAtPoint(p);
			CellPosition cellPos = CellPosition.getInstance(row, col);
			if (plugin.getHelper().hasPostilOnCell(cellPos)
					&&isInCtrlArea(row, col, p)) {
				if (!plugin.getHelper().isCellPostilShow(cellPos)) {
					plugin.getHelper().showPostil(cellPos, true);
					lastShowCellPos = cellPos;
				}
			} else if (lastShowCellPos != null
					&& !plugin.getHelper().shouldBeShown(lastShowCellPos)) {
				plugin.getHelper().hidePostil(lastShowCellPos);
				lastShowCellPos = null;
			}
		}
		private boolean isInCtrlArea(int row, int col, Point p) {
			Rectangle rect = pane.getCellRect(CellPosition.getInstance(
					row, col), true);
			int x = p.x - rect.x;
			int y = p.y - rect.y;
			if (x < rect.width * 11 / 12 || y > rect.height / 5) {
				return false;
			}
			return true;
		}
	}

	private boolean isPostilMouseAdapter(CellsPane pane) {
		boolean isHas = false;
		MouseMotionListener[] listeners = pane.getMouseMotionListeners();
		if (listeners != null) {
			for (int i = 0; i < listeners.length; i++) {
				if (listeners[i] instanceof PostilMouseAdapter) {
					isHas = true;
					break;
				}
			}
		}
		return isHas;
	}
}
