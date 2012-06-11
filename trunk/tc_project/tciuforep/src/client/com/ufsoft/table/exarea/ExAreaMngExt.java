package com.ufsoft.table.exarea;

import java.awt.Component;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.MultiLang;

public class ExAreaMngExt extends AbsExAreaExt {
  
	
	public ExAreaMngExt(ExAreaPlugin p){
		  super(p);
	}

	@Override
	public void excuteImpl(UfoReport report) {
		showMngDlg(report);
	}

	/**
	 * @i18n miufo00078=管理可扩展区域
	 */
	@Override
	public String getDesName() {
		return MultiLang.getString("miufo00078");
	}
	
	public boolean isEnabled(Component focusComp) {
		if(getPlugIn().getReport().getOperationState() == UfoReport.OPERATION_INPUT)
			return false;
		return true;
	}
	
//	private UfoReport getExAreaReport(UfoReport container){
//		CellsModel cellsModel = (CellsModel) container.getCellsModel().clone();
//		
//		cellsModel = new ExAreaExecutor(cellsModel).execute();
//		
//		UfoReport report = new UfoReport(UfoReport.OPERATION_INPUT, container.getContextVo(), cellsModel);
//		
//		return report;
//	}
 
}
  