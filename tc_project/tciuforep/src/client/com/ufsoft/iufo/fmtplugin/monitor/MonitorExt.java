package com.ufsoft.iufo.fmtplugin.monitor;

import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

abstract class MonitorExt  extends AbsActionExt {

	public UfoCommand getCommand() {
		return null;
	}
 

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(getDesName());
		uiDes.setPaths(new String[] { MultiLang.getString("help") });
		return new ActionUIDes[] { uiDes };
	}
 
	protected abstract String getDesName();

}
