package com.ufsoft.iufo.inputplugin.querynavigation;

import com.ufsoft.report.util.MultiLang;

public class UfoFormulaQueryNextAction extends AbsFormulaQueryNavAction{
	
	public UfoFormulaQueryNextAction() {
		super(false);
	}
	@Override
	protected String getToolTip(){
		return MultiLang.getString("uiuforep00135");
	}
	@Override
	protected String getIcon(){
		return "images/reportcore/next.gif";
	}
}
