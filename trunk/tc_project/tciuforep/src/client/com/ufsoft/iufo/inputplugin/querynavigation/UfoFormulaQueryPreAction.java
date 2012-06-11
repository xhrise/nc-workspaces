package com.ufsoft.iufo.inputplugin.querynavigation;

import com.ufsoft.report.util.MultiLang;

public class UfoFormulaQueryPreAction extends AbsFormulaQueryNavAction{
	public UfoFormulaQueryPreAction() {
		super(true);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected String getToolTip(){
		return MultiLang.getString("uiuforep00134");
	}
	@Override
	protected String getIcon(){
		return "images/reportcore/previous.gif";
	}
}
