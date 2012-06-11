package com.ufsoft.table.exarea;

import java.awt.Component;

import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.report.util.MultiLang;

public class ExAreaSeperateByColExt extends AbsExAreaExt {
	
	public ExAreaSeperateByColExt(ExAreaPlugin plugin) {
		super(plugin);
	}

	public boolean isEnabled(Component focusComp) {
		CellPosition anchorCell = getPlugIn().getCellsModel().getSelectModel().getAnchorCell(); 
		
		if(anchorCell == null || !anchorCell.equals(getPlugIn().getCellsModel().getSelectModel().getSelectedArea())){
			return false;
		}
		
		ExAreaCell exCell = getPlugIn().getExAreaModel().getExArea(anchorCell);
		if(exCell == null){
			return false;
		}
		AreaPosition area = exCell.getArea();
		if(area.getStart().getColumn() == anchorCell.getColumn() ){
//				|| area.getEnd().getColumn() == anchorCell.getColumn()){
			return false;
		}
		
		if(!StateUtil.isFormatState(getPlugIn().getReport(), focusComp))
			return false;
		return true;
	}
	
	/**
	 * @i18n miufo00080=按列分割可扩展区域
	 */
	@Override
	public String getDesName() {
		return MultiLang.getString("miufo00080");
	}
	
	protected AreaPosition[] seperateArea(AreaPosition area, CellPosition anchorCell){
		
		CellPosition start = area.getStart();
		CellPosition end = CellPosition.getInstance(area.getEnd().getRow(), anchorCell.getColumn() - 1);
		
		AreaPosition newArea1 = AreaPosition.getInstance(start, end);
		
		start = CellPosition.getInstance(area.getStart().getRow(), anchorCell.getColumn());
		
		AreaPosition newArea2 = AreaPosition.getInstance(start, area.getEnd());
		
		return new AreaPosition[]{newArea1, newArea2};
	}

	/**
	 * @i18n miufo00081=拆分区域存在合并单元格
	 * @i18n miufo00082=错误提示
	 */
	@Override
	public void excuteImpl(UfoReport report) {
		CellsModel model = getPlugIn().getCellsModel();
		CellPosition anchorCell = model.getSelectModel().getAnchorCell(); 
		ExAreaCell exCell = getPlugIn().getExAreaModel().getExArea(anchorCell);
		if(exCell == null || anchorCell == null){
			return;
		}
		AreaPosition[] arr = seperateArea(exCell.getArea(),anchorCell);
		if(arr == null){
			return;
		}
		
		AreaPosition newArea1 = arr[0];
		AreaPosition newArea2 = arr[1];

		if(!getPlugIn().getExAreaModel().check(exCell, newArea1)
				|| !getPlugIn().getExAreaModel().check(exCell, newArea2)){
			UfoPublic.showErrorDialog(getPlugIn().getReport(), MultiLang.getString("miufo00081"), MultiLang.getString("miufo00082"));
			return;
		}
		
		ExAreaCell cell1 = (ExAreaCell)exCell.clone();//new ExAreaCell(newArea1);
		cell1.setArea(newArea1);
		cell1.setExAreaName(null);
		cell1.setModel(null);
		ExAreaCell cell2 = new ExAreaCell(newArea2);
		cell2.setExAreaName(null);
		if(exCell.getExAreaName() != null && exCell.getExAreaName().trim().length() > 0){
			cell1.setExAreaName(exCell.getExAreaName() + "_1");
			cell2.setExAreaName(exCell.getExAreaName() + "_2");
		}
		
		String error = exCell.fireUIEvent(ExAreaModelListener.SPLIT, exCell, new ExAreaCell[] {
				cell1, cell2 });
		 if(error != null && error.length() > 0){
			UfoPublic.showErrorDialog(getPlugIn().getReport(), error, MultiLang.getString("miufo00082")); 
			return;
		 }

		getPlugIn().getExAreaModel().removeExArea(exCell);
		getPlugIn().getExAreaModel().addExArea(cell1);
		getPlugIn().getExAreaModel().addExArea(cell2);
		
	}
} 