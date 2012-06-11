package com.ufsoft.iufo.fmtplugin.dataprocess;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

public class DelDataProcessExt extends AbsDataProcessExt {

	DelDataProcessExt(UfoReport report) {
		super(report);
	}
	String getMenuName() {
		return StringResource.getStringResource("miufo1001599");
	}
	public UfoCommand getCommand() {
		return new DelDataProcessCmd();
	}

}