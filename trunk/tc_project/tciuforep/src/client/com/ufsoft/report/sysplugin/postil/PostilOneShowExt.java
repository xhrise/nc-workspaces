package com.ufsoft.report.sysplugin.postil;

import java.awt.Component;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;

/**
 * 对当前选中的批注进行显示控制的插件
 * 
 * @author zhaopq
 * @Date：2008-12-30
 */
public class PostilOneShowExt extends AbsActionExt {

	private PostilPlugin plugin;
	public PostilOneShowExt(IPlugIn plugin) {
		super();
		this.plugin = (PostilPlugin)plugin;
	}

	@Override
	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				CellPosition cellPos = (CellPosition) params[0];
				plugin.getHelper().showPostil(cellPos,false);
			}
		};
	}

	@Override
	public boolean isEnabled(Component focusComp) {
		return plugin.getHelper().isShowEnable();
	}

	@Override
	public Object[] getParams(UfoReport container) {
		CellPosition cellPos = plugin.getHelper().getSelectedLastCell();
		return new Object[] {cellPos };
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uides = new ActionUIDes();
		uides.setName(MultiLang.getString("uiuforep0001114"));// 显示批注
		uides.setPopup(true);
		uides.setGroup(PostilPlugin.EXT_MENU_GROUP);

		return new ActionUIDes[] { uides };
	}

}
