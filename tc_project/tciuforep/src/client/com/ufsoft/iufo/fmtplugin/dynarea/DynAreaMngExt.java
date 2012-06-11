package com.ufsoft.iufo.fmtplugin.dynarea;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;

public class DynAreaMngExt extends AbsDynAreaExt{
	public DynAreaMngExt(DynAreaDefPlugIn dynAreaPlug){
		super(dynAreaPlug);
	}
	
	@Override
	public void excuteImpl(UfoReport report){
		dynAreaMngImpl(report);
	}

	@Override
	public String getDesName() {
		return StringResource.getStringResource("miufo1001666");
	}

}
