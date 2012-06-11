package com.ufsoft.report.sysplugin.xml;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

public class XMLExportExt extends AbsActionExt {

	/**
	 * @i18n report00114=导出XML
	 * @i18n data=数据
	 * @i18n export=导出
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("report00114"));
		uiDes.setPaths(new String[]{MultiLang.getString("data"), MultiLang.getString("export")});
		return new ActionUIDes[]{uiDes};
	}

	public UfoCommand getCommand() {		
		return new XMLExportCmd();
	}

	public Object[] getParams(UfoReport container) {
		XMLImpExpPlugin pi = (XMLImpExpPlugin) container.getPluginManager().getPlugin(XMLImpExpPlugin.class.getName());
		return new Object[]{container,pi.getXMLObjectPaserManager()};
	}
}
  