package com.ufsoft.iufo.fmtplugin.location;

import java.util.ArrayList;
import java.util.List;

import com.ufsoft.iufo.fmtplugin.formula.FormulaDefPlugin;
import com.ufsoft.iufo.fmtplugin.formula.FormulaHandler;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.FormulaVO;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceBiz;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.script.expression.UfoCmdLet;
import com.ufsoft.script.expression.UfoExpr;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;

/**
 * <pre>
 * </pre>
 * 
 * 定位：引用单元格
 * 
 * @author 王宇光
 * @version Create on 2008-5-20
 */
public class PrecedentsLocation extends AbsLocation {

	public PrecedentsLocation(UfoReport rep) {
		super(rep);
	}

	protected void location() {
		CellsModel cellsModel = getCellsModel();
		AreaPosition area = cellsModel.getSelectModel().getSelectedArea();
		ArrayList<CellPosition> cellPosList = cellsModel
				.getSeperateCellPos(area);		
		if (cellPosList == null || cellPosList.size() == 0) {
			return;
		}
		int iSize = cellPosList.size();
		for (int i = 0; i < iSize; i++) {
			CellPosition cellPosition = cellPosList.get(i);
			locationImpl(cellPosition, cellsModel);
		}
		ArrayList<CellPosition> cellsPositionList = getCellsPositionList();
		if (cellsPositionList != null && cellsPositionList.size() > 0) {
			AreaPosition areaPos = AreaPosition.getInstance(cellsPositionList
					.get(0), cellsPositionList.get(0));
			cellsModel.getSelectModel().setSelectedArea(areaPos);
		}else{
			UfoPublic.sendErrorMessage(StringResource.getStringResource("miufo1004058"), getReport(), null);// 未找到单元格
		}

	}

	@Override
	protected int getConditionType() {
		// TODO Auto-generated method stub
		return AbsLocation.REF_CELL;
	}

	@Override
	protected void locationImpl(CellPosition cellPosition, CellsModel cellsModel) {
		if (cellPosition == null || cellsModel == null) {
			throw new IllegalArgumentException(StringResource.getStringResource("miufo1000496"));//输入参数不允许为空
		}
		FormulaModel formulaModel = getFormulaModel(cellsModel);
		// 获得单元公式
		FormulaVO cellFormulaVo = formulaModel.getDirectFml(cellPosition, true);
		// 获得汇总公式
		FormulaVO totalFormulaVo = formulaModel.getDirectFml(cellPosition,
				false);
		
		UfoFmlExecutor formulaHandler = formulaModel.getUfoFmlExecutor();
		if (cellFormulaVo != null) {
			parseFormulaToPosition(cellFormulaVo, formulaHandler);
		}
		if (totalFormulaVo != null) {
			parseFormulaToPosition(totalFormulaVo, formulaHandler);
		}
	}

	/**
	 * 把公式转化为对应的位置:cellPosition
	 * 
	 * @param FormulaVO
	 *            formulaVo,FormulaHandler formulaHandler
	 * 
	 * @return
	 */
	private void parseFormulaToPosition(FormulaVO formulaVo,
			UfoFmlExecutor formulaHandler) {
		if (formulaVo == null || formulaHandler == null) {
			return;
		}
		List allExpr = new ArrayList();
		if (formulaVo.getLet() instanceof UfoCmdLet) {
			((UfoCmdLet) formulaVo.getLet()).getAllExprsAndNoMeasCord(allExpr);
		} else {
			formulaVo.getLet().getAllExprs(allExpr);
		}
		List<UfoExpr> listAllExpr = FormulaHandler.getCalaElemFromExpr(allExpr,
				formulaHandler.getCalcEnv());//细节表达式
		FormulaTraceBiz formulaTraceBiz = (FormulaTraceBiz) FormulaTraceBiz
				.getInstance();
		if (listAllExpr == null || listAllExpr.size() == 0) {
			return;
		}
		CellsModel cellsModel = getCellsModel();
		int iSize = listAllExpr.size();
		for (int index = 0; index < iSize; index++) {
			UfoExpr expr = listAllExpr.get(index);
			IArea[] aryCellPos = formulaTraceBiz.getTracedPos(expr,
					formulaHandler.getCalcEnv(), cellsModel);			
			if (aryCellPos == null) {
				continue;
			}
			int iAryCellLength = aryCellPos.length;
			for (int i = 0; i < iAryCellLength; i++) {
				IArea iarea = aryCellPos[i];
				if (iarea == null) {
					continue;
				}
				AreaPosition areapos = AreaPosition.getInstance(iarea
						.getStart(), iarea.getEnd());
				ArrayList<CellPosition> cellPosList = cellsModel
						.getSeperateCellPos(areapos);
				getCellsPositionList().addAll(cellPosList);
			}

		}
	}

	/**
	 * 获得公式插件
	 * 
	 * @param
	 * @return FormulaDefPlugin
	 */
	private FormulaDefPlugin getFormulaPI() {
		return (FormulaDefPlugin) getReport().getPluginManager().getPlugin(
				FormulaDefPlugin.class.getName());
	}

}
