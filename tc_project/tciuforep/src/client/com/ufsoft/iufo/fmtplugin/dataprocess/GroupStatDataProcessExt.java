package com.ufsoft.iufo.fmtplugin.dataprocess;

import java.awt.Component;

import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.AreaDataProcess;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessDef;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.GroupLayingDef;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

public class GroupStatDataProcessExt extends AbsDataProcessExt {
	
	GroupStatDataProcessExt(UfoReport report) {
		super(report);
	}
	String getMenuName() {
		return StringResource.getStringResource("miufo1001593");
	}
	public UfoCommand getCommand() {
		return new GroupStatDataProcessCmd();
	}
	public boolean isEnabled(Component focusComp) {
		if(getReport().getCellsModel() == null){
			return false;
		}
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(getReport().getCellsModel());
		DynAreaCell focusDynAreaCell = dynAreaModel.getFocusDynAreaCell();
		if(focusDynAreaCell != null){
			AreaDataProcess areaDataProcess = dynAreaModel.getDataProcess(focusDynAreaCell.getDynAreaPK());
			if(areaDataProcess != null){
				DataProcessDef dataProcessDef = areaDataProcess.getDataProcessDef();
				return dataProcessDef != null && dataProcessDef instanceof GroupLayingDef;
			}
		}
		return false;
	}
}
