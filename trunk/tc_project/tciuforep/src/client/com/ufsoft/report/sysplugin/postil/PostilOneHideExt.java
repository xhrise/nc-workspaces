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
 * �Ե�ǰѡ�е���ע�������ؿ��ƵĲ��
 * 
 * @author zhaopq
 * @Date��2008-12-30
 */
public class PostilOneHideExt extends AbsActionExt {

	private PostilPlugin plugin;

	public PostilOneHideExt(IPlugIn plugin) {
		super();
		this.plugin = (PostilPlugin) plugin;
	}

	@Override
	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				plugin.getHelper().hidePostil((CellPosition) params[0]);
			}
		};
	}

	@Override
	public boolean isEnabled(Component focusComp) {
		return plugin.getHelper().isHideEnable();
	}

	@Override
	public Object[] getParams(UfoReport container) {
		CellPosition cellPos = plugin.getHelper().getSelectedLastCell();
		return new Object[] { cellPos };
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uides = new ActionUIDes();
		uides.setName(MultiLang.getString("uiuforep0001115"));// ������ע
		uides.setPopup(true);
		uides.setGroup(PostilPlugin.EXT_MENU_GROUP);

		return new ActionUIDes[] { uides };
	}

}
