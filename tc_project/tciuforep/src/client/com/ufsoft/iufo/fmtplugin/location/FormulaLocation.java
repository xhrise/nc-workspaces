package com.ufsoft.iufo.fmtplugin.location;

import java.util.ArrayList;
import java.util.List;

import com.ufsoft.iufo.fmtplugin.formula.FormulaDefPlugin;
import com.ufsoft.iufo.fmtplugin.formula.FormulaHandler;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.FormulaVO;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.script.expression.UfoCmdLet;
import com.ufsoft.script.expression.UfoExpr;
import com.ufsoft.script.extfunc.MeasFunc;
import com.ufsoft.script.extfunc.MeasPercentFunc;
import com.ufsoft.script.function.UfoFunc;
import com.ufsoft.script.function.UfoFuncList;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
/**
 * <pre>
 * </pre>  ��ʽ��λ������ʽ��ָ�깫ʽ�����ܹ�ʽ��
 * @author �����
 * @version 
 * Create on 2008-5-21
 */
public class FormulaLocation extends AbsLocation {
	/** ��λ����ʽ */
	protected final static int AREA_FORMULA = 1;
	/** ��λָ�깫ʽ */
	protected final static int MEASURE_FORMULA = 2;
	/** ��λ���ܹ�ʽ */
	protected final static int TOTAL_FORMULA = 3;
	/** ��ʽ���ͣ���λ�Ķ���ѡ�� */
	private int formulaType = 0;

	public FormulaLocation(UfoReport rep, int formulaType) {
		super(rep);
		this.formulaType = formulaType;
	}

	@Override
	protected int getConditionType() {
		return AbsLocation.FORMULA;
	}

	@Override
	protected void locationImpl(CellPosition cellPosition,CellsModel cellsModel) {
		if (cellPosition == null || cellsModel == null)
			throw new IllegalArgumentException(StringResource.getStringResource("miufo1000496"));//�������������Ϊ��
		FormulaModel formulaModel = getFormulaModel(cellsModel);
		Cell cell = cellsModel.getCell(cellPosition);
		if (cell == null) {
			return;
		}
		FormulaVO cellFormulaVo = formulaModel
				.getDirectFml(cellPosition, true);
		FormulaVO totalFormulaVo = formulaModel
				.getDirectFml(cellPosition, false);
		if (getFormulaType() == TOTAL_FORMULA && totalFormulaVo != null) {			
			getCellsPositionList().add(cellPosition);
			return;
		}
		if (cellFormulaVo == null) {
			return;
		}
		if(isFuncByType(cellFormulaVo,getFormulaType())){
			getCellsPositionList().add(cellPosition);
			return;
		}

	}

	/**
	 * �������ͣ��ж��Ƿ�ʱ��Ӧ�ĺ���
	 * @param FormulaVO formulaVo,int formulaType
	 * @return boolean
	 */
	private boolean isFuncByType(FormulaVO formulaVo,int formulaType) {
		List allExpr = new ArrayList();
		FormulaModel formulaModel = FormulaModel.getInstance(getCellsModel());
		
		UfoFmlExecutor formulaHandler = formulaModel.getUfoFmlExecutor();
		if (formulaVo.getLet() instanceof UfoCmdLet) {
			((UfoCmdLet) formulaVo.getLet()).getAllExprsAndNoMeasCord(allExpr);
		} else {
			formulaVo.getLet().getAllExprs(allExpr);
		}
		List<UfoExpr> listAllExpr = FormulaHandler.getCalaElemFromExpr(allExpr,
				formulaHandler.getCalcEnv());
		if(listAllExpr == null){
			return false;
		}
		int iSize = listAllExpr.size();
		for (int index = 0; index < iSize; index++) {
			UfoExpr expr = listAllExpr.get(index);//���ÿһ����ʽ����
			Object objFunc = expr.getElementObjByIndex(0);
			if(formulaType == MEASURE_FORMULA && isMeasFunc(objFunc)){
				return true;
			}else if(formulaType == AREA_FORMULA && isAreaFunc(objFunc)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ��鹫ʽ�����Ƿ�ָ�꺯��
	 * @param Object objFunc
	 * @return boolean
	 */
	private boolean isMeasFunc(Object objFunc){
		if (objFunc != null
				&& (objFunc instanceof MeasFunc || objFunc instanceof MeasPercentFunc)) {
			return true;
		}
		return false;
	}
	
	/**
	 * ��鹫ʽ�����Ƿ�����ʽ
	 * @param Object objFunc
	 * @return boolean
	 */
	private boolean isAreaFunc(Object objFunc){
		if (objFunc != null && objFunc instanceof UfoFunc) {
			UfoFunc ufoFunc = (UfoFunc) objFunc;
			short funcId = ufoFunc.getFid();
			if (funcId == UfoFuncList.FPAVG
					|| funcId == UfoFuncList.FPCOUNT
					|| funcId == UfoFuncList.FPMAX
					|| funcId == UfoFuncList.FPMIN
					|| funcId == UfoFuncList.FPSTD
					|| funcId == UfoFuncList.FPTOTAL
					|| funcId == UfoFuncList.FPVAR) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ��ù�ʽ���
	 * 
	 * @param 
	 * @return FormulaDefPlugin
	 */
	private FormulaDefPlugin getFormulaPI() {
		return (FormulaDefPlugin) getReport().getPluginManager().getPlugin(
				FormulaDefPlugin.class.getName());
	}

	private int getFormulaType() {
		return this.formulaType;
	}

}
