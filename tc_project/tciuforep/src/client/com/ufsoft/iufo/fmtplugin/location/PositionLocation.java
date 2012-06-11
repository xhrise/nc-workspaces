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
 * ��λ������
 * 
 * @author �����
 * @version Create on 2008-5-20
 */
public class PositionLocation extends AbsLocation {
	/** �û���������� */
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
						.getString("uiuforep0000856"), getReport(), null);// ��Ԫ��λʧ��
			}
			SelectModel selectModel = cellsModel.getSelectModel();
			// Ŀ��������һ����Ԫ��
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
					getReport(), e);// ��Ԫ��λʧ��
		}

	}

	private String getPosition() {
		return this.strPosition;
	}
}
