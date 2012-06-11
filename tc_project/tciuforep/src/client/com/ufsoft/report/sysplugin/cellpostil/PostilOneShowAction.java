package com.ufsoft.report.sysplugin.cellpostil;

import com.ufida.zior.plugin.IPlugin;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
/**
 * �Ե�ǰѡ�е���ע������ʾ���ƵĲ��
 */
public class PostilOneShowAction extends AbstractPostilAction{

	public PostilOneShowAction(IPlugin p) {
		super(p);
	}

	@Override
	protected void doAction() {
		CellPosition cellPos = getPostilManager().getSelectedLastCell(getCellsModel());
		getPostilManager().showPostil(getCellsPane(),cellPos,false);
	}
	
	@Override
	public boolean isEnabled() {
		return getPostilManager().isShowEnable(getCellsPane());
	}

	/*
	 * miufo1004051 : ��ע
	 */
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor desc = (PluginActionDescriptor)super.getPluginActionDescriptor();
		desc.setExtensionPoints(XPOINT.MENU,XPOINT.POPUPMENU);
		desc.setGroupPaths(new String[]{MultiLang.getString("format"),MultiLang.getString("miufo1004051"),CellPostilDefPlugin.GROUP});
		return desc;
	}
	
	@Override
	protected String getName() {
		return MultiLang.getString("uiuforep0001116");//��ʾ��ע
	}

}
