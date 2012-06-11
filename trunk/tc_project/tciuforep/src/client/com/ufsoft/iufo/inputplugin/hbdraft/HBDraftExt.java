package com.ufsoft.iufo.inputplugin.hbdraft;

import java.awt.Component;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

public class HBDraftExt extends AbsActionExt{

	private UfoReport _report;
	private boolean bHasHBDraft = false;
	

    /**
     * @param report
     */
	public HBDraftExt(UfoReport report) {
        _report = report;
    }
	@Override
	public boolean isEnabled(Component focusComp) {
		return bHasHBDraft;
	}
	@Override
	public UfoCommand getCommand() {
		return new HBDraftCmd(_report);
	}

	@Override
	public Object[] getParams(UfoReport container) {

		return null;
	}

	/**
	 * @i18n uiuforep00096=合并底稿
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setName(MultiLang.getString("uiuforep00096"));
        uiDes1.setPaths(new String[]{MultiLang.getString("data")});//"数据"
        uiDes1.setGroup("traceDataGroup");
        return new ActionUIDes[]{uiDes1};
    }
	public boolean isHasHBDraft() {
		return bHasHBDraft;
	}
	public void setHasHBDraft(boolean hasHBDraft) {
		bHasHBDraft = hasHBDraft;
	}

}
 