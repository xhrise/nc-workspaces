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
 * 定位功能的基类
 * 
 * @author 王宇光
 * @version Create on 2008-5-19
 */
public abstract class AbsLocation {
	/** 定位类型：批注 */
	protected final static int POSTIL = 0;
	/** 定位类型：常量 */
	protected final static int CONSTANT = 1;
	/** 定位类型：公式 */
	protected final static int FORMULA = 2;
	/** 定位类型：指标 */
	protected final static int MEASURE = 3;
	/** 定位类型：空值 */
	protected final static int NULL_VALUE = 4;
	/** 定位类型：引用单元格 */
	protected final static int REF_CELL = 5;
	/** 定位类型：最后一个单元格 */
	protected final static int LAST_CELL = 6;
	/** 定位类型：条件格式 */
	protected final static int CONDITION_FORMAT = 7;
	/** 定位类型：数据格式 */
	protected final static int DATA_FORMAT = 8;
	/** 定位类型：数据格式 */
	protected final static int REFERENCE_POSITION = 9;
	/** 表格 */
	private UfoReport m_Report = null;
	/** 保存定位出的结果 */
	private ArrayList<CellPosition> cellsPositionList;

	public AbsLocation(UfoReport rep) {
		if (rep == null) {
			throw new IllegalArgumentException(StringResource
					.getStringResource("miufo1000496"));// 输入参数不允许为空
		}
		this.m_Report = rep;
		cellsPositionList = new ArrayList<CellPosition>();// 保存定位出的结果
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
					.getStringResource("miufo1004058"), getReport(), null);// 未找到单元格
		}

	}

	/**
	 * 定位的条件类型
	 * 
	 * @param
	 * @return int
	 */
	abstract protected int getConditionType();

	/**
	 * 具体的定位类型实现此方法
	 * 
	 * @param
	 * @return int
	 */
	abstract protected void locationImpl(CellPosition cellPosition,
			CellsModel cellsModel);

	/**
	 * 获得非空时的最大区域
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
	 * 获得本表非空最大行数
	 * 
	 * @param
	 * @return int
	 */
	protected int getMaxRowCount() {
		int[] validHeaders = getCellsModel().getValidHeaders();
		if (validHeaders == null || validHeaders.length < 2) {
			throw new IllegalArgumentException(StringResource
					.getStringResource("miufo1004059"));// 获得非空最大行失败
		}
		return validHeaders[0] + 1;
	}

	/**
	 * 获得本表非空最大列数
	 * 
	 * @param
	 * @return int
	 */
	protected int getMaxColumnCount() {
		int[] validHeaders = getCellsModel().getValidHeaders();
		if (validHeaders == null || validHeaders.length < 2) {
			throw new IllegalArgumentException(StringResource
					.getStringResource("miufo1004060"));// 获得非空最大列失败
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
