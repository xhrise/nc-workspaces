package com.ufsoft.iufo.fmtplugin.dataprocess;

import java.awt.Component;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

public class CrossTableDataProcessExt extends AbsDataProcessExt {
	
	CrossTableDataProcessExt(UfoReport report) {
		super(report);
	}
	String getMenuName() {
		return StringResource.getStringResource("miufo1001276");
	}
	public UfoCommand getCommand() {
		return new CrossTableDataProcessCmd();
	}
	
	@Override
	public boolean isEnabled(Component focusComp) {	
		
		return isGetDataFunc();
	}	

}
