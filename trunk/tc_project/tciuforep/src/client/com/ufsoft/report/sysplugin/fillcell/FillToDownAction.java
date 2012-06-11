package com.ufsoft.report.sysplugin.fillcell;

import com.ufida.zior.plugin.IPlugin;
import com.ufsoft.report.sysplugin.fill.FillCmd;
import com.ufsoft.report.util.MultiLang;

public class FillToDownAction extends AbstractFillCellAction{

	public FillToDownAction(IPlugin p) {
		super(p);
	}

	@Override
	protected int getFillType() {
		return FillCmd.FillToDown;
	}

	@Override
	protected String getName() {
		return MultiLang.getString("uiuforep0000502");//"ÏòÏÂÌî³ä";
	}

}
