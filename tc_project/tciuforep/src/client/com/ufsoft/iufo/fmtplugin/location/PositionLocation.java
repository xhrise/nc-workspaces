package com.ufsoft.iufo.fmtplugin.location;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectModel;

/**
 * <pre>
 * </pre>
 * 
 * 定位：区域
 * 
 * @author 王宇光
 * @version Create on 2008-5-20
 */
public class PositionLocation extends AbsLocation {
	/** 用户输入的区域 */
	private String strPosition = null;

	public PositionLocation(UfoReport rep, String inputString) {
		super(rep);
		this.strPosition = inputString;
	}

	protected void location() {
		locationImpl(null, getCellsModel());
	}

	@Override
	protected int getConditionType() {
		return AbsLocation.REFERENCE_POSITION;
	}

	@Override
	protected void locationImpl(CellPosition cellPosition, CellsModel cellsModel) {
		try {

			if (cellsModel == null) {
				return;
			}
			AreaPosition area = AreaPosition.getInstance(getPosition());
			if (area == null) {
				UfoPublic.sendErrorMessage(MultiLang
						.getString("uiuforep0000856"), getReport(), null);// 单元定位失败
			}
			SelectModel selectModel = cellsModel.getSelectModel();
			// 目标区域是一个单元格
			if (area.isCell()) {

				selectModel.clear();
				selectModel.setAnchorCell(area.getStart());

				return;
			}

			selectModel.clear();
			selectModel.setSelectedArea(area);

		} catch (Exception e) {
			AppDebug.debug(e);

			UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000856"),
					getReport(), e);// 单元定位失败
		}

	}

	private String getPosition() {
		return this.strPosition;
	}
}
