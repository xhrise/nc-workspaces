package com.ufsoft.report.sysplugin.postil;

import java.awt.Component;

import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;

/**
 * ±à¼­Åú×¢
 * @author zhaopq
 * @created at 2009-1-8,ÏÂÎç06:24:42
 *
 */
public class PostilEditExt extends AbsActionExt {

	private PostilPlugin plugin;

	public PostilEditExt(IPlugIn plugin) {
		super();
		this.plugin = (PostilPlugin)plugin;
	}

	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				plugin.getHelper().editPostil((CellPosition) params[0]);
			}
		};
	}

	public Object[] getParams(UfoReport container) {
		CellPosition cellPos = plugin.getHelper().getSelectedLastCell();
		return new Object[] { cellPos };
	}

	public boolean isEnabled(Component focusComp) {
		return StateUtil.isFormat_CPane(plugin.getReport(), focusComp)
				&& plugin.getHelper().isEditeEnable();
	}
	

	public ActionUIDes[] getUIDesArr() {
		
		ActionUIDes editDes = new ActionUIDes();
		editDes.setName(MultiLang.getString("uiuforep0001101"));// ±à¼­Åú×¢
		editDes.setPaths(new String[] { MultiLang.getString("format") });
		editDes.setGroup(PostilPlugin.EXT_MENU_GROUP);

		ActionUIDes editDes2 = new ActionUIDes();
		editDes2.setName(MultiLang.getString("uiuforep0001101"));// ±à¼­Åú×¢

		editDes2.setPopup(true);
		editDes2.setGroup(PostilPlugin.EXT_MENU_GROUP);

		return new ActionUIDes[] { editDes, editDes2 };
	}
}
