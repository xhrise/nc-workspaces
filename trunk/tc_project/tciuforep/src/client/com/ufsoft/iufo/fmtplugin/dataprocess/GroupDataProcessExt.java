package com.ufsoft.iufo.fmtplugin.dataprocess;

import java.awt.Component;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

public class GroupDataProcessExt extends AbsDataProcessExt {

	GroupDataProcessExt(UfoReport report) {
		super(report);
	}
	String getMenuName() {
		return StringResource.getStringResource("miufo1001596");
	}
	public UfoCommand getCommand() {
		return new GroupDataProcessCmd();
	}
	@Override
	public boolean isEnabled(Component focusComp) {
		return isGetDataFunc();
	}
		
}
