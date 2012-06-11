package com.ufsoft.iufo.fmtplugin.dataprocess;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formula.FormulaHandler;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.FormulaVO;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.script.exception.CmdException;
import com.ufsoft.script.expression.UfoCmdLet;
import com.ufsoft.script.expression.UfoExpr;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;

abstract public class AbsDataProcessExt extends AbsActionExt {
	
	
	private UfoReport _report;

	AbsDataProcessExt(UfoReport report) {
		_report = report;
	}

	abstract String getMenuName();
	
	public ActionUIDes[] getUIDesArr() {		
		ActionUIDes uiDesMenu = new ActionUIDes();
		uiDesMenu.setPaths(new String[]{MultiLang.getString("data"), StringResource.getStringResource("uiuforelease00028")});
		uiDesMenu.setGroup("dataProcess");
		uiDesMenu.setName(getMenuName());
		ActionUIDes uiDesRightBtn = new ActionUIDes();
		uiDesRightBtn.setName(getMenuName());
		uiDesRightBtn.setPaths(new String[]{StringResource.getStringResource("uiuforelease00028")});
		uiDesRightBtn.setPopup(true);
		return new ActionUIDes[]{uiDesMenu,uiDesRightBtn};
	}
	public Object[] getParams(UfoReport container) {
		return new Object[]{ container }; 
	}

	public boolean isEnabled(Component focusComp) {
		CellsModel cellsModel = getReport().getCellsModel();
		if(cellsModel == null){
			return false;
		}
		CellPosition anchorPos = cellsModel.getSelectModel().getAnchorCell();
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		return dynAreaModel.isInDynArea(anchorPos);
	}
	
	/**
	 * add by wangyga
	 * 判断所选择动态区中是否有数据集函数
	 * @return
	 */
	protected boolean isGetDataFunc(){
		if(getCellsModel() == null){
			return false;
		}
        FormulaModel formulaModel = FormulaModel.getInstance(getCellsModel());
		
		DynAreaCell dynAreaCell = getDynAreaCell();
		if(dynAreaCell == null)
			return false;
		AreaPosition dynArea = dynAreaCell.getArea();
		
		IArea fmlArea = formulaModel.getRelatedFmlArea(dynArea, true);
		if (fmlArea == null) {
			fmlArea = formulaModel.getRelatedFmlArea(dynArea, false);
		}
		if(fmlArea == null)
			return true;
		
		FormulaVO publicCellFormula = formulaModel.getPublicDirectFml(fmlArea);				
		FormulaVO personCellFormula = formulaModel.getPersonalDirectFml(fmlArea);				
		FormulaVO totalFormula = formulaModel.getDirectFml(fmlArea, false);

		if (checkFuncExpr(publicCellFormula) || checkFuncExpr(personCellFormula)
				|| checkFuncExpr(totalFormula))
			return false;
		
		return true;	
	}
	
	/**
	 * add by wangyga
	 * @param fmlVo
	 * @return
	 */
	private boolean checkFuncExpr(FormulaVO fmlVo){
		if(fmlVo == null)
			return false;
		//分析公式的每个细节表达式
		List allExpr = new ArrayList();
		if(fmlVo.getLet() instanceof UfoCmdLet){
			((UfoCmdLet)fmlVo.getLet()).getAllExprsAndNoMeasCord(allExpr);
		} else{
			if(fmlVo.getLet() == null)
				return false;
			fmlVo.getLet().getAllExprs(allExpr);
		}
		
		UfoFmlExecutor ufoFmlExecutor = getFmlExecutor();
		if(ufoFmlExecutor == null)
			return false;
		List<UfoExpr> listAllExpr = FormulaHandler.getCalaElemFromExpr(allExpr, ufoFmlExecutor.getCalcEnv());
		for(UfoExpr expr : listAllExpr){
			try {
				if(expr.isDataSetFuncExpr(null))
					return true;
			} catch (CmdException e) {
                AppDebug.debug(e);
                return false;
			}			
		}
		return false;
	}
	
	private UfoFmlExecutor getFmlExecutor(){
		FormulaModel formulaModel = FormulaModel.getInstance(getCellsModel());
		return formulaModel.getUfoFmlExecutor();
		
//		IPlugIn plugin = getReport().getPluginManager().getPlugin("com.ufsoft.iufo.fmtplugin.formula.FormulaDefPlugin");
//		if(plugin == null)
//			return null;
//		return ((FormulaDefPlugin)plugin).getFmlExecutor();
	}

	/**
	 * 获得选中的动态区
	 * @return
	 */
	private DynAreaCell getDynAreaCell(){
		CellsModel cellsModel = getCellsModel();
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		
		CellPosition anchorCell = cellsModel.getSelectModel().getAnchorCell();
		DynAreaCell dynAreaCell = dynAreaModel.getDynAreaCellByPos(anchorCell);
		return dynAreaCell;
	}
	
	private CellsModel getCellsModel(){
		return getReport().getCellsModel();
	}
	
	UfoReport getReport(){
		return _report;
	}
}
