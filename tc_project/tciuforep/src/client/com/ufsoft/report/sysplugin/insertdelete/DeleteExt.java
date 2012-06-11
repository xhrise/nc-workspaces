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
 * ϵͳԤ�Ʋ������:ɾ�� ����ɾ����Ԫ��ɾ���С�ɾ�����������ܵ�
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
	 * ����ɾ���л��еĲ��� params[0]: r/c r��ʾɾ����;c��ʾɾ���� params[1]: int[] ɾ���л����е�����
	 */
	public Object[] getParams(UfoReport container) {
		Object[] params = null;
		CellsModel cModel = getCellsModel();
		if (cModel == null)
			return null;

		/** **********�ж�ɾ����ʽ**************** */
		if (isSelectOnlyRows()) { //�Զ�ɾ����
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
		if (isSelectOnlyColumns()) { //�Զ�ɾ����
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
		if (isSelectOnlyAreas()) {//�û�ѡ��ɾ����ʽ
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
					for (int i = 0; i < selectAreas.length; i++) {//����ϵ�Ԫ���������
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
					for (int i = 0; i < areas.length; i++) {//����ϵ�Ԫ���������
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
		/** *************�����ص�,����ɾ��**************** */
		if (isSelectTogetherRowsAndColumns()) {
			UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000900"),
					this.getReport(), null);//�������ص���ѡ��������ʹ�ô�����
		} else if (isSelectAny()) {
			UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000901"),
					this.getReport(), null);//��ѡ������ͬʱ��������(������)��Ԫ�񼰵�Ԫ������ʱ,��������Ч.Ҫôѡ�����л�����,Ҫôѡ����Ԫ������
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
	 * �Ƿ�ֻѡ������,�����ڽ�������
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
	 * add by wangyga 2008-7-14 ��������ޱ�ʱ��������ȫ��ɾ���л���
	 * @param idelNums ɾ�����л�����
	 * @param isRow ɾ�������л�����:true�У�false��
	 * @return boolean  �Ƿ�У��ͨ����
	 * @i18n miufo00088=�л��в���ȫ��ɾ��
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
	 * �Ƿ�ֻѡ������,�����ڽ�������
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
	 * �Ƿ�ֻѡ��������,������ͬʱ��ѡ�����л�����
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
	 * �Ƿ�ͬʱѡ�����к���
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
	 * �Ƿ���ѡ���������С��л��ߵ�Ԫ ���ڽ����ϴ��ڲ�ѡ���κ������С��л��ߵ�Ԫ�Ŀ��ܣ�����Ҫ���ж� caijie 2004-12-3
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
	 * @i18n edit=�༭
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