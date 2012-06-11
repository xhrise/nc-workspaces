package com.ufsoft.report.sysplugin.fillcell;

import com.ufida.zior.plugin.IPlugin;
import com.ufsoft.report.sysplugin.fill.FillCmd;
import com.ufsoft.report.util.MultiLang;

public class FillToRightAction extends AbstractFillCellAction{

	public FillToRightAction(IPlugin p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getFillType() {
		return FillCmd.FillToRight;
	}

	@Override
	protected String getName() {
		return MultiLang.getString("uiuforep0000504");//"œÚ”“ÃÓ≥‰";
	}

}
