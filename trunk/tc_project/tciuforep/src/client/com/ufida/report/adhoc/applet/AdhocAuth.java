/*
 * Created on 2005-6-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.adhoc.applet;

import java.io.Serializable;

import com.ufida.report.adhoc.model.AdhocArea;
import com.ufida.report.adhoc.model.AdhocPublic;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsAuthorization;
import com.ufsoft.table.IArea;

/**
 * Adhoc报表的读写权限管理
 * 
 * @author caijie
 */
public class AdhocAuth implements CellsAuthorization, Serializable {

	static final long serialVersionUID = -7116280144592434721L;

	/***/
	private AdhocPlugin adhocPlugin;

	// private AdhocModel m_detailArea = null;
	/**
	 * @param report
	 */
	public AdhocAuth(AdhocPlugin adhocPlugin) {
		super();
		this.adhocPlugin = adhocPlugin;
		// m_detailArea =
		// this.adhocPlugin.getModel().getAreaByType(AdhocArea.DETAIL_AREA_TYPE)[0];
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
		// 格式状态
		if (adhocPlugin.isFormat()) {// getOperationState()
										// ==UfoReport.OPERATION_FORMAT){
			CellPosition pos = CellPosition.getInstance(row, col);
			IArea area = adhocPlugin.getModel().getFormatArea(pos, true, false);
			if (area == null)
				return true;

			if (!adhocPlugin.getModel().isFormatState() && adhocPlugin.getModel().getDataCenter().isCross()) {//交叉表样式单独判断
				boolean isInHead = adhocPlugin.getModel().getDataCenter().getCrossTableProperty().isInCrossHead(pos);
				if(isInHead)return true;
				
				return false;
			} else {
				CellPosition fmtPos = area.getStart();
				if (adhocPlugin.getCellsModel().getBsFormat(fmtPos, AdhocPublic.EXT_FMT_SELECTED_FIELD) != null) {
					return false; // 查询字段不能编辑
				}
				if (adhocPlugin.getCellsModel().getBsFormat(fmtPos, AdhocPublic.EXT_FMT_CALCULATE_COLUMN) != null) {
					return false; // 计算字段不能编辑
				}
				area = adhocPlugin.getModel().getFormatArea(pos, false, false);
				if (area == null)
					return true;
				fmtPos = area.getStart();
				if (adhocPlugin.getCellsModel().getBsFormat(fmtPos, AdhocPublic.EXT_FMT_FUNCTION_FIELD) != null) {
					return false; // 计算项不能编辑
				}

				if (adhocPlugin.getCellsModel().getBsFormat(fmtPos, AdhocPublic.EXT_FMT_GROUP_FIELD) != null) {
					return false; // 分组字段不能编辑
				}
				return true;
			}

		} else {// 数据状态
			return false;
		}
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
