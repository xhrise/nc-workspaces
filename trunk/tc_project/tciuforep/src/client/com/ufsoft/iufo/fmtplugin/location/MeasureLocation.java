package com.ufsoft.iufo.fmtplugin.location;

import nc.vo.iufo.measure.MeasureVO;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

/**
 * <pre>
 * </pre>
 * 
 * ��λָ��
 * 
 * @author �����
 * @version Create on 2008-5-21
 */
public class MeasureLocation extends AbsLocation implements IUfoContextKey{
	/** ��λȫ��ָ�� */
	protected final static int ALL_MEASURES = 1;
	/** ��λ����ָ�� */
	protected final static int CURRENT_REPORT_MEASURES = 2;
	/** ��λ������ָ�� */
	protected final static int OTHER_REPORT_MEASURES = 3;
	/** ָ������ */
	private int measureType = 0;

	public MeasureLocation(UfoReport rep, int measureType) {
		super(rep);
		this.measureType = measureType;
	}

	@Override
	protected int getConditionType() {
		// TODO Auto-generated method stub
		return AbsLocation.MEASURE;
	}

	@Override
	protected void locationImpl(CellPosition cellPosition, CellsModel cellsModel) {
		if (cellPosition == null || cellsModel == null) {
			throw new IllegalArgumentException(StringResource.getStringResource("miufo1000496"));//�������������Ϊ��
		}
		MeasureVO measureVo = getMeasureModel(cellsModel).getMeasureVOByPos(
				cellPosition);
		if (measureVo == null) {
			return;
		}
		if (getMeasureType() == ALL_MEASURES) {
			getCellsPositionList().add(cellPosition);
			return;
		} else if (getMeasureType() == CURRENT_REPORT_MEASURES
				&& isRefMeasure(measureVo)) {
			getCellsPositionList().add(cellPosition);
			return;
		} else if (getMeasureType() == OTHER_REPORT_MEASURES
				&& !isRefMeasure(measureVo)) {
			getCellsPositionList().add(cellPosition);
		}
	}

	/**
	 * �ж��Ƿ��Ǳ���ָ��
	 * 
	 * @param MeasureVO
	 *            measureVo
	 * @return boolean
	 */
	private boolean isRefMeasure(MeasureVO measureVo) {
		if (measureVo == null) {
			throw new IllegalArgumentException(StringResource.getStringResource("miufo1000496"));//�������������Ϊ��
		}
		Object repIdObj = getReport().getContextVo().getAttribute(REPORT_PK);
		String strRepPK = repIdObj != null && (repIdObj instanceof String)? (String)repIdObj:null;
		
//		String reportId = getReport().getContextVo().getContextId();
		if (strRepPK == null || strRepPK.length() == 0) {
			throw new IllegalArgumentException("");
		}
		if (strRepPK.equals(measureVo.getReportPK())) {
			return true;
		}
		return false;
	}

	private int getMeasureType() {
		return this.measureType;
	}
}
