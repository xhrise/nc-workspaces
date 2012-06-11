package com.ufsoft.iufo.fmtplugin.location;

import java.util.ArrayList;

import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.measure.MeasureModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

/**
 * <pre>
 * </pre>
 * 
 * ��λ���ܵĻ���
 * 
 * @author �����
 * @version Create on 2008-5-19
 */
public abstract class AbsLocation {
	/** ��λ���ͣ���ע */
	protected final static int POSTIL = 0;
	/** ��λ���ͣ����� */
	protected final static int CONSTANT = 1;
	/** ��λ���ͣ���ʽ */
	protected final static int FORMULA = 2;
	/** ��λ���ͣ�ָ�� */
	protected final static int MEASURE = 3;
	/** ��λ���ͣ���ֵ */
	protected final static int NULL_VALUE = 4;
	/** ��λ���ͣ����õ�Ԫ�� */
	protected final static int REF_CELL = 5;
	/** ��λ���ͣ����һ����Ԫ�� */
	protected final static int LAST_CELL = 6;
	/** ��λ���ͣ�������ʽ */
	protected final static int CONDITION_FORMAT = 7;
	/** ��λ���ͣ����ݸ�ʽ */
	protected final static int DATA_FORMAT = 8;
	/** ��λ���ͣ����ݸ�ʽ */
	protected final static int REFERENCE_POSITION = 9;
	/** ��� */
	private UfoReport m_Report = null;
	/** ���涨λ���Ľ�� */
	private ArrayList<CellPosition> cellsPositionList;

	public AbsLocation(UfoReport rep) {
		if (rep == null) {
			throw new IllegalArgumentException(StringResource
					.getStringResource("miufo1000496"));// �������������Ϊ��
		}
		this.m_Report = rep;
		cellsPositionList = new ArrayList<CellPosition>();// ���涨λ���Ľ��
	}

	protected void location() {
		ArrayList<CellPosition> maxAreaCells = getMaxAreaCells();
		
		if (maxAreaCells == null || maxAreaCells.size() == 0) {
			return;
		}
		CellsModel cellsModel = getCellsModel();
		int iSize = maxAreaCells.size();
		for (int i = 0; i < iSize; i++) {
			CellPosition cellPosition = maxAreaCells.get(i);
			locationImpl(cellPosition, cellsModel);
		}
		ArrayList<CellPosition> cellsPositionList = getCellsPositionList();
		if (cellsPositionList != null && cellsPositionList.size() > 0) {
			AreaPosition area = AreaPosition.getInstance(cellsPositionList
					.get(0), cellsPositionList.get(0));
			cellsModel.getSelectModel().setSelectedArea(area);
		} else {
			UfoPublic.sendErrorMessage(StringResource
					.getStringResource("miufo1004058"), getReport(), null);// δ�ҵ���Ԫ��
		}

	}

	/**
	 * ��λ����������
	 * 
	 * @param
	 * @return int
	 */
	abstract protected int getConditionType();

	/**
	 * ����Ķ�λ����ʵ�ִ˷���
	 * 
	 * @param
	 * @return int
	 */
	abstract protected void locationImpl(CellPosition cellPosition,
			CellsModel cellsModel);

	/**
	 * ��÷ǿ�ʱ���������
	 * 
	 * @param
	 * @return ArrayList<CellPosition>
	 */
	private ArrayList<CellPosition> getMaxAreaCells() {
		AreaPosition allArea = AreaPosition.getInstance(0, 0,
				getMaxColumnCount(), getMaxRowCount());
		ArrayList<CellPosition> allPos = getCellsModel().getSeperateCellPos(
				allArea);
		return allPos;
	}

	protected FormulaModel getFormulaModel(CellsModel cellsModel) {
		return CellsModelOperator.getFormulaModel(cellsModel);
	}

	protected MeasureModel getMeasureModel(CellsModel cellsModel) {
		return CellsModelOperator.getMeasureModel(cellsModel);
	}

	/**
	 * ��ñ���ǿ��������
	 * 
	 * @param
	 * @return int
	 */
	protected int getMaxRowCount() {
		int[] validHeaders = getCellsModel().getValidHeaders();
		if (validHeaders == null || validHeaders.length < 2) {
			throw new IllegalArgumentException(StringResource
					.getStringResource("miufo1004059"));// ��÷ǿ������ʧ��
		}
		return validHeaders[0] + 1;
	}

	/**
	 * ��ñ���ǿ��������
	 * 
	 * @param
	 * @return int
	 */
	protected int getMaxColumnCount() {
		int[] validHeaders = getCellsModel().getValidHeaders();
		if (validHeaders == null || validHeaders.length < 2) {
			throw new IllegalArgumentException(StringResource
					.getStringResource("miufo1004060"));// ��÷ǿ������ʧ��
		}
		return validHeaders[1] + 1;
	}

	protected CellsModel getCellsModel() {
		return m_Report.getCellsModel();
	}

	protected UfoReport getReport() {
		return this.m_Report;
	}

	protected ArrayList<CellPosition> getCellsPositionList() {
		return cellsPositionList;
	}

}
