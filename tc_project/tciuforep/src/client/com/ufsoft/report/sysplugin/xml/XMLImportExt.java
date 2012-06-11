package com.ufsoft.report.sysplugin.xml;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

public class XMLImportExt extends AbsActionExt {

	/**
	 * @i18n report00112=����XML
	 * @i18n data=����
	 * @i18n import=����
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("report00112"));
		uiDes.setPaths(new String[]{MultiLang.getString("data"), MultiLang.getString("import")});
		return new ActionUIDes[]{uiDes};
	}

	public UfoCommand getCommand() {
		return new XMLImportCmd();
	}

	public Object[] getParams(UfoReport container) {
		XMLImpExpPlugin pi = (XMLImpExpPlugin) container.getPluginManager().getPlugin(XMLImpExpPlugin.class.getName());
		return new Object[]{container,pi.getXMLObjectPaserManager()};
	}

}
  