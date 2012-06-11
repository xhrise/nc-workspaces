package com.ufsoft.report.sysplugin.fillcell;

import com.ufida.zior.plugin.IPlugin;
import com.ufsoft.report.sysplugin.fill.FillCmd;
import com.ufsoft.report.util.MultiLang;

public class FillToLeftAction extends AbstractFillCellAction{

	public FillToLeftAction(IPlugin p) {
		super(p);
	}

	@Override
	protected int getFillType() {
		return FillCmd.FillToLeft;
	}

	@Override
	protected String getName() {
		return MultiLang.getString("uiuforep0000503");//"Ïò×óÌî³ä";
	}

}
