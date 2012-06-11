package com.ufsoft.iufo.fmtplugin.businessquery;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;

public class QueryMngExt extends AbsActionExt {

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes actionUIDes = new ActionUIDes();
		actionUIDes.setName(StringResource.getStringResource("miufo1000232"));
		actionUIDes.setPaths(new String[]{
				StringResource.getStringResource("miufo1001692"),
				StringResource.getStringResource("miufo1001590")});
		actionUIDes.setShowDialog(true);
		return new ActionUIDes[]{actionUIDes};
	}

	public UfoCommand getCommand() {
		return new QueryMngCmd();
	}

	public Object[] getParams(UfoReport container) {
		return new Object[]{container};
	}

}
