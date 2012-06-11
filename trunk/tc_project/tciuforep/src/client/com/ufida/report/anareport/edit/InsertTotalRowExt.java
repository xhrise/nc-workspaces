/**
 * 
 */
package com.ufida.report.anareport.edit;

import java.awt.Component;

import nc.ui.bi.query.manager.RptProvider;
import nc.ui.pub.beans.MessageDialog;

import com.ufida.dataset.metadata.DataTypeConstant;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.adhoc.model.IFldCountType;
import com.ufida.report.anareport.applet.AnaInsertColumnsCmd;
import com.ufida.report.anareport.applet.AnaInsertRowsCmd;
import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.FieldCountDef;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author ll
 * 
 */
public class InsertTotalRowExt extends AbsAnaReportExt {

	private int m_type = 0;
	private CellPosition m_pos;// 当前处理单元位置

	public InsertTotalRowExt(AnaReportPlugin plugin, int type) {
		super.setPlugIn(plugin);
		m_type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
	 */
	@Override
	public UfoCommand getCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
	 */
	public boolean isEnabled(Component focusComp) {
		AreaDataModel dModel = getSelAreaModel(true);
		if(dModel == null || dModel.getDSPK() == null)
			return false;
		if(dModel.isCross() || dModel.isChart())
			return false;
//		if(!dModel.getDSDef().supportDescriptorFunc(DescriptorType.AggrDescriptor))
//			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
	 */
	@Override
	public Object[] getParams(UfoReport container) {
		AreaDataModel dataModel = getSelAreaModel(true);
		String dsPK = dataModel.getDSPK();

		CellsModel formatModel = m_plugin.getModel().getFormatModel();
		CellPosition pos = getFirstSelPos();
		CellPosition oldPos = pos;
		ExAreaCell exCell = ExAreaModel.getInstance(formatModel).getExArea(pos);
		AreaPosition exArea = exCell.getArea();
		boolean isRow = (exCell.getExMode() != ExAreaCell.EX_MODE_X);
		AreaPosition aimArea = null;// 新增加的单元范围
		UfoCommand insertRowCmd = null;
		if (isRow) {
			m_pos = pos.getMoveArea(1, 0);
			aimArea = AreaPosition.getInstance(m_pos.getRow(), exArea.getStart().getColumn(), exArea.getWidth(), 1);
			insertRowCmd = new AnaInsertRowsCmd(container, m_plugin) {
				@Override
				protected AreaPosition[] getSelectArea() {
					return new AreaPosition[] { AreaPosition.getInstance(m_pos, m_pos) };
				}
				protected void doRefreshDataModel(){
				}
			};
		} else {
			m_pos = pos.getMoveArea(0, 1);
			aimArea = AreaPosition.getInstance(exArea.getStart().getRow(), m_pos.getColumn(), 1, exArea.getHeigth());
			insertRowCmd = new AnaInsertColumnsCmd(container, m_plugin) {
				@Override
				protected AreaPosition[] getSelectArea() {
					return new AreaPosition[] { AreaPosition.getInstance(m_pos, m_pos) };
				}
				protected void doRefreshDataModel(){
				}
			};
		}
		formatModel.getSelectModel().setAnchorCell(m_pos);
		formatModel.getSelectModel().setSelectedArea(AreaPosition.getInstance(m_pos, m_pos));
		Object[] params = new Object[] { 1 };
		try {
			insertRowCmd.execute(params);
		} catch (Exception ex) {
			AppDebug.debug(ex);
			MessageDialog.showErrorDlg(container, null, ex.getMessage());
			return null;
		}
		boolean procStr = isSupportString(m_type);
		CellPosition[] cells = aimArea.split();
		for (CellPosition cell : cells) {
			AnaRepField fld = findLastMeasure(formatModel, cell, exArea.getStart(), isRow, procStr);
			if (fld != null) {
				if (fld.getField().getExtType() != RptProvider.DIMENSION) {
					FieldCountDef tFld = new FieldCountDef(fld.getField(), m_type, null,
							null, null);

					AnaRepField totalFld = new AnaRepField(fld.getField(),
							AnaRepField.TYPE_CALC_FIELD, dsPK);
					totalFld.setCountFieldDef(tFld);
					m_plugin.appendFields(cell, dsPK,
							new AnaRepField[] { totalFld }, false);
				}
			}
		}
		m_plugin.refreshDataModel(true);
		formatModel.getSelectModel().setAnchorCell(oldPos);
		formatModel.getSelectModel().setSelectedArea(AreaPosition.getInstance(oldPos, oldPos));

		return null;
	}
	private boolean isSupportString(int type){
		return ((type == IFldCountType.TYPE_COUNT)||(type == IFldCountType.TYPE_MAX)||(type == IFldCountType.TYPE_MIN));
	}
	// 从指定位置向左（上）方寻找第一个可以数值类型的指标字段
	private AnaRepField findLastMeasure(CellsModel cells, CellPosition pos, CellPosition start, boolean isRow, boolean procStr) {
		int height = isRow ? (pos.getRow() - start.getRow()) : (pos.getColumn() - start.getColumn());
		for (int i = 0; i < height; i++) {
			pos = pos.getMoveArea(isRow ? -1 : 0, isRow ? 0 : -1);
			Cell cell = cells.getCell(pos);
			if (cell == null)
				continue;
			AnaRepField field = (AnaRepField) cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
			if (field == null || field.getFieldCountDef() != null)
				continue;

			if(procStr)//支持处理字符类型的字段
				return field;
			if (DataTypeConstant.isNumberType(field.getFieldDataType())) {//对于数值类型
				return field;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
		return new ActionUIDes[] { getLocalUIDes(m_type) };
	}

	/**
	 * @i18n iufobi00059=插入小计
	 */
	private ActionUIDes getLocalUIDes(int i) {
		String[] names = (new FieldCountDef()).getCountTypeNames();
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setPaths(new String[] { StringResource.getStringResource("iufobi00059") });// TODO multilang
		uiDes.setPopup(true);
		uiDes.setGroup("insertAndFill");
		uiDes.setName(names[i]);
		return uiDes;
	}
}
 