package com.ufsoft.report.sysplugin.postil;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.menu.UFCheckBoxMenuItem;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.util.MultiLang;

/**
 * 对所有批注进行显示控制的插件，该插件放置在视图菜单组
 * @author zhaopq
 * @created at 2009-1-6,上午09:50:00
 *
 */
public class PostilControlExt extends AbsActionExt {

	private PostilPlugin plugin;

	public PostilControlExt(IPlugIn plugin) {
		this.plugin = (PostilPlugin)plugin;
	}

	@Override
	public UfoCommand getCommand() {
		return null;
	}

	@Override
	public Object[] getParams(UfoReport container) {
		return null;
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uides = new ActionUIDes();
		uides.setName(MultiLang.getString("uiuforep0001114"));// 显示批注
		uides.setPaths(new String[] { MultiLang.getString("view") });
		uides.setCheckBoxMenuItem(true);
		uides.setGroup(PostilPlugin.EXT_MENU_GROUP);

		return new ActionUIDes[] { uides }; 
	}

	@Override
	public void initListenerByComp(Component stateChangeComp) {
		((UFCheckBoxMenuItem) stateChangeComp)
				.addItemListener(new ItemListener() {

					public void itemStateChanged(ItemEvent e) {

						if (e.getStateChange() == ItemEvent.SELECTED) {
							plugin.getHelper().showAllPostils(true);
							plugin.getHelper().setShow(true);

						} else if (e.getStateChange() == ItemEvent.DESELECTED) {
							plugin.getHelper().hideAllPostils();
							plugin.getHelper().setShow(false);
						}

					}

				});
	}
}
