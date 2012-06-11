package com.ufsoft.report.sysplugin.toolbarmng;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.format.TableConstant;

public class ToolBarMngExt extends AbsActionExt {

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setPopup(true);
		uiDes.setName(MultiLang.getString("toolBarMng"));
		uiDes.setPopupAimComp(TableConstant.TOOLBAR);
		return new ActionUIDes[]{uiDes};
	}

	public UfoCommand getCommand() {
		return new UfoCommand(){
			public void execute(Object[] params) {
				UfoReport report = (UfoReport) params[0];
				new ToolBarMngDlg(report).setVisible(true);
			}			
		};
	}

	public Object[] getParams(UfoReport container) {
		return new Object[]{container};
	}
}
