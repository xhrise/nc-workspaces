package com.ufsoft.report.sysplugin.insertdelete;

import java.awt.Component;
import java.util.ArrayList;

import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.BaseDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.AreaCommonOpr;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedCell;

public class InsertExt extends AbsActionExt{

	private UfoReport _report;
	public InsertExt(UfoReport report) {
		_report = report;
	}
	protected UfoReport getReport(){
		return _report;
	}
	@Override
	public UfoCommand getCommand() {
		// TODO Auto-generated method stub
		return new InsertCmd(_report);
	}

	@Override
	public Object[] getParams(UfoReport container) {
		Object[] params = null;
		CellsModel cModel = getCellsModel();
		if (cModel == null)
			return null;

		/** **********判断插入方式**************** */
		if (isSelectOnlyRows()) { //自动插入行
			int[] selectRows = getSelectedRows();
			if ((selectRows != null) && (selectRows.length > 0)) {
				params = new Object[2];
				params[0] = new Integer(DeleteInsertDialog.INSERT_ROW);
				params[1] = selectRows;
				return params;
			}
		}
		if (isSelectOnlyColumns()) { //自动插入列
			int[] selectCols = getSelectedCols();
			if ((selectCols != null) && (selectCols.length > 0)) {
				params = new Object[2];
				params[0] = new Integer(DeleteInsertDialog.INSERT_COLUMN);
				params[1] = selectCols;
				return params;
			}
		}
		if (isSelectOnlyAreas()) {//用户选择插入方式
			DeleteInsertDialog dlg = new DeleteInsertDialog(getReport(),true);
			dlg.show();
			if (dlg.getSelectOption() == BaseDialog.OK_OPTION) {
				int deleteMethod = dlg.getOperatorMethod();
				switch (deleteMethod) {
				case DeleteInsertDialog.CELL_MOVE_RIGHT:
					return new Object[] {
							new Integer(DeleteInsertDialog.CELL_MOVE_RIGHT),
							getSelectedArea() };
				case DeleteInsertDialog.CELL_MOVE_DOWN:
					return new Object[] {
							new Integer(DeleteInsertDialog.CELL_MOVE_DOWN),
							getSelectedArea() };
				case DeleteInsertDialog.INSERT_ROW:
					AreaPosition[] selectAreas = getSelectedAreas();
					if (selectAreas == null)
						break;
					for (int i = 0; i < selectAreas.length; i++) {//将组合单元换算成区域
						selectAreas[i] = getAreaFromCombineCell(selectAreas[i]);
					}
					int[] rows = AreaCommonOpr.getRows(selectAreas);
					
					if ((rows != null) && (rows.length > 0)) {
						params = new Object[2];
						params[0] = new Integer(DeleteInsertDialog.INSERT_ROW);
						params[1] = rows;
						return params;
					}
					break;
				case DeleteInsertDialog.INSERT_COLUMN:
					AreaPosition[] areas = getSelectedAreas();
					if (areas == null)
						break;
					for (int i = 0; i < areas.length; i++) {//将组合单元换算成区域
						areas[i] = getAreaFromCombineCell(areas[i]);
					}
					int[] cols = AreaCommonOpr.getColumns(areas);
					
					if ((cols != null) && (cols.length > 0)) {
						params = new Object[2];
						params[0] = new Integer(DeleteInsertDialog.INSERT_COLUMN);
						params[1] = cols;
						return params;
					}
					break;
				}

			}
		}
		return null;
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("uiuforep0000877"));
        uiDes.setPaths(new String[]{MultiLang.getString("edit")});
        uiDes.setGroup("insertAndFill");
        uiDes.setShowDialog(true);
        ActionUIDes uiDes2 = (ActionUIDes) uiDes.clone();
        uiDes2.setPaths(new String[]{});
        uiDes2.setPopup(true);
        
        return new ActionUIDes[]{uiDes,uiDes2}; 
	}

	@Override
	public boolean isEnabled(Component focusComp) {
		// TODO Auto-generated method stub
		return 
//		StateUtil.isFormatState(getReport(),focusComp)&&
		StateUtil.isCPane1THeader(getReport(), focusComp);
	}
    
	private boolean isSelectOnlyRows() {

		int[] selectRows = getSelectedRows();
		int[] selectCols = getSelectedCols();
		if (selectRows == null)
			return false;
		if (selectCols != null)
			return false;
		return true;
	}
	/**
	 * 是否只选择了列,不存在交叉区域
	 */
	private boolean isSelectOnlyColumns() {
		int[] selectRows = getSelectedRows();
		int[] selectCols = getSelectedCols();
		if (selectCols == null)
			return false;
		if (selectRows != null)
			return false;
		return true;
	}
	/**
	 * 是否只选择了区域,不存在同时还选择了行或者列
	 */
	private boolean isSelectOnlyAreas() {
		int[] selectRows =getSelectedRows();
		int[] selectCols = getSelectedCols();
		AreaPosition[] selectArea = getSelectedAreas();
		if (selectArea == null)
			return false;
		if (selectRows != null)
			return false;
		if (selectCols != null)
			return false;

		return true;
	}
	private AreaPosition getAreaFromCombineCell(AreaPosition area) {
		if (area == null)
			return null;
		ArrayList areaCells = getCellsModel().getAreaDatas();
		for (int i = 0; i < areaCells.size(); i++) {
			Object obj = areaCells.get(i);
			if ((obj instanceof CombinedCell)
					&& (((CombinedCell) obj).getArea().contain(area))) {
				area = ((CombinedCell) obj).getArea();
			}
		}
		return area;
	}
	/**
	 * 是否同时选择了行和列
	 */
	private boolean isSelectTogetherRowsAndColumns() {
		int[] selectRows =getSelectedRows();
		int[] selectCols =getSelectedCols();

		if (selectRows == null)
			return false;
		if (selectCols == null)
			return false;
		return true;
	}

	/**
	 * 是否有选定的区域、行、列或者单元 由于界面上存在不选择任何区域、行、列或者单元的可能，故需要此判断 caijie 2004-12-3
	 * 
	 * @return boolean
	 */
	private boolean isSelectAny() {

		if (getSelectedRows() != null)
			return true;
		if (getSelectedCols() != null)
			return true;

		if (getSelectedArea() != null)
			return true;

		return false;
	}
	
	protected CellsModel getCellsModel(){
		return getReport().getTable().getCells().getDataModel();
	}
	protected int[] getSelectedRows(){
		return getCellsModel().getSelectModel().getSelectedRow();
	}
	
	protected int[] getSelectedCols(){
		return getCellsModel().getSelectModel().getSelectedCol();
	}
	
	protected AreaPosition getSelectedArea(){
		return getCellsModel().getSelectModel().getSelectedArea();
	}
	
	protected AreaPosition[] getSelectedAreas(){
		return getCellsModel().getSelectModel().getSelectedAreas();
	}
}
