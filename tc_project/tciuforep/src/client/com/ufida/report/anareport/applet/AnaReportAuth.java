/*
 * Created on 2005-6-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.anareport.applet;

import java.io.Serializable;

import nc.ui.bi.query.manager.RptProvider;

import com.borland.dx.dataset.Variant;
import com.ufida.report.anareport.fmtplugin.AnaAreaFormulaPlugin;
import com.ufida.report.anareport.model.AnaRepField;
import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.script.base.FormulaVO;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsAuthorization;

/**
 * 分析报表的读写权限管理
 * 
 * @author ll
 */
public class AnaReportAuth implements CellsAuthorization, Serializable {

	static final long serialVersionUID = -7116280144592434721L;

	private AnaReportPlugin m_Plugin;

	/**
	 * @param anaPlugin
	 */
	public AnaReportAuth(AnaReportPlugin anaPlugin) {
		super();
		this.m_Plugin = anaPlugin;
	}

	/**
	 * @see com.ufsoft.table.CellsAuthorization#isReadable(int, int)
	 */
	public boolean isReadable(int row, int col) {
		return true;
	}

	/**
	 * @see com.ufsoft.table.CellsAuthorization#isWritable(int, int)
	 */
	public boolean isWritable(int row, int col) {

		// 字段和公式单元格不能直接编辑
		if(!m_Plugin.isAegisFormat())
			return false;
		AnaReportModel model = m_Plugin.getModel();
		Cell cell = model.getFormatCell(m_Plugin.getCellsModel(),CellPosition.getInstance(row, col));
		if (cell == null) {
			if (model.isFormatState()) {
				return true;
			} else {
				return false;
			}

		}
			
		if(!model.isFormatState()){
			AnaAreaFormulaPlugin fmlPlugin = (AnaAreaFormulaPlugin)m_Plugin.getReport().getPluginManager().getPlugin(AnaAreaFormulaPlugin.class.getName());
			if(fmlPlugin == null)
				return true;
			FormulaVO formulaVo = fmlPlugin.getFmlExecutor().getFormulaModel().getDirectFml(CellPosition.getInstance(cell.getRow(), cell.getCol()));
			if(formulaVo != null)
				return false;
		}
		if (cell.getExtFmt(AnaRepField.EXKEY_FIELDINFO) != null) {
			AnaRepField anaFld = (AnaRepField) cell
					.getExtFmt(AnaRepField.EXKEY_FIELDINFO);
			if (!model.isFormatState()) {
				if (anaFld.getFieldType() == AnaRepField.Type_CROSS_COLUMN
						|| anaFld.getFieldType() == AnaRepField.Type_CROSS_ROW) {
					return true;
				} else if (anaFld.getField() != null
						&& (anaFld.getField().getDataType() == Variant.STRING||anaFld.getField().getExtType()==RptProvider.DIMENSION)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}

		}
			
		return true;
	}

	/**
	 * @param row
	 * @param col
	 * @param type
	 * @see com.ufsoft.table.CellsAuthorization#authorize(int, int, int)
	 */
	public void authorize(int row, int col, int type) {
	}

}
