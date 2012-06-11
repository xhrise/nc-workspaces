/*
 * DeleteExt.java
 * Created on 2004-10-19 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.report.sysplugin.insertdelete;

import java.awt.Component;
import java.util.ArrayList;

import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.BaseDialog;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.AreaCommonOpr;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedCell;

/**
 * 系统预制插件功能:删除 挂载删除单元格、删除行、删除列三个功能点
 * 
 * @author CaiJie
 * @since 3.1
 */
public class DeleteExt extends AbsInsertDeleteExt {// IMainMenuExt,IPopupMenuExt{

	/**
	 * 
	 * CaiJie 2004-10-19
	 * 
	 * @param rep
	 */
	public DeleteExt(UfoReport rep) {
		super(rep);
	}

	/*
	 * Overrding method
	 * 
	 * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {

		return new DeleteCmd(this.getReport());
	}

	/**
	 * 返回删除行或列的参数 params[0]: r/c r表示删除行;c表示删除列 params[1]: int[] 删除行或者列的数组
	 */
	public Object[] getParams(UfoReport container) {
		Object[] params = null;
		CellsModel cModel = getCellsModel();
		if (cModel == null)
			return null;

		/** **********判断删除方式**************** */
		if (isSelectOnlyRows()) { //自动删除行
			int[] selectRows = getSelectedRows();
			if (!validateDel(selectRows, true)) {
				return null;
			}
			if ((selectRows != null) && (selectRows.length > 0)) {
				params = new Object[2];
				params[0] = new Integer(DeleteInsertDialog.DELETE_ROW);
				params[1] = selectRows;
				return params;
			}
		}
		if (isSelectOnlyColumns()) { //自动删除列
			int[] selectCols = getSelectedCols();
			if (!validateDel(selectCols, false)) {
				return null;
			}
			if ((selectCols != null) && (selectCols.length > 0)) {
				params = new Object[2];
				params[0] = new Integer(DeleteInsertDialog.DELTE_COLUMN);
				params[1] = selectCols;
				return params;
			}
		}
		if (isSelectOnlyAreas()) {//用户选择删除方式
			DeleteInsertDialog dlg = new DeleteInsertDialog(getReport(),false);
			dlg.show();
			if (dlg.getSelectOption() == BaseDialog.OK_OPTION) {
				int deleteMethod = dlg.getOperatorMethod();
				switch (deleteMethod) {
				case DeleteInsertDialog.CELL_MOVE_LEFT:
					return new Object[] {
							new Integer(DeleteInsertDialog.CELL_MOVE_LEFT),
							getSelectedArea() };
				case DeleteInsertDialog.CELL_MOVE_UP:
					return new Object[] {
							new Integer(DeleteInsertDialog.CELL_MOVE_UP),
							getSelectedArea() };
				case DeleteInsertDialog.DELETE_ROW:
					AreaPosition[] selectAreas = getSelectAreas();
					if (selectAreas == null)
						break;
					for (int i = 0; i < selectAreas.length; i++) {//将组合单元换算成区域
						selectAreas[i] = getAreaFromCombineCell(selectAreas[i]);
					}
					int[] rows = AreaCommonOpr.getRows(selectAreas);
					if (!validateDel(rows, true)) {
						return null;
					}
					if ((rows != null) && (rows.length > 0)) {
						params = new Object[2];
						params[0] = new Integer(DeleteInsertDialog.DELETE_ROW);
						params[1] = rows;
						return params;
					}
					break;
				case DeleteInsertDialog.DELTE_COLUMN:
					AreaPosition[] areas = getSelectAreas();
					if (areas == null)
						break;
					for (int i = 0; i < areas.length; i++) {//将组合单元换算成区域
						areas[i] = getAreaFromCombineCell(areas[i]);
					}
					int[] cols = AreaCommonOpr.getColumns(areas);
					if (!validateDel(cols, false)) {
						return null;
					}
					if ((cols != null) && (cols.length > 0)) {
						params = new Object[2];
						params[0] = new Integer(DeleteInsertDialog.DELTE_COLUMN);
						params[1] = cols;
						return params;
					}
					break;
				}

			}
			return null;
		}
		/** *************区域重叠,不能删除**************** */
		if (isSelectTogetherRowsAndColumns()) {
			UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000900"),
					this.getReport(), null);//不能在重叠的选定区域上使用此命令
		} else if (isSelectAny()) {
			UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000901"),
					this.getReport(), null);//当选定区域同时包含整行(或整列)单元格及单元格区域时,此命令无效.要么选定整行或整列,要么选定单元格区域
		}
		return null;
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
	
	protected AreaPosition[] getSelectAreas(){
		return getCellsModel().getSelectModel().getSelectedAreas();
	}
	
	protected CellsModel getCellsModel(){
		return getReport().getTable().getCells().getDataModel();
	}
	
	/**
	 * 是否只选择了行,不存在交叉区域
	 */
	private boolean isSelectOnlyRows() {
		CellsModel cModel = getReport().getTable().getCells().getDataModel();
		int[] selectRows = cModel.getSelectModel().getSelectedRow();
		int[] selectCols = cModel.getSelectModel().getSelectedCol();
		if (selectRows == null)
			return false;
		if (selectCols != null)
			return false;
		return true;
	}

	/**
	 * add by wangyga 2008-7-14 如果是有限表时，不允许全部删除行或列
	 * @param idelNums 删除的行或列数
	 * @param isRow 删除的是行或者列:true行，false列
	 * @return boolean  是否校验通过。
	 * @i18n miufo00088=行或列不能全部删除
	 */
	private boolean validateDel(int[] idelNums, boolean isRow) {
		if (idelNums == null || idelNums.length <= 0)
			return true;
		int iLength = idelNums.length;
		CellsModel cModel = getReport().getTable().getCells().getDataModel();
		if (!cModel.isInfinite()) {
			if (isRow) {
				int iRowCount = cModel.getRowHeaderModel().getCount();
				if (iLength == iRowCount) {
					UfoPublic.sendWarningMessage(MultiLang.getString("miufo00088"), this.getReport());
					return false;
				}
			} else {
				int iColCount = cModel.getColumnHeaderModel().getCount();
				if (iLength == iColCount) {
					UfoPublic.sendWarningMessage(MultiLang.getString("miufo00088"), this.getReport());
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 是否只选择了列,不存在交叉区域
	 */
	private boolean isSelectOnlyColumns() {
		CellsModel cModel = getReport().getTable().getCells().getDataModel();
		int[] selectRows = cModel.getSelectModel().getSelectedRow();
		int[] selectCols = cModel.getSelectModel().getSelectedCol();
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
		CellsModel cModel = getReport().getTable().getCells().getDataModel();
		int[] selectRows = cModel.getSelectModel().getSelectedRow();
		int[] selectCols = cModel.getSelectModel().getSelectedCol();
		AreaPosition[] selectArea = cModel.getSelectModel().getSelectedAreas();
		if (selectArea == null)
			return false;
		if (selectRows != null)
			return false;
		if (selectCols != null)
			return false;

		return true;
	}

	/**
	 * 是否同时选择了行和列
	 */
	private boolean isSelectTogetherRowsAndColumns() {
		CellsModel cModel = getReport().getTable().getCells().getDataModel();
		int[] selectRows = cModel.getSelectModel().getSelectedRow();
		int[] selectCols = cModel.getSelectModel().getSelectedCol();

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
		CellsModel cModel = getReport().getTable().getCells().getDataModel();

		if (cModel.getSelectModel().getSelectedRow() != null)
			return true;
		if (cModel.getSelectModel().getSelectedCol() != null)
			return true;

		if (cModel.getSelectModel().getSelectedArea() != null)
			return true;

		return false;
	}

	private AreaPosition getAreaFromCombineCell(AreaPosition area) {
		if (area == null)
			return null;
		ArrayList areaCells = getReport().getCellsModel().getAreaDatas();
		for (int i = 0; i < areaCells.size(); i++) {
			Object obj = areaCells.get(i);
			if ((obj instanceof CombinedCell)
					&& (((CombinedCell) obj).getArea().contain(area))) {
				area = ((CombinedCell) obj).getArea();
			}
		}
		return area;
	}

	@Override
	public boolean isEnabled(Component focusComp) {
		return isFormatState()
				&& StateUtil.isCPane1THeader(getReport(), focusComp);
	}

	/*
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	/**
	 * @i18n edit=编辑
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setImageFile("reportcore/delete.gif");
		uiDes1.setName(MultiLang.getString("uiuforep0000710"));
		uiDes1.setPaths(new String[] { MultiLang.getString("edit") });
		uiDes1.setGroup("insertAndFill");
		
		ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
		uiDes2.setPaths(new String[] {});
		uiDes2.setPopup(true);
		return new ActionUIDes[] { uiDes1, uiDes2 };
	}
} 