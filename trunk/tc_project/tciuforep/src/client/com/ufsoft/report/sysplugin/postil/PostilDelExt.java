package com.ufsoft.report.sysplugin.postil;

import java.awt.Component;

import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.util.MultiLang;

/**
 * É¾³ýÅú×¢
 * 
 * @author zhaopq
 * @created at 2009-1-8,ÏÂÎç02:22:23
 * 
 */
public class PostilDelExt extends AbsActionExt {

	private PostilPlugin plugin;

	public PostilDelExt(IPlugIn plugin) {
		super();
		this.plugin = (PostilPlugin) plugin;
	}

	public UfoCommand getCommand() {

		return new UfoCommand() {

			@Override
			public void execute(Object[] params) {
				plugin.getHelper().deleteAllSelectedPostils();
			}

		};
	}

	public Object[] getParams(UfoReport container) {
		return null;
	}

	public boolean isEnabled(Component focusComp) {
		return StateUtil.isFormat_CPane(plugin.getReport(), focusComp)
				&& plugin.getHelper().isDeleteEnable();
	}

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setName(MultiLang.getString("uiuforep0001102"));// É¾³ýÅú×¢
		uiDes1.setPaths(new String[] { MultiLang.getString("format") });
		uiDes1.setGroup(PostilPlugin.EXT_MENU_GROUP);

		ActionUIDes uiDes2 = new ActionUIDes();
		uiDes2.setName(MultiLang.getString("uiuforep0001102"));// É¾³ýÅú×¢
		uiDes2.setPopup(true);
		uiDes2.setGroup(PostilPlugin.EXT_MENU_GROUP);

		return new ActionUIDes[] { uiDes1, uiDes2 };
	}

}
