package com.ufsoft.report.sysplugin.fillcell;

import com.ufida.zior.plugin.IPlugin;
import com.ufsoft.report.sysplugin.fill.FillCmd;
import com.ufsoft.report.util.MultiLang;

public class FillToUpAction extends AbstractFillCellAction{

	public FillToUpAction(IPlugin p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getFillType() {
		return FillCmd.FillToUp;
	}

	@Override
	protected String getName() {
		return MultiLang.getString("uiuforep0000501");//"œÚ…œÃÓ≥‰";
	}

}
