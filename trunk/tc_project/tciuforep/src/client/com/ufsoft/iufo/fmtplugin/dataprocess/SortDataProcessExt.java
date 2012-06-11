package com.ufsoft.iufo.fmtplugin.dataprocess;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

public class SortDataProcessExt extends AbsDataProcessExt {
	
	SortDataProcessExt(UfoReport report) {
		super(report);
	}

	String getMenuName() {
		return StringResource.getStringResource("miufo1001595");
	}

	public UfoCommand getCommand() {
		return new SortDataProcessCmd();
	}
}
