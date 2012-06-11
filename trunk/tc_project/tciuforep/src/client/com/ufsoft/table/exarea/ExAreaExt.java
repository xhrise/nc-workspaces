package com.ufsoft.table.exarea;

import java.awt.Component;

import com.ufsoft.report.UfoReport;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
import com.ufsoft.report.util.MultiLang;

public class ExAreaExt extends AbsExAreaExt {
  
	public ExAreaExt(ExAreaPlugin p){
		  super(p);
	}
	
	public boolean isEnabled(Component focusComp) {
//		CellsModel model = getPlugIn().getCellsModel();
//		AreaPosition selAnchorCell = model.getSelectModel().getSelectedArea();
//		return getPlugIn().getExAreaModel().check(null, selAnchorCell);
		if(getPlugIn().getReport().getOperationState() == UfoReport.OPERATION_INPUT)
			return false;//add by wangyga 2008-9-1
		return true;
	}

	@Override
	public void excuteImpl(UfoReport report) {
		ExAreaCell cell = getSelectedEx();
		
		showSettingDlg(report,null, cell);
		
		if(cell == null){
			return;
		}

		//重新绘制可扩展区域
		IArea area = cell.getArea();
		getPlugIn().getReport().repaint(area);
		
	}

   
	protected ExAreaCell getSelectedEx() {
		CellsModel model = getPlugIn().getCellsModel();
		AreaPosition selCell = (AreaPosition) model.getSelectModel().getSelectedArea();
		
		ExAreaCell[] cells = getPlugIn().getExAreaModel().getExAreaCells();
		ExAreaCell cell = null;
		for (int i = 0; i < cells.length; i++) {
			AreaPosition area = cells[i].getArea();
			if(selCell.intersection(area)){
				cell = cells[i];
				break;
			}
		}
		return cell;
	}

	/**
	 * @i18n miufo00077=设置可扩展区域
	 */
	@Override
	public String getDesName() {
		return MultiLang.getString("miufo00077");
	}
	
}
  