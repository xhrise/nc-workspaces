package com.ufsoft.report.fmtplugin.formula;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.MultiLang;

/**
 * 简化单元公式计算
 * 
 * @author chxw 2008-4-16
 */
public class AreaFormulaCalcExt extends AbsFormulaExt {
	public String getName() {
		return StringResource.getStringResource("miufo1000033");// "计算";
	}

	public String[] getPath() {
		return new String[] { MultiLang.getString("data") };
	}

	public UfoCommand getCommand() {
		return new AreaFormulaCalcCmd();
	}

}
