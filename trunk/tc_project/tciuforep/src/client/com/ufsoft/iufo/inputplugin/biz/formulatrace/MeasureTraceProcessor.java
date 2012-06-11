package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import nc.vo.iufo.data.MeasureTraceVO;

import com.ufida.dataset.Context;
import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iuforeport.reporttool.temp.KeyDataGroup;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.script.CmdProxy;
import com.ufsoft.script.UfoFormulaProxy;
import com.ufsoft.script.base.CommonExprCalcEnv;
import com.ufsoft.script.base.UfoVal;
import com.ufsoft.script.expression.UfoExpr;
import com.ufsoft.script.extfunc.MeasFunc;
import com.ufsoft.script.extfunc.MeasFuncDriver;
import com.ufsoft.script.spreadsheet.ReportDynCalcEnv;
import com.ufsoft.script.spreadsheet.UfoCalcEnv;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.iufo.resource.StringResource;

public class MeasureTraceProcessor {

	/**
	 * @i18n miufo01186=表达式为空。请确认数据源配置或函数驱动设置。
	 * @i18n miufo00596=预批量计算错误
	 */
	public MeasureTraceVO[] trace(IContext contextVO,CellsModel cellsModel, IFormulaParsedDataItem formulaParsedDataItem,CellPosition cell) {
		UfoFmlExecutor ufoFmlExecutor = FormulaTraceBizHelper.getFormulaHandler((Context)contextVO, cellsModel);
		ReportDynCalcEnv dynCalcEnv = ufoFmlExecutor.getCalcEnv();
		UfoCalcEnv env = ufoFmlExecutor.getCalcEnv();
		
		//如果公式在动态区中，则设置动态区计算环境
		int unitAreaNum = -1, stepRow = -1, stepCol = -1;
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);		
		boolean isInDynArea = dynAreaModel.isInDynArea(cell);
		if(isInDynArea){
			DynAreaCell dynAreaCell = dynAreaModel.getDynAreaCellByPos(cell);
			stepRow = dynAreaCell.getDynAreaVO().isRowDirection() ? 
							 dynAreaCell.getDynAreaVO().getOriArea().getHeigth() : 0;
	        stepCol = dynAreaCell.getDynAreaVO().isRowDirection() ?
	                		 0 : dynAreaCell.getDynAreaVO().getOriArea().getWidth();
	        unitAreaNum = dynAreaCell.getOwnerUnitAreaNum(cell);
	        //设置分组计算环境参数
	        FormulaTraceBiz.setDynAreaCalcEnv(ufoFmlExecutor, dynAreaCell, stepRow, stepCol, unitAreaNum);
	        KeyDataGroup[] objKeydatas = ufoFmlExecutor.getAllDynKeyDataGroups(dynAreaCell.getDynAreaPK());
	        if(objKeydatas == null || objKeydatas.length == 0){
	        	return null;
	        }
	        //将动态区域各行关键字值存储在计算环境中
	        ufoFmlExecutor.getCalcEnv().setDynAllKeyDatas(objKeydatas);
		} else{
			dynCalcEnv.setDynAreaInfo(null, null);
		}
		
		UfoExpr expr  = formulaParsedDataItem.getTracedExpr();
		
		if(expr == null){
			throw new RuntimeException(StringResource.getStringResource("miufo01186"));
		}

		MeasureTraceVO[] tracevos = new MeasureTraceVO[1];
		env.setExEnv(CommonExprCalcEnv.EX_CALCEXFUNC,
					CommonExprCalcEnv.EX_VALUE_ON);
		try{
			//设置标识，此计算环境为指标公式追踪
			env.setMeasureTraceVOs(tracevos);
			try{
				CmdProxy.preCalcExtFuncExpr(expr, ufoFmlExecutor.getCalcEnv(), 3);
			} catch(Exception e){
				AppDebug.debug(StringResource.getStringResource("miufo00596"), e);
			}
			tracevos = env.getMeasureTraceVOs();

			//对于mselect、msum等引用本表指标的情况，preCalcExtFunc不会执行后台sql过程，因此要调度真实计算过程。liuyy
			//对于MSELECTA函数，如果参数引用它表，则重新计算追踪
			
			//对于动态区mselects，preCalcExtFunc的tracevos为空，需要走真实计算调度。
			if(isMSelectAFunc(expr, env) || (tracevos != null && tracevos.length < 1)){
				UfoVal[] vals = expr.getValue(env);
				if(vals == null || vals.length < 1){
					return null;
				}
				tracevos = env.getMeasureTraceVOs();
			}
//
//			
//			try{
//	            CmdProxy.preCalcExtFuncExpr(expr, env, 3);
//	        } catch(Exception e){
//	        	AppDebug.debug("预批量计算错误",e);
//	        }
	        
//			tracevos = env.getMeasureTraceVOs();
//			if(tracevos.length < 1){
//		        //对公式子项执行计算
//
//	            TableInputTransObj inputTransObj = ((TableInputContextVO)contextVO).getInputTransObj();
//				UfoContextVO context = FormulaTraceUtil.getContextVO(inputTransObj); 
//				//批量计算所有与区域无关的外部函数值
//				ReportCalcSrv reportCalcSrv = new ReportCalcSrv(context, cellsModel);
//				String strOld=(String) env.getExEnv(CommonExprCalcEnv.EX_CALCEXFUNC);
//				env.setExEnv(CommonExprCalcEnv.EX_CALCEXFUNC, CommonExprCalcEnv.EX_VALUE_ON);
//				UfoVal[] values = reportCalcSrv.calcFormula(formulaParsedDataItem);
//				reportCalcSrv.getFormHandler().getCalcEnv().setExEnv(CommonExprCalcEnv.EX_CALCEXFUNC,strOld);
//				
//				tracevos = env.getMeasureTraceVOs();
//			}
				 
		 }catch(Throwable e){
		  	AppDebug.debug(e);
		  	throw new RuntimeException(e.getMessage());
				 
		 } finally {
			 UfoFormulaProxy.clearPreCalcValues(new UfoExpr[]{expr},env);
			 
			 FormulaTraceBiz.clearDynAreaCalcEnv(ufoFmlExecutor);
		 }
 
		
		return tracevos;
	}
	
	/**
	 * MSELECTA函数，如果参数引用它表，则重新计算追踪
	 * @return
	 */
	 boolean isMSelectAFunc(UfoExpr expr, UfoCalcEnv env){
		Object objFunc = expr.getElementObjByIndex(0);
		if(objFunc != null && objFunc instanceof MeasFunc){
			MeasFunc mFunc = (MeasFunc)objFunc;
			String funcName = mFunc.getFuncName();
			boolean isInEnvRep = mFunc.isInEnvRep(env);
			if(funcName.equalsIgnoreCase(MeasFuncDriver.MSELECTA) && !isInEnvRep)
				return true;
		}
		return false;
		
	}
	
	public static String getMeasFuncName(UfoExpr expr){
		Object objFunc = expr.getElementObjByIndex(0);
		if(objFunc != null && objFunc instanceof MeasFunc){
			MeasFunc mFunc = (MeasFunc)objFunc;
			String funcName = mFunc.getFuncName();
			return funcName;
		}
		return null;
		
	}
	
}
 