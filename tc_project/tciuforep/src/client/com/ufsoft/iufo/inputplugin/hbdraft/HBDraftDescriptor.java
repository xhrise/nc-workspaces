package com.ufsoft.iufo.inputplugin.hbdraft;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.util.MultiLang;

public class HBDraftDescriptor extends AbstractPlugDes{

	public HBDraftDescriptor(HBDraftPlugin plugin) {
		super(plugin);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @i18n uiuforep00114=合并报表工作底稿
	 */
	public String getName() {
		return MultiLang.getString("uiuforep00114");
	}
	@Override
	protected IExtension[] createExtensions() {
		// TODO Auto-generated method stub
		return new ICommandExt[]{ new HBDraftExt(getReport())};
	}

}
 